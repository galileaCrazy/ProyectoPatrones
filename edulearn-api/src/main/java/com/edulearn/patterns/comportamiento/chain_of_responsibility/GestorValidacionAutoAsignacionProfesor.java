package com.edulearn.patterns.comportamiento.chain_of_responsibility;

import org.springframework.stereotype.Component;

/**
 * GestorConcreto5 - Validador y Auto-Asignador de Profesor
 *
 * Quinta y última validación en la cadena: cuando un profesor crea un curso,
 * automáticamente se asigna como titular del curso.
 *
 * Reglas de asignación:
 * ┌────────────────────┬──────────────────────────────────────────────┐
 * │ Tipo de Usuario    │ Comportamiento                               │
 * ├────────────────────┼──────────────────────────────────────────────┤
 * │ PROFESOR           │ Se auto-asigna como titular                  │
 * │                    │ NO puede asignar a otro profesor             │
 * │                    │ Campo "Profesor" NO visible en formulario    │
 * ├────────────────────┼──────────────────────────────────────────────┤
 * │ ADMINISTRADOR      │ DEBE asignar un profesor titular             │
 * │                    │ Puede elegir cualquier profesor              │
 * │                    │ Campo "Profesor" SÍ visible en formulario    │
 * ├────────────────────┼──────────────────────────────────────────────┤
 * │ ESTUDIANTE         │ NO puede crear cursos (rechazado antes)      │
 * └────────────────────┴──────────────────────────────────────────────┘
 * 
 * Estructura del patrón (diagrama UML clásico):
 * 
 *         Gestor (abstracta)
 *            │
 *            └── GestorValidacionAutoAsignacionProfesor ─── #siguiente ──▶ null (fin)
 *                      │
 *                      └── solicita(SolicitudValidacion)
 *                              │
 *                              ├── manejar() → auto-asigna o valida profesor
 *                              │
 *                              └── si válido → solicitud.setAprobada(true)
 */
@Component
public class GestorValidacionAutoAsignacionProfesor extends Gestor {

