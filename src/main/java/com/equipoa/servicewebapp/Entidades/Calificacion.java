/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.equipoa.servicewebapp.Entidades;

import javax.persistence.Entity;
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

    public Calificacion() {
    }

    public Calificacion(Long id, int puntuacion, String comentario, Usuario clienteEmisor, Usuario proveedorReceptor) {
        this.id = id;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.clienteEmisor = clienteEmisor;
        this.proveedorReceptor = proveedorReceptor;
    }

}
