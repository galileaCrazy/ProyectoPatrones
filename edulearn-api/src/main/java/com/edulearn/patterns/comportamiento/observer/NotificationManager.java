package com.edulearn.patterns.comportamiento.observer;

import com.edulearn.model.Notificacion;
import com.edulearn.repository.NotificacionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * PATRÓN OBSERVER - NotificationManager Avanzado
 * ==============================================
 * Gestor mejorado de notificaciones que soporta:
 * - Suscripciones dinámicas por curso
 * - Filtrado contextual de observadores
 * - Notificaciones dirigidas y broadcast
 * - Gestión de grupos de observadores
 *
 * Casos de uso:
 * - Notificar solo a estudiantes de un curso específico
 * - Notificar a todos los administradores del sistema
 * - Notificar al profesor de un curso concreto
 */
@Component
public class NotificationManager implements Subject {

    private static final Logger logger = LoggerFactory.getLogger(NotificationManager.class);

    // Lista global de todos los observadores
    private final List<Observer> globalObservers = new CopyOnWriteArrayList<>();

    // Mapa de observadores por curso: cursoId -> List<Observer>
    private final Map<Integer, List<Observer>> courseObservers = new ConcurrentHashMap<>();

    // Mapa de observadores por rol: role -> List<Observer>
    private final Map<String, List<Observer>> roleObservers = new ConcurrentHashMap<>();

    // Mapa de profesor por curso: cursoId -> profesorId
    private final Map<Integer, Integer> courseTeachers = new ConcurrentHashMap<>();

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired(required = false)
    private ObserverFactory observerFactory;

    // ===========================
    // MÉTODOS BÁSICOS DEL SUBJECT
    // ===========================

    @Override
    public void attach(Observer observer) {
        if (!globalObservers.contains(observer)) {
            globalObservers.add(observer);
            logger.info("Observer {} registrado globalmente. Total: {}",
                observer.getObserverId(), globalObservers.size());
        }
    }

    @Override
    public void detach(Observer observer) {
        if (globalObservers.remove(observer)) {
            // Remover de todos los grupos
            courseObservers.values().forEach(list -> list.remove(observer));
            roleObservers.values().forEach(list -> list.remove(observer));

            logger.info("Observer {} removido completamente", observer.getObserverId());
        }
    }

    @Override
    public void notifyObservers(NotificationEvent event) {
        logger.info("Notificación broadcast: {} a {} observadores",
            event.getEventType(), globalObservers.size());

        int notificationsCreated = 0;

        for (Observer observer : globalObservers) {
            try {
                if (observer.isInterestedIn(event.getEventType())) {
                    observer.update(event);
                    saveNotificationToDatabase(observer, event);
                    notificationsCreated++;
                }
            } catch (Exception e) {
                logger.error("Error al notificar observer {}: {}",
                    observer.getObserverId(), e.getMessage());
            }
        }

        logger.info("Notificaciones broadcast creadas: {}", notificationsCreated);
    }

    // ======================================
    // MÉTODOS AVANZADOS - GESTIÓN POR CURSO
    // ======================================

    /**
     * Suscribir un observador a un curso específico
     */
    public void attachToCourse(Observer observer, Integer cursoId) {
        courseObservers.computeIfAbsent(cursoId, k -> new CopyOnWriteArrayList<>())
            .add(observer);

        logger.info("Observer {} suscrito al curso {}", observer.getObserverId(), cursoId);
    }

    /**
     * Desuscribir un observador de un curso específico
     */
    public void detachFromCourse(Observer observer, Integer cursoId) {
        List<Observer> observers = courseObservers.get(cursoId);
        if (observers != null) {
            observers.remove(observer);
            logger.info("Observer {} desuscrito del curso {}", observer.getObserverId(), cursoId);
        }
    }

    /**
     * Notificar solo a observadores de un curso específico
     */
    public void notifyCourseObservers(Integer cursoId, NotificationEvent event) {
        List<Observer> observers = courseObservers.getOrDefault(cursoId, Collections.emptyList());

        logger.info("Notificando evento {} a {} observadores del curso {}",
            event.getEventType(), observers.size(), cursoId);

        int notificationsCreated = 0;

        for (Observer observer : observers) {
            try {
                if (observer.isInterestedIn(event.getEventType())) {
                    observer.update(event);
                    saveNotificationToDatabase(observer, event);
                    notificationsCreated++;
                }
            } catch (Exception e) {
                logger.error("Error al notificar observer {} del curso {}: {}",
                    observer.getObserverId(), cursoId, e.getMessage());
            }
        }

        logger.info("Notificaciones del curso {} creadas: {}", cursoId, notificationsCreated);
    }

