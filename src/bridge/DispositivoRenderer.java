package bridge;

/**
 * PATRÓN BRIDGE - Implementor
 * Define la interfaz para las implementaciones concretas de renderizado
 * Cada dispositivo (web, móvil, TV) implementará esta interfaz
 */
public interface DispositivoRenderer {
    // Renderizar un curso
    void renderizarCurso(String nombre, String descripcion, String tipo);

    // Renderizar un módulo
    void renderizarModulo(String titulo, int orden);

    // Renderizar material educativo
    void renderizarMaterial(String nombre, String tipo, String url);

    // Renderizar evaluación
    void renderizarEvaluacion(String nombre, String tipo, int puntajeMaximo);

    // Mostrar notificación
    void mostrarNotificacion(String mensaje, String tipo);

    // Obtener nombre del dispositivo
    String getNombreDispositivo();
}
