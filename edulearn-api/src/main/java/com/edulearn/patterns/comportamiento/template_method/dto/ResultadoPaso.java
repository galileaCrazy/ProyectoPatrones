package com.edulearn.patterns.comportamiento.template_method.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * DTO para representar el resultado de un paso en el proceso de inscripción
 */
public class ResultadoPaso {
    
    private String nombre;
    private boolean exitoso;
    private String mensaje;
    private Map<String, String> detalles;
    private long duracionMs;
    private int orden;
    
    public ResultadoPaso() {
        this.detalles = new HashMap<>();
    }
    
    public ResultadoPaso(String nombre) {
        this.nombre = nombre;
        this.detalles = new HashMap<>();
    }
    
    public void agregarDetalle(String clave, String valor) {
        this.detalles.put(clave, valor);
    }
    
    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public boolean isExitoso() { return exitoso; }
    public void setExitoso(boolean exitoso) { this.exitoso = exitoso; }
    
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    
    public Map<String, String> getDetalles() { return detalles; }
    public void setDetalles(Map<String, String> detalles) { this.detalles = detalles; }
    
    public long getDuracionMs() { return duracionMs; }
    public void setDuracionMs(long duracionMs) { this.duracionMs = duracionMs; }
    
    public int getOrden() { return orden; }
    public void setOrden(int orden) { this.orden = orden; }
}
