package com.example.demo.Models;


import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Entity

public class Gasto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long  id;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    private double monto;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha", nullable = false, updatable = false)
    private Date fecha;






    //---------------Constructor-----------------------------------------------------------------------------------------------------//
    public Gasto(String descripcion, double monto, String categoria) {
        this.fecha = new Date();
        this.descripcion = descripcion;
        this.monto = monto;
        this.categoria = categoria;
    }

    public Gasto() {
    }

    @PrePersist
    protected void onCreate() {
        if (fecha == null) {
            fecha = new Date();
        }
    }

    //---------------Getters&Setters-----------------------------------------------------------------------------------------------------//
    public Long getId() {
        return id;
    }

    public void setId(Long  id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

}
