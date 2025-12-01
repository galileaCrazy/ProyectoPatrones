package com.edulearn.patterns.comportamiento.chain_of_responsibility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Chain of Responsibility - Gestor de Validación de Cupos según Tipo de Curso
 *
 * Este gestor valida y asigna el cupo máximo de estudiantes según el tipo de curso:
 * - PRESENCIAL: Máximo 35 estudiantes
 * - VIRTUAL: Máximo 100 estudiantes
 * - HÍBRIDO: Máximo 50 estudiantes
 *
 * Patrón: Chain of Responsibility
 * Propósito: Validar que el cupo del curso esté dentro de los límites permitidos según su tipo
 */
@Component
public class GestorValidacionCuposTipoCurso extends Gestor {

    private static final Logger logger = LoggerFactory.getLogger(GestorValidacionCuposTipoCurso.class);

    // Límites de cupo por tipo de curso
    private static final int CUPO_MAXIMO_PRESENCIAL = 35;
    private static final int CUPO_MAXIMO_VIRTUAL = 100;
    private static final int CUPO_MAXIMO_HIBRIDO = 50;

    @Override
    protected boolean manejar(SolicitudValidacion solicitud) {
        logger.info("👥 Validando cupo según tipo de curso");

        String accion = solicitud.getAccion();

        // Solo procesamos si la acción es "VALIDAR_CUPO_CURSO" o "CREAR_CURSO"
        if (!"VALIDAR_CUPO_CURSO".equals(accion) && !"CREAR_CURSO".equals(accion)) {
            logger.info("➡️ Acción no relacionada con validación de cupos, pasando al siguiente gestor");
            return true;
        }

        Map<String, Object> metadatos = solicitud.getMetadatos();
        String tipoCurso = (String) metadatos.get("tipoCurso");

        if (tipoCurso == null || tipoCurso.isEmpty()) {
            logger.info("ℹ️ No hay tipo de curso especificado aún");
            // Proporcionar información de cupos para el formulario
            agregarInformacionCupos(solicitud);
            return true;
        }

        logger.info("📚 Tipo de curso: {}", tipoCurso);

        // Determinar cupo máximo según el tipo de curso
        int cupoMaximo = determinarCupoMaximo(tipoCurso);

        if (cupoMaximo == -1) {
            String mensaje = String.format(
                "Tipo de curso inválido: '%s'. Los tipos válidos son: Presencial, Virtual, Híbrido",
                tipoCurso
            );
            solicitud.setMensajeError(mensaje);
            logger.error("❌ {}", mensaje);
            return false;
        }

        // Agregar cupo máximo a los metadatos
        solicitud.agregarMetadato("cupoMaximo", cupoMaximo);
        solicitud.agregarMetadato("tipoCursoValidado", tipoCurso);

        logger.info("✅ Cupo máximo asignado: {} estudiantes para curso {}", cupoMaximo, tipoCurso);

        // Si hay un cupo solicitado, validarlo
        Object cupoSolicitadoObj = metadatos.get("cupoMaximo");
        if (cupoSolicitadoObj != null) {
            try {
                int cupoSolicitado = Integer.parseInt(cupoSolicitadoObj.toString());

                if (cupoSolicitado <= 0) {
                    solicitud.setMensajeError("El cupo debe ser mayor a 0");
                    logger.error("❌ Cupo inválido: {}", cupoSolicitado);
                    return false;
                }

                if (cupoSolicitado > cupoMaximo) {
                    String mensaje = String.format(
                        "El cupo solicitado (%d estudiantes) excede el máximo permitido para cursos %s (%d estudiantes)",
                        cupoSolicitado,
                        tipoCurso.toLowerCase(),
                        cupoMaximo
                    );
                    solicitud.setMensajeError(mensaje);
                    logger.error("❌ {}", mensaje);
                    return false;
                }

                logger.info("✅ Cupo validado: {} estudiantes (dentro del límite de {})", cupoSolicitado, cupoMaximo);

            } catch (NumberFormatException e) {
                solicitud.setMensajeError("El cupo debe ser un número válido");
                logger.error("❌ Error al parsear cupo: {}", e.getMessage());
                return false;
            }
        }

        // Agregar información adicional para el formulario
        agregarInformacionCupos(solicitud);

        logger.info("✅ Validación de cupo completada exitosamente");
        return true;
    }

    /**
     * Determina el cupo máximo según el tipo de curso
     *
     * @param tipoCurso Tipo de curso (Presencial, Virtual, Híbrido)
     * @return Cupo máximo permitido, o -1 si el tipo es inválido
     */
    private int determinarCupoMaximo(String tipoCurso) {
        if (tipoCurso == null) {
            return -1;
        }

        switch (tipoCurso.toUpperCase()) {
            case "PRESENCIAL":
                logger.info("🏫 Curso PRESENCIAL → Cupo máximo: {} estudiantes", CUPO_MAXIMO_PRESENCIAL);
                return CUPO_MAXIMO_PRESENCIAL;

            case "VIRTUAL":
                logger.info("💻 Curso VIRTUAL → Cupo máximo: {} estudiantes", CUPO_MAXIMO_VIRTUAL);
                return CUPO_MAXIMO_VIRTUAL;

            case "HÍBRIDO":
            case "HIBRIDO":
                logger.info("🔄 Curso HÍBRIDO → Cupo máximo: {} estudiantes", CUPO_MAXIMO_HIBRIDO);
                return CUPO_MAXIMO_HIBRIDO;

            default:
                logger.warn("⚠️ Tipo de curso no reconocido: {}", tipoCurso);
                return -1;
        }
    }

    /**
     * Agrega información de cupos a los metadatos para que el frontend la muestre
     */
    private void agregarInformacionCupos(SolicitudValidacion solicitud) {
        Map<String, Integer> limiteCupos = Map.of(
            "Presencial", CUPO_MAXIMO_PRESENCIAL,
            "Virtual", CUPO_MAXIMO_VIRTUAL,
            "Híbrido", CUPO_MAXIMO_HIBRIDO
        );

        solicitud.agregarMetadato("limiteCuposPorTipo", limiteCupos);
        logger.debug("📋 Información de cupos agregada a metadatos: {}", limiteCupos);
    }
}
