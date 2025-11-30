package com.edulearn.patterns.creational.factory_method;

/**
 * Interfaz para diferentes tipos de notificaciones
 */
public interface INotificacion {

    /**
     * Enviar notificación
     */
    boolean enviar(String destinatario, String asunto, String mensaje);

    /**
     * Obtener tipo de notificación
     */
    String getTipo();

    /**
     * Validar destinatario
     */
    boolean validarDestinatario(String destinatario);
}
