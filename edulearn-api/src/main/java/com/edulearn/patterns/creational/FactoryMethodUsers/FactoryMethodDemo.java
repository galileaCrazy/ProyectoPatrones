package com.edulearn.patterns.creational.FactoryMethodUsers;

/**
 * Clase de utilidad para el patrón Factory Method
 * Proporciona un método estático para obtener la factory correcta según el tipo de usuario
 * Esta clase facilita la integración del patrón con las vistas del sistema
 */
public class FactoryMethodDemo {

    /**
     * Obtiene la factory correspondiente según el tipo de usuario
     * Este método centraliza la lógica de selección de factory
     *
     * @param tipoUsuario Tipo de usuario: "Estudiante", "Profesor" o "Administrador"
     * @return La factory correspondiente al tipo de usuario
     * @throws IllegalArgumentException si el tipo de usuario no es válido
     */
    public static UsuarioFactory obtenerFactory(String tipoUsuario) {
        if (tipoUsuario == null || tipoUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de usuario no puede ser nulo o vacío");
        }

        switch (tipoUsuario.trim()) {
            case "Estudiante":
            case "estudiante":
                return new EstudianteFactory();

            case "Profesor":
            case "profesor":
                return new ProfesorFactory();

            case "Administrador":
            case "administrador":
                return new AdministradorFactory();

            default:
                throw new IllegalArgumentException("Tipo de usuario no válido: " + tipoUsuario +
                    ". Tipos válidos: Estudiante, Profesor, Administrador");
        }
    }

    /**
     * Método de demostración del patrón Factory Method
     * Muestra cómo se utilizan las factories para crear diferentes tipos de usuarios
     */
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("  DEMOSTRACIÓN DEL PATRÓN FACTORY METHOD");
        System.out.println("  Sistema de Gestión de Usuarios");
        System.out.println("=".repeat(60));

        try {
            // Demostración 1: Crear un Estudiante
            System.out.println("\n--- Creando un Estudiante ---");
            UsuarioFactory factoryEstudiante = obtenerFactory("Estudiante");
            IUsuario estudiante = factoryEstudiante.registrarUsuario(
                "Juan", "Pérez García", "juan@universidad.edu", "password123"
            );
            factoryEstudiante.mostrarDetallesUsuario(estudiante);

            // Demostración 2: Crear un Profesor
            System.out.println("\n--- Creando un Profesor ---");
            UsuarioFactory factoryProfesor = obtenerFactory("Profesor");
            IUsuario profesor = factoryProfesor.registrarUsuario(
                "María", "González López", "maria@universidad.edu", "prof456"
            );
            factoryProfesor.mostrarDetallesUsuario(profesor);

            // Demostración 3: Crear un Administrador
            System.out.println("\n--- Creando un Administrador ---");
            UsuarioFactory factoryAdmin = obtenerFactory("Administrador");
            IUsuario admin = factoryAdmin.registrarUsuario(
                "Carlos", "Rodríguez", "carlos@universidad.edu", "admin789"
            );
            factoryAdmin.mostrarDetallesUsuario(admin);

            // Demostración del polimorfismo
            System.out.println("\n" + "=".repeat(60));
            System.out.println("  DEMOSTRACIÓN DE POLIMORFISMO");
            System.out.println("=".repeat(60));

            IUsuario[] usuarios = {estudiante, profesor, admin};
            for (IUsuario usuario : usuarios) {
                System.out.println("\n[" + usuario.getTipoUsuario().toUpperCase() + "]");
                System.out.println("Nombre completo: " + usuario.getNombre() + " " + usuario.getApellidos());
                System.out.println("Correo: " + usuario.getCorreo());
            }

            // Demostración de validación de credenciales
            System.out.println("\n" + "=".repeat(60));
            System.out.println("  VALIDACIÓN DE CREDENCIALES");
            System.out.println("=".repeat(60));

            boolean credencialesValidas = factoryEstudiante.validarCredenciales(
                estudiante, "juan@universidad.edu", "password123"
            );
            System.out.println("\nValidando credenciales de estudiante:");
            System.out.println("Resultado: " + (credencialesValidas ? "VÁLIDAS" : "INVÁLIDAS"));

        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("  FIN DE LA DEMOSTRACIÓN");
        System.out.println("=".repeat(60));
    }
}
