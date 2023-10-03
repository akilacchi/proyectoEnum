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

import com.equipoa.servicewebapp.Entidades.Calificacion;
import com.equipoa.servicewebapp.Entidades.Notificaciones;
import com.equipoa.servicewebapp.Entidades.Trabajo;
import com.equipoa.servicewebapp.Entidades.Usuario;
import com.equipoa.servicewebapp.Enum.Estados;
import com.equipoa.servicewebapp.Enum.Rol;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Repositorios.CalificacionRepositorio;
import com.equipoa.servicewebapp.Repositorios.TrabajoRepositorio;
import com.equipoa.servicewebapp.Repositorios.UsuarioRepositorio;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;

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

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    UsuarioServicio usuarioServicio;

    @Autowired
    CalificacionRepositorio calificacionRepositorio;

    @Autowired
    NotificacionServicio notificacionServicio;


    @Transactional
    public void registrarTrabajo(String descripcion, Date fechaInicio, Estados estado, HttpSession session, Long idProveedor) throws MiException {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado != null) {
            Long cliente = logueado.getID();

            System.out.println("Este es el email del cliente logueado: " + logueado.getEmail());

            validar(descripcion, fechaInicio);

            Trabajo trabajo = new Trabajo();
            trabajo.setIdProveedor(idProveedor);
            trabajo.setIdCliente(cliente);
            trabajo.setDescripcion(descripcion);
            trabajo.setFechaInicio(fechaInicio);
            trabajo.setEstado(estado);

//              Creacion de notificacion automatica
            String mensaje = ("Solicitud de trabajo recivida por cliente " + logueado.getName() + " con el mensaje:\n" + descripcion);
            notificacionServicio.crearNotificacion(mensaje, logueado, idProveedor);
//              Notificacion enviada

            trabajoRepositorio.save(trabajo);
        } else {
            throw new MiException("Cliente debe iniciar sesion");
        }

    }

    public void actualizarTrabajo(Long id, Usuario cliente, Usuario proveedor, String descripcion, Date fechaInicio, Date fechaFin, Estados estado) throws MiException {

        Optional<Trabajo> respuestaTrabajo = trabajoRepositorio.findById(id);

        if (respuestaTrabajo.isPresent()) {
            Trabajo trabajo = respuestaTrabajo.get();
            //trabajo.setCliente(cliente);
            //trabajo.setProveedor(proveedor);
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
    public List<Trabajo> listaTrabajosPorUsuario(Long id) {
        return trabajoRepositorio.buscarTrabajosPorUsuario(id);
    }

    @Transactional(readOnly = true)
    public List<Trabajo> listaTrabajos() {
        return trabajoRepositorio.findAll();
    }

    @Transactional
    public void rechazarTrabajo(HttpSession session, Long id) throws MiException {
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if (logueado == null) {
            throw new MiException("Proveedor no loggeado");
        } else if (logueado.getRol() == Rol.CLIENTE) {
            throw new MiException("Trabajo solo puede ser finalizado por Proveedor o Administrador");
        }
        Optional<Trabajo> optTrabajo = trabajoRepositorio.findById(id);
        if (!optTrabajo.isPresent()) {
            throw new MiException("Trabajo no encontrado");
        } else {
            Trabajo trabajo = optTrabajo.get();
            if (trabajo.getIdProveedor().equals(logueado.getID()) || logueado.getRol().equals(Rol.ADMIN)) {
                trabajo.setFechaFin(new Date());
                trabajo.setEstado(Estados.RECHAZADO);
                trabajoRepositorio.save(trabajo);
//                  Crear notificacion
                String mensaje = ("Trabajo rechazado por " + logueado.getRol() + ": " + logueado.getName());
                notificacionServicio.crearNotificacion(mensaje, logueado, trabajo.getIdCliente());
//                  Notificacion enviada
            } else {
                throw new MiException("Acceso denegado");
            }
        }
    }

    @Transactional
    public void finalizarTrabajo(HttpSession session, Long idTrabajo) throws MiException {
        Optional<Trabajo> optTrabajo = trabajoRepositorio.findById(idTrabajo);
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        if (logueado == null) {
            throw new MiException("Usuario no loggeado");
        }
        if (!optTrabajo.isPresent()) {
            throw new MiException("Trabajo no encontrado");
        } else {
            Trabajo trabajo = optTrabajo.get();

            if (!trabajo.getIdProveedor().equals(logueado.getID()) || !logueado.getRol().equals(Rol.ADMIN)) {
                throw new MiException("Acceso denegado");
            } else {
                trabajo.setFechaFin(new Date());
                trabajo.setEstado(Estados.FINALIZADO);
                trabajoRepositorio.save(trabajo);
//              Crear notificacion
                String mensaje = ("Trabajo finalizado con fecha: " + trabajo.getFechaFin() + ". \n Ya puedes calificar al trabajador.");
                notificacionServicio.crearNotificacion(mensaje, logueado, trabajo.getIdCliente());
//              Notificacion enviada
            }
        }
    }

    @Transactional
    public void calificarTrabajo(Long idTrabajo, String comentario, int puntuacion) throws MiException {
        Optional<Trabajo> optTrabajo = trabajoRepositorio.findById(idTrabajo);
        if (!optTrabajo.isPresent()) {
            throw new MiException("Trabajo no encontrado");
        }
        Trabajo trabajo = optTrabajo.get();

        if (calificacionRepositorio.findByTrabajoId(idTrabajo).isPresent()) {
            throw new MiException("El trabajo ya ha sido calificado");
        }

        if (trabajo.getEstado() != Estados.FINALIZADO) {
            throw new MiException("Trabajo no finalizado. No puede ser calificado aún.");

        } else {

            //calificacionServicio.crearCalificacion(trabajo.getCliente().getID(), trabajo.getProveedor().getID(), comentario, puntuacion);
            Calificacion calificacion = new Calificacion();
            calificacion.setComentario(comentario);
            calificacion.setPuntuacion(puntuacion);
            calificacion.setTrabajo(trabajo);

            // Aquí se asocian los usuarios a la calificación

            Usuario cliente = usuarioRepositorio.getById(trabajo.getIdCliente());
            Usuario proveedor = usuarioRepositorio.getById(trabajo.getIdProveedor());
            calificacion.setClienteEmisor(cliente);
            calificacion.setProveedorReceptor(proveedor);

            calificacionRepositorio.save(calificacion);
        }
    }


    public void validar(String descripcion, Date fechaInicio) throws MiException {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new MiException("Debe agregar una descripción del trabajo");
        }
        if (fechaInicio == null) {
            throw new MiException("Debe agregar fecha de inicio");
        }
    }
}
