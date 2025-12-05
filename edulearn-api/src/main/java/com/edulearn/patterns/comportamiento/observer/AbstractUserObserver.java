package com.edulearn.patterns.comportamiento.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

/**
 * PATRÓN OBSERVER - Observer Abstracto Base
 * =========================================
 * Clase abstracta que proporciona funcionalidad común para todos los observers.
 * Implementa la lógica base de suscripción y filtrado de eventos.
 *
 * Los observers concretos solo necesitan:
 * - Definir sus eventos de interés en el constructor
 * - Implementar lógica específica en processNotification() si es necesario
 */
public abstract class AbstractUserObserver implements Observer {

    private static final Logger logger = LoggerFactory.getLogger(AbstractUserObserver.class);

    protected final Integer userId;
    protected final String userName;
    protected final String userRole;
    protected final Set<String> interestedEvents;

    public AbstractUserObserver(Integer userId, String userName, String userRole) {
        this.userId = userId;
        this.userName = userName;
        this.userRole = userRole;
        this.interestedEvents = new HashSet<>();
    }

    @Override
    public void update(NotificationEvent event) {
        logger.info("[{}] {} ({}) recibió: {} - {}",
            userRole.toUpperCase(),
            userName,
            userId,
            event.getTitle(),
            event.getMessage());

        // Hook para procesamiento adicional por observers específicos
        processNotification(event);
    }

    @Override
    public Integer getObserverId() {
        return userId;
    }

    @Override
    public boolean isInterestedIn(String eventType) {
        return interestedEvents.contains(eventType);
    }

    /**
     * Hook para que observers concretos implementen lógica adicional
     * Por ejemplo: enviar email, push notification, actualizar UI, etc.
     */
    protected void processNotification(NotificationEvent event) {
        // Implementación por defecto vacía
        // Los observers concretos pueden sobrescribir este método
    }

    /**
     * Suscribirse a un evento adicional
     */
    public void subscribeToEvent(String eventType) {
        interestedEvents.add(eventType);
        logger.debug("Observer {} suscrito a evento: {}", userId, eventType);
    }

    /**
     * Desuscribirse de un evento
     */
    public void unsubscribeFromEvent(String eventType) {
        interestedEvents.remove(eventType);
        logger.debug("Observer {} desuscrito de evento: {}", userId, eventType);
    }

    /**
     * Obtener todos los eventos suscritos
     */
    public Set<String> getSubscribedEvents() {
        return new HashSet<>(interestedEvents);
    }

    // Getters
    public String getUserName() {
        return userName;
    }

    public String getUserRole() {
        return userRole;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        // CRÍTICO: Solo comparar por userId, NO por clase
        // Un usuario solo debe tener UN observer, independiente del rol
        if (!(obj instanceof AbstractUserObserver)) return false;
        AbstractUserObserver that = (AbstractUserObserver) obj;
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}
