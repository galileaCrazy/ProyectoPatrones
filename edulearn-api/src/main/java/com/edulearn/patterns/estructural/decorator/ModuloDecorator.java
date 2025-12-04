package com.edulearn.patterns.estructural.decorator;

/**
 * Clase Decorator Abstracta (Decorador)
 * Mantiene una referencia al componente decorado y define una interfaz
 * que se ajusta a la interfaz del componente.
 */
public abstract class ModuloDecorator implements ModuloEducativo {
    protected ModuloEducativo moduloEducativo;

    public ModuloDecorator(ModuloEducativo moduloEducativo) {
        this.moduloEducativo = moduloEducativo;
    }

    @Override
    public String mostrarContenido() {
        return moduloEducativo.mostrarContenido();
    }
}
