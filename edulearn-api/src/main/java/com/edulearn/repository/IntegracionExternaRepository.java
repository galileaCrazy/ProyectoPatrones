package com.edulearn.repository;

import com.edulearn.model.IntegracionExterna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IntegracionExternaRepository extends JpaRepository<IntegracionExterna, Integer> {
    List<IntegracionExterna> findByTipoSistema(String tipoSistema);
    List<IntegracionExterna> findByEstado(String estado);
    List<IntegracionExterna> findByCursoId(Integer cursoId);
}
