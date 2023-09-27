package com.equipoa.servicewebapp.Entidades;

import javax.persistence.Entity;
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
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int puntuacion;
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "cliente_emisor_id")
    private Usuario clienteEmisor; // el cliente que deja la calificación

    @ManyToOne
    @JoinColumn(name = "proveedor_receptor_id")
    private Usuario proveedorReceptor; // el proveedor que recibe la calificación
    
    @OneToOne
    @JoinColumn(name = "trabajo_id")
    private Trabajo trabajo; // el trabajo que está siendo calificado

    public Calificacion() {
    }

    public Calificacion(Long id, int puntuacion, String comentario, Usuario clienteEmisor, Usuario proveedorReceptor, Trabajo trabajo) {
        this.id = id;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.clienteEmisor = clienteEmisor;
        this.proveedorReceptor = proveedorReceptor;
        this.trabajo = trabajo;
    }

    public Trabajo getTrabajo() {
        return trabajo;
    }

    public void setTrabajo(Trabajo trabajo) {
        this.trabajo = trabajo;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Usuario getClienteEmisor() {
        return clienteEmisor;
    }

    public void setClienteEmisor(Usuario clienteEmisor) {
        this.clienteEmisor = clienteEmisor;
    }

    public Usuario getProveedorReceptor() {
        return proveedorReceptor;
    }

    public void setProveedorReceptor(Usuario proveedorReceptor) {
        this.proveedorReceptor = proveedorReceptor;
    }
}
