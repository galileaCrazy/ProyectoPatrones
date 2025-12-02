package com.edulearn.service;

import com.edulearn.patterns.estructural.composite.CursoTreeBuilder;
import com.edulearn.patterns.estructural.composite.ModuloCompuesto;
import com.edulearn.patterns.estructural.composite.dto.ComponenteCursoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar el árbol de módulos usando el patrón Composite
 */
@Service
public class CursoTreeService {

    @Autowired
    private CursoTreeBuilder treeBuilder;

    /**
     * Obtiene el árbol completo de un curso
     */
    public ComponenteCursoDTO obtenerArbolCurso(Integer cursoId) {
        ModuloCompuesto arbol = treeBuilder.construirArbolCurso(cursoId);
        return ComponenteCursoDTO.fromComponente(arbol);
    }

    /**
     * Obtiene todos los módulos raíz con sus subárboles
     */
    public List<ComponenteCursoDTO> obtenerModulosRaiz(Integer cursoId) {
        List<ModuloCompuesto> modulos = treeBuilder.construirArbolesModulos(cursoId);
        return modulos.stream()
            .map(ComponenteCursoDTO::fromComponente)
            .collect(Collectors.toList());
    }
}
