package prototype;

import java.math.BigDecimal;

/**
 * CLIENTE - Demuestra el uso del patron Prototype.
 * Muestra como clonar cursos existentes para nuevos periodos academicos.
 */
public class PrototypeDemo {

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("    DEMOSTRACION DEL PATRON PROTOTYPE");
        System.out.println("    Clonacion de Cursos para Nuevos Periodos");
        System.out.println("==============================================\n");

        // ============================================
        // CREAR UN CURSO ORIGINAL (PROTOTIPO)
        // ============================================
        System.out.println(">>> PASO 1: Crear curso original (prototipo) <<<\n");

        CursoPrototype cursoOriginal = new CursoPrototype(
            1, "PROG-2024", "Programacion Java",
            "Curso completo de programacion en Java",
            "VIRTUAL", 1, "2024-2", 30
        );
        cursoOriginal.setEstrategiaEvaluacion("promedio_simple");

        // Agregar modulos al curso original
        ModuloPrototype modulo1 = new ModuloPrototype("Fundamentos", "Conceptos basicos de Java", 1);
        modulo1.agregarClase(new ClasePrototype("Introduccion a Java", "VIDEO", 45, 1));
        modulo1.agregarClase(new ClasePrototype("Variables y Tipos", "VIDEO", 60, 2));
        modulo1.agregarClase(new ClasePrototype("Practica 1", "PRACTICA", 90, 3));
        modulo1.agregarEvaluacion(new EvaluacionPrototype("Quiz Fundamentos", "QUIZ", new BigDecimal("20")));
        cursoOriginal.agregarModulo(modulo1);

        ModuloPrototype modulo2 = new ModuloPrototype("POO", "Programacion Orientada a Objetos", 2);
        modulo2.agregarClase(new ClasePrototype("Clases y Objetos", "VIDEO", 60, 1));
        modulo2.agregarClase(new ClasePrototype("Herencia", "VIDEO", 55, 2));
        modulo2.agregarClase(new ClasePrototype("Polimorfismo", "VIDEO", 50, 3));
        modulo2.agregarEvaluacion(new EvaluacionPrototype("Tarea POO", "TAREA", new BigDecimal("30")));
        modulo2.agregarEvaluacion(new EvaluacionPrototype("Examen Parcial", "EXAMEN", new BigDecimal("50")));
        cursoOriginal.agregarModulo(modulo2);

        System.out.println("Curso original creado:");
        System.out.println("  " + cursoOriginal.getResumen().replace("\n", "\n  "));
        System.out.println();

        // ============================================
        // CLONAR CURSO PARA NUEVO PERIODO
        // ============================================
        System.out.println("\n>>> PASO 2: Clonar curso para periodo 2025-1 <<<\n");

        CursoPrototype cursoClon2025_1 = cursoOriginal.clonarParaNuevoPeriodo("2025-1");

        System.out.println("Curso clonado para 2025-1:");
        System.out.println("  " + cursoClon2025_1.getResumen().replace("\n", "\n  "));
        System.out.println();

        // ============================================
        // CLONAR PARA OTRO PERIODO
        // ============================================
        System.out.println("\n>>> PASO 3: Clonar curso para periodo 2025-2 <<<\n");

        CursoPrototype cursoClon2025_2 = cursoOriginal.clonarParaNuevoPeriodo("2025-2");

        System.out.println("Curso clonado para 2025-2:");
        System.out.println("  " + cursoClon2025_2.getResumen().replace("\n", "\n  "));
        System.out.println();

        // ============================================
        // MODIFICAR CLON SIN AFECTAR ORIGINAL
        // ============================================
        System.out.println("\n>>> PASO 4: Modificar clon (no afecta al original) <<<\n");

        cursoClon2025_1.setNombre("Programacion Java Avanzado");
        cursoClon2025_1.setCupoMaximo(50);

        // Agregar modulo extra solo al clon
        ModuloPrototype moduloExtra = new ModuloPrototype("Patrones de Diseno", "Design Patterns", 3);
        moduloExtra.agregarClase(new ClasePrototype("Patron Singleton", "VIDEO", 40, 1));
        moduloExtra.agregarClase(new ClasePrototype("Patron Factory", "VIDEO", 45, 2));
        moduloExtra.agregarEvaluacion(new EvaluacionPrototype("Proyecto Final", "PROYECTO", new BigDecimal("100")));
        cursoClon2025_1.agregarModulo(moduloExtra);

        System.out.println("Clon 2025-1 modificado:");
        System.out.println("  Nombre: " + cursoClon2025_1.getNombre());
        System.out.println("  Cupo: " + cursoClon2025_1.getCupoMaximo());
        System.out.println("  Modulos: " + cursoClon2025_1.getModulos().size());
        System.out.println();

        System.out.println("Original (sin cambios):");
        System.out.println("  Nombre: " + cursoOriginal.getNombre());
        System.out.println("  Cupo: " + cursoOriginal.getCupoMaximo());
        System.out.println("  Modulos: " + cursoOriginal.getModulos().size());

        // ============================================
        // RESUMEN FINAL
        // ============================================
        System.out.println("\n==============================================");
        System.out.println("         RESUMEN DE CURSOS");
        System.out.println("==============================================");
        System.out.println("1. ORIGINAL: " + cursoOriginal);
        System.out.println("2. CLON 2025-1: " + cursoClon2025_1);
        System.out.println("3. CLON 2025-2: " + cursoClon2025_2);
        System.out.println("==============================================");

        System.out.println("\n[FIN DE LA DEMOSTRACION]");
    }
}
