package decorator;

/**
 * PATRÓN DECORATOR - Concrete Decorator
 * Agrega funcionalidad de tutoría personalizada al módulo
 */
public class TutoriaDecorator extends ModuloDecorator {
    private int horasTutoria;
    private String modalidad; // individual, grupal
    private boolean chatEnVivo;

    public TutoriaDecorator(IModuloEducativo modulo) {
        super(modulo);
        this.horasTutoria = 5;
        this.modalidad = "individual";
        this.chatEnVivo = true;
    }

    @Override
    public String getDescripcion() {
        return moduloDecorado.getDescripcion() + " [CON TUTORÍA]";
    }

    @Override
    public double getCostoAdicional() {
        // La tutoría agrega un costo de $30
        return moduloDecorado.getCostoAdicional() + 30.0;
    }

    @Override
    public void mostrarInfo() {
        System.out.println("═══════════════════════════════════════");
        System.out.println("MÓDULO: " + getNombre());
        System.out.println("Descripción: " + getDescripcion());
        System.out.println("Características: " + getCaracteristicas());
        System.out.println("─────────────────────────────────────────");
        System.out.println("👨‍🏫 TUTORÍA:");
        System.out.println("   • Horas incluidas: " + horasTutoria);
        System.out.println("   • Modalidad: " + modalidad);
        System.out.println("   • Chat en vivo: " + (chatEnVivo ? "Disponible" : "No disponible"));
        System.out.println("─────────────────────────────────────────");
        System.out.println("Costo adicional: $" + getCostoAdicional());
        System.out.println("═══════════════════════════════════════");
    }

    @Override
    public String getCaracteristicas() {
        return moduloDecorado.getCaracteristicas() + " + Tutoría";
    }

    // Métodos específicos de tutoría
    public void agendarTutoria(int estudianteId, String fecha) {
        System.out.println("📅 Tutoría agendada para estudiante " + estudianteId + " el " + fecha);
    }

    public void setHorasTutoria(int horas) {
        this.horasTutoria = horas;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }
}
