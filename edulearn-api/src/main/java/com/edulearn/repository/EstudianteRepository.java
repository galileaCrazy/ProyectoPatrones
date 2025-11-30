package com.edulearn.repository;

import com.edulearn.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {
    @Query("SELECT e FROM Estudiante e WHERE e.usuario.email = :email")
    Optional<Estudiante> findByEmail(@Param("email") String email);

    Optional<Estudiante> findByMatricula(String matricula);
}
