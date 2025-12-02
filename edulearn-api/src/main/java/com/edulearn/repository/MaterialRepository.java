package com.edulearn.repository;

import com.edulearn.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByModuloIdOrderByOrdenAsc(Long moduloId);
    List<Material> findByCursoId(Integer cursoId);
    List<Material> findByModuloId(Long moduloId);
    long countByModuloId(Long moduloId);
    long countByCursoId(Integer cursoId);
}
