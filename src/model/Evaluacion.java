package model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Modelo que representa una Evaluación
 * @author USUARIO
 */
public class Evaluacion {
    private int id;
    private int moduloId;
    private TipoEvaluacion tipo;
    private String nombre;
    private String descripcion;
    private BigDecimal puntajeMaximo;
    private BigDecimal pesoPorcentual;
    private int intentosPermitidos;
    private Integer tiempoLimiteMinutos;
    private Date fechaApertura;
    private Date fechaCierre;
    private Date createdAt;

    /**
     * Enumeración para los tipos de evaluación
     */
    public enum TipoEvaluacion {
        EXAMEN("examen"),
        TAREA("tarea"),
        PROYECTO("proyecto"),
        PARTICIPACION("participacion"),
        QUIZ("quiz");

        private String valor;

        TipoEvaluacion(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }

        public static TipoEvaluacion fromString(String text) {
            for (TipoEvaluacion tipo : TipoEvaluacion.values()) {
                if (tipo.valor.equalsIgnoreCase(text)) {
                    return tipo;
                }
            }
            throw new IllegalArgumentException("No hay tipo de evaluación con valor: " + text);
        }
    }

    // Constructores
    public Evaluacion() {
        this.intentosPermitidos = 1;
    }

    public Evaluacion(int moduloId, TipoEvaluacion tipo, String nombre,
                      BigDecimal puntajeMaximo) {
        this();
        this.moduloId = moduloId;
        this.tipo = tipo;
        this.nombre = nombre;
        this.puntajeMaximo = puntajeMaximo;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModuloId() {
        return moduloId;
    }

    public void setModuloId(int moduloId) {
        this.moduloId = moduloId;
    }

    public TipoEvaluacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoEvaluacion tipo) {
        this.tipo = tipo;
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

    public BigDecimal getPuntajeMaximo() {
        return puntajeMaximo;
    }

    public void setPuntajeMaximo(BigDecimal puntajeMaximo) {
        this.puntajeMaximo = puntajeMaximo;
    }

    public BigDecimal getPesoPorcentual() {
        return pesoPorcentual;
    }

    public void setPesoPorcentual(BigDecimal pesoPorcentual) {
        this.pesoPorcentual = pesoPorcentual;
    }

    public int getIntentosPermitidos() {
        return intentosPermitidos;
    }

    public void setIntentosPermitidos(int intentosPermitidos) {
        this.intentosPermitidos = intentosPermitidos;
    }

    public Integer getTiempoLimiteMinutos() {
        return tiempoLimiteMinutos;
    }

    public void setTiempoLimiteMinutos(Integer tiempoLimiteMinutos) {
        this.tiempoLimiteMinutos = tiempoLimiteMinutos;
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Evaluacion{" +
                "id=" + id +
                ", tipo=" + tipo +
                ", nombre='" + nombre + '\'' +
                ", puntajeMaximo=" + puntajeMaximo +
                ", pesoPorcentual=" + pesoPorcentual +
                '}';
    }
}
