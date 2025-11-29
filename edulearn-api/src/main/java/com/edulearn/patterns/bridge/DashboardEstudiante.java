package com.edulearn.patterns.bridge;

import java.util.HashMap;
import java.util.Map;

/**
 * PATRÓN BRIDGE - Abstracción Refinada: Dashboard de Estudiante
 * La MISMA funcionalidad para todos los dispositivos, pero renderizado diferente
 */
public class DashboardEstudiante extends InterfazUsuario {

    public DashboardEstudiante(IPlataforma plataforma) {
        super(plataforma);
    }

    @Override
    public Map<String, Object> mostrarDashboard() {
        Map<String, Object> dashboard = new HashMap<>();

        // La estructura es la MISMA para todas las plataformas
        dashboard.put("tipo", "Dashboard Estudiante");
        dashboard.put("plataforma", plataforma.getNombre());

        // Pero el renderizado es DIFERENTE según la plataforma
        dashboard.put("navegacion", plataforma.renderizarNavegacion());
        dashboard.put("cursos", plataforma.renderizarCursos());
        dashboard.put("perfil", plataforma.renderizarPerfil());

        // Información adicional de la plataforma
        dashboard.put("resolucion", plataforma.obtenerResolucion());
        dashboard.put("capacidades", plataforma.obtenerCapacidades());

        // Las MISMAS secciones para todos los dispositivos
        dashboard.put("secciones", new String[]{
            "Mis Cursos",
            "Progreso",
            "Tareas Pendientes",
            "Calificaciones",
            "Perfil"
        });

        return dashboard;
    }

    public Map<String, Object> mostrarCursos() {
        Map<String, Object> vista = new HashMap<>();
        vista.put("tipo", "Lista de Cursos");
        vista.put("renderizado", plataforma.renderizarCursos());
        return vista;
    }

    public Map<String, Object> mostrarPerfil() {
        Map<String, Object> vista = new HashMap<>();
        vista.put("tipo", "Perfil de Estudiante");
        vista.put("renderizado", plataforma.renderizarPerfil());
        return vista;
    }
}
