package dao;

import connection.DatabaseConnection;
import model.Material;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del DAO para Material
 * @author USUARIO
 */
public class MaterialDAO implements IMaterialDAO {

    private Connection connection;

    public MaterialDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public int insertar(Material material) {
        String sql = "INSERT INTO materiales (modulo_id, tipo, nombre, descripcion, url_recurso, " +
                     "tamano_bytes, duracion_segundos, orden, requiere_visualizacion, activo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, material.getModuloId());
            stmt.setString(2, material.getTipo().getValor());
            stmt.setString(3, material.getNombre());
            stmt.setString(4, material.getDescripcion());
            stmt.setString(5, material.getUrlRecurso());
            stmt.setObject(6, material.getTamanioBytes());
            stmt.setObject(7, material.getDuracionSegundos());
            stmt.setInt(8, material.getOrden());
            stmt.setBoolean(9, material.isRequiereVisualizacion());
            stmt.setBoolean(10, material.isActivo());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar material: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean actualizar(Material material) {
        String sql = "UPDATE materiales SET modulo_id = ?, tipo = ?, nombre = ?, descripcion = ?, " +
                     "url_recurso = ?, tamano_bytes = ?, duracion_segundos = ?, orden = ?, " +
                     "requiere_visualizacion = ?, activo = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, material.getModuloId());
            stmt.setString(2, material.getTipo().getValor());
            stmt.setString(3, material.getNombre());
            stmt.setString(4, material.getDescripcion());
            stmt.setString(5, material.getUrlRecurso());
            stmt.setObject(6, material.getTamanioBytes());
            stmt.setObject(7, material.getDuracionSegundos());
            stmt.setInt(8, material.getOrden());
            stmt.setBoolean(9, material.isRequiereVisualizacion());
            stmt.setBoolean(10, material.isActivo());
            stmt.setInt(11, material.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar material: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM materiales WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar material: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Material obtenerPorId(int id) {
        String sql = "SELECT * FROM materiales WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToMaterial(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener material por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Material> obtenerPorModulo(int moduloId) {
        List<Material> materiales = new ArrayList<>();
        String sql = "SELECT * FROM materiales WHERE modulo_id = ? ORDER BY orden ASC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, moduloId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                materiales.add(mapResultSetToMaterial(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener materiales por módulo: " + e.getMessage());
            e.printStackTrace();
        }
        return materiales;
    }

    @Override
    public List<Material> obtenerTodos() {
        List<Material> materiales = new ArrayList<>();
        String sql = "SELECT * FROM materiales ORDER BY created_at DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                materiales.add(mapResultSetToMaterial(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los materiales: " + e.getMessage());
            e.printStackTrace();
        }
        return materiales;
    }

    @Override
    public List<Material> obtenerPorTipo(Material.TipoMaterial tipo) {
        List<Material> materiales = new ArrayList<>();
        String sql = "SELECT * FROM materiales WHERE tipo = ? ORDER BY created_at DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipo.getValor());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                materiales.add(mapResultSetToMaterial(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener materiales por tipo: " + e.getMessage());
            e.printStackTrace();
        }
        return materiales;
    }

    /**
     * Mapea un ResultSet a un objeto Material
     */
    private Material mapResultSetToMaterial(ResultSet rs) throws SQLException {
        Material material = new Material();
        material.setId(rs.getInt("id"));
        material.setModuloId(rs.getInt("modulo_id"));
        material.setTipo(Material.TipoMaterial.fromString(rs.getString("tipo")));
        material.setNombre(rs.getString("nombre"));
        material.setDescripcion(rs.getString("descripcion"));
        material.setUrlRecurso(rs.getString("url_recurso"));

        Long tamanio = rs.getLong("tamano_bytes");
        material.setTamanioBytes(rs.wasNull() ? null : tamanio);

        Integer duracion = rs.getInt("duracion_segundos");
        material.setDuracionSegundos(rs.wasNull() ? null : duracion);

        material.setOrden(rs.getInt("orden"));
        material.setRequiereVisualizacion(rs.getBoolean("requiere_visualizacion"));
        material.setActivo(rs.getBoolean("activo"));
        material.setCreatedAt(rs.getTimestamp("created_at"));
        return material;
    }
}
