/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.}
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
 */
package com.equipoa.servicewebapp.Servicios;

import com.equipoa.servicewebapp.Entidades.Trabajo;
import com.equipoa.servicewebapp.Entidades.Usuario;
import com.equipoa.servicewebapp.Enum.Estados;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Repositorios.TrabajoRepositorio;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author alejandrasuarez
 */

@Service
public class TrabajoServicio {

    @Autowired
    TrabajoRepositorio trabajoRepositorio;

    @Transactional
    public void registrarTrabajo(Usuario cliente, Usuario proveedor, String descripcion, Date fechaInicio, Date fechaFin, Estados estado) throws MiException {
        validar(cliente, proveedor, descripcion, fechaInicio, fechaFin);
        Trabajo trabajo = new Trabajo();

        trabajo.setCliente(cliente);
        trabajo.setProveedor(proveedor);
        trabajo.setDescripcion(descripcion);
        trabajo.setFechaInicio(fechaInicio);
        trabajo.setFechaFin(fechaFin);
        trabajo.setEstado(estado);

        trabajoRepositorio.save(trabajo);

    }

    @Transactional
    public void actualizarEstadoTrabajo(Long id, Estados estados) throws MiException {
        if (id < 1) {
            throw new MiException("Id incorrecto");
        }
        Optional<Trabajo> respuestaTrabajo = trabajoRepositorio.findById(id);

        if (respuestaTrabajo.isPresent()) {
            Trabajo trabajo = respuestaTrabajo.get();
            if (!trabajo.getEstado().toString().equals(estados.toString())) {
                trabajo.setEstado(estados);

                trabajoRepositorio.save(trabajo);
            } else {
                throw new MiException("Estado no ha cambiado");
            }
        }
    }

    @Transactional
    public void actualizarTrabajo(Long id, Usuario cliente, Usuario proveedor, String descripcion, Date fechaInicio, Date fechaFin, Estados estado) throws MiException {
        validar(cliente, proveedor, descripcion, fechaInicio, fechaFin);

        if (id < 1) {
            throw new MiException("Id incorrecto");
        }

        Optional<Trabajo> respuestaTrabajo = trabajoRepositorio.findById(id);

        if (respuestaTrabajo.isPresent()) {
            Trabajo trabajo = respuestaTrabajo.get();
            trabajo.setCliente(cliente);
            trabajo.setProveedor(proveedor);
            trabajo.setDescripcion(descripcion);
            trabajo.setFechaInicio(fechaInicio);
            trabajo.setFechaFin(fechaFin);
            trabajo.setEstado(estado);

            trabajoRepositorio.save(trabajo);
        } else {

            throw new MiException("El trabajo que desea actualizar no existe en la base de datos");
        }
    }

    @Transactional(readOnly = true)
    public List<Trabajo> listaTrabajosPorUsuario(String email) {
        return trabajoRepositorio.buscarTrabajosPorUsuario(email);

    }

    @Transactional(readOnly = true)
    public List<Trabajo> listaTrabajos() {
        return trabajoRepositorio.findAll();
    }

    public void validar(Usuario cliente, Usuario proveedor, String descripcion, Date fechaInicio, Date fechaFin) throws MiException {

        if (cliente == null) {
            throw new MiException("Cliente no puede ser nulo");
        }
        if (proveedor == null) {
            throw new MiException("Proveedor no puede ser nulo");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new MiException("Debe agregar una descripción del trabajo");
        }
        if (fechaInicio == null) {
            throw new MiException("Debe agregar fecha de inicio");
        }
        if (fechaFin == null) {
            throw new MiException("Debe agregar fecha final");
        }
    }


}
