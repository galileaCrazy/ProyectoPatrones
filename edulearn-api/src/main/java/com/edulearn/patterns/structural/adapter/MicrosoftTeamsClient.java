package com.edulearn.patterns.structural.adapter;

/**
 * Cliente externo de Microsoft Teams (Adaptee).
 * Representa la API real de MS Teams con su propia interfaz incompatible.
 */
public class MicrosoftTeamsClient {
    private String tenantId;
    private String clientSecret;

    public MicrosoftTeamsClient(String tenantId, String clientSecret) {
        this.tenantId = tenantId;
        this.clientSecret = clientSecret;
    }

    /**
     * Método específico de MS Teams para iniciar sesión online
     */
    public String startOnlineSession(String title, int durationInMinutes) {
        // Simulación de llamada a API de MS Teams
        String sessionId = "teams_session_" + System.currentTimeMillis();
        return "https://teams.microsoft.com/l/meetup-join/" + sessionId;
    }

    /**
     * Método específico de MS Teams para obtener join link
     */
    public String getJoinLink(String sessionId) {
        return "https://teams.microsoft.com/l/meetup-join/" + sessionId;
    }

    /**
     * Método específico de MS Teams para terminar sesión
     */
    public boolean terminateSession(String sessionId) {
        // Simulación de finalización
        return true;
    }

    /**
     * Verificar autenticación de Teams
     */
    public boolean authenticate() {
        return tenantId != null && !tenantId.isEmpty() &&
               clientSecret != null && !clientSecret.isEmpty();
    }
}
