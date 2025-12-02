package com.edulearn.patterns.structural.bridge;

import java.util.Map;

/**
 * Concrete Implementor - Formato Excel
 */
public class FormatoExcel implements IFormatoReporte {

    @Override
    public String generarEncabezado(String titulo) {
        return "<?xml version=\"1.0\"?>\n" +
               "<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\">\n" +
               "<Worksheet ss:Name=\"" + titulo + "\">\n" +
               "<Table>\n" +
               "<Row><Cell><Data ss:Type=\"String\">" + titulo + "</Data></Cell></Row>\n" +
               "<Row></Row>\n";
    }

    @Override
    public String generarCuerpo(Map<String, Object> datos) {
        StringBuilder sb = new StringBuilder();

        // Encabezados
        sb.append("<Row>\n");
        sb.append("  <Cell><Data ss:Type=\"String\">Campo</Data></Cell>\n");
        sb.append("  <Cell><Data ss:Type=\"String\">Valor</Data></Cell>\n");
        sb.append("</Row>\n");

        // Datos
        datos.forEach((clave, valor) -> {
            sb.append("<Row>\n");
            sb.append("  <Cell><Data ss:Type=\"String\">").append(clave).append("</Data></Cell>\n");
            sb.append("  <Cell><Data ss:Type=\"String\">").append(valor).append("</Data></Cell>\n");
            sb.append("</Row>\n");
        });

        return sb.toString();
    }

    @Override
    public String generarPie() {
        return "</Table>\n" +
               "</Worksheet>\n" +
               "</Workbook>";
    }

    @Override
    public String obtenerTipoMIME() {
        return "application/vnd.ms-excel";
    }

    @Override
    public String obtenerExtension() {
        return ".xls";
    }
}
