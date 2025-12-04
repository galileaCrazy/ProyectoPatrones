package com.edulearn.patterns.estructural.proxy.dto;

import com.edulearn.model.Material;

/**
 * DTO para respuesta de material con Proxy
 *
 * Contiene la información del material que se envía al cliente,
 * indicando si el contenido está cargado y proporcionando
 * acceso controlado a los recursos
 */
public class MaterialProxyResponse {

    private Long id;
    private String titulo;
    private String descripcion;
    private String tipoMaterial;
    private String urlRecurso;
    private Long tamanoBytes;
    private Integer duracionSegundos;
    private Boolean esObligatorio;
    private Boolean requiereVisualizacion;
    private String estado;
    private Integer orden;

    // Información del Proxy
    private Boolean contenidoCargado;
    private Boolean tieneAcceso;
    private Boolean esMaterialPesado;
    private String urlCarga; // URL para cargar el contenido cuando se necesite

    // Constructor vacío
    public MaterialProxyResponse() {
    }

    /**
     * Constructor desde Material
     * @param material Material entity
     */
    public MaterialProxyResponse(Material material) {
        this.id = material.getId();
        this.titulo = material.getTitulo();
        this.descripcion = material.getDescripcion();
        this.tipoMaterial = material.getTipoMaterial();
        this.urlRecurso = material.getUrlRecurso();
        this.tamanoBytes = material.getTamanoBytes();
        this.duracionSegundos = material.getDuracionSegundos();
        this.esObligatorio = material.getEsObligatorio();
        this.requiereVisualizacion = material.getRequiereVisualizacion();
        this.estado = material.getEstado();
        this.orden = material.getOrden();
        this.contenidoCargado = false;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    public Long getTamanoBytes() {
        return tamanoBytes;
    }

    public void setTamanoBytes(Long tamanoBytes) {
        this.tamanoBytes = tamanoBytes;
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

    public Boolean getRequiereVisualizacion() {
        return requiereVisualizacion;
    }

    public void setRequiereVisualizacion(Boolean requiereVisualizacion) {
        this.requiereVisualizacion = requiereVisualizacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Boolean getContenidoCargado() {
        return contenidoCargado;
    }

    public void setContenidoCargado(Boolean contenidoCargado) {
        this.contenidoCargado = contenidoCargado;
    }

    public Boolean getTieneAcceso() {
        return tieneAcceso;
    }

    public void setTieneAcceso(Boolean tieneAcceso) {
        this.tieneAcceso = tieneAcceso;
    }

    public Boolean getEsMaterialPesado() {
        return esMaterialPesado;
    }

    public void setEsMaterialPesado(Boolean esMaterialPesado) {
        this.esMaterialPesado = esMaterialPesado;
    }

    public String getUrlCarga() {
        return urlCarga;
    }

    public void setUrlCarga(String urlCarga) {
        this.urlCarga = urlCarga;
    }
}
