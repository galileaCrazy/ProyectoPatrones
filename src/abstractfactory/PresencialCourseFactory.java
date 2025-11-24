package abstractfactory;

/**
 * Fábrica Concreta: PresencialCourseFactory
 * Implementa CourseComponentFactory para crear familias de productos
 * específicos para cursos presenciales:
 * - CursoPresencial
 * - MaterialPresencial (PDF)
 * - EvaluacionPresencial (EXAMEN)
 * @author USUARIO
 */
public class PresencialCourseFactory implements CourseComponentFactory {

    @Override
    public ICurso crearCurso(String codigo, String nombre, String descripcion,
                            int profesorId, String periodoAcademico) {
        System.out.println("✓ Creando Curso PRESENCIAL...");
        return new CursoPresencial(codigo, nombre, descripcion, profesorId, periodoAcademico);
    }

    @Override
    public IMaterial crearMaterial(String nombre, String descripcion) {
        System.out.println("✓ Creando Material PRESENCIAL (PDF)...");
        return new MaterialPresencial(nombre, descripcion);
    }

    @Override
    public IEvaluacion crearEvaluacion(String nombre, String descripcion) {
        System.out.println("✓ Creando Evaluación PRESENCIAL (EXAMEN)...");
        return new EvaluacionPresencial(nombre, descripcion);
    }

    @Override
    public String getTipoFactory() {
        return "Presencial";
    }
}
