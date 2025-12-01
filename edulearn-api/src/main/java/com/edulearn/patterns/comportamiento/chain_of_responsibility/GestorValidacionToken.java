package com.edulearn.patterns.comportamiento.chain_of_responsibility;

import com.edulearn.repository.UsuarioRepository;
import com.edulearn.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * GestorConcreto1 - Validador de Token
 *
 * Primera validación en la cadena: verifica que el token sea válido
 * y extrae información del usuario.
 * 
 * Estructura del patrón (diagrama UML clásico):
 * 
 *         Gestor (abstracta)
 *            │
 *            └── GestorValidacionToken ─── #siguiente ──▶ GestorValidacionRol
 *                      │
 *                      └── solicita(SolicitudValidacion)
 *                              │
 *                              ├── manejar() → valida token
 *                              │
 *                              └── si válido → siguiente.solicita()
 */
@Component
public class GestorValidacionToken extends Gestor {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected boolean manejar(SolicitudValidacion solicitud) {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.println("│ [Gestor 1/5] GestorValidacionToken                          │");
        System.out.println("└─────────────────────────────────────────────────────────────┘");

        String token = solicitud.getToken();

        // Validación básica del token
        if (token == null || token.trim().isEmpty()) {
            solicitud.setMensajeError("Token no proporcionado");
            solicitud.setAprobada(false);
            System.out.println("   ❌ Token vacío o nulo");
            return false;
        }

        // Extraer ID del usuario del token
        // Formatos aceptados: "token-{id}", "TOKEN_DEMO_{id}"
        Integer userId = null;
        
        try {
            if (token.startsWith("token-")) {
                String userIdStr = token.substring(6);
                userId = Integer.parseInt(userIdStr);
            } else if (token.startsWith("TOKEN_DEMO_")) {
                String userIdStr = token.substring(11);
                userId = Integer.parseInt(userIdStr);
            } else if (token.matches("\\d+")) {
                // Token es directamente el ID
                userId = Integer.parseInt(token);
            } else {
                // Intentar extraer cualquier número del token
                String numeros = token.replaceAll("[^0-9]", "");
                if (!numeros.isEmpty()) {
                    userId = Integer.parseInt(numeros);
                }
            }
        } catch (NumberFormatException e) {
            // Continuar para verificar si hay usuario en metadatos
        }

        // Si no se pudo extraer del token, verificar si ya viene en metadatos
        if (userId == null) {
            Object metaUserId = solicitud.getMetadatos().get("usuarioId");
            if (metaUserId != null) {
                if (metaUserId instanceof Integer) {
                    userId = (Integer) metaUserId;
                } else {
                    try {
                        userId = Integer.parseInt(metaUserId.toString());
                    } catch (NumberFormatException e) {
                        // Continuar
                    }
                }
            }
        }

        if (userId == null) {
            solicitud.setMensajeError("No se pudo identificar el usuario del token");
            solicitud.setAprobada(false);
            System.out.println("   ❌ Token inválido, no contiene ID de usuario");
            return false;
        }

        // Buscar usuario en base de datos
        Optional<Usuario> optUsuario = usuarioRepository.findById(userId);

        if (optUsuario.isEmpty()) {
            solicitud.setMensajeError("Usuario no encontrado para el token proporcionado (ID: " + userId + ")");
            solicitud.setAprobada(false);
            System.out.println("   ❌ Usuario ID " + userId + " no existe en la base de datos");
            return false;
        }

        Usuario usuario = optUsuario.get();

        // Agregar información del usuario a la solicitud para siguientes handlers
        // Solo establecer tipo de usuario si no viene ya definido
        if (solicitud.getTipoUsuario() == null || solicitud.getTipoUsuario().isEmpty()) {
            solicitud.setTipoUsuario(usuario.getTipoUsuario());
        }
        
        solicitud.agregarMetadato("usuarioId", usuario.getId());
        solicitud.agregarMetadato("usuarioNombre", usuario.getNombre());
        solicitud.agregarMetadato("usuarioEmail", usuario.getEmail());

        System.out.println("   ✅ Token válido");
        System.out.println("   • Usuario: " + usuario.getNombre());
        System.out.println("   • Email: " + usuario.getEmail());
        System.out.println("   • Tipo: " + usuario.getTipoUsuario());
        System.out.println("   ➡️  Pasando al siguiente gestor...\n");
        
        return true; // Token válido, continuar cadena
    }
}
