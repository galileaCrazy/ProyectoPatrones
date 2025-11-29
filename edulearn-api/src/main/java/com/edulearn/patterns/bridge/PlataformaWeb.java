package com.edulearn.patterns.bridge;

/**
 * PATRÓN BRIDGE - Implementación Concreta: Web
 * Renderiza la interfaz para navegadores web
 */
public class PlataformaWeb implements IPlataforma {

    @Override
    public String getNombre() {
        return "Web Browser";
    }

    @Override
    public String renderizarNavegacion() {
        return "<!-- Navegación Web -->\n" +
               "<nav class=\"navbar navbar-expand-lg bg-primary\">\n" +
               "  <div class=\"container-fluid\">\n" +
               "    <a class=\"navbar-brand\" href=\"#\">EduLearn</a>\n" +
               "    <button class=\"navbar-toggler\" type=\"button\">\n" +
               "      <span class=\"navbar-toggler-icon\"></span>\n" +
               "    </button>\n" +
               "    <div class=\"collapse navbar-collapse\">\n" +
               "      <ul class=\"navbar-nav ms-auto\">\n" +
               "        <li class=\"nav-item\"><a class=\"nav-link\" href=\"#cursos\">Mis Cursos</a></li>\n" +
               "        <li class=\"nav-item\"><a class=\"nav-link\" href=\"#perfil\">Perfil</a></li>\n" +
               "        <li class=\"nav-item\"><a class=\"nav-link\" href=\"#logout\">Salir</a></li>\n" +
               "      </ul>\n" +
               "    </div>\n" +
               "  </div>\n" +
               "</nav>";
    }

    @Override
    public String renderizarCursos() {
        return "<!-- Grid de Cursos Web -->\n" +
               "<div class=\"container mt-4\">\n" +
               "  <div class=\"row\">\n" +
               "    <div class=\"col-md-4 mb-4\">\n" +
               "      <div class=\"card curso-card\">\n" +
               "        <div class=\"card-body\">\n" +
               "          <h5 class=\"card-title\">Nombre del Curso</h5>\n" +
               "          <p class=\"card-text\">Descripción del curso...</p>\n" +
               "          <button class=\"btn btn-primary\">Ver Detalles</button>\n" +
               "        </div>\n" +
               "      </div>\n" +
               "    </div>\n" +
               "  </div>\n" +
               "</div>";
    }

    @Override
    public String renderizarPerfil() {
        return "<!-- Perfil de Usuario Web -->\n" +
               "<div class=\"container profile-container\">\n" +
               "  <div class=\"row\">\n" +
               "    <div class=\"col-md-3\">\n" +
               "      <img src=\"avatar.jpg\" class=\"img-fluid rounded-circle\" alt=\"Avatar\">\n" +
               "    </div>\n" +
               "    <div class=\"col-md-9\">\n" +
               "      <h2>Nombre del Usuario</h2>\n" +
               "      <p>Email: usuario@edulearn.com</p>\n" +
               "      <p>Cursos inscritos: 5</p>\n" +
               "      <button class=\"btn btn-secondary\">Editar Perfil</button>\n" +
               "    </div>\n" +
               "  </div>\n" +
               "</div>";
    }

    @Override
    public String obtenerResolucion() {
        return "1920x1080 (Full HD) - Responsive";
    }

    @Override
    public String obtenerCapacidades() {
        return "Mouse, Teclado, Touch (tablet), JavaScript, CSS Grid, Flexbox, Animations";
    }
}
