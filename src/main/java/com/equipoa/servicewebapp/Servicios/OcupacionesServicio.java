package com.equipoa.servicewebapp.Servicios;

import com.equipoa.servicewebapp.Entidades.Imagen;
import com.equipoa.servicewebapp.Entidades.Ocupaciones;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Repositorios.OcupacionesRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OcupacionesServicio {

    @Autowired

    private OcupacionesRepositorio ocupacionesRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    public void crearNuevaOcupacion(MultipartFile archivo, String nombre, String descripcion) throws MiException {
        validar(nombre, descripcion);
        if (ocupacionesRepositorio.buscarOcupacion(nombre) == null) {

            Ocupaciones ocupacion = new Ocupaciones();
            ocupacion.setOcupacion(nombre);
            ocupacion.setDescripcion(descripcion);

            Imagen imagen = imagenServicio.guardar(archivo);
            ocupacion.setFoto(imagen);
            ocupacionesRepositorio.save(ocupacion);
        } else {
            throw new MiException("Ocupacion ya existente");
        }
    }

    public Ocupaciones buscarOcupacion(String ocuppacion) throws MiException {
        if (ocupacionesRepositorio.buscarOcupacion(ocuppacion) != null) {
            return ocupacionesRepositorio.buscarOcupacion(ocuppacion);
        } else {
            throw new MiException("Ocupacion inexistente");
        }
    }

    @Transactional
    public void eliminarOcupacion(String nombre) throws MiException {
        validar(nombre);
        Ocupaciones ocupacion = ocupacionesRepositorio.buscarOcupacion(nombre);
        if (ocupacion != null) {
            ocupacionesRepositorio.delete(ocupacion);
        } else {
            throw new MiException("Ocupacion Inexistente");
        }
    }

    @Transactional
    public void modificarOcupacion(MultipartFile archivo, String nombre, String nuevoNombre, String descripcion) throws MiException {
        validar(nombre, descripcion);
        Ocupaciones ocupacion = ocupacionesRepositorio.buscarOcupacion(nombre);

        if (archivo == null) {
            throw new MiException("Archivo vacio");
        }
        if (ocupacion == null) {
            throw new MiException("Ocupacion inexistente");
        } else {

            validar(nuevoNombre);
            ocupacion.setOcupacion(nuevoNombre);
            ocupacion.setDescripcion(descripcion);

            if (ocupacion.getFoto() == null) {
                Imagen imagen = imagenServicio.guardar(archivo);
                ocupacion.setFoto(imagen);
                ocupacionesRepositorio.save(ocupacion);
            } else {
                Long idImagen = ocupacion.getFoto().getId();
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                ocupacion.setFoto(imagen);
                ocupacionesRepositorio.save(ocupacion);
            }
        }
    }

    public void validar(String nombre, String descripcion) throws MiException {
        if (nombre.trim().isEmpty() || nombre == null) {
            throw new MiException("Ocupacion vacía o nula");
        }
        if (descripcion.trim().isEmpty() || descripcion == null) {
            throw new MiException("Descripcion de ocupacion vacía o nula");
        }
    }

    public void validar(String nombre) throws MiException {
        if (nombre.trim().isEmpty() || nombre == null) {
            throw new MiException("Ocupacion vacía o nula");
        }
    }

    @Transactional(readOnly = true)
    public List<Ocupaciones> obtenerTodasLasOcupaciones() {
        List<Ocupaciones> ocupaciones = ocupacionesRepositorio.buscarTodo();
        return ocupaciones;
    }
}
