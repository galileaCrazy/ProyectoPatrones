package com.edulearn.patterns.comportamiento.chain_of_responsibility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Chain of Responsibility - Gestor de Validación de Período Académico para Creación de Cursos
 *
 * Este gestor valida que las fechas del curso estén dentro de un período académico válido
 * y que el período seleccionado sea futuro (no pasado).
 *
 * Períodos académicos:
 * - Enero-Junio: 1 de enero al 30 de junio
 * - Agosto-Diciembre: 1 de agosto al 31 de diciembre
 *
 * Regla: Solo se pueden crear cursos para períodos académicos futuros.
 *
 * Patrón: Chain of Responsibility
 */
@Component
public class GestorValidacionPeriodoCreacionCurso extends Gestor {

    private static final Logger logger = LoggerFactory.getLogger(GestorValidacionPeriodoCreacionCurso.class);

    @Override
    protected boolean manejar(SolicitudValidacion solicitud) {
        logger.info("📅 Validando período académico para creación de curso");

        String accion = solicitud.getAccion();

        // Solo procesamos si la acción es "VALIDAR_FECHAS_CURSO"
        if (!"VALIDAR_FECHAS_CURSO".equals(accion)) {
            logger.info("➡️ Acción no relacionada con validación de fechas, pasando al siguiente gestor");
            return true;
        }

        Map<String, Object> metadatos = solicitud.getMetadatos();
        LocalDate fechaActual = LocalDate.now();

        // Obtener información del curso
        String periodoSeleccionado = (String) metadatos.get("periodo");

        logger.info("📆 Fecha actual: {}", fechaActual);
        logger.info("📚 Período seleccionado: {}", periodoSeleccionado);

        // Calcular períodos válidos
        List<String> periodosValidos = calcularPeriodosValidos(fechaActual);

        logger.info("✅ Períodos válidos disponibles: {}", periodosValidos);
        solicitud.agregarMetadato("periodosValidos", periodosValidos);

        // Validar que el período seleccionado sea válido
        if (periodoSeleccionado != null && !periodoSeleccionado.isEmpty()) {
            if (!periodosValidos.contains(periodoSeleccionado)) {
                String mensaje = String.format(
                    "El período '%s' no es válido. Solo puedes crear cursos para períodos futuros: %s",
                    periodoSeleccionado,
                    String.join(", ", periodosValidos)
                );
                solicitud.setMensajeError(mensaje);
                logger.error("❌ {}", mensaje);
                return false;
            }

            // Validar fechas si están proporcionadas
            Object fechaInicioObj = metadatos.get("fechaInicio");
            Object fechaFinObj = metadatos.get("fechaFin");

            if (fechaInicioObj != null && fechaFinObj != null) {
                try {
                    LocalDate fechaInicio = LocalDate.parse(fechaInicioObj.toString());
                    LocalDate fechaFin = LocalDate.parse(fechaFinObj.toString());

                    // Obtener rango del período
                    PeriodoAcademico periodo = parsearPeriodo(periodoSeleccionado);

                    // Validar que las fechas estén dentro del período
                    if (fechaInicio.isBefore(periodo.fechaInicio) || fechaInicio.isAfter(periodo.fechaFin)) {
                        String mensaje = String.format(
                            "La fecha de inicio (%s) debe estar dentro del período %s (%s a %s)",
                            fechaInicio,
                            periodoSeleccionado,
                            periodo.fechaInicio,
                            periodo.fechaFin
                        );
                        solicitud.setMensajeError(mensaje);
                        logger.error("❌ {}", mensaje);
                        return false;
                    }

                    if (fechaFin.isBefore(periodo.fechaInicio) || fechaFin.isAfter(periodo.fechaFin)) {
                        String mensaje = String.format(
                            "La fecha de fin (%s) debe estar dentro del período %s (%s a %s)",
                            fechaFin,
                            periodoSeleccionado,
                            periodo.fechaInicio,
                            periodo.fechaFin
                        );
                        solicitud.setMensajeError(mensaje);
                        logger.error("❌ {}", mensaje);
                        return false;
                    }

                    if (fechaFin.isBefore(fechaInicio)) {
                        solicitud.setMensajeError("La fecha de fin no puede ser anterior a la fecha de inicio");
                        logger.error("❌ Fecha de fin anterior a fecha de inicio");
                        return false;
                    }

                    logger.info("✅ Fechas validadas correctamente: {} a {}", fechaInicio, fechaFin);

                } catch (Exception e) {
                    solicitud.setMensajeError("Error al validar fechas: " + e.getMessage());
                    logger.error("❌ Error al parsear fechas: {}", e.getMessage());
                    return false;
                }
            }

            logger.info("✅ Período académico válido");
            return true;
        }

        // Si no hay período seleccionado aún, solo informamos los períodos válidos
        logger.info("ℹ️ No hay período seleccionado, períodos válidos agregados a metadatos");
        return true;
    }

