package com.edulearn.patterns.comportamiento.chain_of_responsibility;

import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.Month;

/**
 * GestorConcreto4 - Validador de Periodo Académico
 *
 * Cuarta validación en la cadena: verifica que el periodo académico
 * del curso esté dentro de los rangos permitidos según la fecha actual.
 *
 * Reglas de periodos (fecha actual: 30/11/2025):
 * ┌────────────────────┬──────────────────────────────────────────────┐
 * │ Si estamos en...   │ Periodos válidos para crear cursos           │
 * ├────────────────────┼──────────────────────────────────────────────┤
 * │ Enero - Junio      │ Ago-Dic mismo año, Ene-Jun y Ago-Dic sig.   │
 * │ Julio              │ Ago-Dic mismo año, Ene-Jun y Ago-Dic sig.   │
 * │ Agosto - Diciembre │ Ene-Jun y Ago-Dic del año siguiente         │
 * └────────────────────┴──────────────────────────────────────────────┘
 *
 * Ejemplo (fecha actual: 30/11/2025):
 * - Agosto-Diciembre 2025: RECHAZADO (periodo ya en curso)
 * - Enero-Junio 2026: APROBADO ✓
 * - Agosto-Diciembre 2026: APROBADO ✓
 * 
 * Estructura del patrón (diagrama UML clásico):
 * 
 *         Gestor (abstracta)
 *            │
 *            └── GestorValidacionPeriodoAcademico ─── #siguiente ──▶ GestorAutoAsignacion
 *                      │
 *                      └── solicita(SolicitudValidacion)
 *                              │
 *                              ├── manejar() → valida periodo vs fecha actual
 *                              │
 *                              └── si válido → siguiente.solicita()
 */
@Component
public class GestorValidacionPeriodoAcademico extends Gestor {

    @Override
    protected boolean manejar(SolicitudValidacion solicitud) {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ [Gestor 4/5] GestorValidacionPeriodoAcademico               │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");

        String accion = solicitud.getAccion();

        // Solo validar para creación y edición de cursos
        if (accion == null || (!accion.equalsIgnoreCase("crear") && !accion.equalsIgnoreCase("editar"))) {
            System.out.println("   ⚠️  Acción '" + accion + "' no requiere validación de periodo");
            System.out.println("   ➡️  Pasando al siguiente gestor...\n");
            return true;
        }

        // Obtener el periodo académico de los metadatos
        Object periodoObj = solicitud.getMetadatos().get("periodoAcademico");
        
        if (periodoObj == null || periodoObj.toString().trim().isEmpty()) {
            solicitud.setMensajeError("Debe especificar un periodo académico para el curso");
            solicitud.setAprobada(false);
            System.out.println("   ❌ Periodo académico no especificado");
            return false;
        }

        String periodoAcademico = periodoObj.toString().trim();
        System.out.println("   • Periodo solicitado: " + periodoAcademico);

        LocalDate fechaActual = LocalDate.now();
        System.out.println("   • Fecha actual: " + fechaActual);
        System.out.println("   • Periodos válidos: " + String.join(", ", obtenerPeriodosValidos()));

        // Validar formato del periodo
        ResultadoValidacionPeriodo resultado = validarPeriodoAcademico(periodoAcademico);

        if (!resultado.esValido) {
            solicitud.setMensajeError(resultado.mensajeError);
            solicitud.setAprobada(false);
            System.out.println("   ❌ " + resultado.mensajeError);
            return false;
        }

        // Agregar información del periodo validado a los metadatos
        solicitud.agregarMetadato("periodoValidado", resultado.periodoNormalizado);
        solicitud.agregarMetadato("añoPeriodo", resultado.año);
        solicitud.agregarMetadato("semestrePeriodo", resultado.semestre);

        System.out.println("   ✅ Periodo académico válido: " + resultado.periodoNormalizado);
        System.out.println("   ➡️  Pasando al siguiente gestor...\n");

        return true; // Periodo válido, continuar cadena
    }

