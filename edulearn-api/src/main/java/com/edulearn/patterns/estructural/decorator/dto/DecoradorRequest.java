package com.edulearn.patterns.estructural.decorator.dto;

/**
 * DTO para aplicar decoradores a un módulo
 * Permite habilitar gamificación y/o certificación
 */
public class DecoradorRequest {

    // Gamificación
    private Boolean gamificacionHabilitada;
    private Integer gamificacionPuntos;
    private String gamificacionBadge;

    // Certificación
    private Boolean certificacionHabilitada;
    private String certificacionTipo;
    private Boolean certificacionActiva;

    // Constructores
    public DecoradorRequest() {
    }

    public DecoradorRequest(Boolean gamificacionHabilitada, Integer gamificacionPuntos,
                           String gamificacionBadge, Boolean certificacionHabilitada,
                           String certificacionTipo, Boolean certificacionActiva) {
        this.gamificacionHabilitada = gamificacionHabilitada;
        this.gamificacionPuntos = gamificacionPuntos;
        this.gamificacionBadge = gamificacionBadge;
        this.certificacionHabilitada = certificacionHabilitada;
        this.certificacionTipo = certificacionTipo;
        this.certificacionActiva = certificacionActiva;
    }

    // Getters y Setters
    public Boolean getGamificacionHabilitada() {
        return gamificacionHabilitada;
    }

    public void setGamificacionHabilitada(Boolean gamificacionHabilitada) {
        this.gamificacionHabilitada = gamificacionHabilitada;
    }

    public Integer getGamificacionPuntos() {
        return gamificacionPuntos;
    }

    public void setGamificacionPuntos(Integer gamificacionPuntos) {
        this.gamificacionPuntos = gamificacionPuntos;
    }

    public String getGamificacionBadge() {
        return gamificacionBadge;
    }

    public void setGamificacionBadge(String gamificacionBadge) {
        this.gamificacionBadge = gamificacionBadge;
    }

    public Boolean getCertificacionHabilitada() {
        return certificacionHabilitada;
    }

    public void setCertificacionHabilitada(Boolean certificacionHabilitada) {
        this.certificacionHabilitada = certificacionHabilitada;
    }

    public String getCertificacionTipo() {
        return certificacionTipo;
    }

    public void setCertificacionTipo(String certificacionTipo) {
        this.certificacionTipo = certificacionTipo;
    }

    public Boolean getCertificacionActiva() {
        return certificacionActiva;
    }

    public void setCertificacionActiva(Boolean certificacionActiva) {
        this.certificacionActiva = certificacionActiva;
    }
}
