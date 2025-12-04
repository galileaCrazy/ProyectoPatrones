package com.edulearn.patterns.estructural.facade.subsistemas;

import com.edulearn.model.Material;
import com.edulearn.repository.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Subsistema de Materiales (Contenido)
 * Responsable de gestionar el acceso a los materiales del curso
 * Prepara y asigna los materiales educativos al estudiante inscrito
 */
@Component
public class SubsistemaMateriales {

    @Autowired
    private MaterialRepository materialRepository;

    /**
     * Prepara los materiales del curso para el estudiante
     * En un sistema real, esto podría crear registros de acceso o visibilidad
     * @return true si hay materiales disponibles
     */
    public boolean prepararMaterialesCurso(Integer cursoId, Integer estudianteId) {
        // Obtener todos los materiales activos del curso
        List<Material> materiales = obtenerMaterialesPorCurso(cursoId);

        if (materiales.isEmpty()) {
            // No hay materiales, pero no es un error crítico
            System.out.println("⚠️ El curso " + cursoId + " no tiene materiales asignados");
            return false;
        }

        // En un sistema real, aquí se crearían registros de acceso del estudiante a materiales
        System.out.println("📚 Preparados " + materiales.size() + " materiales para el estudiante " + estudianteId);

        // Registrar materiales por tipo
        long videos = materiales.stream()
                .filter(m -> "video".equalsIgnoreCase(m.getTipoMaterial()))
                .count();
        long documentos = materiales.stream()
                .filter(m -> "pdf".equalsIgnoreCase(m.getTipoMaterial()) ||
                           "documento".equalsIgnoreCase(m.getTipoMaterial()))
                .count();

        System.out.println("  - Videos: " + videos);
        System.out.println("  - Documentos: " + documentos);

        return true;
    }

    /**
     * Obtiene todos los materiales activos de un curso
     */
    public List<Material> obtenerMaterialesPorCurso(Integer cursoId) {
        return materialRepository.findAll().stream()
                .filter(m -> m.getCursoId() != null &&
                           m.getCursoId().equals(cursoId) &&
                           "activo".equalsIgnoreCase(m.getEstado()))
                .collect(Collectors.toList());
    }

    /**
     * Cuenta los materiales disponibles para un curso
     */
    public int contarMaterialesCurso(Integer cursoId) {
        return obtenerMaterialesPorCurso(cursoId).size();
    }

    /**
     * Verifica si hay materiales obligatorios
     */
    public boolean tieneMaterialesObligatorios(Integer cursoId) {
        return obtenerMaterialesPorCurso(cursoId).stream()
                .anyMatch(m -> Boolean.TRUE.equals(m.getEsObligatorio()));
    }
}
