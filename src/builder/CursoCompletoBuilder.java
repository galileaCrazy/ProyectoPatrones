package builder;

import java.math.BigDecimal;

/**
 * CONCRETE BUILDER - Implementa cómo se construye cada parte del producto.
 * Esta clase implementa los pasos definidos en ICursoBuilder para
 * construir un CursoCompleto paso a paso.
 */
public class CursoCompletoBuilder implements ICursoBuilder {

    private CursoCompleto curso;
    private int contadorModulos;
    private int contadorClases;

    public CursoCompletoBuilder() {
        this.reset();
    }

    @Override
    public void reset() {
        this.curso = new CursoCompleto();
        this.contadorModulos = 0;
        this.contadorClases = 0;
    }

    @Override
    public ICursoBuilder setInformacionBasica(String codigo, String nombre, String descripcion) {
        curso.setCodigo(codigo);
        curso.setNombre(nombre);
        curso.setDescripcion(descripcion);
        return this;
    }

    @Override
    public ICursoBuilder setTipoCurso(CursoCompleto.TipoCurso tipoCurso) {
        curso.setTipoCurso(tipoCurso);
        return this;
    }

    @Override
    public ICursoBuilder setPeriodoAcademico(String periodo) {
        curso.setPeriodoAcademico(periodo);
        return this;
    }

    @Override
    public ICursoBuilder setInstructor(String nombreInstructor) {
        curso.setInstructor(nombreInstructor);
        return this;
    }

    @Override
    public ICursoBuilder setCupoMaximo(int cupoMaximo) {
        curso.setCupoMaximo(cupoMaximo);
        return this;
    }

    @Override
    public ICursoBuilder setDuracionYNivel(int duracionHoras, String nivel) {
        curso.setDuracionTotalHoras(duracionHoras);
        curso.setNivelDificultad(nivel);
        return this;
    }

    @Override
    public Modulo agregarModulo(String nombre, String descripcion) {
        contadorModulos++;
        Modulo modulo = new Modulo(nombre, descripcion, contadorModulos);
        modulo.setId(contadorModulos);
        curso.agregarModulo(modulo);
        contadorClases = 0; // Reiniciar contador de clases para el nuevo módulo
        return modulo;
    }

    @Override
    public ICursoBuilder agregarClase(Modulo modulo, String nombre, String descripcion,
                                       Clase.TipoClase tipo, int duracionMinutos) {
        contadorClases++;
        Clase clase = new Clase(nombre, descripcion, tipo);
        clase.setId(contadorClases);
        clase.setOrden(contadorClases);
        clase.setDuracionMinutos(duracionMinutos);
        modulo.agregarClase(clase);
        return this;
    }

    @Override
    public ICursoBuilder agregarEvaluacion(Modulo modulo, String nombre,
                                            EvaluacionModulo.TipoEvaluacion tipo,
                                            double pesoPorcentual) {
        EvaluacionModulo evaluacion = new EvaluacionModulo(
                nombre,
                tipo,
                new BigDecimal(pesoPorcentual)
        );
        evaluacion.setId(modulo.getEvaluaciones().size() + 1);
        modulo.agregarEvaluacion(evaluacion);
        return this;
    }

    @Override
    public CursoCompleto getResultado() {
        CursoCompleto resultado = this.curso;
        this.reset(); // Preparar para construir otro curso
        return resultado;
    }
}
