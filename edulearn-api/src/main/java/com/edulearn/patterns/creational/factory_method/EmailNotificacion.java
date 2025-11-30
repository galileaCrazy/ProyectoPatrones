package com.edulearn.patterns.creational.factory_method;

import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Implementación concreta: Notificación por Email
 */
public class EmailNotificacion implements INotificacion {

    private static final Logger logger = Logger.getLogger(EmailNotificacion.class.getName());
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    @Override
    public boolean enviar(String destinatario, String asunto, String mensaje) {
        if (!validarDestinatario(destinatario)) {
            logger.warning("Email inválido: " + destinatario);
            return false;
        }

        // Simulación de envío de email
        logger.info("========== EMAIL ENVIADO ==========");
        logger.info("Para: " + destinatario);
        logger.info("Asunto: " + asunto);
        logger.info("Mensaje: " + mensaje);
        logger.info("===================================");

        // En producción, aquí se integraría con servicio real:
        // - SendGrid
        // - Amazon SES
        // - Gmail API
        // - etc.

        return true;
    }

    @Override
    public String getTipo() {
        return "EMAIL";
    }

    @Override
    public boolean validarDestinatario(String destinatario) {
        return destinatario != null && EMAIL_PATTERN.matcher(destinatario).matches();
    }
}
