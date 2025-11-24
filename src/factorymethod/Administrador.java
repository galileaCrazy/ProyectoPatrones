package factorymethod;

/**
 * ProductoConcreto - Clase que representa un usuario tipo Administrador
 * Implementa la interfaz IUsuario con comportamiento específico para administradores
 */
public class Administrador implements IUsuario {

    private int id;
    private String nombre;
    private String apellidos;
    private String correo;
    private String contrasena;
    private String nivelAcceso;
    private String area;

    public Administrador() {
    }

    public Administrador(String nombre, String apellidos, String correo, String contrasena) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.contrasena = contrasena;
        this.nivelAcceso = "ALTO"; // Por defecto, nivel alto para administradores
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getApellidos() {
        return apellidos;
    }

    @Override
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    @Override
    public String getCorreo() {
        return correo;
    }

    @Override
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public String getContrasena() {
        return contrasena;
    }

    @Override
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String getTipoUsuario() {
        return "Administrador";
    }

    public String getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(String nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public String mostrarInformacion() {
        return "=== ADMINISTRADOR ===" +
               "\nID: " + id +
               "\nNombre: " + nombre + " " + apellidos +
               "\nCorreo: " + correo +
               "\nNivel de Acceso: " + nivelAcceso +
               "\nÁrea: " + (area != null ? area : "No asignada");
    }

    @Override
    public String obtenerPermisos() {
        return "Permisos de Administrador:\n" +
               "- Gestión completa de usuarios\n" +
               "- Gestión completa de cursos\n" +
               "- Configuración del sistema\n" +
               "- Acceso a reportes y estadísticas\n" +
               "- Gestión de permisos y roles\n" +
               "- Backup y restauración de datos";
    }
}
