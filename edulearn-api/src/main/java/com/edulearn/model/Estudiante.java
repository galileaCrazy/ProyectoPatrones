package com.edulearn.model;

import jakarta.persistence.*;

@Entity
@Table(name = "estudiantes")
public class Estudiante {
    @Id
    @Column(name = "usuario_id")
    private Integer id;

    private String nombre;
    private String apellidos;
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}
