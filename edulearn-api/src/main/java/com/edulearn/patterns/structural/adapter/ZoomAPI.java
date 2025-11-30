package com.edulearn.patterns.structural.adapter;

/**
 * Servicio externo de Zoom (Adaptee).
 * Representa la API real de Zoom con su propia interfaz incompatible.
 */
public class ZoomAPI {
    private String apiKey;
    private String apiSecret;

    public ZoomAPI(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    /**
     * Método específico de Zoom para crear meetings
     */
    public String createZoomMeeting(String topic, int duration) {
        // Simulación de llamada a API de Zoom
        String meetingId = "zoom_" + System.currentTimeMillis();
        return "https://zoom.us/j/" + meetingId + "?pwd=" + generatePassword();
    }

    /**
     * Método específico de Zoom para obtener meeting info
     */
    public String getMeetingLink(String meetingId) {
        return "https://zoom.us/j/" + meetingId;
    }

    /**
     * Método específico de Zoom para terminar meeting
     */
    public boolean endMeeting(String meetingId) {
        // Simulación de finalización
        return true;
    }

    /**
     * Verificar credenciales de Zoom
     */
    public boolean validateCredentials() {
        return apiKey != null && !apiKey.isEmpty() && apiSecret != null && !apiSecret.isEmpty();
    }

    private String generatePassword() {
        return Long.toHexString(System.currentTimeMillis()).substring(0, 8);
    }
}
