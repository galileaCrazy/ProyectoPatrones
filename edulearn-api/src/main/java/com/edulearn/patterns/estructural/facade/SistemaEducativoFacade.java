package com.edulearn.patterns.estructural.facade;

import com.edulearn.model.Curso;
import com.edulearn.model.Inscripcion;
import com.edulearn.patterns.comportamiento.template_method.InscripcionTemplateService;
import com.edulearn.patterns.comportamiento.template_method.dto.ResultadoInscripcion;
import com.edulearn.patterns.comportamiento.template_method.dto.SolicitudInscripcion;
import com.edulearn.patterns.estructural.facade.dto.InscripcionRequest;
import com.edulearn.patterns.estructural.facade.dto.InscripcionResponse;
import com.edulearn.patterns.estructural.facade.subsistemas.SubsistemaEvaluacion;
import com.edulearn.patterns.estructural.facade.subsistemas.SubsistemaMateriales;
import com.edulearn.patterns.estructural.facade.subsistemas.SubsistemaSeguimiento;
import com.edulearn.patterns.estructural.facade.subsistemas.SubsistemaValidacion;
import com.edulearn.repository.InscripcionRepository;
import com.edulearn.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * PATRÓN DE DISEÑO: FACADE (Fachada)
 *
 * Propósito:
 * Proporciona una interfaz unificada y simplificada para un conjunto complejo de subsistemas.
 * Hace que el sistema sea más fácil de usar al reducir las dependencias entre el cliente y los subsistemas.
 *
 * Implementación:
 * Esta fachada orquesta cuatro subsistemas complejos para el proceso de inscripción:
 * 1. SubsistemaValidacion - Valida requisitos previos
 * 2. SubsistemaMateriales - Gestiona contenido del curso
 * 3. SubsistemaEvaluacion - Configura evaluaciones
 * 4. SubsistemaSeguimiento - Inicia tracking de progreso
 *
 * Ventajas:
 * - El cliente (Controller) solo conoce una interfaz simple
 * - Reduce acoplamiento entre cliente y subsistemas
 * - Facilita cambios en subsistemas sin afectar al cliente
 * - Promueve la modularización del código
 */
@Component
public class SistemaEducativoFacade {

    // ============= SUBSISTEMAS INTERNOS =============
    @Autowired
    private SubsistemaValidacion subsistemaValidacion;

    @Autowired
    private SubsistemaMateriales subsistemaMateriales;

    @Autowired
    private SubsistemaEvaluacion subsistemaEvaluacion;

    @Autowired
    private SubsistemaSeguimiento subsistemaSeguimiento;

    // ============= SERVICIOS EXISTENTES =============
    @Autowired
    private InscripcionTemplateService inscripcionTemplateService;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private NotificacionService notificacionService;

