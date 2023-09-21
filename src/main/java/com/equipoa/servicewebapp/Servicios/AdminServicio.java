package com.equipoa.servicewebapp.Servicios;

import com.equipoa.servicewebapp.Entidades.Imagen;
import com.equipoa.servicewebapp.Entidades.Ocupaciones;
import com.equipoa.servicewebapp.Entidades.Usuario;
import com.equipoa.servicewebapp.Enum.Provincias;
import com.equipoa.servicewebapp.Enum.Rol;
import com.equipoa.servicewebapp.Excepciones.MiException;
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
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Autowired
    private OcupacionesRepositorio ocupacionesRepositorio;
    @Autowired
    private OcupacionesServicio ocupacionesServicio;

    @Transactional
    public void crearUsuario(MultipartFile archivo, String email, String name, String password, String password2, String phone, Rol rol, Provincias provincia, String ocupacion) throws MiException {

        validar(email, name, password, password2, phone, rol, provincia);

        if (usuarioRepositorio.buscarPorEmail(email) == null && usuarioRepositorio.buscarPorTelefono(phone) == null) {
            Usuario usr = new Usuario();
            Ocupaciones ocupaciones = new Ocupaciones();

            usr.setEmail(email);
            usr.setName(name);
            usr.setPassword(new BCryptPasswordEncoder().encode(password));
            usr.setPhone(phone);
            usr.setRol(rol);
            usr.setProvincia(provincia);
            usr.setActivo(true);
            usr.setFecharegistro(new Date());

//            Set Roles automatico para cliente y admin si no existe.
            if(ocupacion.trim().isEmpty()){
                ocupacionesServicio.crearNuevaOcupacion("Cliente");
            }else if(ocupacionesRepositorio.buscarOcupacion(ocupacion)!=null){
                ocupaciones = ocupacionesRepositorio.buscarOcupacion(ocupacion);
            }else{
                throw new MiException("Ocupacion inexistente");
            }

            if (rol.equals(Rol.PROVEEDOR)) {
                usr.setOcupacion(ocupaciones);
            } else if (rol.equals(Rol.CLIENTE)) {
                Ocupaciones ocupacionesa = ocupacionesRepositorio.buscarOcupacion("Cliente");
                if (ocupaciones != null) {
                    usr.setOcupacion(ocupacionesa);
                } else {
                    ocupacionesServicio.crearNuevaOcupacion("Cliente");
                }
            } else {
                Ocupaciones ocupacionesb = ocupacionesRepositorio.buscarOcupacion("Admin");
                if (ocupaciones != null) {
                    usr.setOcupacion(ocupacionesb);
                } else {
                    ocupacionesServicio.crearNuevaOcupacion("Admin");
                }
            }

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
        if (respuesta!= null) {
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
    public void modificarRol(Long id, String email, String phone, Rol rol) throws MiException {
        Usuario usuario = new Usuario();
        validar(id, email, phone, rol, "a");

        Usuario respuesta = usuarioRepositorio.findById(id);
        if (respuesta!= null) {
            usuario = respuesta;
            usuario.setRol(rol);

            usuarioRepositorio.save(usuario);

        } else if (usuarioRepositorio.buscarPorTelefono(phone) != null) {
            usuario = usuarioRepositorio.buscarPorTelefono(phone);
            usuario.setRol(rol);

            usuarioRepositorio.save(usuario);

        } else if (usuarioRepositorio.buscarPorEmail(email) != null) {
            usuario = usuarioRepositorio.buscarPorTelefono(email);
            usuario.setRol(rol);

            usuarioRepositorio.save(usuario);

        } else {
            throw new MiException("Usuario inexistente");
        }
    }

    @Transactional
    public void crearOcupacion(String ocupacion) throws MiException {
        if (ocupacion.trim().isEmpty() || ocupacion == null) {
            throw new MiException("Ocupacion no puede estar vacia");
        } else if (ocupacionesRepositorio.buscarOcupacion(ocupacion) != null) {
            throw new MiException("Ocupacion ya existente");
        } else {
            ocupacionesServicio.crearNuevaOcupacion(ocupacion);
        }
    }

//  Encuentra un usuario y setea su estado "Activo" a lo opuesto que tenga actualmente
    @Transactional
    public void darDeBajaOAlta(Long id, String email, String phone) throws MiException {
        validar(id,email,phone, Rol.PROVEEDOR, "a");
        Usuario usuario = new Usuario();
        Boolean valorActual;

        Usuario respuesta = usuarioRepositorio.findById(id);
        if (respuesta!=null) {
            usuario = respuesta;
            valorActual = usuario.getActivo();
            usuario.setActivo(!valorActual);

            usuarioRepositorio.save(usuario);

        } else if (usuarioRepositorio.buscarPorTelefono(phone) != null) {
            usuario = usuarioRepositorio.buscarPorTelefono(phone);
            valorActual = usuario.getActivo();
            usuario.setActivo(!valorActual);

            usuarioRepositorio.save(usuario);

        } else if (usuarioRepositorio.buscarPorEmail(email) != null) {
            usuario = usuarioRepositorio.buscarPorTelefono(email);
            valorActual = usuario.getActivo();
            usuario.setActivo(!valorActual);

            usuarioRepositorio.save(usuario);

        } else {
            throw new MiException("Usuario inexistente");
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