    // ===================================
    // MÉTODOS AVANZADOS - GESTIÓN POR ROL
    // ===================================

    /**
     * Registrar un observador en un grupo de rol
     */
    public void attachToRole(Observer observer, String role) {
        // Normalizar el rol a minúsculas para consistencia
        String normalizedRole = role.toLowerCase().trim();

        roleObservers.computeIfAbsent(normalizedRole, k -> new CopyOnWriteArrayList<>())
            .add(observer);

        logger.info("Observer {} registrado en rol {} (normalizado: {})",
            observer.getObserverId(), role, normalizedRole);
    }

    /**
     * Notificar solo a observadores de un rol específico
     * Útil para notificar a todos los administradores
     */
    public void notifyRoleObservers(String role, NotificationEvent event) {
        // Normalizar el rol para la búsqueda
        String normalizedRole = role.toLowerCase().trim();
        List<Observer> observers = roleObservers.getOrDefault(normalizedRole, Collections.emptyList());

        logger.info("Notificando evento {} a {} observadores con rol {} (normalizado: {})",
            event.getEventType(), observers.size(), role, normalizedRole);

        int notificationsCreated = 0;

        for (Observer observer : observers) {
            try {
                if (observer.isInterestedIn(event.getEventType())) {
                    observer.update(event);
                    saveNotificationToDatabase(observer, event);
                    notificationsCreated++;
                }
            } catch (Exception e) {
                logger.error("Error al notificar observer {} del rol {}: {}",
                    observer.getObserverId(), role, e.getMessage());
            }
        }

        logger.info("Notificaciones al rol {} creadas: {}", role, notificationsCreated);
    }

    // =========================================
    // MÉTODOS AVANZADOS - NOTIFICACIONES DIRECTAS
    // =========================================

    /**
     * Notificar a un usuario específico por su ID
     */
    public void notifySpecificUser(Integer userId, NotificationEvent event) {
        globalObservers.stream()
            .filter(obs -> obs.getObserverId().equals(userId))
            .findFirst()
            .ifPresent(observer -> {
                try {
                    observer.update(event);
                    saveNotificationToDatabase(observer, event);
                    logger.info("Notificación enviada a usuario específico: {}", userId);
                } catch (Exception e) {
                    logger.error("Error al notificar usuario {}: {}", userId, e.getMessage());
                }
            });
    }

    /**
     * Notificar a múltiples usuarios específicos
     */
    public void notifySpecificUsers(List<Integer> userIds, NotificationEvent event) {
        Set<Integer> userIdSet = new HashSet<>(userIds);

        globalObservers.stream()
            .filter(obs -> userIdSet.contains(obs.getObserverId()))
            .forEach(observer -> {
                try {
                    if (observer.isInterestedIn(event.getEventType())) {
                        observer.update(event);
                        saveNotificationToDatabase(observer, event);
                    }
                } catch (Exception e) {
                    logger.error("Error al notificar observer {}: {}",
                        observer.getObserverId(), e.getMessage());
                }
            });

        logger.info("Notificaciones enviadas a {} usuarios específicos", userIds.size());
    }

    // ==========================================
    // GESTIÓN DE PROFESORES POR CURSO
    // ==========================================

    /**
     * Registrar qué profesor está asignado a un curso
     */
    public void registerCourseTeacher(Integer cursoId, Integer profesorId) {
        courseTeachers.put(cursoId, profesorId);
        logger.info("Profesor {} asignado al curso {}", profesorId, cursoId);
    }

