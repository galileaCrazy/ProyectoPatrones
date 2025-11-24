package dao;

import connection.DatabaseConnection;
import model.Evaluacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del DAO para Evaluacion
 * @author USUARIO
 */
public class EvaluacionDAO implements IEvaluacionDAO {

    private Connection connection;

    public EvaluacionDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public int insertar(Evaluacion evaluacion) {
        String sql = "INSERT INTO evaluaciones (modulo_id, tipo, nombre, descripcion, puntaje_maximo, " +
                     "peso_porcentual, intentos_permitidos, tiempo_limite_minutos, fecha_apertura, fecha_cierre) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, evaluacion.getModuloId());
            stmt.setString(2, evaluacion.getTipo().getValor());
            stmt.setString(3, evaluacion.getNombre());
            stmt.setString(4, evaluacion.getDescripcion());
            stmt.setBigDecimal(5, evaluacion.getPuntajeMaximo());
            stmt.setBigDecimal(6, evaluacion.getPesoPorcentual());
            stmt.setInt(7, evaluacion.getIntentosPermitidos());
            stmt.setObject(8, evaluacion.getTiempoLimiteMinutos());
            stmt.setTimestamp(9, evaluacion.getFechaApertura() != null ?
                    new Timestamp(evaluacion.getFechaApertura().getTime()) : null);
            stmt.setTimestamp(10, evaluacion.getFechaCierre() != null ?
                    new Timestamp(evaluacion.getFechaCierre().getTime()) : null);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar evaluación: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean actualizar(Evaluacion evaluacion) {
        String sql = "UPDATE evaluaciones SET modulo_id = ?, tipo = ?, nombre = ?, descripcion = ?, " +
                     "puntaje_maximo = ?, peso_porcentual = ?, intentos_permitidos = ?, " +
                     "tiempo_limite_minutos = ?, fecha_apertura = ?, fecha_cierre = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, evaluacion.getModuloId());
            stmt.setString(2, evaluacion.getTipo().getValor());
            stmt.setString(3, evaluacion.getNombre());
            stmt.setString(4, evaluacion.getDescripcion());
            stmt.setBigDecimal(5, evaluacion.getPuntajeMaximo());
            stmt.setBigDecimal(6, evaluacion.getPesoPorcentual());
            stmt.setInt(7, evaluacion.getIntentosPermitidos());
            stmt.setObject(8, evaluacion.getTiempoLimiteMinutos());
            stmt.setTimestamp(9, evaluacion.getFechaApertura() != null ?
                    new Timestamp(evaluacion.getFechaApertura().getTime()) : null);
            stmt.setTimestamp(10, evaluacion.getFechaCierre() != null ?
                    new Timestamp(evaluacion.getFechaCierre().getTime()) : null);
            stmt.setInt(11, evaluacion.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar evaluación: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM evaluaciones WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar evaluación: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Evaluacion obtenerPorId(int id) {
        String sql = "SELECT * FROM evaluaciones WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToEvaluacion(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener evaluación por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Evaluacion> obtenerPorModulo(int moduloId) {
        List<Evaluacion> evaluaciones = new ArrayList<>();
        String sql = "SELECT * FROM evaluaciones WHERE modulo_id = ? ORDER BY fecha_apertura ASC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, moduloId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                evaluaciones.add(mapResultSetToEvaluacion(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener evaluaciones por módulo: " + e.getMessage());
            e.printStackTrace();
        }
        return evaluaciones;
    }

    @Override
    public List<Evaluacion> obtenerTodos() {
        List<Evaluacion> evaluaciones = new ArrayList<>();
        String sql = "SELECT * FROM evaluaciones ORDER BY created_at DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                evaluaciones.add(mapResultSetToEvaluacion(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todas las evaluaciones: " + e.getMessage());
            e.printStackTrace();
        }
        return evaluaciones;
    }

    @Override
    public List<Evaluacion> obtenerPorTipo(Evaluacion.TipoEvaluacion tipo) {
        List<Evaluacion> evaluaciones = new ArrayList<>();
        String sql = "SELECT * FROM evaluaciones WHERE tipo = ? ORDER BY created_at DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipo.getValor());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                evaluaciones.add(mapResultSetToEvaluacion(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener evaluaciones por tipo: " + e.getMessage());
            e.printStackTrace();
        }
        return evaluaciones;
    }

    /**
     * Mapea un ResultSet a un objeto Evaluacion
     */
    private Evaluacion mapResultSetToEvaluacion(ResultSet rs) throws SQLException {
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setId(rs.getInt("id"));
        evaluacion.setModuloId(rs.getInt("modulo_id"));
        evaluacion.setTipo(Evaluacion.TipoEvaluacion.fromString(rs.getString("tipo")));
        evaluacion.setNombre(rs.getString("nombre"));
        evaluacion.setDescripcion(rs.getString("descripcion"));
        evaluacion.setPuntajeMaximo(rs.getBigDecimal("puntaje_maximo"));
        evaluacion.setPesoPorcentual(rs.getBigDecimal("peso_porcentual"));
        evaluacion.setIntentosPermitidos(rs.getInt("intentos_permitidos"));

        Integer tiempoLimite = rs.getInt("tiempo_limite_minutos");
        evaluacion.setTiempoLimiteMinutos(rs.wasNull() ? null : tiempoLimite);

        evaluacion.setFechaApertura(rs.getTimestamp("fecha_apertura"));
        evaluacion.setFechaCierre(rs.getTimestamp("fecha_cierre"));
        evaluacion.setCreatedAt(rs.getTimestamp("created_at"));
        return evaluacion;
    }
}
