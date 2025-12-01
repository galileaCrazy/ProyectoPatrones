package com.edulearn.patterns.comportamiento.chain_of_responsibility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio que configura y gestiona la Cadena de Responsabilidad
 * para validación de permisos, periodos académicos y asignación de profesores.
 *
 * Este servicio actúa como punto de entrada para ejecutar
 * toda la cadena de validación siguiendo la estructura clásica
 * del patrón GoF (Gang of Four).
 */
@Service
public class CadenaValidacionService {

    private final Gestor cadenaCompleta;
    private final Gestor cadenaCreacionCurso;

    /**
     * Constructor que inicializa y configura las cadenas de validación.
     * 
     * Cadena Completa: Token -> Rol -> Permisos -> Horario -> Cupos
     * Cadena para Creación de Curso: Token -> Rol -> Permisos -> Periodo -> Auto-Asignación
     *
     * @param gestorToken Validador de token (primer eslabón)
     * @param gestorRol Validador de rol (segundo eslabón)
     * @param gestorPermisos Validador de permisos (tercer eslabón)
     * @param gestorHorario Validador de horario (cuarto eslabón)
     * @param gestorCupos Validador de cupos (quinto eslabón)
     * @param gestorPeriodo Validador de periodo académico (sexto eslabón)
     * @param gestorAutoAsignacion Validador y auto-asignador de profesor (séptimo eslabón)
     */
    @Autowired
    public CadenaValidacionService(
            GestorValidacionToken gestorToken,
            GestorValidacionRol gestorRol,
            GestorValidacionPermisos gestorPermisos,
            GestorValidacionHorario gestorHorario,
            GestorValidacionCupos gestorCupos,
            GestorValidacionPeriodoAcademico gestorPeriodo,
            GestorValidacionAutoAsignacionProfesor gestorAutoAsignacion
    ) {
        // ========== CADENA COMPLETA (para inscripciones y operaciones generales) ==========
        // Token -> Rol -> Permisos -> Horario -> Cupos
        GestorValidacionToken token1 = gestorToken;
        GestorValidacionRol rol1 = new GestorValidacionRol();
        GestorValidacionPermisos permisos1 = gestorPermisos;
        GestorValidacionHorario horario1 = gestorHorario;
        GestorValidacionCupos cupos1 = gestorCupos;
        
        token1.establecerSiguiente(rol1);
        rol1.establecerSiguiente(permisos1);
        permisos1.establecerSiguiente(horario1);
        horario1.establecerSiguiente(cupos1);
        
        this.cadenaCompleta = token1;

        // ========== CADENA PARA CREACIÓN/EDICIÓN DE CURSOS ==========
        // Token -> Rol -> Permisos -> Periodo Académico -> Auto-Asignación Profesor
        GestorValidacionToken token2 = new GestorValidacionToken();
        GestorValidacionRol rol2 = new GestorValidacionRol();
        GestorValidacionPermisos permisos2 = new GestorValidacionPermisos();
        GestorValidacionPeriodoAcademico periodo = gestorPeriodo;
        GestorValidacionAutoAsignacionProfesor autoAsignacion = gestorAutoAsignacion;
        
        token2.establecerSiguiente(rol2);
        rol2.establecerSiguiente(permisos2);
        permisos2.establecerSiguiente(periodo);
        periodo.establecerSiguiente(autoAsignacion);
        
        this.cadenaCreacionCurso = token2;

        System.out.println("\n[Chain of Responsibility] ✓ Cadenas de validación configuradas:");
        System.out.println("\n[Cadena Completa - Operaciones Generales]");
        System.out.println("  1. GestorValidacionToken");
        System.out.println("  2. GestorValidacionRol");
        System.out.println("  3. GestorValidacionPermisos");
        System.out.println("  4. GestorValidacionHorario");
        System.out.println("  5. GestorValidacionCupos");
        
        System.out.println("\n[Cadena Especial - Creación/Edición de Cursos]");
        System.out.println("  1. GestorValidacionToken");
        System.out.println("  2. GestorValidacionRol");
        System.out.println("  3. GestorValidacionPermisos");
        System.out.println("  4. GestorValidacionPeriodoAcademico");
        System.out.println("  5. GestorValidacionAutoAsignacionProfesor");
        System.out.println();
    }

