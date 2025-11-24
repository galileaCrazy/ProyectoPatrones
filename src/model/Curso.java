package model;

import java.util.Date;

/**
 * Modelo que representa un Curso en la plataforma
 * @author USUARIO
 */
public class Curso {
    private int id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private TipoCurso tipoCurso;
    private EstadoCurso estado;
    private String estrategiaEvaluacion;
    private int profesorTitularId;
    private String periodoAcademico;
    private Date fechaInicio;
    private Date fechaFin;
    private int cupoMaximo;
    private int cupoActual;
    private Date createdAt;
    private Date updatedAt;

    /**
     * Enumeración para los tipos de curso
     */
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

        public static TipoCurso fromString(String text) {
            for (TipoCurso tipo : TipoCurso.values()) {
                if (tipo.valor.equalsIgnoreCase(text)) {
                    return tipo;
                }
            }
            throw new IllegalArgumentException("No hay tipo de curso con valor: " + text);
        }
    }

    /**
     * Enumeración para los estados del curso
     */
    public enum EstadoCurso {
        EN_CREACION("en_creacion"),
        ACTIVO("activo"),
        FINALIZADO("finalizado"),
        ARCHIVADO("archivado");

        private String valor;

        EstadoCurso(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }

        public static EstadoCurso fromString(String text) {
            for (EstadoCurso estado : EstadoCurso.values()) {
                if (estado.valor.equalsIgnoreCase(text)) {
                    return estado;
                }
            }
            throw new IllegalArgumentException("No hay estado de curso con valor: " + text);
        }
    }

    // Constructores
    public Curso() {
        this.estado = EstadoCurso.EN_CREACION;
        this.cupoMaximo = 30;
        this.cupoActual = 0;
    }

    public Curso(String codigo, String nombre, String descripcion, TipoCurso tipoCurso,
                 int profesorTitularId, String periodoAcademico) {
        this();
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoCurso = tipoCurso;
        this.profesorTitularId = profesorTitularId;
        this.periodoAcademico = periodoAcademico;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public EstadoCurso getEstado() {
        return estado;
    }

    public void setEstado(EstadoCurso estado) {
        this.estado = estado;
    }

    public String getEstrategiaEvaluacion() {
        return estrategiaEvaluacion;
    }

    public void setEstrategiaEvaluacion(String estrategiaEvaluacion) {
        this.estrategiaEvaluacion = estrategiaEvaluacion;
    }

    public int getProfesorTitularId() {
        return profesorTitularId;
    }

    public void setProfesorTitularId(int profesorTitularId) {
        this.profesorTitularId = profesorTitularId;
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

    public int getCupoActual() {
        return cupoActual;
    }

    public void setCupoActual(int cupoActual) {
        this.cupoActual = cupoActual;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Curso{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipoCurso=" + tipoCurso +
                ", estado=" + estado +
                ", periodoAcademico='" + periodoAcademico + '\'' +
                '}';
    }
}
