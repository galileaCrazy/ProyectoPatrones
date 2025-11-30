# Propuesta de Estructura Backend - EduLearn API
## Sistema LMS con 23 Patrones de Diseño

---

## 📋 Estado Actual vs Requerido

### ✅ Patrones Implementados (7/23)
1. **Singleton** - ConfiguracionSistema
2. **Builder** - CursoBuilder
3. **Abstract Factory** - Familias de cursos (Virtual, Presencial, Híbrido)
4. **Bridge** - Plataformas (Web, Móvil, SmartTV) con Dashboards
5. **Facade** - GestionCursosFacade
6. **Flyweight** - Recursos compartidos
7. **Prototype** - CursoPrototype

### ❌ Patrones Pendientes (16/23)
8. Factory Method
9. Adapter
10. Composite
11. Decorator
12. Proxy
13. Chain of Responsibility
14. Command
15. Interpreter
16. Iterator
17. Mediator
18. Memento
19. Observer
20. State
21. Strategy
22. Template Method
23. Visitor

---

## 🏗️ Estructura de Carpetas Propuesta

```
edulearn-api/
├── src/main/java/com/edulearn/
│   ├── EduLearnApplication.java
│   │
│   ├── config/
│   │   ├── CorsConfig.java
│   │   └── SecurityConfig.java
│   │
│   ├── model/                          # Entidades JPA
│   │   ├── Usuario.java
│   │   ├── Estudiante.java
│   │   ├── Profesor.java
│   │   ├── Curso.java
│   │   ├── Inscripcion.java
│   │   ├── Evaluacion.java
│   │   ├── Calificacion.java
│   │   ├── Material.java
│   │   ├── Foro.java
│   │   ├── Mensaje.java
│   │   ├── Notificacion.java
│   │   └── Recurso.java
│   │
│   ├── repository/                     # Repositorios JPA
│   │   ├── UsuarioRepository.java
│   │   ├── EstudianteRepository.java
│   │   ├── ProfesorRepository.java
│   │   ├── CursoRepository.java
│   │   ├── InscripcionRepository.java
│   │   ├── EvaluacionRepository.java
│   │   ├── CalificacionRepository.java
│   │   ├── MaterialRepository.java
│   │   ├── ForoRepository.java
│   │   ├── MensajeRepository.java
│   │   └── NotificacionRepository.java
│   │
│   ├── controller/                     # REST Controllers
│   │   ├── AuthController.java
│   │   ├── UsuarioController.java
│   │   ├── EstudianteController.java
│   │   ├── ProfesorController.java
│   │   ├── CursoController.java
│   │   ├── InscripcionController.java
│   │   ├── EvaluacionController.java
│   │   ├── CalificacionController.java
│   │   ├── MaterialController.java
│   │   ├── ForoController.java
│   │   ├── NotificacionController.java
│   │   └── PatronesController.java
│   │
│   ├── service/                        # Lógica de negocio
│   │   ├── AuthService.java
│   │   ├── UsuarioService.java
│   │   ├── CursoService.java
│   │   ├── InscripcionService.java
│   │   ├── EvaluacionService.java
│   │   ├── CalificacionService.java
│   │   ├── MaterialService.java
│   │   ├── ForoService.java
│   │   └── NotificacionService.java
│   │
│   └── patterns/                       # 23 PATRONES DE DISEÑO
│       │
│       ├── creational/                 # PATRONES CREACIONALES (5)
│       │   │
│       │   ├── singleton/
│       │   │   ├── ConfiguracionSistema.java       ✅ IMPLEMENTADO
│       │   │   ├── DatabaseConnection.java
│       │   │   └── CacheManager.java
│       │   │
│       │   ├── builder/
│       │   │   ├── CursoBuilder.java               ✅ IMPLEMENTADO
│       │   │   ├── EvaluacionBuilder.java
│       │   │   ├── UsuarioBuilder.java
│       │   │   └── ReporteBuilder.java
│       │   │
│       │   ├── factory_method/
│       │   │   ├── NotificacionFactory.java        ❌ PENDIENTE
│       │   │   ├── EmailNotificacion.java
│       │   │   ├── SMSNotificacion.java
│       │   │   ├── PushNotificacion.java
│       │   │   └── INotificacion.java
│       │   │
│       │   ├── abstract_factory/
│       │   │   ├── CourseComponentFactory.java     ✅ IMPLEMENTADO
│       │   │   ├── VirtualCourseFactory.java
│       │   │   ├── PresencialCourseFactory.java
│       │   │   ├── HibridoCourseFactory.java
│       │   │   ├── ICurso.java
│       │   │   ├── IMaterial.java
│       │   │   ├── IEvaluacion.java
│       │   │   ├── CursoVirtual.java
│       │   │   ├── CursoPresencial.java
│       │   │   ├── CursoHibrido.java
│       │   │   ├── MaterialVirtual.java
│       │   │   ├── MaterialPresencial.java
│       │   │   ├── MaterialHibrido.java
│       │   │   ├── EvaluacionVirtual.java
│       │   │   ├── EvaluacionPresencial.java
│       │   │   └── EvaluacionHibrida.java
│       │   │
│       │   └── prototype/
│       │       ├── CursoPrototype.java              ✅ IMPLEMENTADO
│       │       ├── EvaluacionPrototype.java
│       │       └── MaterialPrototype.java
│       │
│       ├── structural/                 # PATRONES ESTRUCTURALES (7)
│       │   │
│       │   ├── adapter/
│       │   │   ├── LegacySystemAdapter.java        ❌ PENDIENTE
│       │   │   ├── IModernSystem.java
│       │   │   ├── LegacyCalificacionSystem.java
│       │   │   └── CalificacionAdapter.java
│       │   │
│       │   ├── bridge/
│       │   │   ├── IPlataforma.java                ✅ IMPLEMENTADO
│       │   │   ├── PlataformaWeb.java
│       │   │   ├── PlataformaMovil.java
│       │   │   ├── PlataformaSmartTV.java
│       │   │   ├── InterfazUsuario.java
│       │   │   ├── DashboardEstudiante.java
│       │   │   ├── DashboardProfesor.java
│       │   │   └── DashboardAdmin.java
│       │   │
│       │   ├── composite/
│       │   │   ├── ComponenteCurso.java            ❌ PENDIENTE
│       │   │   ├── Modulo.java
│       │   │   ├── Leccion.java
│       │   │   ├── Seccion.java
│       │   │   └── ContenidoSimple.java
│       │   │
│       │   ├── decorator/
│       │   │   ├── CursoDecorator.java             ❌ PENDIENTE
│       │   │   ├── CursoBase.java
│       │   │   ├── CertificadoDecorator.java
│       │   │   ├── TutoriaDecorator.java
│       │   │   ├── MaterialExtrasDecorator.java
│       │   │   └── ICursoComponent.java
│       │   │
│       │   ├── facade/
│       │   │   ├── GestionCursosFacade.java        ✅ IMPLEMENTADO
│       │   │   ├── GestionEstudiantesFacade.java
│       │   │   ├── GestionEvaluacionesFacade.java
│       │   │   └── ReporteFacade.java
│       │   │
│       │   ├── flyweight/
│       │   │   ├── RecursoVisualFlyweight.java     ✅ IMPLEMENTADO
│       │   │   ├── RecursoVirtualFlyweight.java
│       │   │   ├── RecursoPresencialFlyweight.java
│       │   │   ├── RecursoHibridoFlyweight.java
│       │   │   ├── RecursoVisualFactory.java
│       │   │   ├── RecursoInfo.java
│       │   │   └── ContextoCurso.java
│       │   │
│       │   └── proxy/
│       │       ├── CursoProxy.java                 ❌ PENDIENTE
│       │       ├── ICursoService.java
│       │       ├── CursoServiceImpl.java
│       │       ├── CursoAccessProxy.java
│       │       └── CursoCacheProxy.java
│       │
│       └── behavioral/                # PATRONES COMPORTAMENTALES (11)
│           │
│           ├── chain_of_responsibility/
│           │   ├── ManejadorAutenticacion.java     ❌ PENDIENTE
│           │   ├── ManejadorBase.java
│           │   ├── ValidadorCredenciales.java
│           │   ├── ValidadorRol.java
│           │   ├── ValidadorPermisos.java
│           │   └── ValidadorSesion.java
│           │
│           ├── command/
│           │   ├── ICommand.java                   ❌ PENDIENTE
│           │   ├── CrearCursoCommand.java
│           │   ├── ActualizarCursoCommand.java
│           │   ├── EliminarCursoCommand.java
│           │   ├── InscribirEstudianteCommand.java
│           │   ├── CommandInvoker.java
│           │   └── CommandHistory.java
│           │
│           ├── interpreter/
│           │   ├── BusquedaExpression.java          ❌ PENDIENTE
│           │   ├── IExpression.java
│           │   ├── AndExpression.java
│           │   ├── OrExpression.java
│           │   ├── NotExpression.java
│           │   ├── CriterioNombre.java
│           │   └── CriterioCategoria.java
│           │
│           ├── iterator/
│           │   ├── CursoIterator.java              ❌ PENDIENTE
│           │   ├── IIterator.java
│           │   ├── ICollection.java
│           │   ├── CursoCollection.java
│           │   └── EstudianteIterator.java
│           │
│           ├── mediator/
│           │   ├── ChatMediator.java               ❌ PENDIENTE
│           │   ├── IMediator.java
│           │   ├── ForoMediator.java
│           │   ├── Usuario.java (chat)
│           │   └── MensajeMediator.java
│           │
│           ├── memento/
│           │   ├── CursoMemento.java               ❌ PENDIENTE
│           │   ├── CursoOriginator.java
│           │   ├── CursoCaretaker.java
│           │   └── HistorialCambios.java
│           │
│           ├── observer/
│           │   ├── NotificacionObserver.java       ❌ PENDIENTE
│           │   ├── IObserver.java
│           │   ├── ISubject.java
│           │   ├── CursoSubject.java
│           │   ├── EstudianteObserver.java
│           │   ├── ProfesorObserver.java
│           │   └── AdminObserver.java
│           │
│           ├── state/
│           │   ├── EstadoCurso.java                ❌ PENDIENTE
│           │   ├── IEstadoCurso.java
│           │   ├── CursoBorrador.java
│           │   ├── CursoPublicado.java
│           │   ├── CursoEnProgreso.java
│           │   ├── CursoFinalizado.java
│           │   └── CursoArchivado.java
│           │
│           ├── strategy/
│           │   ├── EvaluacionStrategy.java         ❌ PENDIENTE
│           │   ├── IEvaluacionStrategy.java
│           │   ├── EvaluacionCuantitativa.java
│           │   ├── EvaluacionCualitativa.java
│           │   ├── EvaluacionMixta.java
│           │   └── ContextoEvaluacion.java
│           │
│           ├── template_method/
│           │   ├── ProcesoInscripcion.java         ❌ PENDIENTE
│           │   ├── InscripcionTemplate.java
│           │   ├── InscripcionGratuita.java
│           │   ├── InscripcionPaga.java
│           │   └── InscripcionBeca.java
│           │
│           └── visitor/
│               ├── ReporteVisitor.java             ❌ PENDIENTE
│               ├── IVisitor.java
│               ├── IVisitable.java
│               ├── EstadisticasVisitor.java
│               ├── ExportPDFVisitor.java
│               └── ExportExcelVisitor.java
```

