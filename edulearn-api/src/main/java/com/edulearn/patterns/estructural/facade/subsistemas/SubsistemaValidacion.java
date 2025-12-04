package com.edulearn.patterns.estructural.facade.subsistemas;

import com.edulearn.model.Curso;
import com.edulearn.model.Estudiante;
import com.edulearn.model.Inscripcion;
import com.edulearn.repository.CursoRepository;
import com.edulearn.repository.EstudianteRepository;
import com.edulearn.repository.InscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Subsistema de Validación
 * Responsable de validar todos los requisitos previos a la inscripción
 * Verifica: existencia de estudiante y curso, cupos disponibles, duplicados, requisitos
 */
@Component
public class SubsistemaValidacion {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    /**
     * Valida todos los requisitos previos para la inscripción
     * @return Mensaje de error si falla, null si es exitoso
     */
    public String validarRequisitos(Integer estudianteId, Integer cursoId) {
        // 1. Validar que el estudiante exista
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(estudianteId);
        if (estudianteOpt.isEmpty()) {
            return "El estudiante con ID " + estudianteId + " no existe";
        }

        // 2. Validar que el curso exista
        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);
        if (cursoOpt.isEmpty()) {
            return "El curso con ID " + cursoId + " no existe";
        }

        Curso curso = cursoOpt.get();

        // 3. Validar que el curso esté activo
        if (!"Activo".equalsIgnoreCase(curso.getEstado())) {
            return "El curso no está disponible para inscripción";
        }

        // 4. Validar que no esté ya inscrito
        List<Inscripcion> inscripcionesExistentes = inscripcionRepository.findByEstudianteId(estudianteId);
        boolean yaInscrito = inscripcionesExistentes.stream()
                .anyMatch(i -> i.getCursoId().equals(cursoId) &&
                              "Activa".equalsIgnoreCase(i.getEstadoInscripcion()));

        if (yaInscrito) {
            return "El estudiante ya está inscrito en este curso";
        }

        // 5. Validar cupos disponibles (si aplica)
        if (curso.getCupoMaximo() != null && curso.getCupoMaximo() > 0) {
            long inscritosActivos = inscripcionRepository.findAll().stream()
                    .filter(i -> i.getCursoId().equals(cursoId) &&
                                "Activa".equalsIgnoreCase(i.getEstadoInscripcion()))
                    .count();

            if (inscritosActivos >= curso.getCupoMaximo()) {
                return "El curso ha alcanzado su cupo máximo";
            }
        }

        // Validación exitosa
        return null;
    }

    /**
     * Verifica si el estudiante existe
     */
    public boolean existeEstudiante(Integer estudianteId) {
        return estudianteRepository.existsById(estudianteId);
    }

    /**
     * Verifica si el curso existe y está disponible
     */
    public boolean existeCurso(Integer cursoId) {
        Optional<Curso> curso = cursoRepository.findById(cursoId);
        return curso.isPresent() && "Activo".equalsIgnoreCase(curso.get().getEstado());
    }

    /**
     * Obtiene el curso si existe
     */
    public Optional<Curso> obtenerCurso(Integer cursoId) {
        return cursoRepository.findById(cursoId);
    }
}
