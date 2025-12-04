package com.edulearn.patterns.estructural.decorator.service;

import com.edulearn.model.Modulo;
import com.edulearn.patterns.estructural.decorator.*;
import com.edulearn.patterns.estructural.decorator.dto.DecoradorRequest;
import com.edulearn.patterns.estructural.decorator.dto.DecoradorResponse;
import com.edulearn.repository.ModuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio que implementa el patrón Decorator para módulos educativos
 * Permite extender funcionalidades sin modificar la clase base
 *
 * REGLA DE NEGOCIO: Solo el último módulo del curso puede tener certificación
 */
@Service
public class DecoradorService {

    @Autowired
    private ModuloRepository moduloRepository;

    @Autowired
    private com.edulearn.repository.InscripcionRepository inscripcionRepository;

    /**
     * Verifica si un módulo es el último del curso (puede tener certificación)
     *
     * @param moduloId ID del módulo a verificar
     * @return true si es el último módulo del curso, false en caso contrario
     */
    public boolean esUltimoModuloDelCurso(Long moduloId) {
        System.out.println("=== VERIFICANDO SI ES ÚLTIMO MÓDULO ===");
        System.out.println("moduloId a verificar: " + moduloId);

        Modulo modulo = moduloRepository.findById(moduloId)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado con ID: " + moduloId));

        System.out.println("Módulo encontrado: " + modulo.getNombre());
        System.out.println("Curso ID: " + modulo.getCursoId());
        System.out.println("Orden del módulo: " + modulo.getOrden());

        // Obtener todos los módulos del mismo curso sin padre (módulos raíz)
        List<Modulo> modulosDelCurso = moduloRepository.findByCursoIdAndModuloPadreIdIsNull(modulo.getCursoId());

        System.out.println("Módulos del curso (raíz): " + modulosDelCurso.size());
        for (Modulo m : modulosDelCurso) {
            System.out.println("  - ID: " + m.getId() + ", Nombre: " + m.getNombre() + ", Orden: " + m.getOrden());
        }

        if (modulosDelCurso.isEmpty()) {
            System.out.println("No hay módulos raíz en el curso");
            return false;
        }

        // El último módulo es el que tiene el orden más alto
        Modulo ultimoModulo = modulosDelCurso.stream()
                .max((m1, m2) -> Integer.compare(m1.getOrden(), m2.getOrden()))
                .orElse(null);

        if (ultimoModulo != null) {
            System.out.println("Último módulo: ID=" + ultimoModulo.getId() + ", Nombre=" + ultimoModulo.getNombre() + ", Orden=" + ultimoModulo.getOrden());
            System.out.println("¿Es el último?: " + ultimoModulo.getId().equals(moduloId));
        }

        return ultimoModulo != null && ultimoModulo.getId().equals(moduloId);
    }

