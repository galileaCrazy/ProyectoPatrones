package com.edulearn.patterns.structural.bridge;

import java.util.Map;

/**
 * Concrete Implementor - Formato HTML
 */
public class FormatoHTML implements IFormatoReporte {

    @Override
    public String generarEncabezado(String titulo) {
        return "<!DOCTYPE html>\n" +
               "<html lang=\"es\">\n" +
               "<head>\n" +
               "  <meta charset=\"UTF-8\">\n" +
               "  <title>" + titulo + "</title>\n" +
               "  <style>\n" +
               "    body { font-family: Arial, sans-serif; margin: 20px; }\n" +
               "    h1 { color: #2563eb; border-bottom: 2px solid #2563eb; padding-bottom: 10px; }\n" +
               "    table { width: 100%; border-collapse: collapse; margin-top: 20px; }\n" +
               "    th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }\n" +
               "    th { background-color: #2563eb; color: white; }\n" +
               "    tr:nth-child(even) { background-color: #f2f2f2; }\n" +
               "  </style>\n" +
               "</head>\n" +
               "<body>\n" +
               "  <h1>" + titulo + "</h1>\n";
    }

    @Override
    public String generarCuerpo(Map<String, Object> datos) {
        StringBuilder sb = new StringBuilder();
        sb.append("  <table>\n");
        sb.append("    <thead>\n");
        sb.append("      <tr><th>Campo</th><th>Valor</th></tr>\n");
        sb.append("    </thead>\n");
        sb.append("    <tbody>\n");

        datos.forEach((clave, valor) -> {
            sb.append("      <tr>\n");
            sb.append("        <td><strong>").append(clave).append("</strong></td>\n");
            sb.append("        <td>").append(valor).append("</td>\n");
            sb.append("      </tr>\n");
        });

        sb.append("    </tbody>\n");
        sb.append("  </table>\n");

        return sb.toString();
    }

    @Override
    public String generarPie() {
        return "  <footer style=\"margin-top: 30px; padding-top: 20px; border-top: 1px solid #ddd; color: #666;\">\n" +
               "    <p>Generado por EduLearn Platform | Formato: HTML</p>\n" +
               "  </footer>\n" +
               "</body>\n" +
               "</html>";
    }

    @Override
    public String obtenerTipoMIME() {
        return "text/html";
    }

    @Override
    public String obtenerExtension() {
        return ".html";
    }
}
