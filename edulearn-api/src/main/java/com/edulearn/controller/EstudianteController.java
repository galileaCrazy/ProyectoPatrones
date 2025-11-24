package com.edulearn.controller;

import com.edulearn.model.Estudiante;
import com.edulearn.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {
    @Autowired
    private EstudianteRepository estudianteRepository;

    @GetMapping
    public List<Estudiante> getAll() {
        return estudianteRepository.findAll();
    }

    @GetMapping("/{id}")
    public Estudiante getById(@PathVariable Integer id) {
        return estudianteRepository.findById(id).orElse(null);
    }

    @PostMapping
    public Estudiante create(@RequestBody Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    @PutMapping("/{id}")
    public Estudiante update(@PathVariable Integer id, @RequestBody Estudiante estudiante) {
        estudiante.setId(id);
        return estudianteRepository.save(estudiante);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        estudianteRepository.deleteById(id);
    }
}
