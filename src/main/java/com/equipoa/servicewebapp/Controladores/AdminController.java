package com.equipoa.servicewebapp.Controladores;

import com.equipoa.servicewebapp.Entidades.Ocupaciones;
import com.equipoa.servicewebapp.Entidades.Usuario;
import com.equipoa.servicewebapp.Enum.Provincias;
import com.equipoa.servicewebapp.Enum.Rol;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Repositorios.OcupacionesRepositorio;
import com.equipoa.servicewebapp.Repositorios.UsuarioRepositorio;
import com.equipoa.servicewebapp.Servicios.AdminServicio;
import com.equipoa.servicewebapp.Servicios.OcupacionesServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/admindashboard")
public class AdminController {

    @Autowired
    private AdminServicio adminServicio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private OcupacionesRepositorio ocupacionesRepositorio;
    @Autowired
    private OcupacionesServicio ocupacionesServicio;

    private List<Usuario> getClientes() {
        return usuarioRepositorio.findAllByRol(Rol.CLIENTE);
    }

    private Provincias[] getProvincias() {
        return Provincias.values();
    }

    private List<Ocupaciones> getOcupaciones() {
        return ocupacionesRepositorio.findAll();
    }

    private List<Usuario> getActivos() {
        return usuarioRepositorio.findAllActivo();
    }

    private List<Usuario> getInactivos() {
        return usuarioRepositorio.findAllInactivo();
    }

    @GetMapping("/")
    public String adminDashboard() {
        return "adminDashboard.html";
    }

    @GetMapping("/crearAdmin")
    public String crearUser(ModelMap modelo) {
        modelo.addAttribute("provincia", getProvincias());
        return "registroAdmin.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam MultipartFile archivo, @RequestParam String email, @RequestParam String name,
            @RequestParam String password, @RequestParam String password2, @RequestParam String phone,
            @RequestParam Provincias provincia) {
        try {
            System.out.println("Registro admin existoso");
            adminServicio.crearAdmin(archivo, email, name, password, password2, phone, provincia);
        } catch (MiException e) {
            System.err.println(e.getMessage());
            return "redirect:/admindashboard/";
        }

        return "redirect:/";
    }

    @GetMapping("/crearocupacion")
    public String crearOcupacion() {
        return "crearOcupacion.html";
    }

    @PostMapping("/ocupacion")
    public String postOcupacion(@RequestParam MultipartFile archivo, @RequestParam String nombre, @RequestParam String descripcion) {
        try {
            ocupacionesServicio.crearNuevaOcupacion(archivo, nombre, descripcion);
            System.out.println("coolbeans");
        } catch (MiException e) {
            System.err.println(e.getMessage());
        } finally {
            return "redirect:/";
        }
    }

    @GetMapping("/modificarocupacion")
    public String modificarOcupacion(ModelMap modelo) {
        modelo.addAttribute("listaOcupacion", getOcupaciones());
        return "modificarOcupacion.html";
    }

    @PostMapping("/ocupacionmodificar")
    public String ocupacionModificar(MultipartFile archivo, String nombre, String nuevoNombre, String descripcion) {
        if (archivo == null) {
            System.err.println("archivo no llego a controller");
        }

        try {
            adminServicio.modificarOcupacion(archivo, nombre, nuevoNombre, descripcion);
        } catch (MiException e) {
            System.err.println(e.getMessage());
        }
        return "redirect:/admindashboard/";
    }

    @GetMapping("/borrarocupacion")
    public String borrarocupacion(ModelMap modelo) {
        modelo.addAttribute("listaOcupacion", getOcupaciones());
        return "borrarOcupacion.html";
    }

    @PostMapping("/ocupacionborrar")
    public String postBorrarOcupacion(@RequestParam String ocupacion) {
        try {
            ocupacionesServicio.eliminarOcupacion(ocupacion);
            System.out.println("coolbeans");
        } catch (MiException e) {
            System.err.println(e.getMessage());
        } finally {
            return "redirect:/admindashboard/borrarocupacion";
        }
    }

    @GetMapping("/cambiorol")
    public String cambioRol(ModelMap modelo) {
        modelo.addAttribute("listaUsuarios", getClientes());
        return "cambioRol.html";
    }

    @PostMapping("/rolcambio")
    public String rolCambio(Long id) {
        try {
            adminServicio.modificarRol(id);
        } catch (MiException e) {
            System.err.println(e.getMessage());
        }
        return "redirect:/admindashboard/";
    }

    @GetMapping("/activar")
    public String activar(ModelMap modelo) {
        modelo.addAttribute("activos", getActivos());
        modelo.addAttribute("inactivos", getInactivos());
        return "activar.html";
    }

    @PostMapping("/setactivo")
    public String setActivo(Long id) {
        try {
            adminServicio.activarUsuario(id);
        } catch (MiException e) {
            System.err.println(e.getMessage());
        }
        return "redirect:/admindashboard/";
    }

    @PostMapping("/setinactivo")
    public String setInactivo(Long id) {
        try {
            adminServicio.desactivarUsuario(id);
        } catch (MiException e) {
            System.err.println(e.getMessage());
        }
        return "redirect:/admindashboard/";
    }

}
