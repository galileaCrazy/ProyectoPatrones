package com.edulearn.repository;

import com.edulearn.model.CursoBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository para cursos creados con el patrón Builder
 */
@Repository
public interface CursoBuilderRepository extends JpaRepository<CursoBuilder, Long> {

    Optional<CursoBuilder> findByCodigo(String codigo);

    List<CursoBuilder> findByModalidad(String modalidad);

    List<CursoBuilder> findByNivelDificultad(String nivelDificultad);

    List<CursoBuilder> findByCategoria(String categoria);

    List<CursoBuilder> findByEstado(String estado);

    List<CursoBuilder> findByTipoConstruccion(String tipoConstruccion);

    List<CursoBuilder> findByPrecioBetween(Double min, Double max);

    List<CursoBuilder> findByIncluyeCertificado(Boolean incluyeCertificado);
}