---

## 📦 Implementación Detallada por Patrón

### 1️⃣ PATRONES CREACIONALES

#### **SINGLETON** ✅ (Implementado)
**Propósito:** Garantizar una única instancia de configuración del sistema.

**Archivos:**
- `patterns/creational/singleton/ConfiguracionSistema.java`
- `patterns/creational/singleton/DatabaseConnection.java`
- `patterns/creational/singleton/CacheManager.java`

**Caso de uso:** Configuración global del sistema, conexión a BD única, caché compartido.

---

#### **BUILDER** ✅ (Implementado)
**Propósito:** Construcción fluida de objetos complejos.

**Archivos:**
- `patterns/creational/builder/CursoBuilder.java`
- `patterns/creational/builder/EvaluacionBuilder.java`
- `patterns/creational/builder/UsuarioBuilder.java`
- `patterns/creational/builder/ReporteBuilder.java`

**Caso de uso:** Crear cursos, evaluaciones y usuarios con muchos parámetros opcionales.

**Ejemplo:**
```java
Curso curso = new CursoBuilder()
    .setNombre("Java Avanzado")
    .setDescripcion("Curso completo de Java")
    .setDuracion(40)
    .setModalidad("Virtual")
    .setCertificado(true)
    .build();
```

---

#### **FACTORY METHOD** ❌ (Pendiente)
**Propósito:** Crear diferentes tipos de notificaciones.

