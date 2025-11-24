package prototype;

/**
 * Prototipo de Clase - Permite clonar clases/lecciones.
 */
public class ClasePrototype implements IPrototype<ClasePrototype> {

    private int id;
    private String nombre;
    private String descripcion;
    private String tipo; // VIDEO, LECTURA, PRACTICA, INTERACTIVA
    private int duracionMinutos;
    private int orden;
    private String urlRecurso;

    public ClasePrototype() {}

    public ClasePrototype(String nombre, String tipo, int duracionMinutos, int orden) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.duracionMinutos = duracionMinutos;
        this.orden = orden;
    }

    @Override
    public ClasePrototype clonar() {
        ClasePrototype clon = new ClasePrototype();
        clon.id = 0; // Nueva clase
        clon.nombre = this.nombre;
        clon.descripcion = this.descripcion;
        clon.tipo = this.tipo;
        clon.duracionMinutos = this.duracionMinutos;
        clon.orden = this.orden;
        clon.urlRecurso = this.urlRecurso;
        return clon;
    }

    @Override
    public ClasePrototype clonarParaNuevoPeriodo(String nuevoPeriodo) {
        return clonar();
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(int duracion) { this.duracionMinutos = duracion; }

    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }

    public String getUrlRecurso() { return urlRecurso; }
    public void setUrlRecurso(String url) { this.urlRecurso = url; }

    @Override
    public String toString() {
        return nombre + " [" + tipo + "] " + duracionMinutos + "min";
    }
}
