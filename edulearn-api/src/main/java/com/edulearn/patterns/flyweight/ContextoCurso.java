package com.edulearn.patterns.flyweight;

/**
 * PATRÓN FLYWEIGHT - Estado Extrínseco
 * Contiene información específica de cada curso que NO se comparte
 */
public class ContextoCurso {
    private Integer cursoId;
    private String nombreCurso;
    private String descripcion;
    private Integer duracion;
    private String profesorNombre;
    private Integer estudiantesInscritos;
    private String periodoAcademico;

    public ContextoCurso(Integer cursoId, String nombreCurso, String descripcion,
                        Integer duracion, String profesorNombre, Integer estudiantesInscritos,
                        String periodoAcademico) {
        this.cursoId = cursoId;
        this.nombreCurso = nombreCurso;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.profesorNombre = profesorNombre;
        this.estudiantesInscritos = estudiantesInscritos;
        this.periodoAcademico = periodoAcademico;
    }

    // Getters
    public Integer getCursoId() { return cursoId; }
    public String getNombreCurso() { return nombreCurso; }
    public String getDescripcion() { return descripcion; }
    public Integer getDuracion() { return duracion; }
    public String getProfesorNombre() { return profesorNombre; }
    public Integer getEstudiantesInscritos() { return estudiantesInscritos; }
    public String getPeriodoAcademico() { return periodoAcademico; }
}
