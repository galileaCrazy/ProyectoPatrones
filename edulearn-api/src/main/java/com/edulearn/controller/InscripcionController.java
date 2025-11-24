package com.edulearn.controller;

import com.edulearn.model.Inscripcion;
import com.edulearn.model.Estudiante;
import com.edulearn.model.Curso;
import com.edulearn.repository.InscripcionRepository;
import com.edulearn.repository.EstudianteRepository;
import com.edulearn.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionController {
    @Autowired
    private InscripcionRepository inscripcionRepository;
    @Autowired
    private EstudianteRepository estudianteRepository;
    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public List<Map<String, Object>> getAll() {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Inscripcion ins : inscripcionRepository.findAll()) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", ins.getId());
            map.put("estudianteId", ins.getEstudianteId());
            map.put("cursoId", ins.getCursoId());
            map.put("fechaInscripcion", ins.getFechaInscripcion());

            estudianteRepository.findById(ins.getEstudianteId())
                .ifPresent(e -> map.put("estudianteNombre", e.getNombre() + " " + e.getApellidos()));
            cursoRepository.findById(ins.getCursoId())
                .ifPresent(c -> map.put("cursoNombre", c.getNombre()));

            result.add(map);
        }
        return result;
    }

    @PostMapping
    public Inscripcion create(@RequestBody Inscripcion inscripcion) {
        return inscripcionRepository.save(inscripcion);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        inscripcionRepository.deleteById(id);
    }
}
