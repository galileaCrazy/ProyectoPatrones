package com.edulearn.controller;

import com.edulearn.model.Curso;
import com.edulearn.model.Inscripcion;
import com.edulearn.model.Usuario;
import com.edulearn.repository.CursoRepository;
import com.edulearn.repository.InscripcionRepository;
import com.edulearn.repository.UsuarioRepository;
// Importar clases de patrones
import com.edulearn.patterns.abstractfactory.*;
import com.edulearn.patterns.builder.CursoBuilder;
import com.edulearn.patterns.prototype.CursoPrototype;
import com.edulearn.patterns.singleton.ConfiguracionSistema;
import com.edulearn.patterns.facade.GestionCursosFacade;
import com.edulearn.patterns.bridge.*;
import com.edulearn.patterns.flyweight.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/patrones")
public class PatronesController {
    @Autowired
    private CursoRepository cursoRepository;
    @Autowired
    private InscripcionRepository inscripcionRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private GestionCursosFacade gestionFacade;

    // ═══════════════════════════════════════════════════════════
    // PATRÓN: ABSTRACT FACTORY
    // Crear familias de cursos (Virtual, Presencial, Híbrido)
    // ═══════════════════════════════════════════════════════════
    @PostMapping("/abstract-factory/crear-curso")
    public Map<String, Object> crearCursoPorTipo(@RequestParam String tipo) {
        Map<String, Object> result = new HashMap<>();

        // Seleccionar la fábrica según el tipo
        CourseComponentFactory factory;
        switch (tipo.toLowerCase()) {
            case "virtual":
                factory = new VirtualCourseFactory();
                break;
            case "presencial":
                factory = new PresencialCourseFactory();
                break;
            case "hibrido":
                factory = new HibridoCourseFactory();
                break;
            default:
                result.put("error", "Tipo de curso no válido. Use: virtual, presencial o hibrido");
                return result;
        }

        // Usar la fábrica para crear el curso
        String timestamp = String.valueOf(System.currentTimeMillis());
        String codigo = "AF" + timestamp.substring(timestamp.length() - 6);
        ICurso curso = factory.crearCurso(
            codigo,
            "Curso " + factory.getTipoFactory(),
            "Curso creado usando Abstract Factory",
            1,
            "2024-1"
        );

        // Crear material y evaluación de la familia
        IMaterial material = factory.crearMaterial(
            "Material Principal",
            "Material del curso " + factory.getTipoFactory()
        );

        IEvaluacion evaluacion = factory.crearEvaluacion(
            "Evaluación Final",
            "Evaluación del curso " + factory.getTipoFactory()
        );

        // Mostrar info en logs
        curso.mostrarInfo();
        material.mostrarInfo();
        evaluacion.mostrarInfo();

        // Guardar en BD
        Curso cursoEntity = curso.toEntity();
        Curso saved = cursoRepository.save(cursoEntity);

        result.put("patron", "Abstract Factory");
        result.put("fabrica", factory.getTipoFactory());
        result.put("curso", saved);
        result.put("materialTipo", material.getTipoMaterial());
        result.put("evaluacionTipo", evaluacion.getTipoEvaluacion());
        result.put("cupoMaximo", curso.getCupoMaximo());
        result.put("estrategiaEvaluacion", curso.getEstrategiaEvaluacion());

        return result;
    }

