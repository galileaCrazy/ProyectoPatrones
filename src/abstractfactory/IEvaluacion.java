package abstractfactory;

import java.math.BigDecimal;

/**
 * Producto Abstracto: Interfaz para Evaluacion
 * Define el contrato que deben cumplir todos los tipos de evaluaciones
 * @author USUARIO
 */
public interface IEvaluacion {
    /**
     * Obtiene el nombre de la evaluación
     */
    String getNombre();

    /**
     * Obtiene la descripción de la evaluación
     */
    String getDescripcion();

    /**
     * Obtiene el tipo de evaluación
     */
    String getTipoEvaluacion();

    /**
     * Obtiene el puntaje máximo
     */
    BigDecimal getPuntajeMaximo();

    /**
     * Obtiene los intentos permitidos
     */
    int getIntentosPermitidos();

    /**
     * Obtiene el tiempo límite en minutos
     */
    Integer getTiempoLimiteMinutos();

    /**
     * Obtiene el peso porcentual
     */
    BigDecimal getPesoPorcentual();

    /**
     * Muestra la información de la evaluación
     */
    void mostrarInfo();

    /**
     * Convierte el producto abstracto a modelo de base de datos
     */
    model.Evaluacion toModel(int moduloId);
}
