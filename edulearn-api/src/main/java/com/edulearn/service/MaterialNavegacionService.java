package com.edulearn.service;

import com.edulearn.model.Material;
import com.edulearn.model.MaterialCompletado;
import com.edulearn.model.ProgresoEstudiante;
import com.edulearn.repository.MaterialCompletadoRepository;
import com.edulearn.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Servicio para manejar la navegación de materiales y actualización de progreso
 */
@Service
public class MaterialNavegacionService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private MaterialCompletadoRepository materialCompletadoRepository;

    @Autowired
    private ProgresoEstudianteService progresoService;

    /**
     * Obtiene todos los materiales de un curso ordenados
     */
    public List<Material> obtenerMaterialesCurso(Integer cursoId) {
        return materialRepository.findByCursoId(cursoId)
            .stream()
            .sorted((a, b) -> a.getOrden().compareTo(b.getOrden()))
            .toList();
    }

    /**
     * Obtiene un material específico
     */
    public Optional<Material> obtenerMaterial(Long materialId) {
        return materialRepository.findById(materialId);
    }

    /**
     * Obtiene información del material actual con navegación
     */
    public Map<String, Object> obtenerMaterialConNavegacion(
            Integer estudianteId, Integer cursoId, Long materialId) {

        Material material = materialRepository.findById(materialId)
            .orElseThrow(() -> new RuntimeException("Material no encontrado"));

        List<Material> todosMateriales = obtenerMaterialesCurso(cursoId);

        int indiceActual = -1;
        for (int i = 0; i < todosMateriales.size(); i++) {
            if (todosMateriales.get(i).getId().equals(materialId)) {
                indiceActual = i;
                break;
            }
        }

        Material anterior = indiceActual > 0 ? todosMateriales.get(indiceActual - 1) : null;
        Material siguiente = indiceActual < todosMateriales.size() - 1
            ? todosMateriales.get(indiceActual + 1) : null;

        boolean estaCompletado = materialCompletadoRepository
            .existsByEstudianteIdAndCursoIdAndMaterialId(estudianteId, cursoId, materialId);

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("material", material);
        resultado.put("anterior", anterior);
        resultado.put("siguiente", siguiente);
        resultado.put("completado", estaCompletado);
        resultado.put("indice", indiceActual + 1);
        resultado.put("total", todosMateriales.size());

        return resultado;
    }

    /**
     * Marca un material como completado y actualiza el progreso del estudiante
     */
    @Transactional
    public Map<String, Object> completarMaterial(
            Integer estudianteId, Integer cursoId, Long materialId) {

        // Verificar si ya está completado
        boolean yaCompletado = materialCompletadoRepository
            .existsByEstudianteIdAndCursoIdAndMaterialId(estudianteId, cursoId, materialId);

        if (!yaCompletado) {
            // Marcar como completado
            MaterialCompletado completado = new MaterialCompletado(estudianteId, cursoId, materialId);
            materialCompletadoRepository.save(completado);
        }

        // Actualizar progreso del estudiante
        ProgresoEstudiante progreso = progresoService.obtenerOCrearProgreso(estudianteId, cursoId);
        actualizarProgreso(estudianteId, cursoId, progreso);

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("completado", true);
        resultado.put("progreso", progreso);
        resultado.put("yaEstaba", yaCompletado);

        return resultado;
    }

    /**
     * Desmarca un material como completado
     */
    @Transactional
    public void descompletarMaterial(Integer estudianteId, Integer cursoId, Long materialId) {
        materialCompletadoRepository.deleteByEstudianteIdAndCursoIdAndMaterialId(
            estudianteId, cursoId, materialId);

        // Actualizar progreso
        ProgresoEstudiante progreso = progresoService.obtenerOCrearProgreso(estudianteId, cursoId);
        actualizarProgreso(estudianteId, cursoId, progreso);
    }

    /**
     * Obtiene la lista de materiales completados por un estudiante
     */
    public List<Long> obtenerMaterialesCompletados(Integer estudianteId, Integer cursoId) {
        return materialCompletadoRepository
            .findByEstudianteIdAndCursoId(estudianteId, cursoId)
            .stream()
            .map(MaterialCompletado::getMaterialId)
            .toList();
    }

    /**
     * Obtiene el primer material no completado del curso
     */
    public Optional<Material> obtenerPrimerMaterialPendiente(Integer estudianteId, Integer cursoId) {
        List<Material> todosMateriales = obtenerMaterialesCurso(cursoId);
        List<Long> completados = obtenerMaterialesCompletados(estudianteId, cursoId);

        return todosMateriales.stream()
            .filter(m -> !completados.contains(m.getId()))
            .findFirst();
    }

    /**
     * Actualiza el progreso del estudiante basado en materiales completados
     */
    private void actualizarProgreso(Integer estudianteId, Integer cursoId, ProgresoEstudiante progreso) {
        long totalMateriales = materialRepository.countByCursoId(cursoId);
        long materialesCompletados = materialCompletadoRepository.countByEstudianteIdAndCursoId(
            estudianteId, cursoId);

        if (totalMateriales > 0) {
            int porcentaje = (int) ((materialesCompletados * 100) / totalMateriales);
            progreso.setPorcentajeCompletado(porcentaje);
            progreso.setLeccionesCompletadas((int) materialesCompletados);

            if (porcentaje >= 100) {
                progreso.setEstadoCurso("COMPLETADO");
            } else if (materialesCompletados > 0) {
                progreso.setEstadoCurso("EN_PROGRESO");
            }

            progresoService.actualizarProgreso(progreso);
        }
    }

    /**
     * Obtiene estadísticas de progreso del estudiante en el curso
     */
    public Map<String, Object> obtenerEstadisticasProgreso(Integer estudianteId, Integer cursoId) {
        long totalMateriales = materialRepository.countByCursoId(cursoId);
        long materialesCompletados = materialCompletadoRepository.countByEstudianteIdAndCursoId(
            estudianteId, cursoId);

        int porcentaje = totalMateriales > 0
            ? (int) ((materialesCompletados * 100) / totalMateriales) : 0;

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalMateriales", totalMateriales);
        stats.put("materialesCompletados", materialesCompletados);
        stats.put("materialesPendientes", totalMateriales - materialesCompletados);
        stats.put("porcentajeCompletado", porcentaje);

        return stats;
    }

    /**
     * Obtiene la lista de materiales sin archivo asociado (urlRecurso o archivoPath vacío)
     */
    public List<Material> obtenerMaterialesSinArchivo() {
        return materialRepository.findAll()
            .stream()
            .filter(m -> (m.getUrlRecurso() == null || m.getUrlRecurso().trim().isEmpty()) &&
                         (m.getArchivoPath() == null || m.getArchivoPath().trim().isEmpty()))
            .toList();
    }

    /**
     * Elimina todos los materiales sin archivo asociado
     */
    @Transactional
    public Map<String, Object> eliminarMaterialesSinArchivo() {
        List<Material> materialesSinArchivo = obtenerMaterialesSinArchivo();
        int cantidadEliminada = materialesSinArchivo.size();

        // Eliminar registros de materialCompletado asociados
        for (Material material : materialesSinArchivo) {
            materialCompletadoRepository.deleteByMaterialId(material.getId());
        }

        // Eliminar los materiales
        materialRepository.deleteAll(materialesSinArchivo);

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("eliminados", cantidadEliminada);
        resultado.put("mensaje", cantidadEliminada > 0
            ? "Se eliminaron " + cantidadEliminada + " materiales sin archivo asociado"
            : "No se encontraron materiales sin archivo para eliminar");

        return resultado;
    }
}
