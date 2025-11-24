package dao;

import connection.DatabaseConnection;
import factorymethod.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del DAO para Usuario
 * Gestiona la persistencia de usuarios en la base de datos
 * Compatible con la estructura normalizada (usuarios + estudiantes/profesores)
 * @author USUARIO
 */
public class UsuarioDAO implements IUsuarioDAO {

    private Connection connection;

    public UsuarioDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public int insertar(IUsuario usuario) {
        String sqlUsuario = "INSERT INTO usuarios (email, password_hash, nombre, apellidos, tipo_usuario) " +
                           "VALUES (?, ?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            // 1. Insertar en tabla usuarios
            try (PreparedStatement stmt = connection.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, usuario.getCorreo());
                stmt.setString(2, usuario.getContrasena());
                stmt.setString(3, usuario.getNombre());
                stmt.setString(4, usuario.getApellidos());
                stmt.setString(5, usuario.getTipoUsuario().toLowerCase());

                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int id = generatedKeys.getInt(1);
                            usuario.setId(id);

                            // 2. Insertar en tabla específica según tipo
                            boolean insertadoEspecifico = insertarDatosEspecificos(usuario, id);

                            if (insertadoEspecifico) {
                                connection.commit();
                                return id;
                            } else {
                                connection.rollback();
                                return -1;
                            }
                        }
                    }
                }
            }
            connection.rollback();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Error al insertar usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /**
     * Inserta los datos específicos según el tipo de usuario
     */
    private boolean insertarDatosEspecificos(IUsuario usuario, int usuarioId) throws SQLException {
        if (usuario instanceof Estudiante) {
            Estudiante est = (Estudiante) usuario;
            String sql = "INSERT INTO estudiantes (usuario_id, matricula, programa_academico) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, usuarioId);
                stmt.setString(2, est.getMatricula());
                stmt.setString(3, est.getCarrera());
                return stmt.executeUpdate() > 0;
            }
        } else if (usuario instanceof Profesor) {
            Profesor prof = (Profesor) usuario;
            String sql = "INSERT INTO profesores (usuario_id, numero_empleado, departamento, especialidad) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, usuarioId);
                stmt.setString(2, generarNumeroEmpleado());
                stmt.setString(3, prof.getDepartamento());
                stmt.setString(4, prof.getEspecialidad());
                return stmt.executeUpdate() > 0;
            }
        } else if (usuario instanceof Administrador) {
            // Los administradores no tienen tabla específica, solo están en usuarios
            return true;
        }
        return true;
    }

    /**
     * Genera un número de empleado único para profesores
     */
    private String generarNumeroEmpleado() {
        return "PROF-" + System.currentTimeMillis();
    }

    @Override
    public boolean actualizar(IUsuario usuario) {
        String sqlUsuario = "UPDATE usuarios SET email = ?, password_hash = ?, nombre = ?, apellidos = ? WHERE id = ?";

        try {
            connection.setAutoCommit(false);

            // 1. Actualizar tabla usuarios
            try (PreparedStatement stmt = connection.prepareStatement(sqlUsuario)) {
                stmt.setString(1, usuario.getCorreo());
                stmt.setString(2, usuario.getContrasena());
                stmt.setString(3, usuario.getNombre());
                stmt.setString(4, usuario.getApellidos());
                stmt.setInt(5, usuario.getId());

                if (stmt.executeUpdate() > 0) {
                    // 2. Actualizar datos específicos
                    boolean actualizadoEspecifico = actualizarDatosEspecificos(usuario);

                    if (actualizadoEspecifico) {
                        connection.commit();
                        return true;
                    }
                }
            }
            connection.rollback();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Actualiza los datos específicos según el tipo de usuario
     */
    private boolean actualizarDatosEspecificos(IUsuario usuario) throws SQLException {
        if (usuario instanceof Estudiante) {
            Estudiante est = (Estudiante) usuario;
            String sql = "UPDATE estudiantes SET matricula = ?, programa_academico = ? WHERE usuario_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, est.getMatricula());
                stmt.setString(2, est.getCarrera());
                stmt.setInt(3, usuario.getId());
                return stmt.executeUpdate() >= 0; // >= 0 porque puede no haber cambios
            }
        } else if (usuario instanceof Profesor) {
            Profesor prof = (Profesor) usuario;
            String sql = "UPDATE profesores SET departamento = ?, especialidad = ? WHERE usuario_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, prof.getDepartamento());
                stmt.setString(2, prof.getEspecialidad());
                stmt.setInt(3, usuario.getId());
                return stmt.executeUpdate() >= 0;
            }
        }
        return true;
    }

    @Override
    public boolean eliminar(int id) {
        // Con ON DELETE CASCADE, eliminar de usuarios eliminará automáticamente de estudiantes/profesores
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public IUsuario obtenerPorId(int id) {
        String sql = "SELECT u.*, " +
                     "e.matricula, e.programa_academico, e.semestre, " +
                     "p.numero_empleado, p.departamento, p.especialidad, p.grado_academico " +
                     "FROM usuarios u " +
                     "LEFT JOIN estudiantes e ON u.id = e.usuario_id " +
                     "LEFT JOIN profesores p ON u.id = p.usuario_id " +
                     "WHERE u.id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return crearUsuarioDesdeResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IUsuario obtenerPorCorreo(String correo) {
        String sql = "SELECT u.*, " +
                     "e.matricula, e.programa_academico, e.semestre, " +
                     "p.numero_empleado, p.departamento, p.especialidad, p.grado_academico " +
                     "FROM usuarios u " +
                     "LEFT JOIN estudiantes e ON u.id = e.usuario_id " +
                     "LEFT JOIN profesores p ON u.id = p.usuario_id " +
                     "WHERE u.email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, correo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return crearUsuarioDesdeResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuario por correo: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public IUsuario validarCredenciales(String correo, String contrasena, String tipoUsuario) {
        String sql = "SELECT u.*, " +
                     "e.matricula, e.programa_academico, e.semestre, " +
                     "p.numero_empleado, p.departamento, p.especialidad, p.grado_academico " +
                     "FROM usuarios u " +
                     "LEFT JOIN estudiantes e ON u.id = e.usuario_id " +
                     "LEFT JOIN profesores p ON u.id = p.usuario_id " +
                     "WHERE u.email = ? AND u.password_hash = ? AND u.tipo_usuario = ? AND u.activo = 1";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, correo);
            stmt.setString(2, contrasena);
            stmt.setString(3, tipoUsuario.toLowerCase());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Actualizar último acceso
                    actualizarUltimoAcceso(rs.getInt("id"));
                    return crearUsuarioDesdeResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al validar credenciales: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Actualiza la fecha de último acceso del usuario
     */
    private void actualizarUltimoAcceso(int usuarioId) {
        String sql = "UPDATE usuarios SET ultimo_acceso = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            // No es crítico si falla
            System.err.println("No se pudo actualizar último acceso: " + e.getMessage());
        }
    }

    @Override
    public List<IUsuario> obtenerTodos() {
        List<IUsuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.*, " +
                     "e.matricula, e.programa_academico, e.semestre, " +
                     "p.numero_empleado, p.departamento, p.especialidad, p.grado_academico " +
                     "FROM usuarios u " +
                     "LEFT JOIN estudiantes e ON u.id = e.usuario_id " +
                     "LEFT JOIN profesores p ON u.id = p.usuario_id " +
                     "WHERE u.activo = 1 ORDER BY u.created_at DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                IUsuario usuario = crearUsuarioDesdeResultSet(rs);
                if (usuario != null) {
                    usuarios.add(usuario);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los usuarios: " + e.getMessage());
            e.printStackTrace();
        }
        return usuarios;
    }

    @Override
    public List<IUsuario> obtenerPorTipo(String tipoUsuario) {
        List<IUsuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.*, " +
                     "e.matricula, e.programa_academico, e.semestre, " +
                     "p.numero_empleado, p.departamento, p.especialidad, p.grado_academico " +
                     "FROM usuarios u " +
                     "LEFT JOIN estudiantes e ON u.id = e.usuario_id " +
                     "LEFT JOIN profesores p ON u.id = p.usuario_id " +
                     "WHERE u.tipo_usuario = ? AND u.activo = 1 ORDER BY u.created_at DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tipoUsuario.toLowerCase());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    IUsuario usuario = crearUsuarioDesdeResultSet(rs);
                    if (usuario != null) {
                        usuarios.add(usuario);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener usuarios por tipo: " + e.getMessage());
            e.printStackTrace();
        }
        return usuarios;
    }

    @Override
    public boolean existeCorreo(String correo) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, correo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar correo: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Crea un objeto IUsuario desde un ResultSet
     * Utiliza el patrón Factory Method para crear el tipo correcto de usuario
     * @param rs ResultSet con los datos del usuario
     * @return objeto IUsuario del tipo correcto
     * @throws SQLException si hay error al leer del ResultSet
     */
    private IUsuario crearUsuarioDesdeResultSet(ResultSet rs) throws SQLException {
        String tipoUsuario = rs.getString("tipo_usuario");
        IUsuario usuario;

        // Usar el Factory Method para crear el tipo correcto de usuario
        switch (tipoUsuario.toLowerCase()) {
            case "estudiante":
                Estudiante estudiante = new Estudiante();
                estudiante.setMatricula(rs.getString("matricula"));
                estudiante.setCarrera(rs.getString("programa_academico"));
                usuario = estudiante;
                break;

            case "profesor":
                Profesor profesor = new Profesor();
                profesor.setDepartamento(rs.getString("departamento"));
                profesor.setEspecialidad(rs.getString("especialidad"));
                usuario = profesor;
                break;

            case "administrador":
                Administrador administrador = new Administrador();
                administrador.setNivelAcceso("ALTO");
                administrador.setArea("Administración General");
                usuario = administrador;
                break;

            default:
                System.err.println("Tipo de usuario desconocido: " + tipoUsuario);
                return null;
        }

        // Establecer campos comunes
        usuario.setId(rs.getInt("id"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setApellidos(rs.getString("apellidos"));
        usuario.setCorreo(rs.getString("email"));
        usuario.setContrasena(rs.getString("password_hash"));

        return usuario;
    }
}
