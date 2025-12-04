package com.edulearn.controller;

import com.edulearn.model.PeriodoAcademico;
import com.edulearn.model.EventoCalendario;
import com.edulearn.patterns.creational.singleton.ConfiguracionSistemaManager;
import com.edulearn.repository.PeriodoAcademicoRepository;
import com.edulearn.repository.EventoCalendarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar la configuración académica centralizada
 * Usa el patrón Singleton (ConfiguracionSistemaManager) para acceso centralizado
 */
@RestController
@RequestMapping("/api/configuracion-academica")
@CrossOrigin(origins = "*")
public class ConfiguracionAcademicaController {

    private static final Logger logger = LoggerFactory.getLogger(ConfiguracionAcademicaController.class);

    @Autowired
    private PeriodoAcademicoRepository periodoRepository;

    @Autowired
    private EventoCalendarioRepository calendarioRepository;

    private ConfiguracionSistemaManager configuracionManager;

    /**
     * Inicializar el Singleton con los repositorios
     */
    @PostConstruct
    public void init() {
        configuracionManager = ConfiguracionSistemaManager.getInstance();
        configuracionManager.setPeriodoRepository(periodoRepository);
        configuracionManager.setCalendarioRepository(calendarioRepository);
        logger.info("🎯 ConfiguracionAcademicaController inicializado con Singleton");
    }

    // ========== ENDPOINTS DE PERÍODOS ACADÉMICOS ==========

    /**
     * GET /api/configuracion-academica/periodo-actual
     * Obtener el período académico actual
     */
    @GetMapping("/periodo-actual")
    public ResponseEntity<?> getPeriodoActual() {
        try {
            PeriodoAcademico periodoActual = configuracionManager.getPeriodoActual();

            if (periodoActual == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "No hay período académico activo"));
            }

