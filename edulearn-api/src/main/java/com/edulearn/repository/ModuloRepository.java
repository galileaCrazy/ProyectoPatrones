package com.edulearn.repository;

import com.edulearn.model.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ModuloRepository extends JpaRepository<Modulo, Long> {
    List<Modulo> findByCursoIdOrderByOrdenAsc(Integer cursoId);
    List<Modulo> findByCursoId(Integer cursoId);
    long countByCursoId(Integer cursoId);
}
