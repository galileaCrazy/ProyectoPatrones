package factorymethod;

/**
 * ProductoConcreto - Clase que representa un usuario tipo Profesor
 * Implementa la interfaz IUsuario con comportamiento específico para profesores
 */
public class Profesor implements IUsuario {

    private int id;
    private String nombre;
    private String apellidos;
    private String correo;
    private String contrasena;
    private String departamento;
    private String especialidad;

    public Profesor() {
    }

    public Profesor(String nombre, String apellidos, String correo, String contrasena) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.contrasena = contrasena;
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
        return "Profesor";
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    @Override
    public String mostrarInformacion() {
        return "=== PROFESOR ===" +
               "\nID: " + id +
               "\nNombre: " + nombre + " " + apellidos +
               "\nCorreo: " + correo +
               "\nDepartamento: " + (departamento != null ? departamento : "No asignado") +
               "\nEspecialidad: " + (especialidad != null ? especialidad : "No asignada");
    }

    @Override
    public String obtenerPermisos() {
        return "Permisos de Profesor:\n" +
               "- Crear y gestionar cursos\n" +
               "- Subir materiales de curso\n" +
               "- Crear y calificar evaluaciones\n" +
               "- Ver lista de estudiantes inscritos\n" +
               "- Generar reportes de desempeño";
    }
}
