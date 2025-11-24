package plataformacursos;

import abstractfactory.*;
import model.Curso;
import model.Material;
import model.Evaluacion;
import dao.*;
import ui.CrearCursoVista;

// Importar los nuevos patrones
import adapter.*;
import bridge.*;
import composite.*;
import decorator.*;
import facade.*;

import javax.swing.*;
import java.util.Scanner;

/**
 * Clase principal que demuestra el uso del patrón Abstract Factory
 * Implementación completa según la definición original del patrón:
 * - Productos Abstractos (ICurso, IMaterial, IEvaluacion)
 * - Productos Concretos (CursoPresencial, MaterialVirtual, etc.)
 * - Fábrica Abstracta (CourseComponentFactory)
 * - Fábricas Concretas (PresencialCourseFactory, VirtualCourseFactory, HibridoCourseFactory)
 * @author USUARIO
 */
public class PlataformaCursos {

    public static void main(String[] args) {
        // Preguntar al usuario qué modo quiere ejecutar
        if (args.length > 0 && args[0].equals("--consola")) {
            ejecutarModoConsola();
        } else {
            mostrarMenuInicial();
        }
    }

    private static void mostrarMenuInicial() {
        Object[] opciones = {"Interfaz Gráfica", "Modo Consola", "Salir"};
        int seleccion = JOptionPane.showOptionDialog(
            null,
            "¿Cómo deseas ejecutar la aplicación?",
            "Plataforma de Cursos - Abstract Factory",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
        );

        switch (seleccion) {
            case 0: // Interfaz Gráfica
                ejecutarModoGUI();
                break;
            case 1: // Modo Consola
                ejecutarModoConsola();
                break;
            default: // Salir o cerrar
                System.exit(0);
        }
    }

