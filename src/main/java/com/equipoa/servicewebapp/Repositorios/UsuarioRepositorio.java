package com.equipoa.servicewebapp.Repositorios;

import com.equipoa.servicewebapp.Entidades.Usuario;
import com.equipoa.servicewebapp.Enum.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    Usuario buscarPorEmail(@Param("email") String email);

    @Query("SELECT u FROM Usuario u WHERE u.phone = :phone")
    Usuario buscarPorTelefono(@Param("phone") String phone);

    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol")
    List<Usuario> findAllByRol(@Param("rol") Rol rol);

    @Query("SELECT u FROM Usuario u WHERE u.ID = :id")
    Optional<Usuario> findByIdList(@Param("id") Long id);

    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol AND u.ocupacion.nombre LIKE %:ocupacionNombre%")
    List<Usuario> findAllByRolAndOcupacionNombre(@Param("rol") Rol rol, @Param("ocupacionNombre") String ocupacionNombre);

    @Query("SELECT u FROM Usuario u WHERE u.activo = true")
    List<Usuario> findAllActivo();

    @Query("SELECT u FROM Usuario u WHERE u.activo = false")
    List<Usuario> findAllInactivo();

}
