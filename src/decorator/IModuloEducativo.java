package decorator;

/**
 * PATRÓN DECORATOR - Component Interface
 * Define la interfaz base para módulos educativos
 * Tanto el componente concreto como los decoradores implementan esta interfaz
 */
public interface IModuloEducativo {
    // Obtener nombre del módulo
    String getNombre();

    // Obtener descripción con todas las funcionalidades
    String getDescripcion();

    // Obtener costo adicional del módulo
    double getCostoAdicional();

    // Mostrar información del módulo
    void mostrarInfo();

    // Obtener lista de características
    String getCaracteristicas();
}