**Archivos:**
- `patterns/creational/factory_method/NotificacionFactory.java`
- `patterns/creational/factory_method/INotificacion.java`
- `patterns/creational/factory_method/EmailNotificacion.java`
- `patterns/creational/factory_method/SMSNotificacion.java`
- `patterns/creational/factory_method/PushNotificacion.java`

**Caso de uso:** Enviar notificaciones por diferentes canales según preferencias.

**Estructura sugerida:**
```java
public interface INotificacion {
    void enviar(String destinatario, String mensaje);
}

public abstract class NotificacionFactory {
    public abstract INotificacion crearNotificacion();

    public void notificar(String destinatario, String mensaje) {
        INotificacion notif = crearNotificacion();
        notif.enviar(destinatario, mensaje);
    }
}

public class EmailNotificacionFactory extends NotificacionFactory {
    @Override
    public INotificacion crearNotificacion() {
        return new EmailNotificacion();
    }
}
```

---

#### **ABSTRACT FACTORY** ✅ (Implementado)
**Propósito:** Crear familias de objetos relacionados (cursos, materiales, evaluaciones).

**Archivos:** 16 archivos ya implementados.

**Caso de uso:** Crear componentes coherentes según modalidad (Virtual, Presencial, Híbrido).

---

#### **PROTOTYPE** ✅ (Implementado)
**Propósito:** Clonar cursos existentes para crear plantillas.

**Archivos:**
- `patterns/creational/prototype/CursoPrototype.java`
- `patterns/creational/prototype/EvaluacionPrototype.java`
- `patterns/creational/prototype/MaterialPrototype.java`

**Caso de uso:** Duplicar cursos populares para nuevos períodos académicos.

---

### 2️⃣ PATRONES ESTRUCTURALES

#### **ADAPTER** ❌ (Pendiente)
**Propósito:** Integrar sistemas legados de calificaciones.

**Archivos:**
- `patterns/structural/adapter/LegacySystemAdapter.java`
- `patterns/structural/adapter/IModernSystem.java`
- `patterns/structural/adapter/LegacyCalificacionSystem.java`
- `patterns/structural/adapter/CalificacionAdapter.java`

**Caso de uso:** Migración de sistema antiguo manteniendo compatibilidad.

**Estructura sugerida:**
```java
// Sistema legado con interfaz diferente
public class LegacyCalificacionSystem {
    public int obtenerNota(String codigo) { /* ... */ }
}

// Interfaz moderna esperada
public interface IModernSystem {
    double getCalificacion(Long estudianteId);
}

// Adaptador
public class CalificacionAdapter implements IModernSystem {
    private LegacyCalificacionSystem legacySystem;

    @Override
    public double getCalificacion(Long estudianteId) {
        String codigo = convertirId(estudianteId);
        int nota = legacySystem.obtenerNota(codigo);
        return convertirNota(nota);
    }
}
```

---

#### **BRIDGE** ✅ (Implementado)
**Propósito:** Separar abstracción (Dashboards) de implementación (Plataformas).

**Archivos:** 8 archivos implementados.

**Caso de uso:** Dashboards diferentes en plataformas Web, Móvil, SmartTV.

---

#### **COMPOSITE** ❌ (Pendiente)
**Propósito:** Estructura jerárquica de contenidos del curso.

**Archivos:**
- `patterns/structural/composite/ComponenteCurso.java`
- `patterns/structural/composite/Modulo.java`
- `patterns/structural/composite/Leccion.java`
- `patterns/structural/composite/Seccion.java`
- `patterns/structural/composite/ContenidoSimple.java`

**Caso de uso:** Organizar cursos en módulos → lecciones → secciones.

