package com.equipoa.servicewebapp.Repositorios;

import com.equipoa.servicewebapp.Entidades.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalificacionRepositorio extends JpaRepository<Calificacion, Long> {

}
