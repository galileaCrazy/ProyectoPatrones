package com.edulearn.patterns.estructural.composite;

import com.edulearn.model.Evaluacion;
import com.edulearn.model.Material;
import com.edulearn.model.Modulo;
import com.edulearn.repository.EvaluacionRepository;
import com.edulearn.repository.MaterialRepository;
import com.edulearn.repository.ModuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Builder para construir el árbol del curso usando el patrón Composite
 * Convierte las entidades JPA en la estructura Composite
 */
@Component
public class CursoTreeBuilder {

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    /**
     * Construye el árbol completo de un curso
     */
    public ModuloCompuesto construirArbolCurso(Integer cursoId) {
        // Obtener todos los módulos del curso
        List<Modulo> modulos = moduloRepository.findByCursoIdOrderByOrdenAsc(cursoId);

        if (modulos.isEmpty()) {
            return null;
        }

        // Crear un mapa para acceso rápido a los módulos compuestos
        Map<Long, ModuloCompuesto> mapaModulos = new HashMap<>();

        // Primera pasada: crear todos los módulos compuestos
        for (Modulo modulo : modulos) {
            ModuloCompuesto moduloCompuesto = crearModuloCompuesto(modulo);
            mapaModulos.put(modulo.getId(), moduloCompuesto);
        }

        // Segunda pasada: establecer las relaciones jerárquicas
        ModuloCompuesto raiz = null;
        for (Modulo modulo : modulos) {
            ModuloCompuesto moduloActual = mapaModulos.get(modulo.getId());

            // Agregar materiales y evaluaciones a este módulo
            agregarMateriales(moduloActual, modulo.getId());
            agregarEvaluaciones(moduloActual, modulo.getId());

            // Si tiene padre, agregarlo al padre; si no, es la raíz
            if (modulo.getModuloPadreId() != null) {
                ModuloCompuesto padre = mapaModulos.get(modulo.getModuloPadreId());
                if (padre != null) {
                    padre.agregar(moduloActual);
                }
            } else if (raiz == null) {
                // El primer módulo sin padre se considera la raíz
                raiz = moduloActual;
            } else {
                // Si hay múltiples módulos raíz, crear un contenedor
                if (raiz.getNombre().equals("Curso")) {
                    raiz.agregar(moduloActual);
                } else {
                    ModuloCompuesto contenedor = new ModuloCompuesto(
                        0L, "Curso", "CURSO", "Contenido del curso", 0, 0, "published"
                    );
                    contenedor.agregar(raiz);
                    contenedor.agregar(moduloActual);
                    raiz = contenedor;
                }
            }
        }

        return raiz;
    }

    /**
     * Construye un árbol con todos los módulos raíz (módulos sin padre)
     */
    public List<ModuloCompuesto> construirArbolesModulos(Integer cursoId) {
        List<Modulo> modulosRaiz = moduloRepository.findByCursoIdOrderByOrdenAsc(cursoId).stream()
            .filter(m -> m.getModuloPadreId() == null)
            .collect(Collectors.toList());

        return modulosRaiz.stream()
            .map(modulo -> construirSubarbol(modulo))
            .collect(Collectors.toList());
    }

    /**
     * Construye un subárbol a partir de un módulo específico
     */
    public ModuloCompuesto construirSubarbol(Modulo moduloRaiz) {
        ModuloCompuesto raiz = crearModuloCompuesto(moduloRaiz);

        // Agregar materiales y evaluaciones
        agregarMateriales(raiz, moduloRaiz.getId());
        agregarEvaluaciones(raiz, moduloRaiz.getId());

        // Agregar submódulos recursivamente
        List<Modulo> submodulos = moduloRepository.findByCursoIdOrderByOrdenAsc(moduloRaiz.getCursoId())
            .stream()
            .filter(m -> moduloRaiz.getId().equals(m.getModuloPadreId()))
            .collect(Collectors.toList());

        for (Modulo submodulo : submodulos) {
            ModuloCompuesto submoduloCompuesto = construirSubarbol(submodulo);
            raiz.agregar(submoduloCompuesto);
        }

        return raiz;
    }

    private ModuloCompuesto crearModuloCompuesto(Modulo modulo) {
        return new ModuloCompuesto(
            modulo.getId(),
            modulo.getTitulo(),
            modulo.getTipo(),
            modulo.getDescripcion(),
            modulo.getOrden(),
            modulo.getDuracionEstimada(),
            modulo.getEstado()
        );
    }

    private void agregarMateriales(ModuloCompuesto moduloCompuesto, Long moduloId) {
        List<Material> materiales = materialRepository.findByModuloIdOrderByOrdenAsc(moduloId);

        for (Material material : materiales) {
            MaterialHoja materialHoja = new MaterialHoja(
                material.getId(),
                material.getTitulo(),
                material.getTipoMaterial(),
                material.getDescripcion(),
                material.getOrden(),
                material.getUrlRecurso(),
                material.getArchivoPath(),
                material.getDuracionSegundos(),
                material.getEsObligatorio()
            );
            moduloCompuesto.agregar(materialHoja);
        }
    }

    private void agregarEvaluaciones(ModuloCompuesto moduloCompuesto, Long moduloId) {
        List<Evaluacion> evaluaciones = evaluacionRepository.findByModuloIdOrderByIdAsc(moduloId);

        for (Evaluacion evaluacion : evaluaciones) {
            EvaluacionHoja evaluacionHoja = new EvaluacionHoja(
                evaluacion.getId(),
                evaluacion.getTitulo(),
                evaluacion.getTipoEvaluacion(),
                evaluacion.getDescripcion(),
                0, // Las evaluaciones no tienen orden en la tabla
                evaluacion.getPuntajeMaximo(),
                evaluacion.getTiempoLimiteMinutos(),
                evaluacion.getIntentosPermitidos(),
                evaluacion.getEstado()
            );
            moduloCompuesto.agregar(evaluacionHoja);
        }
    }
}
