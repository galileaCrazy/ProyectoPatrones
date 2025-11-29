package com.edulearn.patterns.flyweight;

import java.util.HashMap;
import java.util.Map;

/**
 * PATRÓN FLYWEIGHT - Flyweight Factory
 * Gestiona la creación y reutilización de objetos Flyweight
 * Asegura que los Flyweights se compartan correctamente
 */
public class RecursoVisualFactory {
    // Pool de objetos Flyweight compartidos
    private static final Map<String, RecursoVisualFlyweight> recursos = new HashMap<>();

    // Singleton para la fábrica
    private static RecursoVisualFactory instance;

    private RecursoVisualFactory() {}

    public static RecursoVisualFactory getInstance() {
        if (instance == null) {
            instance = new RecursoVisualFactory();
        }
        return instance;
    }

    /**
     * Obtiene un Flyweight del pool o lo crea si no existe
     * @param tipoCurso Tipo de curso (virtual, presencial, hibrido)
     * @return Flyweight reutilizable
     */
    public RecursoVisualFlyweight obtenerRecurso(String tipoCurso) {
        String key = tipoCurso.toLowerCase();

        // Si ya existe, reutiliza el objeto (CLAVE DEL PATRÓN FLYWEIGHT)
        if (recursos.containsKey(key)) {
            System.out.println("♻️ FLYWEIGHT: Reutilizando recurso existente para tipo: " + tipoCurso);
            return recursos.get(key);
        }

        // Si no existe, crea uno nuevo y lo almacena
        RecursoVisualFlyweight recurso;
        switch (key) {
            case "virtual":
                recurso = new RecursoVirtualFlyweight();
                break;
            case "presencial":
                recurso = new RecursoPresencialFlyweight();
                break;
            case "hibrido":
            case "híbrido":
                recurso = new RecursoHibridoFlyweight();
                break;
            default:
                // Por defecto, virtual
                recurso = new RecursoVirtualFlyweight();
                key = "virtual";
        }

        recursos.put(key, recurso);
        System.out.println("🆕 FLYWEIGHT: Creando nuevo recurso para tipo: " + tipoCurso);

        return recurso;
    }

    /**
     * Obtiene estadísticas del pool de Flyweights
     */
    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalRecursosEnPool", recursos.size());
        stats.put("tiposDisponibles", recursos.keySet());

        Map<String, Integer> usosPorTipo = new HashMap<>();
        for (Map.Entry<String, RecursoVisualFlyweight> entry : recursos.entrySet()) {
            RecursoInfo info = entry.getValue().obtenerInfo();
            usosPorTipo.put(entry.getKey(), info.getVecesReutilizado());
        }
        stats.put("usosPorTipo", usosPorTipo);

        // Calcular memoria ahorrada
        int totalUsos = usosPorTipo.values().stream().mapToInt(Integer::intValue).sum();
        int objetosCreados = recursos.size();
        int memoriaAhorrada = totalUsos - objetosCreados;
        stats.put("totalRenderizados", totalUsos);
        stats.put("objetosCreados", objetosCreados);
        stats.put("memoriaAhorrada", memoriaAhorrada + " objetos no creados gracias a Flyweight");

        return stats;
    }

    /**
     * Limpia el pool (útil para testing)
     */
    public void limpiarPool() {
        recursos.clear();
        System.out.println("🧹 FLYWEIGHT: Pool limpiado");
    }
}
