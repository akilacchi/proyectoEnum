package com.equipoa.servicewebapp.Servicios;

import com.equipoa.servicewebapp.Entidades.Imagen;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Repositorios.ImagenRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class ImagenServicio {
    @Autowired
    private ImagenRepositorio imagenRepositorio;

    public Imagen guarddar(MultipartFile archivo) throws MiException{
        if(archivo!= null){
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());

                return imagenRepositorio.save(imagen);

            } catch (Exception e) {
                System.err.println(e.getMessage());;
            }
        }
        return null;
    }

    public Imagen actualizar(MultipartFile archivo, Long idImagen) throws MiException{
        if(archivo!= null){
            try {
                Imagen imagen = new Imagen();
                if (idImagen!=null){
                    Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);
                    imagen = respuesta.get();
                }

                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());

                return imagenRepositorio.save(imagen);

            } catch (Exception e) {
                System.err.println(e.getMessage());;
            }
        }
        return null;
    }
}
