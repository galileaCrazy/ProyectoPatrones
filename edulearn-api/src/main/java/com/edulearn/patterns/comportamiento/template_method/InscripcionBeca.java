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
 * Modalidad: BECA
 * Estado: Pendiente de Aprobación/Documentación (requiere validación posterior)
 * Certificado Garantizado: SÍ
 * Instituciones Elegibles: TECNM, UNAM, IPN
 *
 * Validación Específica:
 * - Validar la elegibilidad por convenio institucional (TECNM, UNAM, IPN únicamente)
 * - Establecer el estado inicial como "Pendiente de Aprobación/Documentación"
 *
 * Beneficio Adicional: Otorgar Certificado Garantizado al finalizar el curso
 */
@Component("inscripcionBeca")
public class InscripcionBeca extends ProcesoInscripcionTemplate {

    // Instituciones elegibles para becas por convenio
    private static final String CONVENIO_TECNM = "TECNM";
    private static final String CONVENIO_UNAM = "UNAM";
    private static final String CONVENIO_IPN = "IPN";

    @Override
    protected String getTipoInscripcion() {
        return "BECA";
    }

    @Override
    protected String getEstadoInscripcion() {
        return "Pendiente de Aprobación/Documentación";
    }

    @Override
    protected boolean tieneCertificadoGarantizado() {
        return true;
    }

    @Override
    public String getDescripcion() {
        return "Proceso de inscripción para estudiantes becados por convenio institucional. " +
               "Instituciones elegibles: TECNM, UNAM, IPN. " +
               "Estado inicial: Pendiente de Aprobación/Documentación. " +
               "Incluye certificado garantizado al finalizar.";
    }

    @Override
    public List<String> getPasosEspecificos() {
        return Arrays.asList(
            "Aceptar términos y condiciones",
            "Validar requisitos previos",
            "Verificar disponibilidad de cupo",
            "Validar elegibilidad por convenio institucional (TECNM, UNAM, IPN)",
            "Otorgar certificado garantizado",
            "Registrar inscripción en BD con estado pendiente",
            "Enviar notificaciones",
            "Generar certificado de beca"
        );
    }

    /**
     * PASO VARIABLE 1: Realizar validación específica
     * Para inscripción con beca: Validar elegibilidad por convenio institucional (TECNM, UNAM, IPN)
     */
    @Override
    protected ResultadoPaso realizarValidacionEspecifica(
            Estudiante estudiante, Curso curso, SolicitudInscripcion solicitud) {
        ResultadoPaso paso = new ResultadoPaso("Validación de elegibilidad por convenio institucional");

        // Validar tipo de beca
        if (solicitud.getTipoBeca() == null || solicitud.getTipoBeca().isEmpty()) {
            paso.setExitoso(false);
            paso.setMensaje("Debe especificar la institución de procedencia");
            return paso;
        }

        // Validar código de beca
        if (solicitud.getCodigoBeca() == null || solicitud.getCodigoBeca().isEmpty()) {
            paso.setExitoso(false);
            paso.setMensaje("Debe proporcionar el código de beca asignado");
            return paso;
        }

        String tipoBeca = solicitud.getTipoBeca().toUpperCase();
        String codigoBeca = solicitud.getCodigoBeca();

        // Validar si pertenece a institución con convenio
        boolean esConvenioValido = validarConvenio(tipoBeca, codigoBeca);

        if (!esConvenioValido) {
            paso.setExitoso(false);
            paso.setMensaje("La institución especificada no tiene convenio activo. " +
                          "Instituciones válidas: TECNM, UNAM, IPN");
            paso.agregarDetalle("tipoBecaRecibido", tipoBeca);
            paso.agregarDetalle("codigoBecaRecibido", codigoBeca);
            paso.agregarDetalle("institucionesValidas", "TECNM, UNAM, IPN");
            return paso;
        }

        paso.setExitoso(true);
        paso.setMensaje("Elegibilidad verificada - Convenio institucional válido con " + tipoBeca +
                       ". Estado: Pendiente de Aprobación/Documentación");
        paso.agregarDetalle("codigoBeca", codigoBeca);
        paso.agregarDetalle("tipoBeca", tipoBeca);
        paso.agregarDetalle("convenio", tipoBeca);
        paso.agregarDetalle("estadoInicial", "Pendiente de Aprobación/Documentación");
        paso.agregarDetalle("requiereDocumentacion", "true");
        paso.agregarDetalle("porcentajeCobertura", "100%");

        return paso;
    }

