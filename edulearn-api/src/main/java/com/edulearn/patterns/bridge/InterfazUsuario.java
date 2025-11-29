package com.edulearn.patterns.bridge;

import java.util.HashMap;
import java.util.Map;

/**
 * PATRÓN BRIDGE - Abstracción
 * La interfaz de usuario es la misma para todas las plataformas,
 * pero la implementación (renderizado) cambia según el dispositivo.
 */
public abstract class InterfazUsuario {
    protected IPlataforma plataforma;

    public InterfazUsuario(IPlataforma plataforma) {
        this.plataforma = plataforma;
    }

    public abstract Map<String, Object> mostrarDashboard();

    public Map<String, Object> obtenerInformacionPlataforma() {
        Map<String, Object> info = new HashMap<>();
        info.put("plataforma", plataforma.getNombre());
        info.put("resolucion", plataforma.obtenerResolucion());
        info.put("capacidades", plataforma.obtenerCapacidades());
        return info;
    }
}
