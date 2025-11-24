package decorator;

/**
 * PATRÓN DECORATOR - Concrete Decorator
 * Agrega funcionalidad de gamificación al módulo
 * Incluye puntos, insignias y rankings
 */
public class GamificacionDecorator extends ModuloDecorator {
    private int puntosBase;
    private String[] insignias;
    private boolean rankingHabilitado;

    public GamificacionDecorator(IModuloEducativo modulo) {
        super(modulo);
        this.puntosBase = 100;
        this.insignias = new String[]{"Principiante", "Intermedio", "Experto"};
        this.rankingHabilitado = true;
    }

    @Override
    public String getDescripcion() {
        return moduloDecorado.getDescripcion() + " [CON GAMIFICACIÓN]";
    }

    @Override
    public double getCostoAdicional() {
        // La gamificación agrega un costo de $15
        return moduloDecorado.getCostoAdicional() + 15.0;
    }

    @Override
    public void mostrarInfo() {
        System.out.println("═══════════════════════════════════════");
        System.out.println("MÓDULO: " + getNombre());
        System.out.println("Descripción: " + getDescripcion());
        System.out.println("Características: " + getCaracteristicas());
        System.out.println("─────────────────────────────────────────");
        System.out.println("🎮 GAMIFICACIÓN ACTIVA:");
        System.out.println("   • Puntos base por actividad: " + puntosBase);
        System.out.println("   • Insignias disponibles: " + String.join(", ", insignias));
        System.out.println("   • Ranking: " + (rankingHabilitado ? "Habilitado" : "Deshabilitado"));
        System.out.println("─────────────────────────────────────────");
        System.out.println("Costo adicional: $" + getCostoAdicional());
        System.out.println("═══════════════════════════════════════");
    }

    @Override
    public String getCaracteristicas() {
        return moduloDecorado.getCaracteristicas() + " + Gamificación";
    }

    // Métodos específicos de gamificación
    public void otorgarPuntos(int estudiante, int puntos) {
        System.out.println("🏆 Estudiante " + estudiante + " recibió " + puntos + " puntos");
    }

    public void otorgarInsignia(int estudiante, String insignia) {
        System.out.println("🎖 Estudiante " + estudiante + " obtuvo insignia: " + insignia);
    }

    public void setPuntosBase(int puntos) {
        this.puntosBase = puntos;
    }
}