    /**
     * Aplica decoradores (gamificación y/o certificación) a un módulo
     *
     * VALIDACIÓN: Solo el último módulo del curso puede tener certificación
     *
     * @param moduloId ID del módulo a decorar
     * @param request Datos de los decoradores a aplicar
     * @return Respuesta con el contenido decorado
     * @throws IllegalArgumentException si se intenta agregar certificación a un módulo que no es el último
     */
    @Transactional
    public DecoradorResponse aplicarDecoradores(Long moduloId, DecoradorRequest request) {
        System.out.println("=== DECORATOR SERVICE ===");
        System.out.println("moduloId: " + moduloId);
        System.out.println("Buscando en repositorio...");

        // Buscar el módulo en la base de datos
        Modulo modulo = moduloRepository.findById(moduloId)
                .orElseThrow(() -> {
                    System.out.println("ERROR: Módulo no encontrado con ID: " + moduloId);
                    return new RuntimeException("Módulo no encontrado con ID: " + moduloId);
                });

        System.out.println("Módulo encontrado: " + modulo.getNombre());

        // VALIDACIÓN: Solo el último módulo puede tener certificación
        if (Boolean.TRUE.equals(request.getCertificacionHabilitada())) {
            if (!esUltimoModuloDelCurso(moduloId)) {
                throw new IllegalArgumentException(
                        "La certificación solo puede ser habilitada en el último módulo del curso. " +
                        "Este módulo no es el último del curso."
                );
            }
        }

        // Actualizar los campos de decoradores en el módulo
        modulo.setGamificacionHabilitada(request.getGamificacionHabilitada());
        modulo.setGamificacionPuntos(request.getGamificacionPuntos());
        modulo.setGamificacionBadge(request.getGamificacionBadge());
        modulo.setCertificacionHabilitada(request.getCertificacionHabilitada());
        modulo.setCertificacionTipo(request.getCertificacionTipo());
        modulo.setCertificacionActiva(request.getCertificacionActiva());

        // Guardar cambios
        moduloRepository.save(modulo);

        // Crear el módulo base usando el patrón Decorator
        ModuloEducativo moduloEducativo = new ModuloBasico(
                modulo.getNombre(),
                modulo.getDescripcion() != null ? modulo.getDescripcion() : "Sin descripción"
        );

        // Aplicar decoradores según la configuración
        if (Boolean.TRUE.equals(request.getGamificacionHabilitada())) {
            moduloEducativo = new GamificacionDecorator(
                    moduloEducativo,
                    request.getGamificacionPuntos() != null ? request.getGamificacionPuntos() : 0,
                    request.getGamificacionBadge() != null ? request.getGamificacionBadge() : "Badge por defecto"
            );
        }

        if (Boolean.TRUE.equals(request.getCertificacionHabilitada())) {
            moduloEducativo = new CertificacionDecorator(
                    moduloEducativo,
                    request.getCertificacionTipo() != null ? request.getCertificacionTipo() : "Certificado Básico",
                    request.getCertificacionActiva() != null ? request.getCertificacionActiva() : false
            );
        }

        // Generar la respuesta
        DecoradorResponse response = new DecoradorResponse();
        response.setModuloId(moduloId);
        response.setNombre(modulo.getNombre());
        response.setContenidoBase(modulo.getDescripcion());
        response.setContenidoDecorado(moduloEducativo.mostrarContenido());
        response.setGamificacionHabilitada(modulo.getGamificacionHabilitada());
        response.setGamificacionPuntos(modulo.getGamificacionPuntos());
        response.setGamificacionBadge(modulo.getGamificacionBadge());
        response.setCertificacionHabilitada(modulo.getCertificacionHabilitada());
        response.setCertificacionTipo(modulo.getCertificacionTipo());
        response.setCertificacionActiva(modulo.getCertificacionActiva());

        return response;
    }

    /**
     * Obtiene el contenido decorado de un módulo
     *
     * @param moduloId ID del módulo
     * @return Respuesta con el contenido decorado
     */
    public DecoradorResponse obtenerModuloDecorado(Long moduloId) {
        Modulo modulo = moduloRepository.findById(moduloId)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado con ID: " + moduloId));

        // Crear el módulo base
        ModuloEducativo moduloEducativo = new ModuloBasico(
                modulo.getNombre(),
                modulo.getDescripcion() != null ? modulo.getDescripcion() : "Sin descripción"
        );

        // Aplicar decoradores si están habilitados
        if (Boolean.TRUE.equals(modulo.getGamificacionHabilitada())) {
            moduloEducativo = new GamificacionDecorator(
                    moduloEducativo,
                    modulo.getGamificacionPuntos() != null ? modulo.getGamificacionPuntos() : 0,
                    modulo.getGamificacionBadge() != null ? modulo.getGamificacionBadge() : "Badge por defecto"
            );
        }

        if (Boolean.TRUE.equals(modulo.getCertificacionHabilitada())) {
            moduloEducativo = new CertificacionDecorator(
                    moduloEducativo,
                    modulo.getCertificacionTipo() != null ? modulo.getCertificacionTipo() : "Certificado Básico",
                    modulo.getCertificacionActiva() != null ? modulo.getCertificacionActiva() : false
            );
        }

        // Generar la respuesta
        DecoradorResponse response = new DecoradorResponse();
        response.setModuloId(moduloId);
        response.setNombre(modulo.getNombre());
        response.setContenidoBase(modulo.getDescripcion());
        response.setContenidoDecorado(moduloEducativo.mostrarContenido());
        response.setGamificacionHabilitada(modulo.getGamificacionHabilitada());
        response.setGamificacionPuntos(modulo.getGamificacionPuntos());
        response.setGamificacionBadge(modulo.getGamificacionBadge());
        response.setCertificacionHabilitada(modulo.getCertificacionHabilitada());
        response.setCertificacionTipo(modulo.getCertificacionTipo());
        response.setCertificacionActiva(modulo.getCertificacionActiva());

        return response;
    }

