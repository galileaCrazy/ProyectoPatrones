package com.edulearn.controller;

import com.edulearn.model.Curso;
import com.edulearn.model.Usuario;
import com.edulearn.patterns.comportamiento.chain_of_responsibility.*;
import com.edulearn.patterns.creational.builder.CursoBuilder;
import com.edulearn.repository.CursoRepository;
import com.edulearn.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

/**
 * Controlador para la creación y validación de cursos usando Chain of Responsibility.
 * 
 * Este controlador integra el patrón Chain of Responsibility para:
 * 1. Validar permisos según el rol del usuario
 * 2. Validar el periodo académico según la fecha actual
 * 3. Auto-asignar profesor cuando es el propio profesor quien crea el curso
 * 
 * Estructura del patrón (siguiendo diagrama UML clásico):
 * 
 *              ┌──────────┐
 *              │ Cliente  │
 *              └────┬─────┘
 *                   │
 *              ┌────▼─────┐
 *         0..1 │ Gestor   │ #siguiente
 *         ┌────┤(abstract)├────┐
 *         │    └────┬─────┘    │
 *         │         │          │
 *    ┌────▼────┐  ┌─▼──────┐  ┌▼─────────┐
 *    │Gestor   │  │Gestor  │  │Gestor    │
 *    │Concreto1│  │Conc.2  │  │Concreto3 │
 *    └─────────┘  └────────┘  └──────────┘
 */
@RestController
@RequestMapping("/api/validacion")
@CrossOrigin(origins = "*")
public class ValidacionCursoController {

