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
import javax.persistence.OneToOne;

/**
 *
 * @author Dell
 */
@Entity
public class Trabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    // cliente que solicita el trabajo
    private Long idCliente;

    
     // proveedor que realiza el trabajo
    private Long idProveedor;

    @OneToOne(mappedBy = "trabajo")
    private Calificacion calificacion; // la calificación para el trabajo

    //verificar argumentos de la entidad
    private String descripcion;
    private Date fechaInicio;
    private Date fechaFin;

    @Enumerated(EnumType.STRING)
    Estados estado;

    public Trabajo() {
    }

    public Trabajo(Long id, Long idCliente, Long idProveedor, Calificacion calificacion, String descripcion, Date fechaInicio, Date fechaFin, Estados estado) {
        this.id = id;
        this.idCliente = idCliente;
        this.idProveedor = idProveedor;
        this.calificacion = calificacion;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Calificacion getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Calificacion calificacion) {
        this.calificacion = calificacion;
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

    public Estados getEstado() {
        return estado;
    }

    public void setEstado(Estados estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return super.toString(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}

   
