package com.edulearn.controller;

import com.edulearn.model.Evaluacion;
import com.edulearn.patterns.comportamiento.observer.NotificationEvent;
import com.edulearn.patterns.comportamiento.observer.NotificationOrchestrator;
import com.edulearn.repository.EvaluacionRepository;
import com.edulearn.repository.CursoRepository;
import com.edulearn.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para Evaluaciones
 * Soporta el patrón Command desde el frontend
 */
@RestController
@RequestMapping("/api/evaluaciones")
@CrossOrigin(origins = "*")
public class EvaluacionController {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private NotificationOrchestrator notificationOrchestrator;

    @Autowired
    private CursoRepository cursoRepository;

    /**
     * GET /api/evaluaciones
     * Obtener todas las evaluaciones
     */
    @GetMapping
    public List<Evaluacion> getAllEvaluaciones() {
        return evaluacionRepository.findAll();
    }

    /**
     * GET /api/evaluaciones/{id}
     * Obtener una evaluación por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Evaluacion> getEvaluacion(@PathVariable Long id) {
        return evaluacionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/evaluaciones
     * Crear una nueva evaluación (Comando: CreateEvaluationCommand)
     */
    @PostMapping
    public ResponseEntity<Evaluacion> createEvaluacion(@RequestBody Map<String, Object> params) {
        try {
            Evaluacion evaluacion = new Evaluacion();

            // Mapear campos del frontend al modelo
            evaluacion.setNombre((String) params.get("nombre"));
            evaluacion.setTitulo((String) params.get("nombre")); // Usar nombre como título
            evaluacion.setTipoEvaluacion((String) params.get("tipoEvaluacion"));
            evaluacion.setTipo((String) params.get("tipoEvaluacion")); // Campo requerido por BD

            // Módulo ID (cursoId)
            if (params.get("cursoId") != null) {
                evaluacion.setModuloId(Long.valueOf(params.get("cursoId").toString()));
            }

            // Puntaje máximo
            if (params.get("puntajeMaximo") != null) {
                evaluacion.setPuntajeMaximo(new BigDecimal(params.get("puntajeMaximo").toString()));
            }

            // Duración
            if (params.get("duracionMinutos") != null) {
                evaluacion.setTiempoLimiteMinutos(Integer.valueOf(params.get("duracionMinutos").toString()));
            }

            // Intentos
            if (params.get("intentosPermitidos") != null) {
                evaluacion.setIntentosPermitidos(Integer.valueOf(params.get("intentosPermitidos").toString()));
            }

            // Fechas
            if (params.get("fechaInicio") != null) {
                String fechaStr = (String) params.get("fechaInicio");
                evaluacion.setFechaApertura(LocalDateTime.parse(fechaStr + "T00:00:00"));
            }

            if (params.get("fechaLimite") != null) {
                String fechaStr = (String) params.get("fechaLimite");
                evaluacion.setFechaCierre(LocalDateTime.parse(fechaStr + "T23:59:59"));
            }

            // Estado
            String estado = (String) params.getOrDefault("estado", "Borrador");
            evaluacion.setEstado(estado.toLowerCase());

           Evaluacion saved = evaluacionRepository.save(evaluacion);

            // PATRÓN OBSERVER: Notificar creación de tarea a estudiantes inscritos del curso
            Integer cursoId = null;
            String cursoNombre = "";
            try {
                Object cursoIdObj = params.get("cursoId");
                if (cursoIdObj != null) {
                    cursoId = Integer.valueOf(cursoIdObj.toString());
                    cursoNombre = cursoRepository.findById(cursoId)
                        .map(c -> c.getNombre())
                        .orElse("");
                }
            } catch (Exception ex) {
                // Si falla la obtención del curso, se enviará con el ID sin nombre
            }

            if (cursoId != null) {
                notificationOrchestrator.notifyAssignmentCreated(
                    saved,
                    cursoId,
                    cursoNombre
                );
            }

            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * PUT /api/evaluaciones/{id}
     * Actualizar una evaluación (Comandos: PublishCommand, CancelCommand, etc.)
     */
    @PutMapping("/{id}")
    public ResponseEntity<Evaluacion> updateEvaluacion(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        return evaluacionRepository.findById(id)
                .map(evaluacion -> {
                    // Actualizar estado
                    if (updates.containsKey("estado")) {
                        String estado = (String) updates.get("estado");
                        evaluacion.setEstado(estado.toLowerCase());
                    }

                    // Actualizar fecha límite
                    if (updates.containsKey("fechaLimite")) {
                        String fechaStr = (String) updates.get("fechaLimite");
                        evaluacion.setFechaCierre(LocalDateTime.parse(fechaStr));
                    }

                    // Actualizar fecha de inicio
                    if (updates.containsKey("fechaInicio")) {
                        String fechaStr = (String) updates.get("fechaInicio");
                        evaluacion.setFechaApertura(LocalDateTime.parse(fechaStr));
                    }

                    // Actualizar otros campos si es necesario
                    if (updates.containsKey("nombre")) {
                        evaluacion.setNombre((String) updates.get("nombre"));
                        evaluacion.setTitulo((String) updates.get("nombre"));
                    }

                    Evaluacion saved = evaluacionRepository.save(evaluacion);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/evaluaciones/{id}
     * Eliminar una evaluación (Para el comando Undo después de Create)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluacion(@PathVariable Long id) {
        if (evaluacionRepository.existsById(id)) {
            evaluacionRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
