package com.edulearn.patterns.estructural.decorator.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO para mostrar información de decoradores al estudiante
 * Incluye gamificación, certificación y permisos según tipo de inscripción
 */
public class DecoradorEstudianteResponse {

    private Long cursoId;
    private String nombreCurso;
    private String modalidadInscripcion; // GRATUITA, PAGA, BECA

    // Gamificación total del curso (suma de todos los módulos)
    private int puntosDisponibles;
    private List<BadgeDisponible> badgesDisponibles;
    private int puntosObtenidos; // Puntos que el estudiante ha obtenido
    private List<String> badgesObtenidos; // Badges que el estudiante ha obtenido

    // Certificación
    private boolean certificadoDisponible; // Si el curso tiene certificado
    private String tipoCertificado;
    private boolean puedeDescargarCertificado; // Según modalidad de inscripción
    private String mensajeCertificado; // Mensaje informativo

    // Progreso
    private int modulosCompletados;
    private int totalModulos;
    private boolean cursoCompletado;

    public static class BadgeDisponible {
        private String nombre;
        private String moduloNombre;
        private boolean obtenido;

        public BadgeDisponible(String nombre, String moduloNombre, boolean obtenido) {
            this.nombre = nombre;
            this.moduloNombre = moduloNombre;
            this.obtenido = obtenido;
        }

        // Getters y Setters
        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public String getModuloNombre() { return moduloNombre; }
        public void setModuloNombre(String moduloNombre) { this.moduloNombre = moduloNombre; }

        public boolean isObtenido() { return obtenido; }
        public void setObtenido(boolean obtenido) { this.obtenido = obtenido; }
    }

    // Constructores
    public DecoradorEstudianteResponse() {
        this.badgesDisponibles = new ArrayList<>();
        this.badgesObtenidos = new ArrayList<>();
    }

    // Getters y Setters
    public Long getCursoId() { return cursoId; }
    public void setCursoId(Long cursoId) { this.cursoId = cursoId; }

    public String getNombreCurso() { return nombreCurso; }
    public void setNombreCurso(String nombreCurso) { this.nombreCurso = nombreCurso; }

    public String getModalidadInscripcion() { return modalidadInscripcion; }
    public void setModalidadInscripcion(String modalidadInscripcion) { this.modalidadInscripcion = modalidadInscripcion; }

    public int getPuntosDisponibles() { return puntosDisponibles; }
    public void setPuntosDisponibles(int puntosDisponibles) { this.puntosDisponibles = puntosDisponibles; }

    public List<BadgeDisponible> getBadgesDisponibles() { return badgesDisponibles; }
    public void setBadgesDisponibles(List<BadgeDisponible> badgesDisponibles) { this.badgesDisponibles = badgesDisponibles; }

    public int getPuntosObtenidos() { return puntosObtenidos; }
    public void setPuntosObtenidos(int puntosObtenidos) { this.puntosObtenidos = puntosObtenidos; }

    public List<String> getBadgesObtenidos() { return badgesObtenidos; }
    public void setBadgesObtenidos(List<String> badgesObtenidos) { this.badgesObtenidos = badgesObtenidos; }

    public boolean isCertificadoDisponible() { return certificadoDisponible; }
    public void setCertificadoDisponible(boolean certificadoDisponible) { this.certificadoDisponible = certificadoDisponible; }

    public String getTipoCertificado() { return tipoCertificado; }
    public void setTipoCertificado(String tipoCertificado) { this.tipoCertificado = tipoCertificado; }

    public boolean isPuedeDescargarCertificado() { return puedeDescargarCertificado; }
    public void setPuedeDescargarCertificado(boolean puedeDescargarCertificado) { this.puedeDescargarCertificado = puedeDescargarCertificado; }

    public String getMensajeCertificado() { return mensajeCertificado; }
    public void setMensajeCertificado(String mensajeCertificado) { this.mensajeCertificado = mensajeCertificado; }

    public int getModulosCompletados() { return modulosCompletados; }
    public void setModulosCompletados(int modulosCompletados) { this.modulosCompletados = modulosCompletados; }

    public int getTotalModulos() { return totalModulos; }
    public void setTotalModulos(int totalModulos) { this.totalModulos = totalModulos; }

    public boolean isCursoCompletado() { return cursoCompletado; }
    public void setCursoCompletado(boolean cursoCompletado) { this.cursoCompletado = cursoCompletado; }
}
