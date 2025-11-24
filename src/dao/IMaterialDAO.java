package dao;

import model.Material;
import java.util.List;

/**
 * Interfaz que define las operaciones CRUD para Material
 * @author USUARIO
 */
public interface IMaterialDAO {
    /**
     * Inserta un nuevo material en la base de datos
     * @param material objeto Material a insertar
     * @return id del material insertado, -1 si falla
     */
    int insertar(Material material);

    /**
     * Actualiza un material existente
     * @param material objeto Material con datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    boolean actualizar(Material material);

    /**
     * Elimina un material por su ID
     * @param id identificador del material
     * @return true si se eliminó correctamente, false en caso contrario
     */
    boolean eliminar(int id);

    /**
     * Obtiene un material por su ID
     * @param id identificador del material
     * @return objeto Material si existe, null en caso contrario
     */
    Material obtenerPorId(int id);

    /**
     * Obtiene todos los materiales de un módulo
     * @param moduloId identificador del módulo
     * @return lista de materiales del módulo
     */
    List<Material> obtenerPorModulo(int moduloId);

    /**
     * Obtiene todos los materiales
     * @return lista de materiales
     */
    List<Material> obtenerTodos();

    /**
     * Obtiene materiales por tipo
     * @param tipo tipo de material
     * @return lista de materiales del tipo especificado
     */
    List<Material> obtenerPorTipo(Material.TipoMaterial tipo);
}
