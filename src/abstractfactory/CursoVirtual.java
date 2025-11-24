package abstractfactory;

import model.Curso;

/**
 * Producto Concreto: Curso Virtual
 * Implementación específica de ICurso para cursos virtuales
 * @author USUARIO
 */
public class CursoVirtual implements ICurso {
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
        return "VIRTUAL";
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
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║         CURSO VIRTUAL                          ║");
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
                               Curso.TipoCurso.VIRTUAL,
                               profesorId, periodoAcademico);
        curso.setEstrategiaEvaluacion(getEstrategiaEvaluacion());
        curso.setCupoMaximo(getCupoMaximo());
        return curso;
    }
}