    @Override
    protected boolean manejar(SolicitudValidacion solicitud) {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ [Gestor 5/5] GestorValidacionAutoAsignacionProfesor         │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");

        String accion = solicitud.getAccion();
        String tipoUsuario = solicitud.getTipoUsuario();

        System.out.println("   • Tipo de usuario: " + tipoUsuario);
        System.out.println("   • Acción: " + accion);

        // Solo validar para creación y edición de cursos
        if (accion == null || (!accion.equalsIgnoreCase("crear") && !accion.equalsIgnoreCase("editar"))) {
            System.out.println("   ⚠️  Acción no requiere validación de asignación de profesor");
            System.out.println("   ✅ Fin de la cadena\n");
            return true;
        }

        // Obtener el ID del usuario que está creando/editando el curso
        Object usuarioIdObj = solicitud.getMetadatos().get("usuarioId");
        
        if (usuarioIdObj == null) {
            solicitud.setMensajeError("No se pudo identificar el usuario que realiza la acción");
            solicitud.setAprobada(false);
            System.out.println("   ❌ Usuario ID no encontrado en metadatos");
            return false;
        }

        Integer usuarioId;
        try {
            usuarioId = Integer.parseInt(usuarioIdObj.toString());
        } catch (NumberFormatException e) {
            solicitud.setMensajeError("ID de usuario inválido");
            solicitud.setAprobada(false);
            System.out.println("   ❌ ID de usuario inválido");
            return false;
        }

        // Obtener el profesor titular que se intenta asignar (si existe)
        Object profesorTitularIdObj = solicitud.getMetadatos().get("profesorTitularId");

        System.out.println("   • Usuario ID: " + usuarioId);
        System.out.println("   • Profesor titular especificado: " + (profesorTitularIdObj != null ? profesorTitularIdObj : "ninguno"));

        // ═══════════════════════════════════════════════════════════════
        // APLICAR REGLAS SEGÚN TIPO DE USUARIO
        // ═══════════════════════════════════════════════════════════════

        if (tipoUsuario.equalsIgnoreCase("profesor")) {
            // ─────────────────────────────────────────────────────────────
            // CASO 1: PROFESOR creando/editando curso
            // ─────────────────────────────────────────────────────────────
            System.out.println("\n   📋 Regla: Profesor crea su propio curso");
            
            if (profesorTitularIdObj != null) {
                // El profesor intentó asignar un profesor titular
                Integer profesorTitularId;
                try {
                    profesorTitularId = Integer.parseInt(profesorTitularIdObj.toString());
                } catch (NumberFormatException e) {
                    solicitud.setMensajeError("ID de profesor titular inválido");
                    solicitud.setAprobada(false);
                    System.out.println("   ❌ ID de profesor titular inválido");
                    return false;
                }

                // Validar que el profesor solo pueda asignarse a sí mismo
                if (!profesorTitularId.equals(usuarioId)) {
                    solicitud.setMensajeError(
                        "Los profesores solo pueden crear cursos para sí mismos. " +
                        "No puede asignar a otro profesor como titular."
                    );
                    solicitud.setAprobada(false);
                    System.out.println("   ❌ Profesor " + usuarioId + " intentó asignar a profesor " + profesorTitularId);
                    System.out.println("   ❌ Los profesores NO pueden asignar otros profesores");
                    return false;
                }

                System.out.println("   ✅ Profesor " + usuarioId + " confirmado como titular");
                
            } else {
                // El profesor no especificó profesor titular, se auto-asigna
                solicitud.agregarMetadato("profesorTitularId", usuarioId);
                solicitud.agregarMetadato("autoAsignado", true);
                
                System.out.println("   ✅ Profesor " + usuarioId + " AUTO-ASIGNADO como titular");
                System.out.println("   📌 El campo 'Profesor Titular' no se muestra para profesores");
            }

        } else if (tipoUsuario.equalsIgnoreCase("administrador")) {
            // ─────────────────────────────────────────────────────────────
            // CASO 2: ADMINISTRADOR creando/editando curso
            // ─────────────────────────────────────────────────────────────
            System.out.println("\n   📋 Regla: Administrador debe asignar profesor");
            
            if (profesorTitularIdObj == null) {
                solicitud.setMensajeError(
                    "Los administradores deben especificar un profesor titular al crear un curso"
                );
                solicitud.setAprobada(false);
                System.out.println("   ❌ Administrador NO especificó profesor titular");
                return false;
            }

            // Validar que el ID del profesor titular sea válido
            Integer profesorTitularId;
            try {
                profesorTitularId = Integer.parseInt(profesorTitularIdObj.toString());
            } catch (NumberFormatException e) {
                solicitud.setMensajeError("ID de profesor titular inválido");
                solicitud.setAprobada(false);
                System.out.println("   ❌ ID de profesor titular inválido");
                return false;
            }

            if (profesorTitularId <= 0) {
                solicitud.setMensajeError("Debe especificar un profesor titular válido");
                solicitud.setAprobada(false);
                System.out.println("   ❌ ID de profesor inválido: " + profesorTitularId);
                return false;
            }

            System.out.println("   ✅ Administrador asignó profesor " + profesorTitularId + " como titular");

        } else if (tipoUsuario.equalsIgnoreCase("estudiante")) {
            // ─────────────────────────────────────────────────────────────
            // CASO 3: ESTUDIANTE intentando crear curso
            // (esto no debería llegar aquí, pero por seguridad)
            // ─────────────────────────────────────────────────────────────
            solicitud.setMensajeError(
                "Los estudiantes no tienen permisos para crear cursos"
            );
            solicitud.setAprobada(false);
            System.out.println("   ❌ Estudiantes NO pueden crear cursos");
            return false;
        }

        System.out.println("\n   🎉 Todas las validaciones completadas");
        System.out.println("   ✅ Fin de la cadena - Solicitud APROBADA\n");
        
        return true; // Validación exitosa, fin de la cadena
    }

    /**
     * Método auxiliar para verificar si un usuario puede editar un curso
     * (útil para acciones de edición)
     */
    public static boolean puedeEditarCurso(String tipoUsuario, Integer usuarioId, Integer profesorTitularIdCurso) {
        if (tipoUsuario.equalsIgnoreCase("administrador")) {
            return true; // Administradores pueden editar cualquier curso
        }

        if (tipoUsuario.equalsIgnoreCase("profesor")) {
            // Profesores solo pueden editar sus propios cursos
            return usuarioId.equals(profesorTitularIdCurso);
        }

        return false; // Estudiantes no pueden editar cursos
    }
}
