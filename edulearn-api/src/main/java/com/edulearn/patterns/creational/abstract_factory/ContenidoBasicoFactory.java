package com.edulearn.patterns.creational.abstract_factory;

/**
 * Factory concreta: Crea contenidos de nivel BÁSICO
 */
public class ContenidoBasicoFactory implements IContenidoFactory {

    @Override
    public IContenidoEducativo crearVideo() {
        return new VideoBasico();
    }

    @Override
    public IContenidoEducativo crearDocumento() {
        return new DocumentoBasico();
    }

    @Override
    public IContenidoEducativo crearQuiz() {
        return new QuizBasico();
    }

    @Override
    public String getNivel() {
        return "BASICO";
    }
}
