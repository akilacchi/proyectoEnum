package com.equipoa.servicewebapp.Servicios;

import com.equipoa.servicewebapp.Entidades.Notificaciones;
import com.equipoa.servicewebapp.Entidades.Usuario;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Repositorios.NotificacionesRepositorio;
import com.equipoa.servicewebapp.Repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class NotificacionServicio {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    NotificacionesRepositorio notificacionesRepositorio;

    @Transactional
    public void crearNotificacion(String mensaje, Usuario remitente, Long idReceptor) throws MiException {
        validar(mensaje);
        if (remitente == null) {
            throw new MiException("Remitente inexistente");
        }
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idReceptor);

        if (respuesta.isPresent()) {
            Notificaciones notificacion = new Notificaciones();
            notificacion.setMensaje(mensaje);

            notificacion.setRemitente(remitente);
            notificacion.setReceptor(respuesta.get());

            notificacion.setFechaEnvio(new Date());

            notificacionesRepositorio.save(notificacion);
        } else {
            throw new MiException("Usuario receptor inexistente");
        }
    }

    @Transactional
    public void eliminarNotificacion(Usuario usr, Long idNotidicacion) throws MiException {
        if (usr == null) {
            throw new MiException("Usuario no encontrado");
        }
        Optional<Notificaciones> respuesta = notificacionesRepositorio.findById(idNotidicacion);
        if (respuesta.isPresent()) {
            notificacionesRepositorio.delete(respuesta.get());
        } else {
            throw new MiException("Notificación no encontrada");
        }
    }

    @Transactional
    public void eliminarTodasNotificaciones(Usuario usr) throws MiException {
        if(usr == null){
            throw new MiException("Usuario no encontrado");
        }else{
            notificacionesRepositorio.deleteAll(usr.getNotificacionesRecividas());
        }
    }

    private void validar(String mensaje) throws MiException {
        if (mensaje.trim().isEmpty() || mensaje == null) {
            throw new MiException("Mensaje vacio");
        }
    }
}
