package com.edulearn.service;

import com.edulearn.model.*;
import com.edulearn.patterns.comportamiento.observer.NotificationEvent;
import com.edulearn.patterns.comportamiento.observer.NotificationOrchestrator;
import com.edulearn.patterns.creational.abstractfactory.*;
import com.edulearn.patterns.creational.builder.CursoCompleteBuilder;
import com.edulearn.patterns.creational.builder.CursoCompleteBuilder.CursoCompletoDTO;
import com.edulearn.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SERVICIO DE ORQUESTACIÓN PARA CREACIÓN DE CURSOS COMPLETOS
 * ===========================================================
 * 
 * Este servicio orquesta la creación completa de cursos usando:
 * - Abstract Factory: para crear familias de componentes (Virtual, Presencial, Híbrido)
 * - Builder: para construir el curso paso a paso
 * 
 * Responsabilidades:
 * 1. Seleccionar la factory apropiada según el tipo de curso
 * 2. Orquestar el builder con la factory
 * 3. Persistir todos los componentes en la base de datos (transaccional)
 * 4. Garantizar consistencia de datos (rollback en caso de error)
 */
@Service
public class CursoCreacionService {

    private static final Logger logger = LoggerFactory.getLogger(CursoCreacionService.class);

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
private NotificationOrchestrator notificationOrchestrator;

    /**
     * Crea un curso completo usando los patrones Abstract Factory y Builder
     * 
     * @param request DTO con la información del curso a crear
     * @return Map con el resultado de la operación
     */
    @Transactional
    public Map<String, Object> crearCursoCompleto(CursoCreacionRequest request) {
        logger.info("🎓 Iniciando creación de curso completo: {}", request.nombre);
        
        Map<String, Object> resultado = new HashMap<>();

        try {
            // PASO 1: Seleccionar la Factory según el tipo de curso
            CourseComponentFactory factory = seleccionarFactory(request.tipoCurso);
            logger.info("✓ Factory seleccionada: {}", factory.getTipoFactory());

            // PASO 2: Crear el Builder con la Factory
            CursoCompleteBuilder builder = new CursoCompleteBuilder(factory);
            
            // PASO 3: Configurar el curso base
            builder.setNombre(request.nombre)
                   .setDescripcion(request.descripcion)
                   .setProfesorTitularId(request.profesorId)
                   .setPeriodoAcademico(request.periodoAcademico)
                   .setDuracion(request.duracion)
                   .setEstrategiaEvaluacion(request.estrategiaEvaluacion)
                   .setCupoMaximo(request.cupoMaximo);

            if (request.codigo != null && !request.codigo.isEmpty()) {
                builder.setCodigo(request.codigo);
            }

            // PASO 4: Agregar módulos con su contenido
            if (request.modulos != null && !request.modulos.isEmpty()) {
                for (ModuloRequest moduloReq : request.modulos) {
                    builder.agregarModulo(moduloReq.titulo, moduloReq.descripcion);

                    // Agregar materiales del módulo
                    if (moduloReq.materiales != null) {
                        for (MaterialRequest materialReq : moduloReq.materiales) {
                            builder.agregarMaterial(materialReq.nombre, materialReq.descripcion);
                        }
                    }

                    // Agregar evaluaciones del módulo
                    if (moduloReq.evaluaciones != null) {
                        for (EvaluacionRequest evalReq : moduloReq.evaluaciones) {
                            builder.agregarEvaluacion(evalReq.nombre, evalReq.descripcion);
                        }
                    }
                }
            }

            // PASO 5: Construir el curso completo
            CursoCompletoDTO cursoCompleto = builder.build();
            logger.info("✓ Curso construido con {} módulos", cursoCompleto.modulos.size());

            // PASO 6: Persistir en la base de datos (transaccional)
            
            // 6.1 Guardar el curso
            Curso cursoGuardado = cursoRepository.save(cursoCompleto.curso);
            logger.info("✓ Curso guardado con ID: {}", cursoGuardado.getId());

            // Registrar profesor asignado al curso en el sistema de notificaciones
            try {
                notificationOrchestrator.registerCourseTeacher(cursoGuardado.getId(), request.profesorId);
                logger.info("✓ Profesor {} registrado para curso {} en notificaciones", request.profesorId, cursoGuardado.getId());
            } catch (Exception e) {
                logger.warn("⚠️ No se pudo registrar el profesor {} para el curso {} en notificaciones: {}", request.profesorId, cursoGuardado.getId(), e.getMessage());
            }

            int totalMateriales = 0;
            int totalEvaluaciones = 0;

            // 6.2 Guardar módulos con su contenido
            for (Modulo modulo : cursoCompleto.modulos) {
                modulo.setCursoId(cursoGuardado.getId());
                // Asegurar que nombre tenga el mismo valor que titulo
                if (modulo.getNombre() == null || modulo.getNombre().isEmpty()) {
                    modulo.setNombre(modulo.getTitulo());
                }
                Modulo moduloGuardado = moduloRepository.save(modulo);
                logger.info("✓ Módulo guardado: {} (ID: {})", moduloGuardado.getTitulo(), moduloGuardado.getId());

                // 6.3 Guardar materiales del módulo
                List<Material> materiales = cursoCompleto.materialesPorModulo.get(modulo);
                if (materiales != null) {
                    for (Material material : materiales) {
                        material.setModuloId(moduloGuardado.getId());
                        material.setCursoId(cursoGuardado.getId());
                        materialRepository.save(material);
                        totalMateriales++;
                    }
                }

                // 6.4 Guardar evaluaciones del módulo
                List<Evaluacion> evaluaciones = cursoCompleto.evaluacionesPorModulo.get(modulo);
                if (evaluaciones != null) {
                    for (Evaluacion evaluacion : evaluaciones) {
                        evaluacion.setModuloId(moduloGuardado.getId());
                        evaluacionRepository.save(evaluacion);
                        totalEvaluaciones++;
                    }
                }
            }

            logger.info("✓ Persistencia completa: {} materiales, {} evaluaciones", totalMateriales, totalEvaluaciones);

            // PASO 7: Preparar respuesta
            resultado.put("exito", true);
            resultado.put("mensaje", "Curso creado exitosamente");
            resultado.put("cursoId", cursoGuardado.getId());
            resultado.put("codigo", cursoGuardado.getCodigo());
            resultado.put("nombre", cursoGuardado.getNombre());
            resultado.put("tipoCurso", cursoGuardado.getTipoCurso());
            resultado.put("totalModulos", cursoCompleto.modulos.size());
            resultado.put("totalMateriales", totalMateriales);
            resultado.put("totalEvaluaciones", totalEvaluaciones);
            resultado.put("factoryUsada", factory.getTipoFactory());

            logger.info("✅ Curso creado exitosamente: {} (ID: {})", cursoGuardado.getNombre(), cursoGuardado.getId());

                        // PASO 8: Notificar creación del curso usando patrón Observer
            NotificationEvent event = new NotificationEvent.Builder()
                .eventType(NotificationEvent.EventType.CURSO_CREADO)
                .title("Nuevo curso creado")
                .message("El curso '" + cursoGuardado.getNombre() + "' ha sido creado exitosamente")
                .sourceUserId(request.profesorId)
                .targetId(cursoGuardado.getId())
                .targetType("CURSO")
                .addMetadata("tipoCurso", cursoGuardado.getTipoCurso())
                .addMetadata("totalModulos", cursoCompleto.modulos.size())
                .build();

            // Caso de negocio: Profesor crea Curso -> Notificar a todos los Administradores
            notificationOrchestrator.notifyRoleObservers("admin", event);
            logger.info("✓ Notificación de curso creado enviada a administradores");
        } catch (Exception e) {
            logger.error("❌ Error al crear curso: {}", e.getMessage(), e);
            logger.error("❌ Stack trace completo:", e);
            resultado.put("exito", false);
            resultado.put("mensaje", "Error al crear curso: " + e.getMessage());
            resultado.put("errorDetallado", e.getClass().getName());
            resultado.put("stackTrace", e.toString());
            throw new RuntimeException("Error en la creación del curso: " + e.getMessage(), e);
        }

        return resultado;
    }

