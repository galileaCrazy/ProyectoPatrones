package com.edulearn.patterns.creational.FactoryMethodUsers;

/**
 * Producto - Interfaz base que define el contrato para todos los tipos de usuarios
 * Esta interfaz representa el "Producto" en el patrón Factory Method
 */
public interface IUsuario {

    /**
     * Obtiene el ID del usuario
     * @return ID del usuario
     */
    int getId();

    /**
     * Establece el ID del usuario
     * @param id ID del usuario
     */
    void setId(int id);

    /**
     * Obtiene el nombre del usuario
     * @return Nombre del usuario
     */
    String getNombre();

    /**
     * Establece el nombre del usuario
     * @param nombre Nombre del usuario
     */
    void setNombre(String nombre);

    /**
     * Obtiene los apellidos del usuario
     * @return Apellidos del usuario
     */
    String getApellidos();

    /**
     * Establece los apellidos del usuario
     * @param apellidos Apellidos del usuario
     */
    void setApellidos(String apellidos);

    /**
     * Obtiene el correo electrónico del usuario
     * @return Correo electrónico del usuario
     */
    String getCorreo();

    /**
     * Establece el correo electrónico del usuario
     * @param correo Correo electrónico del usuario
     */
    void setCorreo(String correo);

    /**
     * Obtiene la contraseña del usuario
     * @return Contraseña del usuario
     */
    String getContrasena();

    /**
     * Establece la contraseña del usuario
     * @param contrasena Contraseña del usuario
     */
    void setContrasena(String contrasena);

    /**
     * Obtiene el tipo de usuario (Estudiante, Profesor, Administrador)
     * @return Tipo de usuario
     */
    String getTipoUsuario();

    /**
     * Muestra información del usuario
     * @return String con la información del usuario
     */
    String mostrarInformacion();

    /**
     * Obtiene los permisos específicos del tipo de usuario
     * @return String con los permisos del usuario
     */
    String obtenerPermisos();
}
