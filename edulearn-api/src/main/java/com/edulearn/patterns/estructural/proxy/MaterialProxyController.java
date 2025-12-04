package com.edulearn.patterns.estructural.proxy;

import com.edulearn.model.Material;
import com.edulearn.patterns.estructural.proxy.dto.MaterialProxyRequest;
import com.edulearn.patterns.estructural.proxy.dto.MaterialProxyResponse;
import com.edulearn.patterns.estructural.proxy.dto.ModuloMaterialesRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para el Patrón Proxy de Materiales
 *
 * Expone endpoints para acceder a materiales con lazy loading
 * y control de acceso mediante el patrón Proxy.
 *
 * Endpoints:
 * - POST /api/materiales/proxy - Obtener material con proxy (lazy loading)
 * - POST /api/materiales/proxy/modulo - Obtener materiales de un módulo
 * - POST /api/materiales/proxy/cargar - Cargar contenido de un material
 * - GET /api/materiales/proxy/verificar-acceso/{materialId} - Verificar acceso
 *
 * Patrón de Diseño: Proxy (Estructural)
 */
@RestController
@RequestMapping("/api/materiales/proxy")
@CrossOrigin(origins = "*")
public class MaterialProxyController {

    private static final Logger logger = LoggerFactory.getLogger(MaterialProxyController.class);

    @Autowired
    private MaterialProxyService materialProxyService;

