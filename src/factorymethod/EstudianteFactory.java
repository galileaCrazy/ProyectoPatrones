package factorymethod;

/**
 * ConcreteCreator - Factory concreta para crear usuarios tipo Estudiante
 * Implementa el Factory Method decidiendo crear específicamente un Estudiante
 */
public class EstudianteFactory extends UsuarioFactory {

    /**
     * Implementación del Factory Method
     * Esta clase DECIDE crear un producto concreto: Estudiante
     *
     * @param nombre Nombre del estudiante
     * @param apellidos Apellidos del estudiante
     * @param correo Correo electrónico del estudiante
     * @param contrasena Contraseña del estudiante
     * @return Una nueva instancia de Estudiante
     */
    @Override
    public IUsuario crearUsuario(String nombre, String apellidos, String correo, String contrasena) {
        System.out.println("EstudianteFactory: Creando un nuevo Estudiante...");

        // Crear la instancia específica de Estudiante
        Estudiante estudiante = new Estudiante(nombre, apellidos, correo, contrasena);

        // Configuración adicional específica para estudiantes
        // Por ejemplo, generar matrícula automáticamente
        estudiante.setMatricula(generarMatricula());

        return estudiante;
    }

    /**
     * Método auxiliar para generar una matrícula única
     * @return Matrícula generada
     */
    private String generarMatricula() {
        // Generar matrícula con formato: EST + timestamp
        return "EST" + System.currentTimeMillis();
    }
}
