package com.equipoa.servicewebapp.Servicios;

import com.equipoa.servicewebapp.Entidades.Calificacion;
import com.equipoa.servicewebapp.Entidades.Imagen;
import com.equipoa.servicewebapp.Entidades.Ocupaciones;
import com.equipoa.servicewebapp.Entidades.Usuario;
import com.equipoa.servicewebapp.Enum.Provincias;
import com.equipoa.servicewebapp.Enum.Rol;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Repositorios.CalificacionRepositorio;
import com.equipoa.servicewebapp.Repositorios.OcupacionesRepositorio;
import com.equipoa.servicewebapp.Repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Optional;

@Service
public class AdminServicio {

    @Autowired
    private CalificacionRepositorio calificacionRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Autowired
    private OcupacionesRepositorio ocupacionesRepositorio;
    @Autowired
    private OcupacionesServicio ocupacionesServicio;

    @Transactional
    public void crearAdmin(MultipartFile archivo, String email, String name, String password, String password2, String phone, Provincias provincia) throws MiException {

        validar(email, name, password, password2, phone, Rol.ADMIN, provincia);

        if (usuarioRepositorio.buscarPorEmail(email) == null && usuarioRepositorio.buscarPorTelefono(phone) == null) {
            Usuario usr = new Usuario();

            usr.setEmail(email);
            usr.setName(name);
            usr.setPassword(new BCryptPasswordEncoder().encode(password));
            usr.setPhone(phone);
            usr.setRol(Rol.ADMIN);
            usr.setProvincia(provincia);
            usr.setActivo(true);
            usr.setFecharegistro(new Date());

            Imagen imagen = imagenServicio.guardar(archivo);
            usr.setProfilePicture(imagen);

            usuarioRepositorio.save(usr);

        } else {
            throw new MiException("Usuario ya existente");
        }
    }

    @Transactional
    public void modificarOcupacion(Long id, String email, String phone, String ocupaciones) throws MiException {
        Usuario usuario = new Usuario();
        Ocupaciones ocupacion = new Ocupaciones();
        validar(id, email, phone, Rol.PROVEEDOR, ocupaciones);

        if (ocupacionesRepositorio.buscarOcupacion(ocupaciones) != null) {
            ocupacion = ocupacionesRepositorio.buscarOcupacion(ocupaciones);
        } else {
            throw new MiException("Ocupación inexistente");
        }

        Usuario respuesta = usuarioRepositorio.findById(id);
        if (respuesta != null) {
            usuario = respuesta;
            usuario.setOcupacion(ocupacion);

            usuarioRepositorio.save(usuario);

        } else if (usuarioRepositorio.buscarPorTelefono(phone) != null) {
            usuario = usuarioRepositorio.buscarPorTelefono(phone);
            usuario.setOcupacion(ocupacion);

            usuarioRepositorio.save(usuario);

        } else if (usuarioRepositorio.buscarPorEmail(email) != null) {
            usuario = usuarioRepositorio.buscarPorTelefono(email);
            usuario.setOcupacion(ocupacion);

            usuarioRepositorio.save(usuario);

        } else {
            throw new MiException("Usuario inexistente");
        }
    }

    @Transactional
    public void modificarRol(Long id) throws MiException {
        if (id < 1) {
            throw new MiException("ID incorrecto");
        } else if (usuarioRepositorio.findById(id) == null) {
            throw new MiException("Usuario inexistente");
        } else if (usuarioRepositorio.findById(id).getRol() != Rol.CLIENTE) {
            throw new MiException("Usuario debe tener rol Cliente");
        } else {
            Usuario usuario = usuarioRepositorio.findById(id);
            usuario.setRol(Rol.PROVEEDOR);
            usuarioRepositorio.save(usuario);
        }
    }

    @Transactional
    public void crearOcupacion(MultipartFile archivo, String ocupacion, String descripcion) throws MiException {
        if (ocupacion.trim().isEmpty() || ocupacion == null) {
            throw new MiException("Ocupacion no puede estar vacia");
        } else if (descripcion.trim().isEmpty() || descripcion == null) {
            throw new MiException("Descripcion no puede estar vacia");
        } else if (ocupacionesRepositorio.buscarOcupacion(ocupacion) != null) {
            throw new MiException("Ocupacion ya existente");
        } else {
            ocupacionesServicio.crearNuevaOcupacion(archivo, ocupacion, descripcion);
        }
    }

    @Transactional
    public void modificarOcupacion(MultipartFile archivo, String nombre, String nuevoNombre, String descripcion) throws MiException {
        if (nombre.trim().isEmpty() || nombre == null) {
            throw new MiException("Nombre no puede estar vacio");
        }
        if (nuevoNombre.trim().isEmpty() || nuevoNombre == null) {
            throw new MiException("Nuevo nombre no puede estar vacio");
        }
        if (descripcion.trim().isEmpty() || descripcion == null) {
            throw new MiException("Descripcion no puede estar vacia");
        }
        ocupacionesServicio.modificarOcupacion(archivo, nombre, nuevoNombre, descripcion);
        Ocupaciones ocupacion = ocupacionesRepositorio.buscarOcupacion(nombre);

    }

    //  Encuentra un usuario y setea su estado "Activo" a lo opuesto que tenga actualmente
    @Transactional
    public void desactivarUsuario(Long id) throws MiException {
        if (id < 1) {
            throw new MiException("ID Inexistente");
        } else if (usuarioRepositorio.findById(id) == null) {
            throw new MiException("Usuario Inexistente");
        } else {
            Usuario usuario = usuarioRepositorio.findById(id);
            usuario.setActivo(false);
            usuarioRepositorio.save(usuario);
        }
    }

    @Transactional
    public void activarUsuario(Long id) throws MiException {
        if (id < 1) {
            throw new MiException("ID Inexistente");
        } else if (usuarioRepositorio.findById(id) == null) {
            throw new MiException("Usuario Inexistente");
        } else {
            Usuario usuario = usuarioRepositorio.findById(id);
            usuario.setActivo(true);
            usuarioRepositorio.save(usuario);
        }
    }

    @Transactional
    public void eliminarCalificaion(Long id) throws MiException {
        if (id < 1 || id == null) {
            throw new MiException("Idimposible o nulo");
        }

        Optional<Calificacion> respuesta = calificacionRepositorio.findById(id);
        if (!respuesta.isPresent()) {
            throw new MiException("Calificaion no encontrada");
        } else {
            calificacionRepositorio.delete(respuesta.get());
        }

    }

    //    Validar datos no nulos para modificaciones del admin
    public void validar(Long id, String email, String phone, Rol rol, String ocupacion) throws MiException {
        if (id == null) {
            throw new MiException("ID no puede estar vacío");
        }

        if (email == null) {
            throw new MiException("Email no puede estar vacío");
        }

        if (phone == null) {
            throw new MiException("Número de teléfono no puede estar vacío");
        }

        if (rol == null) {
            throw new MiException("Seleccione un rol válido");
        }

        if (ocupacion == null) {
            throw new MiException("Ocupación no puede estar vacia");

        }
    }

    //    Validacion general
    public void validar(String email, String name, String password, String password2, String phone, Rol rol, Provincias provincia) throws MiException {
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

        if (phone.trim().isEmpty() || phone == null) {
            throw new MiException("Número de teléfono no puede estar vacío");
        }

        if (rol == null) {
            throw new MiException("Seleccione un rol válido");
        }

        if (provincia == null) {
            throw new MiException("Seleccione una provincia válidas");
        }

    }
}
