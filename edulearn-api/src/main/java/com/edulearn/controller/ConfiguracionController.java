package com.edulearn.controller;

import com.edulearn.model.ConfiguracionSistema;
import com.edulearn.service.ConfiguracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controlador REST para gestionar configuraciones del sistema
 * Demuestra el uso del patrón Singleton
 */
@RestController
@RequestMapping("/api/configuraciones")
@CrossOrigin(origins = "*")
public class ConfiguracionController {

    @Autowired
    private ConfiguracionService service;

    /**
     * Obtener todas las configuraciones (desde caché del Singleton)
     * GET /api/configuraciones
     */
    @GetMapping
    public ResponseEntity<Map<String, String>> obtenerTodas() {
        return ResponseEntity.ok(service.obtenerTodasLasConfiguraciones());
    }

    /**
     * Obtener todas las configuraciones completas (desde BD)
     * GET /api/configuraciones/completas
     */
    @GetMapping("/completas")
    public ResponseEntity<List<ConfiguracionSistema>> obtenerCompletas() {
        return ResponseEntity.ok(service.obtenerConfiguracionesCompletas());
    }

    /**
     * Obtener configuración por clave
     * GET /api/configuraciones/{clave}
     */
    @GetMapping("/{clave}")
    public ResponseEntity<Map<String, String>> obtenerPorClave(@PathVariable String clave) {
        String valor = service.obtenerConfiguracion(clave);
        return ResponseEntity.ok(Map.of(
            "clave", clave,
            "valor", valor
        ));
    }

    /**
     * Actualizar una configuración
     * PUT /api/configuraciones/{clave}
     */
    @PutMapping("/{clave}")
    public ResponseEntity<Map<String, String>> actualizar(
        @PathVariable String clave,
        @RequestBody Map<String, String> body
    ) {
        String valor = body.get("valor");
        service.actualizarConfiguracion(clave, valor);
        return ResponseEntity.ok(Map.of(
            "mensaje", "Configuración actualizada",
            "clave", clave,
            "valor", valor
        ));
    }

    /**
     * Actualizar múltiples configuraciones
     * PUT /api/configuraciones
     */
    @PutMapping
    public ResponseEntity<Map<String, String>> actualizarVarias(
        @RequestBody Map<String, String> configuraciones
    ) {
        service.actualizarConfiguraciones(configuraciones);
        return ResponseEntity.ok(Map.of(
            "mensaje", "Configuraciones actualizadas",
            "cantidad", String.valueOf(configuraciones.size())
        ));
    }

    /**
     * Crear nueva configuración
     * POST /api/configuraciones
     */
    @PostMapping
    public ResponseEntity<ConfiguracionSistema> crear(@RequestBody ConfiguracionSistema config) {
        ConfiguracionSistema saved = service.crearConfiguracion(config);
        return ResponseEntity.ok(saved);
    }

    /**
     * Eliminar configuración
     * DELETE /api/configuraciones/{clave}
     */
    @DeleteMapping("/{clave}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable String clave) {
        service.eliminarConfiguracion(clave);
        return ResponseEntity.ok(Map.of(
            "mensaje", "Configuración eliminada",
            "clave", clave
        ));
    }

    /**
     * Recargar configuraciones desde BD
     * POST /api/configuraciones/recargar
     */
    @PostMapping("/recargar")
    public ResponseEntity<Map<String, String>> recargar() {
        service.recargarConfiguraciones();
        return ResponseEntity.ok(Map.of(
            "mensaje", "Configuraciones recargadas desde BD"
        ));
    }

    /**
     * Obtener estadísticas del patrón Singleton
     * GET /api/configuraciones/estadisticas
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        return ResponseEntity.ok(service.obtenerEstadisticasSingleton());
    }

    /**
     * Demo del patrón Singleton
     * GET /api/configuraciones/demo
     */
    @GetMapping("/demo")
    public ResponseEntity<Map<String, Object>> demo() {
        return ResponseEntity.ok(Map.of(
            "patron", "Singleton",
            "proposito", "Garantizar una única instancia de ConfiguracionSistemaManager",
            "ventajas", List.of(
                "Control estricto sobre la instancia única",
                "Acceso global a configuraciones",
                "Caché en memoria para mejor rendimiento",
                "Thread-safe con Double-Checked Locking"
            ),
            "ejemploUso", Map.of(
                "obtenerNombreSistema", service.obtenerConfiguracion("nombre_sistema"),
                "obtenerVersion", service.obtenerConfiguracion("version"),
                "cantidadConfiguraciones", service.obtenerEstadisticasSingleton().get("cantidadEnCache")
            )
        ));
    }
}
