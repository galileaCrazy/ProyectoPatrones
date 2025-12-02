package com.edulearn.repository;

import com.edulearn.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CursoRepository extends JpaRepository<Curso, Integer> {

    // Obtener cursos por profesor
    List<Curso> findByProfesorTitularId(Integer profesorId);

    // Obtener cursos donde el estudiante está inscrito
    @Query("SELECT c FROM Curso c JOIN Inscripcion i ON c.id = i.cursoId WHERE i.estudianteId = :estudianteId")
    List<Curso> findCursosByEstudianteId(@Param("estudianteId") Integer estudianteId);
}
