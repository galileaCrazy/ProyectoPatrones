package com.edulearn.patterns.estructural.proxy;

import com.edulearn.model.Material;
import com.edulearn.repository.MaterialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servicio de Gestión del Patrón Proxy para Materiales
 *
 * Este servicio actúa como fábrica y gestor de proxies de materiales.
 *
 * Responsabilidades:
 * - Crear instancias de MaterialProxy
 * - Gestionar caché de proxies
 * - Proporcionar materiales con lazy loading
 * - Liberar recursos cuando sea necesario
 *
 * Patrón de Diseño: Proxy (Estructural)
 */
@Service
public class MaterialProxyService {

    private static final Logger logger = LoggerFactory.getLogger(MaterialProxyService.class);

    @Autowired
    private MaterialRepository materialRepository;

    // Caché de proxies por usuario y material
    private final Map<String, MaterialProxy> proxyCache = new ConcurrentHashMap<>();

    /**
     * Obtiene un material a través de un Proxy (lazy loading)
     *
     * @param materialId ID del material
     * @param usuarioId ID del usuario que solicita el material
     * @param rolUsuario Rol del usuario (estudiante, profesor, admin)
     * @return MaterialProxy configurado para el usuario
     */
    public MaterialProxy obtenerMaterialConProxy(Long materialId, Long usuarioId, String rolUsuario) {
        logger.info("Solicitando material ID: {} para usuario: {} (rol: {})",
                materialId, usuarioId, rolUsuario);

        // Clave única para caché: usuario + material
        String cacheKey = generarCacheKey(usuarioId, materialId);

        // Verificar si ya existe en caché
        if (proxyCache.containsKey(cacheKey)) {
            logger.debug("Material ID: {} encontrado en caché para usuario: {}", materialId, usuarioId);
            return proxyCache.get(cacheKey);
        }

        // Buscar material en la base de datos
        Optional<Material> materialOpt = materialRepository.findById(materialId);

        if (materialOpt.isEmpty()) {
            logger.error("Material ID: {} no encontrado en la base de datos", materialId);
            throw new IllegalArgumentException("Material no encontrado");
        }

        Material material = materialOpt.get();

        // Crear el Proxy
        MaterialProxy proxy = new MaterialProxy(material, usuarioId, rolUsuario);

        // Guardar en caché
        proxyCache.put(cacheKey, proxy);

        logger.info("MaterialProxy creado y almacenado en caché para material ID: {}", materialId);

        return proxy;
    }

    /**
     * Obtiene múltiples materiales de un módulo con Proxy
     *
     * @param moduloId ID del módulo
     * @param usuarioId ID del usuario
     * @param rolUsuario Rol del usuario
     * @return Lista de MaterialProxy para cada material del módulo
     */
    public List<MaterialProxy> obtenerMaterialesModuloConProxy(Long moduloId, Long usuarioId, String rolUsuario) {
        logger.info("Solicitando materiales del módulo ID: {} para usuario: {} (rol: {})",
                moduloId, usuarioId, rolUsuario);

        List<Material> materiales = materialRepository.findByModuloIdOrderByOrdenAsc(moduloId);

        if (materiales.isEmpty()) {
            logger.warn("No se encontraron materiales para el módulo ID: {}", moduloId);
            return Collections.emptyList();
        }

        List<MaterialProxy> proxies = new ArrayList<>();

        for (Material material : materiales) {
            MaterialProxy proxy = obtenerMaterialConProxy(material.getId(), usuarioId, rolUsuario);
            proxies.add(proxy);
        }

        logger.info("Se crearon {} proxies para el módulo ID: {}", proxies.size(), moduloId);

        return proxies;
    }

    /**
     * Obtiene materiales de un curso con Proxy
     *
     * @param cursoId ID del curso
     * @param usuarioId ID del usuario
     * @param rolUsuario Rol del usuario
     * @return Lista de MaterialProxy para cada material del curso
     */
    public List<MaterialProxy> obtenerMaterialesCursoConProxy(Integer cursoId, Long usuarioId, String rolUsuario) {
        logger.info("Solicitando materiales del curso ID: {} para usuario: {} (rol: {})",
                cursoId, usuarioId, rolUsuario);

        List<Material> materiales = materialRepository.findByCursoId(cursoId);

        if (materiales.isEmpty()) {
            logger.warn("No se encontraron materiales para el curso ID: {}", cursoId);
            return Collections.emptyList();
        }

        List<MaterialProxy> proxies = new ArrayList<>();

        for (Material material : materiales) {
            MaterialProxy proxy = obtenerMaterialConProxy(material.getId(), usuarioId, rolUsuario);
            proxies.add(proxy);
        }

        logger.info("Se crearon {} proxies para el curso ID: {}", proxies.size(), cursoId);

        return proxies;
    }

