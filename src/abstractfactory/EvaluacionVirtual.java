package abstractfactory;

import model.Evaluacion;
import java.math.BigDecimal;

/**
 * Producto Concreto: Evaluacion Virtual
 * Implementación específica de IEvaluacion para cursos virtuales
 * @author USUARIO
 */
public class EvaluacionVirtual implements IEvaluacion {
    private String nombre;
    private String descripcion;

    public EvaluacionVirtual(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
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
    public String getTipoEvaluacion() {
        return "QUIZ";
    }

    @Override
    public BigDecimal getPuntajeMaximo() {
        return new BigDecimal("100.00");
    }

    @Override
    public int getIntentosPermitidos() {
        return 3; // Múltiples intentos en quizzes virtuales
    }

    @Override
    public Integer getTiempoLimiteMinutos() {
        return 60; // 1 hora
    }

    @Override
    public BigDecimal getPesoPorcentual() {
        return new BigDecimal("20.00");
    }

    @Override
    public void mostrarInfo() {
        System.out.println("  └─ Evaluación: " + nombre);
        System.out.println("     Tipo: " + getTipoEvaluacion());
        System.out.println("     Intentos: " + getIntentosPermitidos());
        System.out.println("     Tiempo límite: " + getTiempoLimiteMinutos() + " minutos");
        System.out.println("     Peso: " + getPesoPorcentual() + "%");
    }

    @Override
    public Evaluacion toModel(int moduloId) {
        Evaluacion evaluacion = new Evaluacion(moduloId,
                                              Evaluacion.TipoEvaluacion.QUIZ,
                                              nombre, getPuntajeMaximo());
        evaluacion.setDescripcion(descripcion);
        evaluacion.setIntentosPermitidos(getIntentosPermitidos());
        evaluacion.setTiempoLimiteMinutos(getTiempoLimiteMinutos());
        evaluacion.setPesoPorcentual(getPesoPorcentual());
        return evaluacion;
    }
}
