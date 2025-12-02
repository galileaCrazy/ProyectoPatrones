package com.edulearn.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "modulos_curso")
public class ModuloCurso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "curso_id", nullable = false)
    private Integer cursoId;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "orden")
    private Integer orden;

    @Column(name = "duracion_horas")
    private Integer duracionHoras;

    @Column(name = "modulo_padre_id")
    private Integer moduloPadreId;

    @Column(name = "es_hoja")
    private Boolean esHoja; // true si no tiene hijos (Leaf), false si tiene hijos (Composite)

    @Column(name = "nivel")
    private Integer nivel; // 0 = raíz, 1 = nivel 1, etc.

    @Column(name = "estado")
    private String estado; // ACTIVO, INACTIVO, BORRADOR

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (estado == null) {
            estado = "ACTIVO";
        }
        if (esHoja == null) {
            esHoja = true;
        }
        if (nivel == null) {
            nivel = 0;
        }
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getCursoId() { return cursoId; }
    public void setCursoId(Integer cursoId) { this.cursoId = cursoId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }

    public Integer getDuracionHoras() { return duracionHoras; }
    public void setDuracionHoras(Integer duracionHoras) { this.duracionHoras = duracionHoras; }

    public Integer getModuloPadreId() { return moduloPadreId; }
    public void setModuloPadreId(Integer moduloPadreId) { this.moduloPadreId = moduloPadreId; }

    public Boolean getEsHoja() { return esHoja; }
    public void setEsHoja(Boolean esHoja) { this.esHoja = esHoja; }

    public Integer getNivel() { return nivel; }
    public void setNivel(Integer nivel) { this.nivel = nivel; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
