package facade;

import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN FACADE - Subsistema de Evaluación
 * Maneja las evaluaciones y calificaciones
 */
public class SubsistemaEvaluacion {

    public boolean verificarDisponibilidad(int evaluacionId) {
        System.out.println("  [Evaluación] Verificando disponibilidad...");
        return true;
    }

    public boolean verificarIntentos(int estudianteId, int evaluacionId) {
        System.out.println("  [Evaluación] Verificando intentos disponibles...");
        return true;
    }

    public double ejecutarEvaluacion(int estudianteId, int evaluacionId) {
        System.out.println("  [Evaluación] Ejecutando evaluación...");
        return 85.0; // Simulación
    }

    public void registrarResultado(int estudianteId, int evaluacionId, double calificacion) {
        System.out.println("  [Evaluación] Registrando resultado: " + calificacion);
    }

    public List<Double> obtenerCalificaciones(int estudianteId, int cursoId) {
        List<Double> calificaciones = new ArrayList<>();
        calificaciones.add(85.0);
        calificaciones.add(90.0);
        calificaciones.add(78.0);
        return calificaciones;
    }

    public double calcularPromedio(int estudianteId, int cursoId) {
        List<Double> calificaciones = obtenerCalificaciones(estudianteId, cursoId);
        return calificaciones.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
}
