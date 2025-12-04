package com.edulearn.patterns.estructural.facade.subsistemas;

import com.edulearn.model.Evaluacion;
import com.edulearn.repository.EvaluacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Subsistema de Evaluación
 * Responsable de preparar y configurar las evaluaciones del curso para el estudiante
 * Inicializa el sistema de evaluación y calificaciones
 */
@Component
public class SubsistemaEvaluacion {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    /**
     * Prepara las evaluaciones del curso para el estudiante
     * En un sistema real, esto podría crear registros de asignación de evaluaciones
     * @return true si hay evaluaciones disponibles
     */
    public boolean prepararEvaluacionesCurso(Integer cursoId, Integer estudianteId) {
        // Obtener todas las evaluaciones activas del curso
        List<Evaluacion> evaluaciones = obtenerEvaluacionesPorCurso(cursoId);

        if (evaluaciones.isEmpty()) {
            // No hay evaluaciones, pero no es un error crítico
            System.out.println("⚠️ El curso " + cursoId + " no tiene evaluaciones configuradas");
            return false;
        }

        // En un sistema real, aquí se crearían registros de asignación de evaluaciones al estudiante
        System.out.println("📝 Preparadas " + evaluaciones.size() + " evaluaciones para el estudiante " + estudianteId);

        // Registrar evaluaciones por tipo
        long examenes = evaluaciones.stream()
                .filter(e -> "EXAMEN".equalsIgnoreCase(e.getTipoEvaluacion()))
                .count();
        long quizzes = evaluaciones.stream()
                .filter(e -> "QUIZ".equalsIgnoreCase(e.getTipoEvaluacion()))
                .count();
        long proyectos = evaluaciones.stream()
                .filter(e -> "PROYECTO".equalsIgnoreCase(e.getTipoEvaluacion()))
                .count();

        System.out.println("  - Exámenes: " + examenes);
        System.out.println("  - Quizzes: " + quizzes);
        System.out.println("  - Proyectos: " + proyectos);

        return true;
    }

    /**
     * Obtiene todas las evaluaciones activas de un curso
     */
    public List<Evaluacion> obtenerEvaluacionesPorCurso(Integer cursoId) {
        return evaluacionRepository.findAll().stream()
                .filter(e -> {
                    // Buscar evaluaciones que pertenezcan a módulos del curso
                    // En este caso, asumimos que moduloId puede estar relacionado con el curso
                    return e.getModuloId() != null &&
                           "activa".equalsIgnoreCase(e.getEstado());
                })
                .collect(Collectors.toList());
    }

    /**
     * Cuenta las evaluaciones disponibles para un curso
     */
    public int contarEvaluacionesCurso(Integer cursoId) {
        return obtenerEvaluacionesPorCurso(cursoId).size();
    }

    /**
     * Calcula el puntaje total de las evaluaciones
     */
    public double calcularPuntajeTotal(Integer cursoId) {
        return obtenerEvaluacionesPorCurso(cursoId).stream()
                .mapToDouble(e -> e.getPuntajeMaximo() != null ?
                                 e.getPuntajeMaximo().doubleValue() : 0.0)
                .sum();
    }

    /**
     * Verifica si el curso tiene evaluaciones configuradas
     */
    public boolean tieneEvaluaciones(Integer cursoId) {
        return !obtenerEvaluacionesPorCurso(cursoId).isEmpty();
    }
}
