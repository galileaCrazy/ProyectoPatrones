package com.edulearn.controller;

import com.edulearn.model.IntegracionExterna;
import com.edulearn.patterns.structural.adapter.ISistemaVideoconferencia;
import com.edulearn.patterns.structural.adapter.VideoconferenciaAdapterFactory;
import com.edulearn.repository.IntegracionExternaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/integraciones")
public class IntegracionController {

    @Autowired
    private IntegracionExternaRepository integracionRepository;

    /**
     * GET /api/integraciones
     * Obtener todas las integraciones
     */
    @GetMapping
    public List<IntegracionExterna> getAll() {
        return integracionRepository.findAll();
    }

    /**
     * GET /api/integraciones/{id}
     * Obtener integración por ID
     */
    @GetMapping("/{id}")
    public IntegracionExterna getById(@PathVariable Integer id) {
        return integracionRepository.findById(id).orElse(null);
    }

    /**
     * GET /api/integraciones/tipo/{tipo}
     * Obtener integraciones por tipo de sistema
     */
    @GetMapping("/tipo/{tipo}")
    public List<IntegracionExterna> getByTipo(@PathVariable String tipo) {
        return integracionRepository.findByTipoSistema(tipo);
    }

    /**
     * GET /api/integraciones/curso/{cursoId}
     * Obtener integraciones de un curso específico
     */
    @GetMapping("/curso/{cursoId}")
    public List<IntegracionExterna> getByCurso(@PathVariable Integer cursoId) {
        return integracionRepository.findByCursoId(cursoId);
    }

    /**
     * POST /api/integraciones
     * Crear nueva integración
     */
    @PostMapping
    public IntegracionExterna crear(@RequestBody IntegracionExterna integracion) {
        return integracionRepository.save(integracion);
    }

    /**
     * PUT /api/integraciones/{id}
     * Actualizar integración existente
     */
    @PutMapping("/{id}")
    public IntegracionExterna actualizar(@PathVariable Integer id, @RequestBody IntegracionExterna integracionActualizada) {
        IntegracionExterna existente = integracionRepository.findById(id).orElse(null);
        if (existente == null) {
            return null;
        }

        if (integracionActualizada.getNombreConfiguracion() != null) {
            existente.setNombreConfiguracion(integracionActualizada.getNombreConfiguracion());
        }
        if (integracionActualizada.getApiKey() != null) {
            existente.setApiKey(integracionActualizada.getApiKey());
        }
        if (integracionActualizada.getApiSecret() != null) {
            existente.setApiSecret(integracionActualizada.getApiSecret());
        }
        if (integracionActualizada.getEstado() != null) {
            existente.setEstado(integracionActualizada.getEstado());
        }

        return integracionRepository.save(existente);
    }

    /**
     * DELETE /api/integraciones/{id}
     * Eliminar integración
     */
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Integer id) {
        integracionRepository.deleteById(id);
    }

    // ========== ENDPOINTS CON PATRÓN ADAPTER ==========

    /**
     * POST /api/integraciones/{id}/crear-reunion
     * Crear reunión usando el patrón Adapter
     */
    @PostMapping("/{id}/crear-reunion")
    public Map<String, Object> crearReunion(
        @PathVariable Integer id,
        @RequestBody Map<String, Object> params
    ) {
        IntegracionExterna integracion = integracionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Integración no encontrada con ID: " + id));

        // Crear adapter usando el patrón Adapter
        ISistemaVideoconferencia sistema = VideoconferenciaAdapterFactory.crearAdapter(integracion);

        // Usar la interfaz común independientemente del sistema
        String nombreSala = (String) params.get("nombreSala");
        Integer duracion = (Integer) params.getOrDefault("duracion", 60);

        String urlReunion = sistema.crearReunion(nombreSala, duracion);

        // Actualizar integración con la sala creada
        integracion.setSalaReunion(urlReunion);
        integracion.setUltimaSincronizacion(LocalDateTime.now());
        integracionRepository.save(integracion);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("sistema", integracion.getTipoSistema());
        response.put("urlReunion", urlReunion);
        response.put("nombreSala", nombreSala);
        response.put("duracion", duracion);
        response.put("infoSistema", sistema.obtenerInfoSistema());

        return response;
    }

    /**
     * POST /api/integraciones/{id}/verificar
     * Verificar conexión con el sistema externo
     */
    @PostMapping("/{id}/verificar")
    public Map<String, Object> verificarConexion(@PathVariable Integer id) {
        IntegracionExterna integracion = integracionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Integración no encontrada con ID: " + id));

        ISistemaVideoconferencia sistema = VideoconferenciaAdapterFactory.crearAdapter(integracion);

        boolean conectado = sistema.verificarConexion();
        String info = sistema.obtenerInfoSistema();

        Map<String, Object> response = new HashMap<>();
        response.put("integracionId", id);
        response.put("tipoSistema", integracion.getTipoSistema());
        response.put("conectado", conectado);
        response.put("infoSistema", info);
        response.put("estado", integracion.getEstado());

        return response;
    }

    /**
     * POST /api/integraciones/prueba-adapter
     * Demostración del patrón Adapter con múltiples sistemas
     */
    @PostMapping("/prueba-adapter")
    public Map<String, Object> pruebaAdapter(@RequestBody Map<String, String> params) {
        String tipoSistema = params.get("tipoSistema");
        String nombreSala = params.getOrDefault("nombreSala", "Clase de Prueba");

        // Crear adapter dinámicamente
        ISistemaVideoconferencia sistema = VideoconferenciaAdapterFactory.crearAdapter(
            tipoSistema,
            "demo_api_key",
            "demo_secret"
        );

        String urlReunion = sistema.crearReunion(nombreSala, 30);

        Map<String, Object> response = new HashMap<>();
        response.put("patron", "Adapter");
        response.put("tipoSistema", tipoSistema);
        response.put("urlReunion", urlReunion);
        response.put("infoSistema", sistema.obtenerInfoSistema());
        response.put("mensaje", "Reunión creada usando patrón Adapter para integrar sistema externo");

        return response;
    }
}
