package composite;

import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN COMPOSITE - Composite
 * Representa una lección que puede contener múltiples actividades
 * Es un contenedor de nivel medio en la jerarquía
 */
public class Leccion implements ComponenteCurso {
    private int id;
    private String nombre;
    private String descripcion;
    private List<ComponenteCurso> componentes; // Puede contener actividades

    public Leccion(String nombre) {
        this.id = (int)(Math.random() * 10000);
        this.nombre = nombre;
        this.componentes = new ArrayList<>();
    }

    // Agregar componente hijo
    public void agregar(ComponenteCurso componente) {
        componentes.add(componente);
    }

    // Remover componente hijo
    public void remover(ComponenteCurso componente) {
        componentes.remove(componente);
    }

    // Obtener hijos
    public List<ComponenteCurso> getHijos() {
        return componentes;
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
        System.out.println(indentacion + "📚 " + nombre + " [" + componentes.size() + " actividades, " + getDuracionTotal() + " min]");

        // Mostrar todos los hijos recursivamente
        for (ComponenteCurso componente : componentes) {
            componente.mostrar(nivel + 1);
        }
    }

    @Override
    public int getDuracionTotal() {
        // Sumar duración de todos los componentes hijos
        int total = 0;
        for (ComponenteCurso componente : componentes) {
            total += componente.getDuracionTotal();
        }
        return total;
    }

    @Override
    public boolean esContenedor() {
        return true;
    }

    @Override
    public int getId() {
        return id;
    }
}
