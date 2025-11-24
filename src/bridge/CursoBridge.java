package bridge;

import connection.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN BRIDGE - Refined Abstraction
 * Extiende la abstracción para cursos específicos
 * Puede renderizarse en cualquier dispositivo sin cambiar el código
 */
public class CursoBridge extends ContenidoEducativo {
    private int id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String tipoCurso;
    private List<ModuloBridge> modulos;

    public CursoBridge(DispositivoRenderer renderer) {
        super(renderer);
        this.modulos = new ArrayList<>();
    }

    // Cargar curso desde la base de datos
    public boolean cargarDesdeBD(int cursoId) {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        String sql = "SELECT id, codigo, nombre, descripcion, tipo_curso FROM cursos WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cursoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                this.id = rs.getInt("id");
                this.codigo = rs.getString("codigo");
                this.nombre = rs.getString("nombre");
                this.descripcion = rs.getString("descripcion");
                this.tipoCurso = rs.getString("tipo_curso");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar curso: " + e.getMessage());
        }
        return false;
    }

    // Cargar con datos directos
    public void cargarDatos(String codigo, String nombre, String descripcion, String tipo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoCurso = tipo;
    }

    // Agregar módulo al curso
    public void agregarModulo(ModuloBridge modulo) {
        this.modulos.add(modulo);
    }

    @Override
    public void mostrar() {
        // Usar el renderer (implementor) para mostrar el curso
        renderer.renderizarCurso(nombre, descripcion, tipoCurso);

        // Mostrar módulos
        for (ModuloBridge modulo : modulos) {
            modulo.setRenderer(this.renderer);
            modulo.mostrar();
        }
    }

    // Getters
    public int getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
}
