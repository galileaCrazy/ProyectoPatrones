package abstractfactory;

import model.Evaluacion;
import java.math.BigDecimal;

/**
 * Producto Concreto: Evaluacion Presencial
 * Implementación específica de IEvaluacion para cursos presenciales
 * @author USUARIO
 */
public class EvaluacionPresencial implements IEvaluacion {
    private String nombre;
    private String descripcion;

    public EvaluacionPresencial(String nombre, String descripcion) {
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
        return "EXAMEN";
    }

    @Override
    public BigDecimal getPuntajeMaximo() {
        return new BigDecimal("100.00");
    }

    @Override
    public int getIntentosPermitidos() {
        return 1; // Solo un intento en exámenes presenciales
    }

    @Override
    public Integer getTiempoLimiteMinutos() {
        return 120; // 2 horas
    }

    @Override
    public BigDecimal getPesoPorcentual() {
        return new BigDecimal("30.00");
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
                                              Evaluacion.TipoEvaluacion.EXAMEN,
                                              nombre, getPuntajeMaximo());
        evaluacion.setDescripcion(descripcion);
        evaluacion.setIntentosPermitidos(getIntentosPermitidos());
        evaluacion.setTiempoLimiteMinutos(getTiempoLimiteMinutos());
        evaluacion.setPesoPorcentual(getPesoPorcentual());
        return evaluacion;
    }
}
