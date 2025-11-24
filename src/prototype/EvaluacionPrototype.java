package prototype;

import java.math.BigDecimal;

/**
 * Prototipo de Evaluacion - Permite clonar evaluaciones.
 */
public class EvaluacionPrototype implements IPrototype<EvaluacionPrototype> {

    private int id;
    private String nombre;
    private String descripcion;
    private String tipo; // QUIZ, TAREA, EXAMEN, PROYECTO, PARTICIPACION
    private BigDecimal puntajeMaximo;
    private BigDecimal pesoPorcentual;
    private int intentosPermitidos;
    private Integer tiempoLimiteMinutos;

    public EvaluacionPrototype() {
        this.puntajeMaximo = new BigDecimal("100");
        this.intentosPermitidos = 1;
    }

    public EvaluacionPrototype(String nombre, String tipo, BigDecimal peso) {
        this();
        this.nombre = nombre;
        this.tipo = tipo;
        this.pesoPorcentual = peso;
    }

    @Override
    public EvaluacionPrototype clonar() {
        EvaluacionPrototype clon = new EvaluacionPrototype();
        clon.id = 0; // Nueva evaluacion
        clon.nombre = this.nombre;
        clon.descripcion = this.descripcion;
        clon.tipo = this.tipo;
        clon.puntajeMaximo = this.puntajeMaximo;
        clon.pesoPorcentual = this.pesoPorcentual;
        clon.intentosPermitidos = this.intentosPermitidos;
        clon.tiempoLimiteMinutos = this.tiempoLimiteMinutos;
        return clon;
    }

    @Override
    public EvaluacionPrototype clonarParaNuevoPeriodo(String nuevoPeriodo) {
        return clonar();
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public BigDecimal getPuntajeMaximo() { return puntajeMaximo; }
    public void setPuntajeMaximo(BigDecimal puntaje) { this.puntajeMaximo = puntaje; }

    public BigDecimal getPesoPorcentual() { return pesoPorcentual; }
    public void setPesoPorcentual(BigDecimal peso) { this.pesoPorcentual = peso; }

    public int getIntentosPermitidos() { return intentosPermitidos; }
    public void setIntentosPermitidos(int intentos) { this.intentosPermitidos = intentos; }

    public Integer getTiempoLimiteMinutos() { return tiempoLimiteMinutos; }
    public void setTiempoLimiteMinutos(Integer tiempo) { this.tiempoLimiteMinutos = tiempo; }

    @Override
    public String toString() {
        return nombre + " [" + tipo + "] " + pesoPorcentual + "%";
    }
}
