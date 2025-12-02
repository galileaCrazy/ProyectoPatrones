package com.edulearn.patterns.estructural.adapter;

/**
 * Adapter para Google Meet.
 * Adapta la interfaz incompatible de GoogleMeetService a la interfaz ISistemaVideoconferencia.
 */
public class GoogleMeetAdapter implements ISistemaVideoconferencia {
    private GoogleMeetService googleMeetService;

    public GoogleMeetAdapter(String apiKey) {
        this.googleMeetService = new GoogleMeetService(apiKey);
    }

    @Override
    public String crearReunion(String nombreSala, int duracionMinutos) {
        // Adaptar la llamada de la interfaz común a la API específica de Google Meet
        return googleMeetService.generateMeetingCode(nombreSala);
    }

    @Override
    public String obtenerEnlaceReunion(String idReunion) {
        return googleMeetService.getConferenceUrl(idReunion);
    }

    @Override
    public boolean finalizarReunion(String idReunion) {
        googleMeetService.closeConference(idReunion);
        return true;
    }

    @Override
    public boolean verificarConexion() {
        return googleMeetService.isApiKeyValid();
    }

    @Override
    public String obtenerInfoSistema() {
        return "Google Meet - Workspace API";
    }
}
