/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equipoa.servicewebapp.Controladores;

import com.equipoa.servicewebapp.Entidades.Calificacion;
import com.equipoa.servicewebapp.Entidades.Trabajo;
import com.equipoa.servicewebapp.Entidades.Usuario;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Repositorios.TrabajoRepositorio;
import com.equipoa.servicewebapp.Repositorios.UsuarioRepositorio;
import com.equipoa.servicewebapp.Servicios.TrabajoServicio;
import com.equipoa.servicewebapp.Servicios.UsuarioServicio;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author alejandrasuarez
 */
@Controller
@RequestMapping("/proveedor")
public class ProveedorController {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private TrabajoRepositorio trabajoRepositorio;

    @Autowired
    UsuarioServicio usuarioServicio;
    @Autowired
    TrabajoServicio trabajoServicio;

//    @GetMapping("/")
//    public String perfilProveedor() {
//
//        return ("perfilProveedor.html");
//
//    }
   @GetMapping("/{id}")
    public String perfilProveedor(@PathVariable Long id, Model model, ModelMap modelo) throws MiException {
        
        modelo.put("proveedor", usuarioServicio.getOne(id));
        Optional<Usuario> proveedor = usuarioServicio.obtenerProveedorConCalificaciones(id);

        if (proveedor.isPresent()) {
            Usuario usuarioProveedor = proveedor.get();
            model.addAttribute("proveedor", usuarioProveedor); // añade el objeto proveedor completo al modelo
            List<Calificacion> calificaciones = usuarioProveedor.getCalificacionesRecibidas();
            if (calificaciones != null) {
                double promedio = calificaciones.stream()
                        .mapToInt(Calificacion::getPuntuacion)
                        .average()
                        .orElse(0.0);
                model.addAttribute("calificaciones", calificaciones);
                model.addAttribute("promedio", promedio);
            }
        }
        return "perfilProveedor.html";
    }
    
    @GetMapping("/loginproveedor")
    public String loginProveedor(HttpSession session, ModelMap modelo){
        
         Usuario logueado = (Usuario) session.getAttribute("usuariosession");
         System.out.println("El ID de la sesion es: "+logueado.getID());
         List<Trabajo> trabajos = trabajoServicio.listaTrabajosPorUsuario(logueado.getID());
         System.out.println("Trabajos: " +trabajos);
         modelo.addAttribute("proveedor", logueado);
         modelo.addAttribute("trabajos", trabajos);
         return "login_proveedor.html";
    
    }

    
    
}