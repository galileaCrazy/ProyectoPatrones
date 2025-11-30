# Ejemplo de Implementación: Patrón Observer
## Sistema de Notificaciones para Cambios en Cursos

---

## 📋 Objetivo

Implementar el patrón Observer para notificar automáticamente a estudiantes inscritos cuando un profesor realiza cambios en un curso (actualización de contenido, nueva evaluación, cambio de fechas, etc.).

---

## 🏗️ Estructura de Archivos

```
edulearn-api/src/main/java/com/edulearn/patterns/behavioral/observer/
├── IObserver.java
├── ISubject.java
├── CursoSubject.java
├── EstudianteObserver.java
├── ProfesorObserver.java
└── AdminObserver.java
```

---

## 💻 Código Completo

### 1. IObserver.java (Interfaz)

```java
package com.edulearn.patterns.behavioral.observer;

/**
 * Interfaz Observer
 * Define el contrato para los observadores que serán notificados
 */
public interface IObserver {

    /**
     * Método llamado cuando el Subject notifica un cambio
     * @param evento Tipo de evento ocurrido
     * @param data Datos relacionados al evento
     */
    void actualizar(String evento, Object data);

    /**
     * Obtener identificador del observador
     */
    String getId();
}
```

---

### 2. ISubject.java (Interfaz)

```java
package com.edulearn.patterns.behavioral.observer;

/**
 * Interfaz Subject
 * Define el contrato para los objetos observables
 */
public interface ISubject {

    /**
     * Agregar un observador a la lista
     */
    void agregarObservador(IObserver observer);

    /**
     * Eliminar un observador de la lista
     */
    void eliminarObservador(IObserver observer);

    /**
     * Notificar a todos los observadores registrados
     */
    void notificarObservadores(String evento, Object data);
}
```

---

### 3. CursoSubject.java (Concrete Subject)

```java
package com.edulearn.patterns.behavioral.observer;

import com.edulearn.model.Curso;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Subject concreto: Curso que puede ser observado
 * Notifica a los observadores cuando cambia
 */
@Component
public class CursoSubject implements ISubject {

    private static final Logger logger = Logger.getLogger(CursoSubject.class.getName());

    private List<IObserver> observadores;
    private Curso curso;

    public CursoSubject() {
        this.observadores = new ArrayList<>();
    }

    public CursoSubject(Curso curso) {
        this.curso = curso;
        this.observadores = new ArrayList<>();
    }

    @Override
    public void agregarObservador(IObserver observer) {
        if (!observadores.contains(observer)) {
            observadores.add(observer);
            logger.info("Observador agregado: " + observer.getId());
        }
    }

    @Override
    public void eliminarObservador(IObserver observer) {
        observadores.remove(observer);
        logger.info("Observador eliminado: " + observer.getId());
    }

    @Override
    public void notificarObservadores(String evento, Object data) {
        logger.info("Notificando evento: " + evento + " a " + observadores.size() + " observadores");

        for (IObserver observer : observadores) {
            try {
                observer.actualizar(evento, data);
            } catch (Exception e) {
                logger.severe("Error al notificar observador " + observer.getId() + ": " + e.getMessage());
            }
        }
    }

    // ========== MÉTODOS DE NEGOCIO ==========

    /**
     * Actualizar contenido del curso
     */
    public void actualizarContenido(String nuevoContenido) {
        String contenidoAnterior = this.curso.getDescripcion();
        this.curso.setDescripcion(nuevoContenido);

        notificarObservadores("CONTENIDO_ACTUALIZADO",
            new ContenidoActualizadoData(contenidoAnterior, nuevoContenido));
    }

    /**
     * Agregar nueva evaluación
     */
    public void agregarEvaluacion(String nombreEvaluacion, String fechaEntrega) {
        notificarObservadores("NUEVA_EVALUACION",
            new NuevaEvaluacionData(nombreEvaluacion, fechaEntrega));
    }

    /**
     * Cambiar fecha límite
     */
    public void cambiarFechaLimite(String actividad, String nuevaFecha) {
        notificarObservadores("CAMBIO_FECHA",
            new CambioFechaData(actividad, nuevaFecha));
    }

    /**
     * Publicar anuncio
     */
    public void publicarAnuncio(String titulo, String mensaje) {
        notificarObservadores("NUEVO_ANUNCIO",
            new AnuncioData(titulo, mensaje));
    }

    /**
     * Actualizar calificación
     */
    public void actualizarCalificacion(Long estudianteId, String evaluacion, double calificacion) {
        notificarObservadores("CALIFICACION_ACTUALIZADA",
            new CalificacionData(estudianteId, evaluacion, calificacion));
    }

    // Getters y Setters
    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public int getCantidadObservadores() {
        return observadores.size();
    }

    // ========== CLASES DE DATOS ==========

    public static class ContenidoActualizadoData {
        private String contenidoAnterior;
        private String contenidoNuevo;

        public ContenidoActualizadoData(String contenidoAnterior, String contenidoNuevo) {
            this.contenidoAnterior = contenidoAnterior;
            this.contenidoNuevo = contenidoNuevo;
        }

        public String getContenidoAnterior() { return contenidoAnterior; }
        public String getContenidoNuevo() { return contenidoNuevo; }
    }

    public static class NuevaEvaluacionData {
        private String nombre;
        private String fechaEntrega;

        public NuevaEvaluacionData(String nombre, String fechaEntrega) {
            this.nombre = nombre;
            this.fechaEntrega = fechaEntrega;
        }

        public String getNombre() { return nombre; }
        public String getFechaEntrega() { return fechaEntrega; }
    }

    public static class CambioFechaData {
        private String actividad;
        private String nuevaFecha;

        public CambioFechaData(String actividad, String nuevaFecha) {
            this.actividad = actividad;
            this.nuevaFecha = nuevaFecha;
        }

        public String getActividad() { return actividad; }
        public String getNuevaFecha() { return nuevaFecha; }
    }

    public static class AnuncioData {
        private String titulo;
        private String mensaje;

        public AnuncioData(String titulo, String mensaje) {
            this.titulo = titulo;
            this.mensaje = mensaje;
        }

        public String getTitulo() { return titulo; }
        public String getMensaje() { return mensaje; }
    }

    public static class CalificacionData {
        private Long estudianteId;
        private String evaluacion;
        private double calificacion;

        public CalificacionData(Long estudianteId, String evaluacion, double calificacion) {
            this.estudianteId = estudianteId;
            this.evaluacion = evaluacion;
            this.calificacion = calificacion;
        }

        public Long getEstudianteId() { return estudianteId; }
        public String getEvaluacion() { return evaluacion; }
        public double getCalificacion() { return calificacion; }
    }
}
```

