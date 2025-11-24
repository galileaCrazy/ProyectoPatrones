package facade;

import connection.DatabaseConnection;
import java.sql.*;

/**
 * PATRÓN FACADE - Subsistema de Contenido
 * Maneja la carga y presentación del contenido educativo
 */
public class SubsistemaContenido {
    private Connection connection;

    public SubsistemaContenido() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public void cargarEstructuraCurso(int cursoId) {
        System.out.println("  [Contenido] Cargando estructura del curso " + cursoId);
    }

    public void mostrarModulos(int cursoId) {
        String sql = "SELECT nombre FROM cursos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cursoId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("  [Contenido] Mostrando módulos de: " + rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.err.println("Error mostrando módulos: " + e.getMessage());
        }
    }

    public boolean agregarMaterial(int moduloId, String nombre, String tipo, String url) {
        System.out.println("  [Contenido] Agregando material: " + nombre + " (" + tipo + ")");
        // En producción insertaría en tabla materiales
        return true;
    }
}
