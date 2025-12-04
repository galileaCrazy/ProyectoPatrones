package com.edulearn.patterns.estructural.facade.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO para respuesta de inscripción a través del patrón Facade
 * Encapsula el resultado completo del proceso de inscripción
 */
public class InscripcionResponse {

    private boolean exitoso;
    private String mensaje;
    private Integer inscripcionId;
    private String estadoInscripcion;

    // Información de los subsistemas procesados
    private boolean validacionCompletada;
    private boolean materialesAsignados;
    private boolean evaluacionesPreparadas;
    private boolean seguimientoIniciado;

    // Detalles adicionales
    private List<String> detalles;
    private List<String> errores;

    // Datos del curso inscrito
    private String cursoNombre;
    private Integer totalMateriales;
    private Integer totalEvaluaciones;

    // Constructor vacío
    public InscripcionResponse() {
        this.detalles = new ArrayList<>();
        this.errores = new ArrayList<>();
    }

    // Constructor de éxito
    public InscripcionResponse(boolean exitoso, String mensaje, Integer inscripcionId) {
        this();
        this.exitoso = exitoso;
        this.mensaje = mensaje;
        this.inscripcionId = inscripcionId;
    }

    // Método helper para agregar detalles
    public void agregarDetalle(String detalle) {
        this.detalles.add(detalle);
    }

    // Método helper para agregar errores
    public void agregarError(String error) {
        this.errores.add(error);
    }

    // Getters y Setters
    public boolean isExitoso() {
        return exitoso;
    }

    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Integer getInscripcionId() {
        return inscripcionId;
    }

    public void setInscripcionId(Integer inscripcionId) {
        this.inscripcionId = inscripcionId;
    }

    public String getEstadoInscripcion() {
        return estadoInscripcion;
    }

    public void setEstadoInscripcion(String estadoInscripcion) {
        this.estadoInscripcion = estadoInscripcion;
    }

    public boolean isValidacionCompletada() {
        return validacionCompletada;
    }

    public void setValidacionCompletada(boolean validacionCompletada) {
        this.validacionCompletada = validacionCompletada;
    }

    public boolean isMaterialesAsignados() {
        return materialesAsignados;
    }

    public void setMaterialesAsignados(boolean materialesAsignados) {
        this.materialesAsignados = materialesAsignados;
    }

    public boolean isEvaluacionesPreparadas() {
        return evaluacionesPreparadas;
    }

    public void setEvaluacionesPreparadas(boolean evaluacionesPreparadas) {
        this.evaluacionesPreparadas = evaluacionesPreparadas;
    }

    public boolean isSeguimientoIniciado() {
        return seguimientoIniciado;
    }

    public void setSeguimientoIniciado(boolean seguimientoIniciado) {
        this.seguimientoIniciado = seguimientoIniciado;
    }

    public List<String> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<String> detalles) {
        this.detalles = detalles;
    }

    public List<String> getErrores() {
        return errores;
    }

    public void setErrores(List<String> errores) {
        this.errores = errores;
    }

    public String getCursoNombre() {
        return cursoNombre;
    }

    public void setCursoNombre(String cursoNombre) {
        this.cursoNombre = cursoNombre;
    }

    public Integer getTotalMateriales() {
        return totalMateriales;
    }

    public void setTotalMateriales(Integer totalMateriales) {
        this.totalMateriales = totalMateriales;
    }

    public Integer getTotalEvaluaciones() {
        return totalEvaluaciones;
    }

    public void setTotalEvaluaciones(Integer totalEvaluaciones) {
        this.totalEvaluaciones = totalEvaluaciones;
    }
}
