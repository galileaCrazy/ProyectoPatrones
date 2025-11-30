package com.edulearn.repository;

import com.edulearn.model.ContenidoEducativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para ContenidoEducativo
 */
@Repository
public interface ContenidoEducativoRepository extends JpaRepository<ContenidoEducativo, Long> {

    List<ContenidoEducativo> findByTipo(String tipo);

    List<ContenidoEducativo> findByNivel(String nivel);

    List<ContenidoEducativo> findByTipoAndNivel(String tipo, String nivel);

    List<ContenidoEducativo> findByCursoId(Long cursoId);

    List<ContenidoEducativo> findByActivo(Boolean activo);
}
