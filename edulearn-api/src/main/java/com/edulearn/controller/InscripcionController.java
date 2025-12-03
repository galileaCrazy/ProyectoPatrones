package com.edulearn.controller;

import com.edulearn.model.Inscripcion;
import com.edulearn.repository.InscripcionRepository;
import com.edulearn.repository.EstudianteRepository;
import com.edulearn.repository.CursoRepository;
import com.edulearn.patterns.comportamiento.template_method.InscripcionTemplateService;
import com.edulearn.patterns.comportamiento.template_method.dto.SolicitudInscripcion;
import com.edulearn.patterns.comportamiento.template_method.dto.ResultadoInscripcion;
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
        return inscripcionRepository.save(inscripcion);
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
        inscripcionRepository.save(inscripcion);

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
        inscripcionRepository.save(inscripcion);

        return ResponseEntity.ok(Map.of(
            "exitoso", true,
            "mensaje", "Beca rechazada",
            "motivo", motivo,
            "inscripcionId", inscripcion.getId(),
            "nuevoEstado", "Rechazada"
        ));
    }
}
