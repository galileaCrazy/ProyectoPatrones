package com.edulearn.patterns.structural.bridge;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Refined Abstraction - Reporte de Estudiantes
 */
public class ReporteEstudiantes extends Reporte {
    private int totalEstudiantes;
    private int estudiantesActivos;
    private int estudiantesInactivos;
    private double promedioCalificaciones;

    public ReporteEstudiantes(IFormatoReporte formato) {
        super(formato);
    }

    public void setDatosEstudiantes(int total, int activos, int inactivos, double promedio) {
        this.totalEstudiantes = total;
        this.estudiantesActivos = activos;
        this.estudiantesInactivos = inactivos;
        this.promedioCalificaciones = promedio;
    }

    @Override
    protected String obtenerTitulo() {
        return "Reporte de Estudiantes - EduLearn Platform";
    }

    @Override
    protected Map<String, Object> obtenerDatos() {
        Map<String, Object> datos = new LinkedHashMap<>();
        datos.put("Total de Estudiantes", totalEstudiantes);
        datos.put("Estudiantes Activos", estudiantesActivos);
        datos.put("Estudiantes Inactivos", estudiantesInactivos);
        datos.put("Promedio de Calificaciones", String.format("%.2f", promedioCalificaciones));
        datos.put("Tasa de Actividad", String.format("%.1f%%",
            totalEstudiantes > 0 ? (estudiantesActivos * 100.0 / totalEstudiantes) : 0));
        return datos;
    }
}
