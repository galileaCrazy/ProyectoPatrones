package com.edulearn.patterns.comportamiento.template_method;

import com.edulearn.model.Curso;
import com.edulearn.model.Estudiante;
import com.edulearn.patterns.comportamiento.template_method.dto.ResultadoPaso;
import com.edulearn.patterns.comportamiento.template_method.dto.SolicitudInscripcion;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Implementación concreta para inscripciones con beca
 * 
 * Esta clase implementa el proceso de inscripción para estudiantes becados,
 * incluyendo validación de documentos de beca, verificación de elegibilidad
 * y generación de certificado de beca.
 */
@Component("inscripcionBeca")
public class InscripcionBeca extends ProcesoInscripcionTemplate {
    
    @Override
    protected String getTipoInscripcion() {
        return "BECA";
    }
    
    @Override
    public String getDescripcion() {
        return "Proceso de inscripción para estudiantes becados. " +
               "Incluye validación de documentos de beca, verificación de elegibilidad " +
               "y generación de certificado de beca.";
    }
    
    @Override
    public List<String> getPasosEspecificos() {
        return Arrays.asList(
            "Validar requisitos previos",
            "Verificar disponibilidad de cupo",
            "Validar documentación de beca",
            "Verificar elegibilidad de beca",
            "Aplicar beneficios de beca",
            "Registrar inscripción",
            "Enviar notificaciones",
            "Generar certificado de beca"
        );
    }
    
    @Override
    protected ResultadoPaso validarDocumentacion(Estudiante estudiante, SolicitudInscripcion solicitud) {
        ResultadoPaso paso = new ResultadoPaso("Validación de documentación de beca");
        
        // Validar aceptación de términos
        if (!solicitud.isAceptaTerminos()) {
            paso.setExitoso(false);
            paso.setMensaje("Debe aceptar los términos y condiciones");
            return paso;
        }
        
        // Validar tipo de beca
        if (solicitud.getTipoBeca() == null || solicitud.getTipoBeca().isEmpty()) {
            paso.setExitoso(false);
            paso.setMensaje("Debe especificar el tipo de beca");
            return paso;
        }
        
        // Validar código de beca
        if (solicitud.getCodigoBeca() == null || solicitud.getCodigoBeca().isEmpty()) {
            paso.setExitoso(false);
            paso.setMensaje("Debe proporcionar el código de beca asignado");
            return paso;
        }
        
        // Validar documento de soporte (opcional pero recomendado)
        boolean tieneDocumentoSoporte = solicitud.getDocumentoSoporte() != null && 
                                        !solicitud.getDocumentoSoporte().isEmpty();
        
        paso.setExitoso(true);
        paso.setMensaje("Documentación de beca validada correctamente");
        paso.agregarDetalle("tipoBeca", solicitud.getTipoBeca());
        paso.agregarDetalle("codigoBeca", solicitud.getCodigoBeca());
        paso.agregarDetalle("documentoSoporte", tieneDocumentoSoporte ? "Proporcionado" : "No proporcionado");
        paso.agregarDetalle("fechaValidacion", LocalDateTime.now().toString());
        
        return paso;
    }
    
    @Override
    protected ResultadoPaso procesarAspectoEconomico(
            Estudiante estudiante, Curso curso, SolicitudInscripcion solicitud) {
        ResultadoPaso paso = new ResultadoPaso("Verificación de elegibilidad de beca");
        
        String codigoBeca = solicitud.getCodigoBeca();
        String tipoBeca = solicitud.getTipoBeca();
        
        // Simular validación de beca en el sistema
        // En un sistema real, consultaríamos la base de datos de becas
        BecaInfo becaInfo = validarBeca(codigoBeca, tipoBeca);
        
        if (!becaInfo.esValida) {
            paso.setExitoso(false);
            paso.setMensaje("La beca no es válida o no está vigente: " + becaInfo.mensajeError);
            return paso;
        }
        
        // Verificar promedio mínimo para becas académicas
        if ("ACADEMICA".equalsIgnoreCase(tipoBeca)) {
            Double promedio = estudiante.getPromedioGeneral();
            if (promedio == null || promedio < 8.0) {
                paso.setExitoso(false);
                paso.setMensaje("El promedio general debe ser mínimo 8.0 para beca académica");
                paso.agregarDetalle("promedioActual", promedio != null ? promedio.toString() : "No disponible");
                paso.agregarDetalle("promedioRequerido", "8.0");
                return paso;
            }
        }
        
        paso.setExitoso(true);
        paso.setMensaje("Elegibilidad de beca verificada - " + becaInfo.porcentajeCobertura + "% de cobertura");
        paso.agregarDetalle("codigoBeca", codigoBeca);
        paso.agregarDetalle("tipoBeca", tipoBeca);
        paso.agregarDetalle("porcentajeCobertura", String.valueOf(becaInfo.porcentajeCobertura));
        paso.agregarDetalle("montoExento", becaInfo.montoExento.toString());
        paso.agregarDetalle("estadoBeca", "VIGENTE");
        
        return paso;
    }
    
