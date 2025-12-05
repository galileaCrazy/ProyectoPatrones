package com.edulearn.patterns.comportamiento.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * PATRÓN FACTORY - Fábrica de Observers
 * =====================================
 * Factory que crea observers según el rol del usuario.
 * Combina el patrón Observer con el patrón Factory para
 * simplificar la creación de observers específicos.
 *
 * Beneficios:
 * - Centraliza la lógica de creación
 * - Facilita agregar nuevos tipos de observers
 * - Encapsula la lógica de selección por rol
 * - Mejora la mantenibilidad
 */
@Component
public class ObserverFactory {

    private static final Logger logger = LoggerFactory.getLogger(ObserverFactory.class);

    /**
     * Crear un observer apropiado según el rol del usuario
     *
     * @param userId ID del usuario
     * @param userName Nombre del usuario
     * @param userRole Rol del usuario (admin, profesor, estudiante)
     * @return Observer concreto según el rol
     * @throws IllegalArgumentException si el rol no es reconocido
     */
    public Observer createObserver(Integer userId, String userName, String userRole) {
        if (userRole == null || userRole.trim().isEmpty()) {
            throw new IllegalArgumentException("El rol del usuario no puede ser nulo o vacío");
        }

        Observer observer = switch (userRole.toLowerCase().trim()) {
            case "admin", "administrador" -> {
                logger.info("Creando AdministratorObserver para usuario {} ({})", userId, userName);
                yield new AdministratorObserver(userId, userName);
            }
            case "profesor", "teacher", "docente" -> {
                logger.info("Creando TeacherObserver para usuario {} ({})", userId, userName);
                yield new TeacherObserver(userId, userName);
            }
            case "estudiante", "student", "alumno" -> {
                logger.info("Creando StudentObserver para usuario {} ({})", userId, userName);
                yield new StudentObserver(userId, userName);
            }
            default -> {
                logger.warn("Rol desconocido '{}' para usuario {}. Creando UserObserver genérico",
                    userRole, userId);
                yield new UserObserver(userId, userName, userRole);
            }
        };

        return observer;
    }

    /**
     * Crear un observer específico de Administrador
     */
    public AdministratorObserver createAdministratorObserver(Integer userId, String userName) {
        logger.info("Creando AdministratorObserver para usuario {} ({})", userId, userName);
        return new AdministratorObserver(userId, userName);
    }

    /**
     * Crear un observer específico de Profesor
     */
    public TeacherObserver createTeacherObserver(Integer userId, String userName) {
        logger.info("Creando TeacherObserver para usuario {} ({})", userId, userName);
        return new TeacherObserver(userId, userName);
    }

    /**
     * Crear un observer específico de Estudiante
     */
    public StudentObserver createStudentObserver(Integer userId, String userName) {
        logger.info("Creando StudentObserver para usuario {} ({})", userId, userName);
        return new StudentObserver(userId, userName);
    }

    /**
     * Crear un observer genérico con eventos personalizados
     */
    public UserObserver createCustomObserver(Integer userId, String userName, String userRole,
                                              String... eventTypes) {
        logger.info("Creando UserObserver personalizado para usuario {} ({})", userId, userName);
        UserObserver observer = new UserObserver(userId, userName, userRole);

        // Suscribir a eventos personalizados
        for (String eventType : eventTypes) {
            observer.subscribeToEvent(eventType);
        }

        return observer;
    }

    /**
     * Validar si un rol es reconocido
     */
    public boolean isValidRole(String userRole) {
        if (userRole == null || userRole.trim().isEmpty()) {
            return false;
        }

        String role = userRole.toLowerCase().trim();
        return role.equals("admin") ||
               role.equals("administrador") ||
               role.equals("profesor") ||
               role.equals("teacher") ||
               role.equals("docente") ||
               role.equals("estudiante") ||
               role.equals("student") ||
               role.equals("alumno");
    }

    /**
     * Normalizar el rol a uno de los valores estándar
     */
    public String normalizeRole(String userRole) {
        if (userRole == null || userRole.trim().isEmpty()) {
            return "estudiante"; // rol por defecto
        }

        return switch (userRole.toLowerCase().trim()) {
            case "admin", "administrador" -> "admin";
            case "profesor", "teacher", "docente" -> "profesor";
            case "estudiante", "student", "alumno" -> "estudiante";
            default -> userRole;
        };
    }
}
