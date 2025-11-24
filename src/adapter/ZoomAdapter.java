package adapter;

/**
 * PATRÓN ADAPTER - Adapter Concreto
 * Adapta la interfaz de ZoomAPI a la interfaz IVideoconferencia del LMS
 * Permite usar Zoom sin modificar el código del sistema principal
 */
public class ZoomAdapter implements IVideoconferencia {
    // Referencia al objeto adaptado (Adaptee)
    private ZoomAPI zoomAPI;
    private String enlaceActual;

    public ZoomAdapter() {
        // Crear instancia del servicio externo
        this.zoomAPI = new ZoomAPI();
    }

    @Override
    public String crearReunion(String titulo, int duracionMinutos) {
        // Adaptar: convertir llamada del LMS a llamada de Zoom
        // Zoom requiere timezone, el LMS no lo maneja directamente
        String timezone = "America/Mexico_City";
        String meetingId = zoomAPI.createMeeting(titulo, duracionMinutos, timezone);
        this.enlaceActual = zoomAPI.getJoinUrl();
        return meetingId;
    }

    @Override
    public String obtenerEnlace() {
        return this.enlaceActual;
    }

    @Override
    public void iniciarReunion() {
        // Adaptar: Zoom requiere meetingId y hostKey
        zoomAPI.startMeeting(zoomAPI.getMeetingId(), zoomAPI.getHostKey());
    }

    @Override
    public void finalizarReunion() {
        zoomAPI.endMeeting(zoomAPI.getMeetingId());
    }

    @Override
    public String getProveedor() {
        return "Zoom";
    }
}