**Estructura sugerida:**
```java
public abstract class ComponenteCurso {
    protected String nombre;

    public abstract void agregar(ComponenteCurso componente);
    public abstract void eliminar(ComponenteCurso componente);
    public abstract void mostrar(int nivel);
    public abstract int getDuracion();
}

public class Modulo extends ComponenteCurso {
    private List<ComponenteCurso> hijos = new ArrayList<>();

    @Override
    public void agregar(ComponenteCurso componente) {
        hijos.add(componente);
    }

    @Override
    public int getDuracion() {
        return hijos.stream()
            .mapToInt(ComponenteCurso::getDuracion)
            .sum();
    }
}

public class Leccion extends ComponenteCurso {
    private int duracion; // en minutos

    @Override
    public int getDuracion() {
        return duracion;
    }
}
```

---

#### **DECORATOR** ❌ (Pendiente)
**Propósito:** Añadir funcionalidades adicionales a cursos (certificado, tutoría, extras).

**Archivos:**
- `patterns/structural/decorator/CursoDecorator.java`
- `patterns/structural/decorator/ICursoComponent.java`
- `patterns/structural/decorator/CursoBase.java`
- `patterns/structural/decorator/CertificadoDecorator.java`
- `patterns/structural/decorator/TutoriaDecorator.java`
- `patterns/structural/decorator/MaterialExtrasDecorator.java`

**Caso de uso:** Cursos básicos + certificación + tutoría personalizada.

**Estructura sugerida:**
```java
public interface ICursoComponent {
    String getDescripcion();
    double getPrecio();
}

public class CursoBase implements ICursoComponent {
    private String nombre;

    @Override
    public double getPrecio() {
        return 100.0;
    }
}

public abstract class CursoDecorator implements ICursoComponent {
    protected ICursoComponent curso;
}

public class CertificadoDecorator extends CursoDecorator {
    @Override
    public double getPrecio() {
        return curso.getPrecio() + 50.0; // +$50 por certificado
    }
}
```

---

#### **FACADE** ✅ (Implementado)
**Propósito:** Simplificar operaciones complejas de gestión.

**Archivos:**
- `patterns/structural/facade/GestionCursosFacade.java`
- `patterns/structural/facade/GestionEstudiantesFacade.java`
- `patterns/structural/facade/GestionEvaluacionesFacade.java`
- `patterns/structural/facade/ReporteFacade.java`

**Caso de uso:** Inscribir estudiante (validar, crear inscripción, enviar notificación).

---

#### **FLYWEIGHT** ✅ (Implementado)
**Propósito:** Compartir recursos visuales/multimedia entre cursos.

**Archivos:** 7 archivos implementados.

**Caso de uso:** Reutilizar videos, PDFs, imágenes en múltiples cursos.

---

#### **PROXY** ❌ (Pendiente)
**Propósito:** Control de acceso y caché para servicios de cursos.

**Archivos:**
- `patterns/structural/proxy/CursoProxy.java`
- `patterns/structural/proxy/ICursoService.java`
- `patterns/structural/proxy/CursoServiceImpl.java`
- `patterns/structural/proxy/CursoAccessProxy.java`
- `patterns/structural/proxy/CursoCacheProxy.java`

**Caso de uso:** Verificar permisos antes de acceder a contenido premium, cachear cursos frecuentes.

**Estructura sugerida:**
```java
public interface ICursoService {
    Curso obtenerCurso(Long id);
}

public class CursoServiceImpl implements ICursoService {
    @Override
    public Curso obtenerCurso(Long id) {
        // Lógica real de BD
    }
}

public class CursoAccessProxy implements ICursoService {
    private ICursoService realService;
    private Usuario usuarioActual;

    @Override
    public Curso obtenerCurso(Long id) {
        if (!tienePermiso(usuarioActual, id)) {
            throw new AccessDeniedException();
        }
        return realService.obtenerCurso(id);
    }
}
```

---

### 3️⃣ PATRONES COMPORTAMENTALES

#### **CHAIN OF RESPONSIBILITY** ❌ (Pendiente)
**Propósito:** Cadena de validación para autenticación y permisos.

**Archivos:**
- `patterns/behavioral/chain_of_responsibility/ManejadorBase.java`
- `patterns/behavioral/chain_of_responsibility/ValidadorCredenciales.java`
- `patterns/behavioral/chain_of_responsibility/ValidadorRol.java`
- `patterns/behavioral/chain_of_responsibility/ValidadorPermisos.java`
- `patterns/behavioral/chain_of_responsibility/ValidadorSesion.java`

**Caso de uso:** Login → Validar credenciales → Validar rol → Validar sesión activa.

**Estructura sugerida:**
```java
public abstract class ManejadorBase {
    protected ManejadorBase siguiente;

    public void setSiguiente(ManejadorBase manejador) {
        this.siguiente = manejador;
    }

    public abstract boolean manejar(RequestLogin request);
}

public class ValidadorCredenciales extends ManejadorBase {
    @Override
    public boolean manejar(RequestLogin request) {
        if (!validarUsuarioPassword(request)) {
            return false;
        }
        return siguiente != null ? siguiente.manejar(request) : true;
    }
}
```

---

#### **COMMAND** ❌ (Pendiente)
**Propósito:** Encapsular operaciones CRUD como comandos con historial.

**Archivos:**
- `patterns/behavioral/command/ICommand.java`
- `patterns/behavioral/command/CrearCursoCommand.java`
- `patterns/behavioral/command/ActualizarCursoCommand.java`
- `patterns/behavioral/command/EliminarCursoCommand.java`
- `patterns/behavioral/command/InscribirEstudianteCommand.java`
- `patterns/behavioral/command/CommandInvoker.java`
- `patterns/behavioral/command/CommandHistory.java`

