package com.equipoa.servicewebapp.Repositorios;

import com.equipoa.servicewebapp.Entidades.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenRepositorio extends JpaRepository<Imagen, Long> {
}
