package adapter;

/**
 * PATRÓN ADAPTER - Adaptee (Otra clase externa incompatible)
 * Simula la API real de Google Meet con sus métodos específicos
 */
public class GoogleMeetAPI {
    private String eventId;
    private String meetLink;

    // Método específico de Google para crear evento con Meet
    public String scheduleEvent(String summary, int durationMinutes, String calendarId) {
        // Simulación de llamada a Google Calendar API
        this.eventId = "gcal_" + System.currentTimeMillis();
        this.meetLink = "https://meet.google.com/" + generateMeetCode();

        System.out.println("[GoogleMeetAPI] Evento creado: " + eventId);
        return eventId;
    }

    // Generar código de Meet
    private String generateMeetCode() {
        return "abc-defg-hij";
    }

    // Método específico de Google para obtener link
    public String getMeetLink() {
        return this.meetLink;
    }

    // Método específico de Google para iniciar
    public void joinMeeting(String meetLink) {
        System.out.println("[GoogleMeetAPI] Uniendo a reunión: " + meetLink);
    }

    // Método específico de Google para finalizar
    public void endEvent(String eventId) {
        System.out.println("[GoogleMeetAPI] Finalizando evento " + eventId);
    }

    public String getEventId() {
        return eventId;
    }
}
