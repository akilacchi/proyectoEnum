package com.equipoa.servicewebapp.Controladores;

import com.equipoa.servicewebapp.Enum.Provincias;
import com.equipoa.servicewebapp.Enum.Rol;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalController {

    public Rol[] getRol(){
        return  Rol.values();
    }

    public Provincias[] getProvincias(){
        return Provincias.values();
    }

    @Autowired
    UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String index(){
        return "index.html";
    }
    
    @GetMapping("/servicio")
    public String servicio(){
        return "servicio.html";
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo){
        if(error!=null){
            modelo.put("error","Usuario o contraseña inválidos");
            System.err.println("bruv");
        }else{
            System.out.println("coolbeans");
        }
        return "login.html";
    }

    @PostMapping("/logincheck")
    public String logincheck(){
        return "redirect:/";
    }

    @GetMapping("/registrar")
    public String registrar(Model modelo){
        modelo.addAttribute("rol",getRol());
        modelo.addAttribute("provincia", getProvincias());
        return "registro.html";
    }
    @GetMapping("/registrarProveedor")
    public String registrarProveedor(Model modelo){
        modelo.addAttribute("rol",getRol());
        modelo.addAttribute("provincia", getProvincias());
        return "registroProveedor.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam String email, @RequestParam String name, @RequestParam String password, @RequestParam String password2, @RequestParam int phone, @RequestParam Rol rol, @RequestParam Provincias provincia){
        try {
            System.out.println("holas");
            usuarioServicio.crearUsuario(email,name,password,password2,phone,rol, provincia);
        } catch (MiException e) {
            System.err.println(e);
            return "index.html";
        }

        return "redirect:/";
    }
     @GetMapping("/select")
    public String selectRol(Model modelo){
        modelo.addAttribute("rol",getRol());
        modelo.addAttribute("provincia", getProvincias());
        return "selectClienteProveedor.html";
    }
    
    
}
