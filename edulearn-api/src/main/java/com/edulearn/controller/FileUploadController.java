package com.edulearn.controller;

import com.edulearn.model.Material;
import com.edulearn.repository.MaterialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Controlador para gestión de subida y descarga de archivos
 * Maneja videos, PDFs, documentos, imágenes, etc.
 */
@RestController
@RequestMapping("/api/archivos")
@CrossOrigin(origins = "*")
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    // Directorio donde se almacenarán los archivos
    private static final String UPLOAD_DIR = "uploads";

    @Autowired
    private MaterialRepository materialRepository;

    // Tamaño máximo de archivo: 500MB
    private static final long MAX_FILE_SIZE = 500 * 1024 * 1024;

    /**
     * Subir un archivo (video, PDF, documento, etc.)
     *
     * POST /api/archivos/subir
     *
     * @param file Archivo a subir
     * @param materialId ID del material al que pertenece (opcional)
     * @param tipoMaterial Tipo de material (VIDEO, PDF, DOCUMENTO, etc.)
     * @return Información del archivo subido
     */
    @PostMapping(value = "/subir", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> subirArchivo(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "materialId", required = false) Long materialId,
            @RequestParam(value = "tipoMaterial", required = false) String tipoMaterial) {

        logger.info("📤 Subiendo archivo: {} (Tamaño: {} bytes)", file.getOriginalFilename(), file.getSize());

        Map<String, Object> response = new HashMap<>();

        try {
            // Validaciones
            if (file.isEmpty()) {
                response.put("error", "El archivo está vacío");
                return ResponseEntity.badRequest().body(response);
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                response.put("error", "El archivo excede el tamaño máximo permitido (500MB)");
                return ResponseEntity.badRequest().body(response);
            }

            // Crear directorio de uploads si no existe
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                logger.info("📁 Directorio de uploads creado: {}", uploadPath.toAbsolutePath());
            }

            // Generar nombre único para el archivo
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
            String uniqueFilename = UUID.randomUUID().toString() + extension;

            // Guardar el archivo
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            logger.info("✅ Archivo guardado exitosamente: {}", filePath.toAbsolutePath());

            // Si se proporcionó un materialId, actualizar el material en la BD
            if (materialId != null) {
                materialRepository.findById(materialId).ifPresent(material -> {
                    material.setArchivoPath(uniqueFilename);
                    material.setUrlRecurso("/api/archivos/descargar/" + uniqueFilename);
                    material.setTamanoBytes(file.getSize());

                    // Determinar tipo si no se especificó
                    if (tipoMaterial == null || tipoMaterial.isEmpty()) {
                        material.setTipoMaterial(determinarTipoMaterial(extension));
                    }

                    materialRepository.save(material);
                    logger.info("📝 Material ID {} actualizado con archivo", materialId);
                });
            }

            // Construir respuesta
            response.put("success", true);
            response.put("filename", uniqueFilename);
            response.put("originalFilename", originalFilename);
            response.put("size", file.getSize());
            response.put("url", "/api/archivos/descargar/" + uniqueFilename);
            response.put("urlRecurso", "/api/archivos/descargar/" + uniqueFilename);
            response.put("type", file.getContentType());
            response.put("message", "Archivo subido exitosamente");

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            logger.error("❌ Error al guardar el archivo", e);
            response.put("error", "Error al guardar el archivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Descargar/Visualizar un archivo
     *
     * GET /api/archivos/descargar/{filename}
     *
     * @param filename Nombre del archivo
     * @param inline Si es true, muestra el archivo en el navegador. Si es false, lo descarga.
     * @return Archivo
     */
    @GetMapping("/descargar/{filename}")
    public ResponseEntity<Resource> descargarArchivo(
            @PathVariable String filename,
            @RequestParam(value = "inline", defaultValue = "true") boolean inline) {

        logger.info("📥 Solicitando archivo: {} (inline: {})", filename, inline);

        try {
            // Obtener el archivo
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
            File file = filePath.toFile();

            if (!file.exists()) {
                logger.warn("⚠️ Archivo no encontrado: {}", filename);
                return ResponseEntity.notFound().build();
            }

            // Crear recurso
            Resource resource = new FileSystemResource(file);

            // Determinar content type
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = determinarContentTypePorExtension(filename);
            }

            // Headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(file.length());

            if (inline) {
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"");
            } else {
                headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
            }

            // Agregar cabeceras para soporte de streaming de video
            headers.set("Accept-Ranges", "bytes");

            logger.info("✅ Sirviendo archivo: {} ({} bytes, type: {})", filename, file.length(), contentType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (IOException e) {
            logger.error("❌ Error al leer el archivo: {}", filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Eliminar un archivo
     *
     * DELETE /api/archivos/{filename}
     *
     * @param filename Nombre del archivo a eliminar
     * @return Confirmación
     */
    @DeleteMapping("/{filename}")
    public ResponseEntity<Map<String, String>> eliminarArchivo(@PathVariable String filename) {
        logger.info("🗑️ Eliminando archivo: {}", filename);

        Map<String, String> response = new HashMap<>();

        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
            File file = filePath.toFile();

            if (!file.exists()) {
                response.put("error", "Archivo no encontrado");
                return ResponseEntity.notFound().build();
            }

            if (file.delete()) {
                logger.info("✅ Archivo eliminado: {}", filename);
                response.put("message", "Archivo eliminado exitosamente");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "No se pudo eliminar el archivo");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (Exception e) {
            logger.error("❌ Error al eliminar archivo: {}", filename, e);
            response.put("error", "Error al eliminar el archivo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Obtener información de un archivo
     *
     * GET /api/archivos/info/{filename}
     *
     * @param filename Nombre del archivo
     * @return Información del archivo
     */
    @GetMapping("/info/{filename}")
    public ResponseEntity<Map<String, Object>> obtenerInfoArchivo(@PathVariable String filename) {
        logger.info("ℹ️ Obteniendo información del archivo: {}", filename);

        try {
            Path filePath = Paths.get(UPLOAD_DIR).resolve(filename).normalize();
            File file = filePath.toFile();

            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            Map<String, Object> info = new HashMap<>();
            info.put("filename", filename);
            info.put("size", file.length());
            info.put("exists", true);
            info.put("url", "/api/archivos/descargar/" + filename);
            info.put("lastModified", file.lastModified());

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = determinarContentTypePorExtension(filename);
            }
            info.put("contentType", contentType);

            return ResponseEntity.ok(info);

        } catch (IOException e) {
            logger.error("❌ Error al obtener información del archivo: {}", filename, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Determina el tipo de material basándose en la extensión del archivo
     */
    private String determinarTipoMaterial(String extension) {
        String ext = extension.toLowerCase();

        if (ext.matches("\\.(mp4|avi|mov|wmv|flv|webm|mkv)")) {
            return "VIDEO";
        } else if (ext.equals(".pdf")) {
            return "PDF";
        } else if (ext.matches("\\.(doc|docx|odt|txt)")) {
            return "DOCUMENTO";
        } else if (ext.matches("\\.(jpg|jpeg|png|gif|bmp|svg|webp)")) {
            return "IMAGE";
        } else if (ext.matches("\\.(mp3|wav|ogg|m4a|aac)")) {
            return "AUDIO";
        } else if (ext.matches("\\.(ppt|pptx)")) {
            return "PRESENTACION";
        } else {
            return "OTHER";
        }
    }

    /**
     * Determina el Content-Type basándose en la extensión del archivo
     */
    private String determinarContentTypePorExtension(String filename) {
        String extension = filename.toLowerCase();

        if (extension.endsWith(".mp4")) return "video/mp4";
        if (extension.endsWith(".webm")) return "video/webm";
        if (extension.endsWith(".avi")) return "video/x-msvideo";
        if (extension.endsWith(".mov")) return "video/quicktime";
        if (extension.endsWith(".pdf")) return "application/pdf";
        if (extension.endsWith(".doc") || extension.endsWith(".docx")) {
            return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
        }
        if (extension.endsWith(".jpg") || extension.endsWith(".jpeg")) return "image/jpeg";
        if (extension.endsWith(".png")) return "image/png";
        if (extension.endsWith(".gif")) return "image/gif";
        if (extension.endsWith(".mp3")) return "audio/mpeg";
        if (extension.endsWith(".wav")) return "audio/wav";

        return "application/octet-stream";
    }
}
