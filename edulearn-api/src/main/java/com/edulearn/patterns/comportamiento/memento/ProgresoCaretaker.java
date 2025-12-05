package com.edulearn.patterns.comportamiento.memento;

import java.util.*;

/**
 * Caretaker: Gestiona los mementos (historial de estados)
 * Permite guardar múltiples puntos de restauración
 */
public class ProgresoCaretaker {
    private final Map<String, Stack<ProgresoMemento>> historial;
    private final int maxHistorial;

    public ProgresoCaretaker() {
        this(10); // Máximo 10 estados guardados por defecto
    }

    public ProgresoCaretaker(int maxHistorial) {
        this.historial = new HashMap<>();
        this.maxHistorial = maxHistorial;
    }

    /**
     * Guarda un memento en el historial
     */
    public void guardar(ProgresoMemento memento) {
        String key = generarKey(memento.getEstudianteId(), memento.getCursoId());

        historial.putIfAbsent(key, new Stack<>());
        Stack<ProgresoMemento> stack = historial.get(key);

        // Limitar el tamaño del historial
        if (stack.size() >= maxHistorial) {
            stack.remove(0); // Eliminar el más antiguo
        }

        stack.push(memento);
    }

    /**
     * Obtiene el último memento guardado
     */
    public ProgresoMemento obtenerUltimo(Integer estudianteId, Integer cursoId) {
        String key = generarKey(estudianteId, cursoId);
        Stack<ProgresoMemento> stack = historial.get(key);

        if (stack == null || stack.isEmpty()) {
            return null;
        }

        return stack.peek();
    }

    /**
     * Restaura y elimina el último memento (deshacer)
     */
    public ProgresoMemento deshacer(Integer estudianteId, Integer cursoId) {
        String key = generarKey(estudianteId, cursoId);
        Stack<ProgresoMemento> stack = historial.get(key);

        if (stack == null || stack.isEmpty()) {
            return null;
        }

        return stack.pop();
    }

    /**
     * Obtiene un memento específico por índice (0 = más antiguo)
     */
    public ProgresoMemento obtenerPorIndice(Integer estudianteId, Integer cursoId, int indice) {
        String key = generarKey(estudianteId, cursoId);
        Stack<ProgresoMemento> stack = historial.get(key);

        if (stack == null || indice < 0 || indice >= stack.size()) {
            return null;
        }

        return stack.get(indice);
    }

    /**
     * Obtiene todo el historial de un estudiante en un curso
     */
    public List<ProgresoMemento> obtenerHistorial(Integer estudianteId, Integer cursoId) {
        String key = generarKey(estudianteId, cursoId);
        Stack<ProgresoMemento> stack = historial.get(key);

        if (stack == null) {
            return Collections.emptyList();
        }

        return new ArrayList<>(stack);
    }

    /**
     * Limpia el historial de un estudiante en un curso
     */
    public void limpiarHistorial(Integer estudianteId, Integer cursoId) {
        String key = generarKey(estudianteId, cursoId);
        historial.remove(key);
    }

    /**
     * Obtiene el número de estados guardados
     */
    public int cantidadEstados(Integer estudianteId, Integer cursoId) {
        String key = generarKey(estudianteId, cursoId);
        Stack<ProgresoMemento> stack = historial.get(key);
        return (stack == null) ? 0 : stack.size();
    }

    private String generarKey(Integer estudianteId, Integer cursoId) {
        return estudianteId + "_" + cursoId;
    }

    /**
     * Obtiene todas las claves de estudiantes con historial guardado
     */
    public Set<String> obtenerTodasLasClaves() {
        return historial.keySet();
    }
}
