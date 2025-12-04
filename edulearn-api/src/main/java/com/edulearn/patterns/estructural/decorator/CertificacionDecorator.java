package com.edulearn.patterns.estructural.decorator;

/**
 * Decorator Concreto B (DecoradorConcreto)
 * Añade funcionalidades de certificación al módulo educativo.
 */
public class CertificacionDecorator extends ModuloDecorator {
    private String tipoCertificado;
    private boolean certificadoActivo;

    public CertificacionDecorator(ModuloEducativo moduloEducativo, String tipoCertificado, boolean certificadoActivo) {
        super(moduloEducativo);
        this.tipoCertificado = tipoCertificado;
        this.certificadoActivo = certificadoActivo;
    }

    @Override
    public String mostrarContenido() {
        // Invoca la operación del componente base
        String contenidoBase = super.mostrarContenido();

        // Añade la funcionalidad de certificación
        return contenidoBase + "\n" + añadirCertificacion();
    }

    private String añadirCertificacion() {
        String estado = certificadoActivo ? "ACTIVO" : "INACTIVO";
        return "\n--- CERTIFICACIÓN ---\n" +
               "Tipo de certificado: " + tipoCertificado + "\n" +
               "Estado del certificado: " + estado;
    }

    public String getTipoCertificado() {
        return tipoCertificado;
    }

    public boolean isCertificadoActivo() {
        return certificadoActivo;
    }
}
