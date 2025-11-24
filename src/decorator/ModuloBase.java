package decorator;

/**
 * PATRÓN DECORATOR - Concrete Component
 * Módulo educativo básico sin funcionalidades adicionales
 * Este es el objeto que será decorado
 */
public class ModuloBase implements IModuloEducativo {
    private int id;
    private String nombre;
    private String descripcion;
    private int cursoId;

    public ModuloBase(String nombre, String descripcion) {
        this.id = (int)(Math.random() * 10000);
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public ModuloBase(int id, String nombre, String descripcion, int cursoId) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cursoId = cursoId;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public double getCostoAdicional() {
        return 0.0; // Módulo base no tiene costo adicional
    }

    @Override
    public void mostrarInfo() {
        System.out.println("═══════════════════════════════════════");
        System.out.println("MÓDULO: " + nombre);
        System.out.println("Descripción: " + descripcion);
        System.out.println("Características: " + getCaracteristicas());
        System.out.println("Costo adicional: $" + getCostoAdicional());
        System.out.println("═══════════════════════════════════════");
    }

    @Override
    public String getCaracteristicas() {
        return "Módulo básico";
    }

    public int getId() {
        return id;
    }

    public int getCursoId() {
        return cursoId;
    }
}