    // ═══════════════════════════════════════════════════════════
    // PATRÓN: BUILDER
    // Construir curso paso a paso
    // ═══════════════════════════════════════════════════════════
    @PostMapping("/builder/construir-curso")
    public Map<String, Object> construirCurso(
            @RequestParam String nombre,
            @RequestParam String descripcion,
            @RequestParam String tipoCurso,
            @RequestParam int duracion) {

        String timestamp = String.valueOf(System.currentTimeMillis());
        String codigo = "BD" + timestamp.substring(timestamp.length() - 6);

        // Usar el Builder para construir el curso paso a paso
        Curso curso = new CursoBuilder()
                .codigo(codigo)
                .nombre(nombre)
                .descripcion(descripcion)
                .tipoCurso(tipoCurso)
                .duracion(duracion)
                .profesorTitularId(1)
                .periodoAcademico("2024-1")
                .estado("activo")
                .build();

        Curso saved = cursoRepository.save(curso);

        Map<String, Object> result = new HashMap<>();
        result.put("patron", "Builder");
        result.put("curso", saved);
        result.put("pasos", Arrays.asList(
            "1. Crear builder",
            "2. Configurar código: " + codigo,
            "3. Configurar nombre: " + nombre,
            "4. Configurar tipo: " + tipoCurso,
            "5. Configurar duración: " + duracion,
            "6. Build - obtener curso construido"
        ));

        return result;
    }

    // ═══════════════════════════════════════════════════════════
    // PATRÓN: PROTOTYPE
    // Clonar curso existente
    // ═══════════════════════════════════════════════════════════
    @PostMapping("/prototype/clonar-curso/{id}")
    public Map<String, Object> clonarCurso(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();

        Optional<Curso> original = cursoRepository.findById(id);
        if (original.isEmpty()) {
            result.put("error", "Curso no encontrado");
            return result;
        }

        // Usar el Prototype para clonar
        CursoPrototype prototype = new CursoPrototype(original.get());
        CursoPrototype clon = prototype.clone();

        // Modificar código y nombre del clon
        String timestamp = String.valueOf(System.currentTimeMillis());
        clon.setCodigo("PT" + timestamp.substring(timestamp.length() - 6));
        clon.setNombre(original.get().getNombre() + " (Copia)");
        clon.setPeriodoAcademico("2024-2");

        Curso cursoClonado = clon.toEntity();
        Curso saved = cursoRepository.save(cursoClonado);

        result.put("patron", "Prototype");
        result.put("original", original.get());
        result.put("clon", saved);
        result.put("descripcion", "Curso clonado usando patrón Prototype");

        return result;
    }

    // ═══════════════════════════════════════════════════════════
    // PATRÓN: SINGLETON
    // Configuración global del sistema
    // ═══════════════════════════════════════════════════════════
    @GetMapping("/singleton/configuracion")
    public Map<String, Object> getConfiguracion() {
        ConfiguracionSistema config = ConfiguracionSistema.getInstance();

        Map<String, Object> result = new HashMap<>();
        result.put("patron", "Singleton");
        result.put("configuraciones", config.obtenerTodasLasConfiguraciones());
        result.put("descripcion", "Instancia única de configuración del sistema");

        return result;
    }

    @PostMapping("/singleton/configuracion")
    public Map<String, Object> setConfiguracion(@RequestParam String clave, @RequestParam String valor) {
        ConfiguracionSistema config = ConfiguracionSistema.getInstance();
        config.setConfiguracion(clave, valor);

        Map<String, Object> result = new HashMap<>();
        result.put("patron", "Singleton");
        result.put("mensaje", "Configuración actualizada");
        result.put("configuraciones", config.obtenerTodasLasConfiguraciones());

        return result;
    }

    // ═══════════════════════════════════════════════════════════
    // PATRÓN: FACADE
    // Interfaz simplificada para operaciones complejas
    // ═══════════════════════════════════════════════════════════
    @PostMapping("/facade/inscribir")
    public Map<String, Object> inscribirEstudiante(
            @RequestParam Integer estudianteId,
            @RequestParam Integer cursoId) {

        // Usar Facade para simplificar la operación compleja
        Map<String, Object> resultado = gestionFacade.inscribirEstudiante(estudianteId, cursoId);
        resultado.put("patron", "Facade");
        resultado.put("descripcion", "Operación simplificada usando Facade");

        return resultado;
    }

