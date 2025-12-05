package com.edulearn.repository;

import com.edulearn.model.HistorialProgresoEstudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialProgresoEstudianteRepository extends JpaRepository<HistorialProgresoEstudiante, Integer> {

    List<HistorialProgresoEstudiante> findByEstudianteIdAndCursoIdOrderByFechaGuardadoDesc(Integer estudianteId, Integer cursoId);

    List<HistorialProgresoEstudiante> findByEstudianteIdOrderByFechaGuardadoDesc(Integer estudianteId);

    void deleteByEstudianteIdAndCursoId(Integer estudianteId, Integer cursoId);
}
