/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equipoa.servicewebapp.Repositorios;

import com.equipoa.servicewebapp.Entidades.Trabajo;
import com.equipoa.servicewebapp.Entidades.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author alejandrasuarez
 */
public interface TrabajoRepositorio extends JpaRepository<Trabajo, Long> {

    @Query("SELECT t FROM Trabajo t WHERE t.idProveedor=:id")
    List<Trabajo> buscarTrabajosPorUsuario(@Param("id") Long id);

}
