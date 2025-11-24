package decorator;

/**
 * PATRÓN DECORATOR - Concrete Decorator
 * Agrega funcionalidad de certificación al módulo
 * Incluye generación de certificados y validación
 */
public class CertificacionDecorator extends ModuloDecorator {
    private String tipoCertificado;
    private int horasAcreditadas;
    private String institucionAval;
    private boolean verificacionDigital;

    public CertificacionDecorator(IModuloEducativo modulo) {
        super(modulo);
        this.tipoCertificado = "Certificado de Completación";
        this.horasAcreditadas = 40;
        this.institucionAval = "Instituto LMS";
        this.verificacionDigital = true;
    }

    @Override
    public String getDescripcion() {
        return moduloDecorado.getDescripcion() + " [CON CERTIFICACIÓN]";
    }

    @Override
    public double getCostoAdicional() {
        // La certificación agrega un costo de $50
        return moduloDecorado.getCostoAdicional() + 50.0;
    }

    @Override
    public void mostrarInfo() {
        System.out.println("═══════════════════════════════════════");
        System.out.println("MÓDULO: " + getNombre());
        System.out.println("Descripción: " + getDescripcion());
        System.out.println("Características: " + getCaracteristicas());
        System.out.println("─────────────────────────────────────────");
        System.out.println("📜 CERTIFICACIÓN:");
        System.out.println("   • Tipo: " + tipoCertificado);
        System.out.println("   • Horas acreditadas: " + horasAcreditadas);
        System.out.println("   • Avalado por: " + institucionAval);
        System.out.println("   • Verificación digital: " + (verificacionDigital ? "Sí" : "No"));
        System.out.println("─────────────────────────────────────────");
        System.out.println("Costo adicional: $" + getCostoAdicional());
        System.out.println("═══════════════════════════════════════");
    }

    @Override
    public String getCaracteristicas() {
        return moduloDecorado.getCaracteristicas() + " + Certificación";
    }

    // Métodos específicos de certificación
    public String generarCertificado(int estudianteId, String nombreEstudiante) {
        String codigoCert = "CERT-" + System.currentTimeMillis();
        System.out.println("📜 Certificado generado para " + nombreEstudiante);
        System.out.println("   Código: " + codigoCert);
        System.out.println("   Módulo: " + getNombre());
        System.out.println("   Horas: " + horasAcreditadas);
        return codigoCert;
    }

    public boolean validarCertificado(String codigoCert) {
        // Simulación de validación
        return codigoCert.startsWith("CERT-");
    }

    public void setHorasAcreditadas(int horas) {
        this.horasAcreditadas = horas;
    }

    public void setInstitucionAval(String institucion) {
        this.institucionAval = institucion;
    }
}
