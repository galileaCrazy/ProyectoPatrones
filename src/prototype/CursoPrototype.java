package prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * CONCRETE PROTOTYPE (Prototipo Concreto) - Implementa la clonacion de cursos.
 * Permite duplicar un curso existente con toda su estructura
 * (modulos, clases, evaluaciones) para un nuevo periodo academico.
 */
public class CursoPrototype implements IPrototype<CursoPrototype> {

    // Informacion basica del curso
    private int idOriginal;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String tipoCurso;
    private String estrategiaEvaluacion;
    private int profesorTitularId;
    private String periodoAcademico;
    private int cupoMaximo;

    // Estructura del curso (modulos con sus clases y evaluaciones)
    private List<ModuloPrototype> modulos;

    public CursoPrototype() {
        this.modulos = new ArrayList<>();
    }

    public CursoPrototype(int id, String codigo, String nombre, String descripcion,
                          String tipoCurso, int profesorId, String periodo, int cupo) {
        this();
        this.idOriginal = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoCurso = tipoCurso;
        this.profesorTitularId = profesorId;
        this.periodoAcademico = periodo;
        this.cupoMaximo = cupo;
    }

    /**
     * Clona el curso completo incluyendo toda su estructura.
     */
    @Override
    public CursoPrototype clonar() {
        CursoPrototype clon = new CursoPrototype();

        // Copiar datos basicos
        clon.idOriginal = 0; // Nuevo curso, sin ID aun
        clon.codigo = this.codigo + "-COPIA";
        clon.nombre = this.nombre + " (Copia)";
        clon.descripcion = this.descripcion;
        clon.tipoCurso = this.tipoCurso;
        clon.estrategiaEvaluacion = this.estrategiaEvaluacion;
        clon.profesorTitularId = this.profesorTitularId;
        clon.periodoAcademico = this.periodoAcademico;
        clon.cupoMaximo = this.cupoMaximo;

        // Clonar modulos (deep copy)
        for (ModuloPrototype modulo : this.modulos) {
            clon.modulos.add(modulo.clonar());
        }

        return clon;
    }

    /**
     * Clona el curso para un nuevo periodo academico.
     * Actualiza el codigo y periodo, mantiene la estructura.
     */
    @Override
    public CursoPrototype clonarParaNuevoPeriodo(String nuevoPeriodo) {
        CursoPrototype clon = new CursoPrototype();

        // Copiar datos basicos con nuevo periodo
        clon.idOriginal = 0;
        clon.codigo = generarNuevoCodigo(nuevoPeriodo);
        clon.nombre = this.nombre;
        clon.descripcion = this.descripcion;
        clon.tipoCurso = this.tipoCurso;
        clon.estrategiaEvaluacion = this.estrategiaEvaluacion;
        clon.profesorTitularId = this.profesorTitularId;
        clon.periodoAcademico = nuevoPeriodo;
        clon.cupoMaximo = this.cupoMaximo;

        // Clonar modulos (deep copy)
        for (ModuloPrototype modulo : this.modulos) {
            clon.modulos.add(modulo.clonar());
        }

        return clon;
    }

    private String generarNuevoCodigo(String nuevoPeriodo) {
        // Quitar periodo anterior del codigo si existe
        String codigoBase = this.codigo;
        if (codigoBase.contains("-202")) {
            codigoBase = codigoBase.substring(0, codigoBase.lastIndexOf("-202"));
        }
        return codigoBase + "-" + nuevoPeriodo.replace("-", "");
    }

    // Getters y Setters
    public int getIdOriginal() { return idOriginal; }
    public void setIdOriginal(int id) { this.idOriginal = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipoCurso() { return tipoCurso; }
    public void setTipoCurso(String tipoCurso) { this.tipoCurso = tipoCurso; }

    public String getEstrategiaEvaluacion() { return estrategiaEvaluacion; }
    public void setEstrategiaEvaluacion(String estrategia) { this.estrategiaEvaluacion = estrategia; }

    public int getProfesorTitularId() { return profesorTitularId; }
    public void setProfesorTitularId(int id) { this.profesorTitularId = id; }

    public String getPeriodoAcademico() { return periodoAcademico; }
    public void setPeriodoAcademico(String periodo) { this.periodoAcademico = periodo; }

    public int getCupoMaximo() { return cupoMaximo; }
    public void setCupoMaximo(int cupo) { this.cupoMaximo = cupo; }

    public List<ModuloPrototype> getModulos() { return modulos; }
    public void agregarModulo(ModuloPrototype modulo) { this.modulos.add(modulo); }

    public int getTotalClases() {
        return modulos.stream().mapToInt(m -> m.getClases().size()).sum();
    }

    public int getTotalEvaluaciones() {
        return modulos.stream().mapToInt(m -> m.getEvaluaciones().size()).sum();
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " [" + tipoCurso + "] " + periodoAcademico;
    }

    public String getResumen() {
        return "Curso: " + nombre + "\n" +
               "Codigo: " + codigo + "\n" +
               "Tipo: " + tipoCurso + "\n" +
               "Periodo: " + periodoAcademico + "\n" +
               "Modulos: " + modulos.size() + "\n" +
               "Clases: " + getTotalClases() + "\n" +
               "Evaluaciones: " + getTotalEvaluaciones();
    }
}
