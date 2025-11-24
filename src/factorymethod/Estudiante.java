package factorymethod;

/**
 * ProductoConcreto - Clase que representa un usuario tipo Estudiante
 * Implementa la interfaz IUsuario con comportamiento específico para estudiantes
 */
public class Estudiante implements IUsuario {

    private int id;
    private String nombre;
    private String apellidos;
    private String correo;
    private String contrasena;
    private String matricula;
    private String carrera;

    public Estudiante() {
    }

    public Estudiante(String nombre, String apellidos, String correo, String contrasena) {
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
        return "Estudiante";
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    @Override
    public String mostrarInformacion() {
        return "=== ESTUDIANTE ===" +
               "\nID: " + id +
               "\nNombre: " + nombre + " " + apellidos +
               "\nCorreo: " + correo +
               "\nMatrícula: " + (matricula != null ? matricula : "No asignada") +
               "\nCarrera: " + (carrera != null ? carrera : "No asignada");
    }

    @Override
    public String obtenerPermisos() {
        return "Permisos de Estudiante:\n" +
               "- Ver cursos disponibles\n" +
               "- Inscribirse a cursos\n" +
               "- Ver materiales de cursos inscritos\n" +
               "- Realizar evaluaciones\n" +
               "- Ver calificaciones";
    }
}