    @Autowired
    private CadenaValidacionService cadenaValidacionService;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * POST /api/validacion/curso/crear
     * 
     * Endpoint principal para crear un curso con validación completa
     * usando el patrón Chain of Responsibility.
     * 
     * La cadena de validación es:
     * Token -> Rol -> Permisos -> Periodo Académico -> Auto-Asignación Profesor
     * 
     * @param request Datos del curso y del usuario
     * @return Curso creado o error de validación
     */
    @PostMapping("/curso/crear")
    public ResponseEntity<Map<String, Object>> crearCursoValidado(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        System.out.println("\n" + "═".repeat(70));
        System.out.println("🔗 CHAIN OF RESPONSIBILITY - Creación de Curso");
        System.out.println("═".repeat(70));

        try {
            // Extraer datos del usuario
            Integer usuarioId = extractInteger(request, "usuarioId");
            String tipoUsuario = (String) request.get("tipoUsuario");
            String token = (String) request.getOrDefault("token", "TOKEN_DEMO_" + usuarioId);

            // Extraer datos del curso
            String nombre = (String) request.get("nombre");
            String descripcion = (String) request.get("descripcion");
            String tipoCurso = (String) request.getOrDefault("tipoCurso", "Virtual");
            String periodoAcademico = (String) request.get("periodoAcademico");
            Integer profesorTitularId = extractInteger(request, "profesorTitularId");
            Integer duracion = extractInteger(request, "duracion");

            // Crear la solicitud de validación
            SolicitudValidacion solicitud = new SolicitudValidacion(
                token,
                "/api/cursos/crear",
                "crear"
            );
            solicitud.setTipoUsuario(tipoUsuario);

            // Agregar metadatos para la cadena de validación
            solicitud.agregarMetadato("usuarioId", usuarioId);
            solicitud.agregarMetadato("periodoAcademico", periodoAcademico);
            
            // Solo agregar profesorTitularId si viene (administrador lo asigna)
            if (profesorTitularId != null) {
                solicitud.agregarMetadato("profesorTitularId", profesorTitularId);
            }

            System.out.println("\n📋 Datos recibidos:");
            System.out.println("   • Usuario ID: " + usuarioId);
            System.out.println("   • Tipo Usuario: " + tipoUsuario);
            System.out.println("   • Curso: " + nombre);
            System.out.println("   • Periodo: " + periodoAcademico);
            System.out.println("   • Profesor asignado: " + (profesorTitularId != null ? profesorTitularId : "Auto-asignar"));

            // ═══════════════════════════════════════════════════════════════
            // EJECUTAR CADENA DE RESPONSABILIDAD
            // ═══════════════════════════════════════════════════════════════
            System.out.println("\n🔗 Iniciando cadena de validación...\n");
            
            boolean validacionExitosa = cadenaValidacionService.validarCreacionCurso(solicitud);

            if (!validacionExitosa) {
                // La cadena rechazó la solicitud
                response.put("success", false);
                response.put("error", solicitud.getMensajeError());
                response.put("patronAplicado", "Chain of Responsibility");
                response.put("gestorQueRechazo", obtenerGestorQueRechazo(solicitud.getMensajeError()));
                
                System.out.println("\n❌ Solicitud rechazada por la cadena");
                System.out.println("   Razón: " + solicitud.getMensajeError());
                
                return ResponseEntity.badRequest().body(response);
            }

            // ═══════════════════════════════════════════════════════════════
            // VALIDACIÓN EXITOSA - Crear el curso
            // ═══════════════════════════════════════════════════════════════
            System.out.println("\n✅ Todas las validaciones pasaron");

            // Obtener el profesor titular (puede haber sido auto-asignado)
            Integer profesorFinal = (Integer) solicitud.getMetadatos().get("profesorTitularId");
            Boolean fueAutoAsignado = (Boolean) solicitud.getMetadatos().getOrDefault("autoAsignado", false);

            // Construir el curso usando el patrón Builder
            CursoBuilder builder = new CursoBuilder()
                .setNombre(nombre)
                .setDescripcion(descripcion)
                .setTipoCurso(tipoCurso)
                .setPeriodoAcademico(periodoAcademico)
                .setProfesorTitularId(profesorFinal)
                .setEstado("activo");

            if (duracion != null) {
                builder.setDuracion(duracion);
            }

            Curso curso = builder.build();
            Curso cursoGuardado = cursoRepository.save(curso);

            // Preparar respuesta exitosa
            response.put("success", true);
            response.put("curso", cursoGuardado);
            response.put("mensaje", "Curso creado exitosamente");
            response.put("patronesAplicados", Arrays.asList(
                "Chain of Responsibility (validación)",
                "Builder (construcción del curso)"
            ));

            // Información de la cadena
            Map<String, Object> cadenaInfo = new HashMap<>();
            cadenaInfo.put("gestoresEjecutados", Arrays.asList(
                "GestorValidacionToken",
                "GestorValidacionRol",
                "GestorValidacionPermisos",
                "GestorValidacionPeriodoAcademico",
                "GestorValidacionAutoAsignacionProfesor"
            ));
            cadenaInfo.put("periodoValidado", solicitud.getMetadatos().get("periodoValidado"));
            cadenaInfo.put("profesorTitularId", profesorFinal);
            cadenaInfo.put("profesorAutoAsignado", fueAutoAsignado);
            response.put("cadenaValidacion", cadenaInfo);

            System.out.println("\n🎉 Curso creado exitosamente:");
            System.out.println("   • ID: " + cursoGuardado.getId());
            System.out.println("   • Nombre: " + cursoGuardado.getNombre());
            System.out.println("   • Periodo: " + cursoGuardado.getPeriodoAcademico());
            System.out.println("   • Profesor: " + profesorFinal + (fueAutoAsignado ? " (auto-asignado)" : ""));
            System.out.println("═".repeat(70) + "\n");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error interno: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(response);
        }
    }

