package com.edulearn.patterns.creational.builder;

import com.edulearn.model.*;
import com.edulearn.patterns.creational.abstractfactory.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * BUILDER COMPLETO - Integración con Abstract Factory
 * ======================================================
 * Este Builder extiende el patrón Builder para crear cursos completos
 * con módulos, materiales y evaluaciones usando las familias del Abstract Factory.
 * 
 * Patrón: Builder + Abstract Factory (integración)
 * 
 * Responsabilidades:
 * 1. Construir el curso base (usando CursoBuilder original)
 * 2. Agregar módulos con contenido
 * 3. Agregar materiales usando la factory apropiada
 * 4. Agregar evaluaciones usando la factory apropiada
 * 5. Mantener consistencia entre componentes
 */
public class CursoCompleteBuilder {

    // Campos del curso base
    private String codigo;
    private String nombre;
    private String descripcion = "";
    private String tipoCurso = "virtual";
    private String estado = "ACTIVO";
    private Integer profesorTitularId;
    private String periodoAcademico;
    private Integer duracion = 40;
    private String estrategiaEvaluacion;
    private Integer cupoMaximo = 30;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    // Componentes del curso completo
    private List<ModuloDTO> modulos = new ArrayList<>();
    private CourseComponentFactory factory;

    /**
     * Constructor que recibe la Factory para crear componentes
     */
    public CursoCompleteBuilder(CourseComponentFactory factory) {
        this.factory = factory;
        this.codigo = "CURSO-" + System.currentTimeMillis();
        
        // Establecer tipo de curso según la factory
        if (factory instanceof VirtualCourseFactory) {
            this.tipoCurso = "virtual";
        } else if (factory instanceof PresencialCourseFactory) {
            this.tipoCurso = "presencial";
        } else if (factory instanceof HibridoCourseFactory) {
            this.tipoCurso = "hibrido";
        }
    }

    /**
     * DTO interno para organizar módulos con su contenido
     */
    public static class ModuloDTO {
        public String titulo;
        public String descripcion;
        public Integer orden;
        public String estado = "draft";
        public List<MaterialDTO> materiales = new ArrayList<>();
        public List<EvaluacionDTO> evaluaciones = new ArrayList<>();

        public ModuloDTO(String titulo, String descripcion, Integer orden) {
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.orden = orden;
        }
    }

    public static class MaterialDTO {
        public String nombre;
        public String descripcion;
        public Integer orden;
        
        public MaterialDTO(String nombre, String descripcion, Integer orden) {
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.orden = orden;
        }
    }

    public static class EvaluacionDTO {
        public String nombre;
        public String descripcion;
        
        public EvaluacionDTO(String nombre, String descripcion) {
            this.nombre = nombre;
            this.descripcion = descripcion;
        }
    }

    // ========== MÉTODOS DE CONFIGURACIÓN DEL CURSO BASE ==========

    public CursoCompleteBuilder setCodigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public CursoCompleteBuilder setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public CursoCompleteBuilder setDescripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public CursoCompleteBuilder setTipoCurso(String tipoCurso) {
        this.tipoCurso = tipoCurso;
        return this;
    }

    public CursoCompleteBuilder setEstado(String estado) {
        this.estado = estado;
        return this;
    }

    public CursoCompleteBuilder setProfesorTitularId(Integer profesorTitularId) {
        this.profesorTitularId = profesorTitularId;
        return this;
    }

    public CursoCompleteBuilder setPeriodoAcademico(String periodoAcademico) {
        this.periodoAcademico = periodoAcademico;
        return this;
    }

    public CursoCompleteBuilder setDuracion(Integer duracion) {
        this.duracion = duracion;
        return this;
    }

    public CursoCompleteBuilder setEstrategiaEvaluacion(String estrategiaEvaluacion) {
        this.estrategiaEvaluacion = estrategiaEvaluacion;
        return this;
    }

    public CursoCompleteBuilder setCupoMaximo(Integer cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
        return this;
    }

