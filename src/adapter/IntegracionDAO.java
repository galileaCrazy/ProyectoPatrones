package adapter;

import connection.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN ADAPTER - DAO para persistencia de integraciones
 * Gestiona las integraciones externas en la base de datos
 */
public class IntegracionDAO {
    private Connection connection;

    public IntegracionDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // Registrar una nueva integración externa
    public int registrarIntegracion(String tipo, String proveedor, String configuracion) {
        String sql = "INSERT INTO integraciones_externas (tipo, proveedor, configuracion, activo) VALUES (?, ?, ?, true)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, tipo);
            stmt.setString(2, proveedor);
            stmt.setString(3, configuracion);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar integración: " + e.getMessage());
        }
        return -1;
    }

    // Asociar una integración a un curso
    public boolean asociarIntegracionACurso(int cursoId, int integracionId, String configEspecifica) {
        String sql = "INSERT INTO curso_integraciones (curso_id, integracion_id, configuracion_especifica) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cursoId);
            stmt.setInt(2, integracionId);
            stmt.setString(3, configEspecifica);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al asociar integración: " + e.getMessage());
        }
        return false;
    }

    // Obtener integraciones de un curso
    public List<String> obtenerIntegracionesDeCurso(int cursoId) {
        List<String> integraciones = new ArrayList<>();
        String sql = "SELECT ie.proveedor, ie.tipo FROM integraciones_externas ie " +
                     "JOIN curso_integraciones ci ON ie.id = ci.integracion_id " +
                     "WHERE ci.curso_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, cursoId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                integraciones.add(rs.getString("proveedor") + " (" + rs.getString("tipo") + ")");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener integraciones: " + e.getMessage());
        }
        return integraciones;
    }
}
