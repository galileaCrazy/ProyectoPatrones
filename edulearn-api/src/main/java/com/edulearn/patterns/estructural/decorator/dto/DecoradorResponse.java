package com.edulearn.patterns.estructural.decorator.dto;

/**
 * DTO de respuesta con información del módulo decorado
 * Incluye el contenido base del módulo más las funcionalidades añadidas
 */
public class DecoradorResponse {

    private Long moduloId;
    private String nombre;
    private String contenidoBase;
    private String contenidoDecorado;

    // Información de gamificación
    private Boolean gamificacionHabilitada;
    private Integer gamificacionPuntos;
    private String gamificacionBadge;

    // Información de certificación
    private Boolean certificacionHabilitada;
    private String certificacionTipo;
    private Boolean certificacionActiva;

    // Constructores
    public DecoradorResponse() {
    }

    public DecoradorResponse(Long moduloId, String nombre, String contenidoBase,
                            String contenidoDecorado) {
        this.moduloId = moduloId;
        this.nombre = nombre;
        this.contenidoBase = contenidoBase;
        this.contenidoDecorado = contenidoDecorado;
    }

    // Getters y Setters
    public Long getModuloId() {
        return moduloId;
    }

    public void setModuloId(Long moduloId) {
        this.moduloId = moduloId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContenidoBase() {
        return contenidoBase;
    }

    public void setContenidoBase(String contenidoBase) {
        this.contenidoBase = contenidoBase;
    }

    public String getContenidoDecorado() {
        return contenidoDecorado;
    }

    public void setContenidoDecorado(String contenidoDecorado) {
        this.contenidoDecorado = contenidoDecorado;
    }

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
