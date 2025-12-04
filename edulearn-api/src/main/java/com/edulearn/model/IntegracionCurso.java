package com.edulearn.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad para almacenar integraciones externas de un curso
 * (videoconferencias, repositorios de archivos, etc.)
 * Parte del patrón Adapter para sistemas externos
 */
@Entity
@Table(name = "integraciones_cursos")
public class IntegracionCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "curso_id", nullable = false)
    private Integer cursoId;

    @Column(name = "profesor_id", nullable = false)
    private Integer profesorId;

    @Column(nullable = false, length = 50)
    private String tipo; // VIDEOCONFERENCIA, REPOSITORIO

    @Column(nullable = false, length = 50)
    private String proveedor; // GOOGLE_MEET, ZOOM, GOOGLE_DRIVE, ONEDRIVE

    @Column(nullable = false, length = 200)
    private String nombre; // Nombre descriptivo de la integración

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "url_recurso", columnDefinition = "TEXT")
    private String urlRecurso; // URL de la reunión o carpeta

    @Column(name = "identificador_externo", length = 200)
    private String identificadorExterno; // ID en el sistema externo

    @Column(columnDefinition = "TEXT")
    private String configuracion; // JSON con configuración adicional

    @Column(nullable = false, length = 20)
    private String estado; // ACTIVA, INACTIVA, ELIMINADA

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "fecha_uso")
    private LocalDateTime fechaUso; // Última vez que se usó

    @Column(name = "veces_usado")
    private Integer vecesUsado = 0;

    // Constructores
    public IntegracionCurso() {
        this.fechaCreacion = LocalDateTime.now();
        this.estado = "ACTIVA";
    }

    public IntegracionCurso(Integer cursoId, Integer profesorId, String tipo, String proveedor, String nombre) {
        this();
        this.cursoId = cursoId;
        this.profesorId = profesorId;
        this.tipo = tipo;
        this.proveedor = proveedor;
        this.nombre = nombre;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCursoId() {
        return cursoId;
    }

    public void setCursoId(Integer cursoId) {
        this.cursoId = cursoId;
    }

    public Integer getProfesorId() {
        return profesorId;
    }

    public void setProfesorId(Integer profesorId) {
        this.profesorId = profesorId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
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

    public String getIdentificadorExterno() {
        return identificadorExterno;
    }

    public void setIdentificadorExterno(String identificadorExterno) {
        this.identificadorExterno = identificadorExterno;
    }

    public String getConfiguracion() {
        return configuracion;
    }

    public void setConfiguracion(String configuracion) {
        this.configuracion = configuracion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public LocalDateTime getFechaUso() {
        return fechaUso;
    }

    public void setFechaUso(LocalDateTime fechaUso) {
        this.fechaUso = fechaUso;
    }

    public Integer getVecesUsado() {
        return vecesUsado;
    }

    public void setVecesUsado(Integer vecesUsado) {
        this.vecesUsado = vecesUsado;
    }

    /**
     * Registrar uso de la integración
     */
    public void registrarUso() {
        this.fechaUso = LocalDateTime.now();
        this.vecesUsado++;
    }

    /**
     * Verificar si la integración está activa
     */
    public boolean estaActiva() {
        return "ACTIVA".equals(this.estado);
    }
}
