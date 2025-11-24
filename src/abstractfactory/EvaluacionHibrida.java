package abstractfactory;

import model.Evaluacion;
import java.math.BigDecimal;

/**
 * Producto Concreto: Evaluacion Híbrida
 * Implementación específica de IEvaluacion para cursos híbridos
 * @author USUARIO
 */
public class EvaluacionHibrida implements IEvaluacion {
    private String nombre;
    private String descripcion;

    public EvaluacionHibrida(String nombre, String descripcion) {
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
        return "PROYECTO";
    }

    @Override
    public BigDecimal getPuntajeMaximo() {
        return new BigDecimal("100.00");
    }

    @Override
    public int getIntentosPermitidos() {
        return 2; // Permite reentrega
    }

    @Override
    public Integer getTiempoLimiteMinutos() {
        return null; // Proyectos no tienen límite estricto
    }

    @Override
    public BigDecimal getPesoPorcentual() {
        return new BigDecimal("40.00");
    }

    @Override
    public void mostrarInfo() {
        System.out.println("  └─ Evaluación: " + nombre);
        System.out.println("     Tipo: " + getTipoEvaluacion());
        System.out.println("     Intentos: " + getIntentosPermitidos());
        System.out.println("     Tiempo límite: Sin límite estricto");
        System.out.println("     Peso: " + getPesoPorcentual() + "%");
    }

    @Override
    public Evaluacion toModel(int moduloId) {
        Evaluacion evaluacion = new Evaluacion(moduloId,
                                              Evaluacion.TipoEvaluacion.PROYECTO,
                                              nombre, getPuntajeMaximo());
        evaluacion.setDescripcion(descripcion);
        evaluacion.setIntentosPermitidos(getIntentosPermitidos());
        evaluacion.setTiempoLimiteMinutos(getTiempoLimiteMinutos());
        evaluacion.setPesoPorcentual(getPesoPorcentual());
        return evaluacion;
    }
}
