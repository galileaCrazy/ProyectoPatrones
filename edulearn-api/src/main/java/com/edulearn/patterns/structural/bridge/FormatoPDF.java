package com.edulearn.patterns.structural.bridge;

import java.util.Map;

/**
 * Concrete Implementor - Formato PDF
 */
public class FormatoPDF implements IFormatoReporte {

    @Override
    public String generarEncabezado(String titulo) {
        return "%%PDF-1.4\n" +
               "%REPORTE EDULEARN\n" +
               "1 0 obj\n" +
               "<< /Type /Catalog /Pages 2 0 R >>\n" +
               "endobj\n" +
               "TITULO: " + titulo + "\n" +
               "=" .repeat(50) + "\n";
    }

    @Override
    public String generarCuerpo(Map<String, Object> datos) {
        StringBuilder sb = new StringBuilder();
        sb.append("CONTENIDO DEL REPORTE PDF:\n\n");

        datos.forEach((clave, valor) -> {
            sb.append("  • ").append(clave).append(": ").append(valor).append("\n");
        });

        return sb.toString();
    }

    @Override
    public String generarPie() {
        return "\n" + "=" .repeat(50) + "\n" +
               "Generado por EduLearn Platform\n" +
               "Formato: PDF\n" +
               "%%EOF";
    }

    @Override
    public String obtenerTipoMIME() {
        return "application/pdf";
    }

    @Override
    public String obtenerExtension() {
        return ".pdf";
    }
}
