package composite;

import connection.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PATRÓN COMPOSITE - Composite raíz
 * Representa el curso completo que contiene toda la estructura jerárquica
 * Integra con la base de datos para persistir la estructura
 */
public class CursoComposite implements ComponenteCurso {
    private int id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private List<ComponenteCurso> unidades;

    public CursoComposite(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.unidades = new ArrayList<>();
    }

    // Agregar unidad al curso
    public void agregar(ComponenteCurso componente) {
        unidades.add(componente);
    }

    // Remover unidad del curso
    public void remover(ComponenteCurso componente) {
        unidades.remove(componente);
    }

    // Obtener todas las unidades
    public List<ComponenteCurso> getHijos() {
        return unidades;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public void mostrar(int nivel) {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║ CURSO: " + nombre);
        System.out.println("║ Código: " + codigo);
        System.out.println("║ Total unidades: " + unidades.size());
        System.out.println("║ Duración total: " + getDuracionTotal() + " minutos");
        System.out.println("╚════════════════════════════════════════════╝");

        for (ComponenteCurso unidad : unidades) {
            unidad.mostrar(nivel);
        }
    }

    @Override
    public int getDuracionTotal() {
        int total = 0;
        for (ComponenteCurso unidad : unidades) {
            total += unidad.getDuracionTotal();
        }
        return total;
    }

    @Override
    public boolean esContenedor() {
        return true;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    // Contar total de actividades en todo el curso (recorrido recursivo)
    public int contarActividades() {
        return contarActividadesRecursivo(unidades);
    }

    private int contarActividadesRecursivo(List<ComponenteCurso> componentes) {
        int count = 0;
        for (ComponenteCurso comp : componentes) {
            if (!comp.esContenedor()) {
                count++;
            } else {
                if (comp instanceof Unidad) {
                    count += contarActividadesRecursivo(((Unidad) comp).getHijos());
                } else if (comp instanceof Leccion) {
                    count += contarActividadesRecursivo(((Leccion) comp).getHijos());
                }
            }
        }
        return count;
    }

    // Guardar estructura en la base de datos
    public boolean guardarEnBD(int cursoId) {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        this.id = cursoId;

        try {
            // Aquí se guardaría la estructura jerárquica
            // Por simplicidad, guardamos un log
            String sql = "UPDATE cursos SET metadata = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            String metadata = "{\"unidades\":" + unidades.size() +
                             ",\"duracion_total\":" + getDuracionTotal() +
                             ",\"actividades\":" + contarActividades() + "}";
            stmt.setString(1, metadata);
            stmt.setInt(2, cursoId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar estructura: " + e.getMessage());
            return false;
        }
    }
}
