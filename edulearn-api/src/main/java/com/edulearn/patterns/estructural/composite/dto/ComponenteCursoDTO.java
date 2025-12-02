package com.edulearn.patterns.estructural.composite.dto;

import com.edulearn.patterns.estructural.composite.*;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para serializar el árbol del curso
 * Convierte la estructura Composite en JSON
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ComponenteCursoDTO {
    private Long id;
    private String nombre;
    private String tipo;
    private String descripcion;
    private Integer orden;
    private Integer duracionTotal;
    private Boolean esHoja;
    private List<ComponenteCursoDTO> hijos;

    // Campos específicos de ModuloCompuesto
    private Integer duracionEstimada;
    private String estado;
    private Integer cantidadElementos;

    // Campos específicos de MaterialHoja
    private String tipoMaterial;
    private String urlRecurso;
    private String archivoPath;
    private Integer duracionSegundos;
    private Boolean esObligatorio;

    // Campos específicos de EvaluacionHoja
    private String tipoEvaluacion;
    private BigDecimal puntajeMaximo;
    private Integer tiempoLimiteMinutos;
    private Integer intentosPermitidos;

    public ComponenteCursoDTO() {
    }

    /**
     * Convierte un ComponenteCurso (patrón Composite) a DTO
     */
    public static ComponenteCursoDTO fromComponente(ComponenteCurso componente) {
        if (componente == null) {
            return null;
        }

        ComponenteCursoDTO dto = new ComponenteCursoDTO();
        dto.id = componente.getId();
        dto.nombre = componente.getNombre();
        dto.tipo = componente.getTipo();
        dto.descripcion = componente.getDescripcion();
        dto.orden = componente.getOrden();
        dto.duracionTotal = componente.calcularDuracionTotal();
        dto.esHoja = componente.esHoja();

        // Si es un módulo compuesto
        if (componente instanceof ModuloCompuesto) {
            ModuloCompuesto modulo = (ModuloCompuesto) componente;
            dto.duracionEstimada = modulo.getDuracionEstimada();
            dto.estado = modulo.getEstado();
            dto.cantidadElementos = modulo.contarElementos();

            // Convertir los hijos recursivamente
            dto.hijos = modulo.obtenerHijos().stream()
                .map(ComponenteCursoDTO::fromComponente)
                .collect(Collectors.toList());
        }

        // Si es un material
        else if (componente instanceof MaterialHoja) {
            MaterialHoja material = (MaterialHoja) componente;
            dto.tipoMaterial = material.getTipoMaterial();
            dto.urlRecurso = material.getUrlRecurso();
            dto.archivoPath = material.getArchivoPath();
            dto.duracionSegundos = material.getDuracionSegundos();
            dto.esObligatorio = material.getEsObligatorio();
            dto.hijos = new ArrayList<>();
        }

        // Si es una evaluación
        else if (componente instanceof EvaluacionHoja) {
            EvaluacionHoja evaluacion = (EvaluacionHoja) componente;
            dto.tipoEvaluacion = evaluacion.getTipoEvaluacion();
            dto.puntajeMaximo = evaluacion.getPuntajeMaximo();
            dto.tiempoLimiteMinutos = evaluacion.getTiempoLimiteMinutos();
            dto.intentosPermitidos = evaluacion.getIntentosPermitidos();
            dto.estado = evaluacion.getEstado();
            dto.hijos = new ArrayList<>();
        }

        return dto;
    }

    /**
     * Convierte una lista de componentes a DTOs
     */
    public static List<ComponenteCursoDTO> fromComponentes(List<ComponenteCurso> componentes) {
        if (componentes == null) {
            return new ArrayList<>();
        }
        return componentes.stream()
            .map(ComponenteCursoDTO::fromComponente)
            .collect(Collectors.toList());
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Integer getDuracionTotal() {
        return duracionTotal;
    }

    public void setDuracionTotal(Integer duracionTotal) {
        this.duracionTotal = duracionTotal;
    }

    public Boolean getEsHoja() {
        return esHoja;
    }

    public void setEsHoja(Boolean esHoja) {
        this.esHoja = esHoja;
    }

    public List<ComponenteCursoDTO> getHijos() {
        return hijos;
    }

    public void setHijos(List<ComponenteCursoDTO> hijos) {
        this.hijos = hijos;
    }

    public Integer getDuracionEstimada() {
        return duracionEstimada;
    }

    public void setDuracionEstimada(Integer duracionEstimada) {
        this.duracionEstimada = duracionEstimada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getCantidadElementos() {
        return cantidadElementos;
    }

    public void setCantidadElementos(Integer cantidadElementos) {
        this.cantidadElementos = cantidadElementos;
    }

    public String getTipoMaterial() {
        return tipoMaterial;
    }

    public void setTipoMaterial(String tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }

    public String getUrlRecurso() {
        return urlRecurso;
    }

    public void setUrlRecurso(String urlRecurso) {
        this.urlRecurso = urlRecurso;
    }

    public String getArchivoPath() {
        return archivoPath;
    }

    public void setArchivoPath(String archivoPath) {
        this.archivoPath = archivoPath;
    }

    public Integer getDuracionSegundos() {
        return duracionSegundos;
    }

    public void setDuracionSegundos(Integer duracionSegundos) {
        this.duracionSegundos = duracionSegundos;
    }

    public Boolean getEsObligatorio() {
        return esObligatorio;
    }

    public void setEsObligatorio(Boolean esObligatorio) {
        this.esObligatorio = esObligatorio;
    }

    public String getTipoEvaluacion() {
        return tipoEvaluacion;
    }

    public void setTipoEvaluacion(String tipoEvaluacion) {
        this.tipoEvaluacion = tipoEvaluacion;
    }

    public BigDecimal getPuntajeMaximo() {
        return puntajeMaximo;
    }

    public void setPuntajeMaximo(BigDecimal puntajeMaximo) {
        this.puntajeMaximo = puntajeMaximo;
    }

    public Integer getTiempoLimiteMinutos() {
        return tiempoLimiteMinutos;
    }

    public void setTiempoLimiteMinutos(Integer tiempoLimiteMinutos) {
        this.tiempoLimiteMinutos = tiempoLimiteMinutos;
    }

    public Integer getIntentosPermitidos() {
        return intentosPermitidos;
    }

    public void setIntentosPermitidos(Integer intentosPermitidos) {
        this.intentosPermitidos = intentosPermitidos;
    }
}
