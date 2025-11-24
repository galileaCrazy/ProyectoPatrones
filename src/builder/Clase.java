package builder;

/**
 * Representa una Clase/Lección dentro de un módulo.
 */
public class Clase {
    private int id;
    private String nombre;
    private String descripcion;
    private String contenido;
    private TipoClase tipo;
    private int duracionMinutos;
    private int orden;
    private String urlRecurso;

    public enum TipoClase {
        VIDEO("video"),
        LECTURA("lectura"),
        PRACTICA("practica"),
        INTERACTIVA("interactiva");

        private String valor;

        TipoClase(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }
    }

    public Clase() {
        this.orden = 0;
        this.duracionMinutos = 0;
    }

    public Clase(String nombre, String descripcion, TipoClase tipo) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipo = tipo;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public TipoClase getTipo() {
        return tipo;
    }

    public void setTipo(TipoClase tipo) {
        this.tipo = tipo;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getUrlRecurso() {
        return urlRecurso;
    }

    public void setUrlRecurso(String urlRecurso) {
        this.urlRecurso = urlRecurso;
    }

    @Override
    public String toString() {
        return "Clase{" +
                "nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                ", duracionMinutos=" + duracionMinutos +
                ", orden=" + orden +
                '}';
    }
}
