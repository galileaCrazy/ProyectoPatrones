package builder;

import java.math.BigDecimal;

/**
 * Representa una Evaluación dentro de un módulo del curso.
 */
public class EvaluacionModulo {
    private int id;
    private String nombre;
    private String descripcion;
    private TipoEvaluacion tipo;
    private BigDecimal puntajeMaximo;
    private BigDecimal pesoPorcentual;
    private int intentosPermitidos;
    private Integer tiempoLimiteMinutos;

    public enum TipoEvaluacion {
        EXAMEN("examen"),
        TAREA("tarea"),
        PROYECTO("proyecto"),
        QUIZ("quiz"),
        PARTICIPACION("participacion");

        private String valor;

        TipoEvaluacion(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }
    }

    public EvaluacionModulo() {
        this.intentosPermitidos = 1;
        this.puntajeMaximo = new BigDecimal("100");
    }

    public EvaluacionModulo(String nombre, TipoEvaluacion tipo, BigDecimal pesoPorcentual) {
        this();
        this.nombre = nombre;
        this.tipo = tipo;
        this.pesoPorcentual = pesoPorcentual;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public TipoEvaluacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoEvaluacion tipo) {
        this.tipo = tipo;
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

    @Override
    public String toString() {
        return "EvaluacionModulo{" +
                "nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                ", puntajeMaximo=" + puntajeMaximo +
                ", pesoPorcentual=" + pesoPorcentual +
                '}';
    }
}
