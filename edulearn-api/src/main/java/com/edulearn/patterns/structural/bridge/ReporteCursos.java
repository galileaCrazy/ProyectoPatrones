package com.edulearn.patterns.structural.bridge;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Refined Abstraction - Reporte de Cursos
 */
public class ReporteCursos extends Reporte {
    private int totalCursos;
    private int cursosActivos;
    private int cursosBorrador;
    private int totalInscritos;

    public ReporteCursos(IFormatoReporte formato) {
        super(formato);
    }

    public void setDatosCursos(int total, int activos, int borrador, int inscritos) {
        this.totalCursos = total;
        this.cursosActivos = activos;
        this.cursosBorrador = borrador;
        this.totalInscritos = inscritos;
    }

    @Override
    protected String obtenerTitulo() {
        return "Reporte de Cursos - EduLearn Platform";
    }

    @Override
    protected Map<String, Object> obtenerDatos() {
        Map<String, Object> datos = new LinkedHashMap<>();
        datos.put("Total de Cursos", totalCursos);
        datos.put("Cursos Activos", cursosActivos);
        datos.put("Cursos en Borrador", cursosBorrador);
        datos.put("Total de Inscritos", totalInscritos);
        datos.put("Promedio Inscritos por Curso",
            cursosActivos > 0 ? String.format("%.1f", totalInscritos * 1.0 / cursosActivos) : "0");
        return datos;
    }
}