---

### 4. EstudianteObserver.java (Concrete Observer)

```java
package com.edulearn.patterns.behavioral.observer;

import com.edulearn.model.Estudiante;
import com.edulearn.patterns.behavioral.observer.CursoSubject.*;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Observer concreto: Estudiante
 * Recibe notificaciones sobre cambios en los cursos inscritos
 */
@Component
public class EstudianteObserver implements IObserver {

    private static final Logger logger = Logger.getLogger(EstudianteObserver.class.getName());

    private Estudiante estudiante;
    private String email;

    public EstudianteObserver() {}

    public EstudianteObserver(Estudiante estudiante) {
        this.estudiante = estudiante;
        this.email = estudiante.getCorreo();
    }

    @Override
    public void actualizar(String evento, Object data) {
        logger.info("Estudiante " + estudiante.getNombre() + " notificado: " + evento);

        switch (evento) {
            case "CONTENIDO_ACTUALIZADO":
                manejarContenidoActualizado((ContenidoActualizadoData) data);
                break;

            case "NUEVA_EVALUACION":
                manejarNuevaEvaluacion((NuevaEvaluacionData) data);
                break;

            case "CAMBIO_FECHA":
                manejarCambioFecha((CambioFechaData) data);
                break;

            case "NUEVO_ANUNCIO":
                manejarNuevoAnuncio((AnuncioData) data);
                break;

            case "CALIFICACION_ACTUALIZADA":
                CalificacionData calData = (CalificacionData) data;
                if (calData.getEstudianteId().equals(estudiante.getId())) {
                    manejarCalificacionActualizada(calData);
                }
                break;

            default:
                logger.warning("Evento desconocido: " + evento);
        }
    }

    private void manejarContenidoActualizado(ContenidoActualizadoData data) {
        String mensaje = "El contenido del curso ha sido actualizado.\n\n" +
                        "Revisa los nuevos materiales disponibles.";
        enviarNotificacion("Contenido Actualizado", mensaje);
    }

    private void manejarNuevaEvaluacion(NuevaEvaluacionData data) {
        String mensaje = "Nueva evaluación disponible:\n\n" +
                        "Nombre: " + data.getNombre() + "\n" +
                        "Fecha de entrega: " + data.getFechaEntrega() + "\n\n" +
                        "¡No olvides completarla a tiempo!";
        enviarNotificacion("Nueva Evaluación", mensaje);
    }

    private void manejarCambioFecha(CambioFechaData data) {
        String mensaje = "Cambio de fecha:\n\n" +
                        "Actividad: " + data.getActividad() + "\n" +
                        "Nueva fecha: " + data.getNuevaFecha();
        enviarNotificacion("Cambio de Fecha", mensaje);
    }

    private void manejarNuevoAnuncio(AnuncioData data) {
        String mensaje = data.getMensaje();
        enviarNotificacion(data.getTitulo(), mensaje);
    }

    private void manejarCalificacionActualizada(CalificacionData data) {
        String mensaje = "Tu calificación ha sido actualizada:\n\n" +
                        "Evaluación: " + data.getEvaluacion() + "\n" +
                        "Calificación: " + data.getCalificacion();
        enviarNotificacion("Calificación Actualizada", mensaje);
    }

    /**
     * Enviar notificación por email (simulado)
     */
    private void enviarNotificacion(String titulo, String mensaje) {
        // Aquí iría la lógica real de envío de email
        // Por ahora, solo logueamos
        logger.info("=== EMAIL ENVIADO ===");
        logger.info("Para: " + email);
        logger.info("Asunto: " + titulo);
        logger.info("Mensaje: " + mensaje);
        logger.info("====================");

        // TODO: Integrar con servicio de email real
        // emailService.enviar(email, titulo, mensaje);
    }

    @Override
    public String getId() {
        return "ESTUDIANTE_" + estudiante.getId();
    }

    // Getters y Setters
    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        this.email = estudiante.getCorreo();
    }
}
```

