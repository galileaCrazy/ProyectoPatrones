package factorymethod;

/**
 * ConcreteCreator - Factory concreta para crear usuarios tipo Administrador
 * Implementa el Factory Method decidiendo crear específicamente un Administrador
 */
public class AdministradorFactory extends UsuarioFactory {

    /**
     * Implementación del Factory Method
     * Esta clase DECIDE crear un producto concreto: Administrador
     *
     * @param nombre Nombre del administrador
     * @param apellidos Apellidos del administrador
     * @param correo Correo electrónico del administrador
     * @param contrasena Contraseña del administrador
     * @return Una nueva instancia de Administrador
     */
    @Override
    public IUsuario crearUsuario(String nombre, String apellidos, String correo, String contrasena) {
        System.out.println("AdministradorFactory: Creando un nuevo Administrador...");

        // Crear la instancia específica de Administrador
        Administrador administrador = new Administrador(nombre, apellidos, correo, contrasena);

        // Configuración adicional específica para administradores
        // El nivel de acceso ya se establece como "ALTO" en el constructor
        administrador.setArea("Administración General");

        return administrador;
    }
}
