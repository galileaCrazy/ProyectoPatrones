package com.edulearn.patterns.structural.adapter;

import com.edulearn.model.IntegracionExterna;

/**
 * Factory para crear adaptadores de videoconferencia.
 * Simplifica la creación de adaptadores según el tipo de sistema.
 */
public class VideoconferenciaAdapterFactory {

    /**
     * Crea un adaptador según el tipo de sistema especificado en la integración
     */
    public static ISistemaVideoconferencia crearAdapter(IntegracionExterna integracion) {
        switch (integracion.getTipoSistema().toUpperCase()) {
            case "ZOOM":
                return new ZoomAdapter(
                    integracion.getApiKey(),
                    integracion.getApiSecret()
                );

            case "GOOGLE_MEET":
                return new GoogleMeetAdapter(
                    integracion.getApiKey()
                );

            case "MS_TEAMS":
                return new MicrosoftTeamsAdapter(
                    integracion.getApiKey(),
                    integracion.getApiSecret()
                );

            default:
                throw new IllegalArgumentException(
                    "Tipo de sistema no soportado: " + integracion.getTipoSistema()
                );
        }
    }

    /**
     * Crea un adaptador con parámetros específicos
     */
    public static ISistemaVideoconferencia crearAdapter(String tipoSistema, String apiKey, String apiSecret) {
        switch (tipoSistema.toUpperCase()) {
            case "ZOOM":
                return new ZoomAdapter(apiKey, apiSecret);

            case "GOOGLE_MEET":
                return new GoogleMeetAdapter(apiKey);

            case "MS_TEAMS":
                return new MicrosoftTeamsAdapter(apiKey, apiSecret);

            default:
                throw new IllegalArgumentException(
                    "Tipo de sistema no soportado: " + tipoSistema
                );
        }
    }
}