    /**
     * MÉTODO PRINCIPAL DE LA FACHADA
     *
     * Interfaz simplificada que orquesta todo el proceso de inscripción
     * El cliente solo necesita llamar este método, sin conocer los subsistemas internos
     *
     * @param request Datos de la solicitud de inscripción
     * @return Respuesta consolidada con el resultado del proceso completo
     */
    public InscripcionResponse inscribirEstudiante(InscripcionRequest request) {
        InscripcionResponse response = new InscripcionResponse();

        System.out.println("\n╔══════════════════════════════════════════════════════════╗");
        System.out.println("║  🎓 PATRÓN FACADE: Proceso de Inscripción Iniciado      ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝\n");

        try {
            // ============= PASO 1: VALIDACIÓN =============
            System.out.println("🔍 PASO 1: Validando requisitos previos...");
            String errorValidacion = subsistemaValidacion.validarRequisitos(
                    request.getEstudianteId(),
                    request.getCursoId()
            );

            if (errorValidacion != null) {
                response.setExitoso(false);
                response.setMensaje(errorValidacion);
                response.agregarError(errorValidacion);
                System.out.println("❌ Validación fallida: " + errorValidacion);
                return response;
            }

            response.setValidacionCompletada(true);
            response.agregarDetalle("Validación de requisitos completada exitosamente");
            System.out.println("✓ Validación exitosa\n");

            // ============= PASO 2: CREAR INSCRIPCIÓN =============
            System.out.println("📝 PASO 2: Creando inscripción usando Template Method...");
            SolicitudInscripcion solicitud = mapearASolicitudInscripcion(request);
            ResultadoInscripcion resultadoInscripcion = inscripcionTemplateService.procesarInscripcion(solicitud);

            if (!resultadoInscripcion.isExitoso()) {
                response.setExitoso(false);
                response.setMensaje(resultadoInscripcion.getMensaje());
                response.agregarError("Error en inscripción: " + resultadoInscripcion.getMensaje());
                System.out.println("❌ Error en inscripción\n");
                return response;
            }

            // Obtener el ID de inscripción desde los detalles
            Integer inscripcionId = null;
            if (resultadoInscripcion.getDetalles() != null &&
                resultadoInscripcion.getDetalles().containsKey("inscripcionId")) {
                try {
                    inscripcionId = Integer.valueOf(
                        resultadoInscripcion.getDetalles().get("inscripcionId").toString()
                    );
                } catch (Exception e) {
                    System.err.println("⚠️ No se pudo extraer inscripcionId");
                }
            }

            response.setInscripcionId(inscripcionId);
            response.setEstadoInscripcion(resultadoInscripcion.getEstado());
            response.agregarDetalle("Inscripción creada con ID: " + inscripcionId);
            System.out.println("✓ Inscripción creada exitosamente (ID: " + inscripcionId + ")\n");

            // Obtener información del curso para la respuesta
            subsistemaValidacion.obtenerCurso(request.getCursoId()).ifPresent(curso -> {
                response.setCursoNombre(curso.getNombre());
            });

            // ============= PASO 3: PREPARAR MATERIALES =============
            System.out.println("📚 PASO 3: Preparando materiales del curso...");
            boolean materialesPreparados = subsistemaMateriales.prepararMaterialesCurso(
                    request.getCursoId(),
                    request.getEstudianteId()
            );

            response.setMaterialesAsignados(materialesPreparados);
            int totalMateriales = subsistemaMateriales.contarMaterialesCurso(request.getCursoId());
            response.setTotalMateriales(totalMateriales);

            if (materialesPreparados) {
                response.agregarDetalle("Materiales del curso asignados: " + totalMateriales);
                System.out.println("✓ Materiales preparados\n");
            } else {
                response.agregarDetalle("Curso sin materiales configurados");
                System.out.println("⚠️ Sin materiales\n");
            }

            // ============= PASO 4: CONFIGURAR EVALUACIONES =============
            System.out.println("📝 PASO 4: Configurando evaluaciones...");
            boolean evaluacionesPreparadas = subsistemaEvaluacion.prepararEvaluacionesCurso(
                    request.getCursoId(),
                    request.getEstudianteId()
            );

            response.setEvaluacionesPreparadas(evaluacionesPreparadas);
            int totalEvaluaciones = subsistemaEvaluacion.contarEvaluacionesCurso(request.getCursoId());
            response.setTotalEvaluaciones(totalEvaluaciones);

            if (evaluacionesPreparadas) {
                response.agregarDetalle("Evaluaciones configuradas: " + totalEvaluaciones);
                System.out.println("✓ Evaluaciones configuradas\n");
            } else {
                response.agregarDetalle("Curso sin evaluaciones configuradas");
                System.out.println("⚠️ Sin evaluaciones\n");
            }

            // ============= PASO 5: INICIAR SEGUIMIENTO =============
            System.out.println("📊 PASO 5: Iniciando seguimiento de progreso...");
            boolean seguimientoIniciado = subsistemaSeguimiento.iniciarSeguimientoProgreso(
                    request.getCursoId(),
                    request.getEstudianteId()
            );

            response.setSeguimientoIniciado(seguimientoIniciado);

            if (seguimientoIniciado) {
                response.agregarDetalle("Sistema de seguimiento activado");
                System.out.println("✓ Seguimiento iniciado\n");
            } else {
                response.agregarDetalle("Error al iniciar seguimiento (no crítico)");
                System.out.println("⚠️ Error en seguimiento\n");
            }

            // ============= PASO 6: ENVIAR NOTIFICACIÓN CON OBSERVER =============
            System.out.println("📧 PASO 6: Enviando notificación de inscripción...");
            try {
                // Usar patrón Observer para notificar inscripción
                com.edulearn.patterns.behavioral.observer.NotificationEvent event =
                    new com.edulearn.patterns.behavioral.observer.NotificationEvent.Builder()
                        .eventType(com.edulearn.patterns.behavioral.observer.NotificationEvent.EventType.ESTUDIANTE_INSCRITO)
                        .title("Nueva inscripción")
                        .message("Estudiante " + request.getEstudianteId() + " inscrito al curso " + request.getCursoId())
                        .sourceUserId(request.getEstudianteId())
                        .targetId(request.getCursoId())
                        .targetType("CURSO")
                        .build();

                notificacionService.notifyEvent(event);
                response.agregarDetalle("Notificación de inscripción enviada");
                System.out.println("✓ Notificación enviada\n");
            } catch (Exception e) {
                response.agregarDetalle("Notificación no enviada (no crítico)");
                System.out.println("⚠️ Error al enviar notificación: " + e.getMessage() + "\n");
            }

            // ============= RESULTADO FINAL =============
            response.setExitoso(true);
            response.setMensaje("Inscripción completada exitosamente. Todos los subsistemas configurados.");

            System.out.println("╔══════════════════════════════════════════════════════════╗");
            System.out.println("║  ✅ PATRÓN FACADE: Proceso Completado Exitosamente      ║");
            System.out.println("╚══════════════════════════════════════════════════════════╝\n");

            return response;

        } catch (Exception e) {
            // Manejo de errores global
            response.setExitoso(false);
            response.setMensaje("Error inesperado en el proceso de inscripción");
            response.agregarError("Error: " + e.getMessage());
            System.err.println("❌ Error crítico: " + e.getMessage());
            e.printStackTrace();
            return response;
        }
    }

