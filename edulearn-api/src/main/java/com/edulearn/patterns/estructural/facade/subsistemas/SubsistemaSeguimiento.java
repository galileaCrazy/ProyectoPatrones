package com.edulearn.patterns.estructural.facade.subsistemas;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Subsistema de Seguimiento (Progreso del Estudiante)
 * Responsable de inicializar el sistema de seguimiento del progreso académico
 * Configura métricas, notificaciones y reportes de avance
 */
@Component
public class SubsistemaSeguimiento {

    // En un sistema real, esto se almacenaría en una tabla de base de datos
    private final Map<String, LocalDateTime> registrosSeguimiento = new HashMap<>();

    /**
     * Inicia el seguimiento del progreso del estudiante en el curso
     * En un sistema real, esto crearía registros de progreso, métricas y notificaciones
     */
    public boolean iniciarSeguimientoProgreso(Integer cursoId, Integer estudianteId) {
        try {
            String claveRegistro = estudianteId + "-" + cursoId;

            // Simular creación de registro de seguimiento
            registrosSeguimiento.put(claveRegistro, LocalDateTime.now());

            System.out.println("📊 Iniciado seguimiento de progreso para estudiante " + estudianteId +
                             " en curso " + cursoId);

            // En un sistema real, aquí se inicializarían:
            // 1. Registro de progreso con valores en cero
            // 2. Notificaciones programadas
            // 3. Recordatorios de fechas límite
            // 4. Dashboard de métricas
            // 5. Sistema de badges/logros

            inicializarMetricas(estudianteId, cursoId);
            configurarNotificaciones(estudianteId, cursoId);

            return true;
        } catch (Exception e) {
            System.err.println("❌ Error al iniciar seguimiento: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inicializa las métricas de progreso del estudiante
     */
    private void inicializarMetricas(Integer estudianteId, Integer cursoId) {
        // En un sistema real, esto crearía registros en tabla de métricas
        System.out.println("  ✓ Métricas inicializadas:");
        System.out.println("    - Progreso de materiales: 0%");
        System.out.println("    - Evaluaciones completadas: 0");
        System.out.println("    - Tiempo dedicado: 0 horas");
        System.out.println("    - Último acceso: " + LocalDateTime.now());
    }

    /**
     * Configura el sistema de notificaciones para el estudiante
     */
    private void configurarNotificaciones(Integer estudianteId, Integer cursoId) {
        // En un sistema real, esto programaría notificaciones en el sistema
        System.out.println("  ✓ Notificaciones configuradas:");
        System.out.println("    - Bienvenida al curso");
        System.out.println("    - Recordatorios de materiales");
        System.out.println("    - Alertas de evaluaciones próximas");
    }

    /**
     * Obtiene el estado actual del seguimiento
     */
    public Map<String, Object> obtenerEstadoSeguimiento(Integer estudianteId, Integer cursoId) {
        Map<String, Object> estado = new HashMap<>();
        String claveRegistro = estudianteId + "-" + cursoId;

        if (registrosSeguimiento.containsKey(claveRegistro)) {
            estado.put("activo", true);
            estado.put("fechaInicio", registrosSeguimiento.get(claveRegistro));
            estado.put("progresoMateriales", 0.0);
            estado.put("evaluacionesCompletadas", 0);
        } else {
            estado.put("activo", false);
        }

        return estado;
    }

    /**
     * Verifica si existe un registro de seguimiento activo
     */
    public boolean existeSeguimiento(Integer estudianteId, Integer cursoId) {
        String claveRegistro = estudianteId + "-" + cursoId;
        return registrosSeguimiento.containsKey(claveRegistro);
    }
}