---

### 5. ProfesorObserver.java (Concrete Observer)

```java
package com.edulearn.patterns.behavioral.observer;

import com.edulearn.model.Usuario;
import com.edulearn.patterns.behavioral.observer.CursoSubject.*;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Observer concreto: Profesor
 * Recibe notificaciones sobre actividad en sus cursos
 */
@Component
public class ProfesorObserver implements IObserver {

    private static final Logger logger = Logger.getLogger(ProfesorObserver.class.getName());

    private Usuario profesor;
    private String email;

    public ProfesorObserver() {}

    public ProfesorObserver(Usuario profesor) {
        this.profesor = profesor;
        this.email = profesor.getCorreo();
    }

    @Override
    public void actualizar(String evento, Object data) {
        logger.info("Profesor " + profesor.getNombre() + " notificado: " + evento);

        // Profesores solo reciben notificaciones de ciertos eventos
        switch (evento) {
            case "NUEVA_EVALUACION":
                NuevaEvaluacionData evalData = (NuevaEvaluacionData) data;
                enviarResumen("Nueva evaluación creada: " + evalData.getNombre());
                break;

            case "CALIFICACION_ACTUALIZADA":
                CalificacionData calData = (CalificacionData) data;
                enviarResumen("Calificación registrada para estudiante: " + calData.getEstudianteId());
                break;

            default:
                // Otros eventos no son relevantes para el profesor
                break;
        }
    }

    private void enviarResumen(String mensaje) {
        logger.info("=== RESUMEN PARA PROFESOR ===");
        logger.info("Profesor: " + profesor.getNombre());
        logger.info("Mensaje: " + mensaje);
        logger.info("============================");
    }

    @Override
    public String getId() {
        return "PROFESOR_" + profesor.getId();
    }

    // Getters y Setters
    public Usuario getProfesor() {
        return profesor;
    }

    public void setProfesor(Usuario profesor) {
        this.profesor = profesor;
        this.email = profesor.getCorreo();
    }
}
```

---

### 6. AdminObserver.java (Concrete Observer)

