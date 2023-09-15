package com.equipoa.servicewebapp.Repositorios;

import com.equipoa.servicewebapp.Entidades.Ocupaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OcupacionesRepositorio extends JpaRepository<Ocupaciones, Long> {

    @Query("SELECT o FROM Ocupaciones o")
    public List<Ocupaciones> buscarTodo();

    @Query("SELECT o FROM Ocupaciones o WHERE o.nombre = :ocupacion")
    public Ocupaciones buscarOcupacion(@Param("ocupacion") String ocupacion);
}