    public CursoCompleteBuilder setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
        return this;
    }

    public CursoCompleteBuilder setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
        return this;
    }

    // ========== MÉTODOS PARA AGREGAR MÓDULOS Y CONTENIDO ==========

    /**
     * Agrega un nuevo módulo al curso
     */
    public CursoCompleteBuilder agregarModulo(String titulo, String descripcion) {
        int orden = modulos.size() + 1;
        ModuloDTO modulo = new ModuloDTO(titulo, descripcion, orden);
        modulos.add(modulo);
        return this;
    }

    /**
     * Agrega un material al último módulo agregado usando la Factory
     */
    public CursoCompleteBuilder agregarMaterial(String nombre, String descripcion) {
        if (modulos.isEmpty()) {
            throw new IllegalStateException("Debe agregar un módulo antes de agregar materiales");
        }
        
        ModuloDTO ultimoModulo = modulos.get(modulos.size() - 1);
        int orden = ultimoModulo.materiales.size() + 1;
        ultimoModulo.materiales.add(new MaterialDTO(nombre, descripcion, orden));
        
        return this;
    }

    /**
     * Agrega una evaluación al último módulo agregado usando la Factory
     */
    public CursoCompleteBuilder agregarEvaluacion(String nombre, String descripcion) {
        if (modulos.isEmpty()) {
            throw new IllegalStateException("Debe agregar un módulo antes de agregar evaluaciones");
        }
        
        ModuloDTO ultimoModulo = modulos.get(modulos.size() - 1);
        ultimoModulo.evaluaciones.add(new EvaluacionDTO(nombre, descripcion));
        
        return this;
    }

    /**
     * Agrega material a un módulo específico por índice
     */
    public CursoCompleteBuilder agregarMaterialAModulo(int moduloIndex, String nombre, String descripcion) {
        if (moduloIndex >= modulos.size()) {
            throw new IllegalArgumentException("Índice de módulo inválido");
        }
        
        ModuloDTO modulo = modulos.get(moduloIndex);
        int orden = modulo.materiales.size() + 1;
        modulo.materiales.add(new MaterialDTO(nombre, descripcion, orden));
        
        return this;
    }

    /**
     * Agrega evaluación a un módulo específico por índice
     */
    public CursoCompleteBuilder agregarEvaluacionAModulo(int moduloIndex, String nombre, String descripcion) {
        if (moduloIndex >= modulos.size()) {
            throw new IllegalArgumentException("Índice de módulo inválido");
        }
        
        ModuloDTO modulo = modulos.get(moduloIndex);
        modulo.evaluaciones.add(new EvaluacionDTO(nombre, descripcion));
        
        return this;
    }

    // ========== MÉTODO BUILD ==========

    /**
     * Construye el curso completo con todos sus componentes
     * Retorna un DTO que contiene el curso y todos sus componentes relacionados
     */
    public CursoCompletoDTO build() {
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalStateException("El nombre del curso es obligatorio");
        }

        if (duracion <= 0) {
            throw new IllegalStateException("La duración debe ser mayor a 0");
        }

        // Crear el curso base usando ICurso del Abstract Factory
        ICurso cursoAbstracto = factory.crearCurso(
            codigo,
            nombre,
            descripcion,
            profesorTitularId != null ? profesorTitularId : 1,
            periodoAcademico
        );

        // Convertir a entidad JPA
        Curso curso = cursoAbstracto.toEntity();
        curso.setEstado(estado);
        curso.setDuracion(duracion);
        curso.setEstrategiaEvaluacion(estrategiaEvaluacion);
        curso.setCupoMaximo(cupoMaximo);
        
        // Crear DTO con todos los componentes
        CursoCompletoDTO resultado = new CursoCompletoDTO();
        resultado.curso = curso;

        // Construir módulos con su contenido
        for (ModuloDTO moduloDTO : modulos) {
            Modulo modulo = new Modulo();
            modulo.setNombre(moduloDTO.titulo); // Usar titulo como nombre
            modulo.setTitulo(moduloDTO.titulo);
            modulo.setDescripcion(moduloDTO.descripcion);
            modulo.setOrden(moduloDTO.orden);
            modulo.setEstado(moduloDTO.estado);
            
            // Agregar materiales al módulo usando la Factory
            List<Material> materiales = new ArrayList<>();
            for (MaterialDTO matDTO : moduloDTO.materiales) {
                IMaterial materialAbstracto = factory.crearMaterial(matDTO.nombre, matDTO.descripcion);
                Material material = materialAbstracto.toEntity();
                material.setOrden(matDTO.orden);
                materiales.add(material);
            }
            
            // Agregar evaluaciones al módulo usando la Factory
            List<Evaluacion> evaluaciones = new ArrayList<>();
            for (EvaluacionDTO evalDTO : moduloDTO.evaluaciones) {
                IEvaluacion evaluacionAbstracta = factory.crearEvaluacion(evalDTO.nombre, evalDTO.descripcion);
                Evaluacion evaluacion = evaluacionAbstracta.toEntity();
                evaluaciones.add(evaluacion);
            }
            
            resultado.modulos.add(modulo);
            resultado.materialesPorModulo.put(modulo, materiales);
            resultado.evaluacionesPorModulo.put(modulo, evaluaciones);
        }

        return resultado;
    }

    /**
     * DTO que contiene el curso completo con todos sus componentes
     */
    public static class CursoCompletoDTO {
        public Curso curso;
        public List<Modulo> modulos = new ArrayList<>();
        public java.util.Map<Modulo, List<Material>> materialesPorModulo = new java.util.HashMap<>();
        public java.util.Map<Modulo, List<Evaluacion>> evaluacionesPorModulo = new java.util.HashMap<>();
    }

    /**
     * Reset - permite reutilizar el builder
     */
    public void reset() {
        this.codigo = "CURSO-" + System.currentTimeMillis();
        this.nombre = null;
        this.descripcion = "";
        this.estado = "ACTIVO";
        this.profesorTitularId = null;
        this.periodoAcademico = null;
        this.duracion = 40;
        this.estrategiaEvaluacion = null;
        this.modulos.clear();
    }
}
