package abstractfactory;

/**
 * Producto Abstracto: Interfaz para Curso
 * Define el contrato que deben cumplir todos los tipos de cursos
 * @author USUARIO
 */
public interface ICurso {
    /**
     * Obtiene el código del curso
     */
    String getCodigo();

    /**
     * Obtiene el nombre del curso
     */
    String getNombre();

    /**
     * Obtiene la descripción del curso
     */
    String getDescripcion();

    /**
     * Obtiene el tipo de curso
     */
    String getTipo();

    /**
     * Obtiene la estrategia de evaluación
     */
    String getEstrategiaEvaluacion();

    /**
     * Obtiene el cupo máximo
     */
    int getCupoMaximo();

    /**
     * Muestra la información del curso
     */
    void mostrarInfo();

    /**
     * Convierte el producto abstracto a modelo de base de datos
     */
    model.Curso toModel();
}
