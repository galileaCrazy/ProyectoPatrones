package com.edulearn.controller;

import com.edulearn.patterns.estructural.composite.dto.ComponenteCursoDTO;
import com.edulearn.service.CursoTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller para manejar el árbol de módulos usando el patrón Composite
 * Endpoint principal: GET /api/modulos/tree
 */
@RestController
@RequestMapping("/api/modulos")
@CrossOrigin(origins = "*")
public class ModuloTreeController {

    @Autowired
    private CursoTreeService cursoTreeService;

    /**
     * GET /api/modulos/tree?cursoId={id}
     * Obtiene el árbol completo de módulos, materiales y evaluaciones de un curso
     */
    @GetMapping("/tree")
    public ResponseEntity<?> obtenerArbolCurso(@RequestParam Integer cursoId) {
        try {
            List<ComponenteCursoDTO> arbol = cursoTreeService.obtenerModulosRaiz(cursoId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("cursoId", cursoId);
            response.put("modulos", arbol);
            response.put("totalModulos", arbol.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET /api/modulos/tree/single?cursoId={id}
     * Obtiene el árbol como un único nodo raíz (útil para visualizaciones simples)
     */
    @GetMapping("/tree/single")
    public ResponseEntity<?> obtenerArbolCursoUnico(@RequestParam Integer cursoId) {
        try {
            ComponenteCursoDTO arbol = cursoTreeService.obtenerArbolCurso(cursoId);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("cursoId", cursoId);
            response.put("arbol", arbol);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
