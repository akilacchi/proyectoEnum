package com.equipoa.servicewebapp.Controladores;

import com.equipoa.servicewebapp.Entidades.Notificaciones;
import com.equipoa.servicewebapp.Repositorios.NotificacionesRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificacionesController {

    @Autowired
    private NotificacionesRepositorio notificacionesRepositorio;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/application")
    @SendTo("all/notificaciones")
    public Notificaciones send(final Notificaciones notificacion) throws Exception{
        return notificacion;
    }

    @MessageMapping("/private")
    public void sendToSpecificUser(@Payload Notificaciones notificacion){
        simpMessagingTemplate.convertAndSendToUser(notificacion.getReceptor().getEmail(),"/specific",notificacion);
        notificacionesRepositorio.save(notificacion);
    }
}
