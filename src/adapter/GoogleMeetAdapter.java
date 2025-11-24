package adapter;

/**
 * PATRÓN ADAPTER - Adapter Concreto
 * Adapta la interfaz de GoogleMeetAPI a la interfaz IVideoconferencia del LMS
 */
public class GoogleMeetAdapter implements IVideoconferencia {
    private GoogleMeetAPI googleMeetAPI;
    private String enlaceActual;

    public GoogleMeetAdapter() {
        this.googleMeetAPI = new GoogleMeetAPI();
    }

    @Override
    public String crearReunion(String titulo, int duracionMinutos) {
        // Adaptar: Google usa calendarId, el LMS no lo necesita
        String calendarId = "primary";
        String eventId = googleMeetAPI.scheduleEvent(titulo, duracionMinutos, calendarId);
        this.enlaceActual = googleMeetAPI.getMeetLink();
        return eventId;
    }

    @Override
    public String obtenerEnlace() {
        return this.enlaceActual;
    }

    @Override
    public void iniciarReunion() {
        googleMeetAPI.joinMeeting(enlaceActual);
    }

    @Override
    public void finalizarReunion() {
        googleMeetAPI.endEvent(googleMeetAPI.getEventId());
    }

    @Override
    public String getProveedor() {
        return "Google Meet";
    }
}
