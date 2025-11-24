package facade;

import connection.DatabaseConnection;
import java.sql.*;

/**
 * PATRÓN FACADE - Subsistema de Inscripción
 * Maneja toda la lógica relacionada con inscripciones de estudiantes
 */
public class SubsistemaInscripcion {
    private Connection connection;

    public SubsistemaInscripcion() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public boolean verificarCupoDisponible(int cursoId) {
        String sql = "SELECT cupo_maximo, cupo_actual FROM cursos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cursoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int max = rs.getInt("cupo_maximo");
                int actual = rs.getInt("cupo_actual");
                System.out.println("  [Inscripción] Verificando cupo: " + actual + "/" + max);
                return actual < max;
            }
        } catch (SQLException e) {
            System.err.println("Error verificando cupo: " + e.getMessage());
        }
        return false;
    }

    public boolean verificarPrerrequisitos(int estudianteId, int cursoId) {
        System.out.println("  [Inscripción] Verificando prerrequisitos...");
        // Simulación - en producción verificaría cursos previos
        return true;
    }

    public boolean registrarInscripcion(int estudianteId, int cursoId) {
        String sql = "UPDATE cursos SET cupo_actual = cupo_actual + 1 WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cursoId);
            System.out.println("  [Inscripción] Registrando estudiante " + estudianteId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error en inscripción: " + e.getMessage());
        }
        return false;
    }

    public boolean cancelarInscripcion(int estudianteId, int cursoId) {
        String sql = "UPDATE cursos SET cupo_actual = cupo_actual - 1 WHERE id = ? AND cupo_actual > 0";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cursoId);
            System.out.println("  [Inscripción] Cancelando inscripción del estudiante " + estudianteId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error cancelando inscripción: " + e.getMessage());
        }
        return false;
    }

    public boolean verificarAcceso(int estudianteId, int cursoId) {
        System.out.println("  [Inscripción] Verificando acceso...");
        return true; // Simulación
    }
}
