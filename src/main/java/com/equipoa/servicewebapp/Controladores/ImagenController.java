package com.equipoa.servicewebapp.Controladores;

import com.equipoa.servicewebapp.Entidades.Ocupaciones;
import com.equipoa.servicewebapp.Entidades.Usuario;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Repositorios.OcupacionesRepositorio;
import com.equipoa.servicewebapp.Servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/img")
public class ImagenController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private OcupacionesRepositorio ocupacionesRepositorio;

    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable Long id) { //id del usuario al que se enlaza la imagen
        Usuario usuario = null;
        try {
            usuario = usuarioServicio.getOne(id);
        } catch (MiException e) {
            System.err.println(e.getMessage());;
        }

        byte[] imagen = usuario.getProfilePicture().getContenido();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }

    @GetMapping("/ocupacion/{id}")
    public ResponseEntity<byte[]> imagenOcupacion(@PathVariable Long id) {
        Optional<Ocupaciones> respuesta = ocupacionesRepositorio.findById(id);

        if (!respuesta.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            byte[] imagen = respuesta.get().getFoto().getContenido();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
        }
    }

}
