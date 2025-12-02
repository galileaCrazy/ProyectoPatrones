package com.edulearn.patterns.structural.bridge;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Generador de PDF real usando Apache PDFBox
 */
public class GeneradorPDF {

    public static byte[] generarPDF(String titulo, Map<String, Object> datos) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float yPosition = 750;
                float margin = 50;
                float fontSize = 12;
                float titleFontSize = 18;

                // Título
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), titleFontSize);
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, yPosition);
                contentStream.showText(titulo);
                contentStream.endText();

                yPosition -= 30;

                // Línea separadora
                contentStream.setLineWidth(1f);
                contentStream.moveTo(margin, yPosition);
                contentStream.lineTo(PDRectangle.A4.getWidth() - margin, yPosition);
                contentStream.stroke();

                yPosition -= 30;

                // Contenido
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), fontSize);

                for (Map.Entry<String, Object> entry : datos.entrySet()) {
                    if (yPosition < 50) {
                        // Nueva página si no hay espacio
                        contentStream.close();
                        page = new PDPage(PDRectangle.A4);
                        document.addPage(page);
                        PDPageContentStream newStream = new PDPageContentStream(document, page);
                        yPosition = 750;
                        newStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), fontSize);
                    }

                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);

                    String clave = entry.getKey();
                    String valor = String.valueOf(entry.getValue());

                    // Clave en negrita
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), fontSize);
                    contentStream.showText(clave + ": ");

                    // Valor en normal
                    contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), fontSize);
                    contentStream.showText(valor);

                    contentStream.endText();

                    yPosition -= 20;
                }

                // Pie de página
                yPosition -= 30;
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_OBLIQUE), 10);
                contentStream.beginText();
                contentStream.newLineAtOffset(margin, 30);
                contentStream.showText("Generado por EduLearn Platform - Formato: PDF");
                contentStream.endText();
            }

            // Convertir a bytes
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }
}
