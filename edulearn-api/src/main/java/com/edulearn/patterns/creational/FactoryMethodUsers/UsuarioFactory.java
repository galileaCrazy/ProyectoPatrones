package com.edulearn.patterns.creational.FactoryMethodUsers;
/**
 * Creator - Clase abstracta que define el patrón Factory Method
 * Define cómo se usa el producto (usuario), pero no decide cuál producto crear
 * Las subclases concretas decidirán qué tipo específico de usuario crear
 */
public abstract class UsuarioFactory {

    /**
     * Factory Method - Método abstracto que será implementado por las subclases
     * Cada subclase decidirá qué tipo concreto de usuario crear
     *
     * @param nombre Nombre del usuario
     * @param apellidos Apellidos del usuario
     * @param correo Correo electrónico del usuario
     * @param contrasena Contraseña del usuario
     * @return Una instancia de IUsuario (puede ser Estudiante, Profesor o Administrador)
     */
    public abstract IUsuario crearUsuario(String nombre, String apellidos, String correo, String contrasena);

    /**
     * Método de operación que usa el producto creado por el Factory Method
     * Este método define CÓMO se usa el usuario, pero no decide CUÁL crear
     *
     * @param nombre Nombre del usuario
     * @param apellidos Apellidos del usuario
     * @param correo Correo electrónico del usuario
     * @param contrasena Contraseña del usuario
     * @return El usuario creado y configurado
     */
    public IUsuario registrarUsuario(String nombre, String apellidos, String correo, String contrasena) {
        // Llamada al Factory Method (implementado por las subclases)
        IUsuario usuario = crearUsuario(nombre, apellidos, correo, contrasena);

        // Operaciones comunes para todos los tipos de usuario
        System.out.println("Registrando usuario: " + usuario.getTipoUsuario());
        System.out.println("Correo: " + correo);

        // Aquí podrías agregar lógica adicional como:
        // - Validar el correo
        // - Encriptar la contraseña
        // - Guardar en base de datos
        // - Enviar email de confirmación

        return usuario;
    }

    /**
     * Método auxiliar para mostrar la información del usuario creado
     * Demuestra cómo se UTILIZA el producto sin conocer su tipo concreto
     *
     * @param usuario Usuario del cual mostrar información
     */
    public void mostrarDetallesUsuario(IUsuario usuario) {
        System.out.println("\n" + usuario.mostrarInformacion());
        System.out.println("\n" + usuario.obtenerPermisos());
    }

    /**
     * Método para validar credenciales (ejemplo de uso del producto)
     *
     * @param usuario Usuario a validar
     * @param correo Correo a validar
     * @param contrasena Contraseña a validar
     * @return true si las credenciales son válidas
     */
    public boolean validarCredenciales(IUsuario usuario, String correo, String contrasena) {
        return usuario.getCorreo().equals(correo) &&
               usuario.getContrasena().equals(contrasena);
    }
}
