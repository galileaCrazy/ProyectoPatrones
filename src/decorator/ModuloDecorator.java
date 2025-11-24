package decorator;

/**
 * PATRÓN DECORATOR - Base Decorator
 * Clase abstracta que sirve como base para todos los decoradores
 * Mantiene referencia al componente decorado
 */
public abstract class ModuloDecorator implements IModuloEducativo {
    // Referencia al componente decorado (puede ser base o otro decorador)
    protected IModuloEducativo moduloDecorado;

    public ModuloDecorator(IModuloEducativo modulo) {
        this.moduloDecorado = modulo;
    }

    @Override
    public String getNombre() {
        return moduloDecorado.getNombre();
    }

    @Override
    public String getDescripcion() {
        return moduloDecorado.getDescripcion();
    }

    @Override
    public double getCostoAdicional() {
        return moduloDecorado.getCostoAdicional();
    }

    @Override
    public void mostrarInfo() {
        moduloDecorado.mostrarInfo();
    }

    @Override
    public String getCaracteristicas() {
        return moduloDecorado.getCaracteristicas();
    }
}
