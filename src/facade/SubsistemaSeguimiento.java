package facade;

/**
 * PATRÓN FACADE - Subsistema de Seguimiento
 * Maneja el progreso y seguimiento del estudiante
 */
public class SubsistemaSeguimiento {

    public void inicializarProgreso(int estudianteId, int cursoId) {
        System.out.println("  [Seguimiento] Inicializando progreso del estudiante " + estudianteId);
    }

    public void mostrarProgreso(int estudianteId, int cursoId) {
        System.out.println("  [Seguimiento] Progreso actual: " + obtenerPorcentajeProgreso(estudianteId, cursoId) + "%");
    }

    public double obtenerPorcentajeProgreso(int estudianteId, int cursoId) {
        return 75.0; // Simulación
    }

    public void actualizarProgreso(int estudianteId, int evaluacionId, double calificacion) {
        System.out.println("  [Seguimiento] Actualizando progreso...");
    }

    public int obtenerTiempoTotal(int estudianteId, int cursoId) {
        return 120; // Simulación: 120 minutos
    }

    public void marcarCompletado(int estudianteId, int contenidoId) {
        System.out.println("  [Seguimiento] Contenido " + contenidoId + " marcado como completado");
    }
}
