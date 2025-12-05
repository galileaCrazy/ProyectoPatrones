package com.edulearn.patterns.comportamiento.template_method;

import com.edulearn.model.Curso;
import com.edulearn.model.Estudiante;
import com.edulearn.model.Inscripcion;
import com.edulearn.patterns.comportamiento.observer.NotificationEvent;
import com.edulearn.patterns.comportamiento.observer.NotificationOrchestrator;
import com.edulearn.patterns.comportamiento.template_method.dto.ResultadoInscripcion;
import com.edulearn.patterns.comportamiento.template_method.dto.SolicitudInscripcion;
import com.edulearn.repository.CursoRepository;
import com.edulearn.repository.EstudianteRepository;
import com.edulearn.repository.InscripcionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

/**
 * Servicio que gestiona el proceso de inscripción utilizando el patrón Template Method
 * 
 * Este servicio actúa como fábrica para seleccionar el tipo de proceso de inscripción
 * correcto y ejecutarlo.
 */
@Service
public class InscripcionTemplateService {

    private static final Logger logger = LoggerFactory.getLogger(InscripcionTemplateService.class);

    private final Map<String, ProcesoInscripcionTemplate> procesosInscripcion;
    private final EstudianteRepository estudianteRepository;
    private final CursoRepository cursoRepository;
    private final InscripcionRepository inscripcionRepository;
    private final NotificationOrchestrator notificationOrchestrator;

    @Autowired
    public InscripcionTemplateService(
            InscripcionGratuita inscripcionGratuita,
            InscripcionPaga inscripcionPaga,
            InscripcionBeca inscripcionBeca,
            EstudianteRepository estudianteRepository,
            CursoRepository cursoRepository,
            InscripcionRepository inscripcionRepository,
            NotificationOrchestrator notificationOrchestrator) {

        this.procesosInscripcion = new HashMap<>();
        this.procesosInscripcion.put("GRATUITA", inscripcionGratuita);
        this.procesosInscripcion.put("PAGA", inscripcionPaga);
        this.procesosInscripcion.put("BECA", inscripcionBeca);

        this.estudianteRepository = estudianteRepository;
        this.cursoRepository = cursoRepository;
        this.inscripcionRepository = inscripcionRepository;
        this.notificationOrchestrator = notificationOrchestrator;
    }
    
