package com.edulearn.patterns.structural.adapter;

/**
 * Interfaz objetivo (Target) del patrón Adapter.
 * Define la interfaz común que el cliente espera para todos los sistemas de videoconferencia.
 */
public interface ISistemaVideoconferencia {
    /**
     * Crea una sala de reunión virtual
     * @param nombreSala Nombre de la sala
     * @param duracionMinutos Duración estimada en minutos
     * @return URL de la reunión creada
     */
    String crearReunion(String nombreSala, int duracionMinutos);

    /**
     * Obtiene el enlace de una reunión existente
     * @param idReunion Identificador de la reunión
     * @return URL para unirse a la reunión
     */
    String obtenerEnlaceReunion(String idReunion);

    /**
     * Finaliza una reunión activa
     * @param idReunion Identificador de la reunión
     * @return true si se finalizó correctamente
     */
    boolean finalizarReunion(String idReunion);

    /**
     * Verifica el estado del sistema
     * @return true si el sistema está disponible
     */
    boolean verificarConexion();

    /**
     * Obtiene información del sistema
     * @return Nombre y versión del sistema
     */
    String obtenerInfoSistema();
}
