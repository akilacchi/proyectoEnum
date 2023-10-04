package com.equipoa.servicewebapp.Controladores;

import com.equipoa.servicewebapp.Entidades.Calificacion;
import com.equipoa.servicewebapp.Entidades.Notificaciones;
import com.equipoa.servicewebapp.Entidades.Ocupaciones;
import com.equipoa.servicewebapp.Entidades.Usuario;
import com.equipoa.servicewebapp.Enum.Provincias;
import com.equipoa.servicewebapp.Enum.Rol;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Repositorios.OcupacionesRepositorio;
import com.equipoa.servicewebapp.Repositorios.UsuarioRepositorio;
import com.equipoa.servicewebapp.Servicios.NotificacionServicio;
import com.equipoa.servicewebapp.Servicios.OcupacionesServicio;
import com.equipoa.servicewebapp.Servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/")
public class PortalController {

    public List<Usuario> getAdmins() {
        return usuarioRepositorio.findAllByRol(Rol.ADMIN);
    }

    public List<Ocupaciones> getOcupaciones() {
        return ocupacionesRepositorio.findAll();
    }

    public Rol[] getRol() {
        return Rol.values();
    }

    public Provincias[] getProvincias() {
        return Provincias.values();
    }

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    @Autowired
    UsuarioServicio usuarioServicio;

    @Autowired
    NotificacionServicio notificacionServicio;

    @Autowired
    private OcupacionesRepositorio ocupacionesRepositorio;
    @Autowired
    private OcupacionesServicio ocupacionesServicio;

    @GetMapping("/FAQ")
    public String faq() {
        return "preguntasfrecuentes.html";
    }

    @GetMapping("/contacto")
    public String contacto(ModelMap modelo) {
        modelo.addAttribute("listaAdmin", getAdmins());
        return "Contacto.html";
    }

    @PostMapping("/contactar")
    public String contactar(HttpSession session, String mensaje, Long id) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if (usuario == null) {
            usuario = new Usuario();
            usuario.setName("Anonimo");
            usuario.setID(-1l);
        }
        try {
            notificacionServicio.crearNotificacion(mensaje, usuario, id);
        } catch (MiException e) {
            System.err.println(e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/")
    public String index(HttpSession session) {
        return "index.html";
    }

    @GetMapping("/buscarProveedores")
    public String buscarProveedores(@RequestParam String ocupacion, Model model) {
        List<Usuario> proveedores = usuarioServicio.obtenerProveedoresPorOcupacion(ocupacion);
        model.addAttribute("proveedores", proveedores);
        model.addAttribute("listaOcupaciones", getOcupaciones());
        return "card.html";
    }

    @GetMapping("/servicios")
    public String servicios(ModelMap modelo) {
        modelo.addAttribute("listaOcupaciones", getOcupaciones());
        return "servicios.html";
    }

    @GetMapping("/seleccionar")
    public String seleccionar() {
        return "selector.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN' )")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if (usuario != null) {
            modelo.put("usuario", usuario);
            return "datos_user.html";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/perfil/modificar")
    public String perfilModificar(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if (usuario != null) {
            modelo.put("usuario", usuario);
            modelo.addAttribute("provincia", getProvincias());
            modelo.addAttribute("ocupaciones", getOcupaciones());
            return "editar_user.html";
        } else {
            return "redirect:/login";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN' )")
    @PostMapping("/perfil/{id}")
    public String actualizar(HttpSession session, MultipartFile archivo, @PathVariable Long id, String email, String name, String direccion, String phone,
                             String password, String password2, Provincias provincia, Ocupaciones ocupacion) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        try {
        if(usuario.getRol().equals(Rol.CLIENTE)){
            usuarioServicio.actualizarCliente(archivo, email, name, direccion, phone, password, password2);
        } else if (usuario.getRol().equals(Rol.PROVEEDOR)) {
            usuarioServicio.actualizarProveedor(archivo,email,name,password,password2,phone,provincia, ocupacion.getNombre());
        }
        } catch (MiException e) {
            System.err.println(e.getMessage());
        }
        return "redirect:/perfil";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {
        if (error != null) {
            modelo.put("error", "Usuario o contraseña inválidos");
            System.err.println("bruv");
        } else {
            System.out.println("coolbeans");
        }
        return "login.html";
    }

    @PostMapping("/logincheck")
    public String logincheck() {
        System.out.println("pog");
        return "redirect:/";
    }

    @GetMapping("/registrar")
    public String registrar(Model modelo) {
        modelo.addAttribute("rol", getRol());
        modelo.addAttribute("ocupaciones", getOcupaciones());
        modelo.addAttribute("provincia", getProvincias());
        return "registro.html";
    }

    @GetMapping("/perfil/notificaciones")
    public String centroDeNotificaciones(HttpSession session, ModelMap modelo) {
        Usuario loggeado = (Usuario) session.getAttribute("usuariosession");
        if (loggeado == null) {
            return "redirect:/login";
        } else {
            try {
                List<Notificaciones> lista = usuarioServicio.mostrarTodasLasNotificacionesUsr(loggeado.getID());
                modelo.addAttribute("listaNotificaciones", lista);
                return "centroNotificaciones";
            } catch (MiException e) {
                System.err.println(e.getMessage());
                return "redirect:/login";
            }
        }
    }

    @PostMapping("/perfil/notificaciones/{idNotificacion}")
    public String NotificacionBorrar(@PathVariable Long idNotificacion, HttpSession session) {
        Usuario loggeado = (Usuario) session.getAttribute("usuariosession");
        if (loggeado == null) {
            return "redirect:/";
        } else {
            try {
                notificacionServicio.eliminarNotificacion(loggeado, idNotificacion);
            } catch (MiException e) {
                System.err.println(e.getMessage());
            }
            return "redirect:/";
        }
    }

    @PostMapping("/registro")
    public String registro(@RequestParam MultipartFile archivo, @RequestParam String email, @RequestParam String name,
                           @RequestParam String password, @RequestParam String password2, @RequestParam String phone,
                           @RequestParam Rol rol, @RequestParam Provincias provincia) {
        try {
            System.out.println("holas");
            usuarioServicio.crearUsuario(archivo, email, name, password, password2, phone, rol, provincia);
        } catch (MiException e) {
            System.err.println(e);
            return "index.html";
        }

        return "redirect:/";
    }

}