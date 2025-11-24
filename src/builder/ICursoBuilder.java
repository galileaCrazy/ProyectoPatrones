package builder;

/**
 * BUILDER (Constructor Abstracto) - Define los pasos necesarios para construir el producto.
 * Esta interfaz declara todos los métodos que deben implementar los builders concretos
 * para construir un curso paso a paso.
 */
public interface ICursoBuilder {

    /**
     * Reinicia el builder para construir un nuevo curso
     */
    void reset();

    /**
     * Paso 1: Configura la información básica del curso
     * @param codigo Código único del curso
     * @param nombre Nombre del curso
     * @param descripcion Descripción del curso
     */
    ICursoBuilder setInformacionBasica(String codigo, String nombre, String descripcion);

    /**
     * Paso 2: Configura el tipo y modalidad del curso
     * @param tipoCurso Tipo de curso (presencial, virtual, híbrido)
     */
    ICursoBuilder setTipoCurso(CursoCompleto.TipoCurso tipoCurso);

    /**
     * Paso 3: Configura el periodo académico
     * @param periodo Identificador del periodo (ej: "2025-1")
     */
    ICursoBuilder setPeriodoAcademico(String periodo);

    /**
     * Paso 4: Configura el instructor del curso
     * @param nombreInstructor Nombre del instructor
     */
    ICursoBuilder setInstructor(String nombreInstructor);

    /**
     * Paso 5: Configura la capacidad del curso
     * @param cupoMaximo Número máximo de estudiantes
     */
    ICursoBuilder setCupoMaximo(int cupoMaximo);

    /**
     * Paso 6: Configura la duración y nivel del curso
     * @param duracionHoras Duración total en horas
     * @param nivel Nivel de dificultad (básico, intermedio, avanzado)
     */
    ICursoBuilder setDuracionYNivel(int duracionHoras, String nivel);

    /**
     * Paso 7: Agrega un módulo al curso
     * @param nombre Nombre del módulo
     * @param descripcion Descripción del módulo
     * @return El módulo creado para poder agregarle clases y evaluaciones
     */
    Modulo agregarModulo(String nombre, String descripcion);

    /**
     * Paso 8: Agrega una clase a un módulo específico
     * @param modulo Módulo al que pertenece la clase
     * @param nombre Nombre de la clase
     * @param descripcion Descripción de la clase
     * @param tipo Tipo de clase
     * @param duracionMinutos Duración en minutos
     */
    ICursoBuilder agregarClase(Modulo modulo, String nombre, String descripcion,
                                Clase.TipoClase tipo, int duracionMinutos);

    /**
     * Paso 9: Agrega una evaluación a un módulo específico
     * @param modulo Módulo al que pertenece la evaluación
     * @param nombre Nombre de la evaluación
     * @param tipo Tipo de evaluación
     * @param pesoPorcentual Peso en el total del curso
     */
    ICursoBuilder agregarEvaluacion(Modulo modulo, String nombre,
                                     EvaluacionModulo.TipoEvaluacion tipo,
                                     double pesoPorcentual);

    /**
     * Obtiene el producto construido
     * @return El curso completo construido
     */
    CursoCompleto getResultado();
}
