package dao;

import model.Evaluacion;
import java.util.List;

/**
 * Interfaz que define las operaciones CRUD para Evaluacion
 * @author USUARIO
 */
public interface IEvaluacionDAO {
    /**
     * Inserta una nueva evaluación en la base de datos
     * @param evaluacion objeto Evaluacion a insertar
     * @return id de la evaluación insertada, -1 si falla
     */
    int insertar(Evaluacion evaluacion);

    /**
     * Actualiza una evaluación existente
     * @param evaluacion objeto Evaluacion con datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    boolean actualizar(Evaluacion evaluacion);

    /**
     * Elimina una evaluación por su ID
     * @param id identificador de la evaluación
     * @return true si se eliminó correctamente, false en caso contrario
     */
    boolean eliminar(int id);

    /**
     * Obtiene una evaluación por su ID
     * @param id identificador de la evaluación
     * @return objeto Evaluacion si existe, null en caso contrario
     */
    Evaluacion obtenerPorId(int id);

    /**
     * Obtiene todas las evaluaciones de un módulo
     * @param moduloId identificador del módulo
     * @return lista de evaluaciones del módulo
     */
    List<Evaluacion> obtenerPorModulo(int moduloId);

    /**
     * Obtiene todas las evaluaciones
     * @return lista de evaluaciones
     */
    List<Evaluacion> obtenerTodos();

    /**
     * Obtiene evaluaciones por tipo
     * @param tipo tipo de evaluación
     * @return lista de evaluaciones del tipo especificado
     */
    List<Evaluacion> obtenerPorTipo(Evaluacion.TipoEvaluacion tipo);
}
