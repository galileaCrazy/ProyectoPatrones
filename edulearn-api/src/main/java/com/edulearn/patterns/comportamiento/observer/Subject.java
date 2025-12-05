package com.edulearn.patterns.comportamiento.observer;

/**
 * PATRÓN OBSERVER - Interfaz Subject
 * ==================================
 * Define la interfaz para el objeto que mantiene una lista de observadores
 * y los notifica de cambios.
 *
 * En EduLearn: El sistema de notificaciones actúa como Subject,
 * manteniendo una lista de usuarios suscritos a diferentes eventos.
 */
public interface Subject {

    /**
     * Agregar un observador a la lista de suscritos
     * @param observer Observador a agregar
     */
    void attach(Observer observer);

    /**
     * Remover un observador de la lista de suscritos
     * @param observer Observador a remover
     */
    void detach(Observer observer);

    /**
     * Notificar a todos los observadores interesados sobre un evento
     * @param event Evento a notificar
     */
    void notifyObservers(NotificationEvent event);
}
