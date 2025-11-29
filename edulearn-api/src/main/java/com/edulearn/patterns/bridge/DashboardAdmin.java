package com.edulearn.patterns.bridge;

import java.util.HashMap;
import java.util.Map;

/**
 * PATRÓN BRIDGE - Abstracción Refinada: Dashboard de Administrador
 * La MISMA funcionalidad para todos los dispositivos, pero renderizado diferente
 */
public class DashboardAdmin extends InterfazUsuario {

    public DashboardAdmin(IPlataforma plataforma) {
        super(plataforma);
    }

    @Override
    public Map<String, Object> mostrarDashboard() {
        Map<String, Object> dashboard = new HashMap<>();

        // La estructura es la MISMA para todas las plataformas
        dashboard.put("tipo", "Dashboard Administrador");
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
            "Gestión de Usuarios",
            "Gestión de Cursos",
            "Inscripciones",
            "Reportes",
            "Configuración",
            "Perfil"
        });

        return dashboard;
    }

    public Map<String, Object> mostrarCursos() {
        Map<String, Object> vista = new HashMap<>();
        vista.put("tipo", "Administración de Cursos");
        vista.put("renderizado", plataforma.renderizarCursos());
        return vista;
    }

    public Map<String, Object> mostrarPerfil() {
        Map<String, Object> vista = new HashMap<>();
        vista.put("tipo", "Perfil de Administrador");
        vista.put("renderizado", plataforma.renderizarPerfil());
        return vista;
    }
}
