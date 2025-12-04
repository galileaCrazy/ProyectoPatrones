package com.edulearn.patterns.creational.FactoryMethodUsers;
/**
 * ConcreteCreator - Factory concreta para crear usuarios tipo Profesor
 * Implementa el Factory Method decidiendo crear específicamente un Profesor
 */
public class ProfesorFactory extends UsuarioFactory {

    /**
     * Implementación del Factory Method
     * Esta clase DECIDE crear un producto concreto: Profesor
     *
     * @param nombre Nombre del profesor
     * @param apellidos Apellidos del profesor
     * @param correo Correo electrónico del profesor
     * @param contrasena Contraseña del profesor
     * @return Una nueva instancia de Profesor
     */
    @Override
    public IUsuario crearUsuario(String nombre, String apellidos, String correo, String contrasena) {
        System.out.println("ProfesorFactory: Creando un nuevo Profesor...");

        // Crear la instancia específica de Profesor
        Profesor profesor = new Profesor(nombre, apellidos, correo, contrasena);

        // Configuración adicional específica para profesores
        // Por ejemplo, asignar un departamento por defecto
        profesor.setDepartamento("Por asignar");

        return profesor;
    }
}
