package com.edulearn.patterns.comportamiento.template_method;

import com.edulearn.model.Curso;
import com.edulearn.model.Estudiante;
import com.edulearn.patterns.comportamiento.template_method.dto.ResultadoInscripcion;
import com.edulearn.patterns.comportamiento.template_method.dto.ResultadoPaso;
import com.edulearn.patterns.comportamiento.template_method.dto.SolicitudInscripcion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Template Method Pattern - Plantilla abstracta para el proceso de inscripción
 * 
 * Este patrón define el esqueleto del algoritmo de inscripción en un método,
 * delegando algunos pasos a las subclases. Permite que las subclases redefinan
 * ciertos pasos de un algoritmo sin cambiar la estructura del mismo.
 * 
 * Patrón: Template Method (Comportamiento)
 * Propósito: Definir la estructura del proceso de inscripción permitiendo
 *            que los pasos específicos varíen según el tipo de inscripción
 */
public abstract class ProcesoInscripcionTemplate {
    
    protected List<ResultadoPaso> pasos = new ArrayList<>();
    protected LocalDateTime fechaInicio;
    protected LocalDateTime fechaFin;
    
    /**
     * TEMPLATE METHOD - Define el esqueleto del algoritmo de inscripción
     * Este método es final para evitar que las subclases cambien la estructura
     *
     * Pasos Fijos (obligatorios para todas las modalidades):
     * - aceptarTerminosYCondiciones(): Requisito obligatorio inicial
     * - registrarInscripcionEnBD(): Persistencia final de la inscripción
     *
     * Pasos Variables (implementados por cada modalidad):
     * - realizarValidacionEspecifica(): Lógica única de cada modalidad
     * - otorgarBeneficiosAdicionales(): Beneficios específicos por modalidad
     */
    public final ResultadoInscripcion procesarInscripcion(
            Estudiante estudiante,
            Curso curso,
            SolicitudInscripcion solicitud) {

        fechaInicio = LocalDateTime.now();
        pasos.clear();

        ResultadoInscripcion resultado = new ResultadoInscripcion();
        resultado.setEstudianteId(estudiante.getId());
        resultado.setCursoId(curso.getId());
        resultado.setTipoInscripcion(getTipoInscripcion());
        resultado.setFechaInicio(fechaInicio);

        try {
            // PASO FIJO 1: Aceptar términos y condiciones (obligatorio para todos)
            ResultadoPaso paso1 = aceptarTerminosYCondiciones(solicitud);
            pasos.add(paso1);
            if (!paso1.isExitoso()) {
                return finalizarConError(resultado, "Términos y condiciones no aceptados: " + paso1.getMensaje());
            }

            // Paso 2: Validar requisitos previos (común a todos)
            ResultadoPaso paso2 = validarRequisitosPrevios(estudiante, curso);
            pasos.add(paso2);
            if (!paso2.isExitoso()) {
                return finalizarConError(resultado, "Requisitos previos no cumplidos: " + paso2.getMensaje());
            }

            // Paso 3: Verificar disponibilidad (común a todos)
            ResultadoPaso paso3 = verificarDisponibilidad(curso);
            pasos.add(paso3);
            if (!paso3.isExitoso()) {
                return finalizarConError(resultado, "Curso no disponible: " + paso3.getMensaje());
            }

            // PASO VARIABLE 1: Realizar validación específica de la modalidad
            ResultadoPaso paso4 = realizarValidacionEspecifica(estudiante, curso, solicitud);
            pasos.add(paso4);
            if (!paso4.isExitoso()) {
                return finalizarConError(resultado, "Validación específica fallida: " + paso4.getMensaje());
            }

            // PASO VARIABLE 2: Otorgar beneficios adicionales según modalidad (hook)
            ResultadoPaso paso5 = otorgarBeneficiosAdicionales(estudiante, curso, solicitud);
            pasos.add(paso5);
            // Los beneficios adicionales no bloquean el proceso

            // PASO FIJO 2: Registrar inscripción en la base de datos (obligatorio para todos)
            ResultadoPaso paso6 = registrarInscripcionEnBD(estudiante, curso, solicitud, resultado);
            pasos.add(paso6);
            if (!paso6.isExitoso()) {
                return finalizarConError(resultado, "Error al registrar en BD: " + paso6.getMensaje());
            }

            // Paso 7: Enviar notificaciones (común a todos)
            ResultadoPaso paso7 = enviarNotificaciones(estudiante, curso);
            pasos.add(paso7);
            // Las notificaciones no bloquean el proceso

            // Paso 8: Generar documentos de confirmación (común con detalles variables)
            ResultadoPaso paso8 = generarDocumentos(estudiante, curso, solicitud);
            pasos.add(paso8);

            // Finalizar exitosamente
            return finalizarExitoso(resultado);

        } catch (Exception e) {
            return finalizarConError(resultado, "Error inesperado: " + e.getMessage());
        }
    }
    
