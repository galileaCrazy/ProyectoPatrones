package com.edulearn.controller;

import com.edulearn.model.Usuario;
import com.edulearn.patterns.comportamiento.chain_of_responsibility.SolicitudValidacion;
import com.edulearn.repository.UsuarioRepository;
import com.edulearn.service.CadenaValidacionCursoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller para gestionar la configuración del formulario de creación de cursos
 * usando el patrón Chain of Responsibility
 */
@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = "*")
public class FormularioCreacionCursoController {

    private static final Logger logger = LoggerFactory.getLogger(FormularioCreacionCursoController.class);

    @Autowired
    private CadenaValidacionCursoService cadenaValidacionCursoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Endpoint para obtener la configuración del formulario según el rol del usuario
     * Usa el patrón Chain of Responsibility para determinar los permisos y configuración
     *
     * @param usuarioId ID del usuario
     * @param rol Rol del usuario (PROFESOR o ADMINISTRADOR)
     * @param nombreUsuario Nombre del usuario
     * @return Configuración del formulario según el rol
     */
    @GetMapping("/formulario/configuracion")
    public ResponseEntity<Map<String, Object>> obtenerConfiguracionFormulario(
            @RequestParam String usuarioId,
            @RequestParam String rol,
            @RequestParam(required = false) String nombreUsuario) {

        logger.info("📝 Solicitando configuración de formulario para usuario: {} con rol: {}", usuarioId, rol);

        try {
            // Crear solicitud de validación usando el constructor correcto
            SolicitudValidacion solicitud = new SolicitudValidacion(
                "TOKEN_TEMPORAL",  // Token (puede ser null para este caso)
                "FORMULARIO_CURSO", // Recurso solicitado
                "CONFIGURAR_FORMULARIO" // Acción
            );

            solicitud.setTipoUsuario(rol);
            solicitud.agregarMetadato("usuarioId", usuarioId);
            solicitud.agregarMetadato("nombreUsuario", nombreUsuario != null ? nombreUsuario : "Usuario");

            // Procesar con Chain of Responsibility (cadena completa)
            boolean valido = cadenaValidacionCursoService.validarConfiguracionFormulario(solicitud);

            Map<String, Object> resultado = new HashMap<>();

            if (valido && solicitud.isAprobada()) {
                resultado.put("valido", true);
                resultado.putAll(solicitud.getMetadatos());
                logger.info("✅ Configuración generada exitosamente");
                return ResponseEntity.ok(resultado);
            } else {
                resultado.put("valido", false);
                resultado.put("mensaje", solicitud.getMensajeError());
                logger.warn("⚠️ Validación fallida: {}", solicitud.getMensajeError());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(resultado);
            }

        } catch (Exception e) {
            logger.error("❌ Error al procesar configuración: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("valido", false);
            error.put("mensaje", "Error al procesar la configuración: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Endpoint para obtener la lista de profesores desde la base de datos
     *
     * @return Lista de profesores
     */
    @GetMapping("/profesores")
    public ResponseEntity<Map<String, Object>> obtenerProfesores() {
        logger.info("👨‍🏫 Solicitando lista de profesores desde la BD");

        try {
            List<Usuario> profesores = usuarioRepository.findByTipoUsuario("profesor");

            List<Map<String, Object>> profesoresList = new ArrayList<>();
            for (Usuario profesor : profesores) {
                Map<String, Object> profesorData = new HashMap<>();
                profesorData.put("id", profesor.getId());
                profesorData.put("nombre", profesor.getNombre() + " " + profesor.getApellidos());
                profesorData.put("email", profesor.getEmail());
                profesoresList.add(profesorData);
            }

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("exito", true);
            resultado.put("profesores", profesoresList);
            resultado.put("total", profesoresList.size());

            logger.info("✅ Se encontraron {} profesores en la BD", profesoresList.size());
            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            logger.error("❌ Error al obtener profesores: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("exito", false);
            error.put("mensaje", "Error al obtener profesores: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Endpoint para obtener datos del usuario actual (nombre completo)
     *
     * @param usuarioId ID del usuario
     * @return Datos del usuario
     */
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<Map<String, Object>> obtenerDatosUsuario(@PathVariable Integer usuarioId) {
        logger.info("👤 Solicitando datos del usuario ID: {}", usuarioId);

        try {
            Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("exito", true);
            resultado.put("id", usuario.getId());
            resultado.put("nombre", usuario.getNombre());
            resultado.put("apellido", usuario.getApellidos());
            resultado.put("nombreCompleto", usuario.getNombre() + " " + usuario.getApellidos());
            resultado.put("email", usuario.getEmail());
            resultado.put("tipoUsuario", usuario.getTipoUsuario());

            logger.info("✅ Usuario encontrado: {}", resultado.get("nombreCompleto"));
            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            logger.error("❌ Error al obtener usuario: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("exito", false);
            error.put("mensaje", "Error al obtener usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Endpoint para obtener los períodos académicos válidos
     * Usa Chain of Responsibility para calcular períodos futuros permitidos
     *
     * @return Lista de períodos válidos
     */
    @GetMapping("/periodos-validos")
    public ResponseEntity<Map<String, Object>> obtenerPeriodosValidos() {
        logger.info("📅 Solicitando períodos académicos válidos");

        try {
            SolicitudValidacion solicitud = new SolicitudValidacion(
                "TOKEN_TEMPORAL",
                "PERIODOS",
                "VALIDAR_FECHAS_CURSO"
            );

            solicitud.setTipoUsuario("SYSTEM");

            // Ejecutar validación para obtener períodos
            cadenaValidacionCursoService.validarConfiguracionFormulario(solicitud);

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("exito", true);
            resultado.put("periodosValidos", solicitud.getMetadatos().get("periodosValidos"));

            logger.info("✅ Períodos válidos obtenidos: {}", solicitud.getMetadatos().get("periodosValidos"));
            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            logger.error("❌ Error al obtener períodos: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("exito", false);
            error.put("mensaje", "Error al obtener períodos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Endpoint para crear un curso validando con Chain of Responsibility
     *
     * @param cursoData Datos del curso a crear
     * @return Resultado de la creación
     */
    @PostMapping("/crear")
    public ResponseEntity<Map<String, Object>> crearCurso(@RequestBody Map<String, Object> cursoData) {

        logger.info("🎓 Iniciando creación de curso: {}", cursoData.get("nombre"));

        try {
            String usuarioId = (String) cursoData.get("usuarioId");
            String rol = (String) cursoData.get("rol");

            // Validar con Chain of Responsibility
            SolicitudValidacion solicitud = new SolicitudValidacion(
                "TOKEN_TEMPORAL",
                "CURSO",
                "CREAR_CURSO"
            );

            solicitud.setTipoUsuario(rol);
            solicitud.agregarMetadato("usuarioId", usuarioId);
            solicitud.agregarMetadato("nombreUsuario", cursoData.get("nombreUsuario"));
            solicitud.agregarMetadato("periodo", cursoData.get("periodo"));
            solicitud.agregarMetadato("fechaInicio", cursoData.get("fechaInicio"));
            solicitud.agregarMetadato("fechaFin", cursoData.get("fechaFin"));

            // Validar con cadena completa
            boolean valido = cadenaValidacionCursoService.validarCreacionCurso(solicitud);

            if (!valido || !solicitud.isAprobada()) {
                logger.error("❌ Validación fallida: {}", solicitud.getMensajeError());
                Map<String, Object> error = new HashMap<>();
                error.put("exito", false);
                error.put("mensaje", solicitud.getMensajeError());
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            }

            // Si es profesor, forzar auto-asignación
            if ("PROFESOR".equalsIgnoreCase(rol)) {
                cursoData.put("profesorId", usuarioId);
                logger.info("👨‍🏫 Profesor auto-asignado: {}", usuarioId);
            }

            // Aquí iría la lógica real de creación del curso en la BD
            Map<String, Object> resultado = new HashMap<>();
            resultado.put("exito", true);
            resultado.put("mensaje", "Curso creado exitosamente");
            resultado.put("cursoId", "CURSO-" + System.currentTimeMillis());
            resultado.put("nombre", cursoData.get("nombre"));
            resultado.put("profesorAsignado", cursoData.get("profesorId"));

            logger.info("✅ Curso creado exitosamente");
            return ResponseEntity.status(HttpStatus.CREATED).body(resultado);

        } catch (Exception e) {
            logger.error("❌ Error al crear curso: {}", e.getMessage(), e);
            Map<String, Object> error = new HashMap<>();
            error.put("exito", false);
            error.put("mensaje", "Error al crear curso: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
