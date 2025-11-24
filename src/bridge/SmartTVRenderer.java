package bridge;

/**
 * PATRÓN BRIDGE - Concrete Implementor
 * Implementación específica para Smart TV
 */
public class SmartTVRenderer implements DispositivoRenderer {

    @Override
    public void renderizarCurso(String nombre, String descripcion, String tipo) {
        System.out.println("[SMART TV] ════════════════════════════════════════════");
        System.out.println("[SMART TV]   " + nombre.toUpperCase());
        System.out.println("[SMART TV]   " + descripcion);
        System.out.println("[SMART TV]   Modalidad: " + tipo);
        System.out.println("[SMART TV] ════════════════════════════════════════════");
    }

    @Override
    public void renderizarModulo(String titulo, int orden) {
        System.out.println("[SMART TV] [" + orden + "] " + titulo);
        System.out.println("[SMART TV]     Presiona OK para ver contenido");
    }

    @Override
    public void renderizarMaterial(String nombre, String tipo, String url) {
        if (tipo.equals("video")) {
            System.out.println("[SMART TV] ▶ REPRODUCIENDO: " + nombre);
            System.out.println("[SMART TV]   URL: " + url);
            System.out.println("[SMART TV]   [◀◀] [▶/❚❚] [▶▶] [⬛]");
        } else {
            System.out.println("[SMART TV] Material: " + nombre + " (Use app móvil para ver " + tipo + ")");
        }
    }

    @Override
    public void renderizarEvaluacion(String nombre, String tipo, int puntajeMaximo) {
        System.out.println("[SMART TV] EVALUACIÓN: " + nombre);
        System.out.println("[SMART TV] Tipo: " + tipo + " | Puntaje: " + puntajeMaximo);
        System.out.println("[SMART TV] Use control remoto para responder");
    }

    @Override
    public void mostrarNotificacion(String mensaje, String tipo) {
        System.out.println("[SMART TV] ╔════ NOTIFICACIÓN ════╗");
        System.out.println("[SMART TV] ║ " + mensaje);
        System.out.println("[SMART TV] ╚══════════════════════╝");
    }

    @Override
    public String getNombreDispositivo() {
        return "Smart TV";
    }
}
