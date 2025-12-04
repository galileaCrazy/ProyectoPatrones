package com.edulearn.patterns.structural.adapter.videoconferencia;

import com.edulearn.patterns.structural.adapter.IntegracionExterna;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * PATRÓN ADAPTER - Adaptador para Google Meet
 * ============================================
 * Adapta la API específica de Google Meet a nuestra interfaz común.
 *
 * En producción, este adaptador usaría la API real de Google Calendar
 * para crear reuniones de Meet. Por ahora, simula el comportamiento.
 */
@Component
public class GoogleMeetAdapter implements IntegracionExterna {

    private static final Logger logger = LoggerFactory.getLogger(GoogleMeetAdapter.class);

    // Simulación de almacenamiento en memoria
    private final Map<String, Map<String, Object>> sesiones = new HashMap<>();

    @Override
    public String crearSesion(Map<String, Object> datos) {
        try {
            // Generar ID único para la reunión
            String meetingId = "meet-" + UUID.randomUUID().toString().substring(0, 8);

            // Extraer datos
            String titulo = (String) datos.getOrDefault("titulo", "Reunión sin título");
            String descripcion = (String) datos.getOrDefault("descripcion", "");
            LocalDateTime fechaInicio = datos.containsKey("fechaInicio")
                ? LocalDateTime.parse(datos.get("fechaInicio").toString())
                : LocalDateTime.now();
            Integer duracionMinutos = (Integer) datos.getOrDefault("duracionMinutos", 60);

            // Crear link directo para nueva reunión de Google Meet
            // Este link redirige al usuario a crear una nueva reunión instantánea
            String meetUrl = "https://meet.google.com/new";

            // Guardar información de la sesión
            Map<String, Object> sesionInfo = new HashMap<>();
            sesionInfo.put("id", meetingId);
            sesionInfo.put("titulo", titulo);
            sesionInfo.put("descripcion", descripcion);
            sesionInfo.put("url", meetUrl);
            sesionInfo.put("fechaInicio", fechaInicio.toString());
            sesionInfo.put("duracionMinutos", duracionMinutos);
            sesionInfo.put("proveedor", "GOOGLE_MEET");
            sesionInfo.put("estado", "PROGRAMADA");
            sesionInfo.put("fechaCreacion", LocalDateTime.now().toString());

            sesiones.put(meetingId, sesionInfo);

            logger.info("✅ Reunión de Google Meet creada: {} - {}", meetingId, meetUrl);

            return meetUrl;
        } catch (Exception e) {
            logger.error("❌ Error al crear reunión de Google Meet: {}", e.getMessage());
            throw new RuntimeException("Error al crear reunión de Google Meet", e);
        }
    }

    @Override
    public Map<String, Object> obtenerInformacion(String identificador) {
        Map<String, Object> sesion = sesiones.get(identificador);
        if (sesion == null) {
            logger.warn("⚠️ Reunión no encontrada: {}", identificador);
            return new HashMap<>();
        }
        return new HashMap<>(sesion);
    }

    @Override
    public boolean actualizarSesion(String identificador, Map<String, Object> datos) {
        Map<String, Object> sesion = sesiones.get(identificador);
        if (sesion == null) {
            logger.warn("⚠️ No se puede actualizar, reunión no encontrada: {}", identificador);
            return false;
        }

        // Actualizar campos permitidos
        if (datos.containsKey("titulo")) {
            sesion.put("titulo", datos.get("titulo"));
        }
        if (datos.containsKey("descripcion")) {
            sesion.put("descripcion", datos.get("descripcion"));
        }
        if (datos.containsKey("fechaInicio")) {
            sesion.put("fechaInicio", datos.get("fechaInicio").toString());
        }
        if (datos.containsKey("duracionMinutos")) {
            sesion.put("duracionMinutos", datos.get("duracionMinutos"));
        }

        sesion.put("fechaActualizacion", LocalDateTime.now().toString());

        logger.info("✅ Reunión de Google Meet actualizada: {}", identificador);
        return true;
    }

    @Override
    public boolean eliminarSesion(String identificador) {
        Map<String, Object> removed = sesiones.remove(identificador);
        if (removed != null) {
            logger.info("✅ Reunión de Google Meet eliminada: {}", identificador);
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
        return "GOOGLE_MEET";
    }

    @Override
    public boolean validarCredenciales(Map<String, String> credenciales) {
        // En producción, validaría el token OAuth2 de Google
        String accessToken = credenciales.get("accessToken");

        if (accessToken == null || accessToken.isEmpty()) {
            logger.warn("⚠️ Credenciales inválidas para Google Meet");
            return false;
        }

        logger.info("✅ Credenciales de Google Meet validadas");
        return true;
    }

    /**
     * Generar código de reunión de Meet (formato abc-defg-hij)
     */
    private String generateMeetCode() {
        String chars = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            if (i > 0) code.append("-");
            for (int j = 0; j < (i == 1 ? 4 : 3); j++) {
                int index = (int) (Math.random() * chars.length());
                code.append(chars.charAt(index));
            }
        }

        return code.toString();
    }

    /**
     * Obtener todas las sesiones (para debugging/testing)
     */
    public Map<String, Map<String, Object>> obtenerTodasLasSesiones() {
        return new HashMap<>(sesiones);
    }
}
