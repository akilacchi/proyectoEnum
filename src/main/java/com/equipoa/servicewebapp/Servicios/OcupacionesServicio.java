package com.equipoa.servicewebapp.Servicios;

import com.equipoa.servicewebapp.Entidades.Ocupaciones;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Repositorios.OcupacionesRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OcupacionesServicio {

@Autowired
OcupacionesRepositorio ocupacionesRepositorio;
    public void crearNuevaOcupacion(String nombre) throws MiException {
        validar(nombre);
        if (ocupacionesRepositorio.buscarOcupacion(nombre)== null){

        Ocupaciones ocupacion = new Ocupaciones();
        ocupacion.setOcupacion(nombre);
        ocupacionesRepositorio.save(ocupacion);
        }else {
            throw new MiException("Ocupacion ya existente");
        }
    }

    public Ocupaciones buscarOcupacion(String ocuppacion) throws MiException {
        if(ocupacionesRepositorio.buscarOcupacion(ocuppacion)!=null){
            return ocupacionesRepositorio.buscarOcupacion(ocuppacion);
        }else{
            throw new MiException("Ocupacion inexistente");
        }
    }

    @Transactional
    public void eliminarOcupacion(String nombre) throws MiException {
        validar(nombre);
        Ocupaciones ocupacion = ocupacionesRepositorio.buscarOcupacion(nombre);
        if(ocupacion!= null){
            ocupacionesRepositorio.delete(ocupacion);
        }else{
            throw new MiException("Ocupacion Inexistente");
        }
    }

    @Transactional
    public void modificarOcupacion(String nombre, String nuevoNombre) throws MiException {
        validar(nombre);
        Ocupaciones ocupacion = ocupacionesRepositorio.buscarOcupacion(nombre);
        if(ocupacion!= null){
            validar(nuevoNombre);
            ocupacion.setOcupacion(nuevoNombre);
            ocupacionesRepositorio.save(ocupacion);
        }
    }

    public void validar(String nombre) throws MiException {
        if(nombre.trim().isEmpty() || nombre==null){
            throw new MiException("Ocupacion vacía o nula");
        }
    }
}