    /**
     * Valida operaciones generales (inscripciones, consultas, etc.)
     * Usa la cadena completa con validación de cupos y horarios
     *
     * @param solicitud La solicitud de validación a procesar
     * @return true si todas las validaciones pasaron, false si alguna falló
     */
    public boolean validarOperacionGeneral(SolicitudValidacion solicitud) {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ [Chain of Responsibility] CADENA GENERAL                  ║");
        System.out.println("║ Recurso: " + String.format("%-45s", solicitud.getRecursoSolicitado()) + "║");
        System.out.println("║ Acción:  " + String.format("%-45s", solicitud.getAccion()) + "║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        cadenaCompleta.solicita(solicitud);

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ Resultado: " + (solicitud.isAprobada() ? "✓ APROBADA" : "✗ RECHAZADA") + String.format("%-39s", "") + "║");
        if (!solicitud.isAprobada()) {
            System.out.println("║ Razón: " + String.format("%-48s", solicitud.getMensajeError()) + "║");
        }
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        return solicitud.isAprobada();
    }

    /**
     * Valida creación o edición de cursos
     * Usa la cadena especial con validación de periodo académico y auto-asignación de profesor
     *
     * @param solicitud La solicitud de validación a procesar
     * @return true si todas las validaciones pasaron, false si alguna falló
     */
    public boolean validarCreacionCurso(SolicitudValidacion solicitud) {
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ [Chain of Responsibility] CADENA CREACIÓN DE CURSO        ║");
        System.out.println("║ Recurso: " + String.format("%-45s", solicitud.getRecursoSolicitado()) + "║");
        System.out.println("║ Acción:  " + String.format("%-45s", solicitud.getAccion()) + "║");
        System.out.println("║ Usuario: " + String.format("%-45s", solicitud.getTipoUsuario()) + "║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        cadenaCreacionCurso.solicita(solicitud);

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║ Resultado: " + (solicitud.isAprobada() ? "✓ APROBADA" : "✗ RECHAZADA") + String.format("%-39s", "") + "║");
        if (!solicitud.isAprobada()) {
            String mensaje = solicitud.getMensajeError();
            // Dividir mensaje largo en múltiples líneas si es necesario
            if (mensaje.length() > 48) {
                String[] palabras = mensaje.split(" ");
                StringBuilder linea = new StringBuilder();
                System.out.println("║ Razón:                                                 ║");
                for (String palabra : palabras) {
                    if (linea.length() + palabra.length() + 1 <= 50) {
                        if (linea.length() > 0) linea.append(" ");
                        linea.append(palabra);
                    } else {
                        System.out.println("║   " + String.format("%-52s", linea.toString()) + "║");
                        linea = new StringBuilder(palabra);
                    }
                }
                if (linea.length() > 0) {
                    System.out.println("║   " + String.format("%-52s", linea.toString()) + "║");
                }
            } else {
                System.out.println("║ Razón: " + String.format("%-48s", mensaje) + "║");
            }
        }
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        return solicitud.isAprobada();
    }

    /**
     * Método legacy - redirige a la cadena general
     * Mantenido por compatibilidad con código existente
     */
    public boolean validar(SolicitudValidacion solicitud) {
        return validarOperacionGeneral(solicitud);
    }

    /**
     * Método auxiliar para crear y validar una solicitud en un solo paso
     * Usa la cadena general por defecto
     *
     * @param token Token de autenticación
     * @param recurso Recurso solicitado
     * @param accion Acción a realizar
     * @return La solicitud procesada con el resultado
     */
    public SolicitudValidacion validarAcceso(String token, String recurso, String accion) {
        SolicitudValidacion solicitud = new SolicitudValidacion(token, recurso, accion);
        validarOperacionGeneral(solicitud);
        return solicitud;
    }

    /**
     * Obtiene la cadena completa (útil para testing o inspección)
     */
    public Gestor getCadenaCompleta() {
        return cadenaCompleta;
    }

    /**
     * Obtiene la cadena de creación de curso (útil para testing o inspección)
     */
    public Gestor getCadenaCreacionCurso() {
        return cadenaCreacionCurso;
    }
}
