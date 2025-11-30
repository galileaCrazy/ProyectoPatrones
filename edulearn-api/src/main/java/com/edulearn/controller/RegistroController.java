package com.edulearn.controller;

import com.edulearn.model.Usuario;
import com.edulearn.model.Estudiante;
import com.edulearn.repository.UsuarioRepository;
import com.edulearn.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    private org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder passwordEncoder =
        new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> datos) {
        Map<String, Object> response = new HashMap<>();

        try {
            String nombre = datos.get("nombre");
            String apellidos = datos.get("apellidos");
            String email = datos.get("email");
            String password = datos.get("password");
            String tipoUsuario = datos.get("tipoUsuario");

            // Validaciones
            if (nombre == null || nombre.trim().isEmpty() ||
                apellidos == null || apellidos.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                tipoUsuario == null || tipoUsuario.trim().isEmpty()) {

                response.put("exito", false);
                response.put("mensaje", "Todos los campos son obligatorios");
                return ResponseEntity.badRequest().body(response);
            }

            // Validar que no se registren como administrador (seguridad)
            if ("administrador".equalsIgnoreCase(tipoUsuario)) {
                response.put("exito", false);
                response.put("mensaje", "No está permitido registrarse como administrador");
                return ResponseEntity.badRequest().body(response);
            }

            // Validar tipo de usuario permitido
            if (!"estudiante".equalsIgnoreCase(tipoUsuario) &&
                !"profesor".equalsIgnoreCase(tipoUsuario)) {
                response.put("exito", false);
                response.put("mensaje", "Tipo de usuario no válido");
                return ResponseEntity.badRequest().body(response);
            }

            // Verificar si el email ya existe
            if (usuarioRepository.findByEmail(email).isPresent()) {
                response.put("exito", false);
                response.put("mensaje", "El correo electrónico ya está registrado");
                return ResponseEntity.badRequest().body(response);
            }

            // Crear nuevo usuario usando Factory Method Pattern
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setApellidos(apellidos);
            nuevoUsuario.setEmail(email);
            // Encriptar contraseña con BCrypt
            nuevoUsuario.setPasswordHash(passwordEncoder.encode(password));
            nuevoUsuario.setTipoUsuario(tipoUsuario.toLowerCase());

            // Guardar usuario
            Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

            // Si es estudiante, crear registro en tabla estudiantes
            if ("estudiante".equalsIgnoreCase(tipoUsuario)) {
                Estudiante estudiante = new Estudiante();
                estudiante.setUsuario(usuarioGuardado); // Establecer relación con Usuario

                // Generar matrícula única
                String matricula = generarCodigoEstudiante();
                estudiante.setMatricula(matricula);

                estudianteRepository.save(estudiante);
            }

            // Respuesta exitosa
            response.put("exito", true);
            response.put("mensaje", "Registro exitoso");
            response.put("usuario", Map.of(
                "id", usuarioGuardado.getId(),
                "nombre", usuarioGuardado.getNombre(),
                "apellidos", usuarioGuardado.getApellidos(),
                "email", usuarioGuardado.getEmail(),
                "tipoUsuario", usuarioGuardado.getTipoUsuario()
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("exito", false);
            response.put("mensaje", "Error al registrar usuario: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    private String generarCodigoEstudiante() {
        Random random = new Random();
        int numero = 100000 + random.nextInt(900000);
        return "EST" + numero;
    }
}
