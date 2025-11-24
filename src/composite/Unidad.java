package composite;

import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN COMPOSITE - Composite
 * Representa una unidad que puede contener lecciones y otras unidades
 * Es el contenedor de nivel superior en la jerarquía
 */
public class Unidad implements ComponenteCurso {
    private int id;
    private String nombre;
    private String descripcion;
    private int orden;
    private List<ComponenteCurso> componentes; // Puede contener lecciones u otras unidades

    public Unidad(String nombre, int orden) {
        this.id = (int)(Math.random() * 10000);
        this.nombre = nombre;
        this.orden = orden;
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
        System.out.println(indentacion + "📁 UNIDAD " + orden + ": " + nombre);
        System.out.println(indentacion + "   Duración total: " + getDuracionTotal() + " minutos");

        // Mostrar todos los hijos recursivamente
        for (ComponenteCurso componente : componentes) {
            componente.mostrar(nivel + 1);
        }
    }

    @Override
    public int getDuracionTotal() {
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

    public int getOrden() {
        return orden;
    }
}
