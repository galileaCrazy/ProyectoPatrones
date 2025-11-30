package com.edulearn.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estudiantes")
public class Estudiante {
    @Id
    @Column(name = "usuario_id")
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private String matricula;

    @Column(name = "programa_academico")
    private String programaAcademico;

    private Integer semestre;

    @Column(name = "promedio_general")
    private Double promedioGeneral;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getCodigo() { return matricula; } // Alias para compatibilidad
    public void setCodigo(String codigo) { this.matricula = codigo; }

    // Métodos delegados a Usuario para compatibilidad
    public String getNombre() { return usuario != null ? usuario.getNombre() : null; }
    public void setNombre(String nombre) {
        if (usuario != null) usuario.setNombre(nombre);
    }
    public String getApellidos() { return usuario != null ? usuario.getApellidos() : null; }
    public void setApellidos(String apellidos) {
        if (usuario != null) usuario.setApellidos(apellidos);
    }
    public String getEmail() { return usuario != null ? usuario.getEmail() : null; }
    public void setEmail(String email) {
        if (usuario != null) usuario.setEmail(email);
    }
    public String getPasswordHash() { return usuario != null ? usuario.getPasswordHash() : null; }
    public void setPasswordHash(String passwordHash) {
        if (usuario != null) usuario.setPasswordHash(passwordHash);
    }

    public String getProgramaAcademico() { return programaAcademico; }
    public void setProgramaAcademico(String programaAcademico) { this.programaAcademico = programaAcademico; }
    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }
    public Double getPromedioGeneral() { return promedioGeneral; }
    public void setPromedioGeneral(Double promedioGeneral) { this.promedioGeneral = promedioGeneral; }
}
