package bridge;

/**
 * PATRÓN BRIDGE - Concrete Implementor
 * Implementación específica para navegadores web
 */
public class WebRenderer implements DispositivoRenderer {

    @Override
    public void renderizarCurso(String nombre, String descripcion, String tipo) {
        System.out.println("[WEB] === Renderizando Curso ===");
        System.out.println("<div class='curso-card'>");
        System.out.println("  <h1>" + nombre + "</h1>");
        System.out.println("  <p class='descripcion'>" + descripcion + "</p>");
        System.out.println("  <span class='badge'>" + tipo + "</span>");
        System.out.println("</div>");
    }

    @Override
    public void renderizarModulo(String titulo, int orden) {
        System.out.println("[WEB] <div class='modulo' data-orden='" + orden + "'>");
        System.out.println("  <h2>" + titulo + "</h2>");
        System.out.println("  <div class='modulo-content'></div>");
        System.out.println("</div>");
    }

    @Override
    public void renderizarMaterial(String nombre, String tipo, String url) {
        System.out.println("[WEB] <div class='material material-" + tipo + "'>");
        System.out.println("  <a href='" + url + "' target='_blank'>" + nombre + "</a>");
        if (tipo.equals("video")) {
            System.out.println("  <video src='" + url + "' controls></video>");
        }
        System.out.println("</div>");
    }

    @Override
    public void renderizarEvaluacion(String nombre, String tipo, int puntajeMaximo) {
        System.out.println("[WEB] <div class='evaluacion'>");
        System.out.println("  <h3>" + nombre + "</h3>");
        System.out.println("  <form class='form-evaluacion' data-tipo='" + tipo + "'>");
        System.out.println("    <input type='hidden' name='puntaje_max' value='" + puntajeMaximo + "'/>");
        System.out.println("    <button type='submit'>Iniciar Evaluación</button>");
        System.out.println("  </form>");
        System.out.println("</div>");
    }

    @Override
    public void mostrarNotificacion(String mensaje, String tipo) {
        System.out.println("[WEB] <div class='notification notification-" + tipo + "'>");
        System.out.println("  <p>" + mensaje + "</p>");
        System.out.println("</div>");
    }

    @Override
    public String getNombreDispositivo() {
        return "Navegador Web";
    }
}
