package builder;

/**
 * CLIENTE - Demuestra el uso del patrón Builder.
 *
 * El cliente:
 * 1. Crea el Builder que quiera usar
 * 2. Crea el Director y le pasa el Builder
 * 3. Pide al Director que construya algo
 * 4. Al final obtiene el producto llamando a builder.getResultado()
 */
public class BuilderDemo {

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("    DEMOSTRACION DEL PATRON BUILDER");
        System.out.println("    Construccion de Cursos paso a paso");
        System.out.println("==============================================\n");

        // ============================================
        // EJEMPLO 1: Usando el Director
        // ============================================
        System.out.println(">>> EJEMPLO 1: Construccion con Director <<<\n");

        // 1. Cliente crea el Builder
        ICursoBuilder builder = new CursoCompletoBuilder();

        // 2. Cliente crea el Director y le pasa el Builder
        CursoDirector director = new CursoDirector(builder);

        // 3. Cliente pide al Director que construya un curso básico
        System.out.println("--- Construyendo Curso Basico ---");
        director.construirCursoBasico();

        // 4. Cliente obtiene el producto
        CursoCompleto cursoBasico = builder.getResultado();
        cursoBasico.mostrarEstructura();

        // Construir curso estándar
        System.out.println("\n--- Construyendo Curso Estandar ---");
        director.construirCursoEstandar();
        CursoCompleto cursoEstandar = builder.getResultado();
        cursoEstandar.mostrarEstructura();

        // Construir curso avanzado
        System.out.println("\n--- Construyendo Curso Avanzado ---");
        director.construirCursoAvanzado();
        CursoCompleto cursoAvanzado = builder.getResultado();
        cursoAvanzado.mostrarEstructura();

        // ============================================
        // EJEMPLO 2: Usando el Builder directamente (sin Director)
        // Útil cuando se necesita personalización completa
        // ============================================
        System.out.println("\n>>> EJEMPLO 2: Construccion Personalizada (sin Director) <<<\n");

        // 1. Cliente crea un nuevo Builder
        ICursoBuilder builderPersonalizado = new CursoCompletoBuilder();

        // 2. Cliente construye paso a paso directamente
        builderPersonalizado
                .setInformacionBasica(
                        "CUSTOM-001",
                        "Curso Personalizado de Machine Learning",
                        "Introduccion al aprendizaje automatico con Python")
                .setTipoCurso(CursoCompleto.TipoCurso.VIRTUAL)
                .setPeriodoAcademico("2025-2")
                .setInstructor("Dr. Carlos Rodriguez")
                .setCupoMaximo(50)
                .setDuracionYNivel(60, "Intermedio");

        // Agregar módulos personalizados
        Modulo moduloML1 = builderPersonalizado.agregarModulo(
                "Fundamentos de ML",
                "Conceptos básicos de Machine Learning");

        builderPersonalizado
                .agregarClase(moduloML1, "Que es Machine Learning?",
                        "Introduccion y tipos de aprendizaje",
                        Clase.TipoClase.VIDEO, 45)
                .agregarClase(moduloML1, "Configurar Python y Jupyter",
                        "Ambiente de desarrollo para ML",
                        Clase.TipoClase.PRACTICA, 60)
                .agregarEvaluacion(moduloML1, "Quiz Fundamentos",
                        EvaluacionModulo.TipoEvaluacion.QUIZ, 20.0);

        Modulo moduloML2 = builderPersonalizado.agregarModulo(
                "Regresion y Clasificacion",
                "Algoritmos supervisados basicos");

        builderPersonalizado
                .agregarClase(moduloML2, "Regresion Lineal",
                        "Prediccion de valores continuos",
                        Clase.TipoClase.VIDEO, 90)
                .agregarClase(moduloML2, "Regresion Logistica",
                        "Clasificacion binaria",
                        Clase.TipoClase.VIDEO, 90)
                .agregarClase(moduloML2, "Proyecto Practico",
                        "Implementar modelo de prediccion",
                        Clase.TipoClase.PRACTICA, 120)
                .agregarEvaluacion(moduloML2, "Proyecto Prediccion",
                        EvaluacionModulo.TipoEvaluacion.PROYECTO, 40.0);

        Modulo moduloML3 = builderPersonalizado.agregarModulo(
                "Redes Neuronales",
                "Introduccion a Deep Learning");

        builderPersonalizado
                .agregarClase(moduloML3, "Perceptron",
                        "La neurona artificial",
                        Clase.TipoClase.VIDEO, 60)
                .agregarClase(moduloML3, "Redes Multicapa",
                        "Arquitectura de redes profundas",
                        Clase.TipoClase.VIDEO, 90)
                .agregarEvaluacion(moduloML3, "Examen Final",
                        EvaluacionModulo.TipoEvaluacion.EXAMEN, 40.0);

        // 3. Obtener el producto construido
        CursoCompleto cursoML = builderPersonalizado.getResultado();
        cursoML.mostrarEstructura();

        // ============================================
        // Resumen final
        // ============================================
        System.out.println("\n==============================================");
        System.out.println("         RESUMEN DE CURSOS CREADOS");
        System.out.println("==============================================");
        System.out.println("1. " + cursoBasico);
        System.out.println("2. " + cursoEstandar);
        System.out.println("3. " + cursoAvanzado);
        System.out.println("4. " + cursoML);
        System.out.println("==============================================");

        System.out.println("\n[FIN DE LA DEMOSTRACION]");
    }
}
