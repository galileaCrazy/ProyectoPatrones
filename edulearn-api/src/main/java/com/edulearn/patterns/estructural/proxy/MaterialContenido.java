package com.edulearn.patterns.estructural.proxy;

import com.edulearn.model.Material;

/**
 * Interfaz del Patrón Proxy
 *
 * Define las operaciones comunes entre MaterialReal y MaterialProxy.
 * Permite acceder al contenido de materiales pesados (videos, PDFs, archivos)
 * de manera controlada mediante lazy loading.
 *
 * Patrón de Diseño: Proxy (Estructural)
 * Propósito: Controlar acceso y aplicar carga diferida de materiales pesados
 */
public interface MaterialContenido {

    /**
     * Obtiene la información básica del material sin cargar el contenido pesado
     * @return Material con metadata básica
     */
    Material obtenerInformacionBasica();

    /**
     * Carga y obtiene el contenido real del material (archivo, video, PDF)
     * Este método puede ser costoso en términos de recursos
     * @return Contenido del material en formato byte array
     */
    byte[] cargarContenido();

    /**
     * Obtiene la URL del recurso para streaming o descarga
     * @return URL del recurso
     */
    String obtenerUrlRecurso();

    /**
     * Verifica si el contenido ya ha sido cargado
     * @return true si el contenido está cargado, false en caso contrario
     */
    boolean estaContenidoCargado();

    /**
     * Obtiene el tamaño del material en bytes
     * @return Tamaño en bytes
     */
    Long obtenerTamano();

    /**
     * Verifica si el usuario tiene permisos para acceder al material
     * @param usuarioId ID del usuario
     * @param rol Rol del usuario (estudiante, profesor, admin)
     * @return true si tiene acceso, false en caso contrario
     */
    boolean verificarAcceso(Long usuarioId, String rol);

    /**
     * Registra el acceso al material para métricas
     * @param usuarioId ID del usuario que accede
     */
    void registrarAcceso(Long usuarioId);
}
