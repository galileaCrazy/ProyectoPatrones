package com.edulearn.repository;

import com.edulearn.model.ModuloCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ModuloCursoRepository extends JpaRepository<ModuloCurso, Integer> {
    List<ModuloCurso> findByCursoId(Integer cursoId);
    List<ModuloCurso> findByCursoIdOrderByOrden(Integer cursoId);
    List<ModuloCurso> findByModuloPadreId(Integer moduloPadreId);
    List<ModuloCurso> findByCursoIdAndModuloPadreIdIsNull(Integer cursoId);
    List<ModuloCurso> findByEstado(String estado);
}
