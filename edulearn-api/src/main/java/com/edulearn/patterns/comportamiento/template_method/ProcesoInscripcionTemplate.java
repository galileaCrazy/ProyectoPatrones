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
            // Paso 1: Validar requisitos previos (común a todos)
            ResultadoPaso paso1 = validarRequisitosPrevios(estudiante, curso);
            pasos.add(paso1);
            if (!paso1.isExitoso()) {
                return finalizarConError(resultado, "Requisitos previos no cumplidos: " + paso1.getMensaje());
            }
            
            // Paso 2: Verificar disponibilidad (común a todos)
            ResultadoPaso paso2 = verificarDisponibilidad(curso);
            pasos.add(paso2);
            if (!paso2.isExitoso()) {
                return finalizarConError(resultado, "Curso no disponible: " + paso2.getMensaje());
            }
            
            // Paso 3: Validar documentación específica (varía según tipo)
            ResultadoPaso paso3 = validarDocumentacion(estudiante, solicitud);
            pasos.add(paso3);
            if (!paso3.isExitoso()) {
                return finalizarConError(resultado, "Documentación inválida: " + paso3.getMensaje());
            }
            
            // Paso 4: Procesar pago o verificación económica (varía según tipo)
            ResultadoPaso paso4 = procesarAspectoEconomico(estudiante, curso, solicitud);
            pasos.add(paso4);
            if (!paso4.isExitoso()) {
                return finalizarConError(resultado, "Aspecto económico no resuelto: " + paso4.getMensaje());
            }
            
            // Paso 5: Aplicar beneficios o descuentos (hook - opcional según tipo)
            ResultadoPaso paso5 = aplicarBeneficios(estudiante, curso, solicitud);
            pasos.add(paso5);
            // Los beneficios son opcionales, continuamos aunque falle
            
            // Paso 6: Registrar inscripción (común a todos)
            ResultadoPaso paso6 = registrarInscripcion(estudiante, curso);
            pasos.add(paso6);
            if (!paso6.isExitoso()) {
                return finalizarConError(resultado, "Error al registrar: " + paso6.getMensaje());
            }
            
            // Paso 7: Enviar notificaciones (común a todos)
            ResultadoPaso paso7 = enviarNotificaciones(estudiante, curso);
            pasos.add(paso7);
            // Las notificaciones no bloquean el proceso
            
            // Paso 8: Generar documentos de confirmación (varía según tipo)
            ResultadoPaso paso8 = generarDocumentos(estudiante, curso, solicitud);
            pasos.add(paso8);
            
            // Finalizar exitosamente
            return finalizarExitoso(resultado);
            
        } catch (Exception e) {
            return finalizarConError(resultado, "Error inesperado: " + e.getMessage());
        }
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
    
    // ==================== MÉTODOS ABSTRACTOS (deben implementarse) ====================
    
    /**
     * Valida la documentación requerida según el tipo de inscripción
     */
    protected abstract ResultadoPaso validarDocumentacion(Estudiante estudiante, SolicitudInscripcion solicitud);
    
    /**
     * Procesa el aspecto económico (pago, verificación de beca, etc.)
     */
    protected abstract ResultadoPaso procesarAspectoEconomico(
            Estudiante estudiante, Curso curso, SolicitudInscripcion solicitud);
    
    /**
     * Genera los documentos de confirmación específicos
     */
    protected abstract ResultadoPaso generarDocumentos(
            Estudiante estudiante, Curso curso, SolicitudInscripcion solicitud);
    
    /**
     * Obtiene el tipo de inscripción
     */
    protected abstract String getTipoInscripcion();
    
    /**
     * Obtiene la descripción del proceso
     */
    public abstract String getDescripcion();
    
    /**
     * Obtiene los pasos específicos de este tipo de inscripción
     */
    public abstract List<String> getPasosEspecificos();
    
    // ==================== HOOK METHODS (pueden sobrescribirse) ====================
    
    /**
     * Hook para aplicar beneficios o descuentos
     * Por defecto no aplica ningún beneficio
     */
    protected ResultadoPaso aplicarBeneficios(
            Estudiante estudiante, Curso curso, SolicitudInscripcion solicitud) {
        ResultadoPaso paso = new ResultadoPaso("Aplicación de beneficios");
        paso.setExitoso(true);
        paso.setMensaje("No se aplicaron beneficios adicionales");
        return paso;
    }
    
    /**
     * Hook para validaciones adicionales específicas
     */
    protected boolean requiereValidacionAdicional() {
        return false;
    }
    
    /**
     * Hook para determinar si se genera certificado inmediato
     */
    protected boolean generaCertificadoInmediato() {
        return false;
    }
    
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
