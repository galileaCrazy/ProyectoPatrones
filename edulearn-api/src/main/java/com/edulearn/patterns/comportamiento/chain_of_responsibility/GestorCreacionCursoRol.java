package com.edulearn.patterns.comportamiento.chain_of_responsibility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Chain of Responsibility - Gestor de Creación de Cursos según Rol
 *
 * Este gestor valida y ajusta el comportamiento del formulario de creación de cursos
 * dependiendo del rol del usuario (Profesor o Administrador).
 *
 * Patrón: Chain of Responsibility
 * Propósito: Determinar qué campos del formulario se muestran y cómo se asigna el profesor
 */
@Component
public class GestorCreacionCursoRol extends Gestor {

    private static final Logger logger = LoggerFactory.getLogger(GestorCreacionCursoRol.class);

    @Override
    protected boolean manejar(SolicitudValidacion solicitud) {
        logger.info("🔍 Validando rol para creación de curso");

        String tipoUsuario = solicitud.getTipoUsuario();
        String accion = solicitud.getAccion();

        if (tipoUsuario == null || tipoUsuario.isEmpty()) {
            solicitud.setMensajeError("El rol del usuario es requerido");
            logger.error("❌ Rol no proporcionado");
            return false;
        }

        // Solo procesamos si la acción es "CONFIGURAR_FORMULARIO" o "CREAR_CURSO"
        if (!"CONFIGURAR_FORMULARIO".equals(accion) && !"CREAR_CURSO".equals(accion)) {
            // No es nuestra responsabilidad, pasar al siguiente
            logger.info("➡️ Acción no relacionada con formulario, pasando al siguiente gestor");
            return true;
        }

        // Configuración según el rol
        if ("PROFESOR".equalsIgnoreCase(tipoUsuario)) {
            // PROFESOR: Se asigna automáticamente a sí mismo
            solicitud.agregarMetadato("rol", "PROFESOR");
            solicitud.agregarMetadato("mostrarListaProfesores", false);
            solicitud.agregarMetadato("modoAutoAsignacion", true);
            solicitud.agregarMetadato("mensaje", "Profesor asignado automáticamente");

            logger.info("✅ Configuración PROFESOR: Auto-asignación activada");
            return true;

        } else if ("ADMINISTRADOR".equalsIgnoreCase(tipoUsuario) || "ADMIN".equalsIgnoreCase(tipoUsuario)) {
            // ADMINISTRADOR: Puede seleccionar cualquier profesor
            solicitud.agregarMetadato("rol", "ADMINISTRADOR");
            solicitud.agregarMetadato("mostrarListaProfesores", true);
            solicitud.agregarMetadato("modoAutoAsignacion", false);
            solicitud.agregarMetadato("mensaje", "Administrador puede seleccionar profesor");

            logger.info("✅ Configuración ADMINISTRADOR: Selección de profesor habilitada");
            return true;

        } else {
            // ROL NO AUTORIZADO para crear cursos
            solicitud.setMensajeError("El rol '" + tipoUsuario + "' no tiene permisos para crear cursos");
            logger.error("❌ Rol no autorizado para crear cursos: {}", tipoUsuario);
            return false;
        }
    }
}
