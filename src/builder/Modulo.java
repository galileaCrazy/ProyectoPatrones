package builder;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un Módulo dentro de un curso.
 * Contiene clases (materiales) y evaluaciones.
 */
public class Modulo {
    private int id;
    private String nombre;
    private String descripcion;
    private int orden;
    private List<Clase> clases;
    private List<EvaluacionModulo> evaluaciones;

    public Modulo() {
        this.clases = new ArrayList<>();
        this.evaluaciones = new ArrayList<>();
    }

    public Modulo(String nombre, String descripcion, int orden) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.orden = orden;
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

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public List<Clase> getClases() {
        return clases;
    }

    public void agregarClase(Clase clase) {
        this.clases.add(clase);
    }

    public List<EvaluacionModulo> getEvaluaciones() {
        return evaluaciones;
    }

    public void agregarEvaluacion(EvaluacionModulo evaluacion) {
        this.evaluaciones.add(evaluacion);
    }

    @Override
    public String toString() {
        return "Modulo{" +
                "nombre='" + nombre + '\'' +
                ", orden=" + orden +
                ", clases=" + clases.size() +
                ", evaluaciones=" + evaluaciones.size() +
                '}';
    }
}
