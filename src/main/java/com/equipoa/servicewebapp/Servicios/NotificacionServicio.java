package com.equipoa.servicewebapp.Servicios;

import com.equipoa.servicewebapp.Entidades.Notificaciones;
import com.equipoa.servicewebapp.Entidades.Usuario;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Repositorios.NotificacionesRepositorio;
import com.equipoa.servicewebapp.Repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Service
public class NotificacionServicio {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    NotificacionesRepositorio notificacionesRepositorio;

    @Transactional
    public void crearNotificacion(String mensaje, Usuario emisor, Long idReceptor) throws MiException {
        validar(mensaje);
        if (emisor == null) {
            throw new MiException("Remitente inexistente");
        }
        Usuario receptor = usuarioRepositorio.findById(idReceptor)
                .orElseThrow(() -> new MiException("Usuario receptor inexistente"));

        Notificaciones notificacion = new Notificaciones();

        notificacion.setMensaje(mensaje);
        notificacion.setEmisor(emisor);
        notificacion.setReceptor(receptor);

        notificacion.setFechaEnvio(new Date());

        notificacionesRepositorio.save(notificacion);
    }

    @Transactional
    public void eliminarNotificacion(Usuario usr, Long idNotificacion) throws MiException {
        if (usr == null) {
            throw new MiException("Usuario no encontrado");
        }
        Optional<Notificaciones> respuesta = notificacionesRepositorio.findById(idNotificacion);

        if (respuesta.isPresent()) {
            Notificaciones notificacion = respuesta.get();

            usr.getNotificacionesRecividas().remove(notificacion);
            usuarioRepositorio.save(usr);

            notificacionesRepositorio.delete(notificacion);
        } else {
            throw new MiException("Notificación no encontrada");
        }
    }

    @Transactional
    public void eliminarTodasNotificaciones(Usuario usr) throws MiException {
        if (usr == null) {
            throw new MiException("Usuario no encontrado");
        } else {
            notificacionesRepositorio.deleteAll(usr.getNotificacionesRecividas());
        }
    }

    private void validar(String mensaje) throws MiException {
        if (mensaje.trim().isEmpty() || mensaje == null) {
            throw new MiException("Mensaje vacio");
        }
    }
}