    @GetMapping("/facade/resumen-estudiante/{id}")
    public Map<String, Object> resumenEstudiante(@PathVariable Integer id) {
        Map<String, Object> resumen = gestionFacade.obtenerResumenEstudiante(id);
        resumen.put("patron", "Facade");

        return resumen;
    }

    @GetMapping("/facade/info-curso/{id}")
    public Map<String, Object> infoCurso(@PathVariable Integer id) {
        Map<String, Object> info = gestionFacade.obtenerInformacionCurso(id);
        info.put("patron", "Facade");

        return info;
    }

    // ═══════════════════════════════════════════════════════════
    // PATRÓN: ADAPTER (Simplificado)
    // Adaptar diferentes plataformas de videoconferencia
    // ═══════════════════════════════════════════════════════════
    @PostMapping("/adapter/videoconferencia")
    public Map<String, Object> crearVideoconferencia(@RequestParam String plataforma, @RequestParam String sala) {
        Map<String, Object> result = new HashMap<>();
        result.put("patron", "Adapter");
        result.put("plataforma", plataforma);
        result.put("sala", sala);

        // Adaptar según la plataforma
        switch (plataforma.toLowerCase()) {
            case "zoom":
                result.put("url", "https://zoom.us/j/" + sala);
                result.put("api", "Zoom API v2");
                result.put("descripcion", "Adaptador para Zoom");
                break;
            case "meet":
                result.put("url", "https://meet.google.com/" + sala);
                result.put("api", "Google Meet API");
                result.put("descripcion", "Adaptador para Google Meet");
                break;
            case "teams":
                result.put("url", "https://teams.microsoft.com/l/meetup-join/" + sala);
                result.put("api", "Microsoft Graph API");
                result.put("descripcion", "Adaptador para Microsoft Teams");
                break;
            default:
                result.put("error", "Plataforma no soportada");
        }
        return result;
    }

    // ═══════════════════════════════════════════════════════════
    // PATRÓN: BRIDGE
    // Separar abstracción (interfaz de usuario) de implementación (plataforma)
    // La MISMA interfaz funciona en Web, Móvil y Smart TV
    // ═══════════════════════════════════════════════════════════
    @GetMapping("/bridge/dashboard")
    public Map<String, Object> obtenerDashboard(
            @RequestParam String tipoUsuario,
            @RequestParam String plataforma) {

        Map<String, Object> result = new HashMap<>();
        result.put("patron", "Bridge");

        // Seleccionar la implementación de plataforma
        IPlataforma implementacion;
        switch (plataforma.toLowerCase()) {
            case "web":
                implementacion = new PlataformaWeb();
                break;
            case "movil":
            case "mobile":
                implementacion = new PlataformaMovil();
                break;
            case "smarttv":
            case "tv":
                implementacion = new PlataformaSmartTV();
                break;
            default:
                result.put("error", "Plataforma no válida. Use: web, movil, smarttv");
                return result;
        }

        // Crear la abstracción según el tipo de usuario
        InterfazUsuario dashboard;
        switch (tipoUsuario.toLowerCase()) {
            case "estudiante":
                dashboard = new DashboardEstudiante(implementacion);
                break;
            case "profesor":
                dashboard = new DashboardProfesor(implementacion);
                break;
            case "administrador":
            case "admin":
                dashboard = new DashboardAdmin(implementacion);
                break;
            default:
                result.put("error", "Tipo de usuario no válido. Use: estudiante, profesor, administrador");
                return result;
        }

        // Obtener el dashboard renderizado
        Map<String, Object> dashboardData = dashboard.mostrarDashboard();

        result.put("dashboard", dashboardData);
        result.put("descripcion", "Interfaz idéntica en funcionamiento, implementación diferente por plataforma");
        result.put("principio", "El patrón Bridge separa la abstracción (Dashboard) de su implementación (Plataforma)");

        return result;
    }

