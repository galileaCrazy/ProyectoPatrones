package dao;

import factorymethod.IUsuario;
import java.util.List;

/**
 * Interfaz que define las operaciones CRUD para Usuario
 * @author USUARIO
 */
public interface IUsuarioDAO {

    /**
     * Inserta un nuevo usuario en la base de datos
     * @param usuario objeto IUsuario a insertar
     * @return id del usuario insertado, -1 si falla
     */
    int insertar(IUsuario usuario);

    /**
     * Actualiza un usuario existente
     * @param usuario objeto IUsuario con datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    boolean actualizar(IUsuario usuario);

    /**
     * Elimina un usuario por su ID
     * @param id identificador del usuario
     * @return true si se eliminó correctamente, false en caso contrario
     */
    boolean eliminar(int id);

    /**
     * Obtiene un usuario por su ID
     * @param id identificador del usuario
     * @return objeto IUsuario si existe, null en caso contrario
     */
    IUsuario obtenerPorId(int id);

    /**
     * Obtiene un usuario por su correo electrónico
     * @param correo correo electrónico del usuario
     * @return objeto IUsuario si existe, null en caso contrario
     */
    IUsuario obtenerPorCorreo(String correo);

    /**
     * Valida las credenciales de login de un usuario
     * @param correo correo electrónico del usuario
     * @param contrasena contraseña del usuario
     * @param tipoUsuario tipo de usuario (Estudiante, Profesor, Administrador)
     * @return objeto IUsuario si las credenciales son válidas, null en caso contrario
     */
    IUsuario validarCredenciales(String correo, String contrasena, String tipoUsuario);

    /**
     * Obtiene todos los usuarios
     * @return lista de usuarios
     */
    List<IUsuario> obtenerTodos();

    /**
     * Obtiene usuarios por tipo
     * @param tipoUsuario tipo de usuario (Estudiante, Profesor, Administrador)
     * @return lista de usuarios del tipo especificado
     */
    List<IUsuario> obtenerPorTipo(String tipoUsuario);

    /**
     * Verifica si existe un usuario con el correo especificado
     * @param correo correo electrónico a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existeCorreo(String correo);
}
