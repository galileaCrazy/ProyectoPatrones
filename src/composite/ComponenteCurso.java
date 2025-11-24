package composite;

/**
 * PATRÓN COMPOSITE - Component
 * Interface común para elementos simples (hojas) y compuestos
 * Permite tratar uniformemente unidades, lecciones y actividades
 */
public interface ComponenteCurso {
    // Obtener nombre del componente
    String getNombre();

    // Obtener descripción
    String getDescripcion();

    // Mostrar estructura del componente
    void mostrar(int nivel);

    // Obtener duración total en minutos
    int getDuracionTotal();

    // Verificar si es un contenedor
    boolean esContenedor();

    // Obtener el ID
    int getId();
}
