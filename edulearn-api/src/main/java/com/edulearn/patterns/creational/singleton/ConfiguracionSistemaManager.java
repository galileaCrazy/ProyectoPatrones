package com.edulearn.patterns.creational.singleton;

import com.edulearn.model.ConfiguracionSistema;
import com.edulearn.repository.ConfiguracionSistemaRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PATRÓN SINGLETON
 * ================
 * Propósito: Garantizar que una clase tenga una única instancia y proporcionar
 * un punto de acceso global a ella.
 *
 * Uso en EduLearn: Gestionar configuraciones del sistema de forma centralizada,
 * asegurando que todos los componentes accedan a la misma instancia.
 *
 * Ventajas:
 * - Control estricto sobre la instancia única
 * - Acceso global a las configuraciones
 * - Inicialización perezosa (lazy initialization)
 * - Thread-safe con sincronización
 */
@Component
public class ConfiguracionSistemaManager {

    // Instancia única (singleton)
    private static ConfiguracionSistemaManager instancia;

    // Caché en memoria para configuraciones (thread-safe)
    private final Map<String, String> cache;

    // Referencia al repositorio
    private ConfiguracionSistemaRepository repository;

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
     * Inyectar el repositorio después de la creación
     * (Spring no puede inyectar en constructores privados)
     */
    public void setRepository(ConfiguracionSistemaRepository repository) {
        this.repository = repository;
        cargarConfiguracionesDesdeDB();
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
}
