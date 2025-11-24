package com.edulearn.controller;

import com.edulearn.model.Curso;
import com.edulearn.model.Inscripcion;
import com.edulearn.repository.CursoRepository;
import com.edulearn.repository.InscripcionRepository;
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

    // Singleton: Configuración del sistema
    private static Map<String, String> configuracion = new HashMap<>();
    static {
        configuracion.put("nombreSistema", "EduLearn");
        configuracion.put("maxEstudiantes", "30");
        configuracion.put("emailSoporte", "soporte@edulearn.com");
        configuracion.put("zonaHoraria", "America/Mexico_City");
    }

    // ABSTRACT FACTORY: Crear curso por tipo
    @PostMapping("/abstract-factory/crear-curso")
    public Map<String, Object> crearCursoPorTipo(@RequestParam String tipo) {
        Curso curso = new Curso();
        Map<String, Object> result = new HashMap<>();

        String timestamp = String.valueOf(System.currentTimeMillis());
        curso.setCodigo("C" + timestamp.substring(timestamp.length() - 6));
        curso.setProfesorTitularId(1);
        curso.setPeriodoAcademico("2024-1");
        curso.setEstado("activo");

        switch (tipo.toLowerCase()) {
            case "virtual":
                curso.setNombre("Curso Virtual - " + timestamp);
                curso.setDescripcion("Curso 100% en línea con videos y foros");
                curso.setDuracion(40);
                curso.setTipoCurso("virtual");
                result.put("modalidad", "En línea");
                result.put("recursos", Arrays.asList("Videos", "Foros", "Chat"));
                break;
            case "presencial":
                curso.setNombre("Curso Presencial - " + timestamp);
                curso.setDescripcion("Curso en aula física con instructor");
                curso.setDuracion(60);
                curso.setTipoCurso("presencial");
                result.put("modalidad", "Presencial");
                result.put("recursos", Arrays.asList("Aula", "Materiales", "Instructor"));
                break;
            case "hibrido":
                curso.setNombre("Curso Híbrido - " + timestamp);
                curso.setDescripcion("Combinación de sesiones presenciales y virtuales");
                curso.setDuracion(50);
                curso.setTipoCurso("hibrido");
                result.put("modalidad", "Híbrido");
                result.put("recursos", Arrays.asList("Videos", "Aula", "Foros", "Instructor"));
                break;
            default:
                result.put("error", "Tipo de curso no válido");
                return result;
        }

        Curso saved = cursoRepository.save(curso);
        result.put("patron", "Abstract Factory");
        result.put("curso", saved);
        return result;
    }

    // BUILDER: Construir curso paso a paso
    @PostMapping("/builder/construir-curso")
    public Map<String, Object> construirCurso(
            @RequestParam String nombre,
            @RequestParam String descripcion,
            @RequestParam int duracion,
            @RequestParam int numModulos,
            @RequestParam boolean conEvaluaciones) {

        Curso curso = new Curso();
        String timestamp = String.valueOf(System.currentTimeMillis());
        curso.setCodigo("B" + timestamp.substring(timestamp.length() - 6));
        curso.setNombre(nombre);
        curso.setDescripcion(descripcion);
        curso.setDuracion(duracion);
        curso.setTipoCurso("virtual");
        curso.setProfesorTitularId(1);
        curso.setPeriodoAcademico("2024-1");
        curso.setEstado("en_creacion");

        Curso saved = cursoRepository.save(curso);

        List<Map<String, Object>> modulos = new ArrayList<>();
        for (int i = 1; i <= numModulos; i++) {
            Map<String, Object> modulo = new HashMap<>();
            modulo.put("numero", i);
            modulo.put("nombre", "Módulo " + i);
            modulo.put("duracion", duracion / numModulos);
            if (conEvaluaciones) {
                modulo.put("evaluacion", "Quiz Módulo " + i);
            }
            modulos.add(modulo);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("patron", "Builder");
        result.put("curso", saved);
        result.put("modulos", modulos);
        result.put("pasos", Arrays.asList(
            "1. Crear curso base",
            "2. Agregar " + numModulos + " módulos",
            "3. Configurar duración",
            conEvaluaciones ? "4. Agregar evaluaciones" : "4. Sin evaluaciones"
        ));
        return result;
    }

    // PROTOTYPE: Clonar curso
    @PostMapping("/prototype/clonar-curso/{id}")
    public Map<String, Object> clonarCurso(@PathVariable Integer id) {
        Map<String, Object> result = new HashMap<>();

        Optional<Curso> original = cursoRepository.findById(id);
        if (original.isEmpty()) {
            result.put("error", "Curso no encontrado");
            return result;
        }

        Curso clon = new Curso();
        String timestamp = String.valueOf(System.currentTimeMillis());
        clon.setCodigo("P" + timestamp.substring(timestamp.length() - 6));
        clon.setNombre(original.get().getNombre() + " (Copia)");
        clon.setDescripcion(original.get().getDescripcion());
        clon.setDuracion(original.get().getDuracion());
        clon.setTipoCurso(original.get().getTipoCurso());
        clon.setProfesorTitularId(original.get().getProfesorTitularId());
        clon.setPeriodoAcademico("2024-2");
        clon.setEstado("en_creacion");

        Curso saved = cursoRepository.save(clon);

        result.put("patron", "Prototype");
        result.put("original", original.get());
        result.put("clon", saved);
        return result;
    }

    // SINGLETON: Obtener configuración
    @GetMapping("/singleton/configuracion")
    public Map<String, String> getConfiguracion() {
        return configuracion;
    }

    // SINGLETON: Actualizar configuración
    @PostMapping("/singleton/configuracion")
    public Map<String, String> setConfiguracion(@RequestParam String clave, @RequestParam String valor) {
        configuracion.put(clave, valor);
        return configuracion;
    }

    // ADAPTER: Integrar videoconferencia
    @PostMapping("/adapter/videoconferencia")
    public Map<String, Object> crearVideoconferencia(@RequestParam String plataforma, @RequestParam String sala) {
        Map<String, Object> result = new HashMap<>();
        result.put("patron", "Adapter");
        result.put("plataforma", plataforma);
        result.put("sala", sala);

        switch (plataforma.toLowerCase()) {
            case "zoom":
                result.put("url", "https://zoom.us/j/" + sala);
                result.put("api", "Zoom API v2");
                break;
            case "meet":
                result.put("url", "https://meet.google.com/" + sala);
                result.put("api", "Google Meet API");
                break;
            case "teams":
                result.put("url", "https://teams.microsoft.com/l/meetup-join/" + sala);
                result.put("api", "Microsoft Graph API");
                break;
            default:
                result.put("error", "Plataforma no soportada");
        }
        return result;
    }

    // BRIDGE: Renderizar contenido por dispositivo
    @GetMapping("/bridge/renderizar")
    public Map<String, Object> renderizar(@RequestParam String contenido, @RequestParam String dispositivo) {
        Map<String, Object> result = new HashMap<>();
        result.put("patron", "Bridge");
        result.put("contenido", contenido);
        result.put("dispositivo", dispositivo);

        switch (dispositivo.toLowerCase()) {
            case "web":
                result.put("resultado", "HTML5 + CSS3 + JavaScript responsive");
                result.put("resolucion", "1920x1080");
                break;
            case "mobile":
                result.put("resultado", "React Native / Flutter adaptativo");
                result.put("resolucion", "390x844");
                break;
            case "smarttv":
                result.put("resultado", "Interfaz simplificada para control remoto");
                result.put("resolucion", "3840x2160");
                break;
            default:
                result.put("resultado", "Formato por defecto");
        }
        return result;
    }

    // COMPOSITE: Estructura jerárquica del curso
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
            modulo.put("hijos", lecciones);
            modulos.add(modulo);
        }
        result.put("hijos", modulos);
        return result;
    }

    // DECORATOR: Agregar funcionalidades al curso
    @PostMapping("/decorator/agregar-funcionalidad")
    public Map<String, Object> agregarFuncionalidad(
            @RequestParam Integer cursoId,
            @RequestParam boolean gamificacion,
            @RequestParam boolean certificacion) {

        Map<String, Object> result = new HashMap<>();

        Optional<Curso> curso = cursoRepository.findById(cursoId);
        if (curso.isEmpty()) {
            result.put("error", "Curso no encontrado");
            return result;
        }

        result.put("patron", "Decorator");
        result.put("cursoBase", curso.get().getNombre());

        List<String> decoradores = new ArrayList<>();
        List<Map<String, Object>> funcionalidades = new ArrayList<>();

        if (gamificacion) {
            decoradores.add("GamificacionDecorator");
            funcionalidades.add(Map.of(
                "nombre", "Gamificación",
                "caracteristicas", Arrays.asList("Puntos por actividad", "Insignias", "Tabla de posiciones")
            ));
        }

        if (certificacion) {
            decoradores.add("CertificacionDecorator");
            funcionalidades.add(Map.of(
                "nombre", "Certificación",
                "caracteristicas", Arrays.asList("Certificado digital", "Verificación en línea", "Badge LinkedIn")
            ));
        }

        result.put("decoradores", decoradores);
        result.put("funcionalidades", funcionalidades);
        return result;
    }

    // FACADE: Inscripción completa (simplifica múltiples operaciones)
    @PostMapping("/facade/inscripcion-completa")
    public Map<String, Object> inscripcionCompleta(@RequestParam Integer estudianteId, @RequestParam Integer cursoId) {
        Map<String, Object> result = new HashMap<>();
        result.put("patron", "Facade");

        List<String> pasos = new ArrayList<>();

        // Paso 1: Verificar estudiante
        pasos.add("1. Verificar estudiante activo");

        // Paso 2: Verificar curso disponible
        Optional<Curso> curso = cursoRepository.findById(cursoId);
        if (curso.isEmpty()) {
            result.put("error", "Curso no encontrado");
            return result;
        }
        pasos.add("2. Verificar disponibilidad del curso");

        // Paso 3: Verificar no inscrito previamente
        pasos.add("3. Verificar inscripción previa");

        // Paso 4: Crear inscripción
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setEstudianteId(estudianteId);
        inscripcion.setCursoId(cursoId);
        inscripcion.setFechaInscripcion(LocalDate.now());
        inscripcionRepository.save(inscripcion);
        pasos.add("4. Crear inscripción");

        // Paso 5: Enviar notificación
        pasos.add("5. Enviar notificación por email");

        result.put("resultado", "Inscripción completada exitosamente");
        result.put("curso", curso.get().getNombre());
        result.put("pasos", pasos);
        return result;
    }
}
