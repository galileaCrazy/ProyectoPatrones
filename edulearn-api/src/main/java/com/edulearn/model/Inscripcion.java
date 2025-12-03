package com.edulearn.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "inscripciones")
public class Inscripcion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "estudiante_id")
    private Integer estudianteId;

    @Column(name = "curso_id")
    private Integer cursoId;

    @Column(name = "fecha_inscripcion")
    private LocalDate fechaInscripcion;

    @Column(name = "modalidad", length = 20)
    private String modalidad; // GRATUITA, PAGA, BECA

    @Column(name = "estado_inscripcion", length = 50)
    private String estadoInscripcion; // Activa, Pendiente de Pago, Pendiente de Aprobación/Documentación, Completada

    @Column(name = "certificado_garantizado")
    private Boolean certificadoGarantizado;

    @Column(name = "tipo_beca", length = 50)
    private String tipoBeca; // ACADEMICA, DEPORTIVA, SOCIECONOMICA, CULTURAL, TECNM

    @Column(name = "codigo_beca", length = 100)
    private String codigoBeca;

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago; // TARJETA, TRANSFERENCIA, PAYPAL

    @Column(name = "transaccion_id", length = 100)
    private String transaccionId;

    @Column(name = "monto_pagado", precision = 10, scale = 2)
    private BigDecimal montoPagado;

    @Column(name = "motivo_rechazo", length = 500)
    private String motivoRechazo;

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Integer estudianteId) { this.estudianteId = estudianteId; }

    public Integer getCursoId() { return cursoId; }
    public void setCursoId(Integer cursoId) { this.cursoId = cursoId; }

    public LocalDate getFechaInscripcion() { return fechaInscripcion; }
    public void setFechaInscripcion(LocalDate fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }

    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }

    public String getEstadoInscripcion() { return estadoInscripcion; }
    public void setEstadoInscripcion(String estadoInscripcion) { this.estadoInscripcion = estadoInscripcion; }

    public Boolean getCertificadoGarantizado() { return certificadoGarantizado; }
    public void setCertificadoGarantizado(Boolean certificadoGarantizado) { this.certificadoGarantizado = certificadoGarantizado; }

    public String getTipoBeca() { return tipoBeca; }
    public void setTipoBeca(String tipoBeca) { this.tipoBeca = tipoBeca; }

    public String getCodigoBeca() { return codigoBeca; }
    public void setCodigoBeca(String codigoBeca) { this.codigoBeca = codigoBeca; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getTransaccionId() { return transaccionId; }
    public void setTransaccionId(String transaccionId) { this.transaccionId = transaccionId; }

    public BigDecimal getMontoPagado() { return montoPagado; }
    public void setMontoPagado(BigDecimal montoPagado) { this.montoPagado = montoPagado; }

    public String getMotivoRechazo() { return motivoRechazo; }
    public void setMotivoRechazo(String motivoRechazo) { this.motivoRechazo = motivoRechazo; }
}
