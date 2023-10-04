package com.equipoa.servicewebapp.Controladores;

import com.equipoa.servicewebapp.Entidades.Ocupaciones;
import com.equipoa.servicewebapp.Enum.Provincias;
import com.equipoa.servicewebapp.Enum.Rol;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Repositorios.OcupacionesRepositorio;
import com.equipoa.servicewebapp.Servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/r")
public class RegistroController {

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
    UsuarioServicio usuarioServicio;

    @Autowired
    OcupacionesRepositorio ocupacionesRepositorio;

    @GetMapping("/")
    public String registro() {
        return "redirect:/";
    }

    @GetMapping("/cliente")
    public String clietne(ModelMap modelo) {
        modelo.addAttribute("provincia", getProvincias());
        return "registroCliente.html";
    }

    @PostMapping("/registrocliente")
    public String registroCliente(MultipartFile archivo, String email, String name, String password, String password2, String phone, Provincias provincia, String direccion) {
        try {
            usuarioServicio.crearCliente(archivo, email, name, password, password2, phone, provincia, direccion);
        } catch (MiException e) {
            System.err.println(e.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/proveedor")
    public String proveedor(ModelMap modelo) {
        modelo.addAttribute("provincia", getProvincias());
        modelo.addAttribute("listaOcupacion", getOcupaciones());
        return "registroProveedor.html";
    }

    @PostMapping("/registroproveedor")
    public String registroProveedor(MultipartFile archivo, String email, String name, String password, String password2, String phone, Provincias provincia, String ocupacion) {
        try {
            usuarioServicio.crearProveedor(archivo, email, name, password, password2, phone, provincia, ocupacion);
        } catch (MiException e) {
            System.err.println(e.getMessage());
        }
        return "redirect:/";
    }
}
