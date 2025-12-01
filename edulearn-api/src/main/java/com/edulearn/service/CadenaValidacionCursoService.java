package com.edulearn.service;

import com.edulearn.patterns.comportamiento.chain_of_responsibility.Gestor;
import com.edulearn.patterns.comportamiento.chain_of_responsibility.GestorCreacionCursoRol;
import com.edulearn.patterns.comportamiento.chain_of_responsibility.GestorValidacionPeriodoCreacionCurso;
import com.edulearn.patterns.comportamiento.chain_of_responsibility.GestorValidacionCuposTipoCurso;
import com.edulearn.patterns.comportamiento.chain_of_responsibility.SolicitudValidacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service que configura y gestiona la cadena de validaciones para la creación de cursos
 * usando el patrón Chain of Responsibility
 */
@Service
public class CadenaValidacionCursoService {

    private static final Logger logger = LoggerFactory.getLogger(CadenaValidacionCursoService.class);

    @Autowired
    private GestorCreacionCursoRol gestorCreacionCursoRol;

    @Autowired
    private GestorValidacionPeriodoCreacionCurso gestorValidacionPeriodo;

    @Autowired
    private GestorValidacionCuposTipoCurso gestorValidacionCupos;

    /**
     * Configura la cadena de validaciones para la creación de cursos
     *
     * Orden de la cadena:
     * 1. GestorCreacionCursoRol - Valida permisos según el rol del usuario
     * 2. GestorValidacionCuposTipoCurso - Valida cupo máximo según tipo de curso
     * 3. GestorValidacionPeriodoCreacionCurso - Valida que el período académico sea válido
     *
     * @return El primer gestor de la cadena
     */
    public Gestor configurarCadenaCreacionCurso() {
        logger.info("🔗 Configurando cadena de validaciones para creación de curso");

        // Configurar la cadena: Rol → Cupos → Período
        gestorCreacionCursoRol.establecerSiguiente(gestorValidacionCupos);
        gestorValidacionCupos.establecerSiguiente(gestorValidacionPeriodo);

        logger.info("✅ Cadena configurada: [Rol] → [Cupos] → [Período Académico]");

        return gestorCreacionCursoRol;
    }

    /**
     * Ejecuta la cadena completa de validaciones para la configuración del formulario
     *
     * @param solicitud Solicitud de validación
     * @return true si todas las validaciones pasaron, false en caso contrario
     */
    public boolean validarConfiguracionFormulario(SolicitudValidacion solicitud) {
        logger.info("🎯 Iniciando validación de configuración de formulario");

        Gestor cadena = configurarCadenaCreacionCurso();
        cadena.solicita(solicitud);

        if (solicitud.isAprobada()) {
            logger.info("✅ Todas las validaciones pasaron exitosamente");
            return true;
        } else {
            logger.warn("⚠️ Validación fallida: {}", solicitud.getMensajeError());
            return false;
        }
    }

    /**
     * Ejecuta la cadena completa de validaciones para la creación de un curso
     *
     * @param solicitud Solicitud de validación con los datos del curso
     * @return true si todas las validaciones pasaron, false en caso contrario
     */
    public boolean validarCreacionCurso(SolicitudValidacion solicitud) {
        logger.info("🎯 Iniciando validación para creación de curso");

        Gestor cadena = configurarCadenaCreacionCurso();
        cadena.solicita(solicitud);

        if (solicitud.isAprobada()) {
            logger.info("✅ Curso validado exitosamente - Listo para ser creado");
            return true;
        } else {
            logger.error("❌ Validación fallida: {}", solicitud.getMensajeError());
            return false;
        }
    }
}
