package com.equipoa.servicewebapp.Servicios;

import com.equipoa.servicewebapp.Entidades.Calificacion;
import com.equipoa.servicewebapp.Entidades.Imagen;
import com.equipoa.servicewebapp.Entidades.Notificaciones;
import com.equipoa.servicewebapp.Entidades.Ocupaciones;
import com.equipoa.servicewebapp.Entidades.Usuario;
import com.equipoa.servicewebapp.Enum.Provincias;
import com.equipoa.servicewebapp.Enum.Rol;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Repositorios.CalificacionRepositorio;
import com.equipoa.servicewebapp.Repositorios.OcupacionesRepositorio;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    OcupacionesRepositorio ocupacionesRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Autowired
    private OcupacionesServicio ocupacionesServicio;

    @Autowired
    CalificacionRepositorio calificacionRepositorio;

    @Transactional
    public void crearUsuario(MultipartFile archivo, String email, String name, String password, String password2, String phone, Rol rol, Provincias provincia) throws MiException {

        validar(email, name, password, password2, phone);

        if (usuarioRepositorio.buscarPorEmail(email) == null && usuarioRepositorio.buscarPorTelefono(phone) == null) {
            Usuario usr = new Usuario();
            usr.setEmail(email);
            usr.setName(name);
            usr.setPassword(new BCryptPasswordEncoder().encode(password));
            usr.setPhone(phone);
            usr.setRol(rol);
            usr.setProvincia(provincia);
            usr.setActivo(true);
            usr.setFecharegistro(new Date());

            Imagen imagen = imagenServicio.guardar(archivo);
            usr.setProfilePicture(imagen);

            usuarioRepositorio.save(usr);
        } else {
            throw new MiException("Usuario ya registrado");
        }
    }


    //Crud Cliente
    @Transactional
    public void crearCliente(MultipartFile archivo, String email, String name, String password, String password2, String phone, Provincias provincia, String direccion) throws MiException {
        validar(email, name, password, password2, phone);

        if (usuarioRepositorio.buscarPorEmail(email) == null && usuarioRepositorio.buscarPorTelefono(phone) == null) {
            Usuario cliente = new Usuario();
            cliente.setEmail(email);
            cliente.setName(name);
            cliente.setPassword(new BCryptPasswordEncoder().encode(password));
            cliente.setPhone(phone);
            cliente.setProvincia(provincia);
            cliente.setFecharegistro(new Date());
            cliente.setActivo(true);
            cliente.setRol(Rol.CLIENTE); // Establecer el rol como "cliente"
            cliente.setDireccion(direccion);
            cliente.setCalificacionesEmitidas(new ArrayList<>());
            cliente.setTrabajosCliente(new ArrayList<>());

            Imagen imagen = imagenServicio.guardar(archivo);
            cliente.setProfilePicture(imagen);

            usuarioRepositorio.save(cliente);
        } else {
            throw new MiException("Usuario ya registrado");
        }

    }

    @Transactional
    public void actualizarCliente(MultipartFile archivo, String email, String nombre, String nuevaDireccion, String phone, String password, String password2) throws MiException {

        validar(email, nombre, password, password2, phone);

        // Buscamos al cliente por su dirección de correo electrónico
        Usuario cliente = usuarioRepositorio.buscarPorEmail(email);

        if (cliente == null || !cliente.getRol().equals(Rol.CLIENTE)) {
            throw new MiException("Cliente no encontrado");
        }

        // Actualizamos los datos del cliente
        cliente.setName(nombre);
        cliente.setDireccion(nuevaDireccion);
        cliente.setPhone(phone);
        cliente.setPassword(new BCryptPasswordEncoder().encode(password));

        Long idImagen;
        if (cliente.getProfilePicture() != null) {
            idImagen = cliente.getProfilePicture().getId();
            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            cliente.setProfilePicture(imagen);
        } else {
            Imagen imagen = imagenServicio.guardar(archivo);
            cliente.setProfilePicture(imagen);

        }

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

    @Transactional
    public void crearProveedor(MultipartFile archivo, String email, String name, String password, String password2, String phone, Provincias provincia, String ocupacion) throws MiException {
        Usuario proveedor = new Usuario();

        validar(email, name, password, password2, phone);
        if (usuarioRepositorio.buscarPorEmail(email) == null && usuarioRepositorio.buscarPorTelefono(phone) == null) {
            proveedor.setRol(Rol.PROVEEDOR);                                        //rol de proveedor seteado por defecto
            proveedor.setName(name);

            proveedor.setOcupacion(ocupacionesServicio.buscarOcupacion(ocupacion)); //ocupacion seteado por parametro en lista

            proveedor.setProvincia(provincia);
            proveedor.setActivo(true);                                              //seteado com usuario activo por defecto
            proveedor.setEmail(email);
            proveedor.setPassword(new BCryptPasswordEncoder().encode(password));
            proveedor.setPhone(phone);                                              //numero de telefono para ofrecer contacto
            proveedor.setFecharegistro(new Date());                                 //fecha de registro seteada a la fecha actual
            proveedor.setTrabajosProveedor(new ArrayList<>());                      //trabajos de provedor vacio
            proveedor.setCalificacionesRecibidas(new ArrayList<>());                //calificaciones recividas vacias

            Imagen imagen = imagenServicio.guardar(archivo);
            proveedor.setProfilePicture(imagen);                                    //foto de perfil

            usuarioRepositorio.save(proveedor);
        } else {
            throw new MiException("Usuario ya registrado");
        }
    }

    @Transactional

    public void actualizarProveedor(MultipartFile archivo, String email, String name, String password, String password2, String phone, Provincias provincia, String ocupacion) throws MiException {
        validar(email, name, password, password2, phone);
        Usuario proveedor = usuarioRepositorio.buscarPorEmail(email);
        if (proveedor == null || !proveedor.getRol().equals(Rol.PROVEEDOR)) {

            throw new MiException("Proveedor no encontrado");
        } else {

            proveedor.setEmail(email);
            proveedor.setName(name);
            proveedor.setPhone(phone);
            proveedor.setProvincia(provincia);
            proveedor.setOcupacion(ocupacionesServicio.buscarOcupacion(ocupacion));

            Imagen imagen = imagenServicio.guardar(archivo);
            proveedor.setProfilePicture(imagen);

            usuarioRepositorio.save(proveedor);

        }

    }


    @Transactional(readOnly = true)
    public List<Notificaciones> mostrarTodasLasNotificacionesUsr(Long idUsuario) throws MiException {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        if(respuesta.isPresent()){
            return respuesta.get().getNotificacionesRecividas();
        }else{
            throw new MiException("Usuario no encontrado");
        }
    }


    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodosLosProveedores() {
        return usuarioRepositorio.findAllByRol(Rol.PROVEEDOR);
    }

    @Transactional
    public void eliminarProveedor(String email) throws MiException {

        Usuario proveedor = usuarioRepositorio.buscarPorEmail(email);
        if (proveedor == null || !proveedor.getRol().equals(Rol.PROVEEDOR)) {
            throw new MiException("El proveedor que intenta eliminar no existe");
        } else {

            usuarioRepositorio.delete(proveedor);
        }
    }

    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodosLosClientes() {
        return usuarioRepositorio.findAllByRol(Rol.CLIENTE);
    }

    private void validar(String email, String name, String password, String password2, String phone) throws MiException {
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
        if (phone.isEmpty() || phone == null) {
            throw new MiException("Inserte un número válido");
        }
    }

    @Transactional(readOnly = true)
    public List<Usuario> obtenerProveedoresPorOcupacion(String ocupacionNombre) {
        return usuarioRepositorio.findAllByRolAndOcupacionNombre(Rol.PROVEEDOR, ocupacionNombre);
    }

    public void asignarOcupacionAProveedor(Long proveedorId, Long ocupacionId) throws MiException {
        Optional<Usuario> proveedorOpt = usuarioRepositorio.findByIdList(proveedorId);
        Optional<Ocupaciones> ocupacionOpt = ocupacionesRepositorio.findById(ocupacionId);

        if (proveedorOpt.isPresent() && ocupacionOpt.isPresent()) {
            Usuario proveedor = proveedorOpt.get();
            Ocupaciones ocupacion = ocupacionOpt.get();
            proveedor.setOcupacion(ocupacion);
            usuarioRepositorio.save(proveedor);
        } else {
            throw new MiException("proveedor o ocupación no encontrados");
        }
    }

    public Usuario getOne(Long id) throws MiException {
        Optional<Usuario> usr = usuarioRepositorio.findById(id);
        if (usr.isPresent()) {
            return usr.get();
        }else{
            throw new MiException("Usuario no encontrado");
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

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession sesion = attr.getRequest().getSession(true);
            sesion.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }

    }

    @Transactional(readOnly = true)
    public Usuario obtenerProveedorConCalificaciones(Long idProveedor) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idProveedor);
        if (!respuesta.isPresent() && respuesta.get().getRol() == Rol.PROVEEDOR) {
            Usuario proveedor = respuesta.get();
            List<Calificacion> calificaciones = calificacionRepositorio.findAllByProveedorReceptor(proveedor);
            proveedor.setCalificacionesRecibidas(calificaciones);
            return proveedor;
        }
        return null;
    }

}
