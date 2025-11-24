 package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase Singleton para gestionar la conexión a la base de datos
 * Implementa el patrón Singleton para garantizar una única instancia
 * @author USUARIO
 */
public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    // Configuración de la base de datos
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "plataformaCursos";
    private static final String USER = "root";
    private static final String PASSWORD = "otomegame47";

    /**
     * Constructor privado para implementar patrón Singleton
     */
    private DatabaseConnection() {
        try {
            Class.forName(JDBC_DRIVER);
            this.connection = DriverManager.getConnection(DB_URL + DB_NAME, USER, PASSWORD);
            System.out.println("✓ Conexión a la base de datos establecida exitosamente!");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ Error: Driver JDBC no encontrado.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("✗ Error al conectar con la base de datos.");
            System.err.println("  Verifique que MySQL esté corriendo y la BD exista.");
            e.printStackTrace();
        }
    }

    /**
     * Obtiene la instancia única de DatabaseConnection (Singleton)
     * @return instancia única de DatabaseConnection
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Obtiene la conexión a la base de datos
     * Si la conexión está cerrada, la reconecta automáticamente
     * @return objeto Connection
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Reconectando a la base de datos...");
                connection = DriverManager.getConnection(DB_URL + DB_NAME, USER, PASSWORD);
                System.out.println("✓ Reconexión exitosa!");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error al verificar o restablecer la conexión.");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Cierra la conexión a la base de datos
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("✓ Conexión cerrada exitosamente.");
                connection = null;
                instance = null;
            } catch (SQLException e) {
                System.err.println("✗ Error al cerrar la conexión.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Verifica si la conexión está activa
     * @return true si la conexión está activa, false en caso contrario
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Método para probar la conexión
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════");
        System.out.println("  Probando conexión a la base de datos");
        System.out.println("═══════════════════════════════════════════════\n");

        try {
            // Obtener instancia única
            DatabaseConnection dbConnection = DatabaseConnection.getInstance();

            // Obtener conexión
            Connection conn = dbConnection.getConnection();

            if (conn != null && !conn.isClosed()) {
                System.out.println("\n✓ ÉXITO: Conexión establecida correctamente!");
                System.out.println("  Base de datos: " + DB_NAME);
                System.out.println("  Estado: Conectado");

                // Cerrar conexión
                dbConnection.closeConnection();
            } else {
                System.out.println("\n✗ ERROR: No se pudo establecer la conexión.");
            }

        } catch (Exception e) {
            System.err.println("\n✗ ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("\n═══════════════════════════════════════════════");
    }
}
