package com.edulearn.service;

import com.edulearn.model.HistorialProgresoEstudiante;
import com.edulearn.model.ProgresoEstudiante;
import com.edulearn.patterns.comportamiento.memento.ProgresoCaretaker;
import com.edulearn.patterns.comportamiento.memento.ProgresoMemento;
import com.edulearn.patterns.comportamiento.memento.ProgresoOriginator;
import com.edulearn.repository.HistorialProgresoEstudianteRepository;
import com.edulearn.repository.ProgresoEstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio que implementa el patrón Memento para gestionar el progreso del estudiante
 */
@Service
public class ProgresoEstudianteService {

    @Autowired
    private ProgresoEstudianteRepository progresoRepository;

    @Autowired
    private HistorialProgresoEstudianteRepository historialRepository;

    private final ProgresoCaretaker caretaker;

    public ProgresoEstudianteService() {
        this.caretaker = new ProgresoCaretaker(20); // Máximo 20 estados en memoria
    }

    /**
     * Obtiene o crea el progreso de un estudiante en un curso
     */
    public ProgresoEstudiante obtenerOCrearProgreso(Integer estudianteId, Integer cursoId) {
        Optional<ProgresoEstudiante> progresoOpt = progresoRepository.findByEstudianteIdAndCursoId(estudianteId, cursoId);

        if (progresoOpt.isPresent()) {
            return progresoOpt.get();
        }

        ProgresoEstudiante nuevoProgreso = new ProgresoEstudiante();
        nuevoProgreso.setEstudianteId(estudianteId);
        nuevoProgreso.setCursoId(cursoId);
        return progresoRepository.save(nuevoProgreso);
    }

    /**
     * Guarda el estado actual del progreso (Memento Pattern)
     */
    @Transactional
    public ProgresoMemento guardarEstadoProgreso(Integer estudianteId, Integer cursoId, String descripcion) {
        ProgresoEstudiante progreso = obtenerOCrearProgreso(estudianteId, cursoId);

        // Crear el Originator desde la entidad actual
        ProgresoOriginator originator = convertirAOriginator(progreso);

        // Crear el memento
        ProgresoMemento memento = originator.guardarEstado();

        // Guardar en el Caretaker (memoria)
        caretaker.guardar(memento);

        // Persistir en la base de datos
        HistorialProgresoEstudiante historial = convertirMementoAHistorial(memento);
        historial.setDescripcion(descripcion);
        historialRepository.save(historial);

        return memento;
    }

    /**
     * Restaura el progreso desde el último estado guardado
     */
    @Transactional
    public ProgresoEstudiante restaurarUltimoEstado(Integer estudianteId, Integer cursoId) {
        // Intentar obtener desde la base de datos
        List<HistorialProgresoEstudiante> historial = historialRepository
                .findByEstudianteIdAndCursoIdOrderByFechaGuardadoDesc(estudianteId, cursoId);

        if (historial.isEmpty()) {
            throw new RuntimeException("No hay estados guardados para restaurar");
        }

        HistorialProgresoEstudiante ultimoEstado = historial.get(0);
        ProgresoMemento memento = convertirHistorialAMemento(ultimoEstado);

        return restaurarDesdeMemento(estudianteId, cursoId, memento);
    }

