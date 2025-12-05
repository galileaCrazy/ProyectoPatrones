package com.edulearn.controller;

import com.edulearn.model.Inscripcion;
import com.edulearn.model.Estudiante;
import com.edulearn.model.Curso;
import com.edulearn.repository.InscripcionRepository;
import com.edulearn.repository.EstudianteRepository;
import com.edulearn.repository.CursoRepository;
import com.edulearn.patterns.comportamiento.template_method.InscripcionTemplateService;
import com.edulearn.patterns.comportamiento.template_method.dto.SolicitudInscripcion;
import com.edulearn.patterns.comportamiento.template_method.dto.ResultadoInscripcion;
import com.edulearn.patterns.estructural.facade.SistemaEducativoFacade;
import com.edulearn.patterns.estructural.facade.dto.InscripcionRequest;
import com.edulearn.patterns.estructural.facade.dto.InscripcionResponse;
import com.edulearn.patterns.comportamiento.observer.NotificationOrchestrator;
import com.edulearn.patterns.comportamiento.observer.NotificationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/inscripciones")
@CrossOrigin(origins = "*")
public class InscripcionController {
    @Autowired
    private InscripcionRepository inscripcionRepository;
    @Autowired
    private EstudianteRepository estudianteRepository;
    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private InscripcionTemplateService inscripcionTemplateService;
    @Autowired
    private SistemaEducativoFacade sistemaEducativoFacade;
    @Autowired
    private NotificationOrchestrator notificationOrchestrator;

    @GetMapping
    public List<Map<String, Object>> getAll() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Inscripcion ins : inscripcionRepository.findAll()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", ins.getId());
            map.put("estudianteId", ins.getEstudianteId());
            map.put("cursoId", ins.getCursoId());
            map.put("fechaInscripcion", ins.getFechaInscripcion());
            map.put("modalidad", ins.getModalidad());
            map.put("estadoInscripcion", ins.getEstadoInscripcion());
            map.put("certificadoGarantizado", ins.getCertificadoGarantizado());

            estudianteRepository.findById(ins.getEstudianteId())
                .ifPresent(e -> map.put("estudianteNombre", e.getNombre() + " " + e.getApellidos()));
            cursoRepository.findById(ins.getCursoId())
                .ifPresent(c -> map.put("cursoNombre", c.getNombre()));

