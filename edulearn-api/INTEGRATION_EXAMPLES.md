# 🔧 Ejemplos de Integración - Patrón Observer

## 📚 Tabla de Contenidos

1. [Resumen de Cambios](#resumen-de-cambios)
2. [Ejemplo 1: Servicio de Cursos](#ejemplo-1-servicio-de-cursos)
3. [Ejemplo 2: Servicio de Materiales](#ejemplo-2-servicio-de-materiales)
4. [Ejemplo 3: Servicio de Evaluaciones](#ejemplo-3-servicio-de-evaluaciones)
5. [Ejemplo 4: Servicio de Inscripciones](#ejemplo-4-servicio-de-inscripciones)
6. [Ejemplo 5: Servicio de Calificaciones](#ejemplo-5-servicio-de-calificaciones)
7. [Inicialización del Sistema](#inicialización-del-sistema)
8. [Testing](#testing)

---

## ✅ Resumen de Cambios

### **Archivos Creados**

#### **Patrón Observer Clásico**
```
✅ NotificationManager.java          - Gestor avanzado de notificaciones
✅ AbstractUserObserver.java         - Clase base para observers
✅ AdministratorObserver.java        - Observer para administradores
✅ TeacherObserver.java              - Observer para profesores
✅ StudentObserver.java              - Observer para estudiantes
✅ ObserverFactory.java              - Factory para crear observers
✅ NotificationOrchestrator.java     - Facade de alto nivel
```

#### **Spring Events (Alternativa)**
```
✅ CourseCreatedEvent.java
✅ MaterialUploadedEvent.java
✅ AssignmentCreatedEvent.java
✅ StudentEnrolledEvent.java
✅ AssignmentGradedEvent.java
✅ SpringEventPublisher.java         - Publicador de eventos
✅ CourseEventListener.java
✅ AssignmentEventListener.java
✅ EnrollmentEventListener.java
```

#### **Documentación**
```
✅ OBSERVER_PATTERN_GUIDE.md         - Guía completa del patrón
✅ INTEGRATION_EXAMPLES.md           - Este archivo
```

### **Archivos Corregidos**
```
✅ Subject.java                      - Paquete corregido
✅ Observer.java                     - Paquete corregido
✅ NotificationEvent.java            - Paquete corregido
✅ NotificationSubject.java          - Paquete corregido
✅ UserObserver.java                 - Paquete corregido
✅ NotificacionService.java          - Imports actualizados
✅ CursoCreacionService.java         - Imports actualizados
✅ EvaluacionController.java         - Imports actualizados
✅ SistemaEducativoFacade.java       - Imports actualizados
✅ PatronesController.java           - Imports actualizados
✅ NotificationOrchestrator.java     - PostConstruct corregido
```

---

## 🎯 Ejemplo 1: Servicio de Cursos

### **Crear un servicio completo para gestión de cursos**

```java
package com.edulearn.service;

import com.edulearn.model.Curso;
import com.edulearn.patterns.comportamiento.observer.NotificationOrchestrator;
import com.edulearn.patterns.comportamiento.observer.events.SpringEventPublisher;
import com.edulearn.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private NotificationOrchestrator orchestrator;

    // O alternativamente con Spring Events:
    // @Autowired
    // private SpringEventPublisher eventPublisher;

    /**
     * OPCIÓN A: Usar NotificationOrchestrator (Observer Clásico)
     */
    @Transactional
    public Curso crearCursoConObserver(Curso curso) {
        // 1. Validaciones de negocio
        if (curso.getNombre() == null || curso.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del curso es obligatorio");
        }

        // 2. Guardar el curso
        Curso saved = cursoRepository.save(curso);

        // 3. Registrar al profesor como responsable del curso
        if (saved.getProfesorTitularId() != null) {
            orchestrator.registerCourseTeacher(
                saved.getId(),
                saved.getProfesorTitularId()
            );
        }

        // 4. Notificar a todos los administradores
        orchestrator.notifyCourseCreated(saved);

        return saved;
    }

    /**
     * OPCIÓN B: Usar Spring Events (Recomendado)
     */
    @Transactional
    public Curso crearCursoConSpringEvents(Curso curso, Integer creadorId, String creadorNombre) {
        // 1. Validaciones
        if (curso.getNombre() == null || curso.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del curso es obligatorio");
        }

        // 2. Guardar
        Curso saved = cursoRepository.save(curso);

        // 3. Publicar evento (fire and forget)
        // El sistema se encarga automáticamente de notificar
        // eventPublisher.publishCourseCreated(saved, creadorId, creadorNombre);

        return saved;
    }
}
```

### **Controller correspondiente**

```java
package com.edulearn.controller;

import com.edulearn.model.Curso;
import com.edulearn.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = "*")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping
    public ResponseEntity<Curso> crearCurso(@RequestBody Curso curso,
                                             @RequestHeader("userId") Integer creadorId,
                                             @RequestHeader("userName") String creadorNombre) {
        Curso created = cursoService.crearCursoConObserver(curso);
        // O: cursoService.crearCursoConSpringEvents(curso, creadorId, creadorNombre);

        return ResponseEntity.ok(created);
    }
}
```

---

## 📄 Ejemplo 2: Servicio de Materiales

```java
package com.edulearn.service;

import com.edulearn.model.Material;
import com.edulearn.model.Curso;
import com.edulearn.patterns.comportamiento.observer.NotificationOrchestrator;
import com.edulearn.repository.MaterialRepository;
import com.edulearn.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private NotificationOrchestrator orchestrator;

    @Transactional
    public Material subirMaterial(Material material, Integer cursoId) {
        // 1. Validar que el curso existe
        Curso curso = cursoRepository.findById(cursoId)
            .orElseThrow(() -> new RuntimeException("Curso no encontrado: " + cursoId));

        // 2. Asociar material al curso
        material.setCursoId(cursoId);

        // 3. Guardar material
        Material saved = materialRepository.save(material);

        // 4. Notificar a TODOS los estudiantes del curso
        orchestrator.notifyMaterialUploaded(
            saved,
            cursoId,
            curso.getNombre()
        );

        return saved;
    }
}
```

### **Controller**

```java
@RestController
@RequestMapping("/api/materiales")
@CrossOrigin(origins = "*")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @PostMapping("/curso/{cursoId}")
    public ResponseEntity<Material> subirMaterial(@PathVariable Integer cursoId,
                                                   @RequestBody Material material) {
        Material uploaded = materialService.subirMaterial(material, cursoId);
        return ResponseEntity.ok(uploaded);
    }
}
```

---

## 📝 Ejemplo 3: Servicio de Evaluaciones (Tareas)

```java
package com.edulearn.service;

import com.edulearn.model.Evaluacion;
import com.edulearn.model.Curso;
import com.edulearn.model.Modulo;
import com.edulearn.patterns.comportamiento.observer.NotificationOrchestrator;
import com.edulearn.repository.EvaluacionRepository;
import com.edulearn.repository.CursoRepository;
import com.edulearn.repository.ModuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TareaService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private NotificationOrchestrator orchestrator;

    @Transactional
    public Evaluacion crearTarea(Evaluacion tarea) {
        // 1. Validar módulo
        Modulo modulo = moduloRepository.findById(tarea.getModuloId())
            .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));

        // 2. Configurar tipo
        tarea.setTipo("tarea"); // Campo requerido por la BD
        tarea.setTipoEvaluacion("TAREA");

        // 3. Guardar
        Evaluacion saved = evaluacionRepository.save(tarea);

        // 4. Notificar a estudiantes del curso
        // Necesitamos obtener el cursoId del módulo
        Integer cursoId = modulo.getCursoId();
        String cursoNombre = "Curso " + cursoId; // Obtener de BD si está disponible

        orchestrator.notifyAssignmentCreated(
            saved,
            cursoId,
            cursoNombre
        );

        return saved;
    }
}
```

---

## 👥 Ejemplo 4: Servicio de Inscripciones

```java
package com.edulearn.service;

import com.edulearn.model.Inscripcion;
import com.edulearn.model.Usuario;
import com.edulearn.model.Curso;
import com.edulearn.patterns.comportamiento.observer.NotificationOrchestrator;
import com.edulearn.repository.InscripcionRepository;
import com.edulearn.repository.UsuarioRepository;
import com.edulearn.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private NotificationOrchestrator orchestrator;

    @Transactional
    public Inscripcion inscribirEstudiante(Integer estudianteId, Integer cursoId,
                                            String modalidad) {
        // 1. Validar que no existe inscripción previa
        inscripcionRepository.findByEstudianteIdAndCursoId(estudianteId, cursoId)
            .ifPresent(i -> {
                throw new RuntimeException("El estudiante ya está inscrito en este curso");
            });

        // 2. Obtener información
        Usuario estudiante = usuarioRepository.findById(estudianteId)
            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        Curso curso = cursoRepository.findById(cursoId)
            .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        // 3. Crear inscripción
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setEstudianteId(estudianteId);
        inscripcion.setCursoId(cursoId);
        inscripcion.setFechaInscripcion(LocalDate.now());
        inscripcion.setModalidad(modalidad);
        inscripcion.setEstadoInscripcion("Activa");

        Inscripcion saved = inscripcionRepository.save(inscripcion);

        // 4. Suscribir al estudiante a las notificaciones del curso
        orchestrator.subscribeStudentToCourse(
            estudianteId,
            estudiante.getNombre(),
            cursoId
        );

        // 5. Notificar al profesor del curso
        orchestrator.notifyStudentEnrolled(
            saved,
            estudiante.getNombre(),
            curso.getNombre()
        );

        return saved;
    }

    @Transactional
    public void cancelarInscripcion(Integer estudianteId, Integer cursoId) {
        // 1. Buscar inscripción
        Inscripcion inscripcion = inscripcionRepository
            .findByEstudianteIdAndCursoId(estudianteId, cursoId)
            .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        // 2. Desuscribir de notificaciones
        Usuario estudiante = usuarioRepository.findById(estudianteId).orElseThrow();

        orchestrator.unsubscribeStudentFromCourse(
            estudianteId,
            estudiante.getNombre(),
            cursoId
        );

        // 3. Cambiar estado o eliminar
        inscripcion.setEstadoInscripcion("Cancelada");
        inscripcionRepository.save(inscripcion);
    }
}
```

---

## 📊 Ejemplo 5: Servicio de Calificaciones

```java
package com.edulearn.service;

import com.edulearn.model.Usuario;
import com.edulearn.model.Evaluacion;
import com.edulearn.patterns.comportamiento.observer.NotificationOrchestrator;
import com.edulearn.repository.UsuarioRepository;
import com.edulearn.repository.EvaluacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CalificacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private NotificationOrchestrator orchestrator;

    @Transactional
    public void calificarTarea(Long tareaId, Integer estudianteId,
                                BigDecimal calificacion, String feedback) {
        // 1. Validar tarea
        Evaluacion tarea = evaluacionRepository.findById(tareaId)
            .orElseThrow(() -> new RuntimeException("Tarea no encontrada"));

        // 2. Validar estudiante
        Usuario estudiante = usuarioRepository.findById(estudianteId)
            .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        // 3. Validar calificación
        if (calificacion.compareTo(tarea.getPuntajeMaximo()) > 0) {
            throw new RuntimeException("Calificación excede el puntaje máximo");
        }

        // 4. Guardar calificación en BD
        // (Aquí iría la lógica para guardar en tabla de calificaciones)

        // 5. Notificar AL ESTUDIANTE ESPECÍFICO
        orchestrator.notifyAssignmentGraded(
            estudianteId,
            estudiante.getNombre(),
            tareaId,
            tarea.getTitulo(),
            calificacion,
            feedback
        );
    }
}
```

---

## 🚀 Inicialización del Sistema

### **Registrar usuarios al iniciar sesión**

```java
package com.edulearn.service;

import com.edulearn.model.Usuario;
import com.edulearn.patterns.comportamiento.observer.NotificationOrchestrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private NotificationOrchestrator orchestrator;

    public void onUserLogin(Usuario usuario) {
        // Registrar usuario en el sistema de notificaciones
        orchestrator.registerUser(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getRol()
        );

        System.out.println("Usuario " + usuario.getNombre() +
                         " registrado en sistema de notificaciones");
    }
}
```

### **Configuración inicial del sistema**

```java
package com.edulearn.config;

import com.edulearn.model.Usuario;
import com.edulearn.patterns.comportamiento.observer.NotificationOrchestrator;
import com.edulearn.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig {

    @Autowired
    private NotificationOrchestrator orchestrator;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Registrar todos los usuarios activos al iniciar la aplicación
     */
    @Bean
    public CommandLineRunner initializeNotifications() {
        return args -> {
            System.out.println("=== Inicializando Sistema de Notificaciones ===");

            // Obtener todos los usuarios
            var usuarios = usuarioRepository.findAll();

            // Registrar cada usuario
            for (Usuario usuario : usuarios) {
                orchestrator.registerUser(
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getRol()
                );
            }

            System.out.println("✓ " + usuarios.size() + " usuarios registrados");

            // Mostrar estadísticas
            orchestrator.logStatistics();
        };
    }
}
```

---

## 🧪 Testing

### **Test del OrchestratUse**

```java
package com.edulearn.service;

import com.edulearn.model.Curso;
import com.edulearn.model.Notificacion;
import com.edulearn.patterns.comportamiento.observer.NotificationOrchestrator;
import com.edulearn.repository.CursoRepository;
import com.edulearn.repository.NotificacionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class NotificationIntegrationTest {

    @Autowired
    private NotificationOrchestrator orchestrator;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Test
    public void testCourseCreationNotification() {
        // Arrange
        Curso curso = new Curso();
        curso.setNombre("Java Avanzado");
        curso.setCodigo("JAVA-301");
        curso.setProfesorTitularId(1);

        Curso saved = cursoRepository.save(curso);

        // Act
        orchestrator.notifyCourseCreated(saved);

        // Assert
        List<Notificacion> notifications = notificacionRepository
            .findAll();

        assertFalse(notifications.isEmpty(),
                   "Deberían haberse creado notificaciones");

        // Verificar que las notificaciones son para admins
        notifications.forEach(notif -> {
            assertEquals("Nuevo Curso Creado", notif.getAsunto());
            assertEquals("INTERNA", notif.getTipo());
        });
    }
}
```

---

## 🎯 Casos de Uso Resumidos

| Acción | Método | Destinatarios |
|--------|--------|---------------|
| Crear Curso | `orchestrator.notifyCourseCreated(curso)` | Todos los Administradores |
| Subir Material | `orchestrator.notifyMaterialUploaded(...)` | Estudiantes del curso |
| Crear Tarea | `orchestrator.notifyAssignmentCreated(...)` | Estudiantes del curso |
| Inscribir Estudiante | `orchestrator.subscribeStudentToCourse(...)` + `notifyStudentEnrolled(...)` | Profesor + Estudiante suscrito |
| Calificar Tarea | `orchestrator.notifyAssignmentGraded(...)` | Estudiante específico |

---

## 📝 Notas Finales

1. **Preferir Spring Events** para nuevos desarrollos
2. **Registrar usuarios** al iniciar sesión
3. **Suscribir estudiantes** automáticamente al inscribirse
4. **Manejar excepciones** para no romper el flujo
5. **Usar @Transactional** en servicios
6. **Logging** para depuración

---

**Autor**: Arquitecto de Software EduLearn
**Fecha**: 2025-12-04
**Estado**: ✅ Implementación Completa y Compilación Exitosa
