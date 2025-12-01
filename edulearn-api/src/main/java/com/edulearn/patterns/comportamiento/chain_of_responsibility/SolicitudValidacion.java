package com.edulearn.patterns.comportamiento.chain_of_responsibility;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase que representa una solicitud de validación que viaja a través de la cadena.
 * Contiene toda la información necesaria para que los handlers puedan procesarla.
 */
public class SolicitudValidacion {
    private String token;
    private String tipoUsuario;
    private String recursoSolicitado;
    private String accion;
    private Map<String, Object> metadatos;
    private boolean aprobada;
    private String mensajeError;

    public SolicitudValidacion(String token, String recursoSolicitado, String accion) {
        this.token = token;
        this.recursoSolicitado = recursoSolicitado;
        this.accion = accion;
        this.metadatos = new HashMap<>();
        this.aprobada = false;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getRecursoSolicitado() {
        return recursoSolicitado;
    }

    public void setRecursoSolicitado(String recursoSolicitado) {
        this.recursoSolicitado = recursoSolicitado;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Map<String, Object> getMetadatos() {
        return metadatos;
    }

    public void agregarMetadato(String clave, Object valor) {
        this.metadatos.put(clave, valor);
    }

    public boolean isAprobada() {
        return aprobada;
    }

    public void setAprobada(boolean aprobada) {
        this.aprobada = aprobada;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public void setMensajeError(String mensajeError) {
        this.mensajeError = mensajeError;
    }
}
