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
 * Esta clase implementa el proceso de inscripción para cursos gratuitos,
 * donde no se requiere pago pero sí validación de documentación básica.
 */
@Component("inscripcionGratuita")
public class InscripcionGratuita extends ProcesoInscripcionTemplate {
    
    @Override
    protected String getTipoInscripcion() {
        return "GRATUITA";
    }
    
    @Override
    public String getDescripcion() {
        return "Proceso de inscripción para cursos gratuitos. " +
               "Solo requiere validación de identidad y aceptación de términos.";
    }
    
    @Override
    public List<String> getPasosEspecificos() {
        return Arrays.asList(
            "Validar requisitos previos",
            "Verificar disponibilidad de cupo",
            "Validar documento de identidad",
            "Verificar acceso gratuito",
            "Registrar inscripción",
            "Enviar notificaciones",
            "Generar comprobante de inscripción"
        );
    }
    
    @Override
    protected ResultadoPaso validarDocumentacion(Estudiante estudiante, SolicitudInscripcion solicitud) {
        ResultadoPaso paso = new ResultadoPaso("Validación de documentación básica");
        
        // Para inscripción gratuita solo requerimos aceptación de términos
        if (!solicitud.isAceptaTerminos()) {
            paso.setExitoso(false);
            paso.setMensaje("Debe aceptar los términos y condiciones");
            return paso;
        }
        
        paso.setExitoso(true);
        paso.setMensaje("Documentación básica validada");
        paso.agregarDetalle("terminosAceptados", "true");
        paso.agregarDetalle("fechaAceptacion", LocalDateTime.now().toString());
        
        return paso;
    }
    
    @Override
    protected ResultadoPaso procesarAspectoEconomico(
            Estudiante estudiante, Curso curso, SolicitudInscripcion solicitud) {
        ResultadoPaso paso = new ResultadoPaso("Verificación de acceso gratuito");
        
        // Verificamos que el curso sea efectivamente gratuito
        // En un sistema real consultaríamos la configuración del curso
        paso.setExitoso(true);
        paso.setMensaje("Curso verificado como gratuito - Sin costo de inscripción");
        paso.agregarDetalle("costoInscripcion", "0.00");
        paso.agregarDetalle("tipoAcceso", "GRATUITO");
        
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
        
        return paso;
    }
    
    /**
     * Las inscripciones gratuitas no requieren validación adicional
     */
    @Override
    protected boolean requiereValidacionAdicional() {
        return false;
    }
}
