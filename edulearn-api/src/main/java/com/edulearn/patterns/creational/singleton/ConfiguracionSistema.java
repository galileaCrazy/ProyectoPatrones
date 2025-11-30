package com.edulearn.patterns.singleton;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton - Garantiza que una clase tenga una única instancia
 * Patrón: Singleton
 * Propósito: Asegurar que una clase tenga solo una instancia y proporcionar
 * un punto de acceso global a ella
 */
public class ConfiguracionSistema {
    private static ConfiguracionSistema instancia;
    private Map<String, String> configuraciones;

    private ConfiguracionSistema() {
        this.configuraciones = new HashMap<>();
        cargarConfiguracionPorDefecto();
    }

    public static synchronized ConfiguracionSistema getInstance() {
        if (instancia == null) {
            instancia = new ConfiguracionSistema();
        }
        return instancia;
    }

    private void cargarConfiguracionPorDefecto() {
        configuraciones.put("nombre_sistema", "EduLearn Platform");
        configuraciones.put("version", "1.0.0");
        configuraciones.put("max_intentos_login", "3");
        configuraciones.put("duracion_sesion_minutos", "60");
        configuraciones.put("cupo_default", "30");
        configuraciones.put("calificacion_minima_aprobacion", "60");
    }

    public String getConfiguracion(String clave) {
        return configuraciones.getOrDefault(clave, "");
    }

    public void setConfiguracion(String clave, String valor) {
        configuraciones.put(clave, valor);
    }

    public Map<String, String> obtenerTodasLasConfiguraciones() {
        return new HashMap<>(configuraciones);
    }

    public void actualizarConfiguraciones(Map<String, String> nuevasConfiguraciones) {
        configuraciones.putAll(nuevasConfiguraciones);
    }
}
