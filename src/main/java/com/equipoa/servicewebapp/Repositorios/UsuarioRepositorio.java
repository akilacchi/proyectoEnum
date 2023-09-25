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
    public Usuario buscarPorTelefono(@Param("phone") String phone);

    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol")
    public List<Usuario> findAllByRol(@Param("rol") Rol rol);

    @Query("SELECT u FROM Usuario u WHERE u.ID = :id")
    public Usuario findById(Long id);

    @Query("SELECT u FROM Usuario u WHERE u.ID = :id")
    public Optional<Usuario> findByIdList(@Param("id") Long id);

//    @Query("SELECT u FROM Usuario u WHERE DATEDIFF(current date, u.fecharegistro)= :dias")
//    public List<Usuario> buscarPorAntiguedad(int dias);
    @Query("SELECT u FROM Usuario u WHERE u.rol = :rol AND u.ocupacion.nombre LIKE %:ocupacionNombre%")
    List<Usuario> findAllByRolAndOcupacionNombre(@Param("rol") Rol rol, @Param("ocupacionNombre") String ocupacionNombre);

    @Query("SELECT u FROM Usuario u WHERE u.activo = true")
    public List<Usuario> findAllActivo();

    @Query("SELECT u FROM Usuario u WHERE u.activo = false")
    public List<Usuario> findAllInactivo();

}
