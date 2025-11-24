package abstractfactory;

import model.Material;

/**
 * Producto Concreto: Material Virtual
 * Implementación específica de IMaterial para cursos virtuales
 * @author USUARIO
 */
public class MaterialVirtual implements IMaterial {
    private String nombre;
    private String descripcion;

    public MaterialVirtual(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
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
    public String getTipoMaterial() {
        return "VIDEO";
    }

    @Override
    public boolean requiereVisualizacion() {
        return true; // Videos requieren visualización completa
    }

    @Override
    public Integer getDuracionSegundos() {
        return 1800; // 30 minutos por defecto
    }

    @Override
    public void mostrarInfo() {
        System.out.println("  ├─ Material: " + nombre);
        System.out.println("  │  Tipo: " + getTipoMaterial());
        System.out.println("  │  Duración: " + (getDuracionSegundos() / 60) + " minutos");
        System.out.println("  │  Visualización obligatoria: Sí");
    }

    @Override
    public Material toModel(int moduloId) {
        Material material = new Material(moduloId, Material.TipoMaterial.VIDEO,
                                        nombre, descripcion);
        material.setRequiereVisualizacion(true);
        material.setDuracionSegundos(getDuracionSegundos());
        material.setActivo(true);
        return material;
    }
}
