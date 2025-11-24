package decorator;

import connection.DatabaseConnection;
import java.sql.*;

/**
 * PATRÓN DECORATOR - DAO para persistencia
 * Gestiona la configuración de decoradores en la base de datos
 */
public class ModuloDecoratorDAO {
    private Connection connection;

    public ModuloDecoratorDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    // Cargar módulo base desde la BD
    public ModuloBase cargarModulo(int moduloId) {
        // Simulación - en producción se cargaría de la tabla modulos
        return new ModuloBase(moduloId, "Módulo " + moduloId, "Descripción del módulo", 1);
    }

    // Guardar configuración de decoradores para un módulo
    public boolean guardarConfiguracion(int cursoId, String configuracion) {
        String sql = "UPDATE cursos SET configuracion_evaluacion = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, configuracion);
            stmt.setInt(2, cursoId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar configuración: " + e.getMessage());
            return false;
        }
    }

    // Crear módulo decorado basado en configuración
    public IModuloEducativo crearModuloDecorado(int moduloId, boolean gamificacion,
                                                 boolean certificacion, boolean tutoria) {
        IModuloEducativo modulo = cargarModulo(moduloId);

        // Aplicar decoradores según configuración
        if (gamificacion) {
            modulo = new GamificacionDecorator(modulo);
        }
        if (certificacion) {
            modulo = new CertificacionDecorator(modulo);
        }
        if (tutoria) {
            modulo = new TutoriaDecorator(modulo);
        }

        return modulo;
    }
}
