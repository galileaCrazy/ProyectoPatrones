package com.edulearn.patterns.estructural.proxy;

import com.edulearn.model.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Proxy del Material
 *
 * Actúa como sustituto del MaterialReal, controlando el acceso
 * y aplicando lazy loading (carga diferida) del contenido pesado.
 *
 * El Proxy solo crea el MaterialReal cuando es absolutamente necesario,
 * ahorrando recursos y mejorando el rendimiento del sistema.
 *
 * Responsabilidades:
 * - Control de acceso (verificación de permisos)
 * - Lazy loading (carga diferida del contenido)
 * - Logging y auditoría de accesos
 * - Caché de objetos reales
 *
 * Patrón de Diseño: Proxy (Estructural)
 */
public class MaterialProxy implements MaterialContenido {

    private static final Logger logger = LoggerFactory.getLogger(MaterialProxy.class);

    private final Material material;
    private MaterialReal materialReal;
    private final Long usuarioId;
    private final String rolUsuario;

    /**
     * Constructor del Proxy
     * @param material Material con información básica de la base de datos
     * @param usuarioId ID del usuario que solicita el material
     * @param rolUsuario Rol del usuario (estudiante, profesor, admin)
     */
    public MaterialProxy(Material material, Long usuarioId, String rolUsuario) {
        this.material = material;
        this.usuarioId = usuarioId;
        this.rolUsuario = rolUsuario;

        logger.info("MaterialProxy creado para material ID: {} - Usuario: {} (Rol: {})",
                material.getId(), usuarioId, rolUsuario);
    }

    @Override
    public Material obtenerInformacionBasica() {
        // La información básica está disponible inmediatamente sin crear el objeto real
        logger.debug("Obteniendo información básica del material ID: {} (Proxy)", material.getId());
        return material;
    }

    @Override
    public byte[] cargarContenido() {
        // Verificar permisos antes de cargar contenido
        if (!verificarAcceso(usuarioId, rolUsuario)) {
            logger.warn("Acceso denegado al contenido del material ID: {} para usuario: {}",
                    material.getId(), usuarioId);
            throw new SecurityException("No tiene permisos para acceder a este material");
        }

        // Lazy Loading: solo crea el MaterialReal cuando se necesita el contenido
        if (materialReal == null) {
            logger.info("Creando MaterialReal (Lazy Loading) para material ID: {}", material.getId());
            materialReal = new MaterialReal(material);
        }

        // Registrar el acceso
        registrarAcceso(usuarioId);

        // Delegar al objeto real
        return materialReal.cargarContenido();
    }

    @Override
    public String obtenerUrlRecurso() {
        // La URL puede obtenerse sin cargar el contenido completo
        logger.debug("Obteniendo URL del recurso material ID: {} (Proxy)", material.getId());

        if (materialReal != null) {
            return materialReal.obtenerUrlRecurso();
        }

        return material.getUrlRecurso();
    }

    @Override
    public boolean estaContenidoCargado() {
        // Si el objeto real no existe, el contenido no está cargado
        if (materialReal == null) {
            return false;
        }
        return materialReal.estaContenidoCargado();
    }

    @Override
    public Long obtenerTamano() {
        // El tamaño está en los metadatos, no requiere cargar el contenido
        return material.getTamanoBytes();
    }

    @Override
    public boolean verificarAcceso(Long usuarioId, String rol) {
        logger.info("🔐 [PROXY] Verificando acceso - Usuario: {}, Rol: '{}', Material ID: {}, Estado: '{}'",
                usuarioId, rol, material.getId(), material.getEstado());

        // MODO PERMISIVO: Por defecto damos acceso a todos
        // Solo denegamos en casos muy específicos

        String estado = material.getEstado();

        // Si el material está explícitamente inactivo o bloqueado, solo admin/profesor pueden acceder
        if (estado != null && ("inactivo".equalsIgnoreCase(estado) || "bloqueado".equalsIgnoreCase(estado))) {
            // Administradores y profesores SÍ pueden acceder a materiales inactivos
            if ("admin".equalsIgnoreCase(rol) || "profesor".equalsIgnoreCase(rol) ||
                "ADMIN".equalsIgnoreCase(rol) || "PROFESOR".equalsIgnoreCase(rol)) {
                logger.info("✅ [PROXY] Acceso concedido (admin/profesor) a material inactivo ID: {}", material.getId());
                return true;
            } else {
                logger.warn("❌ [PROXY] Acceso denegado: material ID: {} está inactivo y usuario no es admin/profesor", material.getId());
                return false;
            }
        }

        // En cualquier otro caso (activo, null, u otro estado), dar acceso
        logger.info("✅ [PROXY] Acceso concedido - Usuario: {}, Rol: '{}', Material ID: {}", usuarioId, rol, material.getId());
        return true;
    }

    @Override
    public void registrarAcceso(Long usuarioId) {
        logger.info("PROXY - Registrando acceso: Usuario ID: {} accedió al material ID: {} ({})",
                usuarioId, material.getId(), material.getTitulo());

        // Auditoría a nivel de Proxy
        // Aquí se podría guardar en una tabla de logs de acceso
        // o enviar métricas a un servicio de analytics

        // Delegar al objeto real si existe
        if (materialReal != null) {
            materialReal.registrarAcceso(usuarioId);
        }
    }

    /**
     * Verifica si el objeto real ha sido creado
     * @return true si MaterialReal existe, false si aún no se ha creado (lazy)
     */
    public boolean tieneObjetoReal() {
        return materialReal != null;
    }

    /**
     * Obtiene el tipo de material
     * @return Tipo del material
     */
    public String getTipoMaterial() {
        return material.getTipoMaterial();
    }

    /**
     * Verifica si el material es pesado (requiere proxy)
     * @return true si es video, PDF o archivo grande
     */
    public boolean esMaterialPesado() {
        String tipo = material.getTipoMaterial();
        Long tamano = material.getTamanoBytes();

        // Considerar pesado si:
        // 1. Es video o PDF
        // 2. Tamaño mayor a 1MB
        boolean esTipoPesado = tipo != null && (
                tipo.equalsIgnoreCase("VIDEO") ||
                tipo.equalsIgnoreCase("PDF") ||
                tipo.equalsIgnoreCase("DOCUMENTO")
        );

        boolean esTamanoPesado = tamano != null && tamano > 1_048_576; // > 1MB

        return esTipoPesado || esTamanoPesado;
    }

    /**
     * Libera el objeto real de memoria si existe
     */
    public void liberarMemoria() {
        if (materialReal != null) {
            logger.info("Liberando MaterialReal desde Proxy para material ID: {}", material.getId());
            materialReal.liberarMemoria();
            materialReal = null;
        }
    }
}