    private static void ejecutarModoGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            CrearCursoVista window = new CrearCursoVista();
            window.setVisible(true);
        });
    }

    private static void ejecutarModoConsola() {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   PLATAFORMA DE CURSOS - PATRÓN ABSTRACT FACTORY           ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");

        // Crear DAOs para persistencia en base de datos
        CursoDAO cursoDAO = new CursoDAO();
        MaterialDAO materialDAO = new MaterialDAO();
        EvaluacionDAO evaluacionDAO = new EvaluacionDAO();

        // =================================================================
        // EJEMPLO 1: Crear familia de productos PRESENCIALES
        // =================================================================
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  EJEMPLO 1: Familia de Productos PRESENCIALES");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        CourseComponentFactory presencialFactory = new PresencialCourseFactory();
        crearYMostrarCursoCompleto(presencialFactory, "PRES-001", "Matemáticas Avanzadas",
                                   "Curso presencial de matemáticas", 1, "2024-1",
                                   cursoDAO, materialDAO, evaluacionDAO);

        // =================================================================
        // EJEMPLO 2: Crear familia de productos VIRTUALES
        // =================================================================
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  EJEMPLO 2: Familia de Productos VIRTUALES");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        CourseComponentFactory virtualFactory = new VirtualCourseFactory();
        crearYMostrarCursoCompleto(virtualFactory, "VIRT-001", "Programación Web",
                                   "Curso virtual de desarrollo web", 1, "2024-1",
                                   cursoDAO, materialDAO, evaluacionDAO);

        // =================================================================
        // EJEMPLO 3: Crear familia de productos HÍBRIDOS
        // =================================================================
        System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println("  EJEMPLO 3: Familia de Productos HÍBRIDOS");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n");

        CourseComponentFactory hibridoFactory = new HibridoCourseFactory();
        crearYMostrarCursoCompleto(hibridoFactory, "HIBR-001", "Inteligencia Artificial",
                                   "Curso híbrido de IA y Machine Learning", 1, "2024-1",
                                   cursoDAO, materialDAO, evaluacionDAO);

        // =================================================================
        // MOSTRAR RESUMEN FINAL
        // =================================================================
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║              RESUMEN FINAL DE CURSOS CREADOS               ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        mostrarResumenCursos(cursoDAO);

        System.out.println("\n✓ Patrón Abstract Factory implementado correctamente!");
        System.out.println("  Familias de productos creadas: 3 (Presencial, Virtual, Híbrido)");
        System.out.println("  Productos por familia: 3 (Curso, Material, Evaluación)");

        // =================================================================
        // DEMOSTRACIÓN DE LOS 5 NUEVOS PATRONES
        // =================================================================

        // PATRÓN ADAPTER
        demoAdapter();

        // PATRÓN BRIDGE
        demoBridge();

        // PATRÓN COMPOSITE
        demoComposite();

        // PATRÓN DECORATOR
        demoDecorator();

        // PATRÓN FACADE
        demoFacade();

        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   TODOS LOS PATRONES IMPLEMENTADOS CORRECTAMENTE           ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    // ═══════════════════════════════════════════════════════════════
    // PATRÓN ADAPTER - Integración de herramientas externas
    // ═══════════════════════════════════════════════════════════════
    private static void demoAdapter() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("PATRÓN ADAPTER - Integración de herramientas externas");
        System.out.println("═".repeat(60));

        // Usar Zoom a través del adapter
        IVideoconferencia zoom = new ZoomAdapter();
        zoom.crearReunion("Clase de Patrones de Diseño", 60);
        System.out.println("Enlace Zoom: " + zoom.obtenerEnlace());
        zoom.iniciarReunion();
        zoom.finalizarReunion();

        System.out.println();

        // Usar Google Meet a través del adapter
        IVideoconferencia meet = new GoogleMeetAdapter();
        meet.crearReunion("Tutoría de Java", 30);
        System.out.println("Enlace Meet: " + meet.obtenerEnlace());

        System.out.println();

        // Usar Google Drive a través del adapter
        IRepositorioArchivos drive = new GoogleDriveAdapter();
        String fileId = drive.subirArchivo("material.pdf", "contenido".getBytes());
        System.out.println("URL pública: " + drive.obtenerUrlPublica(fileId));

        System.out.println("\n✓ Patrón Adapter implementado correctamente!");
    }

    // ═══════════════════════════════════════════════════════════════
    // PATRÓN BRIDGE - Interfaz independiente del dispositivo
    // ═══════════════════════════════════════════════════════════════
    private static void demoBridge() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("PATRÓN BRIDGE - Interfaz independiente del dispositivo");
        System.out.println("═".repeat(60));

        // Crear un curso
        CursoBridge curso = new CursoBridge(new WebRenderer());
        curso.cargarDatos("PAT-001", "Patrones de Diseño", "Aprende los 23 patrones GoF", "virtual");

        // Crear módulo con materiales
        ModuloBridge modulo = new ModuloBridge(new WebRenderer(), "Introducción", 1);
        modulo.agregarMaterial(new MaterialBridge(new WebRenderer(), "Video Intro", "video", "http://video.com/1"));
        modulo.agregarEvaluacion(new EvaluacionBridge(new WebRenderer(), "Quiz 1", "quiz", 100));
        curso.agregarModulo(modulo);

        // Mostrar en WEB
        System.out.println("\n--- Renderizado en WEB ---");
        curso.mostrar();

        // Cambiar a MÓVIL
        System.out.println("\n--- Renderizado en MÓVIL ---");
        curso.setRenderer(new MobileRenderer());
        curso.mostrar();

        // Cambiar a SMART TV
        System.out.println("\n--- Renderizado en SMART TV ---");
        curso.setRenderer(new SmartTVRenderer());
        curso.mostrar();

        System.out.println("\n✓ Patrón Bridge implementado correctamente!");
    }

    // ═══════════════════════════════════════════════════════════════
    // PATRÓN COMPOSITE - Módulos y submódulos anidados
    // ═══════════════════════════════════════════════════════════════
    private static void demoComposite() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("PATRÓN COMPOSITE - Módulos y submódulos anidados");
        System.out.println("═".repeat(60));

        // Crear curso
        CursoComposite curso = new CursoComposite("JAVA-101", "Programación en Java");

        // Unidad 1
        Unidad unidad1 = new Unidad("Fundamentos de Java", 1);

        Leccion leccion1 = new Leccion("Variables y Tipos de Datos");
        leccion1.agregar(new Actividad("Introducción a Variables", "video", 15));
        leccion1.agregar(new Actividad("Tipos Primitivos", "lectura", 10));
        leccion1.agregar(new Actividad("Ejercicio Práctico", "practica", 20));

        Leccion leccion2 = new Leccion("Operadores");
        leccion2.agregar(new Actividad("Operadores Aritméticos", "video", 12));
        leccion2.agregar(new Actividad("Quiz de Operadores", "quiz", 15));

        unidad1.agregar(leccion1);
        unidad1.agregar(leccion2);

        // Unidad 2
        Unidad unidad2 = new Unidad("POO en Java", 2);

        Leccion leccion3 = new Leccion("Clases y Objetos");
        leccion3.agregar(new Actividad("Conceptos de POO", "video", 20));
        leccion3.agregar(new Actividad("Crear tu primera clase", "practica", 30));

        unidad2.agregar(leccion3);

        // Agregar unidades al curso
        curso.agregar(unidad1);
        curso.agregar(unidad2);

        // Mostrar estructura completa
        curso.mostrar(0);

        System.out.println("\nTotal de actividades: " + curso.contarActividades());
        System.out.println("\n✓ Patrón Composite implementado correctamente!");
    }

    // ═══════════════════════════════════════════════════════════════
    // PATRÓN DECORATOR - Extender módulos con funcionalidades
    // ═══════════════════════════════════════════════════════════════
    private static void demoDecorator() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("PATRÓN DECORATOR - Extender módulos con funcionalidades");
        System.out.println("═".repeat(60));

        // Módulo base
        IModuloEducativo modulo = new ModuloBase("Introducción a Python", "Aprende los fundamentos de Python");
        System.out.println("\n--- Módulo Base ---");
        modulo.mostrarInfo();

        // Agregar gamificación
        modulo = new GamificacionDecorator(modulo);
        System.out.println("\n--- Con Gamificación ---");
        System.out.println("Costo adicional: $" + modulo.getCostoAdicional());

        // Agregar certificación
        modulo = new CertificacionDecorator(modulo);
        System.out.println("\n--- Con Gamificación + Certificación ---");
        System.out.println("Costo adicional: $" + modulo.getCostoAdicional());

        // Agregar tutoría
        modulo = new TutoriaDecorator(modulo);
        System.out.println("\n--- Con Gamificación + Certificación + Tutoría ---");
        modulo.mostrarInfo();

        System.out.println("\nCaracterísticas finales: " + modulo.getCaracteristicas());
        System.out.println("Costo adicional total: $" + modulo.getCostoAdicional());
        System.out.println("\n✓ Patrón Decorator implementado correctamente!");
    }

    // ═══════════════════════════════════════════════════════════════
    // PATRÓN FACADE - Interfaz unificada del sistema
    // ═══════════════════════════════════════════════════════════════
    private static void demoFacade() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("PATRÓN FACADE - Interfaz unificada del sistema");
        System.out.println("═".repeat(60));

        // Crear fachada del LMS
        LMSFacade lms = new LMSFacade();

        // Mostrar información de un curso (ID 4 existe en la BD)
        lms.mostrarInfoCurso(4);

        // Inscribir estudiante (operación simplificada)
        System.out.println("\n--- Proceso de Inscripción ---");
        lms.inscribirEstudiante(1, 4);

        // Mostrar contenido del curso
        System.out.println("\n--- Acceder al Contenido ---");
        lms.mostrarContenidoCurso(1, 4);

        // Realizar evaluación
        System.out.println("\n--- Realizar Evaluación ---");
        LMSFacade.ResultadoEvaluacion resultado = lms.realizarEvaluacion(1, 1);
        System.out.println("Resultado: " + resultado.getMensaje() + " - Calificación: " + resultado.getCalificacion());

        // Obtener resumen del estudiante
        System.out.println("\n--- Resumen del Estudiante ---");
        LMSFacade.ResumenEstudiante resumen = lms.obtenerResumenEstudiante(1, 4);
        resumen.mostrar();

        System.out.println("\n✓ Patrón Facade implementado correctamente!");
    }

    /**
     * Demuestra el uso del patrón Abstract Factory
     * 1. Usa la fábrica para crear productos abstractos
     * 2. Muestra la información de los productos
     * 3. Convierte a modelos de BD y persiste
     */
    private static void crearYMostrarCursoCompleto(CourseComponentFactory factory,
                                                   String codigo, String nombre, String descripcion,
                                                   int profesorId, String periodo,
                                                   CursoDAO cursoDAO, MaterialDAO materialDAO,
                                                   EvaluacionDAO evaluacionDAO) {

        System.out.println("Fábrica: " + factory.getTipoFactory() + "CourseFactory");
        System.out.println();

        // 1. CREAR PRODUCTOS ABSTRACTOS usando la fábrica
        ICurso curso = factory.crearCurso(codigo, nombre, descripcion, profesorId, periodo);
        IMaterial material = factory.crearMaterial("Material Principal", "Material del módulo 1");
        IEvaluacion evaluacion = factory.crearEvaluacion("Evaluación 1", "Primera evaluación");

        System.out.println();

        // 2. MOSTRAR INFORMACIÓN de los productos abstractos
        curso.mostrarInfo();
        material.mostrarInfo();
        evaluacion.mostrarInfo();

        System.out.println();

        // 3. CONVERTIR a modelos y PERSISTIR en base de datos
        System.out.println("Guardando en base de datos...");
        Curso cursoModel = curso.toModel();
        int cursoId = cursoDAO.insertar(cursoModel);

        if (cursoId > 0) {
            System.out.println("  ✓ Curso guardado con ID: " + cursoId);

            Material materialModel = material.toModel(1); // Módulo ID = 1
            int materialId = materialDAO.insertar(materialModel);
            if (materialId > 0) {
                System.out.println("  ✓ Material guardado con ID: " + materialId);
            }

            Evaluacion evaluacionModel = evaluacion.toModel(1); // Módulo ID = 1
            int evaluacionId = evaluacionDAO.insertar(evaluacionModel);
            if (evaluacionId > 0) {
                System.out.println("  ✓ Evaluación guardada con ID: " + evaluacionId);
            }
        } else {
            System.err.println("  ✗ Error: No se pudo guardar el curso");
        }
    }

    /**
     * Muestra un resumen de todos los cursos creados desde la base de datos
     */
    private static void mostrarResumenCursos(CursoDAO cursoDAO) {
        var cursos = cursoDAO.obtenerTodos();

        System.out.println("\nTotal de cursos en BD: " + cursos.size() + "\n");

        for (Curso curso : cursos) {
            System.out.println("  • " + curso.getNombre());
            System.out.println("    ├─ Código: " + curso.getCodigo());
            System.out.println("    ├─ Tipo: " + curso.getTipoCurso());
            System.out.println("    ├─ Estado: " + curso.getEstado());
            System.out.println("    ├─ Estrategia: " + curso.getEstrategiaEvaluacion());
            System.out.println("    └─ Cupo máximo: " + curso.getCupoMaximo() + " estudiantes");
            System.out.println();
        }
    }
}
