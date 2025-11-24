package prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * Prototipo de Modulo - Permite clonar modulos con sus clases y evaluaciones.
 */
public class ModuloPrototype implements IPrototype<ModuloPrototype> {

    private int id;
    private String nombre;
    private String descripcion;
    private int orden;
    private List<ClasePrototype> clases;
    private List<EvaluacionPrototype> evaluaciones;

    public ModuloPrototype() {
        this.clases = new ArrayList<>();
        this.evaluaciones = new ArrayList<>();
    }

    public ModuloPrototype(String nombre, String descripcion, int orden) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.orden = orden;
    }

    @Override
    public ModuloPrototype clonar() {
        ModuloPrototype clon = new ModuloPrototype();
        clon.id = 0; // Nuevo modulo
        clon.nombre = this.nombre;
        clon.descripcion = this.descripcion;
        clon.orden = this.orden;

        // Clonar clases
        for (ClasePrototype clase : this.clases) {
            clon.clases.add(clase.clonar());
        }

        // Clonar evaluaciones
        for (EvaluacionPrototype eval : this.evaluaciones) {
            clon.evaluaciones.add(eval.clonar());
        }

        return clon;
    }

    @Override
    public ModuloPrototype clonarParaNuevoPeriodo(String nuevoPeriodo) {
        return clonar(); // Los modulos no dependen del periodo
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }

    public List<ClasePrototype> getClases() { return clases; }
    public void agregarClase(ClasePrototype clase) { this.clases.add(clase); }

    public List<EvaluacionPrototype> getEvaluaciones() { return evaluaciones; }
    public void agregarEvaluacion(EvaluacionPrototype eval) { this.evaluaciones.add(eval); }

    @Override
    public String toString() {
        return "Modulo " + orden + ": " + nombre;
    }
}
