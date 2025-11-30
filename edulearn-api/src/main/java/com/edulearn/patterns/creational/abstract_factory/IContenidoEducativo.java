package com.edulearn.patterns.creational.abstract_factory;

/**
 * Interfaz base para todos los contenidos educativos
 */
public interface IContenidoEducativo {
    String getTipo();
    String getNivel();
    String getDescripcion();
    int getDuracionEstimada(); // en minutos
    String renderizar();
}
