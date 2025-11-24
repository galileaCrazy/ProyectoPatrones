package bridge;

import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN BRIDGE - Refined Abstraction
 * Módulo que puede renderizarse en cualquier dispositivo
 */
public class ModuloBridge extends ContenidoEducativo {
    private String titulo;
    private int orden;
    private List<MaterialBridge> materiales;
    private List<EvaluacionBridge> evaluaciones;

    public ModuloBridge(DispositivoRenderer renderer, String titulo, int orden) {
        super(renderer);
        this.titulo = titulo;
        this.orden = orden;
        this.materiales = new ArrayList<>();
        this.evaluaciones = new ArrayList<>();
    }

    public void agregarMaterial(MaterialBridge material) {
        materiales.add(material);
    }

    public void agregarEvaluacion(EvaluacionBridge evaluacion) {
        evaluaciones.add(evaluacion);
    }

    @Override
    public void mostrar() {
        renderer.renderizarModulo(titulo, orden);

        for (MaterialBridge material : materiales) {
            material.setRenderer(this.renderer);
            material.mostrar();
        }

        for (EvaluacionBridge evaluacion : evaluaciones) {
            evaluacion.setRenderer(this.renderer);
            evaluacion.mostrar();
        }
    }

    public String getTitulo() { return titulo; }
    public int getOrden() { return orden; }
}
