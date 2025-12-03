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
 * Modalidad: PAGA
 * Estado: Activa (inmediato tras pago exitoso)
 * Certificado Garantizado: SÍ
 * Monto: 500 pesos (fijo para todos los cursos)
 *
 * Validación Específica: Procesar el pago y confirmarlo
 * Beneficio Adicional: Otorgar Certificado Garantizado al finalizar el curso
 */
@Component("inscripcionPaga")
public class InscripcionPaga extends ProcesoInscripcionTemplate {

    private static final BigDecimal PRECIO_FIJO = new BigDecimal("500.00");

    @Override
    protected String getTipoInscripcion() {
        return "PAGA";
    }

    @Override
    protected String getEstadoInscripcion() {
        return "Activa";
    }

    @Override
    protected boolean tieneCertificadoGarantizado() {
        return true;
    }

    @Override
    public String getDescripcion() {
        return "Proceso de inscripción para cursos de pago. " +
               "Monto fijo: 500 pesos. Incluye procesamiento de pago y " +
               "garantía de certificado al finalizar el curso.";
    }

    @Override
    public List<String> getPasosEspecificos() {
        return Arrays.asList(
            "Aceptar términos y condiciones",
            "Validar requisitos previos",
            "Verificar disponibilidad de cupo",
            "Procesar pago de 500 pesos",
            "Otorgar certificado garantizado",
            "Registrar inscripción en BD",
            "Enviar notificaciones",
            "Generar factura y comprobante"
        );
    }
    
    /**
     * PASO VARIABLE 1: Realizar validación específica
     * Para inscripción paga: Procesar el pago y confirmarlo
     */
    @Override
    protected ResultadoPaso realizarValidacionEspecifica(
            Estudiante estudiante, Curso curso, SolicitudInscripcion solicitud) {
        ResultadoPaso paso = new ResultadoPaso("Procesamiento de pago");

        // Validar método de pago
        if (solicitud.getMetodoPago() == null || solicitud.getMetodoPago().isEmpty()) {
            paso.setExitoso(false);
            paso.setMensaje("Debe especificar un método de pago válido");
            return paso;
        }

        String metodoPago = solicitud.getMetodoPago();

        // Validar datos específicos según método de pago
        if ("TARJETA".equalsIgnoreCase(metodoPago)) {
            if (solicitud.getNumeroTarjeta() == null || solicitud.getNumeroTarjeta().length() < 4) {
                paso.setExitoso(false);
                paso.setMensaje("Número de tarjeta inválido");
                return paso;
            }
        }

        // Simular procesamiento de pago con gateway
        // En producción, aquí se conectaría con el gateway de pago real
        String transaccionId = "TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();

        paso.setExitoso(true);
        paso.setMensaje("Pago procesado exitosamente por $" + PRECIO_FIJO + " MXN");
        paso.agregarDetalle("transaccionId", transaccionId);
        paso.agregarDetalle("monto", PRECIO_FIJO.toString());
        paso.agregarDetalle("moneda", "MXN");
        paso.agregarDetalle("metodoPago", metodoPago);
        paso.agregarDetalle("estadoPago", "APROBADO");
        paso.agregarDetalle("fechaPago", LocalDateTime.now().toString());

        return paso;
    }

    /**
     * PASO VARIABLE 2: Otorgar beneficios adicionales
     * Para inscripción paga: Otorgar Certificado Garantizado
     */
    @Override
    protected ResultadoPaso otorgarBeneficiosAdicionales(
            Estudiante estudiante, Curso curso, SolicitudInscripcion solicitud) {
        ResultadoPaso paso = new ResultadoPaso("Otorgamiento de certificado garantizado");

        paso.setExitoso(true);
        paso.setMensaje("Certificado garantizado otorgado - El estudiante recibirá su certificado al completar el curso");
        paso.agregarDetalle("certificadoGarantizado", "true");
        paso.agregarDetalle("beneficio", "Certificado oficial garantizado al finalizar");
        paso.agregarDetalle("requisito", "Completar el 100% del contenido del curso");

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
        paso.agregarDetalle("monto", PRECIO_FIJO.toString());
        paso.agregarDetalle("moneda", "MXN");
        paso.agregarDetalle("urlFactura", "/api/inscripciones/facturas/" + numeroFactura);
        paso.agregarDetalle("urlComprobante", "/api/inscripciones/documentos/" + numeroComprobante);
        paso.agregarDetalle("modalidad", "PAGA");

        return paso;
    }
}
