package adapter;

/**
 * PATRÓN ADAPTER - Adaptee (Clase externa incompatible)
 * Simula la API real de Zoom con sus métodos específicos
 * Esta clase representa una librería externa que no podemos modificar
 */
public class ZoomAPI {
    private String meetingId;
    private String joinUrl;
    private String hostKey;

    // Método específico de Zoom para crear meeting
    public String createMeeting(String topic, int duration, String timezone) {
        // Simulación de llamada a API de Zoom
        this.meetingId = "zoom_" + System.currentTimeMillis();
        this.joinUrl = "https://zoom.us/j/" + meetingId;
        this.hostKey = "host_" + (int)(Math.random() * 10000);

        System.out.println("[ZoomAPI] Meeting creado: " + meetingId);
        return meetingId;
    }

    // Método específico de Zoom para obtener join URL
    public String getJoinUrl() {
        return this.joinUrl;
    }

    // Método específico de Zoom para iniciar
    public void startMeeting(String meetingId, String hostKey) {
        System.out.println("[ZoomAPI] Iniciando meeting " + meetingId + " con host key " + hostKey);
    }

    // Método específico de Zoom para finalizar
    public void endMeeting(String meetingId) {
        System.out.println("[ZoomAPI] Finalizando meeting " + meetingId);
    }

    public String getMeetingId() {
        return meetingId;
    }

    public String getHostKey() {
        return hostKey;
    }
}
