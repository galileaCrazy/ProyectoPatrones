package com.edulearn.patterns.behavioral.observer;

/**
 * PATRÓN OBSERVER - Interfaz Observer
 * ===================================
 * Define la interfaz que deben implementar los observadores
 * que quieran recibir notificaciones de cambios en el Subject.
 *
 * En EduLearn: Los usuarios actúan como observadores que reciben
 * notificaciones sobre cambios en cursos y tareas.
 */
public interface Observer {

    /**
     * Método que será llamado cuando ocurra un evento
     * @param event Evento que contiene la información de la notificación
     */
    void update(NotificationEvent event);

    /**
     * Obtener el ID del observador
     * @return ID único del observador
     */
    Integer getObserverId();

    /**
     * Verificar si el observador está interesado en este tipo de evento
     * @param eventType Tipo de evento
     * @return true si está interesado, false en caso contrario
     */
    boolean isInterestedIn(String eventType);
}
