package builder;

/**
 * DIRECTOR - Define el orden en que se ejecutan los pasos del builder.
 * El Director conoce las recetas para construir diferentes tipos de cursos
 * y orquesta la construcción utilizando el builder proporcionado.
 */
public class CursoDirector {

    private ICursoBuilder builder;

    public CursoDirector(ICursoBuilder builder) {
        this.builder = builder;
    }

    public void setBuilder(ICursoBuilder builder) {
        this.builder = builder;
    }

    /**
     * Construye un curso básico con estructura mínima:
     * - 1 módulo con 2 clases y 1 evaluación
     */
    public void construirCursoBasico() {
        builder.reset();

        builder.setInformacionBasica(
                        "CURSO-001",
                        "Curso Introductorio",
                        "Curso básico para principiantes")
                .setTipoCurso(CursoCompleto.TipoCurso.VIRTUAL)
                .setPeriodoAcademico("2025-1")
                .setInstructor("Profesor Demo")
                .setCupoMaximo(25)
                .setDuracionYNivel(10, "Basico");

        // Módulo único
        Modulo modulo1 = builder.agregarModulo(
                "Introduccion",
                "Módulo introductorio del curso");

        builder.agregarClase(modulo1, "Bienvenida", "Presentación del curso",
                        Clase.TipoClase.VIDEO, 15)
                .agregarClase(modulo1, "Conceptos Basicos", "Fundamentos iniciales",
                        Clase.TipoClase.LECTURA, 30)
                .agregarEvaluacion(modulo1, "Quiz Introductorio",
                        EvaluacionModulo.TipoEvaluacion.QUIZ, 100.0);
    }

    /**
     * Construye un curso estándar con estructura intermedia:
     * - 3 módulos
     * - Cada módulo con clases variadas y evaluaciones
     */
    public void construirCursoEstandar() {
        builder.reset();

        builder.setInformacionBasica(
                        "CURSO-002",
                        "Curso de Programacion Java",
                        "Aprende Java desde cero hasta nivel intermedio")
                .setTipoCurso(CursoCompleto.TipoCurso.HIBRIDO)
                .setPeriodoAcademico("2025-1")
                .setInstructor("Dr. Juan Perez")
                .setCupoMaximo(30)
                .setDuracionYNivel(40, "Intermedio");

        // Módulo 1: Fundamentos
        Modulo modulo1 = builder.agregarModulo(
                "Fundamentos de Java",
                "Conceptos básicos del lenguaje Java");

        builder.agregarClase(modulo1, "Introduccion a Java", "Historia y caracteristicas",
                        Clase.TipoClase.VIDEO, 45)
                .agregarClase(modulo1, "Instalacion del JDK", "Configurar ambiente de desarrollo",
                        Clase.TipoClase.PRACTICA, 30)
                .agregarClase(modulo1, "Primer Programa", "Hola Mundo en Java",
                        Clase.TipoClase.PRACTICA, 40)
                .agregarEvaluacion(modulo1, "Quiz de Fundamentos",
                        EvaluacionModulo.TipoEvaluacion.QUIZ, 15.0);

        // Módulo 2: POO
        Modulo modulo2 = builder.agregarModulo(
                "Programacion Orientada a Objetos",
                "Clases, objetos, herencia y polimorfismo");

        builder.agregarClase(modulo2, "Clases y Objetos", "Definir clases y crear objetos",
                        Clase.TipoClase.VIDEO, 60)
                .agregarClase(modulo2, "Herencia", "Reutilizacion de codigo",
                        Clase.TipoClase.VIDEO, 50)
                .agregarClase(modulo2, "Polimorfismo", "Flexibilidad en el diseño",
                        Clase.TipoClase.VIDEO, 55)
                .agregarClase(modulo2, "Practica POO", "Ejercicios de POO",
                        Clase.TipoClase.PRACTICA, 90)
                .agregarEvaluacion(modulo2, "Tarea de POO",
                        EvaluacionModulo.TipoEvaluacion.TAREA, 25.0)
                .agregarEvaluacion(modulo2, "Examen Parcial",
                        EvaluacionModulo.TipoEvaluacion.EXAMEN, 20.0);

        // Módulo 3: Patrones
        Modulo modulo3 = builder.agregarModulo(
                "Patrones de Diseno",
                "Patrones de diseño más utilizados");

        builder.agregarClase(modulo3, "Introduccion a Patrones", "Que son los patrones",
                        Clase.TipoClase.LECTURA, 30)
                .agregarClase(modulo3, "Patron Singleton", "Instancia única",
                        Clase.TipoClase.VIDEO, 40)
                .agregarClase(modulo3, "Patron Factory", "Creación de objetos",
                        Clase.TipoClase.VIDEO, 45)
                .agregarClase(modulo3, "Patron Builder", "Construcción paso a paso",
                        Clase.TipoClase.VIDEO, 50)
                .agregarEvaluacion(modulo3, "Proyecto Final",
                        EvaluacionModulo.TipoEvaluacion.PROYECTO, 40.0);
    }

