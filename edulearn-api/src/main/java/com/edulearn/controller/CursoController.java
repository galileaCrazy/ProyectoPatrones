package com.edulearn.controller;

import com.edulearn.model.Curso;
import com.edulearn.patterns.creational.builder.CursoBuilder;
import com.edulearn.patterns.creational.builder.CursoDirector;
import com.edulearn.patterns.creational.prototype.CursoPrototype;
import com.edulearn.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {
    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping         // ← sin nada = responde a /api/cursos
    public List<Curso> getAll() {
        return cursoRepository.findAll();
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
        CursoDirector director = new CursoDirector();
        Curso curso = director.construirCursoIntensivo(
            (String) params.get("nombre"),
            (Integer) params.get("profesorId")
        );
        return cursoRepository.save(curso);
    }

    /**
     * POST /api/cursos/builder/certificacion
     * Crear curso de certificación usando Director
     */
    @PostMapping("/builder/certificacion")
    public Curso crearCursoCertificacion(@RequestBody Map<String, Object> params) {
        CursoDirector director = new CursoDirector();
        Curso curso = director.construirCursoCertificacion(
            (String) params.get("nombre"),
            (Integer) params.get("profesorId"),
            (String) params.get("periodoAcademico")
        );
        return cursoRepository.save(curso);
    }

    // ========== ENDPOINTS CON PATRÓN PROTOTYPE ==========

    /**
     * POST /api/cursos/{id}/clonar
     * Clonar un curso existente usando patrón Prototype
     */
    @PostMapping("/{id}/clonar")
    public Curso clonarCurso(@PathVariable Integer id, @RequestBody(required = false) Map<String, String> params) {
        Curso cursoOriginal = cursoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + id));

        CursoPrototype prototype = new CursoPrototype(cursoOriginal);

        Curso cursoClon;
        if (params != null && (params.containsKey("nombre") || params.containsKey("periodoAcademico"))) {
            cursoClon = prototype.cloneConPersonalizacion(
                params.get("nombre"),
                params.get("periodoAcademico")
            );
        } else {
            cursoClon = prototype.clone();
        }

        return cursoRepository.save(cursoClon);
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
