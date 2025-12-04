package com.edulearn.patterns.behavioral.observer;

import com.edulearn.model.Notificacion;
import com.edulearn.repository.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * PATRÓN OBSERVER - Subject Concreto
 * ==================================
 * Implementación concreta del Subject que gestiona la lista de observadores
 * y los notifica de eventos en el sistema.
 *
 * Características:
 * - Thread-safe usando CopyOnWriteArrayList
 * - Persiste notificaciones en base de datos
 * - Filtra observadores según su interés en el evento
 * - Gestión automática de suscripciones por curso
 */
@Component
public class NotificationSubject implements Subject {

    private static final Logger logger = LoggerFactory.getLogger(NotificationSubject.class);

    private final List<Observer> observers = new CopyOnWriteArrayList<>();

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Override
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
            logger.info("Observer {} registrado. Total observadores: {}",
                observer.getObserverId(), observers.size());
        }
    }

    @Override
    public void detach(Observer observer) {
        if (observers.remove(observer)) {
            logger.info("Observer {} removido. Total observadores: {}",
                observer.getObserverId(), observers.size());
        }
    }

    @Override
    public void notifyObservers(NotificationEvent event) {
        logger.info("Notificando evento: {} a {} observadores",
            event.getEventType(), observers.size());

        int notificationsCreated = 0;

        for (Observer observer : observers) {
            try {
                // Solo notificar si el observador está interesado en este tipo de evento
                if (observer.isInterestedIn(event.getEventType())) {
                    observer.update(event);

                    // Persistir notificación en base de datos
                    saveNotificationToDatabase(observer, event);
                    notificationsCreated++;
                }
            } catch (Exception e) {
                logger.error("Error al notificar observer {}: {}",
                    observer.getObserverId(), e.getMessage());
            }
        }

        logger.info("Notificaciones creadas: {}", notificationsCreated);
    }

    /**
     * Notificar a un observador específico
     */
    public void notifySpecificObserver(Integer observerId, NotificationEvent event) {
        observers.stream()
            .filter(obs -> obs.getObserverId().equals(observerId))
            .forEach(obs -> {
                try {
                    obs.update(event);
                    saveNotificationToDatabase(obs, event);
                    logger.info("Notificación enviada a observer específico: {}", observerId);
                } catch (Exception e) {
                    logger.error("Error al notificar observer {}: {}", observerId, e.getMessage());
                }
            });
    }

    /**
     * Guardar notificación en base de datos
     */
    private void saveNotificationToDatabase(Observer observer, NotificationEvent event) {
        try {
            Notificacion notificacion = new Notificacion();
            notificacion.setTipo("INTERNA");
            notificacion.setDestinatario(observer.getObserverId().toString());
            notificacion.setAsunto(event.getTitle());
            notificacion.setMensaje(event.getMessage());
            notificacion.setEstado("NO_LEIDA");
            notificacion.setFechaCreacion(event.getTimestamp());

            notificacionRepository.save(notificacion);
        } catch (Exception e) {
            logger.error("Error al guardar notificación en BD: {}", e.getMessage());
        }
    }

    /**
     * Obtener número de observadores registrados
     */
    public int getObserverCount() {
        return observers.size();
    }

    /**
     * Obtener lista de IDs de observadores
     */
    public List<Integer> getObserverIds() {
        return observers.stream()
            .map(Observer::getObserverId)
            .toList();
    }

    /**
     * Verificar si un observador está registrado
     */
    public boolean isObserverRegistered(Integer observerId) {
        return observers.stream()
            .anyMatch(obs -> obs.getObserverId().equals(observerId));
    }

    /**
     * Remover todos los observadores
     */
    public void clearObservers() {
        observers.clear();
        logger.info("Todos los observadores removidos");
    }
}
