package com.edulearn.controller;

import com.edulearn.model.ReporteGenerado;
import com.edulearn.patterns.structural.bridge.*;
import com.edulearn.repository.ReporteGeneradoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteGeneradoRepository reporteRepository;

    /**
     * GET /api/reportes
     * Obtener todos los reportes generados
     */
    @GetMapping
    public List<ReporteGenerado> getAll() {
        return reporteRepository.findAll();
    }

    /**
     * GET /api/reportes/{id}
     * Obtener reporte por ID
     */
    @GetMapping("/{id}")
    public ReporteGenerado getById(@PathVariable Integer id) {
        return reporteRepository.findById(id).orElse(null);
    }

    /**
     * GET /api/reportes/tipo/{tipo}
     * Obtener reportes por tipo
     */
    @GetMapping("/tipo/{tipo}")
    public List<ReporteGenerado> getByTipo(@PathVariable String tipo) {
        return reporteRepository.findByTipoReporte(tipo);
    }

    // ========== ENDPOINTS CON PATRÓN BRIDGE ==========

    /**
     * POST /api/reportes/generar
     * Generar reporte usando patrón Bridge
     */
    @PostMapping("/generar")
    public Map<String, Object> generarReporte(@RequestBody Map<String, Object> params) {
        String tipoReporte = (String) params.get("tipoReporte");
        String formato = (String) params.get("formato");

        // Crear formato (Implementación) usando Factory
        IFormatoReporte formatoImpl = crearFormato(formato);

        // Crear reporte (Abstracción) según tipo
        Reporte reporte = crearReporte(tipoReporte, formatoImpl, params);

        // Generar contenido usando Bridge
        String contenido = reporte.generar();

        // Guardar en BD
        ReporteGenerado reporteGenerado = new ReporteGenerado();
        reporteGenerado.setTipoReporte(tipoReporte);
        reporteGenerado.setFormato(formato);
        reporteGenerado.setTitulo(obtenerTituloReporte(tipoReporte));
        reporteGenerado.setContenido(contenido);
        reporteGenerado.setParametros(params.toString());
        reporteGenerado.setEstado("GENERADO");

        reporteGenerado = reporteRepository.save(reporteGenerado);

        // Respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("reporteId", reporteGenerado.getId());
        response.put("tipoReporte", tipoReporte);
        response.put("formato", formato);
        response.put("contenido", contenido);
        response.put("extension", reporte.obtenerExtension());
        response.put("mimeType", reporte.obtenerTipoMIME());
        response.put("mensaje", "Reporte generado exitosamente usando patrón Bridge");

        return response;
    }

    /**
     * POST /api/reportes/estudiantes
     * Generar reporte de estudiantes
     */
    @PostMapping("/estudiantes")
    public Map<String, Object> generarReporteEstudiantes(@RequestBody Map<String, String> params) {
        String formato = params.getOrDefault("formato", "PDF");

        IFormatoReporte formatoImpl = crearFormato(formato);
        ReporteEstudiantes reporte = new ReporteEstudiantes(formatoImpl);

        // Datos simulados (en producción vendrían de la BD)
        reporte.setDatosEstudiantes(150, 142, 8, 85.5);

        String contenido = reporte.generar();

        // Guardar
        ReporteGenerado reporteGenerado = new ReporteGenerado();
        reporteGenerado.setTipoReporte("ESTUDIANTES");
        reporteGenerado.setFormato(formato);
        reporteGenerado.setTitulo("Reporte de Estudiantes");
        reporteGenerado.setContenido(contenido);
        reporteGenerado = reporteRepository.save(reporteGenerado);

        Map<String, Object> response = new HashMap<>();
        response.put("reporteId", reporteGenerado.getId());
        response.put("contenido", contenido);
        response.put("formato", formato);

        return response;
    }

    /**
     * POST /api/reportes/cursos
     * Generar reporte de cursos
     */
    @PostMapping("/cursos")
    public Map<String, Object> generarReporteCursos(@RequestBody Map<String, String> params) {
        String formato = params.getOrDefault("formato", "EXCEL");

        IFormatoReporte formatoImpl = crearFormato(formato);
        ReporteCursos reporte = new ReporteCursos(formatoImpl);

        // Datos simulados
        reporte.setDatosCursos(45, 38, 7, 520);

        String contenido = reporte.generar();

        ReporteGenerado reporteGenerado = new ReporteGenerado();
        reporteGenerado.setTipoReporte("CURSOS");
        reporteGenerado.setFormato(formato);
        reporteGenerado.setTitulo("Reporte de Cursos");
        reporteGenerado.setContenido(contenido);
        reporteGenerado = reporteRepository.save(reporteGenerado);

        Map<String, Object> response = new HashMap<>();
        response.put("reporteId", reporteGenerado.getId());
        response.put("contenido", contenido);
        response.put("formato", formato);

        return response;
    }

    /**
     * POST /api/reportes/calificaciones
     * Generar reporte de calificaciones
     */
    @PostMapping("/calificaciones")
    public Map<String, Object> generarReporteCalificaciones(@RequestBody Map<String, String> params) {
        String formato = params.getOrDefault("formato", "HTML");

        IFormatoReporte formatoImpl = crearFormato(formato);
        ReporteCalificaciones reporte = new ReporteCalificaciones(formatoImpl);

        // Datos simulados
        reporte.setDatosCalificaciones(520, 78.5, 468, 52, 98.5, 45.0);

        String contenido = reporte.generar();

        ReporteGenerado reporteGenerado = new ReporteGenerado();
        reporteGenerado.setTipoReporte("CALIFICACIONES");
        reporteGenerado.setFormato(formato);
        reporteGenerado.setTitulo("Reporte de Calificaciones");
        reporteGenerado.setContenido(contenido);
        reporteGenerado = reporteRepository.save(reporteGenerado);

        Map<String, Object> response = new HashMap<>();
        response.put("reporteId", reporteGenerado.getId());
        response.put("contenido", contenido);
        response.put("formato", formato);

        return response;
    }

    // ========== MÉTODOS AUXILIARES ==========

    private IFormatoReporte crearFormato(String formato) {
        switch (formato.toUpperCase()) {
            case "PDF":
                return new FormatoPDF();
            case "EXCEL":
            case "XLS":
                return new FormatoExcel();
            case "HTML":
                return new FormatoHTML();
            default:
                return new FormatoPDF();
        }
    }

    private Reporte crearReporte(String tipo, IFormatoReporte formato, Map<String, Object> params) {
        switch (tipo.toUpperCase()) {
            case "ESTUDIANTES":
                ReporteEstudiantes repEst = new ReporteEstudiantes(formato);
                repEst.setDatosEstudiantes(150, 142, 8, 85.5);
                return repEst;

            case "CURSOS":
                ReporteCursos repCur = new ReporteCursos(formato);
                repCur.setDatosCursos(45, 38, 7, 520);
                return repCur;

            case "CALIFICACIONES":
                ReporteCalificaciones repCal = new ReporteCalificaciones(formato);
                repCal.setDatosCalificaciones(520, 78.5, 468, 52, 98.5, 45.0);
                return repCal;

            default:
                ReporteEstudiantes repDefault = new ReporteEstudiantes(formato);
                repDefault.setDatosEstudiantes(0, 0, 0, 0);
                return repDefault;
        }
    }

    private String obtenerTituloReporte(String tipo) {
        switch (tipo.toUpperCase()) {
            case "ESTUDIANTES": return "Reporte de Estudiantes";
            case "CURSOS": return "Reporte de Cursos";
            case "CALIFICACIONES": return "Reporte de Calificaciones";
            default: return "Reporte General";
        }
    }
}
