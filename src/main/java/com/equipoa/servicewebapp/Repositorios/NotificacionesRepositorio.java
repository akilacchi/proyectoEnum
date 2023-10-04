package com.equipoa.servicewebapp.Repositorios;

import com.equipoa.servicewebapp.Entidades.Notificaciones;
import com.equipoa.servicewebapp.Enum.TipoNotificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacionesRepositorio extends JpaRepository<Notificaciones, Long> {

    @Query("DELETE FROM Notificaciones n WHERE n.id = :id")
    void eliminarNotificacionPorId(@Param("id") Long id);

    @Query("SELECT n FROM Notificaciones n WHERE n.tipoNotificacion = :tipoNotificacion")
    List<Notificaciones> buscarPorTipo(@Param("tipoNotificacion") TipoNotificacion tipoNotificacion);

    @Query("SELECT n FROM Notificaciones n WHERE n.tipoNotificacion = 'CONTACTO'")
    List<Notificaciones> buscarNotificacionContacto();

    @Query("SELECT n FROM Notificaciones n WHERE n.tipoNotificacion = 'EVENTO'")
    List<Notificaciones> buscarNotificacionEvento();

    @Query("SELECT n FROM Notificaciones n WHERE n.tipoNotificacion = 'SOLICITUD'")
    List<Notificaciones> buscarNotificacionSolicitud();

    @Query("SELECT n FROM Notificaciones n WHERE n.tipoNotificacion = 'REPORTE'")
    List<Notificaciones> buscarNotificacionReporte();

}
