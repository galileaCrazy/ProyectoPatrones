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
 * Implementación concreta para inscripciones pagas
 * 
 * Esta clase implementa el proceso de inscripción para cursos de pago,
 * incluyendo validación de pago, procesamiento de transacción y
 * generación de factura.
 */
@Component("inscripcionPaga")
public class InscripcionPaga extends ProcesoInscripcionTemplate {
    
    private static final BigDecimal PRECIO_BASE = new BigDecimal("500.00");
    
    @Override
    protected String getTipoInscripcion() {
        return "PAGA";
    }
    
    @Override
    public String getDescripcion() {
        return "Proceso de inscripción para cursos de pago. " +
               "Incluye validación de pago, procesamiento de transacción y generación de factura.";
    }
    
    @Override
    public List<String> getPasosEspecificos() {
        return Arrays.asList(
            "Validar requisitos previos",
            "Verificar disponibilidad de cupo",
            "Validar documentación completa",
            "Procesar pago",
            "Aplicar descuentos (si aplica)",
            "Registrar inscripción",
            "Enviar notificaciones",
            "Generar factura y comprobante"
        );
    }
    
    @Override
    protected ResultadoPaso validarDocumentacion(Estudiante estudiante, SolicitudInscripcion solicitud) {
        ResultadoPaso paso = new ResultadoPaso("Validación de documentación completa");
        
        // Validar aceptación de términos
        if (!solicitud.isAceptaTerminos()) {
            paso.setExitoso(false);
            paso.setMensaje("Debe aceptar los términos y condiciones");
            return paso;
        }
        
        // Validar método de pago
        if (solicitud.getMetodoPago() == null || solicitud.getMetodoPago().isEmpty()) {
            paso.setExitoso(false);
            paso.setMensaje("Debe especificar un método de pago");
            return paso;
        }
        
        paso.setExitoso(true);
        paso.setMensaje("Documentación validada correctamente");
        paso.agregarDetalle("terminosAceptados", "true");
        paso.agregarDetalle("metodoPago", solicitud.getMetodoPago());
        paso.agregarDetalle("fechaValidacion", LocalDateTime.now().toString());
        
        return paso;
    }
    
    @Override
    protected ResultadoPaso procesarAspectoEconomico(
            Estudiante estudiante, Curso curso, SolicitudInscripcion solicitud) {
        ResultadoPaso paso = new ResultadoPaso("Procesamiento de pago");
        
        BigDecimal monto = solicitud.getMonto() != null ? solicitud.getMonto() : PRECIO_BASE;
        
        // Simular procesamiento de pago
        // En un sistema real, aquí se conectaría con el gateway de pago
        String transaccionId = "TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
        
        // Simular validación de tarjeta
        String metodoPago = solicitud.getMetodoPago();
        if ("TARJETA".equalsIgnoreCase(metodoPago)) {
            if (solicitud.getNumeroTarjeta() == null || solicitud.getNumeroTarjeta().length() < 4) {
                paso.setExitoso(false);
                paso.setMensaje("Número de tarjeta inválido");
                return paso;
            }
        }
        
        paso.setExitoso(true);
        paso.setMensaje("Pago procesado exitosamente");
        paso.agregarDetalle("transaccionId", transaccionId);
        paso.agregarDetalle("monto", monto.toString());
        paso.agregarDetalle("moneda", "MXN");
        paso.agregarDetalle("metodoPago", metodoPago);
        paso.agregarDetalle("estadoPago", "APROBADO");
        paso.agregarDetalle("fechaPago", LocalDateTime.now().toString());
        
        return paso;
    }
    
    @Override
    protected ResultadoPaso aplicarBeneficios(
            Estudiante estudiante, Curso curso, SolicitudInscripcion solicitud) {
        ResultadoPaso paso = new ResultadoPaso("Aplicación de descuentos");
        
        String codigoDescuento = solicitud.getCodigoDescuento();
        
        if (codigoDescuento != null && !codigoDescuento.isEmpty()) {
            // Simular validación de código de descuento
            BigDecimal descuento = BigDecimal.ZERO;
            String tipoDescuento = "NINGUNO";
            
            switch (codigoDescuento.toUpperCase()) {
                case "PROMO10":
                    descuento = new BigDecimal("10");
                    tipoDescuento = "PORCENTAJE";
                    break;
                case "PROMO20":
                    descuento = new BigDecimal("20");
                    tipoDescuento = "PORCENTAJE";
                    break;
                case "DESC50":
                    descuento = new BigDecimal("50.00");
                    tipoDescuento = "MONTO_FIJO";
                    break;
                default:
                    paso.setExitoso(true);
                    paso.setMensaje("Código de descuento no válido");
                    return paso;
            }
            
            paso.setExitoso(true);
            paso.setMensaje("Descuento aplicado: " + codigoDescuento);
            paso.agregarDetalle("codigoDescuento", codigoDescuento);
            paso.agregarDetalle("descuento", descuento.toString());
            paso.agregarDetalle("tipoDescuento", tipoDescuento);
        } else {
            paso.setExitoso(true);
            paso.setMensaje("Sin código de descuento aplicado");
        }
        
        return paso;
    }
    
    @Override
    protected ResultadoPaso generarDocumentos(
            Estudiante estudiante, Curso curso, SolicitudInscripcion solicitud) {
        ResultadoPaso paso = new ResultadoPaso("Generación de factura y comprobante");
        
        String numeroFactura = "FAC-" + LocalDateTime.now().getYear() + "-" + 
                              UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        String numeroComprobante = "INS-P-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        paso.setExitoso(true);
        paso.setMensaje("Factura y comprobante generados exitosamente");
        paso.agregarDetalle("numeroFactura", numeroFactura);
        paso.agregarDetalle("numeroComprobante", numeroComprobante);
        paso.agregarDetalle("urlFactura", "/api/inscripciones/facturas/" + numeroFactura);
        paso.agregarDetalle("urlComprobante", "/api/inscripciones/documentos/" + numeroComprobante);
        
        return paso;
    }
    
    /**
     * Las inscripciones pagas requieren validación adicional de pago
     */
    @Override
    protected boolean requiereValidacionAdicional() {
        return true;
    }
}
