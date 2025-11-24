package com.edulearn.repository;

import com.edulearn.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Integer> {
    List<Inscripcion> findByEstudianteId(Integer estudianteId);
}
