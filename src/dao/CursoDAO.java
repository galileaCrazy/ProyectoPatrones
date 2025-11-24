package dao;

import connection.DatabaseConnection;
import model.Curso;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del DAO para Curso
 * @author USUARIO
 */
public class CursoDAO implements ICursoDAO {

    private Connection connection;

    public CursoDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public int insertar(Curso curso) {
        String sql = "INSERT INTO cursos (codigo, nombre, descripcion, tipo_curso, estado, " +
                     "estrategia_evaluacion, profesor_titular_id, periodo_academico, fecha_inicio, " +
                     "fecha_fin, cupo_maximo, cupo_actual) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, curso.getCodigo());
            stmt.setString(2, curso.getNombre());
            stmt.setString(3, curso.getDescripcion());
            stmt.setString(4, curso.getTipoCurso().getValor());
            stmt.setString(5, curso.getEstado().getValor());
            stmt.setString(6, curso.getEstrategiaEvaluacion());
            stmt.setInt(7, curso.getProfesorTitularId());
            stmt.setString(8, curso.getPeriodoAcademico());
            stmt.setDate(9, curso.getFechaInicio() != null ? new java.sql.Date(curso.getFechaInicio().getTime()) : null);
            stmt.setDate(10, curso.getFechaFin() != null ? new java.sql.Date(curso.getFechaFin().getTime()) : null);
            stmt.setInt(11, curso.getCupoMaximo());
            stmt.setInt(12, curso.getCupoActual());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar curso: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public boolean actualizar(Curso curso) {
        String sql = "UPDATE cursos SET codigo = ?, nombre = ?, descripcion = ?, tipo_curso = ?, " +
                     "estado = ?, estrategia_evaluacion = ?, periodo_academico = ?, fecha_inicio = ?, " +
                     "fecha_fin = ?, cupo_maximo = ?, cupo_actual = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, curso.getCodigo());
            stmt.setString(2, curso.getNombre());
            stmt.setString(3, curso.getDescripcion());
            stmt.setString(4, curso.getTipoCurso().getValor());
            stmt.setString(5, curso.getEstado().getValor());
            stmt.setString(6, curso.getEstrategiaEvaluacion());
            stmt.setString(7, curso.getPeriodoAcademico());
            stmt.setDate(8, curso.getFechaInicio() != null ? new java.sql.Date(curso.getFechaInicio().getTime()) : null);
            stmt.setDate(9, curso.getFechaFin() != null ? new java.sql.Date(curso.getFechaFin().getTime()) : null);
            stmt.setInt(10, curso.getCupoMaximo());
            stmt.setInt(11, curso.getCupoActual());
            stmt.setInt(12, curso.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar curso: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM cursos WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar curso: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Curso obtenerPorId(int id) {
        String sql = "SELECT * FROM cursos WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCurso(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener curso por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Curso obtenerPorCodigo(String codigo) {
        String sql = "SELECT * FROM cursos WHERE codigo = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToCurso(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener curso por código: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Curso> obtenerTodos() {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT * FROM cursos ORDER BY created_at DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                cursos.add(mapResultSetToCurso(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los cursos: " + e.getMessage());
            e.printStackTrace();
        }
        return cursos;
    }

    @Override
    public List<Curso> obtenerPorTipo(Curso.TipoCurso tipoCurso) {
        List<Curso> cursos = new ArrayList<>();
        String sql = "SELECT * FROM cursos WHERE tipo_curso = ? ORDER BY created_at DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipoCurso.getValor());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                cursos.add(mapResultSetToCurso(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cursos por tipo: " + e.getMessage());
            e.printStackTrace();
        }
        return cursos;
    }

    /**
     * Mapea un ResultSet a un objeto Curso
     */
    private Curso mapResultSetToCurso(ResultSet rs) throws SQLException {
        Curso curso = new Curso();
        curso.setId(rs.getInt("id"));
        curso.setCodigo(rs.getString("codigo"));
        curso.setNombre(rs.getString("nombre"));
        curso.setDescripcion(rs.getString("descripcion"));
        curso.setTipoCurso(Curso.TipoCurso.fromString(rs.getString("tipo_curso")));
        curso.setEstado(Curso.EstadoCurso.fromString(rs.getString("estado")));
        curso.setEstrategiaEvaluacion(rs.getString("estrategia_evaluacion"));
        curso.setProfesorTitularId(rs.getInt("profesor_titular_id"));
        curso.setPeriodoAcademico(rs.getString("periodo_academico"));
        curso.setFechaInicio(rs.getDate("fecha_inicio"));
        curso.setFechaFin(rs.getDate("fecha_fin"));
        curso.setCupoMaximo(rs.getInt("cupo_maximo"));
        curso.setCupoActual(rs.getInt("cupo_actual"));
        curso.setCreatedAt(rs.getTimestamp("created_at"));
        curso.setUpdatedAt(rs.getTimestamp("updated_at"));
        return curso;
    }
}
