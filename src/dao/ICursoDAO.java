package dao;

import model.Curso;
import java.util.List;

/**
 * Interfaz que define las operaciones CRUD para Curso
 * @author USUARIO
 */
public interface ICursoDAO {
    /**
     * Inserta un nuevo curso en la base de datos
     * @param curso objeto Curso a insertar
     * @return id del curso insertado, -1 si falla
     */
    int insertar(Curso curso);

    /**
     * Actualiza un curso existente
     * @param curso objeto Curso con datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    boolean actualizar(Curso curso);

    /**
     * Elimina un curso por su ID
     * @param id identificador del curso
     * @return true si se eliminó correctamente, false en caso contrario
     */
    boolean eliminar(int id);

    /**
     * Obtiene un curso por su ID
     * @param id identificador del curso
     * @return objeto Curso si existe, null en caso contrario
     */
    Curso obtenerPorId(int id);

    /**
     * Obtiene un curso por su código
     * @param codigo código del curso
     * @return objeto Curso si existe, null en caso contrario
     */
    Curso obtenerPorCodigo(String codigo);

    /**
     * Obtiene todos los cursos
     * @return lista de cursos
     */
    List<Curso> obtenerTodos();

    /**
     * Obtiene cursos por tipo
     * @param tipoCurso tipo de curso (presencial, virtual, hibrido)
     * @return lista de cursos del tipo especificado
     */
    List<Curso> obtenerPorTipo(Curso.TipoCurso tipoCurso);
}
