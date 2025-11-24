package abstractfactory;

/**
 * Fábrica Concreta: HibridoCourseFactory
 * Implementa CourseComponentFactory para crear familias de productos
 * específicos para cursos híbridos:
 * - CursoHibrido
 * - MaterialHibrido (DOCUMENTO)
 * - EvaluacionHibrida (PROYECTO)
 * @author USUARIO
 */
public class HibridoCourseFactory implements CourseComponentFactory {

    @Override
    public ICurso crearCurso(String codigo, String nombre, String descripcion,
                            int profesorId, String periodoAcademico) {
        System.out.println("✓ Creando Curso HÍBRIDO...");
        return new CursoHibrido(codigo, nombre, descripcion, profesorId, periodoAcademico);
    }

    @Override
    public IMaterial crearMaterial(String nombre, String descripcion) {
        System.out.println("✓ Creando Material HÍBRIDO (DOCUMENTO)...");
        return new MaterialHibrido(nombre, descripcion);
    }

    @Override
    public IEvaluacion crearEvaluacion(String nombre, String descripcion) {
        System.out.println("✓ Creando Evaluación HÍBRIDA (PROYECTO)...");
        return new EvaluacionHibrida(nombre, descripcion);
    }

    @Override
    public String getTipoFactory() {
        return "Híbrido";
    }
}