    /**
     * Calcula los períodos académicos válidos (SOLO FUTUROS) a partir de la fecha actual
     *
     * Regla estricta: Solo se pueden crear cursos para períodos que AÚN NO HAN COMENZADO
     *
     * Ejemplos:
     * - Hoy: 30/11/2025 (Noviembre) → Solo: Enero-Junio 2026, Agosto-Diciembre 2026
     * - Hoy: 15/01/2026 (Enero, mitad de período) → Solo: Agosto-Diciembre 2026, Enero-Junio 2027
     * - Hoy: 15/08/2026 (Agosto, inicio de período) → Solo: Enero-Junio 2027, Agosto-Diciembre 2027
     */
    private List<String> calcularPeriodosValidos(LocalDate fechaActual) {
        List<String> periodosValidos = new ArrayList<>();
        int añoActual = fechaActual.getYear();
        int mesActual = fechaActual.getMonthValue();

        logger.info("📆 Calculando períodos válidos desde: {} (mes: {}, año: {})",
            fechaActual, mesActual, añoActual);

        // Determinar el siguiente período disponible
        if (mesActual >= 1 && mesActual <= 6) {
            // Estamos en período Enero-Junio
            // El período actual ya empezó, solo podemos crear para períodos futuros
            periodosValidos.add("Agosto-Diciembre " + añoActual);
            periodosValidos.add("Enero-Junio " + (añoActual + 1));
            periodosValidos.add("Agosto-Diciembre " + (añoActual + 1));

            logger.info("📍 Periodo actual: Enero-Junio {} (ya comenzado)", añoActual);
            logger.info("✅ Próximo disponible: Agosto-Diciembre {}", añoActual);

        } else if (mesActual == 7) {
            // Julio - Período de vacaciones entre semestres
            // Podemos crear para Agosto-Diciembre del año actual (está por empezar)
            periodosValidos.add("Agosto-Diciembre " + añoActual);
            periodosValidos.add("Enero-Junio " + (añoActual + 1));
            periodosValidos.add("Agosto-Diciembre " + (añoActual + 1));

            logger.info("📍 Vacaciones de julio");
            logger.info("✅ Próximo disponible: Agosto-Diciembre {}", añoActual);

        } else {
            // Estamos en período Agosto-Diciembre (meses 8-12)
            // El período actual ya empezó, solo podemos crear para períodos futuros
            periodosValidos.add("Enero-Junio " + (añoActual + 1));
            periodosValidos.add("Agosto-Diciembre " + (añoActual + 1));
            periodosValidos.add("Enero-Junio " + (añoActual + 2));

            logger.info("📍 Periodo actual: Agosto-Diciembre {} (ya comenzado)", añoActual);
            logger.info("✅ Próximo disponible: Enero-Junio {}", añoActual + 1);
        }

        logger.info("✅ Períodos válidos calculados: {}", periodosValidos);
        return periodosValidos;
    }

    /**
     * Parsea un período en formato "Mes-Mes Año" y devuelve las fechas de inicio y fin
     */
    private PeriodoAcademico parsearPeriodo(String periodo) {
        String[] partes = periodo.split(" ");
        int año = Integer.parseInt(partes[1]);

        if (periodo.startsWith("Enero-Junio")) {
            return new PeriodoAcademico(
                LocalDate.of(año, Month.JANUARY, 1),
                LocalDate.of(año, Month.JUNE, 30)
            );
        } else if (periodo.startsWith("Agosto-Diciembre")) {
            return new PeriodoAcademico(
                LocalDate.of(año, Month.AUGUST, 1),
                LocalDate.of(año, Month.DECEMBER, 31)
            );
        }

        throw new IllegalArgumentException("Formato de período inválido: " + periodo);
    }

    /**
     * Clase interna para representar un período académico
     */
    private static class PeriodoAcademico {
        LocalDate fechaInicio;
        LocalDate fechaFin;

        PeriodoAcademico(LocalDate fechaInicio, LocalDate fechaFin) {
            this.fechaInicio = fechaInicio;
            this.fechaFin = fechaFin;
        }
    }
}
