package com.edulearn.service;

import com.edulearn.model.ConfiguracionSistema;
import com.edulearn.patterns.creational.singleton.ConfiguracionSistemaManager;
import com.edulearn.repository.ConfiguracionSistemaRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Servicio para gestionar configuraciones del sistema
 * Utiliza el patrón Singleton (ConfiguracionSistemaManager)
 */
@Service
public class ConfiguracionService {

    @Autowired
    private ConfiguracionSistemaRepository repository;

    private ConfiguracionSistemaManager manager;

    /**
     * Inicializar el singleton con el repositorio después de la inyección de dependencias
     */
    @PostConstruct
    public void init() {
        manager = ConfiguracionSistemaManager.getInstance();
        manager.setRepository(repository);
    }

    /**
     * Obtener todas las configuraciones
     */
    public Map<String, String> obtenerTodasLasConfiguraciones() {
        return manager.obtenerTodasLasConfiguraciones();
    }

    /**
     * Obtener configuración por clave
     */
    public String obtenerConfiguracion(String clave) {
        return manager.getConfiguracion(clave);
    }

    /**
     * Actualizar una configuración
     */
    public void actualizarConfiguracion(String clave, String valor) {
        manager.setConfiguracion(clave, valor);
    }

    /**
     * Actualizar múltiples configuraciones
     */
    public void actualizarConfiguraciones(Map<String, String> configuraciones) {
        manager.actualizarConfiguraciones(configuraciones);
    }

    /**
     * Recargar configuraciones desde BD
     */
    public void recargarConfiguraciones() {
        manager.recargarConfiguraciones();
    }

    /**
     * Obtener todas las configuraciones desde BD (sin caché)
     */
    public List<ConfiguracionSistema> obtenerConfiguracionesCompletas() {
        return repository.findAll();
    }

    /**
     * Crear nueva configuración
     */
    public ConfiguracionSistema crearConfiguracion(ConfiguracionSistema config) {
        ConfiguracionSistema saved = repository.save(config);
        manager.setConfiguracion(config.getClave(), config.getValor());
        return saved;
    }

    /**
     * Eliminar configuración
     */
    public void eliminarConfiguracion(String clave) {
        repository.findByClave(clave).ifPresent(config -> {
            repository.delete(config);
            manager.recargarConfiguraciones();
        });
    }

    /**
     * Obtener estadísticas del singleton
     */
    public Map<String, Object> obtenerEstadisticasSingleton() {
        return Map.of(
            "cantidadEnCache", manager.getCantidadConfiguraciones(),
            "cantidadEnBD", repository.count(),
            "instanciaActiva", manager != null,
            "patron", "Singleton"
        );
    }
}
