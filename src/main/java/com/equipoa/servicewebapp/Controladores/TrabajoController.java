/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.equipoa.servicewebapp.Controladores;

import com.equipoa.servicewebapp.Entidades.Calificacion;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Servicios.TrabajoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Dell
 */
@Controller
@RequestMapping("/trabajos")
public class TrabajoController {

    @Autowired
    private TrabajoServicio trabajoServicio;

    // Otros métodos
    @GetMapping("/calificar/{idTrabajo}")
    public String mostrarFormularioCalificacion(@PathVariable Long idTrabajo, Model model) {
        model.addAttribute("idTrabajo", idTrabajo);
        model.addAttribute("calificacion", new Calificacion());
        return "calificarTrabajo";
    }

    @PostMapping("/calificar/{idTrabajo}")
    public String calificarTrabajo(@PathVariable Long idTrabajo, @ModelAttribute Calificacion calificacion) {
        try {
            trabajoServicio.calificarTrabajo(idTrabajo, calificacion.getComentario(), calificacion.getPuntuacion());
            return "redirect:/trabajos"; // Redirige a la lista de trabajos o a una página de éxito
        } catch (MiException e) {
            return "error";
        }
    }
}
