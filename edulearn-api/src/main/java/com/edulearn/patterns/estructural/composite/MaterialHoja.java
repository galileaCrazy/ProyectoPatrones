package com.edulearn.patterns.estructural.composite;

import java.util.ArrayList;
import java.util.List;

/**
 * Leaf - Representa un material educativo (video, documento, etc.)
 * Es una hoja en el árbol de la estructura del curso
 */
public class MaterialHoja extends ComponenteCurso {
    private String tipoMaterial;
    private String urlRecurso;
    private String archivoPath;
    private Integer duracionSegundos;
    private Boolean esObligatorio;

    public MaterialHoja(Long id, String nombre, String tipoMaterial, String descripcion,
                       Integer orden, String urlRecurso, String archivoPath, Integer duracionSegundos,
                       Boolean esObligatorio) {
        super(id, nombre, "MATERIAL", descripcion, orden);
        this.tipoMaterial = tipoMaterial;
        this.urlRecurso = urlRecurso;
        this.archivoPath = archivoPath;
        this.duracionSegundos = duracionSegundos;
        this.esObligatorio = esObligatorio;
    }

    @Override
    public String obtenerInformacion() {
        StringBuilder info = new StringBuilder();
        info.append("Material: ").append(nombre)
            .append(" (").append(tipoMaterial).append(")")
            .append(" - Duración: ").append(calcularDuracionTotal()).append(" min");

        if (esObligatorio) {
            info.append(" [OBLIGATORIO]");
        }

        if (descripcion != null && !descripcion.isEmpty()) {
            info.append("\n  Descripción: ").append(descripcion);
        }

        return info.toString();
    }

    @Override
    public Integer calcularDuracionTotal() {
        // Convertir segundos a minutos
        return duracionSegundos != null ? (int) Math.ceil(duracionSegundos / 60.0) : 0;
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
    public String getTipoMaterial() {
        return tipoMaterial;
    }

    public void setTipoMaterial(String tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }

    public String getUrlRecurso() {
        return urlRecurso;
    }

    public void setUrlRecurso(String urlRecurso) {
        this.urlRecurso = urlRecurso;
    }

    public String getArchivoPath() {
        return archivoPath;
    }

    public void setArchivoPath(String archivoPath) {
        this.archivoPath = archivoPath;
    }

    public Integer getDuracionSegundos() {
        return duracionSegundos;
    }

    public void setDuracionSegundos(Integer duracionSegundos) {
        this.duracionSegundos = duracionSegundos;
    }

    public Boolean getEsObligatorio() {
        return esObligatorio;
    }

    public void setEsObligatorio(Boolean esObligatorio) {
        this.esObligatorio = esObligatorio;
    }
}
