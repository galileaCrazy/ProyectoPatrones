package com.edulearn.patterns.creational.singleton;

import com.edulearn.model.ConfiguracionSistema;
import com.edulearn.model.PeriodoAcademico;
import com.edulearn.model.EventoCalendario;
import com.edulearn.repository.ConfiguracionSistemaRepository;
import com.edulearn.repository.PeriodoAcademicoRepository;
import com.edulearn.repository.EventoCalendarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PATRÓN SINGLETON
 * ================
 * Propósito: Garantizar que una clase tenga una única instancia y proporcionar
 * un punto de acceso global a ella.
 *
 * Uso en EduLearn: Gestionar configuraciones del sistema de forma centralizada,
 * incluyendo:
 * - Configuraciones generales del sistema
 * - Períodos académicos
 * - Calendario académico
 *
 * Ventajas:
 * - Control estricto sobre la instancia única
 * - Acceso global centralizado a configuraciones, períodos y calendario
 * - Inicialización perezosa (lazy initialization)
 * - Thread-safe con sincronización
 * - Caché en memoria para optimizar accesos frecuentes
 */
@Component
public class ConfiguracionSistemaManager {

    private static final Logger logger = LoggerFactory.getLogger(ConfiguracionSistemaManager.class);

    // Instancia única (singleton)
    private static ConfiguracionSistemaManager instancia;

    // Caché en memoria para configuraciones (thread-safe)
    private final Map<String, String> cache;

    // Caché para período académico actual
    private PeriodoAcademico periodoActualCache;
    private LocalDateTime ultimaActualizacionPeriodo;

    // Referencias a repositorios
    private ConfiguracionSistemaRepository repository;
    private PeriodoAcademicoRepository periodoRepository;
    private EventoCalendarioRepository calendarioRepository;

    /**
     * Constructor privado para evitar instanciación externa
     */
    private ConfiguracionSistemaManager() {
        this.cache = new ConcurrentHashMap<>();
    }

    /**
     * Obtener la instancia única (Double-Checked Locking)
     * Thread-safe sin sacrificar rendimiento
     */
    public static ConfiguracionSistemaManager getInstance() {
        if (instancia == null) {
            synchronized (ConfiguracionSistemaManager.class) {
                if (instancia == null) {
                    instancia = new ConfiguracionSistemaManager();
                }
            }
        }
        return instancia;
    }

    /**
     * Inyectar los repositorios después de la creación
     * (Spring no puede inyectar en constructores privados)
     */
    public void setRepository(ConfiguracionSistemaRepository repository) {
        this.repository = repository;
        cargarConfiguracionesDesdeDB();
    }

    public void setPeriodoRepository(PeriodoAcademicoRepository periodoRepository) {
        this.periodoRepository = periodoRepository;
        logger.info("📅 Repositorio de períodos académicos configurado");
    }

    public void setCalendarioRepository(EventoCalendarioRepository calendarioRepository) {
        this.calendarioRepository = calendarioRepository;
        logger.info("📆 Repositorio de eventos de calendario configurado");
    }

    /**
     * Cargar todas las configuraciones desde BD a caché
     */
    private void cargarConfiguracionesDesdeDB() {
        if (repository != null) {
            List<ConfiguracionSistema> configs = repository.findAll();
            for (ConfiguracionSistema config : configs) {
                cache.put(config.getClave(), config.getValor());
            }

            // Si no hay configuraciones, cargar valores por defecto
            if (cache.isEmpty()) {
                cargarConfiguracionesPorDefecto();
            }
        }
    }

    /**
     * Cargar configuraciones por defecto
     */
    private void cargarConfiguracionesPorDefecto() {
        Map<String, ConfiguracionSistema> defaults = new HashMap<>();

        defaults.put("nombre_sistema", new ConfiguracionSistema(
            "nombre_sistema",
            "EduLearn Platform",
            "Nombre del sistema LMS",
            "STRING"
        ));

        defaults.put("version", new ConfiguracionSistema(
            "version",
            "1.0.0",
            "Versión del sistema",
            "STRING"
        ));

        defaults.put("max_intentos_login", new ConfiguracionSistema(
            "max_intentos_login",
            "3",
            "Máximo de intentos de login antes de bloqueo",
            "INTEGER"
        ));

        defaults.put("duracion_sesion_minutos", new ConfiguracionSistema(
            "duracion_sesion_minutos",
            "60",
            "Duración de la sesión en minutos",
            "INTEGER"
        ));

        defaults.put("cupo_default", new ConfiguracionSistema(
            "cupo_default",
            "30",
            "Cupo por defecto para nuevos cursos",
            "INTEGER"
        ));

        defaults.put("calificacion_minima_aprobacion", new ConfiguracionSistema(
            "calificacion_minima_aprobacion",
            "60",
            "Calificación mínima para aprobar (0-100)",
            "INTEGER"
        ));

        defaults.put("permitir_registro_estudiantes", new ConfiguracionSistema(
            "permitir_registro_estudiantes",
            "true",
            "Permitir auto-registro de estudiantes",
            "BOOLEAN"
        ));

        defaults.put("modo_mantenimiento", new ConfiguracionSistema(
            "modo_mantenimiento",
            "false",
            "Sistema en modo mantenimiento",
            "BOOLEAN"
        ));

        defaults.put("email_notificaciones", new ConfiguracionSistema(
            "email_notificaciones",
            "noreply@edulearn.com",
            "Email para envío de notificaciones",
            "STRING"
        ));

        defaults.put("url_base", new ConfiguracionSistema(
            "url_base",
            "http://localhost:3000",
            "URL base del frontend",
            "STRING"
        ));

        // Guardar en BD
        if (repository != null) {
            for (ConfiguracionSistema config : defaults.values()) {
                if (!repository.existsByClave(config.getClave())) {
                    repository.save(config);
                    cache.put(config.getClave(), config.getValor());
                }
            }
        }
    }

