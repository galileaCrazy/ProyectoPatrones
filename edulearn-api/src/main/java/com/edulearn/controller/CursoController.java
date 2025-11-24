package com.edulearn.controller;

import com.edulearn.model.Curso;
import com.edulearn.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {
    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
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
    public Curso update(@PathVariable Integer id, @RequestBody Curso curso) {
        curso.setId(id);
        return cursoRepository.save(curso);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        cursoRepository.deleteById(id);
    }
}
