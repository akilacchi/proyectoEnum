package com.equipoa.servicewebapp.Entidades;

import com.equipoa.servicewebapp.Enum.Estados;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author Dell
 */
@Entity
public class Trabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id") // cliente que solicita el trabajo
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "proveedor_id") // proveedor que realiza el trabajo
    private Usuario proveedor;

    //verificar argumentos de la entidad
    private String descripcion;
    private Date fechaInicio;
    private Date fechaFin;
    
    @Enumerated(EnumType.STRING)
    Estados estado;

    public Trabajo(Long id, Usuario cliente, Usuario proveedor, String descripcion, Date fechaInicio, Date fechaFin, Estados estado) {
        this.id = id;
        this.cliente = cliente;
        this.proveedor = proveedor;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }
    

  
    public Trabajo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public Usuario getProveedor() {
        return proveedor;
    }

    public void setProveedor(Usuario proveedor) {
        this.proveedor = proveedor;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
}
