package facade;

import connection.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN FACADE - Fachada Principal
 * Proporciona una interfaz unificada y simplificada para todos los subsistemas del LMS
 * El cliente interactúa solo con esta clase en lugar de múltiples subsistemas
 */
public class LMSFacade {
    // Subsistemas internos
    private SubsistemaInscripcion inscripcion;
    private SubsistemaContenido contenido;
    private SubsistemaEvaluacion evaluacion;
    private SubsistemaSeguimiento seguimiento;
    private Connection connection;

    public LMSFacade() {
        // Inicializar todos los subsistemas
        this.inscripcion = new SubsistemaInscripcion();
        this.contenido = new SubsistemaContenido();
        this.evaluacion = new SubsistemaEvaluacion();
        this.seguimiento = new SubsistemaSeguimiento();
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // ═══════════════════════════════════════════════════════════════
    // OPERACIONES SIMPLIFICADAS DE INSCRIPCIÓN
    // ═══════════════════════════════════════════════════════════════

    /**
     * Inscribir estudiante en un curso (operación simplificada)
     * Internamente coordina verificación de cupo, prerrequisitos y registro
     */
    public boolean inscribirEstudiante(int estudianteId, int cursoId) {
        System.out.println("\n[FACADE] Procesando inscripción...");

        // Coordinar múltiples subsistemas
        if (!inscripcion.verificarCupoDisponible(cursoId)) {
            System.out.println("✗ No hay cupo disponible");
            return false;
        }

        if (!inscripcion.verificarPrerrequisitos(estudianteId, cursoId)) {
            System.out.println("✗ No cumple prerrequisitos");
            return false;
        }

        if (inscripcion.registrarInscripcion(estudianteId, cursoId)) {
            // Inicializar seguimiento del estudiante
            seguimiento.inicializarProgreso(estudianteId, cursoId);
            System.out.println("✓ Inscripción completada exitosamente");
            return true;
        }

        return false;
    }

    /**
     * Dar de baja a un estudiante
     */
    public boolean darDeBajaEstudiante(int estudianteId, int cursoId) {
        System.out.println("\n[FACADE] Procesando baja...");
        return inscripcion.cancelarInscripcion(estudianteId, cursoId);
    }

    // ═══════════════════════════════════════════════════════════════
    // OPERACIONES SIMPLIFICADAS DE CONTENIDO
    // ═══════════════════════════════════════════════════════════════

    /**
     * Obtener contenido del curso para un estudiante
     * Filtra según su progreso y permisos
     */
    public void mostrarContenidoCurso(int estudianteId, int cursoId) {
        System.out.println("\n[FACADE] Cargando contenido del curso...");

        // Verificar acceso
        if (!inscripcion.verificarAcceso(estudianteId, cursoId)) {
            System.out.println("✗ No tiene acceso a este curso");
            return;
        }

        // Obtener y mostrar contenido
        contenido.cargarEstructuraCurso(cursoId);
        contenido.mostrarModulos(cursoId);

        // Mostrar progreso actual
        seguimiento.mostrarProgreso(estudianteId, cursoId);
    }

    /**
     * Agregar material a un módulo
     */
    public boolean agregarMaterial(int moduloId, String nombre, String tipo, String url) {
        System.out.println("\n[FACADE] Agregando material...");
        return contenido.agregarMaterial(moduloId, nombre, tipo, url);
    }

    // ═══════════════════════════════════════════════════════════════
    // OPERACIONES SIMPLIFICADAS DE EVALUACIÓN
    // ═══════════════════════════════════════════════════════════════

    /**
     * Realizar evaluación (proceso completo simplificado)
     */
    public ResultadoEvaluacion realizarEvaluacion(int estudianteId, int evaluacionId) {
        System.out.println("\n[FACADE] Iniciando evaluación...");

        // Verificar si puede realizar la evaluación
        if (!evaluacion.verificarDisponibilidad(evaluacionId)) {
            return new ResultadoEvaluacion(false, "Evaluación no disponible", 0);
        }

        if (!evaluacion.verificarIntentos(estudianteId, evaluacionId)) {
            return new ResultadoEvaluacion(false, "No tiene intentos disponibles", 0);
        }

        // Realizar evaluación
        double calificacion = evaluacion.ejecutarEvaluacion(estudianteId, evaluacionId);

        // Registrar resultado
        evaluacion.registrarResultado(estudianteId, evaluacionId, calificacion);

        // Actualizar progreso
        seguimiento.actualizarProgreso(estudianteId, evaluacionId, calificacion);

        return new ResultadoEvaluacion(true, "Evaluación completada", calificacion);
    }

    /**
     * Obtener calificaciones del estudiante en un curso
     */
    public List<Double> obtenerCalificaciones(int estudianteId, int cursoId) {
        return evaluacion.obtenerCalificaciones(estudianteId, cursoId);
    }

    // ═══════════════════════════════════════════════════════════════
    // OPERACIONES SIMPLIFICADAS DE SEGUIMIENTO
    // ═══════════════════════════════════════════════════════════════

    /**
     * Obtener resumen completo del estudiante en un curso
     */
    public ResumenEstudiante obtenerResumenEstudiante(int estudianteId, int cursoId) {
        System.out.println("\n[FACADE] Generando resumen del estudiante...");

        ResumenEstudiante resumen = new ResumenEstudiante();
        resumen.setProgreso(seguimiento.obtenerPorcentajeProgreso(estudianteId, cursoId));
        resumen.setCalificaciones(evaluacion.obtenerCalificaciones(estudianteId, cursoId));
        resumen.setPromedio(evaluacion.calcularPromedio(estudianteId, cursoId));
        resumen.setTiempoTotal(seguimiento.obtenerTiempoTotal(estudianteId, cursoId));

        return resumen;
    }

    /**
     * Marcar contenido como completado
     */
    public void marcarContenidoCompletado(int estudianteId, int contenidoId) {
        seguimiento.marcarCompletado(estudianteId, contenidoId);
    }

    // ═══════════════════════════════════════════════════════════════
    // OPERACIONES DE ALTO NIVEL
    // ═══════════════════════════════════════════════════════════════

    /**
     * Proceso completo de finalización de curso
     */
    public boolean finalizarCurso(int estudianteId, int cursoId) {
        System.out.println("\n[FACADE] Procesando finalización de curso...");

        // Verificar que completó todo el contenido
        if (seguimiento.obtenerPorcentajeProgreso(estudianteId, cursoId) < 100) {
            System.out.println("✗ No ha completado todo el contenido");
            return false;
        }

        // Verificar calificación mínima
        double promedio = evaluacion.calcularPromedio(estudianteId, cursoId);
        if (promedio < 60) {
            System.out.println("✗ No alcanza la calificación mínima");
            return false;
        }

        // Generar certificado
        System.out.println("✓ Curso finalizado exitosamente");
        System.out.println("  Promedio final: " + promedio);

        return true;
    }

    /**
     * Obtener información general del curso desde BD
     */
    public void mostrarInfoCurso(int cursoId) {
        String sql = "SELECT codigo, nombre, descripcion, tipo_curso, estado, cupo_maximo, cupo_actual " +
                     "FROM cursos WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cursoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("\n═══════════════════════════════════════");
                System.out.println("INFORMACIÓN DEL CURSO");
                System.out.println("═══════════════════════════════════════");
                System.out.println("Código: " + rs.getString("codigo"));
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Tipo: " + rs.getString("tipo_curso"));
                System.out.println("Estado: " + rs.getString("estado"));
                System.out.println("Cupo: " + rs.getInt("cupo_actual") + "/" + rs.getInt("cupo_maximo"));
                System.out.println("═══════════════════════════════════════");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener info del curso: " + e.getMessage());
        }
    }

