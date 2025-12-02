package com.edulearn.service;

import com.edulearn.model.ContenidoEducativo;
import com.edulearn.patterns.creational.abstractfactory.ContenidoFactoryProvider;
import com.edulearn.patterns.creational.abstractfactory.IContenidoFactory;
import com.edulearn.patterns.creational.abstractfactory.IContenidoEducativo;
import com.edulearn.repository.ContenidoEducativoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Servicio para gestionar contenidos educativos
 * Utiliza el patrón Abstract Factory
 */
@Service
public class ContenidoEducativoService {

    @Autowired
    private ContenidoEducativoRepository repository;

    /**
     * Crear contenido educativo usando el patrón Abstract Factory
     */
    public ContenidoEducativo crearContenidoEducativo(String tipo, String nivel, String descripcion) {
        // Usar Abstract Factory para crear el contenido
        IContenidoFactory factory = ContenidoFactoryProvider.getFactory(tipo, nivel);
        
        // Crear el contenido usando la factory
        IContenidoEducativo contenido = factory.crearContenido();
        
        // Convertir a entidad y guardar
        ContenidoEducativo entidad = new ContenidoEducativo();
        entidad.setTipo(tipo);
        entidad.setNivel(nivel);
        entidad.setDescripcion(descripcion);
        entidad.setDuracionEstimada(60); // Por defecto 60 minutos
        entidad.setContenidoRenderizado(contenido.renderizar());
        entidad.setActivo(true);
        
        return repository.save(entidad);
    }

    /**
     * Obtener todos los contenidos educativos
     */
    public List<ContenidoEducativo> obtenerTodos() {
        return repository.findAll();
    }

    /**
     * Obtener contenido por ID
     */
    public ContenidoEducativo obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    /**
     * Crear demo de contenido educativo para diferentes tipos y niveles
     */
    public Map<String, Object> crearDemo(String tipo, String nivel) {
        // Usar Abstract Factory para crear el contenido
        IContenidoFactory factory = ContenidoFactoryProvider.getFactory(tipo, nivel);
        
        // Crear los diferentes tipos de contenido
        IContenidoEducativo video = factory.crearVideo();
        IContenidoEducativo documento = factory.crearDocumento();
        IContenidoEducativo quiz = factory.crearQuiz();
        
        return Map.of(
            "tipo", tipo,
            "nivel", nivel,
            "video", video.renderizar(),
            "documento", documento.renderizar(),
            "quiz", quiz.renderizar()
        );
    }
}