    @GetMapping("/bridge/cursos")
    public Map<String, Object> obtenerVistaCursos(
            @RequestParam String tipoUsuario,
            @RequestParam String plataforma) {

        Map<String, Object> result = new HashMap<>();

        // Seleccionar plataforma
        IPlataforma implementacion;
        switch (plataforma.toLowerCase()) {
            case "web":
                implementacion = new PlataformaWeb();
                break;
            case "movil":
            case "mobile":
                implementacion = new PlataformaMovil();
                break;
            case "smarttv":
            case "tv":
                implementacion = new PlataformaSmartTV();
                break;
            default:
                result.put("error", "Plataforma no válida");
                return result;
        }

        // Crear dashboard según tipo de usuario
        Map<String, Object> vista = null;
        switch (tipoUsuario.toLowerCase()) {
            case "estudiante":
                DashboardEstudiante dashEst = new DashboardEstudiante(implementacion);
                vista = dashEst.mostrarCursos();
                break;
            case "profesor":
                DashboardProfesor dashProf = new DashboardProfesor(implementacion);
                vista = dashProf.mostrarCursos();
                break;
            case "administrador":
            case "admin":
                DashboardAdmin dashAdmin = new DashboardAdmin(implementacion);
                vista = dashAdmin.mostrarCursos();
                break;
        }

        result.put("patron", "Bridge");
        result.put("vista", vista);
        return result;
    }

    @GetMapping("/bridge/perfil")
    public Map<String, Object> obtenerVistaPerfil(
            @RequestParam String tipoUsuario,
            @RequestParam String plataforma) {

        Map<String, Object> result = new HashMap<>();

        // Seleccionar plataforma
        IPlataforma implementacion;
        switch (plataforma.toLowerCase()) {
            case "web":
                implementacion = new PlataformaWeb();
                break;
            case "movil":
            case "mobile":
                implementacion = new PlataformaMovil();
                break;
            case "smarttv":
            case "tv":
                implementacion = new PlataformaSmartTV();
                break;
            default:
                result.put("error", "Plataforma no válida");
                return result;
        }

        // Crear dashboard según tipo de usuario
        Map<String, Object> vista = null;
        switch (tipoUsuario.toLowerCase()) {
            case "estudiante":
                DashboardEstudiante dashEst = new DashboardEstudiante(implementacion);
                vista = dashEst.mostrarPerfil();
                break;
            case "profesor":
                DashboardProfesor dashProf = new DashboardProfesor(implementacion);
                vista = dashProf.mostrarPerfil();
                break;
            case "administrador":
            case "admin":
                DashboardAdmin dashAdmin = new DashboardAdmin(implementacion);
                vista = dashAdmin.mostrarPerfil();
                break;
        }

        result.put("patron", "Bridge");
        result.put("vista", vista);
        return result;
    }

    // ═══════════════════════════════════════════════════════════
    // PATRÓN: COMPOSITE (Simplificado)
    // Estructura jerárquica de curso
    // ═══════════════════════════════════════════════════════════
    @GetMapping("/composite/estructura-curso/{id}")
    public Map<String, Object> estructuraCurso(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();

        Optional<Curso> curso = cursoRepository.findById(id);
        if (curso.isEmpty()) {
            result.put("error", "Curso no encontrado");
            return result;
        }

        result.put("patron", "Composite");
        result.put("tipo", "CursoCompuesto");
        result.put("nombre", curso.get().getNombre());

        // Simular estructura jerárquica
        List<Map<String, Object>> modulos = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Map<String, Object> modulo = new HashMap<>();
            modulo.put("tipo", "ModuloCompuesto");
            modulo.put("nombre", "Módulo " + i);

            List<Map<String, Object>> lecciones = new ArrayList<>();
            for (int j = 1; j <= 2; j++) {
                Map<String, Object> leccion = new HashMap<>();
                leccion.put("tipo", "LeccionHoja");
                leccion.put("nombre", "Lección " + i + "." + j);
                leccion.put("duracion", 30);
                lecciones.add(leccion);
            }
            modulo.put("lecciones", lecciones);
            modulos.add(modulo);
        }

