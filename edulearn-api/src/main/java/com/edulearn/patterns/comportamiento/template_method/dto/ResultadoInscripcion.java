package com.edulearn.patterns.comportamiento.template_method.dto;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para el resultado completo del proceso de inscripción
 */
public class ResultadoInscripcion {
    
    private Integer estudianteId;
    private Integer cursoId;
    private String tipoInscripcion;
    private boolean exitoso;
    private String estado; // COMPLETADA, FALLIDA, PENDIENTE
    private String mensaje;
    private List<ResultadoPaso> pasos;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private long duracionTotalMs;
    
    // Información adicional
    private String numeroInscripcion;
    private BigDecimal montoTotal;
    private BigDecimal descuentoAplicado;
    private String urlComprobante;
    private String urlRecibo;
    
    public void calcularDuracion() {
        if (fechaInicio != null && fechaFin != null) {
            this.duracionTotalMs = Duration.between(fechaInicio, fechaFin).toMillis();
        }
    }
    
    // Getters y Setters
    public Integer getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Integer estudianteId) { this.estudianteId = estudianteId; }
    
    public Integer getCursoId() { return cursoId; }
    public void setCursoId(Integer cursoId) { this.cursoId = cursoId; }
    
    public String getTipoInscripcion() { return tipoInscripcion; }
    public void setTipoInscripcion(String tipoInscripcion) { this.tipoInscripcion = tipoInscripcion; }
    
    public boolean isExitoso() { return exitoso; }
    public void setExitoso(boolean exitoso) { this.exitoso = exitoso; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    
    public List<ResultadoPaso> getPasos() { return pasos; }
    public void setPasos(List<ResultadoPaso> pasos) { this.pasos = pasos; }
    
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    
    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
    
    public long getDuracionTotalMs() { return duracionTotalMs; }
    public void setDuracionTotalMs(long duracionTotalMs) { this.duracionTotalMs = duracionTotalMs; }
    
    public String getNumeroInscripcion() { return numeroInscripcion; }
    public void setNumeroInscripcion(String numeroInscripcion) { this.numeroInscripcion = numeroInscripcion; }
    
    public BigDecimal getMontoTotal() { return montoTotal; }
    public void setMontoTotal(BigDecimal montoTotal) { this.montoTotal = montoTotal; }
    
    public BigDecimal getDescuentoAplicado() { return descuentoAplicado; }
    public void setDescuentoAplicado(BigDecimal descuentoAplicado) { this.descuentoAplicado = descuentoAplicado; }
    
    public String getUrlComprobante() { return urlComprobante; }
    public void setUrlComprobante(String urlComprobante) { this.urlComprobante = urlComprobante; }
    
    public String getUrlRecibo() { return urlRecibo; }
    public void setUrlRecibo(String urlRecibo) { this.urlRecibo = urlRecibo; }
}
