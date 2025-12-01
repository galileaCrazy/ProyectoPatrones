package com.edulearn.patterns.structural.bridge;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Refined Abstraction - Reporte de Calificaciones
 */
public class ReporteCalificaciones extends Reporte {
    private int totalCalificaciones;
    private double promedioGeneral;
    private int aprobados;
    private int reprobados;
    private double calificacionMasAlta;
    private double calificacionMasBaja;

    public ReporteCalificaciones(IFormatoReporte formato) {
        super(formato);
    }

    public void setDatosCalificaciones(int total, double promedio, int aprobados, int reprobados,
                                      double masAlta, double masBaja) {
        this.totalCalificaciones = total;
        this.promedioGeneral = promedio;
        this.aprobados = aprobados;
        this.reprobados = reprobados;
        this.calificacionMasAlta = masAlta;
        this.calificacionMasBaja = masBaja;
    }

    @Override
    protected String obtenerTitulo() {
        return "Reporte de Calificaciones - EduLearn Platform";
    }

    @Override
    protected Map<String, Object> obtenerDatos() {
        Map<String, Object> datos = new LinkedHashMap<>();
        datos.put("Total de Calificaciones", totalCalificaciones);
        datos.put("Promedio General", String.format("%.2f", promedioGeneral));
        datos.put("Estudiantes Aprobados", aprobados);
        datos.put("Estudiantes Reprobados", reprobados);
        datos.put("Tasa de Aprobación", String.format("%.1f%%",
            totalCalificaciones > 0 ? (aprobados * 100.0 / totalCalificaciones) : 0));
        datos.put("Calificación Más Alta", String.format("%.2f", calificacionMasAlta));
        datos.put("Calificación Más Baja", String.format("%.2f", calificacionMasBaja));
        return datos;
    }
}
