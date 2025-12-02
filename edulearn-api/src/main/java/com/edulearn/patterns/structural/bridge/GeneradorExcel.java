package com.edulearn.patterns.structural.bridge;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Generador de Excel real usando Apache POI
 */
public class GeneradorExcel {

    public static byte[] generarExcel(String titulo, Map<String, Object> datos) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Reporte");

            // Estilos
            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 16);
            titleFont.setColor(IndexedColors.BLUE.getIndex());
            titleStyle.setFont(titleFont);

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            // Título
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue(titulo);
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 1));

            // Espacio
            sheet.createRow(1);

            // Encabezados
            Row headerRow = sheet.createRow(2);
            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("Campo");
            headerCell1.setCellStyle(headerStyle);

            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("Valor");
            headerCell2.setCellStyle(headerStyle);

            // Datos
            int rowNum = 3;
            for (Map.Entry<String, Object> entry : datos.entrySet()) {
                Row row = sheet.createRow(rowNum++);

                Cell keyCell = row.createCell(0);
                keyCell.setCellValue(entry.getKey());
                keyCell.setCellStyle(dataStyle);

                Cell valueCell = row.createCell(1);
                valueCell.setCellValue(String.valueOf(entry.getValue()));
                valueCell.setCellStyle(dataStyle);
            }

            // Pie de página
            rowNum++;
            Row footerRow = sheet.createRow(rowNum);
            Cell footerCell = footerRow.createCell(0);
            footerCell.setCellValue("Generado por EduLearn Platform - Formato: Excel");
            CellStyle footerStyle = workbook.createCellStyle();
            Font footerFont = workbook.createFont();
            footerFont.setItalic(true);
            footerFont.setFontHeightInPoints((short) 9);
            footerStyle.setFont(footerFont);
            footerCell.setCellStyle(footerStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rowNum, rowNum, 0, 1));

            // Ajustar ancho de columnas
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);

            // Convertir a bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return baos.toByteArray();
        }
    }
}
