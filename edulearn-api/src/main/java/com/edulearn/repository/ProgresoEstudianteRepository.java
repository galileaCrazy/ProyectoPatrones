package com.edulearn.repository;

import com.edulearn.model.ProgresoEstudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgresoEstudianteRepository extends JpaRepository<ProgresoEstudiante, Integer> {

    Optional<ProgresoEstudiante> findByEstudianteIdAndCursoId(Integer estudianteId, Integer cursoId);

    List<ProgresoEstudiante> findByEstudianteId(Integer estudianteId);

    List<ProgresoEstudiante> findByCursoId(Integer cursoId);

    List<ProgresoEstudiante> findByEstadoCurso(String estadoCurso);

    List<ProgresoEstudiante> findByEstudianteIdAndEstadoCurso(Integer estudianteId, String estadoCurso);
}
