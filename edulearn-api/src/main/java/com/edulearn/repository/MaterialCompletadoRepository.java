package com.edulearn.repository;

import com.edulearn.model.MaterialCompletado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MaterialCompletadoRepository extends JpaRepository<MaterialCompletado, Integer> {

    List<MaterialCompletado> findByEstudianteIdAndCursoId(Integer estudianteId, Integer cursoId);

    Optional<MaterialCompletado> findByEstudianteIdAndCursoIdAndMaterialId(
        Integer estudianteId, Integer cursoId, Long materialId);

    boolean existsByEstudianteIdAndCursoIdAndMaterialId(
        Integer estudianteId, Integer cursoId, Long materialId);

    long countByEstudianteIdAndCursoId(Integer estudianteId, Integer cursoId);

    void deleteByEstudianteIdAndCursoIdAndMaterialId(
        Integer estudianteId, Integer cursoId, Long materialId);

    void deleteByMaterialId(Long materialId);
}
