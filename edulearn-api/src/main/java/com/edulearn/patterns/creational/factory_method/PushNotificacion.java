package com.edulearn.patterns.creational.factory_method;

import java.util.logging.Logger;

/**
 * Implementación concreta: Notificación Push
 */
public class PushNotificacion implements INotificacion {

    private static final Logger logger = Logger.getLogger(PushNotificacion.class.getName());

    @Override
    public boolean enviar(String destinatario, String asunto, String mensaje) {
        if (!validarDestinatario(destinatario)) {
            logger.warning("Device token inválido: " + destinatario);
            return false;
        }

        // Simulación de envío de notificación push
        logger.info("========== PUSH ENVIADO ==========");
        logger.info("Device Token: " + destinatario);
        logger.info("Título: " + asunto);
        logger.info("Mensaje: " + mensaje);
        logger.info("==================================");

        // En producción, aquí se integraría con servicio real:
        // - Firebase Cloud Messaging (FCM)
        // - Apple Push Notification Service (APNS)
        // - OneSignal
        // - etc.

        return true;
    }

    @Override
    public String getTipo() {
        return "PUSH";
    }

    @Override
    public boolean validarDestinatario(String destinatario) {
        // Validación básica de device token (normalmente es un string largo)
        return destinatario != null && destinatario.length() > 20;
    }
}
