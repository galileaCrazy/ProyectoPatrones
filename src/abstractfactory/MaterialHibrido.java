package abstractfactory;

import model.Material;

/**
 * Producto Concreto: Material Híbrido
 * Implementación específica de IMaterial para cursos híbridos
 * @author USUARIO
 */
public class MaterialHibrido implements IMaterial {
    private String nombre;
    private String descripcion;

    public MaterialHibrido(String nombre, String descripcion) {
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
        return "DOCUMENTO";
    }

    @Override
    public boolean requiereVisualizacion() {
        return false; // Documentos interactivos opcionales
    }

    @Override
    public Integer getDuracionSegundos() {
        return null; // Documentos no tienen duración definida
    }

    @Override
    public void mostrarInfo() {
        System.out.println("  ├─ Material: " + nombre);
        System.out.println("  │  Tipo: " + getTipoMaterial());
        System.out.println("  │  Visualización obligatoria: No");
    }

    @Override
    public Material toModel(int moduloId) {
        Material material = new Material(moduloId, Material.TipoMaterial.DOCUMENTO,
                                        nombre, descripcion);
        material.setRequiereVisualizacion(false);
        material.setActivo(true);
        return material;
    }
}
