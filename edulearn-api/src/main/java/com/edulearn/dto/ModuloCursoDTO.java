package com.edulearn.dto;

import java.util.List;

/**
 * DTO para operaciones CRUD de Módulos de Curso
 * Incluye materiales asociados para persistencia completa
 */
public class ModuloCursoDTO {
    private Integer id;
    private Integer cursoId;
    private String nombre;
    private String descripcion;
    private Integer orden;
    private Integer duracionHoras;
    private Integer moduloPadreId;
    private Boolean esHoja;
    private Integer nivel;
    private String estado;
    private List<MaterialDTO> materiales;

    // Constructores
    public ModuloCursoDTO() {}

    public ModuloCursoDTO(Integer id, Integer cursoId, String nombre, String descripcion) {
        this.id = id;
        this.cursoId = cursoId;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCursoId() {
        return cursoId;
    }

    public void setCursoId(Integer cursoId) {
        this.cursoId = cursoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public Integer getDuracionHoras() {
        return duracionHoras;
    }

    public void setDuracionHoras(Integer duracionHoras) {
        this.duracionHoras = duracionHoras;
    }

    public Integer getModuloPadreId() {
        return moduloPadreId;
    }

    public void setModuloPadreId(Integer moduloPadreId) {
        this.moduloPadreId = moduloPadreId;
    }

    public Boolean getEsHoja() {
        return esHoja;
    }

    public void setEsHoja(Boolean esHoja) {
        this.esHoja = esHoja;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<MaterialDTO> getMateriales() {
        return materiales;
    }

    public void setMateriales(List<MaterialDTO> materiales) {
        this.materiales = materiales;
    }
}
