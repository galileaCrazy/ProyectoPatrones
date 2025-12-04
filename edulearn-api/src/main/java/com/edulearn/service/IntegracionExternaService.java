package com.edulearn.service;

import com.edulearn.model.IntegracionCurso;
import com.edulearn.patterns.structural.adapter.IntegracionExterna;
import com.edulearn.patterns.structural.adapter.videoconferencia.GoogleMeetAdapter;
import com.edulearn.patterns.structural.adapter.videoconferencia.ZoomAdapter;
import com.edulearn.patterns.structural.adapter.repositorio.GoogleDriveAdapter;
import com.edulearn.patterns.structural.adapter.repositorio.OneDriveAdapter;
import com.edulearn.repository.IntegracionCursoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio que coordina las integraciones externas
 * Usa el patrón Adapter para trabajar con diferentes proveedores
 * de forma uniforme
 */
@Service
public class IntegracionExternaService {

    private static final Logger logger = LoggerFactory.getLogger(IntegracionExternaService.class);

    @Autowired
    private IntegracionCursoRepository repository;

    @Autowired
    private GoogleMeetAdapter googleMeetAdapter;

    @Autowired
    private ZoomAdapter zoomAdapter;

    @Autowired
    private GoogleDriveAdapter googleDriveAdapter;

    @Autowired
    private OneDriveAdapter oneDriveAdapter;

    /**
     * Obtener el adaptador correcto según el proveedor
     */
    private IntegracionExterna getAdapter(String proveedor) {
        return switch (proveedor.toUpperCase()) {
            case "GOOGLE_MEET" -> googleMeetAdapter;
            case "ZOOM" -> zoomAdapter;
            case "GOOGLE_DRIVE" -> googleDriveAdapter;
            case "ONEDRIVE" -> oneDriveAdapter;
            default -> throw new IllegalArgumentException("Proveedor no soportado: " + proveedor);
        };
    }

    /**
     * Crear una nueva integración
     */
    public IntegracionCurso crearIntegracion(
        Integer cursoId,
        Integer profesorId,
        String proveedor,
        Map<String, Object> datos
    ) {
        try {
            // Obtener el adaptador correspondiente
            IntegracionExterna adapter = getAdapter(proveedor);

            // Crear sesión/recurso en el sistema externo
            String urlRecurso = adapter.crearSesion(datos);

            // Crear registro en nuestra BD
            IntegracionCurso integracion = new IntegracionCurso();
            integracion.setCursoId(cursoId);
            integracion.setProfesorId(profesorId);
            integracion.setTipo(adapter.getTipo());
            integracion.setProveedor(adapter.getProveedor());
            integracion.setNombre((String) datos.getOrDefault("nombre", "Sin nombre"));
            integracion.setDescripcion((String) datos.getOrDefault("descripcion", ""));
            integracion.setUrlRecurso(urlRecurso);
            integracion.setEstado("ACTIVA");

            // Guardar en BD
            IntegracionCurso guardada = repository.save(integracion);

            logger.info("✅ Integración creada: {} - {} para curso {}",
                guardada.getId(), proveedor, cursoId);

            return guardada;
        } catch (Exception e) {
            logger.error("❌ Error al crear integración: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear integración: " + e.getMessage());
        }
    }

    /**
     * Obtener todas las integraciones de un curso
     */
    public List<IntegracionCurso> obtenerIntegracionesPorCurso(Integer cursoId) {
        return repository.findByCursoId(cursoId);
    }

    /**
     * Obtener integraciones activas de un curso
     */
    public List<IntegracionCurso> obtenerIntegracionesActivasPorCurso(Integer cursoId) {
        return repository.findByCursoIdAndEstado(cursoId, "ACTIVA");
    }

    /**
     * Obtener integraciones por tipo
     */
    public List<IntegracionCurso> obtenerIntegracionesPorTipo(Integer cursoId, String tipo) {
        return repository.findByCursoIdAndTipo(cursoId, tipo);
    }

