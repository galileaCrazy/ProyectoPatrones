package com.edulearn.repository;

import com.edulearn.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByModuloIdOrderByOrdenAsc(Long moduloId);
    List<Material> findByModuloIdOrderByOrden(Long moduloId);
    List<Material> findByCursoId(Integer cursoId);
    List<Material> findByModuloId(Long moduloId);
    long countByModuloId(Long moduloId);
    long countByCursoId(Integer cursoId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Material m WHERE m.moduloId = :moduloId")
    void deleteByModuloId(Long moduloId);
}
