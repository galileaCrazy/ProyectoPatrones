package com.edulearn.patterns.behavioral.state;

import com.edulearn.model.Curso;

/**
 * PATRÓN STATE - Interfaz Estado
 * ===============================
 * Define la interfaz para encapsular el comportamiento asociado
 * con un estado particular del curso.
 *
 * Permite que un objeto (Curso) altere su comportamiento cuando
 * su estado interno cambia.
 */
public interface CursoState {

    /**
     * Transición al estado "Activo"
     */
    void activar(CursoContext context);

    /**
     * Transición al estado "Finalizado"
     */
    void finalizar(CursoContext context);

    /**
     * Transición al estado "Archivado"
     */
    void archivar(CursoContext context);

    /**
     * Publicar el curso (solo desde en_creacion)
     */
    void publicar(CursoContext context);

    /**
     * Verificar si se pueden inscribir estudiantes
     */
    boolean puedeInscribirEstudiantes();

    /**
     * Verificar si se puede modificar el contenido
     */
    boolean puedeModificarContenido();

    /**
     * Verificar si se puede eliminar
     */
    boolean puedeEliminar();

    /**
     * Obtener nombre del estado
     */
    String getNombreEstado();

    /**
     * Obtener descripción del estado
     */
    String getDescripcion();
}
