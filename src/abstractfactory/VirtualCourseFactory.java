package abstractfactory;

/**
 * Fábrica Concreta: VirtualCourseFactory
 * Implementa CourseComponentFactory para crear familias de productos
 * específicos para cursos virtuales:
 * - CursoVirtual
 * - MaterialVirtual (VIDEO)
 * - EvaluacionVirtual (QUIZ)
 * @author USUARIO
 */
public class VirtualCourseFactory implements CourseComponentFactory {

    @Override
    public ICurso crearCurso(String codigo, String nombre, String descripcion,
                            int profesorId, String periodoAcademico) {
        System.out.println("✓ Creando Curso VIRTUAL...");
        return new CursoVirtual(codigo, nombre, descripcion, profesorId, periodoAcademico);
    }

    @Override
    public IMaterial crearMaterial(String nombre, String descripcion) {
        System.out.println("✓ Creando Material VIRTUAL (VIDEO)...");
        return new MaterialVirtual(nombre, descripcion);
    }

    @Override
    public IEvaluacion crearEvaluacion(String nombre, String descripcion) {
        System.out.println("✓ Creando Evaluación VIRTUAL (QUIZ)...");
        return new EvaluacionVirtual(nombre, descripcion);
    }

    @Override
    public String getTipoFactory() {
        return "Virtual";
    }
}
