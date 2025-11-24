package abstractfactory;

import model.Curso;

/**
 * Producto Concreto: Curso Presencial
 * Implementación específica de ICurso para cursos presenciales
 * @author USUARIO
 */
public class CursoPresencial implements ICurso {
    private String codigo;
    private String nombre;
    private String descripcion;
    private int profesorId;
    private String periodoAcademico;

    public CursoPresencial(String codigo, String nombre, String descripcion,
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
        return "PRESENCIAL";
    }

    @Override
    public String getEstrategiaEvaluacion() {
        return "ponderada";
    }

    @Override
    public int getCupoMaximo() {
        return 40; // Cursos presenciales tienen cupo limitado por aula física
    }

    @Override
    public void mostrarInfo() {
        System.out.println("╔════════════════════════════════════════════════╗");
        System.out.println("║         CURSO PRESENCIAL                       ║");
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
                               Curso.TipoCurso.PRESENCIAL,
                               profesorId, periodoAcademico);
        curso.setEstrategiaEvaluacion(getEstrategiaEvaluacion());
        curso.setCupoMaximo(getCupoMaximo());
        return curso;
    }
}
