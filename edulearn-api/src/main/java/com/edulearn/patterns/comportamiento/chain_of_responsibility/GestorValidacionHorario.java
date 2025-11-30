package com.edulearn.patterns.comportamiento.chain_of_responsibility;

import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * GestorConcreto4 - Validador de Horario (Ejemplo adicional)
 *
 * Cuarta validación en la cadena: verifica restricciones de horario
 * para ciertos recursos (ejemplo: mantenimiento programado, horarios de servicio)
 */
@Component
public class GestorValidacionHorario extends Gestor {

    // Horario de servicio (ejemplo: 6 AM a 11 PM)
    private static final LocalTime HORA_INICIO = LocalTime.of(6, 0);
    private static final LocalTime HORA_FIN = LocalTime.of(23, 0);

    // Recursos que requieren validación de horario (opcional)
    private static final String[] RECURSOS_CON_HORARIO = {
        "/api/admin/mantenimiento"
    };

    @Override
    protected boolean manejar(SolicitudValidacion solicitud) {
        System.out.println("[Chain of Responsibility] GestorValidacionHorario procesando...");

        String recurso = solicitud.getRecursoSolicitado();

        // Verificar si este recurso requiere validación de horario
        boolean requiereValidacionHorario = false;
        for (String recursoConHorario : RECURSOS_CON_HORARIO) {
            if (recurso.startsWith(recursoConHorario)) {
                requiereValidacionHorario = true;
                break;
            }
        }

        // Si no requiere validación de horario, aprobar
        if (!requiereValidacionHorario) {
            System.out.println("[Chain of Responsibility] Recurso sin restricción de horario");
            return true;
        }

        // Obtener hora y día actual
        LocalDateTime ahora = LocalDateTime.now();
        LocalTime horaActual = ahora.toLocalTime();
        DayOfWeek diaActual = ahora.getDayOfWeek();

        // Validar que no sea fin de semana (opcional)
        if (diaActual == DayOfWeek.SATURDAY || diaActual == DayOfWeek.SUNDAY) {
            solicitud.setMensajeError("El recurso no está disponible los fines de semana");
            solicitud.setAprobada(false);
            return false;
        }

        // Validar horario de servicio
        if (horaActual.isBefore(HORA_INICIO) || horaActual.isAfter(HORA_FIN)) {
            solicitud.setMensajeError(
                "El recurso solo está disponible entre " +
                HORA_INICIO + " y " + HORA_FIN
            );
            solicitud.setAprobada(false);
            return false;
        }

        System.out.println("[Chain of Responsibility] Horario válido: " + horaActual);
        return true; // Horario válido, continuar cadena
    }
}
