package com.edulearn.patterns.comportamiento.chain_of_responsibility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Chain of Responsibility - Gestor de Visualización de Cursos para Estudiantes
 *
 * Este gestor valida que los estudiantes pueden ver solo los cursos en los que están inscritos.
 *
 * Patrón: Chain of Responsibility
 * Propósito: Validar permisos de visualización de cursos para estudiantes
 */
@Component
public class GestorVisualizacionCursosEstudiante extends Gestor {

    private static final Logger logger = LoggerFactory.getLogger(GestorVisualizacionCursosEstudiante.class);

    @Override
    protected boolean manejar(SolicitudValidacion solicitud) {
        logger.info("🔍 Validando permisos de visualización de cursos - Estudiante");

        String tipoUsuario = solicitud.getTipoUsuario();
        String accion = solicitud.getAccion();

        // Solo procesamos si la acción es "VISUALIZAR_CURSOS"
        if (!"VISUALIZAR_CURSOS".equals(accion)) {
            logger.info("➡️ Acción no relacionada con visualización de cursos, pasando al siguiente gestor");
            return true;
        }

        // Validar si es estudiante
        if ("estudiante".equalsIgnoreCase(tipoUsuario)) {
            solicitud.agregarMetadato("tipoFiltro", "POR_ESTUDIANTE");
            solicitud.agregarMetadato("rol", "ESTUDIANTE");
            solicitud.agregarMetadato("mensaje", "Estudiante puede ver solo cursos inscritos");
            solicitud.setAprobada(true);

            logger.info("✅ Estudiante autorizado para ver solo cursos INSCRITOS");
            return true;
        }

        // No es ninguno de los roles válidos
        logger.error("❌ Rol no autorizado para visualizar cursos: {}", tipoUsuario);
        solicitud.setMensajeError("El rol '" + tipoUsuario + "' no tiene permisos para visualizar cursos");
        return false;
    }
}
