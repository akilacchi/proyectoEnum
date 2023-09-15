package com.equipoa.servicewebapp.Servicios;

import com.equipoa.servicewebapp.Entidades.Calificacion;
import com.equipoa.servicewebapp.Entidades.Trabajo;
import com.equipoa.servicewebapp.Entidades.Usuario;
import com.equipoa.servicewebapp.Enum.Provincias;
import com.equipoa.servicewebapp.Enum.Rol;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void crearUsuario(String email, String name, String password, String password2, int phone, Rol rol, Provincias provincia) throws MiException {
        validar(email, name, password, password2, phone, rol);
        if (usuarioRepositorio.buscarPorEmail(email) == null && usuarioRepositorio.buscarPorTelefono(phone) == null) {
            Usuario usr = new Usuario();
            usr.setEmail(email);
            usr.setName(name);
            usr.setPassword(new BCryptPasswordEncoder().encode(password));
            usr.setPhone(phone);
            usr.setRol(rol);
            usr.setProvincia(provincia);
            usr.setFecharegistro(new Date());
            usuarioRepositorio.save(usr);
        } else {
            throw new MiException("Usuario ya registrado");
        }
    }

    private void validar(String email, String name, String password, String password2, int phone, Rol rol) throws MiException {
        if (email.trim().isEmpty() || email == null) {
            throw new MiException("Email no puede estar vacío");
        }
        if (name.trim().isEmpty() || name == null) {
            throw new MiException("Nombre no puede estar vacío");
        }
        if (password.trim().isEmpty() || password == null) {
            throw new MiException("Contraseña no puede estar vacía");
        }
        if (!password.equals(password2)) {
            throw new MiException("Ambas contraseñas deben coincidir");
        }
        if (phone < 1) {
            throw new MiException("Inserte un número válido");
        }
        if (rol == null) {
            throw new MiException("Rol no puede estar vacío");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);

        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();

            // Obtener el tipo de usuario y construir el rol correspondiente
            String rol = "ROLE_" + usuario.getRol().toString();

            // Agregar el rol a la lista de permisos
            GrantedAuthority p = new SimpleGrantedAuthority(rol);
            permisos.add(p);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }

    }

    //Crud Cliente
    @Transactional
    public void crearCliente(String email, String name, String password, String password2, int phone, Rol rol, Provincias provincia, String direccion, List<Trabajo> trabajosCliente, List<Calificacion> calificacionesEmitidas) throws MiException {
        //validarCliente(email, name, password, password2, phone);
        if (usuarioRepositorio.buscarPorEmail(email) == null && usuarioRepositorio.buscarPorTelefono(phone) == null) {
            Usuario cliente = new Usuario();
            cliente.setEmail(email);
            cliente.setName(name);
            cliente.setPassword(new BCryptPasswordEncoder().encode(password));
            cliente.setPhone(phone);
            cliente.setProvincia(provincia);
            cliente.setFecharegistro(new Date());;
            cliente.setRol(Rol.CLIENTE); // Establecer el rol como "cliente"
            cliente.setDireccion(direccion);
            cliente.setCalificacionesEmitidas(calificacionesEmitidas);
            cliente.setTrabajosCliente(trabajosCliente);

            usuarioRepositorio.save(cliente);
        } else {
            throw new MiException("Usuario ya registrado");
        }

    }

    public void actualizarCliente(String email, String nuevoNombre, String nuevaDireccion, int nuevoPhone) throws MiException {
        // Buscamos al cliente por su dirección de correo electrónico
        Usuario cliente = usuarioRepositorio.buscarPorEmail(email);

        if (cliente == null || !cliente.getRol().equals(Rol.CLIENTE)) {
            throw new MiException("Cliente no encontrado");
        }

        // Actualizamos los datos del cliente
        cliente.setName(nuevoNombre);
        cliente.setDireccion(nuevaDireccion);
        cliente.setPhone(nuevoPhone);

        // Guardamos los cambios en la base de datos
        usuarioRepositorio.save(cliente);
    }

    @Transactional
    public void eliminarClientePorEmail(String email) throws MiException {
        // Verificamos si el cliente existe
        Usuario clienteExistente = usuarioRepositorio.buscarPorEmail(email);

        if (clienteExistente == null || !clienteExistente.getRol().equals(Rol.CLIENTE)) {
            throw new MiException("Cliente no encontrado");
        }

        // Eliminamos al cliente de la base de datos
        usuarioRepositorio.delete(clienteExistente);
    }

    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodosLosClientes() {
        List<Usuario> clientes = usuarioRepositorio.findAllByRol(Rol.CLIENTE);
        return clientes;
    }

//    private void validarCliente(String email, String name, String password, String password2, int phone) throws MiException {
//        if (email.trim().isEmpty() || email == null) {
//            throw new MiException("Email no puede estar vacío");
//        }
//        if (name.trim().isEmpty() || name == null) {
//            throw new MiException("Nombre no puede estar vacío");
//        }
//        if (password.trim().isEmpty() || password == null) {
//            throw new MiException("Contraseña no puede estar vacía");
//        }
//        if (!password.equals(password2)) {
//            throw new MiException("Ambas contraseñas deben coincidir");
//        }
//        if (phone < 1) {
//            throw new MiException("Inserte un número válido");
//        }
//    }
}