    /**
     * Obtener información detallada de una integración
     */
    public Map<String, Object> obtenerDetalleIntegracion(Long integracionId) {
        IntegracionCurso integracion = repository.findById(integracionId)
            .orElseThrow(() -> new RuntimeException("Integración no encontrada"));

        Map<String, Object> detalle = new HashMap<>();
        detalle.put("id", integracion.getId());
        detalle.put("cursoId", integracion.getCursoId());
        detalle.put("tipo", integracion.getTipo());
        detalle.put("proveedor", integracion.getProveedor());
        detalle.put("nombre", integracion.getNombre());
        detalle.put("descripcion", integracion.getDescripcion());
        detalle.put("urlRecurso", integracion.getUrlRecurso());
        detalle.put("estado", integracion.getEstado());
        detalle.put("vecesUsado", integracion.getVecesUsado());
        detalle.put("fechaCreacion", integracion.getFechaCreacion());
        detalle.put("fechaUso", integracion.getFechaUso());

        return detalle;
    }

    /**
     * Actualizar una integración
     */
    public IntegracionCurso actualizarIntegracion(Long integracionId, Map<String, Object> datos) {
        IntegracionCurso integracion = repository.findById(integracionId)
            .orElseThrow(() -> new RuntimeException("Integración no encontrada"));

        if (datos.containsKey("nombre")) {
            integracion.setNombre((String) datos.get("nombre"));
        }
        if (datos.containsKey("descripcion")) {
            integracion.setDescripcion((String) datos.get("descripcion"));
        }
        if (datos.containsKey("estado")) {
            integracion.setEstado((String) datos.get("estado"));
        }

        integracion.setFechaActualizacion(LocalDateTime.now());

        return repository.save(integracion);
    }

    /**
     * Eliminar una integración
     */
    public boolean eliminarIntegracion(Long integracionId) {
        try {
            IntegracionCurso integracion = repository.findById(integracionId)
                .orElseThrow(() -> new RuntimeException("Integración no encontrada"));

            // Marcar como eliminada en lugar de borrar físicamente
            integracion.setEstado("ELIMINADA");
            integracion.setFechaActualizacion(LocalDateTime.now());
            repository.save(integracion);

            logger.info("✅ Integración eliminada: {}", integracionId);
            return true;
        } catch (Exception e) {
            logger.error("❌ Error al eliminar integración: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Registrar uso de una integración
     */
    public void registrarUso(Long integracionId) {
        IntegracionCurso integracion = repository.findById(integracionId).orElse(null);
        if (integracion != null) {
            integracion.registrarUso();
            repository.save(integracion);
            logger.info("📊 Uso registrado para integración: {}", integracionId);
        }
    }

    /**
     * Obtener estadísticas de integraciones
     */
    public Map<String, Object> obtenerEstadisticas(Integer cursoId) {
        List<IntegracionCurso> integraciones = repository.findByCursoId(cursoId);

        Map<String, Object> stats = new HashMap<>();
        stats.put("total", integraciones.size());
        stats.put("activas", integraciones.stream().filter(IntegracionCurso::estaActiva).count());
        stats.put("videoconferencias", integraciones.stream()
            .filter(i -> "VIDEOCONFERENCIA".equals(i.getTipo())).count());
        stats.put("repositorios", integraciones.stream()
            .filter(i -> "REPOSITORIO".equals(i.getTipo())).count());
        stats.put("usosTotal", integraciones.stream()
            .mapToInt(IntegracionCurso::getVecesUsado).sum());

        return stats;
    }

    /**
     * Validar credenciales de un proveedor
     */
    public boolean validarCredenciales(String proveedor, Map<String, String> credenciales) {
        try {
            IntegracionExterna adapter = getAdapter(proveedor);
            return adapter.validarCredenciales(credenciales);
        } catch (Exception e) {
            logger.error("❌ Error al validar credenciales: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Obtener proveedores disponibles
     */
    public Map<String, Object> obtenerProveedoresDisponibles() {
        Map<String, Object> proveedores = new HashMap<>();

        Map<String, String> videoconferencias = new HashMap<>();
        videoconferencias.put("GOOGLE_MEET", "Google Meet");
        videoconferencias.put("ZOOM", "Zoom");

        Map<String, String> repositorios = new HashMap<>();
        repositorios.put("GOOGLE_DRIVE", "Google Drive");
        repositorios.put("ONEDRIVE", "OneDrive");

        proveedores.put("videoconferencias", videoconferencias);
        proveedores.put("repositorios", repositorios);

        return proveedores;
    }
}
