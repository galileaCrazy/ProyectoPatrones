package com.edulearn.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "integraciones_externas")
public class IntegracionExterna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipo_sistema", nullable = false)
    private String tipoSistema; // ZOOM, GOOGLE_MEET, MS_TEAMS

    @Column(name = "nombre_configuracion", nullable = false)
    private String nombreConfiguracion;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "api_secret")
    private String apiSecret;

    @Column(name = "url_base")
    private String urlBase;

    @Column(name = "estado", nullable = false)
    private String estado; // ACTIVO, INACTIVO

    @Column(name = "curso_id")
    private Integer cursoId;

    @Column(name = "sala_reunion")
    private String salaReunion;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "ultima_sincronizacion")
    private LocalDateTime ultimaSincronizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        if (estado == null) {
            estado = "ACTIVO";
        }
    }

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTipoSistema() { return tipoSistema; }
    public void setTipoSistema(String tipoSistema) { this.tipoSistema = tipoSistema; }

    public String getNombreConfiguracion() { return nombreConfiguracion; }
    public void setNombreConfiguracion(String nombreConfiguracion) { this.nombreConfiguracion = nombreConfiguracion; }

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public String getApiSecret() { return apiSecret; }
    public void setApiSecret(String apiSecret) { this.apiSecret = apiSecret; }

    public String getUrlBase() { return urlBase; }
    public void setUrlBase(String urlBase) { this.urlBase = urlBase; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Integer getCursoId() { return cursoId; }
    public void setCursoId(Integer cursoId) { this.cursoId = cursoId; }

    public String getSalaReunion() { return salaReunion; }
    public void setSalaReunion(String salaReunion) { this.salaReunion = salaReunion; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getUltimaSincronizacion() { return ultimaSincronizacion; }
    public void setUltimaSincronizacion(LocalDateTime ultimaSincronizacion) { this.ultimaSincronizacion = ultimaSincronizacion; }
}
