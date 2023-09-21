package com.equipoa.servicewebapp.Entidades;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Ocupaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public String nombre;

    // Relación muchos-a-muchos con Proveedor
    @OneToMany(mappedBy = "ocupacion")
    private List<Usuario> proveedores;

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

    public List<Usuario> getProveedores() {
        return proveedores;
    }

    public void setProveedores(List<Usuario> proveedores) {
        this.proveedores = proveedores;
    }
    
}