```java
package com.edulearn.patterns.behavioral.observer;

import com.edulearn.model.Usuario;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 * Observer concreto: Administrador
 * Recibe todas las notificaciones para auditoría
 */
@Component
public class AdminObserver implements IObserver {

    private static final Logger logger = Logger.getLogger(AdminObserver.class.getName());

    private Usuario admin;

    public AdminObserver() {}

    public AdminObserver(Usuario admin) {
        this.admin = admin;
    }

    @Override
    public void actualizar(String evento, Object data) {
        // Administradores registran todo para auditoría
        registrarEnAuditoria(evento, data);
    }

    private void registrarEnAuditoria(String evento, Object data) {
        logger.info("=== REGISTRO DE AUDITORÍA ===");
        logger.info("Evento: " + evento);
        logger.info("Datos: " + data.toString());
        logger.info("Timestamp: " + System.currentTimeMillis());
        logger.info("===========================");

        // TODO: Guardar en base de datos de auditoría
    }

    @Override
    public String getId() {
        return "ADMIN_" + admin.getId();
    }

    // Getters y Setters
    public Usuario getAdmin() {
        return admin;
    }

    public void setAdmin(Usuario admin) {
        this.admin = admin;
    }
}
```

---

## 🔌 Integración con Spring Service

### CursoService.java

```java
package com.edulearn.service;

import com.edulearn.model.Curso;
import com.edulearn.model.Estudiante;
import com.edulearn.model.Inscripcion;
import com.edulearn.repository.CursoRepository;
import com.edulearn.repository.InscripcionRepository;
import com.edulearn.patterns.behavioral.observer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    /**
     * Actualizar contenido del curso y notificar estudiantes
     */
    public void actualizarContenido(Long cursoId, String nuevoContenido) {
        // Obtener curso
        Curso curso = cursoRepository.findById(cursoId)
            .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        // Crear subject
        CursoSubject subject = new CursoSubject(curso);

        // Obtener estudiantes inscritos
        List<Inscripcion> inscripciones = inscripcionRepository.findByCursoId(cursoId);

        // Agregar observadores (estudiantes)
        for (Inscripcion inscripcion : inscripciones) {
            Estudiante estudiante = inscripcion.getEstudiante();
            EstudianteObserver observer = new EstudianteObserver(estudiante);
            subject.agregarObservador(observer);
        }

        // Agregar observador administrador (para auditoría)
        // AdminObserver adminObserver = new AdminObserver(adminUser);
        // subject.agregarObservador(adminObserver);

        // Actualizar contenido (esto notificará automáticamente)
        subject.actualizarContenido(nuevoContenido);

        // Guardar cambios en BD
        cursoRepository.save(curso);
    }

    /**
     * Agregar nueva evaluación y notificar
     */
    public void agregarEvaluacion(Long cursoId, String nombreEvaluacion, String fechaEntrega) {
        Curso curso = cursoRepository.findById(cursoId)
            .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        CursoSubject subject = new CursoSubject(curso);

        // Agregar observadores
        List<Inscripcion> inscripciones = inscripcionRepository.findByCursoId(cursoId);
        for (Inscripcion inscripcion : inscripciones) {
            subject.agregarObservador(new EstudianteObserver(inscripcion.getEstudiante()));
        }

        // Notificar nueva evaluación
        subject.agregarEvaluacion(nombreEvaluacion, fechaEntrega);
    }

    /**
     * Publicar anuncio en el curso
     */
    public void publicarAnuncio(Long cursoId, String titulo, String mensaje) {
        Curso curso = cursoRepository.findById(cursoId)
            .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        CursoSubject subject = new CursoSubject(curso);

        // Agregar observadores
        List<Inscripcion> inscripciones = inscripcionRepository.findByCursoId(cursoId);
        for (Inscripcion inscripcion : inscripciones) {
            subject.agregarObservador(new EstudianteObserver(inscripcion.getEstudiante()));
        }

        // Publicar anuncio
        subject.publicarAnuncio(titulo, mensaje);
    }
}
```

---

## 🎮 Controller REST

### CursoController.java

