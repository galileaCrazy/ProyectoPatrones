package com.edulearn.repository;

import com.edulearn.model.PeriodoAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface PeriodoAcademicoRepository extends JpaRepository<PeriodoAcademico, Long> {

    Optional<PeriodoAcademico> findByCodigo(String codigo);

    List<PeriodoAcademico> findByEsActual(Boolean esActual);

    List<PeriodoAcademico> findByEstado(String estado);

    List<PeriodoAcademico> findAllByOrderByFechaInicioDesc();

    boolean existsByCodigo(String codigo);
}
