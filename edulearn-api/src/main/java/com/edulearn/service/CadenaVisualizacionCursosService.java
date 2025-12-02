package com.edulearn.service;

import com.edulearn.patterns.comportamiento.chain_of_responsibility.Gestor;
import com.edulearn.patterns.comportamiento.chain_of_responsibility.GestorVisualizacionCursosAdmin;
import com.edulearn.patterns.comportamiento.chain_of_responsibility.GestorVisualizacionCursosProfesor;
import com.edulearn.patterns.comportamiento.chain_of_responsibility.GestorVisualizacionCursosEstudiante;
import com.edulearn.patterns.comportamiento.chain_of_responsibility.SolicitudValidacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Servicio que construye y gestiona la cadena de responsabilidad
 * para la validación de permisos de visualización de cursos.
 *
 * Patrón: Chain of Responsibility
 * Propósito: Validar que un usuario tiene permisos para visualizar cursos según su rol
 */
@Service
public class CadenaVisualizacionCursosService {

    private static final Logger logger = LoggerFactory.getLogger(CadenaVisualizacionCursosService.class);

    @Autowired
    private GestorVisualizacionCursosAdmin gestorAdmin;

    @Autowired
    private GestorVisualizacionCursosProfesor gestorProfesor;

    @Autowired
    private GestorVisualizacionCursosEstudiante gestorEstudiante;

    /**
     * Construye la cadena de validación para visualización de cursos.
     * Orden: Admin -> Profesor -> Estudiante
     *
     * @return El primer gestor de la cadena
     */
    private Gestor construirCadena() {
        // Construir la cadena: Admin -> Profesor -> Estudiante
        gestorAdmin.establecerSiguiente(gestorProfesor);
        gestorProfesor.establecerSiguiente(gestorEstudiante);

        logger.info("🔗 Cadena de visualización de cursos construida: Admin -> Profesor -> Estudiante");
        return gestorAdmin;
    }

    /**
     * Valida los permisos de visualización de cursos para un usuario.
     *
     * @param userId ID del usuario
     * @param tipoUsuario Tipo de usuario (administrador, profesor, estudiante)
     * @return SolicitudValidacion con el resultado de la validación
     */
    public SolicitudValidacion validarVisualizacion(Integer userId, String tipoUsuario) {
        logger.info("🚀 Iniciando validación de visualización de cursos para usuario {} con rol {}",
                    userId, tipoUsuario);

        // Crear la solicitud de validación
        SolicitudValidacion solicitud = new SolicitudValidacion(
            userId.toString(),
            "cursos",
            "VISUALIZAR_CURSOS"
        );
        solicitud.setTipoUsuario(tipoUsuario);
        solicitud.agregarMetadato("userId", userId);

        // Construir y ejecutar la cadena
        Gestor cadena = construirCadena();
        cadena.solicita(solicitud);

        // Registrar el resultado
        if (solicitud.isAprobada()) {
            logger.info("✅ Validación aprobada para usuario {} - Tipo filtro: {}",
                        userId, solicitud.getMetadatos().get("tipoFiltro"));
        } else {
            logger.error("❌ Validación rechazada para usuario {}: {}",
                         userId, solicitud.getMensajeError());
        }

        return solicitud;
    }
}
