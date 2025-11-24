package composite;

/**
 * PATRÓN COMPOSITE - Leaf (Hoja)
 * Representa una actividad individual que no puede contener otros elementos
 * Es el elemento más básico de la estructura del curso
 */
public class Actividad implements ComponenteCurso {
    private int id;
    private String nombre;
    private String descripcion;
    private String tipo; // video, lectura, quiz, practica
    private int duracionMinutos;
    private String urlRecurso;

    public Actividad(String nombre, String tipo, int duracionMinutos) {
        this.id = (int)(Math.random() * 10000);
        this.nombre = nombre;
        this.tipo = tipo;
        this.duracionMinutos = duracionMinutos;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public void mostrar(int nivel) {
        String indentacion = "  ".repeat(nivel);
        String icono = obtenerIcono();
        System.out.println(indentacion + icono + " " + nombre + " (" + duracionMinutos + " min)");
    }

    private String obtenerIcono() {
        switch (tipo) {
            case "video": return "▶";
            case "lectura": return "📖";
            case "quiz": return "✎";
            case "practica": return "💻";
            default: return "•";
        }
    }

    @Override
    public int getDuracionTotal() {
        return duracionMinutos;
    }

    @Override
    public boolean esContenedor() {
        return false; // Las actividades son hojas, no contenedores
    }

    @Override
    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setUrlRecurso(String url) {
        this.urlRecurso = url;
    }

    public String getUrlRecurso() {
        return urlRecurso;
    }
}