    /**
     * Procesa una solicitud de inscripción según el tipo especificado
     */
    @Transactional
    public ResultadoInscripcion procesarInscripcion(SolicitudInscripcion solicitud) {
        // Validar tipo de inscripción
        String tipo = solicitud.getTipoInscripcion();
        if (tipo == null || !procesosInscripcion.containsKey(tipo.toUpperCase())) {
            ResultadoInscripcion resultado = new ResultadoInscripcion();
            resultado.setExitoso(false);
            resultado.setEstado("FALLIDA");
            resultado.setMensaje("Tipo de inscripción no válido: " + tipo + 
                               ". Tipos válidos: " + String.join(", ", procesosInscripcion.keySet()));
            return resultado;
        }
        
        // Obtener estudiante
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(solicitud.getEstudianteId());
        if (estudianteOpt.isEmpty()) {
            ResultadoInscripcion resultado = new ResultadoInscripcion();
            resultado.setExitoso(false);
            resultado.setEstado("FALLIDA");
            resultado.setMensaje("Estudiante no encontrado con ID: " + solicitud.getEstudianteId());
            return resultado;
        }
        
        // Obtener curso
        Optional<Curso> cursoOpt = cursoRepository.findById(solicitud.getCursoId());
        if (cursoOpt.isEmpty()) {
            ResultadoInscripcion resultado = new ResultadoInscripcion();
            resultado.setExitoso(false);
            resultado.setEstado("FALLIDA");
            resultado.setMensaje("Curso no encontrado con ID: " + solicitud.getCursoId());
            return resultado;
        }
        
        // Verificar que no esté ya inscrito
        List<Inscripcion> inscripcionesExistentes = inscripcionRepository.findByEstudianteId(solicitud.getEstudianteId());
        boolean yaInscrito = inscripcionesExistentes != null && inscripcionesExistentes.stream()
                .anyMatch(i -> i.getCursoId().equals(solicitud.getCursoId()));
        
        if (yaInscrito) {
            ResultadoInscripcion resultado = new ResultadoInscripcion();
            resultado.setExitoso(false);
            resultado.setEstado("FALLIDA");
            resultado.setMensaje("El estudiante ya está inscrito en este curso");
            return resultado;
        }
        
        // Obtener el proceso de inscripción correspondiente
        ProcesoInscripcionTemplate proceso = procesosInscripcion.get(tipo.toUpperCase());
        
        // Ejecutar el template method
        ResultadoInscripcion resultado = proceso.procesarInscripcion(
                estudianteOpt.get(), 
                cursoOpt.get(), 
                solicitud);
        
        // Si fue exitoso, persistir la inscripción con todos los datos
        if (resultado.isExitoso()) {
            Inscripcion inscripcion = new Inscripcion();
            inscripcion.setEstudianteId(solicitud.getEstudianteId());
            inscripcion.setCursoId(solicitud.getCursoId());
            inscripcion.setFechaInscripcion(LocalDate.now());

            // Datos del Template Method
            inscripcion.setModalidad(proceso.getTipoInscripcion());
            inscripcion.setEstadoInscripcion(proceso.getEstadoInscripcion());
            inscripcion.setCertificadoGarantizado(proceso.tieneCertificadoGarantizado());

            // Datos específicos según modalidad
            if ("PAGA".equals(tipo.toUpperCase())) {
                inscripcion.setMetodoPago(solicitud.getMetodoPago());
                inscripcion.setMontoPagado(new java.math.BigDecimal("500.00"));

                // Extraer transacción ID de los pasos del resultado
                resultado.getPasos().stream()
                    .filter(paso -> paso.getDetalles() != null && paso.getDetalles().containsKey("transaccionId"))
                    .findFirst()
                    .ifPresent(paso -> inscripcion.setTransaccionId((String) paso.getDetalles().get("transaccionId")));
            }

            if ("BECA".equals(tipo.toUpperCase())) {
                inscripcion.setTipoBeca(solicitud.getTipoBeca());
                inscripcion.setCodigoBeca(solicitud.getCodigoBeca());
                inscripcion.setMontoPagado(java.math.BigDecimal.ZERO); // Becas no pagan
            }

            if ("GRATUITA".equals(tipo.toUpperCase())) {
                inscripcion.setMontoPagado(java.math.BigDecimal.ZERO); // Sin costo
            }

            Inscripcion guardada = inscripcionRepository.save(inscripcion);
            resultado.setNumeroInscripcion("INS-" + guardada.getId());
            resultado.agregarDetalle("inscripcionId", String.valueOf(guardada.getId()));
            resultado.agregarDetalle("modalidad", guardada.getModalidad());
            resultado.agregarDetalle("estadoInscripcion", guardada.getEstadoInscripcion());
            resultado.agregarDetalle("certificadoGarantizado", String.valueOf(guardada.getCertificadoGarantizado()));

            // 📧 ENVIAR NOTIFICACIONES según el tipo de inscripción
            try {
                Estudiante estudiante = estudianteOpt.get();
                Curso curso = cursoOpt.get();
                String estudianteNombre = estudiante.getNombre() + " " + estudiante.getApellidos();

                // Si es BECA, notificar a ADMINISTRADORES
                if ("BECA".equals(tipo.toUpperCase())) {
                    NotificationEvent event = new NotificationEvent.Builder()
                        .eventType(NotificationEvent.EventType.BECA_SOLICITADA)
                        .title("Nueva Solicitud de Beca")
                        .message(String.format("El estudiante %s ha solicitado una beca para el curso '%s' (Tipo: %s)",
                            estudianteNombre, curso.getNombre(), guardada.getTipoBeca()))
                        .sourceUserId(guardada.getEstudianteId())
                        .targetId(guardada.getCursoId())
                        .targetType("INSCRIPCION")
                        .addMetadata("inscripcionId", guardada.getId())
                        .addMetadata("tipoBeca", guardada.getTipoBeca())
                        .addMetadata("codigoBeca", guardada.getCodigoBeca())
                        .build();

                    notificationOrchestrator.notifyRoleObservers("admin", event);
                    logger.info("📧 Notificación de solicitud de beca enviada a administradores - Inscripción ID: {}", guardada.getId());
                }

                // Si es GRATUITA o inscripción activa, suscribir estudiante y notificar profesor
                if ("Activa".equals(guardada.getEstadoInscripcion())) {
                    // Suscribir estudiante al curso
                    notificationOrchestrator.subscribeStudentToCourse(
                        guardada.getEstudianteId(),
                        estudianteNombre,
                        guardada.getCursoId()
                    );

                    // Notificar al profesor
                    notificationOrchestrator.notifyStudentEnrolled(
                        guardada,
                        estudianteNombre,
                        curso.getNombre()
                    );

                    logger.info("📧 Estudiante suscrito al curso y profesor notificado - Inscripción ID: {}", guardada.getId());
                }
            } catch (Exception e) {
                logger.error("⚠️ Error al enviar notificaciones para inscripción: {}", e.getMessage());
            }
        }

        return resultado;
    }
    