    /**
     * PASO VARIABLE 2: Otorgar beneficios adicionales
     * Para inscripción con beca: Otorgar Certificado Garantizado
     */
    @Override
    protected ResultadoPaso otorgarBeneficiosAdicionales(
            Estudiante estudiante, Curso curso, SolicitudInscripcion solicitud) {
        ResultadoPaso paso = new ResultadoPaso("Otorgamiento de certificado garantizado y beneficios de beca");

        String tipoBeca = solicitud.getTipoBeca();
        List<String> beneficiosAdicionales = getBeneficiosPorTipo(tipoBeca);

        paso.setExitoso(true);
        paso.setMensaje("Certificado garantizado otorgado - Beneficios de beca aplicados");
        paso.agregarDetalle("certificadoGarantizado", "true");
        paso.agregarDetalle("tipoBeca", tipoBeca);
        paso.agregarDetalle("beneficiosPrincipales", String.join(", ", beneficiosAdicionales));
        paso.agregarDetalle("exencionCosto", "100%");
        paso.agregarDetalle("requisito", "Completar documentación para activar inscripción");

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
        paso.setMensaje("Certificado de beca y comprobante generados exitosamente. " +
                       "Estado: Pendiente de documentación");
        paso.agregarDetalle("numeroCertificado", numeroCertificado);
        paso.agregarDetalle("numeroComprobante", numeroComprobante);
        paso.agregarDetalle("tipoBeca", solicitud.getTipoBeca());
        paso.agregarDetalle("codigoBeca", solicitud.getCodigoBeca());
        paso.agregarDetalle("estadoActual", "Pendiente de Aprobación/Documentación");
        paso.agregarDetalle("urlCertificado", "/api/inscripciones/certificados/" + numeroCertificado);
        paso.agregarDetalle("urlComprobante", "/api/inscripciones/documentos/" + numeroComprobante);
        paso.agregarDetalle("modalidad", "BECA");

        return paso;
    }

    // Métodos auxiliares

    /**
     * Valida si el convenio con la institución es válido
     * Solo acepta: TECNM, UNAM, IPN
     */
    private boolean validarConvenio(String tipoBeca, String codigoBeca) {
        // Validar que el código de beca tenga el formato correcto
        if (codigoBeca == null || !codigoBeca.startsWith("BECA-")) {
            return false;
        }

        // Validar instituciones con convenio - SOLO TECNM, UNAM, IPN
        String tipoBecaUpper = tipoBeca.toUpperCase();

        return tipoBecaUpper.equals(CONVENIO_TECNM) ||
               tipoBecaUpper.equals(CONVENIO_UNAM) ||
               tipoBecaUpper.equals(CONVENIO_IPN);
    }

    /**
     * Obtiene los beneficios específicos según el tipo de beca
     * Solo maneja: TECNM, UNAM, IPN
     */
    private List<String> getBeneficiosPorTipo(String tipoBeca) {
        String tipoBecaUpper = tipoBeca != null ? tipoBeca.toUpperCase() : "";

        switch (tipoBecaUpper) {
            case "TECNM":
                return Arrays.asList(
                    "100% exención de matrícula (convenio TECNM)",
                    "Certificado garantizado",
                    "Acceso completo a contenido del curso",
                    "Material didáctico incluido",
                    "Soporte técnico prioritario"
                );
            case "UNAM":
                return Arrays.asList(
                    "100% exención de matrícula (convenio UNAM)",
                    "Certificado garantizado",
                    "Acceso completo a contenido del curso",
                    "Material didáctico incluido"
                );
            case "IPN":
                return Arrays.asList(
                    "100% exención de matrícula (convenio IPN)",
                    "Certificado garantizado",
                    "Acceso completo a contenido del curso",
                    "Material didáctico incluido"
                );
            default:
                return Arrays.asList(
                    "Beneficios básicos de beca",
                    "Certificado garantizado"
                );
        }
    }
}
