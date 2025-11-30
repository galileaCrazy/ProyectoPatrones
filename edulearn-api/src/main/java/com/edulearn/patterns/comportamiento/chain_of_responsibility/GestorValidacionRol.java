package com.edulearn.patterns.comportamiento.chain_of_responsibility;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.List;

/**
 * GestorConcreto2 - Validador de Rol
 *
 * Segunda validación en la cadena: verifica que el tipo de usuario
 * tenga acceso al recurso solicitado.
 * 
 * Estructura del patrón (diagrama UML clásico):
 * 
 *         Gestor (abstracta)
 *            │
 *            └── GestorValidacionRol ─── #siguiente ──▶ GestorValidacionPermisos
 *                      │
 *                      └── solicita(SolicitudValidacion)
 *                              │
 *                              ├── manejar() → valida rol vs recurso
 *                              │
 *                              └── si válido → siguiente.solicita()
 */
@Component
public class GestorValidacionRol extends Gestor {

    // Mapeo de recursos a roles permitidos
    private static final Map<String, List<String>> RECURSOS_ROLES = new HashMap<>();

    static {
        // Recursos de estudiante
        RECURSOS_ROLES.put("/api/cursos/listar", Arrays.asList("estudiante", "profesor", "administrador"));
        RECURSOS_ROLES.put("/api/inscripciones", Arrays.asList("estudiante", "profesor", "administrador"));
        RECURSOS_ROLES.put("/api/estudiante", Arrays.asList("estudiante", "administrador"));

        // Recursos de profesor
        RECURSOS_ROLES.put("/api/cursos/crear", Arrays.asList("profesor", "administrador"));
        RECURSOS_ROLES.put("/api/cursos/editar", Arrays.asList("profesor", "administrador"));
        RECURSOS_ROLES.put("/api/cursos/clonar", Arrays.asList("profesor", "administrador"));
        RECURSOS_ROLES.put("/api/patrones", Arrays.asList("profesor", "administrador"));

        // Recursos de administrador
        RECURSOS_ROLES.put("/api/configuracion", Arrays.asList("administrador"));
        RECURSOS_ROLES.put("/api/admin", Arrays.asList("administrador"));
    }

    @Override
    protected boolean manejar(SolicitudValidacion solicitud) {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ [Gestor 2/5] GestorValidacionRol                            │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");

        String tipoUsuario = solicitud.getTipoUsuario();
        String recurso = solicitud.getRecursoSolicitado();

        System.out.println("   • Rol del usuario: " + tipoUsuario);
        System.out.println("   • Recurso solicitado: " + recurso);

        // Si el tipo de usuario no está definido, es un error del gestor anterior
        if (tipoUsuario == null || tipoUsuario.trim().isEmpty()) {
            solicitud.setMensajeError("Tipo de usuario no identificado");
            solicitud.setAprobada(false);
            System.out.println("   ❌ Rol no identificado");
            return false;
        }

        // Buscar roles permitidos para este recurso
        List<String> rolesPermitidos = RECURSOS_ROLES.get(recurso);

        if (rolesPermitidos == null) {
            // Recurso no definido en el mapa, permitir por defecto
            System.out.println("   ⚠️  Recurso no mapeado, permitido por defecto");
            System.out.println("   ➡️  Pasando al siguiente gestor...\n");
            return true;
        }

        // Verificar si el rol del usuario está en la lista de roles permitidos
        if (!rolesPermitidos.contains(tipoUsuario.toLowerCase())) {
            solicitud.setMensajeError("Rol '" + tipoUsuario + "' no tiene acceso al recurso: " + recurso);
            solicitud.setAprobada(false);
            System.out.println("   ❌ Rol '" + tipoUsuario + "' NO tiene acceso");
            System.out.println("   • Roles permitidos: " + rolesPermitidos);
            return false;
        }

        System.out.println("   ✅ Rol autorizado para el recurso");
        System.out.println("   • Roles permitidos: " + rolesPermitidos);
        System.out.println("   ➡️  Pasando al siguiente gestor...\n");
        
        return true; // Rol autorizado, continuar cadena
    }

    /**
     * Método auxiliar para agregar recursos dinámicamente
     */
    public static void agregarRecursoRol(String recurso, List<String> roles) {
        RECURSOS_ROLES.put(recurso, roles);
    }
}