            result.add(map);
        }
        return result;
    }

    /**
     * ============================================================
     * PATRÓN FACADE - Endpoint simplificado de inscripción
     * ============================================================
     *
     * Este endpoint utiliza el patrón de diseño Facade para proporcionar
     * una interfaz única y simplificada al proceso complejo de inscripción.
     *
     * El SistemaEducativoFacade orquesta automáticamente:
     * 1. Validación de requisitos (SubsistemaValidacion)
     * 2. Creación de inscripción (Template Method)
     * 3. Asignación de materiales (SubsistemaMateriales)
     * 4. Configuración de evaluaciones (SubsistemaEvaluacion)
     * 5. Inicio de seguimiento de progreso (SubsistemaSeguimiento)
     * 6. Envío de notificaciones
     *
     * Ventaja: El cliente (frontend) solo necesita llamar este endpoint
     * sin conocer ni gestionar la complejidad de los subsistemas internos.
     */
    @PostMapping("/facade")
    public ResponseEntity<InscripcionResponse> inscribirConFacade(@RequestBody InscripcionRequest request) {
        try {
            // La fachada maneja toda la complejidad internamente
            InscripcionResponse response = sistemaEducativoFacade.inscribirEstudiante(request);

            if (response.isExitoso()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            InscripcionResponse errorResponse = new InscripcionResponse();
            errorResponse.setExitoso(false);
            errorResponse.setMensaje("Error del servidor: " + e.getMessage());
            errorResponse.agregarError(e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Endpoint adicional del Facade: Obtener resumen completo del estudiante en un curso
     */
    @GetMapping("/facade/resumen/{estudianteId}/{cursoId}")
    public ResponseEntity<InscripcionResponse> obtenerResumenFacade(
            @PathVariable Integer estudianteId,
            @PathVariable Integer cursoId) {
        try {
            InscripcionResponse response = sistemaEducativoFacade.obtenerResumenEstudiante(estudianteId, cursoId);

            if (response.isExitoso()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            InscripcionResponse errorResponse = new InscripcionResponse();
            errorResponse.setExitoso(false);
            errorResponse.setMensaje("Error del servidor: " + e.getMessage());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Inscripción rápida - usa automáticamente modalidad GRATUITA
     * Endpoint para inscripción directa desde la lista de cursos
     */
    @PostMapping("/rapida")
    public ResponseEntity<?> inscripcionRapida(@RequestBody Map<String, Object> request) {
        try {
            Integer estudianteId = (Integer) request.get("estudianteId");
            Integer cursoId = (Integer) request.get("cursoId");

            if (estudianteId == null || cursoId == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "exitoso", false,
                    "mensaje", "Debe proporcionar estudianteId y cursoId"
                ));
            }

            // Crear solicitud con modalidad GRATUITA por defecto
            SolicitudInscripcion solicitud = new SolicitudInscripcion();
            solicitud.setEstudianteId(estudianteId);
            solicitud.setCursoId(cursoId);
            solicitud.setTipoInscripcion("GRATUITA");
            solicitud.setAceptaTerminos(true); // Auto-aceptar para inscripción rápida

            // Procesar usando Template Method
            ResultadoInscripcion resultado = inscripcionTemplateService.procesarInscripcion(solicitud);

            if (resultado.isExitoso()) {
                // 📧 ENVIAR NOTIFICACIONES
                try {
                    Estudiante estudiante = estudianteRepository.findById(estudianteId).orElse(null);
                    Curso curso = cursoRepository.findById(cursoId).orElse(null);

                    if (estudiante != null && curso != null) {
                        String estudianteNombre = estudiante.getNombre() + " " + estudiante.getApellidos();

                        // 1. Suscribir estudiante al curso
                        notificationOrchestrator.subscribeStudentToCourse(
                            estudianteId,
                            estudianteNombre,
                            cursoId
                        );

                        // 2. Notificar al profesor del curso
                        // Buscar la inscripción recién creada
                        Optional<Inscripcion> inscripcionOpt = inscripcionRepository
                            .findByEstudianteIdAndCursoId(estudianteId, cursoId);

                        if (inscripcionOpt.isPresent()) {
                            // Registrar profesor del curso de forma defensiva para garantizar notificación
                            if (curso.getProfesorTitularId() != null) {
                                try {
                                    notificationOrchestrator.registerCourseTeacher(curso.getId(), curso.getProfesorTitularId());
                                } catch (Exception ex) {
                                    System.err.println("⚠️ No se pudo registrar profesor del curso en notificaciones: " + ex.getMessage());
                                }
                            }

                            notificationOrchestrator.notifyStudentEnrolled(
                                inscripcionOpt.get(),
                                estudianteNombre,
                                curso.getNombre()
                            );
                        }

                        System.out.println("📧 Notificaciones enviadas para inscripción rápida");
                    }
                } catch (Exception e) {
                    System.err.println("⚠️ Error al enviar notificaciones: " + e.getMessage());
                }

                return ResponseEntity.ok(resultado);
            } else {
                return ResponseEntity.badRequest().body(resultado);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "exitoso", false,
                "mensaje", "Error al procesar inscripción: " + e.getMessage()
            ));
        }
    }

    @PostMapping
    public Inscripcion create(@RequestBody Inscripcion inscripcion) {
        Inscripcion saved = inscripcionRepository.save(inscripcion);

        // 📧 ENVIAR NOTIFICACIONES
        try {
            Estudiante estudiante = estudianteRepository.findById(saved.getEstudianteId()).orElse(null);
            Curso curso = cursoRepository.findById(saved.getCursoId()).orElse(null);

            if (estudiante != null && curso != null) {
                String estudianteNombre = estudiante.getNombre() + " " + estudiante.getApellidos();

                // Si es una beca, notificar a ADMINISTRADORES
                if ("BECA".equals(saved.getModalidad())) {
                    NotificationEvent event = new NotificationEvent.Builder()
                        .eventType(NotificationEvent.EventType.BECA_SOLICITADA)
                        .title("Nueva Solicitud de Beca")
                        .message(String.format("Estudiante %s ha solicitado una beca para el curso '%s' (Tipo: %s)",
                            estudianteNombre, curso.getNombre(), saved.getTipoBeca()))
                        .sourceUserId(saved.getEstudianteId())
                        .targetId(saved.getCursoId())
                        .targetType("INSCRIPCION")
                        .addMetadata("inscripcionId", saved.getId())
                        .addMetadata("tipoBeca", saved.getTipoBeca())
                        .addMetadata("codigoBeca", saved.getCodigoBeca())
                        .build();

                    notificationOrchestrator.notifyRoleObservers("admin", event);
                    System.out.println("📧 Notificación de solicitud de beca enviada a administradores");
                }

                // Si es inscripción activa, notificar al profesor y suscribir estudiante
                if ("Activa".equals(saved.getEstadoInscripcion())) {
                    // Suscribir estudiante al curso
                    notificationOrchestrator.subscribeStudentToCourse(
                        saved.getEstudianteId(),
                        estudianteNombre,
                        saved.getCursoId()
                    );

                    // Registrar profesor del curso de forma defensiva para garantizar notificación
                    if (curso.getProfesorTitularId() != null) {
                        try {
                            notificationOrchestrator.registerCourseTeacher(curso.getId(), curso.getProfesorTitularId());
                        } catch (Exception ex) {
                            System.err.println("⚠️ No se pudo registrar profesor del curso en notificaciones: " + ex.getMessage());
                        }
                    }

                    // Notificar al profesor
                    notificationOrchestrator.notifyStudentEnrolled(
                        saved,
                        estudianteNombre,
                        curso.getNombre()
                    );

                    System.out.println("📧 Notificaciones de inscripción enviadas");
                }
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al enviar notificaciones: " + e.getMessage());
        }

        return saved;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        inscripcionRepository.deleteById(id);
    }

    /**
     * Obtener inscripciones ACTIVAS de un estudiante específico (para "Mis Cursos")
     * Solo retorna inscripciones con estado "Activa"
     */
    @GetMapping("/estudiante/{estudianteId}")
    public List<Map<String, Object>> getInscripcionesByEstudiante(@PathVariable Integer estudianteId) {
        List<Inscripcion> inscripciones = inscripcionRepository.findByEstudianteId(estudianteId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Inscripcion ins : inscripciones) {
            // SOLO mostrar inscripciones activas en "Mis Cursos"
            if (!"Activa".equals(ins.getEstadoInscripcion())) {
                continue;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("id", ins.getId());
            map.put("estudianteId", ins.getEstudianteId());
            map.put("cursoId", ins.getCursoId());
            map.put("fechaInscripcion", ins.getFechaInscripcion());
            map.put("modalidad", ins.getModalidad());
            map.put("estadoInscripcion", ins.getEstadoInscripcion());
            map.put("certificadoGarantizado", ins.getCertificadoGarantizado());

            cursoRepository.findById(ins.getCursoId())
                .ifPresent(c -> {
                    map.put("cursoNombre", c.getNombre());
                    map.put("cursoDescripcion", c.getDescripcion());
                    map.put("cursoCodigo", c.getCodigo());
                });

            result.add(map);
        }
        return result;
    }

    /**
     * Obtener TODAS las solicitudes de inscripción de un estudiante (incluyendo pendientes y rechazadas)
     * Para la vista de "Estado de Solicitudes"
     */
    @GetMapping("/estudiante/{estudianteId}/todas")
    public List<Map<String, Object>> getTodasInscripcionesByEstudiante(@PathVariable Integer estudianteId) {
        List<Inscripcion> inscripciones = inscripcionRepository.findByEstudianteId(estudianteId);
        List<Map<String, Object>> result = new ArrayList<>();

        for (Inscripcion ins : inscripciones) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", ins.getId());
            map.put("estudianteId", ins.getEstudianteId());
            map.put("cursoId", ins.getCursoId());
            map.put("fechaInscripcion", ins.getFechaInscripcion());
            map.put("modalidad", ins.getModalidad());
            map.put("estadoInscripcion", ins.getEstadoInscripcion());
            map.put("certificadoGarantizado", ins.getCertificadoGarantizado());

            // Incluir datos específicos de beca si aplica
            if ("BECA".equals(ins.getModalidad())) {
                map.put("tipoBeca", ins.getTipoBeca());
                map.put("codigoBeca", ins.getCodigoBeca());
            }

            // Incluir datos de pago si aplica
            if ("PAGA".equals(ins.getModalidad())) {
                map.put("montoPagado", ins.getMontoPagado());
                map.put("metodoPago", ins.getMetodoPago());
            }

            // Incluir motivo de rechazo si existe
            if ("Rechazada".equals(ins.getEstadoInscripcion()) && ins.getMotivoRechazo() != null) {
                map.put("motivoRechazo", ins.getMotivoRechazo());
            }

            cursoRepository.findById(ins.getCursoId())
                .ifPresent(c -> {
                    map.put("cursoNombre", c.getNombre());
                    map.put("cursoDescripcion", c.getDescripcion());
                    map.put("cursoCodigo", c.getCodigo());
                });

            estudianteRepository.findById(ins.getEstudianteId())
                .ifPresent(e -> {
                    map.put("estudianteNombre", e.getNombre() + " " + e.getApellidos());
                });

            result.add(map);
        }
        return result;
    }

    /**
     * Obtener becas pendientes de aprobación (solo para administradores)
     */
    @GetMapping("/becas/pendientes")
    public List<Map<String, Object>> getBecasPendientes() {
        List<Inscripcion> inscripciones = inscripcionRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (Inscripcion ins : inscripciones) {
            if ("BECA".equals(ins.getModalidad()) &&
                "Pendiente de Aprobación/Documentación".equals(ins.getEstadoInscripcion())) {

                Map<String, Object> map = new HashMap<>();
                map.put("id", ins.getId());
                map.put("estudianteId", ins.getEstudianteId());
                map.put("cursoId", ins.getCursoId());
                map.put("fechaInscripcion", ins.getFechaInscripcion());
                map.put("modalidad", ins.getModalidad());
                map.put("estadoInscripcion", ins.getEstadoInscripcion());
                map.put("tipoBeca", ins.getTipoBeca());
                map.put("codigoBeca", ins.getCodigoBeca());
                map.put("certificadoGarantizado", ins.getCertificadoGarantizado());

                estudianteRepository.findById(ins.getEstudianteId())
                    .ifPresent(e -> {
                        map.put("estudianteNombre", e.getNombre() + " " + e.getApellidos());
                        map.put("estudianteMatricula", e.getMatricula());
                        map.put("estudianteEmail", e.getEmail());
                    });

                cursoRepository.findById(ins.getCursoId())
                    .ifPresent(c -> {
                        map.put("cursoNombre", c.getNombre());
                        map.put("cursoCodigo", c.getCodigo());
                    });

                result.add(map);
            }
        }
        return result;
    }

    /**
     * Aprobar una beca (solo para administradores)
     */
    @PutMapping("/becas/{id}/aprobar")
    public ResponseEntity<?> aprobarBeca(@PathVariable Integer id) {
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(id);

        if (inscripcionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Inscripcion inscripcion = inscripcionOpt.get();

        if (!"BECA".equals(inscripcion.getModalidad())) {
            return ResponseEntity.badRequest().body(Map.of(
                "exitoso", false,
                "mensaje", "Esta inscripción no es de tipo BECA"
            ));
        }

        // Cambiar estado a Activa
        inscripcion.setEstadoInscripcion("Activa");
        Inscripcion saved = inscripcionRepository.save(inscripcion);

        // 📧 NOTIFICAR AL ESTUDIANTE Y PROFESOR
        try {
            Estudiante estudiante = estudianteRepository.findById(saved.getEstudianteId()).orElse(null);
            Curso curso = cursoRepository.findById(saved.getCursoId()).orElse(null);

            if (estudiante != null && curso != null) {
                String estudianteNombre = estudiante.getNombre() + " " + estudiante.getApellidos();

                // 1. Notificar SOLO al estudiante sobre la aprobación
                NotificationEvent eventEstudiante = new NotificationEvent.Builder()
                    .eventType(NotificationEvent.EventType.BECA_APROBADA)
                    .title("Beca Aprobada")
                    .message(String.format("Tu solicitud de beca para el curso '%s' ha sido APROBADA. Ya puedes acceder al curso.",
                        curso.getNombre()))
                    .sourceUserId(null)
                    .targetId(curso.getId())
                    .targetType("INSCRIPCION")
                    .addMetadata("tipoBeca", saved.getTipoBeca())
                    .build();

                notificationOrchestrator.notifySpecificUser(saved.getEstudianteId(), eventEstudiante);

                // 2. Suscribir estudiante al curso
                notificationOrchestrator.subscribeStudentToCourse(
                    saved.getEstudianteId(),
                    estudianteNombre,
                    saved.getCursoId()
                );

                // 3. Notificar al profesor sobre el nuevo estudiante
                notificationOrchestrator.notifyStudentEnrolled(
                    saved,
                    estudianteNombre,
                    curso.getNombre()
                );

                System.out.println("📧 Notificaciones de aprobación de beca enviadas");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al enviar notificaciones: " + e.getMessage());
        }

        return ResponseEntity.ok(Map.of(
            "exitoso", true,
            "mensaje", "Beca aprobada exitosamente",
            "inscripcionId", inscripcion.getId(),
            "nuevoEstado", "Activa"
        ));
    }

    /**
     * Rechazar una beca (solo para administradores)
     */
    @PutMapping("/becas/{id}/rechazar")
    public ResponseEntity<?> rechazarBeca(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        Optional<Inscripcion> inscripcionOpt = inscripcionRepository.findById(id);

        if (inscripcionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Inscripcion inscripcion = inscripcionOpt.get();

        if (!"BECA".equals(inscripcion.getModalidad())) {
            return ResponseEntity.badRequest().body(Map.of(
                "exitoso", false,
                "mensaje", "Esta inscripción no es de tipo BECA"
            ));
        }

        String motivo = body.get("motivo");
        if (motivo == null || motivo.isEmpty()) {
            motivo = "No especificado";
        }

        // Cambiar estado a Rechazada y guardar motivo
        inscripcion.setEstadoInscripcion("Rechazada");
        inscripcion.setMotivoRechazo(motivo);
        Inscripcion saved = inscripcionRepository.save(inscripcion);

        // 📧 NOTIFICAR AL ESTUDIANTE SOBRE EL RECHAZO
        try {
            Estudiante estudiante = estudianteRepository.findById(saved.getEstudianteId()).orElse(null);
            Curso curso = cursoRepository.findById(saved.getCursoId()).orElse(null);

            if (estudiante != null && curso != null) {
                // Notificar SOLO al estudiante sobre el rechazo
                NotificationEvent eventEstudiante = new NotificationEvent.Builder()
                    .eventType(NotificationEvent.EventType.BECA_RECHAZADA)
                    .title("Solicitud de Beca Rechazada")
                    .message(String.format("Tu solicitud de beca para el curso '%s' ha sido rechazada. Motivo: %s",
                        curso.getNombre(), motivo))
                    .sourceUserId(null)
                    .targetId(curso.getId())
                    .targetType("INSCRIPCION")
                    .addMetadata("tipoBeca", saved.getTipoBeca())
                    .addMetadata("motivo", motivo)
                    .build();

                notificationOrchestrator.notifySpecificUser(saved.getEstudianteId(), eventEstudiante);

                System.out.println("📧 Notificación de rechazo de beca enviada al estudiante");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al enviar notificación de rechazo: " + e.getMessage());
        }

        return ResponseEntity.ok(Map.of(
            "exitoso", true,
            "mensaje", "Beca rechazada",
            "motivo", motivo,
            "inscripcionId", inscripcion.getId(),
            "nuevoEstado", "Rechazada"
        ));
    }
}
