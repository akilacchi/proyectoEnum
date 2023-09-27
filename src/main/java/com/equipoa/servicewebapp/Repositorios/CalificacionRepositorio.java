package com.equipoa.servicewebapp.Repositorios;

import com.equipoa.servicewebapp.Entidades.Calificacion;
import com.equipoa.servicewebapp.Entidades.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalificacionRepositorio extends JpaRepository<Calificacion, Long> {

    Optional<Calificacion> findByTrabajoId(Long trabajoId);

    List<Calificacion> findAllByProveedorReceptor(Usuario proveedorReceptor);

}
