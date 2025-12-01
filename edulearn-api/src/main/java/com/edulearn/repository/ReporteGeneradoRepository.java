package com.edulearn.repository;

import com.edulearn.model.ReporteGenerado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReporteGeneradoRepository extends JpaRepository<ReporteGenerado, Integer> {
    List<ReporteGenerado> findByTipoReporte(String tipoReporte);
    List<ReporteGenerado> findByFormato(String formato);
    List<ReporteGenerado> findByUsuarioId(Integer usuarioId);
    List<ReporteGenerado> findByEstado(String estado);
}
