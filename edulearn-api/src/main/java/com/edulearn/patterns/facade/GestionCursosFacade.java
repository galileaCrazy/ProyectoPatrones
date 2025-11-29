package com.edulearn.patterns.facade;

import com.edulearn.model.Curso;
import com.edulearn.model.Estudiante;
import com.edulearn.model.Inscripcion;
import com.edulearn.repository.CursoRepository;
import com.edulearn.repository.EstudianteRepository;
import com.edulearn.repository.InscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Facade - Proporciona una interfaz simplificada a un conjunto de interfaces en un subsistema
 * Patrón: Facade
 * Propósito: Proporcionar una interfaz unificada para un conjunto de interfaces en un subsistema.
 * Facade define una interfaz de nivel superior que hace que el subsistema sea más fácil de usar.
 */
@Component
public class GestionCursosFacade {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    /**
     * Inscribir estudiante - Operación simplificada que coordina múltiples subsistemas
     */
    public Map<String, Object> inscribirEstudiante(Integer estudianteId, Integer cursoId) {
        Map<String, Object> resultado = new HashMap<>();

        // Verificar que existan estudiante y curso
        if (!estudianteRepository.existsById(estudianteId)) {
            resultado.put("exito", false);
            resultado.put("mensaje", "Estudiante no encontrado");
            return resultado;
        }

        if (!cursoRepository.existsById(cursoId)) {
            resultado.put("exito", false);
            resultado.put("mensaje", "Curso no encontrado");
            return resultado;
        }

        // Crear inscripción
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setEstudianteId(estudianteId);
        inscripcion.setCursoId(cursoId);
        inscripcion.setFechaInscripcion(java.time.LocalDate.now());

        inscripcionRepository.save(inscripcion);

        resultado.put("exito", true);
        resultado.put("mensaje", "Inscripción exitosa");
        resultado.put("inscripcion", inscripcion);
        return resultado;
    }

    /**
     * Obtener resumen del estudiante
     */
    public Map<String, Object> obtenerResumenEstudiante(Integer estudianteId) {
        Map<String, Object> resumen = new HashMap<>();

        Estudiante estudiante = estudianteRepository.findById(estudianteId).orElse(null);
        if (estudiante == null) {
            resumen.put("error", "Estudiante no encontrado");
            return resumen;
        }

        List<Inscripcion> inscripciones = inscripcionRepository.findAll();
        long totalInscripciones = inscripciones.stream()
                .filter(i -> i.getEstudianteId().equals(estudianteId))
                .count();

        resumen.put("estudiante", estudiante);
        resumen.put("totalInscripciones", totalInscripciones);
        resumen.put("cursosActivos", totalInscripciones);

        return resumen;
    }

    /**
     * Obtener información completa del curso
     */
    public Map<String, Object> obtenerInformacionCurso(Integer cursoId) {
        Map<String, Object> info = new HashMap<>();

        Curso curso = cursoRepository.findById(cursoId).orElse(null);
        if (curso == null) {
            info.put("error", "Curso no encontrado");
            return info;
        }

        List<Inscripcion> inscripciones = inscripcionRepository.findAll();
        long totalEstudiantes = inscripciones.stream()
                .filter(i -> i.getCursoId().equals(cursoId))
                .count();

        info.put("curso", curso);
        info.put("totalEstudiantes", totalEstudiantes);

        return info;
    }
}
