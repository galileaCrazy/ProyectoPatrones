package com.edulearn.controller;

import com.edulearn.model.Usuario;
import com.edulearn.repository.UsuarioRepository;
import com.edulearn.patterns.comportamiento.observer.NotificationOrchestrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NotificationOrchestrator notificationOrchestrator;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> credentials) {
        Map<String, Object> response = new HashMap<>();
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<Usuario> optUsuario = usuarioRepository.findByEmail(email);

        if (optUsuario.isEmpty()) {
            response.put("error", "Usuario no encontrado");
            return response;
        }

        Usuario usuario = optUsuario.get();

        // Verificar contraseña
        String storedPassword = usuario.getPasswordHash();
        boolean passwordValid = false;

        if (storedPassword != null) {
            if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$")) {
                passwordValid = passwordEncoder.matches(password, storedPassword);
            } else {
                passwordValid = storedPassword.equals(password);
            }
        }

        if (!passwordValid) {
            response.put("error", "Contraseña incorrecta");
            return response;
        }

        // ✅ REGISTRAR USUARIO EN SISTEMA DE NOTIFICACIONES
        try {
            notificationOrchestrator.registerUser(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getTipoUsuario()
            );
            System.out.println("📧 Usuario " + usuario.getNombre() + " registrado en sistema de notificaciones");
        } catch (Exception e) {
            System.err.println("⚠️ Error al registrar usuario en notificaciones: " + e.getMessage());
        }

        // Factory Method: crear respuesta según tipo de usuario
        Map<String, Object> usuarioData = new HashMap<>();
        usuarioData.put("id", usuario.getId());
        usuarioData.put("nombre", usuario.getNombre());
        usuarioData.put("email", usuario.getEmail());
        usuarioData.put("tipoUsuario", usuario.getTipoUsuario());

        response.put("usuario", usuarioData);
        response.put("dashboard", crearDashboard(usuario.getTipoUsuario()));
        response.put("permisos", crearPermisos(usuario.getTipoUsuario()));
        response.put("menu", crearMenu(usuario.getTipoUsuario()));

        return response;
    }

    // Factory Method para crear dashboard según tipo
    private String crearDashboard(String tipoUsuario) {
        switch (tipoUsuario.toLowerCase()) {
            case "estudiante": return "/dashboard/estudiante";
            case "profesor": return "/dashboard/profesor";
            case "administrador": return "/dashboard/admin";
            default: return "/dashboard/estudiante";
        }
    }

    // Factory Method para crear permisos según tipo
    private List<String> crearPermisos(String tipoUsuario) {
        switch (tipoUsuario.toLowerCase()) {
            case "estudiante":
                return Arrays.asList("ver_cursos", "inscribirse", "ver_estructura", "cambiar_vista");
            case "profesor":
                return Arrays.asList("crear_curso", "editar_curso", "clonar_curso", "decorar_curso", "videoconferencia");
            case "administrador":
                return Arrays.asList("todo");
            default:
                return Arrays.asList("ver_cursos");
        }
    }

    // Factory Method para crear menú según tipo
    private List<Map<String, String>> crearMenu(String tipoUsuario) {
        List<Map<String, String>> menu = new ArrayList<>();

        switch (tipoUsuario.toLowerCase()) {
            case "estudiante":
                menu.add(Map.of("nombre", "Mis Cursos", "patron", "Factory Method"));
                menu.add(Map.of("nombre", "Inscribirse", "patron", "Facade"));
                menu.add(Map.of("nombre", "Estructura", "patron", "Composite"));
                menu.add(Map.of("nombre", "Vista", "patron", "Bridge"));
                break;
            case "profesor":
                menu.add(Map.of("nombre", "Crear por Tipo", "patron", "Abstract Factory"));
                menu.add(Map.of("nombre", "Construir Curso", "patron", "Builder"));
                menu.add(Map.of("nombre", "Clonar Curso", "patron", "Prototype"));
                menu.add(Map.of("nombre", "Decorar Curso", "patron", "Decorator"));
                menu.add(Map.of("nombre", "Videoconferencia", "patron", "Adapter"));
                break;
            case "administrador":
                menu.add(Map.of("nombre", "Dashboard", "patron", "General"));
                menu.add(Map.of("nombre", "Gestión", "patron", "CRUD"));
                menu.add(Map.of("nombre", "Patrones", "patron", "Todos"));
                menu.add(Map.of("nombre", "Configuración", "patron", "Singleton"));
                break;
        }

        return menu;
    }
}
