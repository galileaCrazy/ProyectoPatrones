package com.edulearn.controller;

import com.edulearn.model.ReporteGenerado;
import com.edulearn.patterns.structural.bridge.*;
import com.edulearn.repository.ReporteGeneradoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    @Autowired
    private ReporteGeneradoRepository reporteRepository;

    @Autowired
    private ObjectMapper objectMapper;

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

    /**
     * GET /api/reportes/{id}/descargar
     * Descargar reporte como archivo
     */
    @GetMapping("/{id}/descargar")
    public ResponseEntity<byte[]> descargarReporte(@PathVariable Integer id) {
        ReporteGenerado reporte = reporteRepository.findById(id).orElse(null);

        if (reporte == null) {
            return ResponseEntity.notFound().build();
        }

        // Determinar extensión y tipo MIME
        String extension;
        String mimeType;
        switch (reporte.getFormato().toUpperCase()) {
            case "PDF":
                extension = ".pdf";
                mimeType = "application/pdf";
                break;
            case "EXCEL":
            case "XLS":
                extension = ".xlsx";
                mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                break;
            case "HTML":
                extension = ".html";
                mimeType = "text/html";
                break;
            default:
                extension = ".txt";
                mimeType = "text/plain";
        }

        String nombreArchivo = reporte.getTitulo().replaceAll("[^a-zA-Z0-9]", "_") + extension;

        // Usar contenido binario si está disponible (PDF, Excel), sino usar contenido de texto (HTML)
        byte[] contenidoBytes;
        if (reporte.getContenidoBinario() != null && reporte.getContenidoBinario().length > 0) {
            contenidoBytes = reporte.getContenidoBinario();
        } else {
            String contenido = reporte.getContenido();
            if (contenido == null) {
                contenido = "Contenido no disponible";
            }
            contenidoBytes = contenido.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + nombreArchivo + "\"")
                .header("Content-Type", mimeType + "; charset=UTF-8")
                .body(contenidoBytes);
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
        reporteGenerado.setEstado("GENERADO");

        // Generar contenido binario para PDF y Excel, texto para HTML
        try {
            Map<String, Object> datos = obtenerDatosReporte(tipoReporte);

            if (formato.equalsIgnoreCase("PDF")) {
                byte[] pdfBytes = GeneradorPDF.generarPDF(obtenerTituloReporte(tipoReporte), datos);
                reporteGenerado.setContenidoBinario(pdfBytes);
                reporteGenerado.setContenido("[PDF Binario - " + pdfBytes.length + " bytes]");
            } else if (formato.equalsIgnoreCase("EXCEL") || formato.equalsIgnoreCase("XLS")) {
                byte[] excelBytes = GeneradorExcel.generarExcel(obtenerTituloReporte(tipoReporte), datos);
                reporteGenerado.setContenidoBinario(excelBytes);
                reporteGenerado.setContenido("[Excel Binario - " + excelBytes.length + " bytes]");
            } else {
                // HTML - usar el contenido generado por Bridge
                reporteGenerado.setContenido(contenido);
            }
        } catch (Exception e) {
            System.err.println("❌ Error al generar contenido binario: " + e.getMessage());
            e.printStackTrace();
            reporteGenerado.setContenido(contenido);
        }

        // Convertir params a JSON
        try {
            String parametrosJson = objectMapper.writeValueAsString(params);
            reporteGenerado.setParametros(parametrosJson);
        } catch (Exception e) {
            System.err.println("❌ Error al convertir parámetros a JSON: " + e.getMessage());
            reporteGenerado.setParametros("{}");
        }

        try {
            reporteGenerado = reporteRepository.save(reporteGenerado);
            System.out.println("✅ Reporte guardado en BD con ID: " + reporteGenerado.getId());
        } catch (Exception e) {
            System.err.println("❌ Error al guardar reporte: " + e.getMessage());
            e.printStackTrace();
        }

        // Respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("reporteId", reporteGenerado.getId() != null ? reporteGenerado.getId() : System.currentTimeMillis());
        response.put("tipoReporte", tipoReporte);
        response.put("formato", formato);
        response.put("titulo", obtenerTituloReporte(tipoReporte));
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

    private Map<String, Object> obtenerDatosReporte(String tipo) {
        Map<String, Object> datos = new HashMap<>();

        switch (tipo.toUpperCase()) {
            case "ESTUDIANTES":
                datos.put("Total de Estudiantes", 150);
                datos.put("Estudiantes Activos", 142);
                datos.put("Estudiantes Inactivos", 8);
                datos.put("Promedio de Calificaciones", "85.5%");
                datos.put("Tasa de Actividad", "94.7%");
                break;

            case "CURSOS":
                datos.put("Total de Cursos", 45);
                datos.put("Cursos Activos", 38);
                datos.put("Cursos Finalizados", 7);
                datos.put("Total de Inscripciones", 520);
                datos.put("Promedio de Alumnos por Curso", 13.7);
                break;

            case "CALIFICACIONES":
                datos.put("Total de Calificaciones Registradas", 520);
                datos.put("Promedio General", "78.5%");
                datos.put("Calificaciones Aprobatorias", 468);
                datos.put("Calificaciones Reprobatorias", 52);
                datos.put("Calificación Más Alta", "98.5%");
                datos.put("Calificación Más Baja", "45.0%");
                datos.put("Tasa de Aprobación", "90.0%");
                break;

            default:
                datos.put("Sin Datos", "No disponible");
                break;
        }

        return datos;
    }
}
