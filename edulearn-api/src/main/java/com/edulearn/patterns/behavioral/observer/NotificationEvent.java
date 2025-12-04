package com.edulearn.patterns.behavioral.observer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * PATRÓN OBSERVER - Evento de Notificación
 * ========================================
 * Representa un evento que será notificado a los observadores.
 * Contiene toda la información necesaria sobre el cambio ocurrido.
 */
public class NotificationEvent {

    private final String eventType;
    private final String title;
    private final String message;
    private final Integer sourceUserId;
    private final Integer targetId;
    private final String targetType;
    private final LocalDateTime timestamp;
    private final Map<String, Object> metadata;

    private NotificationEvent(Builder builder) {
        this.eventType = builder.eventType;
        this.title = builder.title;
        this.message = builder.message;
        this.sourceUserId = builder.sourceUserId;
        this.targetId = builder.targetId;
        this.targetType = builder.targetType;
        this.timestamp = LocalDateTime.now();
        this.metadata = builder.metadata;
    }

    // Getters
    public String getEventType() {
        return eventType;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Integer getSourceUserId() {
        return sourceUserId;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    /**
     * Builder Pattern para crear eventos de manera fluida
     */
    public static class Builder {
        private String eventType;
        private String title;
        private String message;
        private Integer sourceUserId;
        private Integer targetId;
        private String targetType;
        private Map<String, Object> metadata = new HashMap<>();

        public Builder eventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder sourceUserId(Integer sourceUserId) {
            this.sourceUserId = sourceUserId;
            return this;
        }

        public Builder targetId(Integer targetId) {
            this.targetId = targetId;
            return this;
        }

        public Builder targetType(String targetType) {
            this.targetType = targetType;
            return this;
        }

        public Builder addMetadata(String key, Object value) {
            this.metadata.put(key, value);
            return this;
        }

        public NotificationEvent build() {
            return new NotificationEvent(this);
        }
    }

    /**
     * Tipos de eventos del sistema
     */
    public static class EventType {
        public static final String CURSO_CREADO = "CURSO_CREADO";
        public static final String CURSO_ACTUALIZADO = "CURSO_ACTUALIZADO";
        public static final String CURSO_ELIMINADO = "CURSO_ELIMINADO";
        public static final String TAREA_CREADA = "TAREA_CREADA";
        public static final String TAREA_ACTUALIZADA = "TAREA_ACTUALIZADA";
        public static final String TAREA_ELIMINADA = "TAREA_ELIMINADA";
        public static final String TAREA_ENTREGADA = "TAREA_ENTREGADA";
        public static final String TAREA_CALIFICADA = "TAREA_CALIFICADA";
        public static final String ESTUDIANTE_INSCRITO = "ESTUDIANTE_INSCRITO";
        public static final String MATERIAL_AGREGADO = "MATERIAL_AGREGADO";
    }
}