        result.put("modulos", modulos);
        result.put("descripcion", "Estructura jerárquica usando Composite");
        return result;
    }

    // ═══════════════════════════════════════════════════════════
    // PATRÓN: DECORATOR (Simplificado)
    // Agregar funcionalidades a cursos dinámicamente
    // ═══════════════════════════════════════════════════════════
    @PostMapping("/decorator/decorar-curso/{id}")
    public Map<String, Object> decorarCurso(@PathVariable Integer id, @RequestParam String decorador) {
        Map<String, Object> result = new HashMap<>();

        Optional<Curso> curso = cursoRepository.findById(id);
        if (curso.isEmpty()) {
            result.put("error", "Curso no encontrado");
            return result;
        }

        result.put("patron", "Decorator");
        result.put("cursoBase", curso.get().getNombre());

        List<String> caracteristicas = new ArrayList<>();
        caracteristicas.add("Contenido básico del curso");

        switch (decorador.toLowerCase()) {
            case "certificado":
                caracteristicas.add("+ Certificado de finalización");
                result.put("decorador", "CertificadoDecorator");
                break;
            case "tutoria":
                caracteristicas.add("+ Sesiones de tutoría 1-a-1");
                result.put("decorador", "TutoriaDecorator");
                break;
            case "proyecto":
                caracteristicas.add("+ Proyecto práctico final");
                result.put("decorador", "ProyectoDecorator");
                break;
            default:
                result.put("error", "Decorador no válido");
                return result;
        }

        result.put("caracteristicas", caracteristicas);
        result.put("descripcion", "Funcionalidades agregadas dinámicamente usando Decorator");
        return result;
    }

    // ==================== PATRÓN FLYWEIGHT ====================
    /**
     * Renderiza todos los cursos usando el patrón Flyweight
     * Los recursos visuales (iconos, colores, plantillas) se comparten entre cursos del mismo tipo
     * Solo los datos específicos (nombre, descripción, etc.) son únicos
     */
    @GetMapping("/flyweight/renderizar-cursos")
    public Map<String, Object> renderizarCursosConFlyweight() {
        Map<String, Object> result = new HashMap<>();

        try {
            RecursoVisualFactory factory = RecursoVisualFactory.getInstance();
            List<Curso> cursos = cursoRepository.findAll();

            if (cursos.isEmpty()) {
                result.put("error", "No hay cursos disponibles");
                return result;
            }

            List<Map<String, Object>> cursosRenderizados = new ArrayList<>();

            for (Curso curso : cursos) {
                // Obtener el Flyweight del pool (reutilizado si existe)
                String tipoCurso = curso.getTipoCurso() != null ? curso.getTipoCurso() : "virtual";
                RecursoVisualFlyweight recurso = factory.obtenerRecurso(tipoCurso);

                // Obtener nombre del profesor
                String nombreProfesor = "Sin asignar";
                if (curso.getProfesorTitularId() != null) {
                    Optional<Usuario> usuario = usuarioRepository.findById(curso.getProfesorTitularId());
                    if (usuario.isPresent()) {
                        nombreProfesor = usuario.get().getNombre();
                    }
                }

                // Contar inscripciones
                List<Inscripcion> inscripciones = inscripcionRepository.findByCursoId(curso.getId());
                int numEstudiantes = inscripciones != null ? inscripciones.size() : 0;

                // Crear el contexto con datos únicos del curso (estado extrínseco)
                ContextoCurso contexto = new ContextoCurso(
                    curso.getId(),
                    curso.getNombre(),
                    curso.getDescripcion(),
                    curso.getDuracion(),
                    nombreProfesor,
                    numEstudiantes,
                    curso.getPeriodoAcademico() != null ? curso.getPeriodoAcademico() : "2024-1"
                );

                // Renderizar usando el Flyweight (combina estado intrínseco + extrínseco)
                String html = recurso.renderizar(contexto);
                RecursoInfo info = recurso.obtenerInfo();

                Map<String, Object> cursoData = new HashMap<>();
                cursoData.put("cursoId", curso.getId());
                cursoData.put("nombre", curso.getNombre());
                cursoData.put("tipo", tipoCurso);
                cursoData.put("html", html);
                cursoData.put("recursoCompartido", info);

                cursosRenderizados.add(cursoData);
            }

            result.put("patron", "Flyweight");
            result.put("totalCursos", cursos.size());
            result.put("cursos", cursosRenderizados);
            result.put("descripcion", "Los recursos visuales (iconos, colores, plantillas) se reutilizan entre cursos del mismo tipo");

        } catch (Exception e) {
            result.put("error", "Error al renderizar cursos: " + e.getMessage());
        }

        return result;
    }

    /**
     * Obtiene estadísticas del pool de Flyweights
     * Muestra cuántos objetos se reutilizaron y cuánta memoria se ahorró
     */
    @GetMapping("/flyweight/estadisticas")
    public Map<String, Object> obtenerEstadisticasFlyweight() {
        Map<String, Object> result = new HashMap<>();

        try {
            RecursoVisualFactory factory = RecursoVisualFactory.getInstance();
            Map<String, Object> stats = factory.obtenerEstadisticas();

            result.put("patron", "Flyweight");
            result.put("estadisticas", stats);
            result.put("explicacion", "Flyweight ahorra memoria compartiendo objetos inmutables entre múltiples contextos");
            result.put("beneficio", "Sin Flyweight se crearían N objetos para N cursos. Con Flyweight solo se crean 3 objetos (uno por tipo) sin importar cuántos cursos haya");

        } catch (Exception e) {
            result.put("error", "Error al obtener estadísticas: " + e.getMessage());
        }

        return result;
    }

    /**
     * Renderiza un curso específico usando Flyweight
     */
    @GetMapping("/flyweight/renderizar-curso/{id}")
    public Map<String, Object> renderizarCursoConFlyweight(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();

        Optional<Curso> cursoOpt = cursoRepository.findById(id);
        if (cursoOpt.isEmpty()) {
            result.put("error", "Curso no encontrado");
            return result;
        }

        Curso curso = cursoOpt.get();
        RecursoVisualFactory factory = RecursoVisualFactory.getInstance();

        String tipoCurso = curso.getTipoCurso() != null ? curso.getTipoCurso() : "virtual";
        RecursoVisualFlyweight recurso = factory.obtenerRecurso(tipoCurso);

        // Obtener nombre del profesor
        String nombreProfesor = "Sin asignar";
        if (curso.getProfesorTitularId() != null) {
            Optional<Usuario> usuario = usuarioRepository.findById(curso.getProfesorTitularId());
            if (usuario.isPresent()) {
                nombreProfesor = usuario.get().getNombre();
            }
        }

        // Contar inscripciones
        List<Inscripcion> inscripciones = inscripcionRepository.findByCursoId(curso.getId());
        int numEstudiantes = inscripciones != null ? inscripciones.size() : 0;

        ContextoCurso contexto = new ContextoCurso(
            curso.getId(),
            curso.getNombre(),
            curso.getDescripcion(),
            curso.getDuracion(),
            nombreProfesor,
            numEstudiantes,
            curso.getPeriodoAcademico() != null ? curso.getPeriodoAcademico() : "2024-1"
        );

        String html = recurso.renderizar(contexto);
        RecursoInfo info = recurso.obtenerInfo();

        result.put("patron", "Flyweight");
        result.put("cursoId", curso.getId());
        result.put("nombre", curso.getNombre());
        result.put("tipo", tipoCurso);
        result.put("html", html);
        result.put("estadoIntrínseco", info);
        result.put("estadoExtrínseco", contexto);

        return result;
    }
}
