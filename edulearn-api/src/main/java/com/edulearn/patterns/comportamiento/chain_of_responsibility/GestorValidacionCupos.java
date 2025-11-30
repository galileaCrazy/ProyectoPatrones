package com.edulearn.patterns.comportamiento.chain_of_responsibility;

import com.edulearn.repository.CursoRepository;
import com.edulearn.repository.InscripcionRepository;
import com.edulearn.model.Curso;
import com.edulearn.model.Inscripcion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;

/**
 * GestorConcreto5 - Validador de Cupos
 *
 * Quinta validación en la cadena: verifica que haya cupos disponibles
 * según el tipo de curso (presencial, virtual, híbrido).
 *
 * Límites de cupos por tipo de curso:
 * - Presencial: 35 estudiantes (limitado por capacidad del salón)
 * - Híbrido: 35 estudiantes (mismo límite que presencial)
 * - Virtual: 45 estudiantes (mayor capacidad al no tener límite físico)
 */
@Component
public class GestorValidacionCupos extends Gestor {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private InscripcionRepository inscripcionRepository;

    // Límites de cupos según tipo de curso
    private static final Map<String, Integer> CUPOS_POR_TIPO = new HashMap<>();

    static {
        CUPOS_POR_TIPO.put("presencial", 35);  // Limitado por salón físico
        CUPOS_POR_TIPO.put("hibrido", 35);     // Limitado por salón físico
        CUPOS_POR_TIPO.put("virtual", 45);     // Mayor capacidad - sin límite físico
    }

    @Override
    protected boolean manejar(SolicitudValidacion solicitud) {
        System.out.println("[Chain of Responsibility] GestorValidacionCupos procesando...");

        String recurso = solicitud.getRecursoSolicitado();
        String accion = solicitud.getAccion();

        // Solo validar cupos para acciones de inscripción
        if (accion == null || !accion.equalsIgnoreCase("inscribir")) {
            System.out.println("[Chain of Responsibility] Acción no requiere validación de cupos: " + accion);
            return true;
        }

        // Extraer ID del curso del recurso o de los metadatos
        Integer cursoId = extraerCursoId(solicitud);

        if (cursoId == null) {
            // Si no se puede determinar el curso, permitir (la validación se hará en otro nivel)
            System.out.println("[Chain of Responsibility] No se pudo determinar el curso, se omite validación de cupos");
            return true;
        }

        // Buscar el curso en la base de datos
        Optional<Curso> cursoOpt = cursoRepository.findById(cursoId);

        if (cursoOpt.isEmpty()) {
            solicitud.setMensajeError("Curso no encontrado con ID: " + cursoId);
            solicitud.setAprobada(false);
            return false;
        }

        Curso curso = cursoOpt.get();
        String tipoCurso = curso.getTipoCurso() != null ? curso.getTipoCurso().toLowerCase() : "virtual";

        // Obtener límite de cupos para este tipo de curso
        Integer cupoMaximo = CUPOS_POR_TIPO.getOrDefault(tipoCurso, 45);

        // Contar inscripciones actuales en el curso
        List<Inscripcion> inscripciones = inscripcionRepository.findByCursoId(cursoId);
        int inscritosActuales = inscripciones != null ? inscripciones.size() : 0;

        // Verificar si hay cupos disponibles
        if (inscritosActuales >= cupoMaximo) {
            solicitud.setMensajeError(
                String.format(
                    "No hay cupos disponibles. Curso '%s' (%s) tiene %d/%d estudiantes inscritos",
                    curso.getNombre(),
                    tipoCurso,
                    inscritosActuales,
                    cupoMaximo
                )
            );
            solicitud.setAprobada(false);
            return false;
        }

        // Agregar información de cupos a los metadatos
        solicitud.agregarMetadato("cursoId", cursoId);
        solicitud.agregarMetadato("cursoNombre", curso.getNombre());
        solicitud.agregarMetadato("tipoCurso", tipoCurso);
        solicitud.agregarMetadato("cupoMaximo", cupoMaximo);
        solicitud.agregarMetadato("inscritosActuales", inscritosActuales);
        solicitud.agregarMetadato("cuposDisponibles", cupoMaximo - inscritosActuales);

        System.out.println(String.format(
            "[Chain of Responsibility] Cupos válidos: %d/%d disponibles en curso '%s' (%s)",
            cupoMaximo - inscritosActuales,
            cupoMaximo,
            curso.getNombre(),
            tipoCurso
        ));

        return true; // Hay cupos disponibles, continuar cadena
    }

    /**
     * Extrae el ID del curso de la solicitud.
     * Puede venir en el recurso (ej: /api/cursos/5/inscribir) o en los metadatos.
     */
    private Integer extraerCursoId(SolicitudValidacion solicitud) {
        // Intentar obtener de los metadatos primero
        Object cursoIdObj = solicitud.getMetadatos().get("cursoId");
        if (cursoIdObj instanceof Integer) {
            return (Integer) cursoIdObj;
        }

        // Intentar extraer del recurso (ej: /api/cursos/5/inscribir)
        String recurso = solicitud.getRecursoSolicitado();
        if (recurso != null && recurso.contains("/cursos/")) {
            try {
                String[] partes = recurso.split("/");
                for (int i = 0; i < partes.length - 1; i++) {
                    if (partes[i].equals("cursos")) {
                        return Integer.parseInt(partes[i + 1]);
                    }
                }
            } catch (NumberFormatException e) {
                // No se pudo parsear, retornar null
            }
        }

        return null;
    }

    /**
     * Método auxiliar para actualizar el límite de cupos de un tipo de curso
     */
    public static void actualizarCupoMaximo(String tipoCurso, int cupoMaximo) {
        CUPOS_POR_TIPO.put(tipoCurso.toLowerCase(), cupoMaximo);
    }

    /**
     * Método auxiliar para obtener el límite de cupos de un tipo de curso
     */
    public static int obtenerCupoMaximo(String tipoCurso) {
        return CUPOS_POR_TIPO.getOrDefault(tipoCurso.toLowerCase(), 45);
    }
}