    /**
     * Elimina todos los decoradores de un módulo
     *
     * @param moduloId ID del módulo
     */
    @Transactional
    public void eliminarDecoradores(Long moduloId) {
        Modulo modulo = moduloRepository.findById(moduloId)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado con ID: " + moduloId));

        // Resetear todos los campos de decoradores
        modulo.setGamificacionHabilitada(false);
        modulo.setGamificacionPuntos(null);
        modulo.setGamificacionBadge(null);
        modulo.setCertificacionHabilitada(false);
        modulo.setCertificacionTipo(null);
        modulo.setCertificacionActiva(false);

        moduloRepository.save(modulo);
    }

    /**
     * Obtiene la información de decoradores para un estudiante en un curso
     * Incluye gamificación, certificación y validación según tipo de inscripción
     *
     * @param cursoId ID del curso
     * @param estudianteId ID del estudiante
     * @return Información de decoradores para el estudiante
     */
    public com.edulearn.patterns.estructural.decorator.dto.DecoradorEstudianteResponse obtenerDecoradoresParaEstudiante(
            Integer cursoId, Integer estudianteId) {

        System.out.println("=== OBTENIENDO DECORADORES PARA ESTUDIANTE ===");
        System.out.println("Curso ID: " + cursoId);
        System.out.println("Estudiante ID: " + estudianteId);

        com.edulearn.patterns.estructural.decorator.dto.DecoradorEstudianteResponse response =
                new com.edulearn.patterns.estructural.decorator.dto.DecoradorEstudianteResponse();

        response.setCursoId(cursoId.longValue());

        // Obtener todos los módulos raíz del curso
        List<Modulo> modulos = moduloRepository.findByCursoIdAndModuloPadreIdIsNull(cursoId);
        System.out.println("Módulos del curso: " + modulos.size());

        response.setTotalModulos(modulos.size());

        // Calcular puntos totales y badges disponibles
        int puntosTotal = 0;
        for (Modulo modulo : modulos) {
            if (Boolean.TRUE.equals(modulo.getGamificacionHabilitada()) && modulo.getGamificacionPuntos() != null) {
                puntosTotal += modulo.getGamificacionPuntos();

                if (modulo.getGamificacionBadge() != null && !modulo.getGamificacionBadge().isEmpty()) {
                    response.getBadgesDisponibles().add(
                            new com.edulearn.patterns.estructural.decorator.dto.DecoradorEstudianteResponse.BadgeDisponible(
                                    modulo.getGamificacionBadge(),
                                    modulo.getNombre(),
                                    false // TODO: Implementar lógica de progreso real
                            )
                    );
                }
            }

            // Verificar si el último módulo tiene certificación
            if (esUltimoModuloDelCurso(modulo.getId()) &&
                    Boolean.TRUE.equals(modulo.getCertificacionHabilitada())) {
                response.setCertificadoDisponible(true);
                response.setTipoCertificado(modulo.getCertificacionTipo());
            }
        }

        response.setPuntosDisponibles(puntosTotal);
        response.setPuntosObtenidos(0); // TODO: Implementar lógica de progreso real

        // Verificar tipo de inscripción
        com.edulearn.model.Inscripcion inscripcion = inscripcionRepository
                .findByEstudianteIdAndCursoId(estudianteId, cursoId)
                .orElse(null);

        if (inscripcion != null) {
            response.setModalidadInscripcion(inscripcion.getModalidad());

            // Determinar si puede descargar el certificado
            boolean puedeDescargar = false;
            String mensaje = "";

            if (!response.isCertificadoDisponible()) {
                mensaje = "Este curso no otorga certificado";
                puedeDescargar = false;
            } else if ("GRATUITA".equals(inscripcion.getModalidad())) {
                mensaje = "El certificado solo está disponible para inscripciones de pago. " +
                        "Actualiza tu inscripción para obtener el certificado.";
                puedeDescargar = false;
            } else if ("PAGA".equals(inscripcion.getModalidad()) ||
                    "BECA".equals(inscripcion.getModalidad())) {
                mensaje = "Completa el curso para obtener tu certificado";
                puedeDescargar = true;
            } else {
                mensaje = "Estado de inscripción desconocido";
                puedeDescargar = false;
            }

            response.setPuedeDescargarCertificado(puedeDescargar);
            response.setMensajeCertificado(mensaje);

            System.out.println("Modalidad: " + inscripcion.getModalidad());
            System.out.println("Puede descargar certificado: " + puedeDescargar);
        } else {
            response.setModalidadInscripcion("NO_INSCRITO");
            response.setPuedeDescargarCertificado(false);
            response.setMensajeCertificado("No estás inscrito en este curso");
            System.out.println("Estudiante no inscrito en el curso");
        }

        response.setModulosCompletados(0); // TODO: Implementar lógica de progreso real
        response.setCursoCompletado(false);

        return response;
    }
}
