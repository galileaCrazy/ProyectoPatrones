package com.edulearn.patterns.behavioral.memento;

import com.edulearn.model.Curso;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PATRÓN MEMENTO - Originator
 * ============================
 * Crea mementos con su estado actual y puede restaurarse desde un memento.
 * Es el único que conoce el contenido completo del memento.
 */
public class CursoOriginator {

    private static final Logger logger = LoggerFactory.getLogger(CursoOriginator.class);

    private Curso curso;

    public CursoOriginator(Curso curso) {
        this.curso = curso;
    }

    /**
     * Crea un memento con el estado actual del curso
     */
    public CursoMemento guardarEstado(String operacion, String descripcionCambio) {
        logger.info("Guardando estado del curso {} - Operación: {}", curso.getId(), operacion);

        return new CursoMemento.Builder()
            .id(curso.getId())
            .codigo(curso.getCodigo())
            .nombre(curso.getNombre())
            .descripcion(curso.getDescripcion())
            .tipoCurso(curso.getTipoCurso())
            .estado(curso.getEstado())
            .profesorTitularId(curso.getProfesorTitularId())
            .periodoAcademico(curso.getPeriodoAcademico())
            .duracion(curso.getDuracion())
            .estrategiaEvaluacion(curso.getEstrategiaEvaluacion())
            .cupoMaximo(curso.getCupoMaximo())
            .operacion(operacion)
            .descripcionCambio(descripcionCambio)
            .build();
    }

    /**
     * Restaura el curso desde un memento
     */
    public void restaurarEstado(CursoMemento memento) {
        logger.info("Restaurando estado del curso {} desde memento de {}",
            curso.getId(), memento.getFechaCreacion());

        curso.setCodigo(memento.getCodigo());
        curso.setNombre(memento.getNombre());
        curso.setDescripcion(memento.getDescripcion());
        curso.setTipoCurso(memento.getTipoCurso());
        curso.setEstado(memento.getEstado());
        curso.setProfesorTitularId(memento.getProfesorTitularId());
        curso.setPeriodoAcademico(memento.getPeriodoAcademico());
        curso.setDuracion(memento.getDuracion());
        curso.setEstrategiaEvaluacion(memento.getEstrategiaEvaluacion());
        curso.setCupoMaximo(memento.getCupoMaximo());
    }

    /**
     * Obtiene el curso actual
     */
    public Curso getCurso() {
        return curso;
    }

    /**
     * Establece un nuevo curso
     */
    public void setCurso(Curso curso) {
        this.curso = curso;
    }
}
