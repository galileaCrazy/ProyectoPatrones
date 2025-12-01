package com.edulearn.patterns.comportamiento.chain_of_responsibility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Middleware de validación que utiliza el patrón Chain of Responsibility.
 *
 * Este componente puede ser inyectado en cualquier controller para
 * validar permisos antes de ejecutar la lógica del endpoint.
 */
@Component
public class ValidacionMiddleware {

    @Autowired
    private CadenaValidacionService cadenaValidacion;

    /**
     * Valida el acceso a un recurso utilizando la cadena de responsabilidad
     *
     * @param token Token de autenticación (puede venir del header)
     * @param recurso Recurso al que se intenta acceder
     * @param accion Acción que se intenta realizar
     * @return Respuesta con el resultado de la validación
     */
    public RespuestaValidacion validarAcceso(String token, String recurso, String accion) {
        SolicitudValidacion solicitud = cadenaValidacion.validarAcceso(token, recurso, accion);

        RespuestaValidacion respuesta = new RespuestaValidacion();
        respuesta.setAprobada(solicitud.isAprobada());
        respuesta.setMensajeError(solicitud.getMensajeError());
        respuesta.setMetadatos(solicitud.getMetadatos());

        return respuesta;
    }

    /**
     * Clase interna para encapsular la respuesta de validación
     */
    public static class RespuestaValidacion {
        private boolean aprobada;
        private String mensajeError;
        private Map<String, Object> metadatos;

        public boolean isAprobada() {
            return aprobada;
        }

        public void setAprobada(boolean aprobada) {
            this.aprobada = aprobada;
        }

        public String getMensajeError() {
            return mensajeError;
        }

        public void setMensajeError(String mensajeError) {
            this.mensajeError = mensajeError;
        }

        public Map<String, Object> getMetadatos() {
            return metadatos;
        }

        public void setMetadatos(Map<String, Object> metadatos) {
            this.metadatos = metadatos;
        }

        /**
         * Convierte la respuesta a un Map para facilitar el retorno en APIs REST
         */
        public Map<String, Object> toMap() {
            return Map.of(
                "aprobada", aprobada,
                "error", mensajeError != null ? mensajeError : "",
                "metadatos", metadatos != null ? metadatos : Map.of()
            );
        }
    }
}
