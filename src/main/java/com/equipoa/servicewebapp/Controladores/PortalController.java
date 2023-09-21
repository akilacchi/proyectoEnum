package com.equipoa.servicewebapp.Controladores;

import com.equipoa.servicewebapp.Entidades.Ocupaciones;
import com.equipoa.servicewebapp.Entidades.Usuario;
import com.equipoa.servicewebapp.Enum.Provincias;
import com.equipoa.servicewebapp.Enum.Rol;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Repositorios.OcupacionesRepositorio;
import com.equipoa.servicewebapp.Servicios.OcupacionesServicio;
import com.equipoa.servicewebapp.Servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/")
public class PortalController {

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
    private OcupacionesRepositorio ocupacionesRepositorio;
    @Autowired
    private OcupacionesServicio ocupacionesServicio;

    @GetMapping("/")
    public String index(HttpSession session) {
//        Usuario loggeado = (Usuario) session.getAttribute("usuariosession");
//        if(loggeado.getRol().toString().equals("ADMIN")){
//            return "redirect:/admindashboard/";
//        }else{
        return "index.html";

//        }
    }

    @GetMapping("/buscarProveedores")
    public String buscarProveedores(@RequestParam String ocupacion, Model model) {
        List<Usuario> proveedores = usuarioServicio.obtenerProveedoresPorOcupacion(ocupacion);
        model.addAttribute("proveedores", proveedores);
        return "card.html";
    }

    @GetMapping("/servicios")
    public String servicios() {
        return "servicios.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN' )")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        if (usuario != null) {
            modelo.put("usuario", usuario);
            return "perfil.html";
        } else {
            return "redirect:/login";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENTE', 'ROLE_PROVEEDOR', 'ROLE_ADMIN' )")
    @PostMapping("/perfil/{id}")
    public String actualizar(MultipartFile archivo, Long id, String email, String nombre, String direccion, String phone, String password, String password2) {
        try {
            usuarioServicio.actualizarCliente(archivo, email, nombre, direccion, phone, password, password2);
        } catch (MiException e) {
            System.err.println(e.getMessage());
            return "usuario_modificar.html";
        }
        return "redirect:/pefil";
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
