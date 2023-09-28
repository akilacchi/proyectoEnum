package com.equipoa.servicewebapp.Repositorios;

import com.equipoa.servicewebapp.Entidades.Usuario;
import com.equipoa.servicewebapp.Enum.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolRepositorio extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol")
    List<Usuario> buscarTodosPorRol(@Param("rol") Rol rol);

    @Query("SELECT u FROM Usuario u WHERE u.rol = 'PROVEEDOR'")
    List<Usuario> buscarTodosLosProveedores();

    @Query("SELECT u FROM Usuario u WHERE u.rol = 'CLIENTE'")
    List<Usuario> buscarTodosLosClientes();
}
