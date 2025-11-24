package builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * PRODUCTO - Objeto complejo que se construye paso a paso.
 * Representa un curso completo con todos sus componentes:
 * módulos, clases y evaluaciones.
 */
public class CursoCompleto {
    // Información básica del curso
    private String codigo;
    private String nombre;
    private String descripcion;
    private TipoCurso tipoCurso;
    private String periodoAcademico;
    private Date fechaInicio;
    private Date fechaFin;
    private int cupoMaximo;

    // Componentes del curso
    private List<Modulo> modulos;

    // Metadatos
    private String instructor;
    private int duracionTotalHoras;
    private String nivelDificultad;

    public enum TipoCurso {
        PRESENCIAL("presencial"),
        VIRTUAL("virtual"),
        HIBRIDO("hibrido");

        private String valor;

        TipoCurso(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }
    }

    public CursoCompleto() {
        this.modulos = new ArrayList<>();
        this.cupoMaximo = 30;
    }

    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoCurso getTipoCurso() {
        return tipoCurso;
    }

    public void setTipoCurso(TipoCurso tipoCurso) {
        this.tipoCurso = tipoCurso;
    }

    public String getPeriodoAcademico() {
        return periodoAcademico;
    }

    public void setPeriodoAcademico(String periodoAcademico) {
        this.periodoAcademico = periodoAcademico;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public List<Modulo> getModulos() {
        return modulos;
    }

    public void agregarModulo(Modulo modulo) {
        this.modulos.add(modulo);
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public int getDuracionTotalHoras() {
        return duracionTotalHoras;
    }

    public void setDuracionTotalHoras(int duracionTotalHoras) {
        this.duracionTotalHoras = duracionTotalHoras;
    }

    public String getNivelDificultad() {
        return nivelDificultad;
    }

    public void setNivelDificultad(String nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
    }

    // Métodos de utilidad
    public int getTotalClases() {
        return modulos.stream()
                .mapToInt(m -> m.getClases().size())
                .sum();
    }

    public int getTotalEvaluaciones() {
        return modulos.stream()
                .mapToInt(m -> m.getEvaluaciones().size())
                .sum();
    }

    public void mostrarEstructura() {
        System.out.println("========================================");
        System.out.println("CURSO: " + nombre);
        System.out.println("========================================");
        System.out.println("Codigo: " + codigo);
        System.out.println("Descripcion: " + descripcion);
        System.out.println("Tipo: " + tipoCurso);
        System.out.println("Instructor: " + instructor);
        System.out.println("Nivel: " + nivelDificultad);
        System.out.println("Duracion Total: " + duracionTotalHoras + " horas");
        System.out.println("Cupo Maximo: " + cupoMaximo + " estudiantes");
        System.out.println("Periodo: " + periodoAcademico);
        System.out.println("----------------------------------------");
        System.out.println("ESTRUCTURA DEL CURSO:");
        System.out.println("Total Modulos: " + modulos.size());
        System.out.println("Total Clases: " + getTotalClases());
        System.out.println("Total Evaluaciones: " + getTotalEvaluaciones());
        System.out.println("----------------------------------------");

        for (Modulo modulo : modulos) {
            System.out.println("\n  MODULO " + modulo.getOrden() + ": " + modulo.getNombre());
            System.out.println("  " + modulo.getDescripcion());

            if (!modulo.getClases().isEmpty()) {
                System.out.println("    Clases:");
                for (Clase clase : modulo.getClases()) {
                    System.out.println("      - " + clase.getNombre() +
                            " [" + clase.getTipo() + "] " +
                            clase.getDuracionMinutos() + " min");
                }
            }

            if (!modulo.getEvaluaciones().isEmpty()) {
                System.out.println("    Evaluaciones:");
                for (EvaluacionModulo eval : modulo.getEvaluaciones()) {
                    System.out.println("      - " + eval.getNombre() +
                            " [" + eval.getTipo() + "] " +
                            eval.getPesoPorcentual() + "%");
                }
            }
        }
        System.out.println("\n========================================");
    }

    @Override
    public String toString() {
        return "CursoCompleto{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipoCurso=" + tipoCurso +
                ", modulos=" + modulos.size() +
                ", totalClases=" + getTotalClases() +
                ", totalEvaluaciones=" + getTotalEvaluaciones() +
                '}';
    }
}
