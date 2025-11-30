package com.edulearn.service;

import com.edulearn.model.CursoBuilder;
import com.edulearn.patterns.creational.builder.CursoBuilderProduct;
import com.edulearn.patterns.creational.builder.CursoDirector;
import com.edulearn.repository.CursoBuilderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Servicio para gestionar cursos usando el patrón Builder
 */
@Service
public class CursoBuilderService {

    @Autowired
    private CursoBuilderRepository repository;

    private final CursoDirector director = new CursoDirector();

    /**
     * Crear curso usando Builder personalizado
     */
    public CursoBuilder crearCursoPersonalizado(Map<String, Object> parametros) {
        com.edulearn.patterns.creational.builder.CursoBuilder builder =
            new com.edulearn.patterns.creational.builder.CursoBuilder();

        // Aplicar parámetros del mapa
        if (parametros.containsKey("nombre")) {
            builder.setNombre((String) parametros.get("nombre"));
        }
        if (parametros.containsKey("codigo")) {
            builder.setCodigo((String) parametros.get("codigo"));
        }
        if (parametros.containsKey("descripcion")) {
            builder.setDescripcion((String) parametros.get("descripcion"));
        }
        if (parametros.containsKey("modalidad")) {
            builder.setModalidad((String) parametros.get("modalidad"));
        }
        if (parametros.containsKey("nivelDificultad")) {
            builder.setNivelDificultad((String) parametros.get("nivelDificultad"));
        }
        if (parametros.containsKey("duracionHoras")) {
            builder.setDuracionHoras((Integer) parametros.get("duracionHoras"));
        }
        if (parametros.containsKey("cupoMaximo")) {
            builder.setCupoMaximo((Integer) parametros.get("cupoMaximo"));
        }
        if (parametros.containsKey("precio")) {
            builder.setPrecio(((Number) parametros.get("precio")).doubleValue());
        }
        if (parametros.containsKey("categoria")) {
            builder.setCategoria((String) parametros.get("categoria"));
        }
        if (parametros.containsKey("incluyeCertificado")) {
            builder.setIncluyeCertificado((Boolean) parametros.get("incluyeCertificado"));
        }
        if (parametros.containsKey("incluyeVideoLectures")) {
            builder.setIncluyeVideoLectures((Boolean) parametros.get("incluyeVideoLectures"));
        }
        if (parametros.containsKey("incluyeEvaluaciones")) {
            builder.setIncluyeEvaluaciones((Boolean) parametros.get("incluyeEvaluaciones"));
        }
        if (parametros.containsKey("incluyeProyectoFinal")) {
            builder.setIncluyeProyectoFinal((Boolean) parametros.get("incluyeProyectoFinal"));
        }
        if (parametros.containsKey("requisitosPrevios")) {
            builder.setRequisitosPrevios((String) parametros.get("requisitosPrevios"));
        }
        if (parametros.containsKey("objetivos")) {
            builder.setObjetivos((String) parametros.get("objetivos"));
        }
        if (parametros.containsKey("estado")) {
            builder.setEstado((String) parametros.get("estado"));
        }

        // Construir el producto
        CursoBuilderProduct producto = builder.build();

        // Convertir a entidad JPA y persistir
        return persistirCurso(producto, "CUSTOM");
    }

    /**
     * Crear curso básico usando Director
     */
    public CursoBuilder crearCursoBasico(String nombre) {
        CursoBuilderProduct producto = director.construirCursoBasico(nombre);
        return persistirCurso(producto, "BASICO");
    }

    /**
     * Crear curso premium usando Director
     */
    public CursoBuilder crearCursoPremium(String nombre, String categoria) {
        CursoBuilderProduct producto = director.construirCursoPremium(nombre, categoria);
        return persistirCurso(producto, "PREMIUM");
    }

    /**
     * Crear curso virtual usando Director
     */
    public CursoBuilder crearCursoVirtual(String nombre, Integer duracion) {
        CursoBuilderProduct producto = director.construirCursoVirtual(nombre, duracion);
        return persistirCurso(producto, "VIRTUAL");
    }

    /**
     * Crear curso intensivo usando Director
     */
    public CursoBuilder crearCursoIntensivo(String nombre, LocalDate fechaInicio) {
        CursoBuilderProduct producto = director.construirCursoIntensivo(nombre, fechaInicio);
        return persistirCurso(producto, "INTENSIVO");
    }

