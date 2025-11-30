package com.edulearn.patterns.creational.factory_method;

import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Implementación concreta: Notificación por SMS
 */
public class SMSNotificacion implements INotificacion {

    private static final Logger logger = Logger.getLogger(SMSNotificacion.class.getName());
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");

    @Override
    public boolean enviar(String destinatario, String asunto, String mensaje) {
        if (!validarDestinatario(destinatario)) {
            logger.warning("Número de teléfono inválido: " + destinatario);
            return false;
        }

        // Simulación de envío de SMS
        logger.info("========== SMS ENVIADO ==========");
        logger.info("Para: " + destinatario);
        logger.info("Mensaje: " + mensaje);
        logger.info("(Asunto ignorado en SMS)");
        logger.info("=================================");

        // En producción, aquí se integraría con servicio real:
        // - Twilio
        // - Amazon SNS
        // - Nexmo
        // - etc.

        return true;
    }

    @Override
    public String getTipo() {
        return "SMS";
    }

    @Override
    public boolean validarDestinatario(String destinatario) {
        return destinatario != null && PHONE_PATTERN.matcher(destinatario).matches();
    }
}
