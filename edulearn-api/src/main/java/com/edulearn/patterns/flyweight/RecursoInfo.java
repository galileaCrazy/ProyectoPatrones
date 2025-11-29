package com.edulearn.patterns.flyweight;

/**
 * PATRÓN FLYWEIGHT - Información del Recurso Compartido
 * DTO para devolver información sobre recursos Flyweight
 */
public class RecursoInfo {
    private String tipo;
    private String icono;
    private String colorPrimario;
    private String colorSecundario;
    private String plantilla;
    private boolean esCompartido;
    private int vecesReutilizado;

    public RecursoInfo(String tipo, String icono, String colorPrimario,
                      String colorSecundario, String plantilla) {
        this.tipo = tipo;
        this.icono = icono;
        this.colorPrimario = colorPrimario;
        this.colorSecundario = colorSecundario;
        this.plantilla = plantilla;
        this.esCompartido = true;
        this.vecesReutilizado = 0;
    }

    public void incrementarUso() {
        this.vecesReutilizado++;
    }

    // Getters y Setters
    public String getTipo() { return tipo; }
    public String getIcono() { return icono; }
    public String getColorPrimario() { return colorPrimario; }
    public String getColorSecundario() { return colorSecundario; }
    public String getPlantilla() { return plantilla; }
    public boolean isEsCompartido() { return esCompartido; }
    public int getVecesReutilizado() { return vecesReutilizado; }

    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setIcono(String icono) { this.icono = icono; }
    public void setColorPrimario(String colorPrimario) { this.colorPrimario = colorPrimario; }
    public void setColorSecundario(String colorSecundario) { this.colorSecundario = colorSecundario; }
    public void setPlantilla(String plantilla) { this.plantilla = plantilla; }
    public void setEsCompartido(boolean esCompartido) { this.esCompartido = esCompartido; }
    public void setVecesReutilizado(int vecesReutilizado) { this.vecesReutilizado = vecesReutilizado; }
}
