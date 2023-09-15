package com.equipoa.servicewebapp.Entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Ocupaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    public Ocupaciones() {
    }

    public Ocupaciones(String nombre) {
        this.nombre = nombre;
    }

    public String getOcupacion() {
        return nombre;
    }

    public void setOcupacion(String nombre) {
        this.nombre = nombre;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
