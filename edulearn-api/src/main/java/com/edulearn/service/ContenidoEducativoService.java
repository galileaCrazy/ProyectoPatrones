package com.edulearn.service;

import com.edulearn.model.ContenidoEducativo;
import com.edulearn.patterns.creational.abstract_factory.ContenidoFactoryProvider;
import com.edulearn.patterns.creational.abstract_factory.IContenidoFactory;
import com.edulearn.patterns.creational.abstract_factory.IContenidoEducativo;
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
     * Crear contenido educativo usando Abstract Factory
     */
    public ContenidoEducativo crearContenido(String nivel, String tipoContenido, Long cursoId) {
        // Obtener la factory apropiada según el nivel
        IContenidoFactory factory = ContenidoFactoryProvider.getFactory(nivel);

        // Crear el contenido específico
        IContenidoEducativo contenido;
        switch (tipoContenido.toUpperCase()) {
            case "VIDEO":
                contenido = factory.crearVideo();
                break;
            case "DOCUMENTO":
                contenido = factory.crearDocumento();
                break;
            case "QUIZ":
                contenido = factory.crearQuiz();
                break;
            default:
                throw new IllegalArgumentException("Tipo de contenido no soportado: " + tipoContenido);
        }

        // Renderizar y persistir en BD
        String renderizado = contenido.renderizar();

        ContenidoEducativo entity = new ContenidoEducativo(
            contenido.getTipo(),
            contenido.getNivel(),
            contenido.getDescripcion(),
            contenido.getDuracionEstimada(),
            renderizado
        );
        entity.setCursoId(cursoId);

        return repository.save(entity);
    }

    /**
     * Crear familia completa de contenidos (Video + Documento + Quiz) para un nivel
     */
    public List<ContenidoEducativo> crearFamiliaCompleta(String nivel, Long cursoId) {
        IContenidoFactory factory = ContenidoFactoryProvider.getFactory(nivel);

        // Crear los 3 tipos de contenido
        IContenidoEducativo video = factory.crearVideo();
        IContenidoEducativo documento = factory.crearDocumento();
        IContenidoEducativo quiz = factory.crearQuiz();

        // Persistir todos
        ContenidoEducativo videoEntity = new ContenidoEducativo(
            video.getTipo(), video.getNivel(), video.getDescripcion(),
            video.getDuracionEstimada(), video.renderizar()
        );
        videoEntity.setCursoId(cursoId);

        ContenidoEducativo documentoEntity = new ContenidoEducativo(
            documento.getTipo(), documento.getNivel(), documento.getDescripcion(),
            documento.getDuracionEstimada(), documento.renderizar()
        );
        documentoEntity.setCursoId(cursoId);

        ContenidoEducativo quizEntity = new ContenidoEducativo(
            quiz.getTipo(), quiz.getNivel(), quiz.getDescripcion(),
            quiz.getDuracionEstimada(), quiz.renderizar()
        );
        quizEntity.setCursoId(cursoId);

        repository.save(videoEntity);
        repository.save(documentoEntity);
        repository.save(quizEntity);

        return List.of(videoEntity, documentoEntity, quizEntity);
    }

    /**
     * Obtener todos los contenidos
     */
    public List<ContenidoEducativo> obtenerTodos() {
        return repository.findAll();
    }

    /**
     * Obtener contenidos por tipo
     */
    public List<ContenidoEducativo> obtenerPorTipo(String tipo) {
        return repository.findByTipo(tipo);
    }

    /**
     * Obtener contenidos por nivel
     */
    public List<ContenidoEducativo> obtenerPorNivel(String nivel) {
        return repository.findByNivel(nivel);
    }

    /**
     * Obtener contenidos por tipo y nivel
     */
    public List<ContenidoEducativo> obtenerPorTipoYNivel(String tipo, String nivel) {
        return repository.findByTipoAndNivel(tipo, nivel);
    }

    /**
     * Obtener contenidos por curso
     */
    public List<ContenidoEducativo> obtenerPorCurso(Long cursoId) {
        return repository.findByCursoId(cursoId);
    }

    /**
     * Obtener estadísticas
     */
    public Map<String, Object> obtenerEstadisticas() {
        long total = repository.count();
        long videos = repository.findByTipo("VIDEO").size();
        long documentos = repository.findByTipo("DOCUMENTO").size();
        long quizzes = repository.findByTipo("QUIZ").size();

        return Map.of(
            "total", total,
            "porTipo", Map.of(
                "VIDEO", videos,
                "DOCUMENTO", documentos,
                "QUIZ", quizzes
            ),
            "porNivel", Map.of(
                "BASICO", repository.findByNivel("BASICO").size(),
                "INTERMEDIO", repository.findByNivel("INTERMEDIO").size(),
                "AVANZADO", repository.findByNivel("AVANZADO").size()
            )
        );
    }
}