    /**
     * Selecciona la factory apropiada según el tipo de curso
     */
    private CourseComponentFactory seleccionarFactory(String tipoCurso) {
        if (tipoCurso == null) {
            tipoCurso = "virtual";
        }

        switch (tipoCurso.toLowerCase()) {
            case "virtual":
                return new VirtualCourseFactory();
            case "presencial":
                return new PresencialCourseFactory();
            case "hibrido":
            case "híbrido":
                return new HibridoCourseFactory();
            default:
                logger.warn("⚠️ Tipo de curso desconocido '{}', usando Virtual por defecto", tipoCurso);
                return new VirtualCourseFactory();
        }
    }

    /**
     * DTOs para las solicitudes de creación
     */
    public static class CursoCreacionRequest {
        public String codigo;
        public String nombre;
        public String descripcion;
        public String tipoCurso;
        public Integer profesorId;
        public String periodoAcademico;
        public Integer duracion;
        public String estrategiaEvaluacion;
        public Integer cupoMaximo;
        public List<ModuloRequest> modulos;
    }

    public static class ModuloRequest {
        public String titulo;
        public String descripcion;
        public List<MaterialRequest> materiales;
        public List<EvaluacionRequest> evaluaciones;
    }

    public static class MaterialRequest {
        public String nombre;
        public String descripcion;
    }

    public static class EvaluacionRequest {
        public String nombre;
        public String descripcion;
    }
}