**Caso de uso:** Sistema de deshacer/rehacer cambios, auditoría.

**Estructura sugerida:**
```java
public interface ICommand {
    void ejecutar();
    void deshacer();
}

public class CrearCursoCommand implements ICommand {
    private CursoService service;
    private Curso curso;
    private Long cursoIdCreado;

    @Override
    public void ejecutar() {
        cursoIdCreado = service.crear(curso).getId();
    }

    @Override
    public void deshacer() {
        service.eliminar(cursoIdCreado);
    }
}

public class CommandInvoker {
    private Stack<ICommand> historial = new Stack<>();

    public void ejecutar(ICommand comando) {
        comando.ejecutar();
        historial.push(comando);
    }

    public void deshacer() {
        if (!historial.isEmpty()) {
            historial.pop().deshacer();
        }
    }
}
```

---

#### **INTERPRETER** ❌ (Pendiente)
**Propósito:** Interpretar consultas de búsqueda avanzada.

**Archivos:**
- `patterns/behavioral/interpreter/IExpression.java`
- `patterns/behavioral/interpreter/BusquedaExpression.java`
- `patterns/behavioral/interpreter/AndExpression.java`
- `patterns/behavioral/interpreter/OrExpression.java`
- `patterns/behavioral/interpreter/NotExpression.java`
- `patterns/behavioral/interpreter/CriterioNombre.java`
- `patterns/behavioral/interpreter/CriterioCategoria.java`

**Caso de uso:** Búsqueda tipo: `(categoria:programacion AND duracion:<20) OR nivel:avanzado`

**Estructura sugerida:**
```java
public interface IExpression {
    List<Curso> interpretar(List<Curso> cursos);
}

public class CriterioNombre implements IExpression {
    private String nombre;

    @Override
    public List<Curso> interpretar(List<Curso> cursos) {
        return cursos.stream()
            .filter(c -> c.getNombre().contains(nombre))
            .collect(Collectors.toList());
    }
}

public class AndExpression implements IExpression {
    private IExpression expr1, expr2;

    @Override
    public List<Curso> interpretar(List<Curso> cursos) {
        List<Curso> resultado1 = expr1.interpretar(cursos);
        return expr2.interpretar(resultado1);
    }
}
```

---

#### **ITERATOR** ❌ (Pendiente)
**Propósito:** Iterar colecciones de cursos/estudiantes sin exponer estructura.

**Archivos:**
- `patterns/behavioral/iterator/IIterator.java`
- `patterns/behavioral/iterator/ICollection.java`
- `patterns/behavioral/iterator/CursoIterator.java`
- `patterns/behavioral/iterator/CursoCollection.java`
- `patterns/behavioral/iterator/EstudianteIterator.java`

**Caso de uso:** Recorrer cursos con diferentes criterios de ordenamiento.

**Estructura sugerida:**
```java
public interface IIterator<T> {
    boolean hasNext();
    T next();
}

public interface ICollection<T> {
    IIterator<T> createIterator();
}

public class CursoCollection implements ICollection<Curso> {
    private List<Curso> cursos;

    @Override
    public IIterator<Curso> createIterator() {
        return new CursoIterator(cursos);
    }
}

public class CursoIterator implements IIterator<Curso> {
    private List<Curso> cursos;
    private int posicion = 0;

    @Override
    public boolean hasNext() {
        return posicion < cursos.size();
    }

    @Override
    public Curso next() {
        return cursos.get(posicion++);
    }
}
```

---

#### **MEDIATOR** ❌ (Pendiente)
**Propósito:** Centralizar comunicación en foros y chats.

**Archivos:**
- `patterns/behavioral/mediator/IMediator.java`
- `patterns/behavioral/mediator/ForoMediator.java`
- `patterns/behavioral/mediator/ChatMediator.java`
- `patterns/behavioral/mediator/MensajeMediator.java`

**Caso de uso:** Usuarios envían mensajes al mediador, este los distribuye.

**Estructura sugerida:**
```java
public interface IMediator {
    void enviarMensaje(String mensaje, Usuario emisor);
    void agregarUsuario(Usuario usuario);
}

public class ForoMediator implements IMediator {
    private List<Usuario> usuarios = new ArrayList<>();

    @Override
    public void enviarMensaje(String mensaje, Usuario emisor) {
        for (Usuario usuario : usuarios) {
            if (usuario != emisor) {
                usuario.recibirMensaje(mensaje, emisor);
            }
        }
    }
}

public class Usuario {
    private String nombre;
    private IMediator mediator;

    public void enviar(String mensaje) {
        mediator.enviarMensaje(mensaje, this);
    }

    public void recibirMensaje(String mensaje, Usuario emisor) {
        // Mostrar notificación
    }
}
```

---

#### **MEMENTO** ❌ (Pendiente)
**Propósito:** Guardar y restaurar estados anteriores de cursos.

**Archivos:**
- `patterns/behavioral/memento/CursoMemento.java`
- `patterns/behavioral/memento/CursoOriginator.java`
- `patterns/behavioral/memento/CursoCaretaker.java`
- `patterns/behavioral/memento/HistorialCambios.java`

**Caso de uso:** Versiones de un curso, rollback a versión anterior.

