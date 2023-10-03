/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equipoa.servicewebapp.Controladores;

import com.equipoa.servicewebapp.Entidades.Calificacion;
import com.equipoa.servicewebapp.Entidades.Usuario;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Repositorios.TrabajoRepositorio;
import com.equipoa.servicewebapp.Repositorios.UsuarioRepositorio;
import com.equipoa.servicewebapp.Servicios.UsuarioServicio;
import java.util.List;
import java.util.Optional;
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


    @GetMapping("/")
    public String perfilProveedor(@RequestParam Long idProveedor, Model model) {
        Optional<Usuario> proveedorOpt = usuarioServicio.obtenerProveedorConCalificaciones(idProveedor);
        if (proveedorOpt.isPresent()) {
            Usuario proveedor = proveedorOpt.get(); 
            model.addAttribute("proveedor", proveedor);

            List<Calificacion> calificaciones = proveedor.getCalificacionesRecibidas();
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

    
    
}