            return ResponseEntity.ok(periodoActual);
        } catch (Exception e) {
            logger.error("Error al obtener período actual: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener período actual", "mensaje", e.getMessage()));
        }
    }

    /**
     * GET /api/configuracion-academica/periodos
     * Obtener todos los períodos académicos
     */
    @GetMapping("/periodos")
    public ResponseEntity<?> getTodosPeriodos() {
        try {
            List<PeriodoAcademico> periodos = configuracionManager.getTodosPeriodos();
            return ResponseEntity.ok(periodos);
        } catch (Exception e) {
            logger.error("Error al obtener períodos: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener períodos"));
        }
    }

    /**
     * GET /api/configuracion-academica/periodos/{codigo}
     * Buscar período por código
     */
    @GetMapping("/periodos/{codigo}")
    public ResponseEntity<?> getPeriodoPorCodigo(@PathVariable String codigo) {
        try {
            return configuracionManager.getPeriodoPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            logger.error("Error al buscar período: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al buscar período"));
        }
    }

    /**
     * POST /api/configuracion-academica/periodos
     * Crear nuevo período académico
     */
    @PostMapping("/periodos")
    public ResponseEntity<?> crearPeriodo(@RequestBody PeriodoAcademico periodo) {
        try {
            // Validar que no exista un período con el mismo código
            if (configuracionManager.existePeriodo(periodo.getCodigo())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ya existe un período con el código: " + periodo.getCodigo()));
            }

            PeriodoAcademico guardado = configuracionManager.guardarPeriodo(periodo);
            logger.info("✅ Período académico creado: {}", guardado.getCodigo());

            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (Exception e) {
            logger.error("Error al crear período: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al crear período", "mensaje", e.getMessage()));
        }
    }

    /**
     * PUT /api/configuracion-academica/periodos/{id}
     * Actualizar período académico
     */
    @PutMapping("/periodos/{id}")
    public ResponseEntity<?> actualizarPeriodo(@PathVariable Long id, @RequestBody PeriodoAcademico periodo) {
        try {
            periodo.setId(id);
            PeriodoAcademico actualizado = configuracionManager.guardarPeriodo(periodo);
            logger.info("✅ Período académico actualizado: {}", actualizado.getCodigo());

            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            logger.error("Error al actualizar período: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al actualizar período"));
        }
    }

    /**
     * PUT /api/configuracion-academica/periodo-actual/{id}
     * Establecer un período como actual
     */
    @PutMapping("/periodo-actual/{id}")
    public ResponseEntity<?> setPeriodoActual(@PathVariable Long id) {
        try {
            configuracionManager.setPeriodoActual(id);
            PeriodoAcademico periodo = configuracionManager.getPeriodoActual();

            logger.info("✅ Período actual establecido: {}", periodo.getCodigo());

            return ResponseEntity.ok(Map.of(
                "mensaje", "Período establecido como actual",
                "periodo", periodo
            ));
        } catch (Exception e) {
            logger.error("Error al establecer período actual: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al establecer período actual"));
        }
    }

    /**
     * GET /api/configuracion-academica/inscripciones-abiertas
     * Verificar si hay inscripciones abiertas
     */
    @GetMapping("/inscripciones-abiertas")
    public ResponseEntity<?> verificarInscripcionesAbiertas() {
        try {
            boolean abiertas = configuracionManager.hayInscripcionesAbiertas();
            Map<String, LocalDate> fechas = configuracionManager.getFechasInscripcion();

            return ResponseEntity.ok(Map.of(
                "abiertas", abiertas,
                "fechas", fechas
            ));
        } catch (Exception e) {
            logger.error("Error al verificar inscripciones: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al verificar inscripciones"));
        }
    }

    // ========== ENDPOINTS DE CALENDARIO ACADÉMICO ==========

    /**
     * GET /api/configuracion-academica/calendario
     * Obtener todos los eventos del calendario
     */
    @GetMapping("/calendario")
    public ResponseEntity<?> getTodosEventos() {
        try {
            List<EventoCalendario> eventos = configuracionManager.getTodosEventos();
            return ResponseEntity.ok(eventos);
        } catch (Exception e) {
            logger.error("Error al obtener eventos: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener eventos"));
        }
    }

    /**
     * GET /api/configuracion-academica/calendario/periodo-actual
     * Obtener eventos del período actual
     */
    @GetMapping("/calendario/periodo-actual")
    public ResponseEntity<?> getEventosPeriodoActual() {
        try {
            List<EventoCalendario> eventos = configuracionManager.getEventosPeriodoActual();
            return ResponseEntity.ok(eventos);
        } catch (Exception e) {
            logger.error("Error al obtener eventos del período: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener eventos"));
        }
    }

    /**
     * GET /api/configuracion-academica/calendario/proximos
     * Obtener eventos próximos (próximos 7 días)
     */
    @GetMapping("/calendario/proximos")
    public ResponseEntity<?> getEventosProximos() {
        try {
            List<EventoCalendario> eventos = configuracionManager.getEventosProximos();
            return ResponseEntity.ok(eventos);
        } catch (Exception e) {
            logger.error("Error al obtener eventos próximos: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener eventos próximos"));
        }
    }

    /**
     * GET /api/configuracion-academica/calendario/curso/{cursoId}
     * Obtener eventos de un curso específico
     */
    @GetMapping("/calendario/curso/{cursoId}")
    public ResponseEntity<?> getEventosPorCurso(@PathVariable Integer cursoId) {
        try {
            List<EventoCalendario> eventos = configuracionManager.getEventosPorCurso(cursoId);
            return ResponseEntity.ok(eventos);
        } catch (Exception e) {
            logger.error("Error al obtener eventos del curso: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener eventos del curso"));
        }
    }

    /**
     * GET /api/configuracion-academica/calendario/rango
     * Obtener eventos en un rango de fechas
     */
    @GetMapping("/calendario/rango")
    public ResponseEntity<?> getEventosEnRango(
        @RequestParam String inicio,
        @RequestParam String fin
    ) {
        try {
            LocalDateTime fechaInicio = LocalDateTime.parse(inicio);
            LocalDateTime fechaFin = LocalDateTime.parse(fin);

            List<EventoCalendario> eventos = configuracionManager.getEventosEntreFechas(fechaInicio, fechaFin);
            return ResponseEntity.ok(eventos);
        } catch (Exception e) {
            logger.error("Error al obtener eventos en rango: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener eventos en rango"));
        }
    }

    /**
     * POST /api/configuracion-academica/calendario
     * Crear nuevo evento en el calendario
     */
    @PostMapping("/calendario")
    public ResponseEntity<?> crearEvento(@RequestBody EventoCalendario evento) {
        try {
            EventoCalendario guardado = configuracionManager.guardarEvento(evento);
            logger.info("✅ Evento creado: {}", guardado.getTitulo());

            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (Exception e) {
            logger.error("Error al crear evento: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al crear evento", "mensaje", e.getMessage()));
        }
    }

    /**
     * PUT /api/configuracion-academica/calendario/{id}
     * Actualizar evento del calendario
     */
    @PutMapping("/calendario/{id}")
    public ResponseEntity<?> actualizarEvento(@PathVariable Long id, @RequestBody EventoCalendario evento) {
        try {
            evento.setId(id);
            EventoCalendario actualizado = configuracionManager.guardarEvento(evento);
            logger.info("✅ Evento actualizado: {}", actualizado.getTitulo());

            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            logger.error("Error al actualizar evento: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al actualizar evento"));
        }
    }

    /**
     * DELETE /api/configuracion-academica/calendario/{id}
     * Eliminar evento del calendario
     */
    @DeleteMapping("/calendario/{id}")
    public ResponseEntity<?> eliminarEvento(@PathVariable Long id) {
        try {
            configuracionManager.eliminarEvento(id);
            logger.info("✅ Evento eliminado: {}", id);

            return ResponseEntity.ok(Map.of("mensaje", "Evento eliminado exitosamente"));
        } catch (Exception e) {
            logger.error("Error al eliminar evento: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al eliminar evento"));
        }
    }

    /**
     * POST /api/configuracion-academica/limpiar-cache
     * Limpiar caché del Singleton (forzar recarga)
     */
    @PostMapping("/limpiar-cache")
    public ResponseEntity<?> limpiarCache() {
        try {
            configuracionManager.limpiarCachePeriodos();
            logger.info("✅ Caché limpiada");

            return ResponseEntity.ok(Map.of("mensaje", "Caché limpiada exitosamente"));
        } catch (Exception e) {
            logger.error("Error al limpiar caché: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al limpiar caché"));
        }
    }

    /**
     * GET /api/configuracion-academica/info
     * Obtener información completa de configuración académica
     */
    @GetMapping("/info")
    public ResponseEntity<?> getInformacionCompleta() {
        try {
            Map<String, Object> info = new HashMap<>();

            // Período actual
            PeriodoAcademico periodoActual = configuracionManager.getPeriodoActual();
            info.put("periodoActual", periodoActual);

            // Inscripciones
            info.put("inscripcionesAbiertas", configuracionManager.hayInscripcionesAbiertas());
            info.put("fechasInscripcion", configuracionManager.getFechasInscripcion());

            // Eventos próximos
            info.put("eventosProximos", configuracionManager.getEventosProximos());

            // Estadísticas
            info.put("totalPeriodos", configuracionManager.getTodosPeriodos().size());
            info.put("totalEventos", configuracionManager.getTodosEventos().size());

            return ResponseEntity.ok(info);
        } catch (Exception e) {
            logger.error("Error al obtener información completa: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al obtener información"));
        }
    }
}
