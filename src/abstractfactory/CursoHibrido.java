package abstractfactory;

import model.Curso;

/**
 * Producto Concreto: Curso Híbrido
 * Implementación específica de ICurso para cursos híbridos
 * @author USUARIO
 */
public class CursoHibrido implements ICurso {
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
        return "HÍBRIDO";
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
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║         CURSO HÍBRIDO                          ║");
        System.out.println("╠════════════════════════════════════════════════╣");
        System.out.println("  Código: " + codigo);
        System.out.println("  Nombre: " + nombre);
        System.out.println("  Tipo: " + getTipo());
        System.out.println("  Estrategia: " + getEstrategiaEvaluacion());
        System.out.println("  Cupo máximo: " + getCupoMaximo() + " estudiantes");
        System.out.println("╚════════════════════════════════════════════════╝");
    }

    @Override
    public Curso toModel() {
        Curso curso = new Curso(codigo, nombre, descripcion,
                               Curso.TipoCurso.HIBRIDO,
                               profesorId, periodoAcademico);
        curso.setEstrategiaEvaluacion(getEstrategiaEvaluacion());
        curso.setCupoMaximo(getCupoMaximo());
        return curso;
    }
}
