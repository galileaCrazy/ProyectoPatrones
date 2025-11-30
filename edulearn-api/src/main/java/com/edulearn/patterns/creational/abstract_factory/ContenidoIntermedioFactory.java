package com.edulearn.patterns.creational.abstract_factory;

/**
 * Factory concreta: Crea contenidos de nivel INTERMEDIO
 */
public class ContenidoIntermedioFactory implements IContenidoFactory {

    @Override
    public IContenidoEducativo crearVideo() {
        return new VideoIntermedio();
    }

    @Override
    public IContenidoEducativo crearDocumento() {
        return new DocumentoIntermedio();
    }

    @Override
    public IContenidoEducativo crearQuiz() {
        return new QuizIntermedio();
    }

    @Override
    public String getNivel() {
        return "INTERMEDIO";
    }
}
