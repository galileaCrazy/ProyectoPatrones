package com.edulearn.patterns.flyweight;

/**
 * PATRÓN FLYWEIGHT - Flyweight Concreto para Cursos Virtuales
 * Estado intrínseco (compartido): icono, colores, plantilla
 */
public class RecursoVirtualFlyweight implements RecursoVisualFlyweight {
    // Estado intrínseco (COMPARTIDO entre todos los cursos virtuales)
    private final String icono = "💻";
    private final String colorPrimario = "#3B82F6"; // Azul
    private final String colorSecundario = "#DBEAFE";
    private final String plantilla = "VIRTUAL";
    private final String badge = "En línea";

    private RecursoInfo info;

    public RecursoVirtualFlyweight() {
        this.info = new RecursoInfo("Virtual", icono, colorPrimario, colorSecundario, plantilla);
    }

    @Override
    public String renderizar(ContextoCurso contexto) {
        info.incrementarUso();

        // Combina estado intrínseco (compartido) con extrínseco (específico)
        StringBuilder html = new StringBuilder();
        html.append("<div class='curso-card virtual' style='border-color: ").append(colorPrimario).append(";'>\n");
        html.append("  <div class='header' style='background: ").append(colorSecundario).append(";'>\n");
        html.append("    <span class='icon'>").append(icono).append("</span>\n");
        html.append("    <span class='badge' style='background: ").append(colorPrimario).append(";'>").append(badge).append("</span>\n");
        html.append("  </div>\n");
        html.append("  <div class='body'>\n");
        html.append("    <h3>").append(contexto.getNombreCurso()).append("</h3>\n");
        html.append("    <p>").append(contexto.getDescripcion()).append("</p>\n");
        html.append("    <div class='meta'>\n");
        html.append("      <span>⏱ ").append(contexto.getDuracion()).append("h</span>\n");
        html.append("      <span>👨‍🏫 ").append(contexto.getProfesorNombre()).append("</span>\n");
        html.append("      <span>👥 ").append(contexto.getEstudiantesInscritos()).append(" estudiantes</span>\n");
        html.append("    </div>\n");
        html.append("    <div class='footer'>\n");
        html.append("      <span>📅 ").append(contexto.getPeriodoAcademico()).append("</span>\n");
        html.append("      <span class='tipo'>Curso ").append(plantilla).append("</span>\n");
        html.append("    </div>\n");
        html.append("  </div>\n");
        html.append("</div>");

        return html.toString();
    }

    @Override
    public RecursoInfo obtenerInfo() {
        return info;
    }
}