**Estructura sugerida:**
```java
public class CursoMemento {
    private final String nombre;
    private final String descripcion;
    private final LocalDateTime fecha;

    // Constructor, getters
}

public class CursoOriginator {
    private String nombre;
    private String descripcion;

    public CursoMemento guardar() {
        return new CursoMemento(nombre, descripcion, LocalDateTime.now());
    }

    public void restaurar(CursoMemento memento) {
        this.nombre = memento.getNombre();
        this.descripcion = memento.getDescripcion();
    }
}

public class CursoCaretaker {
    private Stack<CursoMemento> historial = new Stack<>();

    public void guardarVersion(CursoOriginator curso) {
        historial.push(curso.guardar());
    }

    public void restaurarVersion(CursoOriginator curso) {
        if (!historial.isEmpty()) {
            curso.restaurar(historial.pop());
        }
    }
}
```

---

#### **OBSERVER** ❌ (Pendiente)
**Propósito:** Notificar automáticamente cambios en cursos a estudiantes inscritos.

**Archivos:**
- `patterns/behavioral/observer/IObserver.java`
- `patterns/behavioral/observer/ISubject.java`
- `patterns/behavioral/observer/CursoSubject.java`
- `patterns/behavioral/observer/EstudianteObserver.java`
- `patterns/behavioral/observer/ProfesorObserver.java`
- `patterns/behavioral/observer/AdminObserver.java`

**Caso de uso:** Profesor actualiza curso → Estudiantes reciben notificación.

**Estructura sugerida:**
```java
public interface IObserver {
    void actualizar(String evento, Object data);
}

public interface ISubject {
    void agregarObservador(IObserver observer);
    void eliminarObservador(IObserver observer);
    void notificarObservadores(String evento, Object data);
}

public class CursoSubject implements ISubject {
    private List<IObserver> observadores = new ArrayList<>();
    private Curso curso;

    @Override
    public void notificarObservadores(String evento, Object data) {
        for (IObserver obs : observadores) {
            obs.actualizar(evento, data);
        }
    }

    public void actualizarContenido(String nuevoContenido) {
        curso.setContenido(nuevoContenido);
        notificarObservadores("CONTENIDO_ACTUALIZADO", nuevoContenido);
    }
}

public class EstudianteObserver implements IObserver {
    private String email;

    @Override
    public void actualizar(String evento, Object data) {
        // Enviar email de notificación
    }
}
```

---

#### **STATE** ❌ (Pendiente)
**Propósito:** Gestionar estados del ciclo de vida de un curso.

**Archivos:**
- `patterns/behavioral/state/IEstadoCurso.java`
- `patterns/behavioral/state/CursoBorrador.java`
- `patterns/behavioral/state/CursoPublicado.java`
- `patterns/behavioral/state/CursoEnProgreso.java`
- `patterns/behavioral/state/CursoFinalizado.java`
- `patterns/behavioral/state/CursoArchivado.java`

**Caso de uso:** Borrador → Publicado → En Progreso → Finalizado → Archivado.

**Estructura sugerida:**
```java
public interface IEstadoCurso {
    void publicar(Curso curso);
    void iniciar(Curso curso);
    void finalizar(Curso curso);
    void archivar(Curso curso);
    String getNombre();
}

public class CursoBorrador implements IEstadoCurso {
    @Override
    public void publicar(Curso curso) {
        // Validar que tiene contenido
        curso.setEstado(new CursoPublicado());
    }

    @Override
    public void iniciar(Curso curso) {
        throw new IllegalStateException("No se puede iniciar un curso en borrador");
    }
}

public class Curso {
    private IEstadoCurso estado;

    public void publicar() {
        estado.publicar(this);
    }

    public void setEstado(IEstadoCurso nuevoEstado) {
        this.estado = nuevoEstado;
    }
}
```

---

#### **STRATEGY** ❌ (Pendiente)
**Propósito:** Diferentes estrategias de evaluación.

**Archivos:**
- `patterns/behavioral/strategy/IEvaluacionStrategy.java`
- `patterns/behavioral/strategy/EvaluacionCuantitativa.java`
- `patterns/behavioral/strategy/EvaluacionCualitativa.java`
- `patterns/behavioral/strategy/EvaluacionMixta.java`
- `patterns/behavioral/strategy/ContextoEvaluacion.java`

**Caso de uso:** Calcular calificación según tipo (numérica, conceptual, mixta).

**Estructura sugerida:**
```java
public interface IEvaluacionStrategy {
    String calcularCalificacion(double puntaje, double maxPuntaje);
}

public class EvaluacionCuantitativa implements IEvaluacionStrategy {
    @Override
    public String calcularCalificacion(double puntaje, double maxPuntaje) {
        return String.format("%.2f", (puntaje / maxPuntaje) * 100);
    }
}

public class EvaluacionCualitativa implements IEvaluacionStrategy {
    @Override
    public String calcularCalificacion(double puntaje, double maxPuntaje) {
        double porcentaje = (puntaje / maxPuntaje) * 100;
        if (porcentaje >= 90) return "Excelente";
        if (porcentaje >= 70) return "Bueno";
        if (porcentaje >= 50) return "Regular";
        return "Insuficiente";
    }
}

public class ContextoEvaluacion {
    private IEvaluacionStrategy strategy;

    public void setStrategy(IEvaluacionStrategy strategy) {
        this.strategy = strategy;
    }

    public String evaluar(double puntaje, double maxPuntaje) {
        return strategy.calcularCalificacion(puntaje, maxPuntaje);
    }
}
```

---

#### **TEMPLATE METHOD** ❌ (Pendiente)
**Propósito:** Definir esqueleto del proceso de inscripción.

