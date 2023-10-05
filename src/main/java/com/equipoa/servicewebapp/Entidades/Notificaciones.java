package com.equipoa.servicewebapp.Entidades;

import com.equipoa.servicewebapp.Enum.TipoNotificacion;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Notificaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "emisor_id")
    private Usuario emisor;              //Usuario que envia la notificacion

    @ManyToOne
    @JoinColumn(name = "receptor_id")
    private Usuario receptor;            //Usuarios que reciben la notificacion

    private String mensaje;              //Mensaje de la notificacion

    @Enumerated(EnumType.STRING)
    private TipoNotificacion tipoNotificacion;

    private Date fechaEnvio;

    public Notificaciones() {
    }

    public TipoNotificacion getTipoNotificacion() {
        return tipoNotificacion;
    }

    public void setTipoNotificacion(TipoNotificacion tipoNotificacion) {
        this.tipoNotificacion = tipoNotificacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getEmisor() {
        return emisor;
    }

    public void setEmisor(Usuario emisor) {
        this.emisor = emisor;
    }

    public Usuario getReceptor() {
        return receptor;
    }

    public void setReceptor(Usuario receptor) {
        this.receptor = receptor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}
