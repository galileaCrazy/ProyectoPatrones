package com.edulearn.controller;

import com.edulearn.model.Material;
import com.edulearn.service.MaterialNavegacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para la navegación de materiales y seguimiento de progreso
 */
@RestController
@RequestMapping("/api/materiales")
@CrossOrigin(origins = "*")
public class MaterialNavegacionController {

    @Autowired
    private MaterialNavegacionService navegacionService;

    /**
     * Obtiene todos los materiales de un curso ordenados
     * GET /api/materiales/curso/{cursoId}
     */
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<Material>> obtenerMaterialesCurso(@PathVariable Integer cursoId) {
        List<Material> materiales = navegacionService.obtenerMaterialesCurso(cursoId);
        return ResponseEntity.ok(materiales);
    }

    /**
     * Obtiene un material con información de navegación
     * GET /api/materiales/{materialId}/navegacion?estudianteId={id}&cursoId={id}
     */
    @GetMapping("/{materialId}/navegacion")
    public ResponseEntity<Map<String, Object>> obtenerMaterialConNavegacion(
            @PathVariable Long materialId,
            @RequestParam Integer estudianteId,
            @RequestParam Integer cursoId) {

        Map<String, Object> resultado = navegacionService.obtenerMaterialConNavegacion(
            estudianteId, cursoId, materialId);
        return ResponseEntity.ok(resultado);
    }

    /**
     * Marca un material como completado
     * POST /api/materiales/{materialId}/completar
     */
    @PostMapping("/{materialId}/completar")
    public ResponseEntity<Map<String, Object>> completarMaterial(
            @PathVariable Long materialId,
            @RequestBody Map<String, Integer> body) {

        Integer estudianteId = body.get("estudianteId");
        Integer cursoId = body.get("cursoId");

        Map<String, Object> resultado = navegacionService.completarMaterial(
            estudianteId, cursoId, materialId);

        return ResponseEntity.ok(resultado);
    }

    /**
     * Desmarca un material como completado
     * DELETE /api/materiales/{materialId}/completar
     */
    @DeleteMapping("/{materialId}/completar")
    public ResponseEntity<Map<String, String>> descompletarMaterial(
            @PathVariable Long materialId,
            @RequestParam Integer estudianteId,
            @RequestParam Integer cursoId) {

        navegacionService.descompletarMaterial(estudianteId, cursoId, materialId);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Material desmarcado como completado");
        return ResponseEntity.ok(respuesta);
    }

    /**
     * Obtiene la lista de IDs de materiales completados
     * GET /api/materiales/completados?estudianteId={id}&cursoId={id}
     */
    @GetMapping("/completados")
    public ResponseEntity<List<Long>> obtenerMaterialesCompletados(
            @RequestParam Integer estudianteId,
            @RequestParam Integer cursoId) {

        List<Long> completados = navegacionService.obtenerMaterialesCompletados(estudianteId, cursoId);
        return ResponseEntity.ok(completados);
    }

    /**
     * Obtiene el primer material pendiente (no completado)
     * GET /api/materiales/siguiente-pendiente?estudianteId={id}&cursoId={id}
     */
    @GetMapping("/siguiente-pendiente")
    public ResponseEntity<?> obtenerSiguientePendiente(
            @RequestParam Integer estudianteId,
            @RequestParam Integer cursoId) {

        return navegacionService.obtenerPrimerMaterialPendiente(estudianteId, cursoId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.noContent().build());
    }

    /**
     * Obtiene estadísticas de progreso
     * GET /api/materiales/estadisticas?estudianteId={id}&cursoId={id}
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas(
            @RequestParam Integer estudianteId,
            @RequestParam Integer cursoId) {

        Map<String, Object> stats = navegacionService.obtenerEstadisticasProgreso(estudianteId, cursoId);
        return ResponseEntity.ok(stats);
    }

    /**
     * Obtiene un material específico
     * GET /api/materiales/{materialId}
     */
    @GetMapping("/{materialId}")
    public ResponseEntity<?> obtenerMaterial(@PathVariable Long materialId) {
        return navegacionService.obtenerMaterial(materialId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina todos los materiales sin archivo asociado
     * DELETE /api/materiales/sin-archivo
     */
    @DeleteMapping("/sin-archivo")
    public ResponseEntity<Map<String, Object>> eliminarMaterialesSinArchivo() {
        Map<String, Object> resultado = navegacionService.eliminarMaterialesSinArchivo();
        return ResponseEntity.ok(resultado);
    }

    /**
     * Obtiene la lista de materiales sin archivo asociado
     * GET /api/materiales/sin-archivo
     */
    @GetMapping("/sin-archivo")
    public ResponseEntity<List<Material>> obtenerMaterialesSinArchivo() {
        List<Material> materiales = navegacionService.obtenerMaterialesSinArchivo();
        return ResponseEntity.ok(materiales);
    }
}
