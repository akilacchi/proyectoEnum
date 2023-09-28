package com.equipoa.servicewebapp.Repositorios;

import com.equipoa.servicewebapp.Entidades.Notificaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionesRepositorio extends JpaRepository<Notificaciones, Long> {

    @Query("DELETE FROM Notificaciones n WHERE n.id = :id")
    public void eliminarNotificacionPorId(@Param("id") Long id);


}