    @Override
    protected ResultadoPaso aplicarBeneficios(
            Estudiante estudiante, Curso curso, SolicitudInscripcion solicitud) {
        ResultadoPaso paso = new ResultadoPaso("Aplicación de beneficios de beca");
        
        String tipoBeca = solicitud.getTipoBeca();
        Integer porcentaje = solicitud.getPorcentajeBeca() != null ? solicitud.getPorcentajeBeca() : 100;
        
        // Calcular beneficios según tipo de beca
        List<String> beneficiosAdicionales = getBeneficiosPorTipo(tipoBeca);
        
        paso.setExitoso(true);
        paso.setMensaje("Beneficios de beca aplicados exitosamente");
        paso.agregarDetalle("tipoBeca", tipoBeca);
        paso.agregarDetalle("porcentajeDescuento", porcentaje + "%");
        paso.agregarDetalle("beneficiosAdicionales", String.join(", ", beneficiosAdicionales));
        paso.agregarDetalle("fechaAplicacion", LocalDateTime.now().toString());
        
        return paso;
    }
    
    @Override
    protected ResultadoPaso generarDocumentos(
            Estudiante estudiante, Curso curso, SolicitudInscripcion solicitud) {
        ResultadoPaso paso = new ResultadoPaso("Generación de certificado de beca");
        
        String numeroCertificado = "CERT-BECA-" + LocalDateTime.now().getYear() + "-" +
                                   UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        String numeroComprobante = "INS-B-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        paso.setExitoso(true);
        paso.setMensaje("Certificado de beca y comprobante generados exitosamente");
        paso.agregarDetalle("numeroCertificado", numeroCertificado);
        paso.agregarDetalle("numeroComprobante", numeroComprobante);
        paso.agregarDetalle("tipoBeca", solicitud.getTipoBeca());
        paso.agregarDetalle("urlCertificado", "/api/inscripciones/certificados/" + numeroCertificado);
        paso.agregarDetalle("urlComprobante", "/api/inscripciones/documentos/" + numeroComprobante);
        
        return paso;
    }
    
    /**
     * Las inscripciones con beca requieren validación adicional
     */
    @Override
    protected boolean requiereValidacionAdicional() {
        return true;
    }
    
    /**
     * Las becas pueden generar certificado inmediato
     */
    @Override
    protected boolean generaCertificadoInmediato() {
        return true;
    }
    
    // Métodos auxiliares
    
    private BecaInfo validarBeca(String codigo, String tipo) {
        BecaInfo info = new BecaInfo();
        
        // Simular validación de becas
        // En producción, esto consultaría la base de datos
        if (codigo != null && codigo.startsWith("BECA-")) {
            info.esValida = true;
            
            switch (tipo.toUpperCase()) {
                case "ACADEMICA":
                    info.porcentajeCobertura = 100;
                    info.montoExento = new BigDecimal("500.00");
                    break;
                case "DEPORTIVA":
                    info.porcentajeCobertura = 75;
                    info.montoExento = new BigDecimal("375.00");
                    break;
                case "SOCIECONOMICA":
                    info.porcentajeCobertura = 80;
                    info.montoExento = new BigDecimal("400.00");
                    break;
                case "CULTURAL":
                    info.porcentajeCobertura = 50;
                    info.montoExento = new BigDecimal("250.00");
                    break;
                default:
                    info.porcentajeCobertura = 50;
                    info.montoExento = new BigDecimal("250.00");
            }
        } else {
            info.esValida = false;
            info.mensajeError = "Código de beca no reconocido. Debe comenzar con 'BECA-'";
        }
        
        return info;
    }
    
    private List<String> getBeneficiosPorTipo(String tipoBeca) {
        switch (tipoBeca.toUpperCase()) {
            case "ACADEMICA":
                return Arrays.asList(
                    "100% exención de matrícula",
                    "Acceso a biblioteca premium",
                    "Material didáctico incluido",
                    "Tutoría personalizada"
                );
            case "DEPORTIVA":
                return Arrays.asList(
                    "75% exención de matrícula",
                    "Horarios flexibles para entrenamientos",
                    "Acceso a instalaciones deportivas"
                );
            case "SOCIECONOMICA":
                return Arrays.asList(
                    "80% exención de matrícula",
                    "Material didáctico incluido",
                    "Acceso a comedor estudiantil"
                );
            case "CULTURAL":
                return Arrays.asList(
                    "50% exención de matrícula",
                    "Acceso a eventos culturales",
                    "Participación en actividades artísticas"
                );
            default:
                return Arrays.asList("Beneficios básicos de beca");
        }
    }
    
    // Clase interna para información de beca
    private static class BecaInfo {
        boolean esValida;
        int porcentajeCobertura;
        BigDecimal montoExento = BigDecimal.ZERO;
        String mensajeError;
    }
}
