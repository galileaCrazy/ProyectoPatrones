package com.edulearn.patterns.estructural.facade.dto;

/**
 * DTO para solicitud de inscripción a través del patrón Facade
 * Simplifica la comunicación entre el frontend y el sistema complejo de inscripción
 */
public class InscripcionRequest {

    private Integer estudianteId;
    private Integer cursoId;
    private String tipoInscripcion; // GRATUITA, PAGA, BECA
    private String metodoPago;      // Solo para PAGA
    private Double monto;            // Solo para PAGA
    private String tipoBeca;         // Solo para BECA
    private String codigoBeca;       // Solo para BECA
    private Boolean aceptaTerminos;

    // Constructor vacío
    public InscripcionRequest() {
    }

    // Constructor completo
    public InscripcionRequest(Integer estudianteId, Integer cursoId, String tipoInscripcion) {
        this.estudianteId = estudianteId;
        this.cursoId = cursoId;
        this.tipoInscripcion = tipoInscripcion;
    }

    // Getters y Setters
    public Integer getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Integer estudianteId) {
        this.estudianteId = estudianteId;
    }

    public Integer getCursoId() {
        return cursoId;
    }

    public void setCursoId(Integer cursoId) {
        this.cursoId = cursoId;
    }

    public String getTipoInscripcion() {
        return tipoInscripcion;
    }

    public void setTipoInscripcion(String tipoInscripcion) {
        this.tipoInscripcion = tipoInscripcion;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getTipoBeca() {
        return tipoBeca;
    }

    public void setTipoBeca(String tipoBeca) {
        this.tipoBeca = tipoBeca;
    }

    public String getCodigoBeca() {
        return codigoBeca;
    }

    public void setCodigoBeca(String codigoBeca) {
        this.codigoBeca = codigoBeca;
    }

    public Boolean getAceptaTerminos() {
        return aceptaTerminos;
    }

    public void setAceptaTerminos(Boolean aceptaTerminos) {
        this.aceptaTerminos = aceptaTerminos;
    }
}
