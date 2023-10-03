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
import com.equipoa.servicewebapp.Entidades.Trabajo;
import com.equipoa.servicewebapp.Entidades.Usuario;
import com.equipoa.servicewebapp.Enum.Estados;
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
    
    
    
    @Transactional
    public void registrarTrabajo(String descripcion, Date fechaInicio, Date fechaFin, Estados estado, HttpSession session, Long idProveedor) throws MiException{
        
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

       
        Long cliente = logueado.getID();
       
        System.out.println("Este es el email del cliente logueado: "+logueado.getEmail());
        
        validar(descripcion, fechaInicio, fechaFin);
        
        Trabajo trabajo = new Trabajo();
        trabajo.setIdProveedor(idProveedor);
        trabajo.setIdCliente(cliente);
        trabajo.setDescripcion(descripcion);
        trabajo.setFechaInicio(fechaInicio);
        trabajo.setFechaFin(fechaFin);
        trabajo.setEstado(estado);
        
        
        trabajoRepositorio.save(trabajo);
        
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

        List<Trabajo> trabajos = trabajoRepositorio.buscarTrabajosPorUsuario(id);
        return trabajos;

    }

    @Transactional(readOnly = true)
    public List<Trabajo> listaTrabajos() {

        List<Trabajo> trabajos = trabajoRepositorio.findAll();

        return trabajos;
    }
    public void validar(String descripcion, Date fechaInicio, Date fechaFin) throws MiException{
        
        
        if(descripcion==null || descripcion.trim().isEmpty()){
            
            throw new MiException("Debe agregar una descripción del trabajo");
        }
        
        if(fechaInicio==null){
            throw new MiException("Debe agregar fecha de inicio");
        }
        if(fechaFin==null){
            throw new MiException("Debe agregar fecha final");
        }
        
        
        }
    
    

    @Transactional
    public void finalizarTrabajo(Long idTrabajo) throws MiException {
        Optional<Trabajo> optTrabajo = trabajoRepositorio.findById(idTrabajo);
        if (optTrabajo.isPresent()) {
            Trabajo trabajo = optTrabajo.get();
            trabajo.setEstado(Estados.FINALIZADO);
            trabajoRepositorio.save(trabajo);
        } else {
            throw new MiException("Trabajo no encontrado");
        }
    }

    @Transactional
    public void calificarTrabajo(Long idTrabajo, String comentario, int puntuacion) throws MiException {
        Optional<Trabajo> optTrabajo = trabajoRepositorio.findById(idTrabajo);
        if (optTrabajo.isPresent() && optTrabajo.get().getEstado() == Estados.FINALIZADO) {
            if (calificacionRepositorio.findByTrabajoId(idTrabajo).isPresent()) {
                throw new MiException("El trabajo ya ha sido calificado");
            }

            Trabajo trabajo = optTrabajo.get();

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
        } else {
            throw new MiException("El trabajo no ha sido finalizado o no fue encontrado");
        }
    }

}
