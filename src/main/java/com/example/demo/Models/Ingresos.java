package com.example.demo.Models;

import jakarta.persistence.*;

import java.util.Date;



@Entity
public class Ingresos {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
    public Ingresos(String descripcion, double monto, String categoria) {
        this.fecha = new Date();
        this.descripcion = descripcion;
        this.monto = monto;
        this.categoria = categoria;
    }

    @PrePersist
    protected void onCreate() {
        if (fecha == null) {
            fecha = new Date();
        }
    }

    public Ingresos() {
    }



    //---------------Getters&Setters-----------------------------------------------------------------------------------------------------//
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
