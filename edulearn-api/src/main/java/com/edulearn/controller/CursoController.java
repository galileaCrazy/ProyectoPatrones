package com.edulearn.controller;

import com.edulearn.model.Curso;
import com.edulearn.model.Inscripcion;
import com.edulearn.model.Modulo;
import com.edulearn.patterns.creational.builder.CursoBuilder;
import com.edulearn.patterns.creational.builder.CursoDirector;
import com.edulearn.patterns.creational.prototype.CursoPrototype;
import com.edulearn.patterns.comportamiento.chain_of_responsibility.SolicitudValidacion;
import com.edulearn.repository.CursoRepository;
import com.edulearn.repository.InscripcionRepository;
import com.edulearn.repository.ModuloRepository;
import com.edulearn.service.CadenaVisualizacionCursosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = "*")
public class CursoController {
    private static final Logger logger = LoggerFactory.getLogger(CursoController.class);

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CadenaVisualizacionCursosService cadenaVisualizacionService;

    @GetMapping
    public List<Curso> getAll() {
        return cursoRepository.findAll();
    }

    /**
     * GET /api/cursos/por-usuario/{userId}?rol={rol}
     * Obtener cursos filtrados según el rol del usuario usando Chain of Responsibility.
     *
     * Patrón utilizado: Chain of Responsibility
     * - administrador: todos los cursos
     * - profesor: solo sus cursos
     * - estudiante: solo cursos inscritos
     */
    @GetMapping("/por-usuario/{userId}")
    public ResponseEntity<?> getCursosPorUsuario(
            @PathVariable Integer userId,
            @RequestParam String rol
    ) {
        logger.info("🔍 Obteniendo cursos para usuario {} con rol {}", userId, rol);

        // Usar Chain of Responsibility para validar permisos
        SolicitudValidacion validacion = cadenaVisualizacionService.validarVisualizacion(userId, rol);

        // Si la validación falló, retornar error
        if (!validacion.isAprobada()) {
            logger.error("❌ Validación fallida: {}", validacion.getMensajeError());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                        "error", validacion.getMensajeError(),
                        "userId", userId,
                        "rol", rol
                    ));
        }

        // Obtener el tipo de filtro determinado por la cadena
        String tipoFiltro = (String) validacion.getMetadatos().get("tipoFiltro");
        List<Curso> cursos;

        switch (tipoFiltro) {
            case "TODOS":
                // Administrador ve todos los cursos
                cursos = cursoRepository.findAll();
                logger.info("✅ Administrador - Retornando {} cursos", cursos.size());
                break;

            case "POR_PROFESOR":
                // Profesor ve solo sus cursos
                cursos = cursoRepository.findByProfesorTitularId(userId);
                logger.info("✅ Profesor - Retornando {} cursos", cursos.size());
                break;

            case "POR_ESTUDIANTE":
                // Estudiante ve solo cursos donde está inscrito
                cursos = cursoRepository.findCursosByEstudianteId(userId);
                logger.info("✅ Estudiante - Retornando {} cursos inscritos", cursos.size());
                break;

            default:
                logger.error("❌ Tipo de filtro no reconocido: {}", tipoFiltro);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Tipo de filtro no reconocido"));
        }

        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/{id}")
    public Curso getById(@PathVariable Integer id) {
        return cursoRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Curso create(@RequestBody Curso curso) {
        return cursoRepository.save(curso);
    }

    @PutMapping("/{id}")
    public Curso update(@PathVariable Integer id, @RequestBody Curso cursoActualizado) {
        Curso cursoExistente = cursoRepository.findById(id).orElse(null);
        if (cursoExistente == null) {
            return null;
        }

        // Actualizar solo los campos que vienen en el request
        if (cursoActualizado.getNombre() != null) {
            cursoExistente.setNombre(cursoActualizado.getNombre());
        }
        if (cursoActualizado.getDescripcion() != null) {
            cursoExistente.setDescripcion(cursoActualizado.getDescripcion());
        }
        if (cursoActualizado.getTipoCurso() != null) {
            cursoExistente.setTipoCurso(cursoActualizado.getTipoCurso());
        }
        if (cursoActualizado.getDuracion() != null) {
            cursoExistente.setDuracion(cursoActualizado.getDuracion());
        }
        if (cursoActualizado.getEstado() != null) {
            cursoExistente.setEstado(cursoActualizado.getEstado());
        }
        if (cursoActualizado.getEstrategiaEvaluacion() != null) {
            cursoExistente.setEstrategiaEvaluacion(cursoActualizado.getEstrategiaEvaluacion());
        }

        return cursoRepository.save(cursoExistente);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        cursoRepository.deleteById(id);
    }

    // ========== ENDPOINTS CON PATRÓN BUILDER ==========

    /**
     * POST /api/cursos/builder
     * Crear curso usando patrón Builder (construcción paso a paso)
     */
    @PostMapping("/builder")
    public Curso crearConBuilder(@RequestBody Map<String, Object> params) {
        CursoBuilder builder = new CursoBuilder();

        if (params.containsKey("nombre")) {
            builder.setNombre((String) params.get("nombre"));
        }
        if (params.containsKey("codigo")) {
            builder.setCodigo((String) params.get("codigo"));
        }
        if (params.containsKey("descripcion")) {
            builder.setDescripcion((String) params.get("descripcion"));
        }
        if (params.containsKey("tipoCurso")) {
            builder.setTipoCurso((String) params.get("tipoCurso"));
        }
        if (params.containsKey("duracion")) {
            builder.setDuracion((Integer) params.get("duracion"));
        }
        if (params.containsKey("profesorTitularId")) {
            builder.setProfesorTitularId((Integer) params.get("profesorTitularId"));
        }
        if (params.containsKey("periodoAcademico")) {
            builder.setPeriodoAcademico((String) params.get("periodoAcademico"));
        }
        if (params.containsKey("estado")) {
            builder.setEstado((String) params.get("estado"));
        }
        if (params.containsKey("estrategiaEvaluacion")) {
            builder.setEstrategiaEvaluacion((String) params.get("estrategiaEvaluacion"));
        }

        Curso curso = builder.build();
        return cursoRepository.save(curso);
    }

    /**
     * POST /api/cursos/builder/regular
     * Crear curso regular usando Director
     */
    @PostMapping("/builder/regular")
    public Curso crearCursoRegular(@RequestBody Map<String, String> params) {
        CursoDirector director = new CursoDirector();
        Curso curso = director.construirCursoRegular(
            params.get("nombre"),
            params.get("periodoAcademico")
        );
        return cursoRepository.save(curso);
    }

    /**
     * POST /api/cursos/builder/intensivo
     * Crear curso intensivo usando Director
     */
    @PostMapping("/builder/intensivo")
    public Curso crearCursoIntensivo(@RequestBody Map<String, Object> params) {
        try {
            System.out.println("📥 Recibiendo solicitud de curso intensivo");
            System.out.println("📦 Parámetros: " + params);

            String nombre = (String) params.get("nombre");
            Object profesorIdObj = params.get("profesorId");

            Integer profesorId = null;
            if (profesorIdObj != null) {
                if (profesorIdObj instanceof Integer) {
                    profesorId = (Integer) profesorIdObj;
                } else if (profesorIdObj instanceof String) {
                    try {
                        profesorId = Integer.parseInt((String) profesorIdObj);
                    } catch (NumberFormatException e) {
                        System.err.println("❌ Error: profesorId no es un número válido: " + profesorIdObj);
                    }
                }
            }

            System.out.println("👤 Profesor ID: " + profesorId);
            System.out.println("📝 Nombre: " + nombre);

            CursoDirector director = new CursoDirector();
            Curso curso = director.construirCursoIntensivo(nombre, profesorId);
            Curso cursoGuardado = cursoRepository.save(curso);

            System.out.println("✅ Curso intensivo creado con ID: " + cursoGuardado.getId());
            return cursoGuardado;
        } catch (Exception e) {
            System.err.println("❌ Error al crear curso intensivo: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al crear curso intensivo: " + e.getMessage(), e);
        }
    }

    /**
     * POST /api/cursos/builder/certificacion
     * Crear curso de certificación usando Director
     */
    @PostMapping("/builder/certificacion")
    public Curso crearCursoCertificacion(@RequestBody Map<String, Object> params) {
        try {
            System.out.println("📥 Recibiendo solicitud de curso certificación");
            System.out.println("📦 Parámetros: " + params);

            String nombre = (String) params.get("nombre");
            String periodoAcademico = (String) params.get("periodoAcademico");
            Object profesorIdObj = params.get("profesorId");

            Integer profesorId = null;
            if (profesorIdObj != null) {
                if (profesorIdObj instanceof Integer) {
                    profesorId = (Integer) profesorIdObj;
                } else if (profesorIdObj instanceof String) {
                    try {
                        profesorId = Integer.parseInt((String) profesorIdObj);
                    } catch (NumberFormatException e) {
                        System.err.println("❌ Error: profesorId no es un número válido: " + profesorIdObj);
                    }
                }
            }

            System.out.println("👤 Profesor ID: " + profesorId);
            System.out.println("📝 Nombre: " + nombre);
            System.out.println("📅 Período: " + periodoAcademico);

            CursoDirector director = new CursoDirector();
            Curso curso = director.construirCursoCertificacion(nombre, profesorId, periodoAcademico);
            Curso cursoGuardado = cursoRepository.save(curso);

            System.out.println("✅ Curso certificación creado con ID: " + cursoGuardado.getId());
            return cursoGuardado;
        } catch (Exception e) {
            System.err.println("❌ Error al crear curso certificación: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al crear curso certificación: " + e.getMessage(), e);
        }
    }

    // ========== ENDPOINTS CON PATRÓN PROTOTYPE ==========

    /**
     * POST /api/cursos/{id}/clonar
     * Clonar un curso existente usando patrón Prototype
     * Incluye la duplicación de todos los módulos asociados al curso
     */
    @PostMapping("/{id}/clonar")
    public ResponseEntity<?> clonarCurso(@PathVariable Integer id, @RequestBody(required = false) Map<String, String> params) {
        try {
            logger.info("📋 Iniciando clonación de curso con ID: {}", id);

            // Buscar el curso original
            Curso cursoOriginal = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + id));

            // Crear el prototype
            CursoPrototype prototype = new CursoPrototype(cursoOriginal);

            // Clonar el curso
            Curso cursoClon;
            if (params != null && (params.containsKey("nombre") || params.containsKey("periodoAcademico"))) {
                cursoClon = prototype.cloneConPersonalizacion(
                    params.get("nombre"),
                    params.get("periodoAcademico")
                );
                logger.info("✅ Curso clonado con personalización - Nombre: {}, Período: {}",
                    params.get("nombre"), params.get("periodoAcademico"));
            } else {
                cursoClon = prototype.clone();
                logger.info("✅ Curso clonado sin personalización");
            }

            // Guardar el curso clonado
            Curso cursoGuardado = cursoRepository.save(cursoClon);
            logger.info("💾 Curso guardado con nuevo ID: {}", cursoGuardado.getId());

            // Clonar los módulos del curso original
            List<Modulo> modulosOriginales = moduloRepository.findByCursoIdOrderByOrdenAsc(id);
            logger.info("📚 Encontrados {} módulos para clonar", modulosOriginales.size());

            int modulosClonados = 0;
            for (Modulo moduloOriginal : modulosOriginales) {
                Modulo moduloClon = new Modulo();
                moduloClon.setCursoId(cursoGuardado.getId());
                moduloClon.setNombre(moduloOriginal.getNombre());
                moduloClon.setTitulo(moduloOriginal.getTitulo());
                moduloClon.setDescripcion(moduloOriginal.getDescripcion());
                moduloClon.setOrden(moduloOriginal.getOrden());
                moduloClon.setEstado("draft"); // Los módulos clonados empiezan en draft
                moduloClon.setTipo(moduloOriginal.getTipo());
                moduloClon.setDuracionEstimada(moduloOriginal.getDuracionEstimada());

                moduloRepository.save(moduloClon);
                modulosClonados++;
            }

            logger.info("✅ Se clonaron {} módulos exitosamente", modulosClonados);

            // Construir respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("curso", cursoGuardado);
            response.put("modulosClonados", modulosClonados);
            response.put("mensaje", String.format("Curso duplicado exitosamente con %d módulos", modulosClonados));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("❌ Error al clonar curso: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "error", "Error al clonar curso",
                    "mensaje", e.getMessage()
                ));
        }
    }

    /**
     * POST /api/cursos/plantilla/{tipoCurso}
     * Crear una plantilla de curso usando Prototype
     */
    @PostMapping("/plantilla/{tipoCurso}")
    public Curso crearPlantilla(@PathVariable String tipoCurso) {
        Curso plantilla = CursoPrototype.crearPlantilla(tipoCurso);
        return cursoRepository.save(plantilla);
    }

    /**
     * POST /api/cursos/plantilla/{tipoCurso}/clonar
     * Crear curso desde plantilla con personalización
     */
    @PostMapping("/plantilla/{tipoCurso}/clonar")
    public Curso crearDesdePlantilla(
        @PathVariable String tipoCurso,
        @RequestBody Map<String, String> params
    ) {
        Curso plantilla = CursoPrototype.crearPlantilla(tipoCurso);
        CursoPrototype prototype = new CursoPrototype(plantilla);

        Curso cursoNuevo = prototype.cloneConPersonalizacion(
            params.get("nombre"),
            params.get("periodoAcademico")
        );

        // Asignar profesor si viene en params
        if (params.containsKey("profesorId")) {
            cursoNuevo.setProfesorTitularId(Integer.parseInt(params.get("profesorId")));
        }

        return cursoRepository.save(cursoNuevo);
    }
}
