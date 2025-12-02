package com.edulearn.patterns.comportamiento.template_method.dto;

import java.math.BigDecimal;

/**
 * DTO para la solicitud de inscripción
 */
public class SolicitudInscripcion {
    
    private Integer estudianteId;
    private Integer cursoId;
    private String tipoInscripcion; // GRATUITA, PAGA, BECA
    
    // Para inscripción paga
    private String metodoPago;
    private String numeroTarjeta;
    private BigDecimal monto;
    private String codigoDescuento;
    
    // Para inscripción con beca
    private String tipoBeca;
    private String codigoBeca;
    private Integer porcentajeBeca;
    private String documentoSoporte;
    
    // Documentación general
    private boolean aceptaTerminos;
    private String motivoInscripcion;
    private String documentoIdentidad;
    
    // Getters y Setters
    public Integer getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Integer estudianteId) { this.estudianteId = estudianteId; }
    
    public Integer getCursoId() { return cursoId; }
    public void setCursoId(Integer cursoId) { this.cursoId = cursoId; }
    
    public String getTipoInscripcion() { return tipoInscripcion; }
    public void setTipoInscripcion(String tipoInscripcion) { this.tipoInscripcion = tipoInscripcion; }
    
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    
    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }
    
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    
    public String getCodigoDescuento() { return codigoDescuento; }
    public void setCodigoDescuento(String codigoDescuento) { this.codigoDescuento = codigoDescuento; }
    
    public String getTipoBeca() { return tipoBeca; }
    public void setTipoBeca(String tipoBeca) { this.tipoBeca = tipoBeca; }
    
    public String getCodigoBeca() { return codigoBeca; }
    public void setCodigoBeca(String codigoBeca) { this.codigoBeca = codigoBeca; }
    
    public Integer getPorcentajeBeca() { return porcentajeBeca; }
    public void setPorcentajeBeca(Integer porcentajeBeca) { this.porcentajeBeca = porcentajeBeca; }
    
    public String getDocumentoSoporte() { return documentoSoporte; }
    public void setDocumentoSoporte(String documentoSoporte) { this.documentoSoporte = documentoSoporte; }
    
    public boolean isAceptaTerminos() { return aceptaTerminos; }
    public void setAceptaTerminos(boolean aceptaTerminos) { this.aceptaTerminos = aceptaTerminos; }
    
    public String getMotivoInscripcion() { return motivoInscripcion; }
    public void setMotivoInscripcion(String motivoInscripcion) { this.motivoInscripcion = motivoInscripcion; }
    
    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }
}
