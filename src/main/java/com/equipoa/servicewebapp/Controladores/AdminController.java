package com.equipoa.servicewebapp.Controladores;

import com.equipoa.servicewebapp.Entidades.Ocupaciones;
import com.equipoa.servicewebapp.Enum.Provincias;
import com.equipoa.servicewebapp.Enum.Rol;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Repositorios.OcupacionesRepositorio;
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
    private OcupacionesRepositorio ocupacionesRepositorio;
    @Autowired
    private OcupacionesServicio ocupacionesServicio;

    public Rol[] getRol() {
        return Rol.values();
    }

    public Provincias[] getProvincias() {
        return Provincias.values();
    }

    public List<Ocupaciones> getOcupaciones() {
        return ocupacionesRepositorio.findAll();
    }

    @GetMapping("/")
    public String adminDashboard() {
        return "Inserte html";
    }

    @GetMapping("/crearuser")
    public String crearUser(ModelMap modelo) {
        modelo.addAttribute("rol", getRol());
        modelo.addAttribute("provincia", getProvincias());
        modelo.addAttribute("listaOcupacion", getOcupaciones());
        return "registro.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam MultipartFile archivo, @RequestParam String email, @RequestParam String name,
                           @RequestParam String password, @RequestParam String password2, @RequestParam String phone,
                           @RequestParam Rol rol, @RequestParam Provincias provincia, @RequestParam String ocupacion) {
        try {
            System.out.println("holas");
            adminServicio.crearUsuario(archivo, email, name, password, password2, phone, rol, provincia, ocupacion);
        } catch (MiException e) {
            System.err.println(e.getMessage());
            return "index.html";
        }

        return "redirect:/";
    }

    @GetMapping("/crearocupacion")
    public String crearOcupacion() {
        return "crearOcupacion.html";
    }

    @PostMapping("/ocupacion")
    public String postOcupacion(@RequestParam String ocupacion) {
        try {
            ocupacionesServicio.crearNuevaOcupacion(ocupacion);
            System.out.println("coolbeans");
        } catch (MiException e) {
            System.err.println(e.getMessage());
        } finally {
            return "redirect:/";
        }
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
            return "redirect:/";
        }
    }


}