    /**
     * Obtiene información sobre los tipos de inscripción disponibles
     */
    public List<Map<String, Object>> getTiposInscripcion() {
        List<Map<String, Object>> tipos = new ArrayList<>();
        
        for (Map.Entry<String, ProcesoInscripcionTemplate> entry : procesosInscripcion.entrySet()) {
            Map<String, Object> info = new HashMap<>();
            info.put("tipo", entry.getKey());
            info.put("descripcion", entry.getValue().getDescripcion());
            info.put("pasos", entry.getValue().getPasosEspecificos());
            tipos.add(info);
        }
        
        return tipos;
    }
    
    /**
     * Obtiene los pasos específicos para un tipo de inscripción
     */
    public List<String> getPasosParaTipo(String tipo) {
        ProcesoInscripcionTemplate proceso = procesosInscripcion.get(tipo.toUpperCase());
        if (proceso == null) {
            return Collections.emptyList();
        }
        return proceso.getPasosEspecificos();
    }
    
    /**
     * Información de demostración del patrón
     */
    public Map<String, Object> getDemoInfo() {
        Map<String, Object> demo = new HashMap<>();
        
        demo.put("patron", "Template Method");
        demo.put("proposito", "Definir el esqueleto de un algoritmo en un método, " +
                             "delegando algunos pasos a las subclases sin cambiar la estructura.");
        
        demo.put("ventajas", Arrays.asList(
            "Reutilización de código - el algoritmo común está en la clase base",
            "Extensibilidad - nuevos tipos de inscripción solo implementan pasos específicos",
            "Inversión de control - la clase base llama a los métodos de las subclases",
            "Consistencia - todos los tipos siguen el mismo flujo general"
        ));
        
        demo.put("tiposDisponibles", getTiposInscripcion());
        
        demo.put("ejemploUso", Map.of(
            "descripcion", "Procesar inscripción gratuita para un estudiante",
            "endpoint", "POST /api/inscripciones/proceso",
            "body", Map.of(
                "estudianteId", 4,
                "cursoId", 4,
                "tipoInscripcion", "GRATUITA",
                "aceptaTerminos", true
            )
        ));
        
        demo.put("flujoGeneral", Arrays.asList(
            "1. Validar requisitos previos (común)",
            "2. Verificar disponibilidad (común)",
            "3. Validar documentación (específico por tipo)",
            "4. Procesar aspecto económico (específico por tipo)",
            "5. Aplicar beneficios (hook - opcional)",
            "6. Registrar inscripción (común)",
            "7. Enviar notificaciones (común)",
            "8. Generar documentos (específico por tipo)"
        ));
        
        return demo;
    }
    
    /**
     * Obtiene cursos disponibles para inscripción
     */
    public List<Curso> getCursosDisponibles() {
        return cursoRepository.findAll();
    }
    
    /**
     * Verifica si un estudiante puede inscribirse a un curso
     */
    public Map<String, Object> verificarElegibilidad(Integer estudianteId, Integer cursoId) {
        Map<String, Object> resultado = new HashMap<>();
        
        Optional<Estudiante> estudiante = estudianteRepository.findById(estudianteId);
        Optional<Curso> curso = cursoRepository.findById(cursoId);
        
        if (estudiante.isEmpty()) {
            resultado.put("elegible", false);
            resultado.put("mensaje", "Estudiante no encontrado");
            return resultado;
        }
        
        if (curso.isEmpty()) {
            resultado.put("elegible", false);
            resultado.put("mensaje", "Curso no encontrado");
            return resultado;
        }
        
        // Verificar si ya está inscrito
        List<Inscripcion> inscripciones = inscripcionRepository.findByEstudianteId(estudianteId);
        boolean yaInscrito = inscripciones != null && inscripciones.stream()
                .anyMatch(i -> i.getCursoId().equals(cursoId));
        
        if (yaInscrito) {
            resultado.put("elegible", false);
            resultado.put("mensaje", "Ya está inscrito en este curso");
            resultado.put("yaInscrito", true);
            return resultado;
        }
        
        resultado.put("elegible", true);
        resultado.put("mensaje", "El estudiante puede inscribirse al curso");
        resultado.put("estudiante", Map.of(
            "id", estudiante.get().getId(),
            "nombre", estudiante.get().getNombre() != null ? estudiante.get().getNombre() : "N/A",
            "matricula", estudiante.get().getMatricula() != null ? estudiante.get().getMatricula() : "N/A"
        ));
        resultado.put("curso", Map.of(
            "id", curso.get().getId(),
            "nombre", curso.get().getNombre(),
            "codigo", curso.get().getCodigo()
        ));
        resultado.put("tiposInscripcionDisponibles", Arrays.asList("GRATUITA", "PAGA", "BECA"));
        
        return resultado;
    }
}
