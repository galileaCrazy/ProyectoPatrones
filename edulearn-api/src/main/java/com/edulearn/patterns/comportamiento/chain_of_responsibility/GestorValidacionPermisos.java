package com.edulearn.patterns.comportamiento.chain_of_responsibility;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;

/**
 * GestorConcreto3 - Validador de Permisos Específicos
 *
 * Tercera validación en la cadena: verifica permisos específicos
 * para acciones sobre recursos (crear, editar, eliminar, etc.)
 * 
 * Estructura del patrón (diagrama UML clásico):
 * 
 *         Gestor (abstracta)
 *            │
 *            └── GestorValidacionPermisos ─── #siguiente ──▶ GestorValidacionPeriodo
 *                      │
 *                      └── solicita(SolicitudValidacion)
 *                              │
 *                              ├── manejar() → valida acción vs permisos del rol
 *                              │
 *                              └── si válido → siguiente.solicita()
 */
@Component
public class GestorValidacionPermisos extends Gestor {

    // Mapeo de tipo de usuario a permisos
    private static final Map<String, List<String>> PERMISOS_POR_ROL = new HashMap<>();

    static {
        PERMISOS_POR_ROL.put("estudiante", Arrays.asList(
            "ver_cursos",
            "inscribirse",
            "ver_estructura",
            "cambiar_vista",
            "ver_progreso"
        ));

        PERMISOS_POR_ROL.put("profesor", Arrays.asList(
            "crear_curso",
            "editar_curso",
            "eliminar_curso",
            "clonar_curso",
            "decorar_curso",
            "videoconferencia",
            "ver_cursos",
            "inscribirse",
            "ver_estructura",
            "calificar"
        ));

        PERMISOS_POR_ROL.put("administrador", Arrays.asList(
            "todo" // Administradores tienen todos los permisos
        ));
    }

    // Mapeo de acciones a permisos requeridos
    private static final Map<String, String> ACCION_PERMISO = new HashMap<>();

    static {
        ACCION_PERMISO.put("listar", "ver_cursos");
        ACCION_PERMISO.put("ver", "ver_cursos");
        ACCION_PERMISO.put("crear", "crear_curso");
        ACCION_PERMISO.put("editar", "editar_curso");
        ACCION_PERMISO.put("actualizar", "editar_curso");
        ACCION_PERMISO.put("eliminar", "eliminar_curso");
        ACCION_PERMISO.put("clonar", "clonar_curso");
        ACCION_PERMISO.put("inscribir", "inscribirse");
        ACCION_PERMISO.put("estructura", "ver_estructura");
        ACCION_PERMISO.put("calificar", "calificar");
    }

    @Override
    protected boolean manejar(SolicitudValidacion solicitud) {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ [Gestor 3/5] GestorValidacionPermisos                       │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");

        String tipoUsuario = solicitud.getTipoUsuario();
        String accion = solicitud.getAccion();

        System.out.println("   • Tipo de usuario: " + tipoUsuario);
        System.out.println("   • Acción solicitada: " + accion);

        // Si no hay acción específica, permitir (la validación de rol fue suficiente)
        if (accion == null || accion.trim().isEmpty()) {
            System.out.println("   ⚠️  Sin acción específica, permiso otorgado");
            System.out.println("   ➡️  Pasando al siguiente gestor...\n");
            return true;
        }

        // Obtener permisos del usuario
        List<String> permisosUsuario = PERMISOS_POR_ROL.get(tipoUsuario.toLowerCase());

        if (permisosUsuario == null) {
            solicitud.setMensajeError("Tipo de usuario no reconocido: " + tipoUsuario);
            solicitud.setAprobada(false);
            System.out.println("   ❌ Tipo de usuario no reconocido");
            return false;
        }

        System.out.println("   • Permisos del rol: " + permisosUsuario);

        // Si el usuario es administrador con permiso "todo", aprobar
        if (permisosUsuario.contains("todo")) {
            System.out.println("   ✅ Administrador con permisos totales");
            System.out.println("   ➡️  Pasando al siguiente gestor...\n");
            return true;
        }

        // Obtener permiso requerido para la acción
        String permisoRequerido = ACCION_PERMISO.get(accion.toLowerCase());

        if (permisoRequerido == null) {
            // Acción no mapeada, permitir por defecto
            System.out.println("   ⚠️  Acción no mapeada, permiso otorgado");
            System.out.println("   ➡️  Pasando al siguiente gestor...\n");
            return true;
        }

        System.out.println("   • Permiso requerido: " + permisoRequerido);

        // Verificar si el usuario tiene el permiso requerido
        if (!permisosUsuario.contains(permisoRequerido)) {
            solicitud.setMensajeError(
                "Usuario '" + tipoUsuario + "' no tiene permiso '" +
                permisoRequerido + "' para acción: " + accion
            );
            solicitud.setAprobada(false);
            System.out.println("   ❌ Permiso '" + permisoRequerido + "' NO encontrado en rol");
            return false;
        }

        System.out.println("   ✅ Permiso '" + permisoRequerido + "' verificado");
        System.out.println("   ➡️  Pasando al siguiente gestor...\n");
        
        return true; // Permiso autorizado, continuar cadena
    }

    /**
     * Método auxiliar para agregar permisos dinámicamente
     */
    public static void agregarPermisoARol(String rol, String permiso) {
        List<String> permisos = PERMISOS_POR_ROL.get(rol.toLowerCase());
        if (permisos != null && !permisos.contains(permiso)) {
            permisos.add(permiso);
        }
    }
}