    /**
     * Obtener una configuración por clave
     */
    public String getConfiguracion(String clave) {
        // Primero buscar en caché
        String valor = cache.get(clave);

        // Si no está en caché, buscar en BD
        if (valor == null && repository != null) {
            ConfiguracionSistema config = repository.findByClave(clave).orElse(null);
            if (config != null) {
                valor = config.getValor();
                cache.put(clave, valor);
            }
        }

        return valor != null ? valor : "";
    }

    /**
     * Establecer una configuración
     */
    public void setConfiguracion(String clave, String valor) {
        // Actualizar caché
        cache.put(clave, valor);

        // Actualizar en BD
        if (repository != null) {
            ConfiguracionSistema config = repository.findByClave(clave)
                .orElse(new ConfiguracionSistema(clave, valor, "", "STRING"));

            config.setValor(valor);
            repository.save(config);
        }
    }

    /**
     * Obtener todas las configuraciones
     */
    public Map<String, String> obtenerTodasLasConfiguraciones() {
        return new HashMap<>(cache);
    }

    /**
     * Actualizar múltiples configuraciones
     */
    public void actualizarConfiguraciones(Map<String, String> nuevasConfiguraciones) {
        for (Map.Entry<String, String> entry : nuevasConfiguraciones.entrySet()) {
            setConfiguracion(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Obtener configuración como entero
     */
    public int getConfiguracionInt(String clave, int valorPorDefecto) {
        try {
            String valor = getConfiguracion(clave);
            return valor.isEmpty() ? valorPorDefecto : Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            return valorPorDefecto;
        }
    }

    /**
     * Obtener configuración como booleano
     */
    public boolean getConfiguracionBoolean(String clave, boolean valorPorDefecto) {
        String valor = getConfiguracion(clave);
        return valor.isEmpty() ? valorPorDefecto : Boolean.parseBoolean(valor);
    }

    /**
     * Recargar configuraciones desde BD
     */
    public void recargarConfiguraciones() {
        cache.clear();
        cargarConfiguracionesDesdeDB();
    }

    /**
     * Limpiar caché
     */
    public void limpiarCache() {
        cache.clear();
    }

    /**
     * Obtener cantidad de configuraciones en caché
     */
    public int getCantidadConfiguraciones() {
        return cache.size();
    }

    // ========== GESTIÓN DE PERÍODOS ACADÉMICOS ==========

    /**
     * Obtener el período académico actual
     */
    public PeriodoAcademico getPeriodoActual() {
        // Si tenemos caché y es reciente (menos de 5 minutos), usarlo
        if (periodoActualCache != null && ultimaActualizacionPeriodo != null) {
            if (LocalDateTime.now().minusMinutes(5).isBefore(ultimaActualizacionPeriodo)) {
                return periodoActualCache;
            }
        }

        // Buscar en BD
        if (periodoRepository != null) {
            List<PeriodoAcademico> periodosActuales = periodoRepository.findByEsActual(true);
            if (!periodosActuales.isEmpty()) {
                periodoActualCache = periodosActuales.get(0);
                ultimaActualizacionPeriodo = LocalDateTime.now();
                logger.info("✅ Período actual cargado: {}", periodoActualCache.getCodigo());
                return periodoActualCache;
            }
        }

        logger.warn("⚠️ No se encontró período académico actual");
        return null;
    }

    /**
     * Establecer un período como actual
     */
    public synchronized void setPeriodoActual(Long periodoId) {
        if (periodoRepository == null) {
            logger.error("❌ Repositorio de períodos no configurado");
            return;
        }

        // Desmarcar todos los períodos actuales
        List<PeriodoAcademico> periodosActivos = periodoRepository.findByEsActual(true);
        for (PeriodoAcademico p : periodosActivos) {
            p.setEsActual(false);
            periodoRepository.save(p);
        }

        // Marcar el nuevo período como actual
        Optional<PeriodoAcademico> nuevoPeriodo = periodoRepository.findById(periodoId);
        if (nuevoPeriodo.isPresent()) {
            PeriodoAcademico periodo = nuevoPeriodo.get();
            periodo.setEsActual(true);
            periodo.setEstado("ACTIVO");
            periodoRepository.save(periodo);

            // Actualizar caché
            periodoActualCache = periodo;
            ultimaActualizacionPeriodo = LocalDateTime.now();

            logger.info("✅ Período actual establecido: {}", periodo.getCodigo());
        }
    }

    /**
     * Obtener todos los períodos académicos
     */
    public List<PeriodoAcademico> getTodosPeriodos() {
        if (periodoRepository != null) {
            return periodoRepository.findAllByOrderByFechaInicioDesc();
        }
        return List.of();
    }

    /**
     * Buscar período por código
     */
    public Optional<PeriodoAcademico> getPeriodoPorCodigo(String codigo) {
        if (periodoRepository != null) {
            return periodoRepository.findByCodigo(codigo);
        }
        return Optional.empty();
    }

    /**
     * Crear o actualizar período académico
     */
    public PeriodoAcademico guardarPeriodo(PeriodoAcademico periodo) {
        if (periodoRepository != null) {
            PeriodoAcademico guardado = periodoRepository.save(periodo);
            logger.info("💾 Período guardado: {}", guardado.getCodigo());

            // Si es el período actual, actualizar caché
            if (guardado.getEsActual()) {
                periodoActualCache = guardado;
                ultimaActualizacionPeriodo = LocalDateTime.now();
            }

            return guardado;
        }
        return periodo;
    }

    /**
     * Verificar si un período existe
     */
    public boolean existePeriodo(String codigo) {
        if (periodoRepository != null) {
            return periodoRepository.existsByCodigo(codigo);
        }
        return false;
    }

    // ========== GESTIÓN DE CALENDARIO ACADÉMICO ==========

    /**
     * Obtener todos los eventos del calendario
     */
    public List<EventoCalendario> getTodosEventos() {
        if (calendarioRepository != null) {
            return calendarioRepository.findAllByOrderByFechaInicioAsc();
        }
        return List.of();
    }

    /**
     * Obtener eventos en un rango de fechas
     */
    public List<EventoCalendario> getEventosEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
        if (calendarioRepository != null) {
            return calendarioRepository.findEventosEntreFechas(inicio, fin);
        }
        return List.of();
    }

    /**
     * Obtener eventos del período actual
     */
    public List<EventoCalendario> getEventosPeriodoActual() {
        PeriodoAcademico periodoActual = getPeriodoActual();
        if (periodoActual != null && calendarioRepository != null) {
            return calendarioRepository.findByPeriodoAcademico(periodoActual);
        }
        return List.of();
    }

    /**
     * Obtener eventos de un curso específico
     */
    public List<EventoCalendario> getEventosPorCurso(Integer cursoId) {
        if (calendarioRepository != null) {
            return calendarioRepository.findByCursoId(cursoId);
        }
        return List.of();
    }

    /**
     * Obtener eventos de un curso en un rango de fechas
     */
    public List<EventoCalendario> getEventosPorCursoEntreFechas(Integer cursoId, LocalDateTime inicio, LocalDateTime fin) {
        if (calendarioRepository != null) {
            return calendarioRepository.findEventosPorCursoEntreFechas(cursoId, inicio, fin);
        }
        return List.of();
    }

    /**
     * Crear o actualizar evento del calendario
     */
    public EventoCalendario guardarEvento(EventoCalendario evento) {
        if (calendarioRepository != null) {
            EventoCalendario guardado = calendarioRepository.save(evento);
            logger.info("📆 Evento guardado: {} - {}", guardado.getTitulo(), guardado.getFechaInicio());
            return guardado;
        }
        return evento;
    }

    /**
     * Eliminar evento del calendario
     */
    public void eliminarEvento(Long eventoId) {
        if (calendarioRepository != null) {
            calendarioRepository.deleteById(eventoId);
            logger.info("🗑️ Evento eliminado: {}", eventoId);
        }
    }

    /**
     * Obtener eventos próximos (próximos 7 días)
     */
    public List<EventoCalendario> getEventosProximos() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime en7Dias = ahora.plusDays(7);
        return getEventosEntreFechas(ahora, en7Dias);
    }

    /**
     * Verificar si hay inscripciones abiertas
     */
    public boolean hayInscripcionesAbiertas() {
        PeriodoAcademico periodoActual = getPeriodoActual();
        return periodoActual != null && periodoActual.inscripcionesAbiertas();
    }

    /**
     * Obtener fechas de inscripción del período actual
     */
    public Map<String, LocalDate> getFechasInscripcion() {
        Map<String, LocalDate> fechas = new HashMap<>();
        PeriodoAcademico periodoActual = getPeriodoActual();

        if (periodoActual != null) {
            fechas.put("inicio", periodoActual.getFechaInicioInscripciones());
            fechas.put("fin", periodoActual.getFechaFinInscripciones());
        }

        return fechas;
    }

    /**
     * Limpiar caché de períodos (forzar recarga)
     */
    public void limpiarCachePeriodos() {
        periodoActualCache = null;
        ultimaActualizacionPeriodo = null;
        logger.info("🗑️ Caché de períodos limpiada");
    }
}
