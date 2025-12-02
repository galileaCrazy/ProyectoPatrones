package com.edulearn.repository;

import com.edulearn.model.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
    List<Evaluacion> findByModuloId(Long moduloId);
    List<Evaluacion> findByModuloIdOrderByIdAsc(Long moduloId);
    long countByModuloId(Long moduloId);
}
