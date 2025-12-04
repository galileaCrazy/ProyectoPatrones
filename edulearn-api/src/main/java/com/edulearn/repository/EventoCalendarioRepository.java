package com.edulearn.repository;

import com.edulearn.model.EventoCalendario;
import com.edulearn.model.PeriodoAcademico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoCalendarioRepository extends JpaRepository<EventoCalendario, Long> {

    List<EventoCalendario> findByPeriodoAcademico(PeriodoAcademico periodoAcademico);

    List<EventoCalendario> findByCursoId(Integer cursoId);

    List<EventoCalendario> findByTipo(String tipo);

    @Query("SELECT e FROM EventoCalendario e WHERE e.fechaInicio >= :inicio AND e.fechaFin <= :fin ORDER BY e.fechaInicio")
    List<EventoCalendario> findEventosEntreFechas(LocalDateTime inicio, LocalDateTime fin);

    @Query("SELECT e FROM EventoCalendario e WHERE e.fechaInicio >= :inicio AND e.fechaFin <= :fin AND e.cursoId = :cursoId ORDER BY e.fechaInicio")
    List<EventoCalendario> findEventosPorCursoEntreFechas(Integer cursoId, LocalDateTime inicio, LocalDateTime fin);

    List<EventoCalendario> findAllByOrderByFechaInicioAsc();
}
