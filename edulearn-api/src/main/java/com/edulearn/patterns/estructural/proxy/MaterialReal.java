package com.edulearn.patterns.estructural.proxy;

import com.edulearn.model.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

/**
 * Implementación Real del Material
 *
 * Esta clase representa el objeto real que carga y maneja
 * el contenido pesado de los materiales (videos, PDFs, archivos).
 *
 * La carga del contenido es costosa en recursos, por eso
 * el Proxy controla cuándo se debe instanciar esta clase.
 *
 * Patrón de Diseño: Proxy (Estructural) - Objeto Real
 */
public class MaterialReal implements MaterialContenido {

    private static final Logger logger = LoggerFactory.getLogger(MaterialReal.class);

    private final Material material;
    private byte[] contenidoCargado;
    private boolean cargado = false;
    private LocalDateTime fechaUltimaCarga;

    /**
     * Constructor que recibe el material desde la base de datos
     * @param material Material con metadata
     */
    public MaterialReal(Material material) {
        this.material = material;
        logger.info("MaterialReal creado para material ID: {} - Tipo: {}",
                material.getId(), material.getTipoMaterial());
    }

    @Override
    public Material obtenerInformacionBasica() {
        return material;
    }

    @Override
    public byte[] cargarContenido() {
        if (!cargado) {
            logger.info("Cargando contenido pesado del material ID: {} - Tamaño: {} bytes",
                    material.getId(), material.getTamanoBytes());

            try {
                contenidoCargado = cargarContenidoDesdeAlmacenamiento();
                cargado = true;
                fechaUltimaCarga = LocalDateTime.now();

                logger.info("Contenido cargado exitosamente para material ID: {}", material.getId());
            } catch (IOException e) {
                logger.error("Error al cargar contenido del material ID: {}", material.getId(), e);
                throw new RuntimeException("No se pudo cargar el contenido del material", e);
            }
        } else {
            logger.debug("Contenido ya está cargado en memoria para material ID: {}", material.getId());
        }

        return contenidoCargado;
    }

    /**
     * Carga el contenido real desde el sistema de archivos o almacenamiento
     * @return Contenido en bytes
     * @throws IOException Si hay error al leer el archivo
     */
    private byte[] cargarContenidoDesdeAlmacenamiento() throws IOException {
        // Directorio de uploads
        String UPLOAD_DIR = "uploads";

        if (material.getArchivoPath() != null && !material.getArchivoPath().isEmpty()) {
            // Cargar desde archivo local en el directorio uploads
            Path path = Paths.get(UPLOAD_DIR, material.getArchivoPath());

            logger.info("Intentando cargar archivo desde: {}", path.toAbsolutePath());

            if (Files.exists(path)) {
                logger.info("✅ Archivo encontrado, cargando {} bytes", Files.size(path));
                return Files.readAllBytes(path);
            } else {
                logger.warn("❌ Archivo no encontrado en: {}", path.toAbsolutePath());
                throw new IOException("Archivo no encontrado: " + material.getArchivoPath());
            }
        } else if (material.getUrlRecurso() != null && !material.getUrlRecurso().isEmpty()) {
            // Si tiene URL pero no archivoPath, intentar extraer el nombre del archivo de la URL
            String url = material.getUrlRecurso();
            if (url.contains("/descargar/")) {
                String filename = url.substring(url.lastIndexOf("/") + 1);
                Path path = Paths.get(UPLOAD_DIR, filename);

                logger.info("Intentando cargar archivo desde URL: {}", path.toAbsolutePath());

                if (Files.exists(path)) {
                    logger.info("✅ Archivo encontrado vía URL, cargando {} bytes", Files.size(path));
                    return Files.readAllBytes(path);
                }
            }
        }

        // Si no hay archivo, lanzar excepción
        logger.error("❌ No se puede cargar contenido para material ID: {} - No hay archivo asociado", material.getId());
        throw new IOException("No hay archivo asociado al material");
    }

    @Override
    public String obtenerUrlRecurso() {
        if (material.getUrlRecurso() != null && !material.getUrlRecurso().isEmpty()) {
            return material.getUrlRecurso();
        }
        return null;
    }

    @Override
    public boolean estaContenidoCargado() {
        return cargado;
    }

    @Override
    public Long obtenerTamano() {
        return material.getTamanoBytes();
    }

    @Override
    public boolean verificarAcceso(Long usuarioId, String rol) {
        // MODO PERMISIVO: Por defecto damos acceso a todos
        // Solo denegamos en casos muy específicos

        logger.info("🔐 Verificando acceso - Usuario: {}, Rol: '{}', Material ID: {}, Estado: '{}'",
                    usuarioId, rol, material.getId(), material.getEstado());

        // Si el material está explícitamente inactivo o bloqueado, solo admin/profesor pueden acceder
        String estado = material.getEstado();
        if (estado != null && ("inactivo".equalsIgnoreCase(estado) || "bloqueado".equalsIgnoreCase(estado))) {
            if ("admin".equalsIgnoreCase(rol) || "profesor".equalsIgnoreCase(rol) || "ADMIN".equalsIgnoreCase(rol) || "PROFESOR".equalsIgnoreCase(rol)) {
                logger.info("✅ Acceso concedido (admin/profesor) a material inactivo ID: {}", material.getId());
                return true;
            } else {
                logger.warn("❌ Acceso denegado: material ID: {} está inactivo y usuario no es admin/profesor", material.getId());
                return false;
            }
        }

        // En cualquier otro caso (estado activo, null, o cualquier otro), dar acceso
        logger.info("✅ Acceso concedido - Usuario: {}, Rol: '{}', Material ID: {}", usuarioId, rol, material.getId());
        return true;
    }

    @Override
    public void registrarAcceso(Long usuarioId) {
        logger.info("Acceso registrado - Usuario ID: {} accedió al material ID: {} ({})",
                usuarioId, material.getId(), material.getTitulo());
        // Aquí se podría guardar el registro en una tabla de auditoría o métricas
    }

    /**
     * Obtiene la fecha de la última carga del contenido
     * @return Fecha de última carga
     */
    public LocalDateTime getFechaUltimaCarga() {
        return fechaUltimaCarga;
    }

    /**
     * Libera el contenido de memoria para ahorrar recursos
     */
    public void liberarMemoria() {
        if (cargado) {
            contenidoCargado = null;
            cargado = false;
            logger.info("Contenido liberado de memoria para material ID: {}", material.getId());
        }
    }
}
