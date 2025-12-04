package com.edulearn.patterns.estructural.proxy.dto;

/**
 * DTO para solicitud de material con Proxy
 *
 * Contiene la información necesaria para crear un proxy
 * de material con control de acceso y lazy loading
 */
public class MaterialProxyRequest {

    private Long materialId;
    private Long usuarioId;
    private String rolUsuario;
    private Boolean cargarContenido; // Si es true, carga el contenido inmediatamente

    // Constructores
    public MaterialProxyRequest() {
        this.cargarContenido = false; // Por defecto no carga el contenido (lazy)
    }

    public MaterialProxyRequest(Long materialId, Long usuarioId, String rolUsuario) {
        this.materialId = materialId;
        this.usuarioId = usuarioId;
        this.rolUsuario = rolUsuario;
        this.cargarContenido = false;
    }

    // Getters y Setters
    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(String rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public Boolean getCargarContenido() {
        return cargarContenido;
    }

    public void setCargarContenido(Boolean cargarContenido) {
        this.cargarContenido = cargarContenido;
    }

    @Override
    public String toString() {
        return "MaterialProxyRequest{" +
                "materialId=" + materialId +
                ", usuarioId=" + usuarioId +
                ", rolUsuario='" + rolUsuario + '\'' +
                ", cargarContenido=" + cargarContenido +
                '}';
    }
}