    // Clase auxiliar para resultados
    public static class ResultadoEvaluacion {
        private boolean exito;
        private String mensaje;
        private double calificacion;

        public ResultadoEvaluacion(boolean exito, String mensaje, double calificacion) {
            this.exito = exito;
            this.mensaje = mensaje;
            this.calificacion = calificacion;
        }

        public boolean isExito() { return exito; }
        public String getMensaje() { return mensaje; }
        public double getCalificacion() { return calificacion; }
    }

    // Clase auxiliar para resumen
    public static class ResumenEstudiante {
        private double progreso;
        private List<Double> calificaciones;
        private double promedio;
        private int tiempoTotal;

        public void setProgreso(double progreso) { this.progreso = progreso; }
        public void setCalificaciones(List<Double> cal) { this.calificaciones = cal; }
        public void setPromedio(double promedio) { this.promedio = promedio; }
        public void setTiempoTotal(int tiempo) { this.tiempoTotal = tiempo; }

        public void mostrar() {
            System.out.println("\n─────────────────────────────────");
            System.out.println("RESUMEN DEL ESTUDIANTE");
            System.out.println("─────────────────────────────────");
            System.out.println("Progreso: " + progreso + "%");
            System.out.println("Promedio: " + promedio);
            System.out.println("Tiempo total: " + tiempoTotal + " minutos");
            System.out.println("─────────────────────────────────");
        }
    }
}
