package bridge;

/**
 * PATRÓN BRIDGE - Concrete Implementor
 * Implementación específica para dispositivos móviles
 */
public class MobileRenderer implements DispositivoRenderer {

    @Override
    public void renderizarCurso(String nombre, String descripcion, String tipo) {
        System.out.println("[MOBILE] === Renderizando Curso ===");
        System.out.println("┌─────────────────────────┐");
        System.out.println("│ " + truncar(nombre, 23) + " │");
        System.out.println("│ " + truncar(descripcion, 23) + " │");
        System.out.println("│ [" + tipo + "]" + " ".repeat(Math.max(0, 20 - tipo.length())) + " │");
        System.out.println("└─────────────────────────┘");
    }

    @Override
    public void renderizarModulo(String titulo, int orden) {
        System.out.println("[MOBILE] ├── Módulo " + orden + ": " + truncar(titulo, 15));
    }

    @Override
    public void renderizarMaterial(String nombre, String tipo, String url) {
        String icono = tipo.equals("video") ? "▶" : tipo.equals("pdf") ? "📄" : "📁";
        System.out.println("[MOBILE]     " + icono + " " + truncar(nombre, 18));
    }

    @Override
    public void renderizarEvaluacion(String nombre, String tipo, int puntajeMaximo) {
        System.out.println("[MOBILE]     ✎ " + truncar(nombre, 15) + " (" + puntajeMaximo + " pts)");
    }

    @Override
    public void mostrarNotificacion(String mensaje, String tipo) {
        String icono = tipo.equals("success") ? "✓" : tipo.equals("error") ? "✗" : "ℹ";
        System.out.println("[MOBILE] " + icono + " " + truncar(mensaje, 30));
    }

    @Override
    public String getNombreDispositivo() {
        return "Dispositivo Móvil";
    }

    private String truncar(String texto, int maxLen) {
        if (texto.length() <= maxLen) return texto;
        return texto.substring(0, maxLen - 3) + "...";
    }
}
