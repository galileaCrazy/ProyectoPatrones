package com.edulearn.patterns.comportamiento.chain_of_responsibility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Chain of Responsibility - Gestor de Visualización de Cursos para Administradores
 *
 * Este gestor valida que los administradores pueden ver todos los cursos del sistema.
 *
 * Patrón: Chain of Responsibility
 * Propósito: Validar permisos de visualización de cursos para administradores
 */
@Component
public class GestorVisualizacionCursosAdmin extends Gestor {

    private static final Logger logger = LoggerFactory.getLogger(GestorVisualizacionCursosAdmin.class);

    @Override
    protected boolean manejar(SolicitudValidacion solicitud) {
        logger.info("🔍 Validando permisos de visualización de cursos - Administrador");

        String tipoUsuario = solicitud.getTipoUsuario();
        String accion = solicitud.getAccion();

        // Solo procesamos si la acción es "VISUALIZAR_CURSOS"
        if (!"VISUALIZAR_CURSOS".equals(accion)) {
            logger.info("➡️ Acción no relacionada con visualización de cursos, pasando al siguiente gestor");
            return true;
        }

        // Validar si es administrador
        if ("administrador".equalsIgnoreCase(tipoUsuario) || "admin".equalsIgnoreCase(tipoUsuario)) {
            solicitud.agregarMetadato("tipoFiltro", "TODOS");
            solicitud.agregarMetadato("rol", "ADMINISTRADOR");
            solicitud.agregarMetadato("mensaje", "Administrador puede ver todos los cursos");
            solicitud.setAprobada(true);

            logger.info("✅ Administrador autorizado para ver TODOS los cursos");
            return true;
        }

        // No es administrador, pasar al siguiente gestor
        logger.info("➡️ No es administrador, pasando al siguiente gestor");
        return true;
    }
}
