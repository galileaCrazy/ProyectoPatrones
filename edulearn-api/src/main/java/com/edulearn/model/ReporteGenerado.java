package com.edulearn.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportes_generados")
public class ReporteGenerado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipo_reporte", nullable = false)
    private String tipoReporte; // ESTUDIANTES, CURSOS, CALIFICACIONES

    @Column(name = "formato", nullable = false)
    private String formato; // PDF, EXCEL, HTML

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "contenido", columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "contenido_binario", columnDefinition = "LONGBLOB")
    private byte[] contenidoBinario;

    @Column(name = "ruta_archivo")
    private String rutaArchivo;

    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "generado_por")
    private Integer generadoPor;

    @Column(name = "fecha_generacion")
    private LocalDateTime fechaGeneracion;

    @Column(name = "parametros", columnDefinition = "TEXT")
    private String parametros;

    @Column(name = "estado")
    private String estado; // GENERADO, ERROR

    @PrePersist
    protected void onCreate() {
        fechaGeneracion = LocalDateTime.now();
        if (estado == null) {
            estado = "GENERADO";
        }
        if (generadoPor == null) {
            generadoPor = 1; // 1 = SYSTEM user
        }
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTipoReporte() { return tipoReporte; }
    public void setTipoReporte(String tipoReporte) { this.tipoReporte = tipoReporte; }

    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public String getRutaArchivo() { return rutaArchivo; }
    public void setRutaArchivo(String rutaArchivo) { this.rutaArchivo = rutaArchivo; }

    public Integer getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Integer usuarioId) { this.usuarioId = usuarioId; }

    public LocalDateTime getFechaGeneracion() { return fechaGeneracion; }
    public void setFechaGeneracion(LocalDateTime fechaGeneracion) { this.fechaGeneracion = fechaGeneracion; }

    public String getParametros() { return parametros; }
    public void setParametros(String parametros) { this.parametros = parametros; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public byte[] getContenidoBinario() { return contenidoBinario; }
    public void setContenidoBinario(byte[] contenidoBinario) { this.contenidoBinario = contenidoBinario; }

    public Integer getGeneradoPor() { return generadoPor; }
    public void setGeneradoPor(Integer generadoPor) { this.generadoPor = generadoPor; }
}
