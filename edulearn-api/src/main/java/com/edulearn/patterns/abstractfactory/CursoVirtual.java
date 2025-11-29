package com.edulearn.patterns.abstractfactory;

import com.edulearn.model.Curso;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Producto Concreto: Curso Virtual
 * Implementación específica de ICurso para cursos virtuales
 * Patrón: Abstract Factory
 */
public class CursoVirtual implements ICurso {
    private static final Logger logger = LoggerFactory.getLogger(CursoVirtual.class);

    private String codigo;
    private String nombre;
    private String descripcion;
    private int profesorId;
    private String periodoAcademico;

    public CursoVirtual(String codigo, String nombre, String descripcion,
                       int profesorId, String periodoAcademico) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.profesorId = profesorId;
        this.periodoAcademico = periodoAcademico;
    }

    @Override
    public String getCodigo() {
        return codigo;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String getTipo() {
        return "virtual";
    }

    @Override
    public String getEstrategiaEvaluacion() {
        return "promedio_simple";
    }

    @Override
    public int getCupoMaximo() {
        return 100; // Cursos virtuales pueden tener más estudiantes
    }

    @Override
    public void mostrarInfo() {
        logger.info("╔════════════════════════════════════════════════╗");
        logger.info("║         CURSO VIRTUAL                          ║");
        logger.info("╠════════════════════════════════════════════════╣");
        logger.info("  Código: {}", codigo);
        logger.info("  Nombre: {}", nombre);
        logger.info("  Tipo: {}", getTipo());
        logger.info("  Estrategia: {}", getEstrategiaEvaluacion());
        logger.info("  Cupo máximo: {} estudiantes", getCupoMaximo());
        logger.info("╚════════════════════════════════════════════════╝");
    }

    @Override
    public Curso toEntity() {
        Curso curso = new Curso();
        curso.setCodigo(codigo);
        curso.setNombre(nombre);
        curso.setDescripcion(descripcion);
        curso.setTipoCurso("virtual");
        curso.setProfesorTitularId(profesorId);
        curso.setPeriodoAcademico(periodoAcademico);
        curso.setEstado("activo");
        return curso;
    }
}
