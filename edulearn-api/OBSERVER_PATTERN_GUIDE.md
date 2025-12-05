# 📚 Guía Completa del Patrón Observer - EduLearn

## 📋 Índice

1. [Introducción](#introducción)
2. [Arquitectura Implementada](#arquitectura-implementada)
3. [Enfoque 1: Patrón Observer Clásico](#enfoque-1-patrón-observer-clásico)
4. [Enfoque 2: Spring Events (Recomendado)](#enfoque-2-spring-events-recomendado)
5. [Casos de Uso Implementados](#casos-de-uso-implementados)
6. [Ejemplos de Integración](#ejemplos-de-integración)
7. [Comparación de Enfoques](#comparación-de-enfoques)
8. [Testing](#testing)

---

## 🎯 Introducción

Este documento describe la implementación completa del **Patrón Observer** para el sistema de notificaciones internas de EduLearn. Se proporcionan **dos enfoques**:

1. **Patrón Observer Clásico**: Implementación tradicional del patrón con `Subject` y `Observer`
2. **Spring Events**: Implementación idiomática usando el sistema de eventos de Spring Boot

---

## 🏗️ Arquitectura Implementada

### **Estructura de Paquetes**

```
com.edulearn.patterns.comportamiento.observer/
├── Observer.java                    # Interfaz Observer
├── Subject.java                     # Interfaz Subject
├── NotificationEvent.java           # Modelo de evento
├── NotificationSubject.java         # Subject básico
├── NotificationManager.java         # Subject avanzado (★)
├── UserObserver.java                # Observer genérico
├── AbstractUserObserver.java        # Observer base abstracto
├── AdministratorObserver.java       # Observer para admins
├── TeacherObserver.java             # Observer para profesores
├── StudentObserver.java             # Observer para estudiantes
├── ObserverFactory.java             # Factory de observers (★)
├── NotificationOrchestrator.java    # Facade de alto nivel (★)
└── events/
    ├── CourseCreatedEvent.java
    ├── MaterialUploadedEvent.java
    ├── AssignmentCreatedEvent.java
    ├── StudentEnrolledEvent.java
    ├── AssignmentGradedEvent.java
    ├── SpringEventPublisher.java    # Publicador de eventos (★)
    └── listeners/
        ├── CourseEventListener.java
        ├── AssignmentEventListener.java
        └── EnrollmentEventListener.java
```

**(★) = Componentes principales**

---

## 🔧 Enfoque 1: Patrón Observer Clásico

### **Componentes Principales**

#### **1. NotificationManager**
El `Subject` concreto que gestiona todas las suscripciones y notificaciones.

**Características:**
- ✅ Gestión global de observers
- ✅ Suscripciones por curso
- ✅ Suscripciones por rol
- ✅ Notificaciones dirigidas
- ✅ Thread-safe (CopyOnWriteArrayList)
- ✅ Persistencia automática en BD

#### **2. Observers Específicos**

| Observer | Rol | Eventos de Interés |
|----------|-----|-------------------|
| `AdministratorObserver` | admin | CURSO_CREADO, CURSO_ACTUALIZADO, CURSO_ELIMINADO |
| `TeacherObserver` | profesor | ESTUDIANTE_INSCRITO, TAREA_ENTREGADA |
| `StudentObserver` | estudiante | TAREA_CREADA, TAREA_CALIFICADA, MATERIAL_AGREGADO |

#### **3. NotificationOrchestrator**
Facade que simplifica el uso del patrón para los servicios de negocio.

### **Uso Básico - Observer Clásico**

#### **Inicialización del Sistema**

```java
@Service
public class UserManagementService {

    @Autowired
    private NotificationOrchestrator orchestrator;

    /**
     * Registrar usuario en el sistema de notificaciones
     */
    public void registerUserForNotifications(Usuario usuario) {
        orchestrator.registerUser(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getRol()
        );
    }
}
```

#### **Caso 1: Creación de Curso → Notificar Admins**

```java
@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private NotificationOrchestrator orchestrator;

    public Curso crearCurso(Curso curso) {
        // 1. Guardar el curso
        Curso saved = cursoRepository.save(curso);

        // 2. Registrar profesor del curso
        orchestrator.registerCourseTeacher(
            saved.getId(),
            saved.getProfesorTitularId()
        );

        // 3. Notificar a todos los administradores
        orchestrator.notifyCourseCreated(saved);

        return saved;
    }
}
```

#### **Caso 2: Subir Material → Notificar Estudiantes del Curso**

```java
@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private NotificationOrchestrator orchestrator;

    public Material subirMaterial(Material material, Integer cursoId) {
        // 1. Guardar material
        Material saved = materialRepository.save(material);

        // 2. Obtener información del curso
        Curso curso = cursoRepository.findById(cursoId)
            .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        // 3. Notificar a estudiantes del curso
        orchestrator.notifyMaterialUploaded(
            saved,
            cursoId,
            curso.getNombre()
        );

        return saved;
    }
}
```

#### **Caso 3: Crear Tarea → Notificar Estudiantes del Curso**

```java
@Service
public class EvaluacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private NotificationOrchestrator orchestrator;

    public Evaluacion crearTarea(Evaluacion tarea, Integer cursoId, String cursoNombre) {
        // 1. Guardar tarea
        Evaluacion saved = evaluacionRepository.save(tarea);

        // 2. Notificar a estudiantes del curso
        orchestrator.notifyAssignmentCreated(
            saved,
            cursoId,
            cursoNombre
        );

        return saved;
    }
}
```

#### **Caso 4: Inscripción → Notificar Profesor + Suscribir Estudiante**

```java
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

    public Inscripcion inscribirEstudiante(Inscripcion inscripcion) {
        // 1. Guardar inscripción
        Inscripcion saved = inscripcionRepository.save(inscripcion);

        // 2. Obtener información
        Usuario estudiante = usuarioRepository.findById(saved.getEstudianteId())
            .orElseThrow();
        Curso curso = cursoRepository.findById(saved.getCursoId())
            .orElseThrow();

        // 3. Suscribir estudiante a notificaciones del curso
        orchestrator.subscribeStudentToCourse(
            estudiante.getId(),
            estudiante.getNombre(),
            curso.getId()
        );

        // 4. Notificar al profesor del curso
        orchestrator.notifyStudentEnrolled(
            saved,
            estudiante.getNombre(),
            curso.getNombre()
        );

        return saved;
    }
}
```

#### **Caso 5: Calificar Tarea → Notificar Estudiante**

```java
@Service
public class CalificacionService {

    @Autowired
    private NotificationOrchestrator orchestrator;

    public void calificarTarea(Integer estudianteId, String estudianteNombre,
                                Long tareaId, String tareaNombre,
                                BigDecimal calificacion, String feedback) {

        // Lógica de guardado de calificación...

        // Notificar al estudiante
        orchestrator.notifyAssignmentGraded(
            estudianteId,
            estudianteNombre,
            tareaId,
            tareaNombre,
            calificacion,
            feedback
        );
    }
}
```

---

## 🌟 Enfoque 2: Spring Events (Recomendado)

### **¿Por qué Spring Events?**

✅ **Desacoplamiento Total**: Los servicios no conocen los observers
✅ **Asíncrono**: Procesamiento en background con `@Async`
✅ **Transaccional**: Soporte para `@TransactionalEventListener`
✅ **Testeable**: Fácil mockeo y testing
✅ **Idiomático**: Forma natural de Spring Boot

### **Arquitectura**

```
Servicio de Negocio → Publica Evento → Event Listener → NotificationOrchestrator
```

### **Uso con Spring Events**

#### **Habilitar Procesamiento Asíncrono**

```java
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("notification-");
        executor.initialize();
        return executor;
    }
}
```

#### **Caso 1: Creación de Curso con Spring Events**

```java
@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private SpringEventPublisher eventPublisher;  // ★ Inyectar event publisher

    public Curso crearCurso(Curso curso, Integer creadorId, String creadorNombre) {
        // 1. Lógica de negocio
        Curso saved = cursoRepository.save(curso);

        // 2. Publicar evento (Fire and Forget)
        eventPublisher.publishCourseCreated(saved, creadorId, creadorNombre);

        return saved;
    }
}
```

**¿Qué pasa después?**
1. El evento `CourseCreatedEvent` es publicado
2. Spring invoca automáticamente `CourseEventListener.handleCourseCreated()`
3. El listener llama a `NotificationOrchestrator.notifyCourseCreated()`
4. Se notifica a todos los administradores

#### **Caso 2: Subir Material con Spring Events**

```java
@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private SpringEventPublisher eventPublisher;

    public Material subirMaterial(Material material, Integer cursoId,
                                   String cursoNombre, Integer profesorId) {
        Material saved = materialRepository.save(material);

        // Publicar evento
        eventPublisher.publishMaterialUploaded(
            saved,
            cursoId,
            cursoNombre,
            profesorId
        );

        return saved;
    }
}
```

#### **Caso 3: Inscripción con Spring Events**

```java
@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    @Autowired
    private SpringEventPublisher eventPublisher;

    public Inscripcion inscribirEstudiante(Inscripcion inscripcion,
                                            String estudianteNombre,
                                            String cursoNombre) {
        Inscripcion saved = inscripcionRepository.save(inscripcion);

        // Publicar evento - el listener se encarga de suscribir y notificar
        eventPublisher.publishStudentEnrolled(
            saved,
            estudianteNombre,
            cursoNombre
        );

        return saved;
    }
}
```

#### **Procesamiento Transaccional**

Si quieres que el evento se procese SOLO después de que la transacción se complete:

```java
@Component
public class CourseEventListener {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void handleCourseCreated(CourseCreatedEvent event) {
        // Este código se ejecuta SOLO si la transacción fue exitosa
        notificationOrchestrator.notifyCourseCreated(event.getCurso());
    }
}
```

---

## 📊 Casos de Uso Implementados

### **Resumen Completo**

| # | Evento | Disparador | Destinatario | Método |
|---|--------|-----------|--------------|--------|
| 1 | Curso Creado | CursoService.crearCurso() | Todos los Admins | `notifyCourseCreated()` |
| 2 | Material Subido | MaterialService.subirMaterial() | Estudiantes del curso | `notifyMaterialUploaded()` |
| 3 | Tarea Creada | EvaluacionService.crearTarea() | Estudiantes del curso | `notifyAssignmentCreated()` |
| 4 | Estudiante Inscrito | InscripcionService.inscribir() | Profesor del curso | `notifyStudentEnrolled()` |
| 5 | Tarea Calificada | CalificacionService.calificar() | Estudiante específico | `notifyAssignmentGraded()` |

---

## 🔄 Comparación de Enfoques

| Aspecto | Observer Clásico | Spring Events |
|---------|------------------|---------------|
| **Acoplamiento** | Medio (inyección de Orchestrator) | Bajo (solo EventPublisher) |
| **Complejidad** | Media | Baja |
| **Control** | Alto (control directo) | Medio (delegado a Spring) |
| **Testing** | Requiere mocks | Fácil con eventos de test |
| **Asíncrono** | Manual | Automático con @Async |
| **Transaccional** | Manual | Automático con @TransactionalEventListener |
| **Escalabilidad** | Buena | Excelente |
| **Idiomático Spring** | No | Sí ★★★ |

### **Recomendación**

🌟 **Para nuevos desarrollos**: Usar **Spring Events**
🔧 **Para control fino**: Usar **Observer Clásico**
💡 **Híbrido**: Ambos pueden coexistir (Spring Events llama al Orchestrator)

---

## 🧪 Testing

### **Test de Observer Clásico**

```java
@SpringBootTest
public class NotificationOrchestratorTest {

    @Autowired
    private NotificationOrchestrator orchestrator;

    @Test
    public void testCourseCreatedNotification() {
        // Arrange
        Curso curso = new Curso();
        curso.setId(1);
        curso.setNombre("Java Avanzado");
        curso.setCodigo("JAVA-202");

        // Act
        orchestrator.notifyCourseCreated(curso);

        // Assert
        // Verificar que se crearon notificaciones en BD
        List<Notificacion> notifs = notificacionRepository
            .findByAsunto("Nuevo Curso Creado");

        assertFalse(notifs.isEmpty());
    }
}
```

### **Test de Spring Events**

```java
@SpringBootTest
public class CourseEventListenerTest {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Test
    public void testCourseCreatedEvent() {
        // Arrange
        Curso curso = new Curso();
        curso.setId(1);
        curso.setNombre("Java Avanzado");

        CourseCreatedEvent event = new CourseCreatedEvent(
            this, curso, 100, "Profesor Test"
        );

        // Act
        eventPublisher.publishEvent(event);

        // Esperar procesamiento asíncrono
        await().atMost(5, TimeUnit.SECONDS).until(() ->
            !notificacionRepository.findByAsunto("Nuevo Curso Creado").isEmpty()
        );

        // Assert
        List<Notificacion> notifs = notificacionRepository
            .findByAsunto("Nuevo Curso Creado");
        assertFalse(notifs.isEmpty());
    }
}
```

---

## 🎓 Mejores Prácticas

1. **Usar `NotificationOrchestrator`** como punto de entrada único
2. **Preferir Spring Events** para nuevos desarrollos
3. **Mantener observers simples** - solo lógica de notificación
4. **Usar `@Async`** para no bloquear el hilo principal
5. **Manejar excepciones** en los listeners para no romper el flujo
6. **Registrar usuarios** al iniciar sesión o al crear cuenta
7. **Suscribir estudiantes automáticamente** al inscribirse en un curso
8. **Desuscribir estudiantes** al darse de baja de un curso

---

## 🚀 Próximos Pasos

### **Extensiones Futuras**

1. **Notificaciones Push**: Integrar con Firebase Cloud Messaging
2. **Email Notifications**: Agregar listener para enviar emails
3. **SMS Notifications**: Integrar con Twilio
4. **Notificaciones In-App**: WebSocket para notificaciones en tiempo real
5. **Preferencias de Usuario**: Permitir al usuario elegir qué notificaciones recibir
6. **Digest de Notificaciones**: Agrupar notificaciones en resúmenes diarios

### **Optimizaciones**

1. **Cache**: Cachear lista de observadores por curso
2. **Batch Processing**: Agrupar notificaciones similares
3. **Rate Limiting**: Evitar spam de notificaciones
4. **Priorización**: Cola de prioridad para notificaciones urgentes

---

## 📖 Referencias

- **Patrón Observer**: Gang of Four - Design Patterns
- **Spring Events**: [Spring Framework Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#context-functionality-events)
- **@Async Processing**: [Spring Async Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#scheduling)

---

**Autor**: Arquitecto de Software EduLearn
**Fecha**: 2025-12-04
**Versión**: 1.0
