package com.edulearn.config;

import com.edulearn.model.Usuario;
import com.edulearn.patterns.comportamiento.observer.NotificationOrchestrator;
import com.edulearn.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Inicializador del Sistema de Notificaciones
 * ===========================================
 * Este componente se ejecuta automáticamente al iniciar la aplicación
 * y registra a todos los usuarios activos en el sistema de notificaciones.
 *
 * IMPORTANTE: Esto es CRÍTICO para que las notificaciones funcionen.
 * Cada usuario debe estar registrado como Observer para recibir notificaciones.
 */
@Component
public class NotificationInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(NotificationInitializer.class);

    @Autowired
    private NotificationOrchestrator orchestrator;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        logger.info("========================================");
        logger.info("Inicializando Sistema de Notificaciones");
        logger.info("========================================");

        try {
            // Obtener todos los usuarios
            var usuarios = usuarioRepository.findAll();
            logger.info("Total de usuarios encontrados: {}", usuarios.size());

            int registrados = 0;

            // Registrar cada usuario como observer
            for (Usuario usuario : usuarios) {
                try {
                    // Registrar globalmente
                    orchestrator.registerUser(
                        usuario.getId(),
                        usuario.getNombre(),
                        usuario.getTipoUsuario()
                    );

                    // Si es profesor, registrarlo para sus cursos
                    // (Esto se podría mejorar consultando los cursos del profesor)

                    registrados++;

                    logger.debug("Usuario registrado: {} ({}) con rol {}",
                        usuario.getId(), usuario.getNombre(), usuario.getTipoUsuario());

                } catch (Exception e) {
                    logger.error("Error al registrar usuario {}: {}",
                        usuario.getId(), e.getMessage());
                }
            }

            logger.info("✓ {} usuarios registrados exitosamente", registrados);

            // Mostrar estadísticas
            orchestrator.logStatistics();

            logger.info("========================================");
            logger.info("Sistema de Notificaciones Inicializado");
            logger.info("========================================");

        } catch (Exception e) {
            logger.error("Error al inicializar sistema de notificaciones: {}", e.getMessage(), e);
        }
    }
}
