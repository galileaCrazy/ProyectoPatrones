package com.edulearn.repository;

import com.edulearn.model.ConfiguracionSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para gestionar configuraciones del sistema
 */
@Repository
public interface ConfiguracionSistemaRepository extends JpaRepository<ConfiguracionSistema, Long> {

    /**
     * Buscar configuración por clave
     */
    Optional<ConfiguracionSistema> findByClave(String clave);

    /**
     * Verificar si existe una configuración por clave
     */
    boolean existsByClave(String clave);
}