    /**
     * Notificar al profesor de un curso específico
     */
    public void notifyCourseTeacher(Integer cursoId, NotificationEvent event) {
        Integer profesorId = courseTeachers.get(cursoId);

        if (profesorId == null) {
            logger.warn("No se encontró profesor asignado al curso {}. No se puede notificar.", cursoId);
            return;
        }

        // Intentar notificar directamente si el profesor ya está en globalObservers
        boolean notified = globalObservers.stream()
            .anyMatch(obs -> {
                if (obs.getObserverId().equals(profesorId)) {
                    try {
                        if (obs.isInterestedIn(event.getEventType())) {
                            obs.update(event);
                        } else {
                            // Forzar entrega aunque no tenga el evento suscrito explícitamente
                            obs.update(event);
                        }
                        saveNotificationToDatabase(obs, event);
                        logger.info("Notificación enviada al profesor {} del curso {}", profesorId, cursoId);
                        return true;
                    } catch (Exception e) {
                        logger.error("Error al notificar profesor {}: {}", profesorId, e.getMessage());
                    }
                }
                return false;
            });

        if (notified) return;

        // Fallback: crear y registrar dinámicamente un TeacherObserver si no existe
        try {
            if (observerFactory != null) {
                Observer tempTeacher = observerFactory.createTeacherObserver(profesorId, "Profesor " + profesorId);
                attach(tempTeacher); // registrar globalmente
                logger.warn("Profesor {} no estaba registrado. Registrado dinámicamente para notificación.", profesorId);

                // Enviar la notificación
                if (tempTeacher.isInterestedIn(event.getEventType())) {
                    tempTeacher.update(event);
                } else {
                    // Forzar entrega en fallback aun si el interés no está explícito
                    tempTeacher.update(event);
                }
                saveNotificationToDatabase(tempTeacher, event);
                logger.info("Notificación (fallback) enviada al profesor {} del curso {}", profesorId, cursoId);
            } else {
                logger.error("ObserverFactory no disponible. No se pudo registrar dinámicamente al profesor {}", profesorId);
            }
        } catch (Exception ex) {
            logger.error("Error en fallback al notificar profesor {} del curso {}: {}", profesorId, cursoId, ex.getMessage());
        }
    }

    // ==========================================
    // MÉTODOS DE UTILIDAD Y CONSULTA
    // ==========================================

    /**
     * Obtener observadores de un curso
     */
    public List<Observer> getCourseObservers(Integer cursoId) {
        return new ArrayList<>(courseObservers.getOrDefault(cursoId, Collections.emptyList()));
    }

    /**
     * Obtener observadores de un rol
     */
    public List<Observer> getRoleObservers(String role) {
        return new ArrayList<>(roleObservers.getOrDefault(role.toLowerCase(), Collections.emptyList()));
    }

    /**
     * Verificar si un usuario está suscrito a un curso
     */
    public boolean isUserSubscribedToCourse(Integer userId, Integer cursoId) {
        List<Observer> observers = courseObservers.get(cursoId);
        if (observers == null) return false;

        return observers.stream()
            .anyMatch(obs -> obs.getObserverId().equals(userId));
    }

    /**
     * Obtener todos los cursos a los que está suscrito un usuario
     */
    public List<Integer> getUserSubscribedCourses(Integer userId) {
        return courseObservers.entrySet().stream()
            .filter(entry -> entry.getValue().stream()
                .anyMatch(obs -> obs.getObserverId().equals(userId)))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    /**
     * Obtener estadísticas del manager
     */
    public Map<String, Object> getStatistics() {
        return Map.of(
            "totalObservers", globalObservers.size(),
            "coursesWithObservers", courseObservers.size(),
            "rolesWithObservers", roleObservers.size(),
            "registeredTeachers", courseTeachers.size()
        );
    }

    /**
     * Limpiar todas las suscripciones
     */
    public void clearAll() {
        globalObservers.clear();
        courseObservers.clear();
        roleObservers.clear();
        courseTeachers.clear();
        logger.warn("Todas las suscripciones han sido limpiadas");
    }

    // ==========================================
    // PERSISTENCIA EN BASE DE DATOS
    // ==========================================

    private void saveNotificationToDatabase(Observer observer, NotificationEvent event) {
        try {
            Notificacion notificacion = new Notificacion();
            notificacion.setTipo("INTERNA");
            notificacion.setDestinatario(observer.getObserverId().toString());
            notificacion.setAsunto(event.getTitle());
            notificacion.setMensaje(event.getMessage());
            notificacion.setEstado("NO_LEIDA");
            notificacion.setFechaCreacion(event.getTimestamp());

            notificacionRepository.save(notificacion);
        } catch (Exception e) {
            logger.error("Error al guardar notificación en BD: {}", e.getMessage());
        }
    }
}
