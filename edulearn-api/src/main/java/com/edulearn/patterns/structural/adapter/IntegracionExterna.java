package com.edulearn.patterns.structural.adapter;

import java.util.Map;

/**
 * PATRÓN ADAPTER - Interface objetivo (Target)
 * ============================================
 * Define la interfaz común que esperan los clientes.
 * Todos los adaptadores implementarán esta interfaz para
 * proporcionar una forma unificada de interactuar con sistemas externos.
 */
public interface IntegracionExterna {

    /**
     * Crear una sesión/recurso en el sistema externo
     * @param datos Datos necesarios para crear la sesión
     * @return URL o identificador del recurso creado
     */
    String crearSesion(Map<String, Object> datos);

    /**
     * Obtener información de una sesión/recurso
     * @param identificador ID del recurso
     * @return Datos del recurso
     */
    Map<String, Object> obtenerInformacion(String identificador);

    /**
     * Actualizar una sesión/recurso
     * @param identificador ID del recurso
     * @param datos Nuevos datos
     * @return true si se actualizó correctamente
     */
    boolean actualizarSesion(String identificador, Map<String, Object> datos);

    /**
     * Eliminar una sesión/recurso
     * @param identificador ID del recurso
     * @return true si se eliminó correctamente
     */
    boolean eliminarSesion(String identificador);

    /**
     * Obtener el tipo de integración
     * @return Tipo (VIDEOCONFERENCIA, REPOSITORIO)
     */
    String getTipo();

    /**
     * Obtener el nombre del proveedor
     * @return Nombre (GOOGLE_MEET, ZOOM, GOOGLE_DRIVE, etc.)
     */
    String getProveedor();

    /**
     * Validar credenciales/configuración
     * @param credenciales Credenciales del usuario
     * @return true si las credenciales son válidas
     */
    boolean validarCredenciales(Map<String, String> credenciales);
}