**Archivos:**
- `patterns/behavioral/template_method/InscripcionTemplate.java`
- `patterns/behavioral/template_method/InscripcionGratuita.java`
- `patterns/behavioral/template_method/InscripcionPaga.java`
- `patterns/behavioral/template_method/InscripcionBeca.java`

**Caso de uso:** Proceso común de inscripción con pasos variables según tipo.

**Estructura sugerida:**
```java
public abstract class InscripcionTemplate {

    // Template Method
    public final void inscribir(Estudiante estudiante, Curso curso) {
        validarDisponibilidad(curso);
        validarRequisitos(estudiante, curso);
        procesarPago(estudiante, curso);
        crearInscripcion(estudiante, curso);
        enviarConfirmacion(estudiante);
    }

    // Pasos comunes
    private void validarDisponibilidad(Curso curso) {
        if (curso.getCuposDisponibles() <= 0) {
            throw new IllegalStateException("No hay cupos");
        }
    }

    private void crearInscripcion(Estudiante estudiante, Curso curso) {
        // Lógica común
    }

    // Pasos variables (hook methods)
    protected abstract void procesarPago(Estudiante estudiante, Curso curso);
    protected abstract void validarRequisitos(Estudiante estudiante, Curso curso);

    private void enviarConfirmacion(Estudiante estudiante) {
        // Email común
    }
}

public class InscripcionGratuita extends InscripcionTemplate {
    @Override
    protected void procesarPago(Estudiante estudiante, Curso curso) {
        // No hace nada
    }

    @Override
    protected void validarRequisitos(Estudiante estudiante, Curso curso) {
        // Validaciones mínimas
    }
}

public class InscripcionPaga extends InscripcionTemplate {
    @Override
    protected void procesarPago(Estudiante estudiante, Curso curso) {
        // Integración con pasarela de pago
        PagoService.cobrar(estudiante, curso.getPrecio());
    }

    @Override
    protected void validarRequisitos(Estudiante estudiante, Curso curso) {
        // Validar método de pago registrado
    }
}
```

---

#### **VISITOR** ❌ (Pendiente)
**Propósito:** Generar diferentes tipos de reportes sin modificar entidades.

**Archivos:**
- `patterns/behavioral/visitor/IVisitor.java`
- `patterns/behavioral/visitor/IVisitable.java`
- `patterns/behavioral/visitor/ReporteVisitor.java`
- `patterns/behavioral/visitor/EstadisticasVisitor.java`
- `patterns/behavioral/visitor/ExportPDFVisitor.java`
- `patterns/behavioral/visitor/ExportExcelVisitor.java`

**Caso de uso:** Exportar datos a PDF, Excel, generar estadísticas.

**Estructura sugerida:**
```java
public interface IVisitor {
    void visitarCurso(Curso curso);
    void visitarEstudiante(Estudiante estudiante);
    void visitarEvaluacion(Evaluacion evaluacion);
}

public interface IVisitable {
    void aceptar(IVisitor visitor);
}

public class Curso implements IVisitable {
    @Override
    public void aceptar(IVisitor visitor) {
        visitor.visitarCurso(this);
    }
}

public class ExportPDFVisitor implements IVisitor {
    private PDFDocument pdf = new PDFDocument();

    @Override
    public void visitarCurso(Curso curso) {
        pdf.addSection("Curso: " + curso.getNombre());
        pdf.addText("Descripción: " + curso.getDescripcion());
    }

    @Override
    public void visitarEstudiante(Estudiante estudiante) {
        pdf.addSection("Estudiante: " + estudiante.getNombre());
    }

    public PDFDocument generarPDF() {
        return pdf;
    }
}

public class EstadisticasVisitor implements IVisitor {
    private int totalCursos = 0;
    private int totalEstudiantes = 0;

    @Override
    public void visitarCurso(Curso curso) {
        totalCursos++;
    }

    @Override
    public void visitarEstudiante(Estudiante estudiante) {
        totalEstudiantes++;
    }

    public Map<String, Integer> getEstadisticas() {
        return Map.of(
            "cursos", totalCursos,
            "estudiantes", totalEstudiantes
        );
    }
}
```

---

## 🔗 Integración con Spring Boot

### Service Layer Integration

Cada patrón debe integrarse con los servicios de Spring:

```java
@Service
public class CursoService {

    @Autowired
    private CursoRepository repository;

    // BUILDER
    public Curso crearCurso(CursoDTO dto) {
        return new CursoBuilder()
            .setNombre(dto.getNombre())
            .setDescripcion(dto.getDescripcion())
            .build();
    }

    // PROTOTYPE
    public Curso clonarCurso(Long cursoId) {
        Curso original = repository.findById(cursoId)
            .orElseThrow();
        return new CursoPrototype(original).clonar();
    }

    // OBSERVER
    public void actualizarCurso(Long cursoId, CursoDTO dto) {
        CursoSubject subject = new CursoSubject(/* ... */);
        // Agregar observadores (estudiantes inscritos)
        subject.actualizarContenido(dto.getContenido());
    }

    // STATE
    public void publicarCurso(Long cursoId) {
        Curso curso = repository.findById(cursoId).orElseThrow();
        curso.publicar(); // Usa el patrón State
        repository.save(curso);
    }

    // STRATEGY
    public String evaluarEstudiante(Long evaluacionId, double puntaje) {
        Evaluacion eval = evaluacionRepository.findById(evaluacionId)
            .orElseThrow();

        ContextoEvaluacion contexto = new ContextoEvaluacion();

        if (eval.getTipo().equals("CUANTITATIVA")) {
            contexto.setStrategy(new EvaluacionCuantitativa());
        } else {
            contexto.setStrategy(new EvaluacionCualitativa());
        }

        return contexto.evaluar(puntaje, eval.getPuntajeMaximo());
    }
}
```