    /**
     * Carga el contenido de un material a través del proxy
     *
     * @param materialId ID del material
     * @param usuarioId ID del usuario
     * @param rolUsuario Rol del usuario
     * @return Contenido del material en bytes
     */
    public byte[] cargarContenidoMaterial(Long materialId, Long usuarioId, String rolUsuario) {
        logger.info("Solicitando carga de contenido del material ID: {} por usuario: {}",
                materialId, usuarioId);

        MaterialProxy proxy = obtenerMaterialConProxy(materialId, usuarioId, rolUsuario);

        // El proxy verificará permisos y aplicará lazy loading
        return proxy.cargarContenido();
    }

    /**
     * Obtiene solo la información básica del material (sin cargar contenido)
     *
     * @param materialId ID del material
     * @param usuarioId ID del usuario
     * @param rolUsuario Rol del usuario
     * @return Material con información básica
     */
    public Material obtenerInformacionBasica(Long materialId, Long usuarioId, String rolUsuario) {
        logger.info("Solicitando información básica del material ID: {} para usuario: {}",
                materialId, usuarioId);

        MaterialProxy proxy = obtenerMaterialConProxy(materialId, usuarioId, rolUsuario);

        // Solo retorna metadatos, sin cargar el contenido pesado
        return proxy.obtenerInformacionBasica();
    }

    /**
     * Verifica si un usuario tiene acceso a un material
     *
     * @param materialId ID del material
     * @param usuarioId ID del usuario
     * @param rolUsuario Rol del usuario
     * @return true si tiene acceso, false en caso contrario
     */
    public boolean verificarAccesoMaterial(Long materialId, Long usuarioId, String rolUsuario) {
        logger.info("Verificando acceso al material ID: {} para usuario: {} (rol: {})",
                materialId, usuarioId, rolUsuario);

        MaterialProxy proxy = obtenerMaterialConProxy(materialId, usuarioId, rolUsuario);

        return proxy.verificarAcceso(usuarioId, rolUsuario);
    }

    /**
     * Libera de caché los materiales de un usuario específico
     *
     * @param usuarioId ID del usuario
     */
    public void liberarCacheUsuario(Long usuarioId) {
        logger.info("Liberando caché de materiales para usuario: {}", usuarioId);

        String prefix = "user_" + usuarioId + "_";
        List<String> keysToRemove = new ArrayList<>();

        proxyCache.keySet().forEach(key -> {
            if (key.startsWith(prefix)) {
                keysToRemove.add(key);
            }
        });

        keysToRemove.forEach(key -> {
            MaterialProxy proxy = proxyCache.remove(key);
            if (proxy != null) {
                proxy.liberarMemoria();
            }
        });

        logger.info("Se liberaron {} materiales del caché para usuario: {}",
                keysToRemove.size(), usuarioId);
    }

    /**
     * Libera completamente la caché de proxies
     */
    public void limpiarCacheCompleto() {
        logger.info("Limpiando caché completo de materiales. Total items: {}", proxyCache.size());

        proxyCache.values().forEach(MaterialProxy::liberarMemoria);
        proxyCache.clear();

        logger.info("Caché de materiales limpiado completamente");
    }

    /**
     * Obtiene estadísticas de uso del caché
     *
     * @return Mapa con estadísticas
     */
    public Map<String, Object> obtenerEstadisticasCache() {
        Map<String, Object> stats = new HashMap<>();

        int totalProxies = proxyCache.size();
        long proxiesConContenidoCargado = proxyCache.values().stream()
                .filter(MaterialProxy::estaContenidoCargado)
                .count();

        stats.put("totalProxiesEnCache", totalProxies);
        stats.put("proxiesConContenidoCargado", proxiesConContenidoCargado);
        stats.put("proxiesSinCargar", totalProxies - proxiesConContenidoCargado);

        logger.debug("Estadísticas de caché: {}", stats);

        return stats;
    }

    /**
     * Genera una clave única para el caché
     *
     * @param usuarioId ID del usuario
     * @param materialId ID del material
     * @return Clave única
     */
    private String generarCacheKey(Long usuarioId, Long materialId) {
        return "user_" + usuarioId + "_material_" + materialId;
    }
}
