package com.edulearn.repository;

import com.edulearn.model.IntegracionCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IntegracionCursoRepository extends JpaRepository<IntegracionCurso, Long> {

    List<IntegracionCurso> findByCursoId(Integer cursoId);

    List<IntegracionCurso> findByCursoIdAndEstado(Integer cursoId, String estado);

    List<IntegracionCurso> findByProfesorId(Integer profesorId);

    List<IntegracionCurso> findByCursoIdAndTipo(Integer cursoId, String tipo);

    List<IntegracionCurso> findByCursoIdAndProveedor(Integer cursoId, String proveedor);

    Optional<IntegracionCurso> findByIdentificadorExterno(String identificadorExterno);

    boolean existsByIdentificadorExterno(String identificadorExterno);
}