    /**
     * Método helper para mapear el request de la fachada al DTO del Template Method
     */
    private SolicitudInscripcion mapearASolicitudInscripcion(InscripcionRequest request) {
        SolicitudInscripcion solicitud = new SolicitudInscripcion();
        solicitud.setEstudianteId(request.getEstudianteId());
        solicitud.setCursoId(request.getCursoId());
        solicitud.setTipoInscripcion(request.getTipoInscripcion() != null ?
                request.getTipoInscripcion() : "GRATUITA");
        solicitud.setAceptaTerminos(request.getAceptaTerminos() != null ?
                request.getAceptaTerminos() : true);

        // Mapear datos específicos según el tipo
        if ("PAGA".equals(request.getTipoInscripcion())) {
            solicitud.setMetodoPago(request.getMetodoPago());
            if (request.getMonto() != null) {
                solicitud.setMonto(java.math.BigDecimal.valueOf(request.getMonto()));
            }
        } else if ("BECA".equals(request.getTipoInscripcion())) {
            solicitud.setTipoBeca(request.getTipoBeca());
            solicitud.setCodigoBeca(request.getCodigoBeca());
        }

        return solicitud;
    }

    /**
     * Método adicional: Obtener resumen completo del estudiante
     * Demuestra otra operación simplificada de la fachada
     */
    public InscripcionResponse obtenerResumenEstudiante(Integer estudianteId, Integer cursoId) {
        InscripcionResponse response = new InscripcionResponse();

        try {
            // Validar existencia
            if (!subsistemaValidacion.existeEstudiante(estudianteId)) {
                response.setExitoso(false);
                response.setMensaje("Estudiante no encontrado");
                return response;
            }

            // Obtener datos de los subsistemas
            int materiales = subsistemaMateriales.contarMaterialesCurso(cursoId);
            int evaluaciones = subsistemaEvaluacion.contarEvaluacionesCurso(cursoId);
            boolean seguimientoActivo = subsistemaSeguimiento.existeSeguimiento(estudianteId, cursoId);

            response.setExitoso(true);
            response.setMensaje("Resumen obtenido exitosamente");
            response.setTotalMateriales(materiales);
            response.setTotalEvaluaciones(evaluaciones);
            response.setSeguimientoIniciado(seguimientoActivo);

            return response;
        } catch (Exception e) {
            response.setExitoso(false);
            response.setMensaje("Error al obtener resumen: " + e.getMessage());
            return response;
        }
    }
}
