package com.edulearn.patterns.creational.abstractfactory;

import com.edulearn.model.Curso;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Producto Concreto: Curso Híbrido
 * Implementación específica de ICurso para cursos híbridos
 * Patrón: Abstract Factory
 */
public class CursoHibrido implements ICurso {
    private static final Logger logger = LoggerFactory.getLogger(CursoHibrido.class);

    private String codigo;
    private String nombre;
    private String descripcion;
    private int profesorId;
    private String periodoAcademico;

    public CursoHibrido(String codigo, String nombre, String descripcion,
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
        return "hibrido";
    }

    @Override
    public String getEstrategiaEvaluacion() {
        return "por_competencias";
    }

    @Override
    public int getCupoMaximo() {
        return 60; // Cupo intermedio para cursos híbridos
    }

    @Override
    public void mostrarInfo() {
        logger.info("╔════════════════════════════════════════════════╗");
        logger.info("║         CURSO HÍBRIDO                          ║");
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
        curso.setTipoCurso("hibrido");
        curso.setProfesorTitularId(profesorId);
        curso.setPeriodoAcademico(periodoAcademico);
        curso.setEstado("activo");
        return curso;
    }
}