    /**
     * Restaura el progreso desde un estado específico del historial
     */
    @Transactional
    public ProgresoEstudiante restaurarEstadoPorId(Integer estudianteId, Integer cursoId, Integer historialId) {
        HistorialProgresoEstudiante historial = historialRepository.findById(historialId)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));

        if (!historial.getEstudianteId().equals(estudianteId) || !historial.getCursoId().equals(cursoId)) {
            throw new RuntimeException("El estado no pertenece a este estudiante/curso");
        }

        ProgresoMemento memento = convertirHistorialAMemento(historial);
        return restaurarDesdeMemento(estudianteId, cursoId, memento);
    }

    /**
     * Actualiza el progreso del estudiante
     */
    @Transactional
    public ProgresoEstudiante actualizarProgreso(ProgresoEstudiante progreso) {
        progreso.setFechaUltimaActualizacion(LocalDateTime.now());
        return progresoRepository.save(progreso);
    }

    /**
     * Obtiene el historial completo de un estudiante en un curso
     */
    public List<HistorialProgresoEstudiante> obtenerHistorial(Integer estudianteId, Integer cursoId) {
        return historialRepository.findByEstudianteIdAndCursoIdOrderByFechaGuardadoDesc(estudianteId, cursoId);
    }

    /**
     * Limpia el historial de un estudiante en un curso
     */
    @Transactional
    public void limpiarHistorial(Integer estudianteId, Integer cursoId) {
        historialRepository.deleteByEstudianteIdAndCursoId(estudianteId, cursoId);
        caretaker.limpiarHistorial(estudianteId, cursoId);
    }

    /**
     * Obtiene todos los progresos de un estudiante
     */
    public List<ProgresoEstudiante> obtenerProgresosPorEstudiante(Integer estudianteId) {
        return progresoRepository.findByEstudianteId(estudianteId);
    }

    // Métodos de conversión privados

    private ProgresoOriginator convertirAOriginator(ProgresoEstudiante entidad) {
        ProgresoOriginator originator = new ProgresoOriginator(
            entidad.getEstudianteId(),
            entidad.getCursoId()
        );
        originator.setModuloActualId(entidad.getModuloActualId());
        originator.setPorcentajeCompletado(entidad.getPorcentajeCompletado());
        originator.setCalificacionAcumulada(entidad.getCalificacionAcumulada());
        originator.setLeccionesCompletadas(entidad.getLeccionesCompletadas());
        originator.setEvaluacionesCompletadas(entidad.getEvaluacionesCompletadas());
        originator.setEstadoCurso(entidad.getEstadoCurso());
        originator.setNotasEstudiante(entidad.getNotasEstudiante());
        return originator;
    }

    private ProgresoEstudiante restaurarDesdeMemento(Integer estudianteId, Integer cursoId, ProgresoMemento memento) {
        ProgresoEstudiante progreso = obtenerOCrearProgreso(estudianteId, cursoId);

        progreso.setModuloActualId(memento.getModuloActualId());
        progreso.setPorcentajeCompletado(memento.getPorcentajeCompletado());
        progreso.setCalificacionAcumulada(memento.getCalificacionAcumulada());
        progreso.setLeccionesCompletadas(memento.getLeccionesCompletadas());
        progreso.setEvaluacionesCompletadas(memento.getEvaluacionesCompletadas());
        progreso.setEstadoCurso(memento.getEstadoCurso());
        progreso.setNotasEstudiante(memento.getNotasEstudiante());
        progreso.setFechaUltimaActualizacion(LocalDateTime.now());

        return progresoRepository.save(progreso);
    }

    private HistorialProgresoEstudiante convertirMementoAHistorial(ProgresoMemento memento) {
        HistorialProgresoEstudiante historial = new HistorialProgresoEstudiante();
        historial.setEstudianteId(memento.getEstudianteId());
        historial.setCursoId(memento.getCursoId());
        historial.setModuloActualId(memento.getModuloActualId());
        historial.setPorcentajeCompletado(memento.getPorcentajeCompletado());
        historial.setCalificacionAcumulada(memento.getCalificacionAcumulada());
        historial.setLeccionesCompletadas(memento.getLeccionesCompletadas());
        historial.setEvaluacionesCompletadas(memento.getEvaluacionesCompletadas());
        historial.setEstadoCurso(memento.getEstadoCurso());
        historial.setNotasEstudiante(memento.getNotasEstudiante());
        historial.setFechaGuardado(memento.getFechaGuardado());
        return historial;
    }

    private ProgresoMemento convertirHistorialAMemento(HistorialProgresoEstudiante historial) {
        return new ProgresoMemento(
            historial.getEstudianteId(),
            historial.getCursoId(),
            historial.getModuloActualId(),
            historial.getPorcentajeCompletado(),
            historial.getCalificacionAcumulada(),
            historial.getLeccionesCompletadas(),
            historial.getEvaluacionesCompletadas(),
            historial.getEstadoCurso(),
            historial.getNotasEstudiante()
        );
    }
}
