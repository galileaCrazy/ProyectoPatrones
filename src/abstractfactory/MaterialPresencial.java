package abstractfactory;

import model.Material;

/**
 * Producto Concreto: Material Presencial
 * Implementación específica de IMaterial para cursos presenciales
 * @author USUARIO
 */
public class MaterialPresencial implements IMaterial {
    private String nombre;
    private String descripcion;

    public MaterialPresencial(String nombre, String descripcion) {
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
        return "PDF";
    }

    @Override
    public boolean requiereVisualizacion() {
        return false; // Material físico/descargable
    }

    @Override
    public Integer getDuracionSegundos() {
        return null; // PDFs no tienen duración
    }

    @Override
    public void mostrarInfo() {
        System.out.println("  ├─ Material: " + nombre);
        System.out.println("  │  Tipo: " + getTipoMaterial());
        System.out.println("  │  Visualización obligatoria: No");
    }

    @Override
    public Material toModel(int moduloId) {
        Material material = new Material(moduloId, Material.TipoMaterial.PDF,
                                        nombre, descripcion);
        material.setRequiereVisualizacion(false);
        material.setActivo(true);
        return material;
    }
}