    /**
     * Crear curso gratuito usando Director
     */
    public CursoBuilder crearCursoGratuito(String nombre, String categoria) {
        CursoBuilderProduct producto = director.construirCursoGratuito(nombre, categoria);
        return persistirCurso(producto, "GRATUITO");
    }

    /**
     * Crear curso corporativo usando Director
     */
    public CursoBuilder crearCursoCorporativo(
        String nombre, Integer duracion, Integer cupo, LocalDate fechaInicio
    ) {
        CursoBuilderProduct producto = director.construirCursoCorporativo(
            nombre, duracion, cupo, fechaInicio
        );
        return persistirCurso(producto, "CORPORATIVO");
    }

    /**
     * Convertir producto del Builder a entidad JPA y persistir
     */
    private CursoBuilder persistirCurso(CursoBuilderProduct producto, String tipoConstruccion) {
        CursoBuilder entity = new CursoBuilder();
        entity.setNombre(producto.getNombre());
        entity.setCodigo(producto.getCodigo());
        entity.setDescripcion(producto.getDescripcion());
        entity.setModalidad(producto.getModalidad());
        entity.setNivelDificultad(producto.getNivelDificultad());
        entity.setDuracionHoras(producto.getDuracionHoras());
        entity.setCupoMaximo(producto.getCupoMaximo());
        entity.setPrecio(producto.getPrecio());
        entity.setCategoria(producto.getCategoria());
        entity.setIncluyeCertificado(producto.getIncluyeCertificado());
        entity.setIncluyeVideoLectures(producto.getIncluyeVideoLectures());
        entity.setIncluyeEvaluaciones(producto.getIncluyeEvaluaciones());
        entity.setIncluyeProyectoFinal(producto.getIncluyeProyectoFinal());
        entity.setRequisitosPrevios(producto.getRequisitosPrevios());
        entity.setObjetivos(producto.getObjetivos());
        entity.setFechaInicio(producto.getFechaInicio());
        entity.setFechaFin(producto.getFechaFin());
        entity.setEstado(producto.getEstado());
        entity.setTipoConstruccion(tipoConstruccion);

        return repository.save(entity);
    }

    /**
     * Obtener todos los cursos
     */
    public List<CursoBuilder> obtenerTodos() {
        return repository.findAll();
    }

    /**
     * Obtener por modalidad
     */
    public List<CursoBuilder> obtenerPorModalidad(String modalidad) {
        return repository.findByModalidad(modalidad);
    }

    /**
     * Obtener por nivel
     */
    public List<CursoBuilder> obtenerPorNivel(String nivel) {
        return repository.findByNivelDificultad(nivel);
    }

    /**
     * Obtener por tipo de construcción
     */
    public List<CursoBuilder> obtenerPorTipoConstruccion(String tipo) {
        return repository.findByTipoConstruccion(tipo);
    }

    /**
     * Obtener estadísticas
     */
    public Map<String, Object> obtenerEstadisticas() {
        long total = repository.count();

        return Map.of(
            "total", total,
            "porTipoConstruccion", Map.of(
                "BASICO", repository.findByTipoConstruccion("BASICO").size(),
                "PREMIUM", repository.findByTipoConstruccion("PREMIUM").size(),
                "VIRTUAL", repository.findByTipoConstruccion("VIRTUAL").size(),
                "INTENSIVO", repository.findByTipoConstruccion("INTENSIVO").size(),
                "GRATUITO", repository.findByTipoConstruccion("GRATUITO").size(),
                "CORPORATIVO", repository.findByTipoConstruccion("CORPORATIVO").size(),
                "CUSTOM", repository.findByTipoConstruccion("CUSTOM").size()
            ),
            "porModalidad", Map.of(
                "PRESENCIAL", repository.findByModalidad("PRESENCIAL").size(),
                "VIRTUAL", repository.findByModalidad("VIRTUAL").size(),
                "HIBRIDO", repository.findByModalidad("HIBRIDO").size()
            ),
            "porNivel", Map.of(
                "BASICO", repository.findByNivelDificultad("BASICO").size(),
                "INTERMEDIO", repository.findByNivelDificultad("INTERMEDIO").size(),
                "AVANZADO", repository.findByNivelDificultad("AVANZADO").size()
            )
        );
    }
}