    /**
     * Valida si el periodo académico es válido según la fecha actual
     */
    private ResultadoValidacionPeriodo validarPeriodoAcademico(String periodoAcademico) {
        ResultadoValidacionPeriodo resultado = new ResultadoValidacionPeriodo();

        // Parsear el periodo académico
        String periodoNormalizado = periodoAcademico.toUpperCase().trim();
        
        // Determinar si es Enero-Junio o Agosto-Diciembre
        boolean esEneroJunio = periodoNormalizado.contains("ENERO") || 
                               periodoNormalizado.contains("ENE") ||
                               periodoNormalizado.contains("JUNIO") || 
                               periodoNormalizado.contains("JUN");
                               
        boolean esAgostoDiciembre = periodoNormalizado.contains("AGOSTO") || 
                                    periodoNormalizado.contains("AGO") ||
                                    periodoNormalizado.contains("DICIEMBRE") || 
                                    periodoNormalizado.contains("DIC");

        if (!esEneroJunio && !esAgostoDiciembre) {
            resultado.mensajeError = "El periodo académico debe ser 'Enero-Junio' o 'Agosto-Diciembre'";
            return resultado;
        }

        // Extraer el año del periodo
        int añoPeriodo;
        try {
            // Buscar un número de 4 dígitos en el string
            String[] partes = periodoNormalizado.split("\\s+");
            String añoStr = null;
            
            for (String parte : partes) {
                if (parte.matches("\\d{4}")) {
                    añoStr = parte;
                    break;
                }
            }

            if (añoStr == null) {
                resultado.mensajeError = "Debe especificar el año del periodo académico (ej: 2026)";
                return resultado;
            }

            añoPeriodo = Integer.parseInt(añoStr);

        } catch (NumberFormatException e) {
            resultado.mensajeError = "Formato de año inválido en el periodo académico";
            return resultado;
        }

        // Obtener fecha actual
        LocalDate fechaActual = LocalDate.now();
        int añoActual = fechaActual.getYear();
        Month mesActual = fechaActual.getMonth();

        // Validar según el periodo y la fecha actual
        if (esEneroJunio) {
            if (añoPeriodo < añoActual) {
                resultado.mensajeError = String.format(
                    "No puede crear cursos para periodos pasados. Periodo Enero-Junio %d ya finalizó",
                    añoPeriodo
                );
                return resultado;
            }

            if (añoPeriodo == añoActual) {
                // Si estamos en Enero-Junio del mismo año, ya no se puede crear
                if (mesActual.getValue() >= Month.JANUARY.getValue() && 
                    mesActual.getValue() <= Month.JUNE.getValue()) {
                    resultado.mensajeError = String.format(
                        "El periodo Enero-Junio %d ya está en curso. Seleccione: Agosto-Diciembre %d, Enero-Junio %d o Agosto-Diciembre %d",
                        añoActual, añoActual, añoActual + 1, añoActual + 1
                    );
                    return resultado;
                }
            }

            resultado.esValido = true;
            resultado.periodoNormalizado = String.format("Enero-Junio %d", añoPeriodo);
            resultado.semestre = "ENE-JUN";
            resultado.año = añoPeriodo;
            return resultado;

        } else {
            // Periodo Agosto-Diciembre
            if (añoPeriodo < añoActual) {
                resultado.mensajeError = String.format(
                    "No puede crear cursos para periodos pasados. Periodo Agosto-Diciembre %d ya finalizó",
                    añoPeriodo
                );
                return resultado;
            }

            if (añoPeriodo == añoActual) {
                // Si estamos en Agosto-Diciembre del mismo año, ya no se puede crear
                if (mesActual.getValue() >= Month.AUGUST.getValue() && 
                    mesActual.getValue() <= Month.DECEMBER.getValue()) {
                    resultado.mensajeError = String.format(
                        "El periodo Agosto-Diciembre %d ya está en curso. Seleccione: Enero-Junio %d o Agosto-Diciembre %d",
                        añoActual, añoActual + 1, añoActual + 1
                    );
                    return resultado;
                }
            }

            resultado.esValido = true;
            resultado.periodoNormalizado = String.format("Agosto-Diciembre %d", añoPeriodo);
            resultado.semestre = "AGO-DIC";
            resultado.año = añoPeriodo;
            return resultado;
        }
    }

    /**
     * Clase interna para retornar el resultado de la validación
     */
    private static class ResultadoValidacionPeriodo {
        boolean esValido = false;
        String mensajeError = "";
        String periodoNormalizado = "";
        String semestre = "";
        int año = 0;
    }

    /**
     * Método estático para obtener los periodos académicos válidos desde la fecha actual
     * (útil para el frontend y el controlador)
     */
    public static String[] obtenerPeriodosValidos() {
        LocalDate fechaActual = LocalDate.now();
        int añoActual = fechaActual.getYear();
        Month mesActual = fechaActual.getMonth();

        java.util.List<String> periodosValidos = new java.util.ArrayList<>();

        // Si estamos en Enero-Junio o Julio
        if (mesActual.getValue() >= Month.JANUARY.getValue() && 
            mesActual.getValue() <= Month.JULY.getValue()) {
            
            periodosValidos.add(String.format("Agosto-Diciembre %d", añoActual));
            periodosValidos.add(String.format("Enero-Junio %d", añoActual + 1));
            periodosValidos.add(String.format("Agosto-Diciembre %d", añoActual + 1));
        }
        // Si estamos en Agosto-Diciembre
        else {
            periodosValidos.add(String.format("Enero-Junio %d", añoActual + 1));
            periodosValidos.add(String.format("Agosto-Diciembre %d", añoActual + 1));
        }

        return periodosValidos.toArray(new String[0]);
    }
}
