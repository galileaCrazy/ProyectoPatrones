package com.edulearn.patterns.creational.factory_method;

/**
 * PATRÓN FACTORY METHOD
 * =====================
 * Propósito: Define una interfaz para crear objetos, pero permite a las subclases
 * decidir qué clase instanciar. Factory Method permite que una clase delegue
 * la responsabilidad de instanciación a las subclases.
 *
 * Uso en EduLearn: Crear diferentes tipos de notificaciones (Email, SMS, Push)
 * sin que el código cliente necesite conocer las clases concretas.
 *
 * Ventajas:
 * - Evita acoplamiento entre el código cliente y las clases concretas
 * - Facilita agregar nuevos tipos de notificaciones
 * - Cumple con el principio Open/Closed (abierto para extensión, cerrado para modificación)
 * - Centraliza la lógica de creación
 */
public abstract class NotificacionFactory {

    /**
     * Factory Method - Debe ser implementado por subclases
     */
    public abstract INotificacion crearNotificacion();

    /**
     * Template Method - Usa el Factory Method
     * Este método define el algoritmo de envío utilizando el objeto creado
     */
    public boolean enviarNotificacion(String destinatario, String asunto, String mensaje) {
        // Crear la notificación específica usando el factory method
        INotificacion notificacion = crearNotificacion();

        // Validar destinatario
        if (!notificacion.validarDestinatario(destinatario)) {
            return false;
        }

        // Enviar notificación
        return notificacion.enviar(destinatario, asunto, mensaje);
    }

    /**
     * Obtener información sobre el tipo de notificación
     */
    public String getTipoNotificacion() {
        return crearNotificacion().getTipo();
    }

    /**
     * Factory Method estático para crear factory según tipo
     */
    public static NotificacionFactory getFactory(String tipo) {
        switch (tipo.toUpperCase()) {
            case "EMAIL":
                return new EmailNotificacionFactory();
            case "SMS":
                return new SMSNotificacionFactory();
            case "PUSH":
                return new PushNotificacionFactory();
            default:
                throw new IllegalArgumentException("Tipo de notificación no soportado: " + tipo);
        }
    }
}

/**
 * Factory concreta para Email
 */
class EmailNotificacionFactory extends NotificacionFactory {
    @Override
    public INotificacion crearNotificacion() {
        return new EmailNotificacion();
    }
}

/**
 * Factory concreta para SMS
 */
class SMSNotificacionFactory extends NotificacionFactory {
    @Override
    public INotificacion crearNotificacion() {
        return new SMSNotificacion();
    }
}

/**
 * Factory concreta para Push
 */
class PushNotificacionFactory extends NotificacionFactory {
    @Override
    public INotificacion crearNotificacion() {
        return new PushNotificacion();
    }
}
