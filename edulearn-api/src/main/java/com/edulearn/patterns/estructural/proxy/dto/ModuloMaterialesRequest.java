package com.edulearn.patterns.estructural.proxy.dto;

/**
 * DTO para solicitar todos los materiales de un módulo con Proxy
 */
public class ModuloMaterialesRequest {

    private Long moduloId;
    private Long usuarioId;
    private String rolUsuario;

    public ModuloMaterialesRequest() {
    }

    public ModuloMaterialesRequest(Long moduloId, Long usuarioId, String rolUsuario) {
        this.moduloId = moduloId;
        this.usuarioId = usuarioId;
        this.rolUsuario = rolUsuario;
    }

    public Long getModuloId() {
        return moduloId;
    }

    public void setModuloId(Long moduloId) {
        this.moduloId = moduloId;
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

    @Override
    public String toString() {
        return "ModuloMaterialesRequest{" +
                "moduloId=" + moduloId +
                ", usuarioId=" + usuarioId +
                ", rolUsuario='" + rolUsuario + '\'' +
                '}';
    }
}
