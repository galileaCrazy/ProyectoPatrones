package com.edulearn.repository;

import com.edulearn.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para gestionar notificaciones
 */
@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    /**
     * Buscar notificaciones por tipo
     */
    List<Notificacion> findByTipo(String tipo);

    /**
     * Buscar notificaciones por estado
     */
    List<Notificacion> findByEstado(String estado);

    /**
     * Buscar notificaciones por destinatario
     */
    List<Notificacion> findByDestinatario(String destinatario);

    /**
     * Buscar notificaciones por tipo y estado
     */
    List<Notificacion> findByTipoAndEstado(String tipo, String estado);

    /**
     * Buscar notificaciones por destinatario y estado
     */
    List<Notificacion> findByDestinatarioAndEstado(String destinatario, String estado);
}
