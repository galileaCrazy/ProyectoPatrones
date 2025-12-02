package com.edulearn.patterns.comportamiento.chain_of_responsibility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Chain of Responsibility - Gestor de Visualización de Cursos para Profesores
 *
 * Este gestor valida que los profesores pueden ver solo sus cursos asignados.
 *
 * Patrón: Chain of Responsibility
 * Propósito: Validar permisos de visualización de cursos para profesores
 */
@Component
public class GestorVisualizacionCursosProfesor extends Gestor {

    private static final Logger logger = LoggerFactory.getLogger(GestorVisualizacionCursosProfesor.class);

    @Override
    protected boolean manejar(SolicitudValidacion solicitud) {
        logger.info("🔍 Validando permisos de visualización de cursos - Profesor");

        String tipoUsuario = solicitud.getTipoUsuario();
        String accion = solicitud.getAccion();

        // Solo procesamos si la acción es "VISUALIZAR_CURSOS"
        if (!"VISUALIZAR_CURSOS".equals(accion)) {
            logger.info("➡️ Acción no relacionada con visualización de cursos, pasando al siguiente gestor");
            return true;
        }

        // Validar si es profesor
        if ("profesor".equalsIgnoreCase(tipoUsuario)) {
            solicitud.agregarMetadato("tipoFiltro", "POR_PROFESOR");
            solicitud.agregarMetadato("rol", "PROFESOR");
            solicitud.agregarMetadato("mensaje", "Profesor puede ver solo sus cursos asignados");
            solicitud.setAprobada(true);

            logger.info("✅ Profesor autorizado para ver solo SUS cursos");
            return true;
        }

        // No es profesor, pasar al siguiente gestor
        logger.info("➡️ No es profesor, pasando al siguiente gestor");
        return true;
    }
}