    /**
     * GET /api/validacion/periodos-validos
     * 
     * Retorna los periodos académicos válidos según la fecha actual.
     * Esto permite que el frontend muestre solo las opciones permitidas.
     */
    @GetMapping("/periodos-validos")
    public ResponseEntity<Map<String, Object>> obtenerPeriodosValidos() {
        Map<String, Object> response = new HashMap<>();

        LocalDate fechaActual = LocalDate.now();
        int añoActual = fechaActual.getYear();
        Month mesActual = fechaActual.getMonth();

        List<Map<String, Object>> periodosValidos = new ArrayList<>();

        // Lógica del patrón Chain of Responsibility (GestorValidacionPeriodoAcademico)
        if (mesActual.getValue() >= Month.JANUARY.getValue() && 
            mesActual.getValue() <= Month.JUNE.getValue()) {
            // Enero-Junio: se pueden crear cursos para Ago-Dic actual y Ene-Jun/Ago-Dic siguiente
            periodosValidos.add(crearPeriodo("Agosto-Diciembre " + añoActual, "AGO-DIC", añoActual));
            periodosValidos.add(crearPeriodo("Enero-Junio " + (añoActual + 1), "ENE-JUN", añoActual + 1));
            periodosValidos.add(crearPeriodo("Agosto-Diciembre " + (añoActual + 1), "AGO-DIC", añoActual + 1));
        } else if (mesActual == Month.JULY) {
            // Julio: mismo que Enero-Junio
            periodosValidos.add(crearPeriodo("Agosto-Diciembre " + añoActual, "AGO-DIC", añoActual));
            periodosValidos.add(crearPeriodo("Enero-Junio " + (añoActual + 1), "ENE-JUN", añoActual + 1));
            periodosValidos.add(crearPeriodo("Agosto-Diciembre " + (añoActual + 1), "AGO-DIC", añoActual + 1));
        } else {
            // Agosto-Diciembre: solo periodos del siguiente año
            periodosValidos.add(crearPeriodo("Enero-Junio " + (añoActual + 1), "ENE-JUN", añoActual + 1));
            periodosValidos.add(crearPeriodo("Agosto-Diciembre " + (añoActual + 1), "AGO-DIC", añoActual + 1));
        }

        response.put("periodosValidos", periodosValidos);
        response.put("fechaActual", fechaActual.toString());
        response.put("mesActual", mesActual.toString());
        response.put("añoActual", añoActual);
        response.put("patronAplicado", "Chain of Responsibility - GestorValidacionPeriodoAcademico");

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/validacion/configuracion-formulario/{tipoUsuario}
     * 
     * Retorna la configuración del formulario de creación de cursos
     * según el tipo de usuario. Esto implementa las reglas de:
     * - Profesor: no ve campo "asignar profesor" (se auto-asigna)
     * - Administrador: ve todos los campos incluyendo selección de profesor
     * - Estudiante: no tiene acceso (la cadena lo rechazará)
     */
    @GetMapping("/configuracion-formulario/{tipoUsuario}")
    public ResponseEntity<Map<String, Object>> obtenerConfiguracionFormulario(
            @PathVariable String tipoUsuario) {
        
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> campos = new ArrayList<>();

        // Campos comunes para todos
        campos.add(crearCampoConfig("nombre", "Nombre del Curso", "text", true, null));
        campos.add(crearCampoConfig("descripcion", "Descripción", "textarea", true, null));
        campos.add(crearCampoConfig("tipoCurso", "Tipo de Curso", "select", true, 
            Arrays.asList("Virtual", "Presencial", "Híbrido")));
        campos.add(crearCampoConfig("periodoAcademico", "Periodo Académico", "select", true, null));
        campos.add(crearCampoConfig("duracion", "Duración (horas)", "number", false, null));

        boolean puedeCrearCurso = false;
        boolean mostrarSelectorProfesor = false;
        String mensajeAutoAsignacion = null;

        switch (tipoUsuario.toLowerCase()) {
            case "profesor":
                puedeCrearCurso = true;
                mostrarSelectorProfesor = false;
                mensajeAutoAsignacion = "Serás asignado automáticamente como profesor titular de este curso";
                break;
            case "administrador":
                puedeCrearCurso = true;
                mostrarSelectorProfesor = true;
                // Agregar campo de selección de profesor para administradores
                campos.add(crearCampoConfig("profesorTitularId", "Profesor Titular", "select-profesor", true, null));
                break;
            case "estudiante":
                puedeCrearCurso = false;
                break;
            default:
                puedeCrearCurso = false;
        }

        response.put("campos", campos);
        response.put("puedeCrearCurso", puedeCrearCurso);
        response.put("mostrarSelectorProfesor", mostrarSelectorProfesor);
        response.put("mensajeAutoAsignacion", mensajeAutoAsignacion);
        response.put("tipoUsuario", tipoUsuario);
        response.put("patronAplicado", "Chain of Responsibility - Configuración dinámica según rol");

        // Validaciones que aplica la cadena
        List<String> validacionesAplicadas = new ArrayList<>();
        validacionesAplicadas.add("GestorValidacionToken - Verifica autenticación");
        validacionesAplicadas.add("GestorValidacionRol - Verifica rol tiene acceso");
        validacionesAplicadas.add("GestorValidacionPermisos - Verifica permiso 'crear_curso'");
        validacionesAplicadas.add("GestorValidacionPeriodoAcademico - Valida fechas del periodo");
        validacionesAplicadas.add("GestorValidacionAutoAsignacionProfesor - Auto-asigna o valida profesor");
        response.put("validacionesCadena", validacionesAplicadas);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/validacion/profesores
     * 
     * Lista los profesores disponibles para asignar (solo para administradores)
     */
    @GetMapping("/profesores")
    public ResponseEntity<List<Map<String, Object>>> listarProfesores() {
        List<Usuario> profesores = usuarioRepository.findByTipoUsuario("profesor");
        
        List<Map<String, Object>> resultado = new ArrayList<>();
        for (Usuario profesor : profesores) {
            Map<String, Object> p = new HashMap<>();
            p.put("id", profesor.getId());
            p.put("nombre", profesor.getNombre());
            p.put("email", profesor.getEmail());
            resultado.add(p);
        }

        return ResponseEntity.ok(resultado);
    }

    /**
     * POST /api/validacion/validar-periodo
     * 
     * Valida un periodo académico específico sin crear el curso.
     * Útil para validación en tiempo real en el frontend.
     */
    @PostMapping("/validar-periodo")
    public ResponseEntity<Map<String, Object>> validarPeriodo(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        String periodoAcademico = request.get("periodoAcademico");
        
        SolicitudValidacion solicitud = new SolicitudValidacion("TOKEN_VALIDACION", "/api/cursos", "crear");
        solicitud.setTipoUsuario("profesor"); // Simular para solo validar periodo
        solicitud.agregarMetadato("periodoAcademico", periodoAcademico);
        solicitud.agregarMetadato("usuarioId", 1);

        // Crear instancia del gestor para validar solo el periodo
        GestorValidacionPeriodoAcademico gestorPeriodo = new GestorValidacionPeriodoAcademico();
        
        // Crear solicitud específica para validar periodo
        boolean esValido = validarPeriodoDirecto(periodoAcademico);
        
        if (esValido) {
            response.put("valido", true);
            response.put("mensaje", "El periodo '" + periodoAcademico + "' es válido para crear cursos");
        } else {
            response.put("valido", false);
            response.put("mensaje", "El periodo '" + periodoAcademico + "' no es válido. Debe seleccionar un periodo futuro.");
            response.put("periodosValidos", GestorValidacionPeriodoAcademico.obtenerPeriodosValidos());
        }

        return ResponseEntity.ok(response);
    }

    // ═══════════════════════════════════════════════════════════════
    // MÉTODOS AUXILIARES
    // ═══════════════════════════════════════════════════════════════

    private Integer extractInteger(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return null;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Number) return ((Number) value).intValue();
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Map<String, Object> crearPeriodo(String nombre, String codigo, int año) {
        Map<String, Object> periodo = new HashMap<>();
        periodo.put("nombre", nombre);
        periodo.put("codigo", codigo);
        periodo.put("año", año);
        periodo.put("valor", nombre);
        return periodo;
    }

    private Map<String, Object> crearCampoConfig(String id, String label, String tipo, 
                                                  boolean requerido, List<String> opciones) {
        Map<String, Object> campo = new HashMap<>();
        campo.put("id", id);
        campo.put("label", label);
        campo.put("tipo", tipo);
        campo.put("requerido", requerido);
        if (opciones != null) {
            campo.put("opciones", opciones);
        }
        return campo;
    }

    private String obtenerGestorQueRechazo(String mensajeError) {
        if (mensajeError == null) return "Desconocido";
        
        if (mensajeError.contains("token") || mensajeError.contains("Token")) {
            return "GestorValidacionToken";
        }
        if (mensajeError.contains("rol") || mensajeError.contains("Rol")) {
            return "GestorValidacionRol";
        }
        if (mensajeError.contains("permiso") || mensajeError.contains("Permiso")) {
            return "GestorValidacionPermisos";
        }
        if (mensajeError.contains("periodo") || mensajeError.contains("Periodo") || 
            mensajeError.contains("académico") || mensajeError.contains("curso")) {
            return "GestorValidacionPeriodoAcademico";
        }
        if (mensajeError.contains("profesor") || mensajeError.contains("Profesor") || 
            mensajeError.contains("asignar") || mensajeError.contains("titular")) {
            return "GestorValidacionAutoAsignacionProfesor";
        }
        return "GestorDesconocido";
    }

    private boolean validarPeriodoDirecto(String periodoAcademico) {
        if (periodoAcademico == null || periodoAcademico.trim().isEmpty()) {
            return false;
        }

        String[] periodosValidos = GestorValidacionPeriodoAcademico.obtenerPeriodosValidos();
        for (String pv : periodosValidos) {
            if (pv.equalsIgnoreCase(periodoAcademico.trim())) {
                return true;
            }
        }
        return false;
    }
}