    /**
     * Endpoint principal: Obtener material con Proxy (sin cargar contenido pesado)
     *
     * POST /api/materiales/proxy
     *
     * @param request Datos de la solicitud (materialId, usuarioId, rol)
     * @return Información básica del material sin contenido pesado
     */
    @PostMapping
    public ResponseEntity<MaterialProxyResponse> obtenerMaterialConProxy(
            @RequestBody MaterialProxyRequest request) {

        logger.info("POST /api/materiales/proxy - Request: {}", request);

        try {
            // Validaciones
            if (request.getMaterialId() == null) {
                return ResponseEntity.badRequest().build();
            }

            if (request.getUsuarioId() == null) {
                return ResponseEntity.badRequest().build();
            }

            if (request.getRolUsuario() == null || request.getRolUsuario().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            // Obtener el proxy del material
            MaterialProxy proxy = materialProxyService.obtenerMaterialConProxy(
                    request.getMaterialId(),
                    request.getUsuarioId(),
                    request.getRolUsuario()
            );

            // Verificar acceso
            boolean tieneAcceso = proxy.verificarAcceso(request.getUsuarioId(), request.getRolUsuario());

            // Construir respuesta
            Material material = proxy.obtenerInformacionBasica();
            MaterialProxyResponse response = new MaterialProxyResponse(material);
            response.setContenidoCargado(proxy.estaContenidoCargado());
            response.setTieneAcceso(tieneAcceso);
            response.setEsMaterialPesado(proxy.esMaterialPesado());
            response.setUrlCarga("/api/materiales/proxy/cargar");

            logger.info("Material Proxy creado exitosamente - ID: {}, Acceso: {}, Pesado: {}",
                    material.getId(), tieneAcceso, proxy.esMaterialPesado());

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.error("Material no encontrado: {}", e.getMessage());
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            logger.error("Error al crear proxy de material", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener todos los materiales de un módulo con Proxy
     *
     * POST /api/materiales/proxy/modulo
     *
     * @param request Datos del módulo y usuario
     * @return Lista de materiales con información básica (lazy loading)
     */
    @PostMapping("/modulo")
    public ResponseEntity<List<MaterialProxyResponse>> obtenerMaterialesModulo(
            @RequestBody ModuloMaterialesRequest request) {

        logger.info("POST /api/materiales/proxy/modulo - Request: {}", request);

        try {
            // Validaciones
            if (request.getModuloId() == null || request.getUsuarioId() == null ||
                request.getRolUsuario() == null) {
                return ResponseEntity.badRequest().build();
            }

            // Obtener proxies de todos los materiales del módulo
            List<MaterialProxy> proxies = materialProxyService.obtenerMaterialesModuloConProxy(
                    request.getModuloId(),
                    request.getUsuarioId(),
                    request.getRolUsuario()
            );

            // Convertir a DTOs
            List<MaterialProxyResponse> responses = proxies.stream()
                    .map(proxy -> {
                        Material material = proxy.obtenerInformacionBasica();
                        MaterialProxyResponse response = new MaterialProxyResponse(material);
                        response.setContenidoCargado(proxy.estaContenidoCargado());
                        response.setTieneAcceso(proxy.verificarAcceso(request.getUsuarioId(), request.getRolUsuario()));
                        response.setEsMaterialPesado(proxy.esMaterialPesado());
                        response.setUrlCarga("/api/materiales/proxy/cargar");
                        return response;
                    })
                    .collect(Collectors.toList());

            logger.info("Retornando {} materiales del módulo ID: {}", responses.size(), request.getModuloId());

            return ResponseEntity.ok(responses);

        } catch (Exception e) {
            logger.error("Error al obtener materiales del módulo", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cargar el contenido real de un material (lazy loading activado)
     *
     * POST /api/materiales/proxy/cargar
     *
     * @param request Datos de la solicitud
     * @return Contenido del material en bytes
     */
    @PostMapping("/cargar")
    public ResponseEntity<byte[]> cargarContenidoMaterial(
            @RequestBody MaterialProxyRequest request) {

        logger.info("POST /api/materiales/proxy/cargar - Material ID: {}, Usuario: {}",
                request.getMaterialId(), request.getUsuarioId());

        try {
            // Validaciones
            if (request.getMaterialId() == null || request.getUsuarioId() == null ||
                request.getRolUsuario() == null) {
                return ResponseEntity.badRequest().build();
            }

            // Cargar contenido a través del proxy
            byte[] contenido = materialProxyService.cargarContenidoMaterial(
                    request.getMaterialId(),
                    request.getUsuarioId(),
                    request.getRolUsuario()
            );

            // Obtener información del material para el Content-Type
            Material material = materialProxyService.obtenerInformacionBasica(
                    request.getMaterialId(),
                    request.getUsuarioId(),
                    request.getRolUsuario()
            );

            // Determinar el tipo de contenido
            String contentType = determinarContentType(material.getTipoMaterial());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(contenido.length);
            headers.set("Content-Disposition", "inline; filename=\"" + material.getTitulo() + "\"");

            logger.info("Contenido cargado exitosamente - Material ID: {}, Tamaño: {} bytes",
                    request.getMaterialId(), contenido.length);

            return new ResponseEntity<>(contenido, headers, HttpStatus.OK);

        } catch (SecurityException e) {
            logger.warn("Acceso denegado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        } catch (IllegalArgumentException e) {
            logger.error("Material no encontrado: {}", e.getMessage());
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            logger.error("Error al cargar contenido del material", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Verificar si un usuario tiene acceso a un material
     *
     * GET /api/materiales/proxy/verificar-acceso/{materialId}
     *
     * @param materialId ID del material
     * @param usuarioId ID del usuario (query param)
     * @param rol Rol del usuario (query param)
     * @return Información de acceso
     */
    @GetMapping("/verificar-acceso/{materialId}")
    public ResponseEntity<Map<String, Object>> verificarAcceso(
            @PathVariable Long materialId,
            @RequestParam Long usuarioId,
            @RequestParam String rol) {

        logger.info("GET /api/materiales/proxy/verificar-acceso/{} - Usuario: {}, Rol: {}",
                materialId, usuarioId, rol);

        try {
            boolean tieneAcceso = materialProxyService.verificarAccesoMaterial(
                    materialId, usuarioId, rol
            );

            Map<String, Object> response = new HashMap<>();
            response.put("materialId", materialId);
            response.put("usuarioId", usuarioId);
            response.put("tieneAcceso", tieneAcceso);
            response.put("mensaje", tieneAcceso ? "Acceso concedido" : "Acceso denegado");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            logger.error("Material no encontrado: {}", e.getMessage());
            return ResponseEntity.notFound().build();

        } catch (Exception e) {
            logger.error("Error al verificar acceso", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtener estadísticas del caché de proxies
     *
     * GET /api/materiales/proxy/estadisticas
     *
     * @return Estadísticas del caché
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        logger.info("GET /api/materiales/proxy/estadisticas");

        try {
            Map<String, Object> stats = materialProxyService.obtenerEstadisticasCache();
            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            logger.error("Error al obtener estadísticas", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Limpiar caché de un usuario específico
     *
     * DELETE /api/materiales/proxy/cache/{usuarioId}
     *
     * @param usuarioId ID del usuario
     * @return Confirmación
     */
    @DeleteMapping("/cache/{usuarioId}")
    public ResponseEntity<Map<String, String>> limpiarCacheUsuario(@PathVariable Long usuarioId) {
        logger.info("DELETE /api/materiales/proxy/cache/{}", usuarioId);

        try {
            materialProxyService.liberarCacheUsuario(usuarioId);

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Caché liberado exitosamente para usuario " + usuarioId);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error al limpiar caché", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Determina el Content-Type basado en el tipo de material
     *
     * @param tipoMaterial Tipo de material
     * @return Content-Type
     */
    private String determinarContentType(String tipoMaterial) {
        if (tipoMaterial == null) {
            return "application/octet-stream";
        }

        switch (tipoMaterial.toUpperCase()) {
            case "VIDEO":
                return "video/mp4";
            case "PDF":
                return "application/pdf";
            case "DOCUMENTO":
                return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "IMAGE":
            case "IMAGEN":
                return "image/jpeg";
            case "AUDIO":
                return "audio/mpeg";
            default:
                return "application/octet-stream";
        }
    }
}
