package bridge;

/**
 * PATRÓN BRIDGE - Refined Abstraction
 * Evaluación renderizable en múltiples dispositivos
 */
public class EvaluacionBridge extends ContenidoEducativo {
    private String nombre;
    private String tipo;
    private int puntajeMaximo;

    public EvaluacionBridge(DispositivoRenderer renderer, String nombre, String tipo, int puntajeMaximo) {
        super(renderer);
        this.nombre = nombre;
        this.tipo = tipo;
        this.puntajeMaximo = puntajeMaximo;
    }

    @Override
    public void mostrar() {
        renderer.renderizarEvaluacion(nombre, tipo, puntajeMaximo);
    }

    public String getNombre() { return nombre; }
    public String getTipo() { return tipo; }
    public int getPuntajeMaximo() { return puntajeMaximo; }
}
