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
public interface UsuarioRepositorio extends JpaRepository<Usuario, String> {
    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    public Usuario buscarPorEmail(@Param("email") String email);

    @Query("SELECT u FROM Usuario u WHERE u.phone = :phone")
    public Usuario buscarPorTelefono(@Param("phone") String phone );

    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol")
    public List<Usuario> findAllByRol(@Param("rol") Rol rol);

    @Query("SELECT u FROM Usuario u WHERE u.ID = :id")
    public Optional<Usuario> findById(@Param("id") Long id);
}