    // ==================== PASOS FIJOS (métodos concretos - obligatorios) ====================

    /**
     * PASO FIJO 1: Aceptar términos y condiciones
     * Requisito obligatorio para todas las inscripciones
     */
    protected final ResultadoPaso aceptarTerminosYCondiciones(SolicitudInscripcion solicitud) {
        ResultadoPaso paso = new ResultadoPaso("Aceptación de términos y condiciones");

        if (!solicitud.isAceptaTerminos()) {
            paso.setExitoso(false);
            paso.setMensaje("Debe aceptar los términos y condiciones para continuar con la inscripción");
            return paso;
        }

        paso.setExitoso(true);
        paso.setMensaje("Términos y condiciones aceptados correctamente");
        paso.agregarDetalle("terminosAceptados", "true");
        paso.agregarDetalle("fechaAceptacion", LocalDateTime.now().toString());

        return paso;
    }

    /**
     * PASO FIJO 2: Registrar inscripción en la base de datos
     * Paso final que persiste la inscripción y su estado en la base de datos
     * Este método será implementado por el servicio que tiene acceso al repositorio
     */
    protected ResultadoPaso registrarInscripcionEnBD(
            Estudiante estudiante,
            Curso curso,
            SolicitudInscripcion solicitud,
            ResultadoInscripcion resultado) {
        ResultadoPaso paso = new ResultadoPaso("Registro en base de datos");

        // La persistencia real se maneja en el servicio
        // Aquí solo preparamos los datos que se deben guardar
        paso.setExitoso(true);
        paso.setMensaje("Datos preparados para persistencia en BD");
        paso.agregarDetalle("estudianteId", String.valueOf(estudiante.getId()));
        paso.agregarDetalle("cursoId", String.valueOf(curso.getId()));
        paso.agregarDetalle("modalidad", getTipoInscripcion());
        paso.agregarDetalle("estadoInscripcion", getEstadoInscripcion());
        paso.agregarDetalle("certificadoGarantizado", String.valueOf(tieneCertificadoGarantizado()));
        paso.agregarDetalle("fechaRegistro", LocalDateTime.now().toString());

        return paso;
    }

    // ==================== MÉTODOS COMUNES (no se sobrescriben) ====================

    /**
     * Valida requisitos previos del estudiante para inscribirse
     */
    protected ResultadoPaso validarRequisitosPrevios(Estudiante estudiante, Curso curso) {
        ResultadoPaso paso = new ResultadoPaso("Validación de requisitos previos");
        
        if (estudiante == null) {
            paso.setExitoso(false);
            paso.setMensaje("Estudiante no encontrado");
            return paso;
        }
        
        if (curso == null) {
            paso.setExitoso(false);
            paso.setMensaje("Curso no encontrado");
            return paso;
        }
        
        if (!"activo".equalsIgnoreCase(curso.getEstado()) && 
            !"en_creacion".equalsIgnoreCase(curso.getEstado())) {
            paso.setExitoso(false);
            paso.setMensaje("El curso no está disponible para inscripciones");
            return paso;
        }
        
        paso.setExitoso(true);
        paso.setMensaje("Requisitos previos validados correctamente");
        return paso;
    }
    
    /**
     * Verifica disponibilidad de cupo en el curso
     */
    protected ResultadoPaso verificarDisponibilidad(Curso curso) {
        ResultadoPaso paso = new ResultadoPaso("Verificación de disponibilidad");
        
        // Por ahora simulamos que siempre hay cupo
        // En producción verificaríamos cupo_actual < cupo_maximo
        paso.setExitoso(true);
        paso.setMensaje("Cupo disponible en el curso");
        paso.agregarDetalle("cupoDisponible", "true");
        
        return paso;
    }
    
