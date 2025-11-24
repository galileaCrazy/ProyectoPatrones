package adapter;

/**
 * PATRÓN ADAPTER - Interface Target
 * Define la interfaz que el sistema LMS espera para videoconferencias
 * Permite integrar diferentes herramientas externas de forma uniforme
 */
public interface IVideoconferencia {
    // Crear una reunión y obtener el enlace
    String crearReunion(String titulo, int duracionMinutos);

    // Obtener el enlace de acceso a la reunión
    String obtenerEnlace();

    // Iniciar la reunión
    void iniciarReunion();

    // Finalizar la reunión
    void finalizarReunion();

    // Obtener información del proveedor
    String getProveedor();
}