### Controller Integration

```java
@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    @Autowired
    private CursoService service;

    // FACADE
    @Autowired
    private GestionCursosFacade facade;

    // COMMAND
    @Autowired
    private CommandInvoker invoker;

    @PostMapping
    public ResponseEntity<Curso> crear(@RequestBody CursoDTO dto) {
        ICommand comando = new CrearCursoCommand(service, dto);
        invoker.ejecutar(comando);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/publicar")
    public ResponseEntity<?> publicar(@PathVariable Long id) {
        service.publicarCurso(id); // Usa STATE pattern
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/clonar")
    public ResponseEntity<Curso> clonar(@PathVariable Long id) {
        Curso clonado = service.clonarCurso(id); // Usa PROTOTYPE
        return ResponseEntity.ok(clonado);
    }
}
```

---

## 📊 Mapeo de Requisitos LMS a Patrones

| Funcionalidad LMS | Patrón Aplicado | Propósito |
|------------------|-----------------|-----------|
| **Gestión de Cursos** |
| Crear curso con muchos parámetros | Builder | Construcción fluida |
| Clonar curso plantilla | Prototype | Reutilización |
| Curso Virtual/Presencial/Híbrido | Abstract Factory | Familias coherentes |
| Estados del curso | State | Ciclo de vida |
| Añadir certificado/tutoría | Decorator | Extensión dinámica |
| Estructura modular | Composite | Jerarquía de contenido |
| **Evaluaciones** |
| Diferentes tipos evaluación | Strategy | Algoritmos intercambiables |
| Construir evaluación compleja | Builder | Configuración flexible |
| **Usuarios** |
| Configuración global | Singleton | Instancia única |
| Dashboards multiplataforma | Bridge | Abstracción vs implementación |
| Autenticación multietapa | Chain of Responsibility | Validaciones en cadena |
| **Notificaciones** |
| Email/SMS/Push | Factory Method | Creación polimórfica |
| Notificar cambios en curso | Observer | Publicador-Suscriptor |
| **Búsquedas** |
| Consultas complejas | Interpreter | Lenguaje de búsqueda |
| Iterar resultados | Iterator | Recorrido uniforme |
| **Reportes** |
| Generar PDF/Excel/Stats | Visitor | Operaciones sobre estructuras |
| Construir reporte complejo | Builder | Configuración paso a paso |
| **Recursos Multimedia** |
| Compartir videos/PDFs | Flyweight | Optimización memoria |
| **Comunicación** |
| Foros y chats | Mediator | Comunicación centralizada |
| **Integración** |
| Sistema legacy de notas | Adapter | Compatibilidad |
| **Seguridad** |
| Control de acceso a cursos | Proxy | Protección y caché |
| **Operaciones** |
| CRUD con historial | Command | Encapsulación + Undo/Redo |
| Versiones de curso | Memento | Snapshot de estado |
| Proceso de inscripción | Template Method | Algoritmo esqueleto |
| Simplificar operaciones | Facade | Interfaz unificada |

---

## 🚀 Plan de Implementación Sugerido

### Fase 1: Patrones Creacionales Pendientes (1 patrón)
- [ ] Factory Method (Notificaciones)

### Fase 2: Patrones Estructurales Pendientes (3 patrones)
- [ ] Adapter (Sistema legacy)
- [ ] Composite (Estructura curso)
- [ ] Decorator (Extensiones curso)
- [ ] Proxy (Control acceso)

### Fase 3: Patrones Comportamentales Críticos (4 patrones)
- [ ] Observer (Notificaciones automáticas)
- [ ] State (Estados curso)
- [ ] Strategy (Tipos evaluación)
- [ ] Template Method (Proceso inscripción)

### Fase 4: Patrones Comportamentales Avanzados (7 patrones)
- [ ] Chain of Responsibility (Autenticación)
- [ ] Command (Historial operaciones)
- [ ] Interpreter (Búsquedas)
- [ ] Iterator (Colecciones)
- [ ] Mediator (Comunicación)
- [ ] Memento (Versionado)
- [ ] Visitor (Reportes)

---

## 📝 Convenciones de Código

1. **Interfaces:** Prefijo `I` → `ICommand`, `IObserver`, `IStrategy`
2. **Clases abstractas:** Sufijo `Base` o nombre descriptivo → `ManejadorBase`, `InscripcionTemplate`
3. **Implementaciones concretas:** Nombre descriptivo → `EmailNotificacion`, `CursoBorrador`
4. **Factories:** Sufijo `Factory` → `NotificacionFactory`, `RecursoVisualFactory`
5. **Package structure:** `patterns/{tipo}/{patron}/`

---

## 🔧 Herramientas y Dependencias

```xml
<!-- Útiles para implementar patrones -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
<!-- Para Proxy dinámico -->

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
<!-- Para Cache en Flyweight/Proxy -->

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
</dependency>
<!-- Reduce boilerplate -->
```

---

## 📖 Referencias

- **Libro:** "Design Patterns: Elements of Reusable Object-Oriented Software" (Gang of Four)
- **Refactoring Guru:** https://refactoring.guru/design-patterns
- **Spring Patterns:** https://spring.io/guides

---

**Versión:** 1.0
**Fecha:** 2025-11-29
**Autor:** EduLearn Development Team
