package com.edulearn.patterns.creational.abstract_factory;

/**
 * Factory concreta: Crea contenidos de nivel AVANZADO
 */
public class ContenidoAvanzadoFactory implements IContenidoFactory {

    @Override
    public IContenidoEducativo crearVideo() {
        return new VideoAvanzado();
    }

    @Override
    public IContenidoEducativo crearDocumento() {
        return new DocumentoAvanzado();
    }

    @Override
    public IContenidoEducativo crearQuiz() {
        return new QuizAvanzado();
    }

    @Override
    public String getNivel() {
        return "AVANZADO";
    }
}
