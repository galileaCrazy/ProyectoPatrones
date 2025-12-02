package com.edulearn.patterns.estructural.adapter;

/**
 * Servicio externo de Google Meet (Adaptee).
 * Representa la API real de Google Meet con su propia interfaz incompatible.
 */
public class GoogleMeetService {
    private String apiKey;

    public GoogleMeetService(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Método específico de Google Meet para generar conferencias
     */
    public String generateMeetingCode(String eventName) {
        // Simulación de llamada a API de Google Meet
        String code = generateCode();
        return "https://meet.google.com/" + code;
    }

    /**
     * Método específico de Google Meet para obtener URL
     */
    public String getConferenceUrl(String code) {
        return "https://meet.google.com/" + code;
    }

    /**
     * Método específico de Google Meet para cerrar conferencia
     */
    public void closeConference(String code) {
        // Simulación de cierre
    }

    /**
     * Verificar API key de Google
     */
    public boolean isApiKeyValid() {
        return apiKey != null && !apiKey.isEmpty();
    }

    private String generateCode() {
        // Generar código de 10 caracteres tipo "abc-defg-hij"
        String chars = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            if (i == 3 || i == 7) {
                code.append("-");
            } else {
                code.append(chars.charAt((int) (Math.random() * chars.length())));
            }
        }
        return code.toString();
    }
}
