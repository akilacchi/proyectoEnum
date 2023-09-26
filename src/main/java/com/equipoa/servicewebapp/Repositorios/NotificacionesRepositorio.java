package com.equipoa.servicewebapp.Repositorios;

import com.equipoa.servicewebapp.Entidades.Notificaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionesRepositorio extends JpaRepository<Notificaciones, Long> {

}