    /**
     * Registra la inscripción en el sistema
     */
    protected ResultadoPaso registrarInscripcion(Estudiante estudiante, Curso curso) {
        ResultadoPaso paso = new ResultadoPaso("Registro de inscripción");
        
        // Este paso será implementado por el servicio que persiste los datos
        paso.setExitoso(true);
        paso.setMensaje("Inscripción registrada exitosamente");
        paso.agregarDetalle("estudianteId", String.valueOf(estudiante.getId()));
        paso.agregarDetalle("cursoId", String.valueOf(curso.getId()));
        paso.agregarDetalle("fechaRegistro", LocalDateTime.now().toString());
        
        return paso;
    }
    
    /**
     * Envía notificaciones al estudiante
     */
    protected ResultadoPaso enviarNotificaciones(Estudiante estudiante, Curso curso) {
        ResultadoPaso paso = new ResultadoPaso("Envío de notificaciones");
        
        paso.setExitoso(true);
        paso.setMensaje("Notificaciones enviadas al estudiante");
        paso.agregarDetalle("email", estudiante.getEmail() != null ? estudiante.getEmail() : "No disponible");
        paso.agregarDetalle("tipoNotificacion", "EMAIL");
        
        return paso;
    }
    
    // ==================== MÉTODOS ABSTRACTOS (pasos variables - deben implementarse) ====================

    /**
     * PASO VARIABLE 1: Realizar validación específica según la modalidad
     *
     * - InscripcionGratuita: Ninguna validación adicional (solo términos aceptados)
     * - InscripcionConPago: Procesar el pago y confirmarlo
     * - InscripcionPorBeca: Validar elegibilidad TECNM y establecer estado pendiente
     */
    protected abstract ResultadoPaso realizarValidacionEspecifica(
            Estudiante estudiante, Curso curso, SolicitudInscripcion solicitud);

    /**
     * PASO VARIABLE 2: Otorgar beneficios adicionales según la modalidad
     *
     * - InscripcionGratuita: Ninguno
     * - InscripcionConPago: Certificado Garantizado
     * - InscripcionPorBeca: Certificado Garantizado
     */
    protected abstract ResultadoPaso otorgarBeneficiosAdicionales(
            Estudiante estudiante, Curso curso, SolicitudInscripcion solicitud);

    /**
     * Genera los documentos de confirmación específicos
     */
    protected abstract ResultadoPaso generarDocumentos(
            Estudiante estudiante, Curso curso, SolicitudInscripcion solicitud);

    /**
     * Obtiene el tipo de inscripción (modalidad)
     */
    protected abstract String getTipoInscripcion();

    /**
     * Obtiene el estado de la inscripción según la modalidad
     */
    protected abstract String getEstadoInscripcion();

    /**
     * Indica si la modalidad otorga certificado garantizado
     */
    protected abstract boolean tieneCertificadoGarantizado();

    /**
     * Obtiene la descripción del proceso
     */
    public abstract String getDescripcion();

    /**
     * Obtiene los pasos específicos de este tipo de inscripción
     */
    public abstract List<String> getPasosEspecificos();
    
    
    // ==================== MÉTODOS AUXILIARES ====================
    
    private ResultadoInscripcion finalizarExitoso(ResultadoInscripcion resultado) {
        fechaFin = LocalDateTime.now();
        resultado.setExitoso(true);
        resultado.setEstado("COMPLETADA");
        resultado.setMensaje("Inscripción procesada exitosamente");
        resultado.setPasos(new ArrayList<>(pasos));
        resultado.setFechaFin(fechaFin);
        resultado.calcularDuracion();
        return resultado;
    }
    
    private ResultadoInscripcion finalizarConError(ResultadoInscripcion resultado, String mensaje) {
        fechaFin = LocalDateTime.now();
        resultado.setExitoso(false);
        resultado.setEstado("FALLIDA");
        resultado.setMensaje(mensaje);
        resultado.setPasos(new ArrayList<>(pasos));
        resultado.setFechaFin(fechaFin);
        resultado.calcularDuracion();
        return resultado;
    }
    
    /**
     * Obtiene información del patrón para demostración
     */
    public static String getPatronInfo() {
        return "Template Method Pattern - Define el esqueleto de un algoritmo en un método, " +
               "delegando algunos pasos a las subclases sin cambiar la estructura del algoritmo.";
    }
}