    /**
     * Construye un curso avanzado con estructura completa:
     * - 5 módulos
     * - Múltiples tipos de clases y evaluaciones
     * - Proyecto integrador
     */
    public void construirCursoAvanzado() {
        builder.reset();

        builder.setInformacionBasica(
                        "CURSO-003",
                        "Arquitectura de Software Empresarial",
                        "Diseño y desarrollo de sistemas empresariales robustos")
                .setTipoCurso(CursoCompleto.TipoCurso.PRESENCIAL)
                .setPeriodoAcademico("2025-1")
                .setInstructor("Dra. Maria Garcia")
                .setCupoMaximo(20)
                .setDuracionYNivel(80, "Avanzado");

        // Módulo 1
        Modulo m1 = builder.agregarModulo(
                "Principios SOLID",
                "Los 5 principios del diseño orientado a objetos");
        builder.agregarClase(m1, "Single Responsibility", "Principio de responsabilidad única",
                        Clase.TipoClase.VIDEO, 60)
                .agregarClase(m1, "Open/Closed", "Abierto para extensión, cerrado para modificación",
                        Clase.TipoClase.VIDEO, 60)
                .agregarClase(m1, "Liskov Substitution", "Sustitución de Liskov",
                        Clase.TipoClase.VIDEO, 60)
                .agregarClase(m1, "Interface Segregation", "Segregación de interfaces",
                        Clase.TipoClase.VIDEO, 60)
                .agregarClase(m1, "Dependency Inversion", "Inversión de dependencias",
                        Clase.TipoClase.VIDEO, 60)
                .agregarEvaluacion(m1, "Quiz SOLID",
                        EvaluacionModulo.TipoEvaluacion.QUIZ, 10.0);

        // Módulo 2
        Modulo m2 = builder.agregarModulo(
                "Patrones Creacionales",
                "Factory, Abstract Factory, Builder, Prototype, Singleton");
        builder.agregarClase(m2, "Factory Method", "Creación delegada a subclases",
                        Clase.TipoClase.VIDEO, 90)
                .agregarClase(m2, "Abstract Factory", "Familias de objetos",
                        Clase.TipoClase.VIDEO, 90)
                .agregarClase(m2, "Builder", "Construcción paso a paso",
                        Clase.TipoClase.VIDEO, 90)
                .agregarClase(m2, "Laboratorio Patrones", "Práctica de patrones",
                        Clase.TipoClase.PRACTICA, 180)
                .agregarEvaluacion(m2, "Tarea Patrones Creacionales",
                        EvaluacionModulo.TipoEvaluacion.TAREA, 15.0);

        // Módulo 3
        Modulo m3 = builder.agregarModulo(
                "Patrones Estructurales",
                "Adapter, Decorator, Facade, Composite, Proxy");
        builder.agregarClase(m3, "Adapter", "Compatibilidad entre interfaces",
                        Clase.TipoClase.VIDEO, 60)
                .agregarClase(m3, "Decorator", "Añadir funcionalidad dinámicamente",
                        Clase.TipoClase.VIDEO, 60)
                .agregarClase(m3, "Facade", "Simplificar interfaces complejas",
                        Clase.TipoClase.VIDEO, 60)
                .agregarEvaluacion(m3, "Examen Parcial",
                        EvaluacionModulo.TipoEvaluacion.EXAMEN, 20.0);

        // Módulo 4
        Modulo m4 = builder.agregarModulo(
                "Patrones de Comportamiento",
                "Strategy, Observer, Command, State");
        builder.agregarClase(m4, "Strategy", "Algoritmos intercambiables",
                        Clase.TipoClase.VIDEO, 90)
                .agregarClase(m4, "Observer", "Notificación de cambios",
                        Clase.TipoClase.VIDEO, 90)
                .agregarClase(m4, "Command", "Encapsular solicitudes",
                        Clase.TipoClase.VIDEO, 90)
                .agregarClase(m4, "Casos de Estudio", "Análisis de sistemas reales",
                        Clase.TipoClase.INTERACTIVA, 120)
                .agregarEvaluacion(m4, "Tarea Integradora",
                        EvaluacionModulo.TipoEvaluacion.TAREA, 15.0);

        // Módulo 5
        Modulo m5 = builder.agregarModulo(
                "Proyecto Integrador",
                "Aplicar todos los conceptos en un proyecto real");
        builder.agregarClase(m5, "Definicion del Proyecto", "Alcance y requisitos",
                        Clase.TipoClase.INTERACTIVA, 120)
                .agregarClase(m5, "Diseño Arquitectonico", "Diseño de la solución",
                        Clase.TipoClase.PRACTICA, 180)
                .agregarClase(m5, "Implementacion", "Desarrollo del proyecto",
                        Clase.TipoClase.PRACTICA, 240)
                .agregarClase(m5, "Presentacion Final", "Defensa del proyecto",
                        Clase.TipoClase.INTERACTIVA, 60)
                .agregarEvaluacion(m5, "Proyecto Final",
                        EvaluacionModulo.TipoEvaluacion.PROYECTO, 40.0);
    }
}
