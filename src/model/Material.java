package model;

import java.util.Date;

/**
 * Modelo que representa un Material educativo
 * @author USUARIO
 */
public class Material {
    private int id;
    private int moduloId;
    private TipoMaterial tipo;
    private String nombre;
    private String descripcion;
    private String urlRecurso;
    private Long tamanioBytes;
    private Integer duracionSegundos;
    private int orden;
    private boolean requiereVisualizacion;
    private boolean activo;
    private Date createdAt;

    /**
     * Enumeración para los tipos de material
     */
    public enum TipoMaterial {
        VIDEO("video"),
        PDF("pdf"),
        DOCUMENTO("documento"),
        ENLACE("enlace"),
        IMAGEN("imagen"),
        AUDIO("audio");

        private String valor;

        TipoMaterial(String valor) {
            this.valor = valor;
        }

        public String getValor() {
            return valor;
        }

        public static TipoMaterial fromString(String text) {
            for (TipoMaterial tipo : TipoMaterial.values()) {
                if (tipo.valor.equalsIgnoreCase(text)) {
                    return tipo;
                }
            }
            throw new IllegalArgumentException("No hay tipo de material con valor: " + text);
        }
    }

    // Constructores
    public Material() {
        this.orden = 0;
        this.requiereVisualizacion = false;
        this.activo = true;
    }

    public Material(int moduloId, TipoMaterial tipo, String nombre, String descripcion) {
        this();
        this.moduloId = moduloId;
        this.tipo = tipo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getModuloId() {
        return moduloId;
    }

    public void setModuloId(int moduloId) {
        this.moduloId = moduloId;
    }

    public TipoMaterial getTipo() {
        return tipo;
    }

    public void setTipo(TipoMaterial tipo) {
        this.tipo = tipo;
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

    public String getUrlRecurso() {
        return urlRecurso;
    }

    public void setUrlRecurso(String urlRecurso) {
        this.urlRecurso = urlRecurso;
    }

    public Long getTamanioBytes() {
        return tamanioBytes;
    }

    public void setTamanioBytes(Long tamanioBytes) {
        this.tamanioBytes = tamanioBytes;
    }

    public Integer getDuracionSegundos() {
        return duracionSegundos;
    }

    public void setDuracionSegundos(Integer duracionSegundos) {
        this.duracionSegundos = duracionSegundos;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public boolean isRequiereVisualizacion() {
        return requiereVisualizacion;
    }

    public void setRequiereVisualizacion(boolean requiereVisualizacion) {
        this.requiereVisualizacion = requiereVisualizacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", tipo=" + tipo +
                ", nombre='" + nombre + '\'' +
                ", urlRecurso='" + urlRecurso + '\'' +
                ", orden=" + orden +
                '}';
    }
}
