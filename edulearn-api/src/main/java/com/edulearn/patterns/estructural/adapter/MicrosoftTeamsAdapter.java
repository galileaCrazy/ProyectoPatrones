package com.edulearn.patterns.estructural.adapter;

/**
 * Adapter para Microsoft Teams.
 * Adapta la interfaz incompatible de MicrosoftTeamsClient a la interfaz ISistemaVideoconferencia.
 */
public class MicrosoftTeamsAdapter implements ISistemaVideoconferencia {
    private MicrosoftTeamsClient teamsClient;

    public MicrosoftTeamsAdapter(String tenantId, String clientSecret) {
        this.teamsClient = new MicrosoftTeamsClient(tenantId, clientSecret);
    }

    @Override
    public String crearReunion(String nombreSala, int duracionMinutos) {
        // Adaptar la llamada de la interfaz común a la API específica de MS Teams
        return teamsClient.startOnlineSession(nombreSala, duracionMinutos);
    }

    @Override
    public String obtenerEnlaceReunion(String idReunion) {
        return teamsClient.getJoinLink(idReunion);
    }

    @Override
    public boolean finalizarReunion(String idReunion) {
        return teamsClient.terminateSession(idReunion);
    }

    @Override
    public boolean verificarConexion() {
        return teamsClient.authenticate();
    }

    @Override
    public String obtenerInfoSistema() {
        return "Microsoft Teams - Graph API v1.0";
    }
}
