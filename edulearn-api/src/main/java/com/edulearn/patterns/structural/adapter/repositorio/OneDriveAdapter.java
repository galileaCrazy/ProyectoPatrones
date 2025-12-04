package com.edulearn.patterns.structural.adapter.repositorio;

import com.edulearn.patterns.structural.adapter.IntegracionExterna;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * PATRÓN ADAPTER - Adaptador para OneDrive
 * ========================================
 * Adapta la API específica de OneDrive (Microsoft) a nuestra interfaz común.
 *
 * En producción, este adaptador usaría la Microsoft Graph API
 * para gestionar carpetas y archivos en OneDrive.
 */
@Component
public class OneDriveAdapter implements IntegracionExterna {

    private static final Logger logger = LoggerFactory.getLogger(OneDriveAdapter.class);

    // Simulación de almacenamiento en memoria
    private final Map<String, Map<String, Object>> carpetas = new HashMap<>();

    @Override
    public String crearSesion(Map<String, Object> datos) {
        try {
            // Generar ID de carpeta
            String folderId = "onedrive-" + UUID.randomUUID().toString().substring(0, 16);

            // Extraer datos
            String nombre = (String) datos.getOrDefault("nombre", "Nueva Carpeta");
            String descripcion = (String) datos.getOrDefault("descripcion", "");
            Boolean esPublica = (Boolean) datos.getOrDefault("esPublica", false);
            String permisos = (String) datos.getOrDefault("permisos", "edit"); // read, write, edit

            // Crear link directo para abrir OneDrive
            // Este link redirige al usuario a su OneDrive donde puede crear carpetas
            String shareUrl = "https://onedrive.live.com/about/signin/";
            String folderUrl = shareUrl;

            // Guardar información de la carpeta
            Map<String, Object> carpetaInfo = new HashMap<>();
            carpetaInfo.put("id", folderId);
            carpetaInfo.put("nombre", nombre);
            carpetaInfo.put("descripcion", descripcion);
            carpetaInfo.put("url", folderUrl);
            carpetaInfo.put("shareUrl", shareUrl);
            carpetaInfo.put("esPublica", esPublica);
            carpetaInfo.put("permisos", permisos);
            carpetaInfo.put("proveedor", "ONEDRIVE");
            carpetaInfo.put("tipo", "CARPETA");
            carpetaInfo.put("cantidadArchivos", 0);
            carpetaInfo.put("tamanoBytes", 0L);
            carpetaInfo.put("fechaCreacion", LocalDateTime.now().toString());
            carpetaInfo.put("creadoPor", "Usuario OneDrive");

            carpetas.put(folderId, carpetaInfo);

            logger.info("✅ Carpeta de OneDrive creada: {} - {}", folderId, nombre);

            return shareUrl;
        } catch (Exception e) {
            logger.error("❌ Error al crear carpeta de OneDrive: {}", e.getMessage());
            throw new RuntimeException("Error al crear carpeta de OneDrive", e);
        }
    }

    @Override
    public Map<String, Object> obtenerInformacion(String identificador) {
        Map<String, Object> carpeta = carpetas.get(identificador);
        if (carpeta == null) {
            logger.warn("⚠️ Carpeta no encontrada: {}", identificador);
            return new HashMap<>();
        }
        return new HashMap<>(carpeta);
    }

    @Override
    public boolean actualizarSesion(String identificador, Map<String, Object> datos) {
        Map<String, Object> carpeta = carpetas.get(identificador);
        if (carpeta == null) {
            logger.warn("⚠️ No se puede actualizar, carpeta no encontrada: {}", identificador);
            return false;
        }

        // Actualizar campos permitidos
        if (datos.containsKey("nombre")) {
            carpeta.put("nombre", datos.get("nombre"));
        }
        if (datos.containsKey("descripcion")) {
            carpeta.put("descripcion", datos.get("descripcion"));
        }
        if (datos.containsKey("esPublica")) {
            carpeta.put("esPublica", datos.get("esPublica"));
        }
        if (datos.containsKey("permisos")) {
            carpeta.put("permisos", datos.get("permisos"));
        }
        if (datos.containsKey("cantidadArchivos")) {
            carpeta.put("cantidadArchivos", datos.get("cantidadArchivos"));
        }
        if (datos.containsKey("tamanoBytes")) {
            carpeta.put("tamanoBytes", datos.get("tamanoBytes"));
        }

        carpeta.put("fechaActualizacion", LocalDateTime.now().toString());

        logger.info("✅ Carpeta de OneDrive actualizada: {}", identificador);
        return true;
    }

    @Override
    public boolean eliminarSesion(String identificador) {
        Map<String, Object> removed = carpetas.remove(identificador);
        if (removed != null) {
            logger.info("✅ Carpeta de OneDrive eliminada: {}", identificador);
            return true;
        }
        logger.warn("⚠️ No se puede eliminar, carpeta no encontrada: {}", identificador);
        return false;
    }

    @Override
    public String getTipo() {
        return "REPOSITORIO";
    }

    @Override
    public String getProveedor() {
        return "ONEDRIVE";
    }

    @Override
    public boolean validarCredenciales(Map<String, String> credenciales) {
        // En producción, validaría el token de Microsoft Graph API
        String accessToken = credenciales.get("accessToken");

        if (accessToken == null || accessToken.isEmpty()) {
            logger.warn("⚠️ Credenciales inválidas para OneDrive");
            return false;
        }

        logger.info("✅ Credenciales de OneDrive validadas");
        return true;
    }

    /**
     * Simular subida de archivo
     */
    public boolean subirArchivo(String folderId, String nombreArchivo, long tamanoBytes) {
        Map<String, Object> carpeta = carpetas.get(folderId);
        if (carpeta == null) {
            return false;
        }

        Integer cantidadActual = (Integer) carpeta.get("cantidadArchivos");
        Long tamanoActual = (Long) carpeta.get("tamanoBytes");

        carpeta.put("cantidadArchivos", cantidadActual + 1);
        carpeta.put("tamanoBytes", tamanoActual + tamanoBytes);

        logger.info("📄 Archivo subido a OneDrive: {} en carpeta {}", nombreArchivo, folderId);
        return true;
    }

    /**
     * Obtener todas las carpetas (para debugging/testing)
     */
    public Map<String, Map<String, Object>> obtenerTodasLasCarpetas() {
        return new HashMap<>(carpetas);
    }
}
