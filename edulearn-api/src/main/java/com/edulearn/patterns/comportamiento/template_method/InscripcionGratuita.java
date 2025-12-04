package com.edulearn.patterns.comportamiento.template_method;

import com.edulearn.model.Curso;
import com.edulearn.model.Estudiante;
import com.edulearn.patterns.comportamiento.template_method.dto.ResultadoPaso;
import com.edulearn.patterns.comportamiento.template_method.dto.SolicitudInscripcion;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Implementación concreta para inscripciones gratuitas
 *
 * Modalidad: GRATUITA
 * Estado: Activa (inmediato)
 * Certificado Garantizado: NO
 *
 * Validación Específica: Ninguna adicional (solo aceptación de términos)
 * Beneficio Adicional: Ninguno
 */
@Component("inscripcionGratuita")
public class InscripcionGratuita extends ProcesoInscripcionTemplate {

    @Override
    protected String getTipoInscripcion() {
        return "GRATUITA";
    }

    @Override
    protected String getEstadoInscripcion() {
        return "Activa";
    }

    @Override
    protected boolean tieneCertificadoGarantizado() {
        return false;
    }

    @Override
    public String getDescripcion() {
        return "Proceso de inscripción para cursos gratuitos. " +
               "Solo requiere aceptación de términos. Sin costo de inscripción.";
    }

    @Override
    public List<String> getPasosEspecificos() {
        return Arrays.asList(
            "Aceptar términos y condiciones",
            "Validar requisitos previos",
            "Verificar disponibilidad de cupo",
            "Verificar acceso gratuito",
            "Registrar inscripción en BD",
            "Enviar notificaciones",
            "Generar comprobante de inscripción"
        );
    }

    /**
     * PASO VARIABLE 1: Realizar validación específica
     * Para inscripción gratuita: Verificar que el acceso es gratuito
     */
    @Override
    protected ResultadoPaso realizarValidacionEspecifica(
            Estudiante estudiante, Curso curso, SolicitudInscripcion solicitud) {
        ResultadoPaso paso = new ResultadoPaso("Verificación de acceso gratuito");

        // Verificar que el curso sea efectivamente gratuito
        // En un sistema real consultaríamos la configuración del curso
        paso.setExitoso(true);
        paso.setMensaje("Curso verificado como gratuito - Sin costo de inscripción");
        paso.agregarDetalle("costoInscripcion", "0.00");
        paso.agregarDetalle("tipoAcceso", "GRATUITO");
        paso.agregarDetalle("modalidad", "GRATUITA");

        return paso;
    }

    /**
     * PASO VARIABLE 2: Otorgar beneficios adicionales
     * Para inscripción gratuita: Ningún beneficio adicional
     */
    @Override
    protected ResultadoPaso otorgarBeneficiosAdicionales(
            Estudiante estudiante, Curso curso, SolicitudInscripcion solicitud) {
        ResultadoPaso paso = new ResultadoPaso("Beneficios adicionales");

        paso.setExitoso(true);
        paso.setMensaje("Sin beneficios adicionales - Inscripción gratuita estándar");
        paso.agregarDetalle("certificadoGarantizado", "false");
        paso.agregarDetalle("beneficios", "Acceso completo al contenido del curso");

        return paso;
    }

    @Override
    protected ResultadoPaso generarDocumentos(
            Estudiante estudiante, Curso curso, SolicitudInscripcion solicitud) {
        ResultadoPaso paso = new ResultadoPaso("Generación de comprobante");

        String numeroComprobante = "INS-G-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        paso.setExitoso(true);
        paso.setMensaje("Comprobante de inscripción gratuita generado");
        paso.agregarDetalle("numeroComprobante", numeroComprobante);
        paso.agregarDetalle("tipoDocumento", "COMPROBANTE_INSCRIPCION_GRATUITA");
        paso.agregarDetalle("urlDescarga", "/api/inscripciones/documentos/" + numeroComprobante);
        paso.agregarDetalle("modalidad", "GRATUITA");

        return paso;
    }
}
