package bridge;

/**
 * PATRÓN BRIDGE - Refined Abstraction
 * Material educativo renderizable en múltiples dispositivos
 */
public class MaterialBridge extends ContenidoEducativo {
    private String nombre;
    private String tipo;
    private String url;

    public MaterialBridge(DispositivoRenderer renderer, String nombre, String tipo, String url) {
        super(renderer);
        this.nombre = nombre;
        this.tipo = tipo;
        this.url = url;
    }

    @Override
    public void mostrar() {
        renderer.renderizarMaterial(nombre, tipo, url);
    }

    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
}
