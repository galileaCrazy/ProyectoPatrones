package com.edulearn.patterns.estructural.decorator;

/**
 * Clase Cliente - Demostración del patrón Decorator
 * Muestra cómo se pueden añadir responsabilidades dinámicamente a un objeto.
 */
public class DecoratorDemo {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("PATRÓN DECORATOR - MÓDULOS EDUCATIVOS");
        System.out.println("========================================\n");

        // 1. Módulo básico sin decoradores
        System.out.println("1. MÓDULO BÁSICO (sin decoradores):");
        System.out.println("------------------------------------");
        ModuloEducativo moduloBasico = new ModuloBasico(
            "Introducción a Java",
            "Contenido: Variables, tipos de datos, operadores y estructuras de control."
        );
        System.out.println(moduloBasico.mostrarContenido());
        System.out.println("\n");

        // 2. Módulo con Gamificación
        System.out.println("2. MÓDULO CON GAMIFICACIÓN:");
        System.out.println("------------------------------------");
        ModuloEducativo moduloConGamificacion = new GamificacionDecorator(
            new ModuloBasico(
                "Programación Orientada a Objetos",
                "Contenido: Clases, objetos, herencia, polimorfismo y encapsulación."
            ),
            150, // puntos
            "Maestro POO" // badge
        );
        System.out.println(moduloConGamificacion.mostrarContenido());
        System.out.println("\n");

        // 3. Módulo con Certificación
        System.out.println("3. MÓDULO CON CERTIFICACIÓN:");
        System.out.println("------------------------------------");
        ModuloEducativo moduloConCertificacion = new CertificacionDecorator(
            new ModuloBasico(
                "Patrones de Diseño",
                "Contenido: Patrones creacionales, estructurales y de comportamiento."
            ),
            "Certificado Profesional", // tipo de certificado
            true // certificado activo
        );
        System.out.println(moduloConCertificacion.mostrarContenido());
        System.out.println("\n");

        // 4. Módulo con ambos decoradores (Gamificación + Certificación)
        System.out.println("4. MÓDULO CON GAMIFICACIÓN Y CERTIFICACIÓN:");
        System.out.println("------------------------------------");
        ModuloEducativo moduloCompleto = new CertificacionDecorator(
            new GamificacionDecorator(
                new ModuloBasico(
                    "Arquitectura de Software",
                    "Contenido: Microservicios, arquitecturas limpias y mejores prácticas."
                ),
                200, // puntos
                "Arquitecto Experto" // badge
            ),
            "Certificado de Especialización", // tipo de certificado
            true // certificado activo
        );
        System.out.println(moduloCompleto.mostrarContenido());
        System.out.println("\n");

        // 5. Demostración de flexibilidad: orden inverso de decoradores
        System.out.println("5. MÓDULO CON ORDEN INVERSO (Certificación + Gamificación):");
        System.out.println("------------------------------------");
        ModuloEducativo moduloFlexible = new GamificacionDecorator(
            new CertificacionDecorator(
                new ModuloBasico(
                    "Desarrollo Web Full Stack",
                    "Contenido: Frontend, Backend, Bases de datos y Deployment."
                ),
                "Certificado Full Stack Developer", // tipo de certificado
                true // certificado activo
            ),
            250, // puntos
            "Full Stack Master" // badge
        );
        System.out.println(moduloFlexible.mostrarContenido());
        System.out.println("\n");

        System.out.println("========================================");
        System.out.println("VENTAJAS DEL PATRÓN DECORATOR:");
        System.out.println("========================================");
        System.out.println("✓ No modifica la clase ModuloBasico original");
        System.out.println("✓ Añade funcionalidades dinámicamente");
        System.out.println("✓ Se pueden combinar múltiples decoradores");
        System.out.println("✓ Flexibilidad en el orden de aplicación");
        System.out.println("✓ Cumple el principio Open/Closed (SOLID)");
    }
}
