package com.edulearn.patterns.structural.adapter.videoconferencia;

import com.edulearn.patterns.structural.adapter.IntegracionExterna;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * PATRÓN ADAPTER - Adaptador para Zoom
 * ====================================
 * Adapta la API específica de Zoom a nuestra interfaz común.
 *
 * En producción, este adaptador usaría la API real de Zoom
 * para crear y gestionar reuniones. Por ahora, simula el comportamiento.
 */
@Component
public class ZoomAdapter implements IntegracionExterna {

    private static final Logger logger = LoggerFactory.getLogger(ZoomAdapter.class);

    // Simulación de almacenamiento en memoria
    private final Map<String, Map<String, Object>> reuniones = new HashMap<>();
    private final Random random = new Random();

    @Override
    public String crearSesion(Map<String, Object> datos) {
        try {
            // Generar Meeting ID de Zoom (formato: 123 4567 8901)
            long meetingId = 10000000000L + (long) (random.nextDouble() * 90000000000L);
            String meetingIdStr = "zoom-" + meetingId;

            // Extraer datos
            String titulo = (String) datos.getOrDefault("titulo", "Reunión Zoom");
            String descripcion = (String) datos.getOrDefault("descripcion", "");
            LocalDateTime fechaInicio = datos.containsKey("fechaInicio")
                ? LocalDateTime.parse(datos.get("fechaInicio").toString())
                : LocalDateTime.now();
            Integer duracionMinutos = (Integer) datos.getOrDefault("duracionMinutos", 60);
            Boolean requierePassword = (Boolean) datos.getOrDefault("requierePassword", true);

            // Crear link directo para programar reunión en Zoom
            // Este link redirige al usuario a la página de programar reunión
            String joinUrl = "https://zoom.us/meeting/schedule";
            String startUrl = "https://zoom.us/meeting/schedule";

            // Guardar información de la reunión
            Map<String, Object> reunionInfo = new HashMap<>();
            reunionInfo.put("id", meetingIdStr);
            reunionInfo.put("meetingId", meetingId);
            reunionInfo.put("titulo", titulo);
            reunionInfo.put("descripcion", descripcion);
            reunionInfo.put("joinUrl", joinUrl);
            reunionInfo.put("startUrl", startUrl);
            reunionInfo.put("password", null);
            reunionInfo.put("fechaInicio", fechaInicio.toString());
            reunionInfo.put("duracionMinutos", duracionMinutos);
            reunionInfo.put("proveedor", "ZOOM");
            reunionInfo.put("estado", "PROGRAMADA");
            reunionInfo.put("fechaCreacion", LocalDateTime.now().toString());
            reunionInfo.put("tipoReunion", "scheduled");

            reuniones.put(meetingIdStr, reunionInfo);

            logger.info("✅ Reunión de Zoom creada: {} - {}", meetingIdStr, joinUrl);

            return joinUrl;
        } catch (Exception e) {
            logger.error("❌ Error al crear reunión de Zoom: {}", e.getMessage());
            throw new RuntimeException("Error al crear reunión de Zoom", e);
        }
    }

    @Override
    public Map<String, Object> obtenerInformacion(String identificador) {
        Map<String, Object> reunion = reuniones.get(identificador);
        if (reunion == null) {
            logger.warn("⚠️ Reunión de Zoom no encontrada: {}", identificador);
            return new HashMap<>();
        }
        return new HashMap<>(reunion);
    }

    @Override
    public boolean actualizarSesion(String identificador, Map<String, Object> datos) {
        Map<String, Object> reunion = reuniones.get(identificador);
        if (reunion == null) {
            logger.warn("⚠️ No se puede actualizar, reunión no encontrada: {}", identificador);
            return false;
        }

        // Actualizar campos permitidos
        if (datos.containsKey("titulo")) {
            reunion.put("titulo", datos.get("titulo"));
        }
        if (datos.containsKey("descripcion")) {
            reunion.put("descripcion", datos.get("descripcion"));
        }
        if (datos.containsKey("fechaInicio")) {
            reunion.put("fechaInicio", datos.get("fechaInicio").toString());
        }
        if (datos.containsKey("duracionMinutos")) {
            reunion.put("duracionMinutos", datos.get("duracionMinutos"));
        }
        if (datos.containsKey("estado")) {
            reunion.put("estado", datos.get("estado"));
        }

        reunion.put("fechaActualizacion", LocalDateTime.now().toString());

        logger.info("✅ Reunión de Zoom actualizada: {}", identificador);
        return true;
    }

    @Override
    public boolean eliminarSesion(String identificador) {
        Map<String, Object> removed = reuniones.remove(identificador);
        if (removed != null) {
            logger.info("✅ Reunión de Zoom eliminada: {}", identificador);
            return true;
        }
        logger.warn("⚠️ No se puede eliminar, reunión no encontrada: {}", identificador);
        return false;
    }

    @Override
    public String getTipo() {
        return "VIDEOCONFERENCIA";
    }

    @Override
    public String getProveedor() {
        return "ZOOM";
    }

    @Override
    public boolean validarCredenciales(Map<String, String> credenciales) {
        // En producción, validaría el API Key y Secret de Zoom
        String apiKey = credenciales.get("apiKey");
        String apiSecret = credenciales.get("apiSecret");

        if (apiKey == null || apiKey.isEmpty() || apiSecret == null || apiSecret.isEmpty()) {
            logger.warn("⚠️ Credenciales inválidas para Zoom");
            return false;
        }

        logger.info("✅ Credenciales de Zoom validadas");
        return true;
    }

    /**
     * Generar password aleatorio para reunión de Zoom
     */
    private String generatePassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    /**
     * Obtener todas las reuniones (para debugging/testing)
     */
    public Map<String, Map<String, Object>> obtenerTodasLasReuniones() {
        return new HashMap<>(reuniones);
    }
}