```java
package com.edulearn.controller;

import com.edulearn.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    /**
     * Actualizar contenido del curso
     * POST /api/cursos/1/contenido
     */
    @PostMapping("/{id}/contenido")
    public ResponseEntity<?> actualizarContenido(
        @PathVariable Long id,
        @RequestBody ContenidoRequest request
    ) {
        cursoService.actualizarContenido(id, request.getContenido());
        return ResponseEntity.ok("Contenido actualizado y estudiantes notificados");
    }

    /**
     * Agregar evaluación
     * POST /api/cursos/1/evaluaciones
     */
    @PostMapping("/{id}/evaluaciones")
    public ResponseEntity<?> agregarEvaluacion(
        @PathVariable Long id,
        @RequestBody EvaluacionRequest request
    ) {
        cursoService.agregarEvaluacion(
            id,
            request.getNombre(),
            request.getFechaEntrega()
        );
        return ResponseEntity.ok("Evaluación agregada y estudiantes notificados");
    }

    /**
     * Publicar anuncio
     * POST /api/cursos/1/anuncios
     */
    @PostMapping("/{id}/anuncios")
    public ResponseEntity<?> publicarAnuncio(
        @PathVariable Long id,
        @RequestBody AnuncioRequest request
    ) {
        cursoService.publicarAnuncio(id, request.getTitulo(), request.getMensaje());
        return ResponseEntity.ok("Anuncio publicado y estudiantes notificados");
    }

    // DTOs
    static class ContenidoRequest {
        private String contenido;
        public String getContenido() { return contenido; }
        public void setContenido(String contenido) { this.contenido = contenido; }
    }

    static class EvaluacionRequest {
        private String nombre;
        private String fechaEntrega;
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public String getFechaEntrega() { return fechaEntrega; }
        public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }
    }

    static class AnuncioRequest {
        private String titulo;
        private String mensaje;
        public String getTitulo() { return titulo; }
        public void setTitulo(String titulo) { this.titulo = titulo; }
        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    }
}
```

---

## 🧪 Ejemplo de Uso

```java
// En el servicio
public void ejemplo() {
    // Crear curso
    Curso curso = new Curso();
    curso.setNombre("Programación Java");

    // Crear subject
    CursoSubject subject = new CursoSubject(curso);

    // Crear estudiantes
    Estudiante estudiante1 = new Estudiante();
    estudiante1.setId(1L);
    estudiante1.setNombre("Juan Pérez");
    estudiante1.setCorreo("juan@example.com");

    Estudiante estudiante2 = new Estudiante();
    estudiante2.setId(2L);
    estudiante2.setNombre("María García");
    estudiante2.setCorreo("maria@example.com");

    // Crear observadores
    EstudianteObserver obs1 = new EstudianteObserver(estudiante1);
    EstudianteObserver obs2 = new EstudianteObserver(estudiante2);

    // Agregar observadores
    subject.agregarObservador(obs1);
    subject.agregarObservador(obs2);

    // Realizar cambios (automáticamente notifica)
    subject.actualizarContenido("Nuevo tema: Herencia y Polimorfismo");
    subject.agregarEvaluacion("Quiz 3", "2025-12-15");
    subject.publicarAnuncio("Clase extra", "Habrá clase de refuerzo el sábado");
}
```

---

## 📊 Diagrama de Secuencia

```
Profesor                 CursoSubject              EstudianteObserver1        EstudianteObserver2
   |                          |                            |                         |
   |--actualizarContenido()-->|                            |                         |
   |                          |                            |                         |
   |                          |--actualizar(evento,data)-->|                         |
   |                          |                            |                         |
   |                          |                            |--enviarNotificacion()-->|
   |                          |                            |                         |
   |                          |--actualizar(evento,data)----------------------->|    |
   |                          |                            |                    |    |
   |                          |                            |                    |--enviarNotificacion()
   |                          |                            |                    |    |
   |<------ Confirmación -----|                            |                    |    |
   |                          |                            |                    |    |
```

---

## ✅ Ventajas de esta Implementación

1. **Desacoplamiento**: Curso no conoce a los estudiantes directamente
2. **Escalabilidad**: Fácil agregar nuevos tipos de observadores
3. **Flexibilidad**: Diferentes observadores pueden reaccionar diferente al mismo evento
4. **Mantenibilidad**: Lógica de notificación centralizada
5. **Extensibilidad**: Fácil agregar nuevos tipos de eventos

---

## 🚀 Próximos Pasos

1. Integrar con servicio de email real (SendGrid, SES, etc.)
2. Agregar persistencia de notificaciones en BD
3. Implementar preferencias de notificación por usuario
4. Agregar notificaciones push para móviles
5. Implementar cola de mensajes (RabbitMQ/Kafka) para procesar notificaciones asíncronamente

---

**Autor:** EduLearn Development Team
**Fecha:** 2025-11-29
**Patrón:** Observer (Behavioral Pattern)
