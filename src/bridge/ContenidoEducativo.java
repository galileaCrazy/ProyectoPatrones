package bridge;

/**
 * PATRÓN BRIDGE - Abstraction
 * Clase abstracta que define la abstracción del contenido educativo
 * Contiene referencia al implementor (DispositivoRenderer)
 */
public abstract class ContenidoEducativo {
    // Referencia al implementor - EL PUENTE
    protected DispositivoRenderer renderer;

    // Constructor que recibe el implementor
    public ContenidoEducativo(DispositivoRenderer renderer) {
        this.renderer = renderer;
    }

    // Cambiar el renderer en tiempo de ejecución
    public void setRenderer(DispositivoRenderer renderer) {
        this.renderer = renderer;
    }

    // Método abstracto que las subclases deben implementar
    public abstract void mostrar();

    // Método para obtener el dispositivo actual
    public String getDispositivoActual() {
        return renderer.getNombreDispositivo();
    }
}
