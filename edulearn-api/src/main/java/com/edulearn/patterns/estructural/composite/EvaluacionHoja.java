package com.edulearn.patterns.estructural.composite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Leaf - Representa una evaluación (quiz, examen, etc.)
 * Es una hoja en el árbol de la estructura del curso
 */
public class EvaluacionHoja extends ComponenteCurso {
    private String tipoEvaluacion;
    private BigDecimal puntajeMaximo;
    private Integer tiempoLimiteMinutos;
    private Integer intentosPermitidos;
    private String estado;

    public EvaluacionHoja(Long id, String nombre, String tipoEvaluacion, String descripcion,
                         Integer orden, BigDecimal puntajeMaximo, Integer tiempoLimiteMinutos,
                         Integer intentosPermitidos, String estado) {
        super(id, nombre, "EVALUACION", descripcion, orden);
        this.tipoEvaluacion = tipoEvaluacion;
        this.puntajeMaximo = puntajeMaximo;
        this.tiempoLimiteMinutos = tiempoLimiteMinutos;
        this.intentosPermitidos = intentosPermitidos;
        this.estado = estado;
    }

    @Override
    public String obtenerInformacion() {
        StringBuilder info = new StringBuilder();
        info.append("Evaluación: ").append(nombre)
            .append(" (").append(tipoEvaluacion).append(")")
            .append(" - Tiempo: ").append(calcularDuracionTotal()).append(" min")
            .append(" - Puntaje: ").append(puntajeMaximo)
            .append(" - Intentos: ").append(intentosPermitidos);

        if (descripcion != null && !descripcion.isEmpty()) {
            info.append("\n  Descripción: ").append(descripcion);
        }

        return info.toString();
    }

    @Override
    public Integer calcularDuracionTotal() {
        return tiempoLimiteMinutos != null ? tiempoLimiteMinutos : 0;
    }

    @Override
    public List<ComponenteCurso> obtenerHijos() {
        return new ArrayList<>();
    }

    @Override
    public boolean esHoja() {
        return true;
    }

    // Getters específicos
    public String getTipoEvaluacion() {
        return tipoEvaluacion;
    }

    public void setTipoEvaluacion(String tipoEvaluacion) {
        this.tipoEvaluacion = tipoEvaluacion;
    }

    public BigDecimal getPuntajeMaximo() {
        return puntajeMaximo;
    }

    public void setPuntajeMaximo(BigDecimal puntajeMaximo) {
        this.puntajeMaximo = puntajeMaximo;
    }

    public Integer getTiempoLimiteMinutos() {
        return tiempoLimiteMinutos;
    }

    public void setTiempoLimiteMinutos(Integer tiempoLimiteMinutos) {
        this.tiempoLimiteMinutos = tiempoLimiteMinutos;
    }

    public Integer getIntentosPermitidos() {
        return intentosPermitidos;
    }

    public void setIntentosPermitidos(Integer intentosPermitidos) {
        this.intentosPermitidos = intentosPermitidos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
