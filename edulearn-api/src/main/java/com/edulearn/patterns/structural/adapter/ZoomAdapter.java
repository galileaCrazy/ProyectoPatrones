package com.edulearn.patterns.structural.adapter;

/**
 * Adapter para Zoom.
 * Adapta la interfaz incompatible de ZoomAPI a la interfaz ISistemaVideoconferencia.
 */
public class ZoomAdapter implements ISistemaVideoconferencia {
    private ZoomAPI zoomAPI;

    public ZoomAdapter(String apiKey, String apiSecret) {
        this.zoomAPI = new ZoomAPI(apiKey, apiSecret);
    }

    @Override
    public String crearReunion(String nombreSala, int duracionMinutos) {
        // Adaptar la llamada de la interfaz común a la API específica de Zoom
        return zoomAPI.createZoomMeeting(nombreSala, duracionMinutos);
    }

    @Override
    public String obtenerEnlaceReunion(String idReunion) {
        return zoomAPI.getMeetingLink(idReunion);
    }

    @Override
    public boolean finalizarReunion(String idReunion) {
        return zoomAPI.endMeeting(idReunion);
    }

    @Override
    public boolean verificarConexion() {
        return zoomAPI.validateCredentials();
    }

    @Override
    public String obtenerInfoSistema() {
        return "Zoom Video Communications - API v2";
    }
}
