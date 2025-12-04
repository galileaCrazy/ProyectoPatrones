package com.edulearn.patterns.behavioral.memento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PATRÓN MEMENTO - Caretaker
 * ===========================
 * Gestiona el historial de mementos sin conocer su contenido.
 * Permite deshacer operaciones y mantener un historial de cambios.
 */
@Component
public class CursoCaretaker {

    private static final Logger logger = LoggerFactory.getLogger(CursoCaretaker.class);

    // Historial por curso - thread-safe
    private final Map<Integer, Stack<CursoMemento>> historiales = new ConcurrentHashMap<>();

    // Límite de elementos en el historial
    private static final int MAX_HISTORY_SIZE = 20;

    /**
     * Guardar memento en el historial
     */
    public void guardarMemento(Integer cursoId, CursoMemento memento) {
        logger.info("Guardando memento para curso {}: {}", cursoId, memento.getOperacion());

        Stack<CursoMemento> historial = historiales.computeIfAbsent(cursoId, k -> new Stack<>());

        // Limitar tamaño del historial
        if (historial.size() >= MAX_HISTORY_SIZE) {
            // Remover el más antiguo
            historial.remove(0);
            logger.debug("Historial lleno, removiendo memento más antiguo");
        }

        historial.push(memento);
        logger.debug("Memento guardado. Tamaño del historial: {}", historial.size());
    }

    /**
     * Obtener el último memento (para deshacer)
     */
    public Optional<CursoMemento> obtenerUltimoMemento(Integer cursoId) {
        Stack<CursoMemento> historial = historiales.get(cursoId);

        if (historial == null || historial.isEmpty()) {
            logger.warn("No hay mementos disponibles para el curso {}", cursoId);
            return Optional.empty();
        }

        CursoMemento memento = historial.pop();
        logger.info("Recuperando memento para curso {}: {}", cursoId, memento.getOperacion());
        return Optional.of(memento);
    }

    /**
     * Ver el último memento sin removerlo
     */
    public Optional<CursoMemento> verUltimoMemento(Integer cursoId) {
        Stack<CursoMemento> historial = historiales.get(cursoId);

        if (historial == null || historial.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(historial.peek());
    }

    /**
     * Obtener el historial completo de un curso
     */
    public List<CursoMemento> obtenerHistorial(Integer cursoId) {
        Stack<CursoMemento> historial = historiales.get(cursoId);

        if (historial == null) {
            return Collections.emptyList();
        }

        return new ArrayList<>(historial);
    }

    /**
     * Obtener número de cambios en el historial
     */
    public int obtenerTamanoHistorial(Integer cursoId) {
        Stack<CursoMemento> historial = historiales.get(cursoId);
        return historial != null ? historial.size() : 0;
    }

    /**
     * Verificar si hay cambios que deshacer
     */
    public boolean hayMementosDisponibles(Integer cursoId) {
        return obtenerTamanoHistorial(cursoId) > 0;
    }

    /**
     * Limpiar historial de un curso
     */
    public void limpiarHistorial(Integer cursoId) {
        logger.info("Limpiando historial del curso {}", cursoId);
        historiales.remove(cursoId);
    }

    /**
     * Obtener estadísticas del historial
     */
    public Map<String, Object> obtenerEstadisticas(Integer cursoId) {
        Stack<CursoMemento> historial = historiales.get(cursoId);

        if (historial == null || historial.isEmpty()) {
            return Map.of(
                "cursoid", cursoId,
                "tamano", 0,
                "hayMementos", false
            );
        }

        CursoMemento ultimo = historial.peek();

        return Map.of(
            "cursoid", cursoId,
            "tamano", historial.size(),
            "hayMementos", true,
            "ultimaOperacion", ultimo.getOperacion(),
            "ultimaFecha", ultimo.getFechaCreacion(),
            "limiteHistorial", MAX_HISTORY_SIZE
        );
    }
}
