package com.equipoa.servicewebapp.Controladores;

import com.equipoa.servicewebapp.Entidades.Calificacion;
import com.equipoa.servicewebapp.Enum.Estados;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Repositorios.TrabajoRepositorio;
import com.equipoa.servicewebapp.Repositorios.UsuarioRepositorio;
import com.equipoa.servicewebapp.Servicios.TrabajoServicio;
import com.equipoa.servicewebapp.Servicios.UsuarioServicio;
import java.util.Date;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author alejandrasuarez
 */
@Controller
@RequestMapping("/trabajo")
public class TrabajoController {

    @Autowired
    TrabajoRepositorio trabajoRepositorio;
    UsuarioRepositorio usuarioRepositorio;
    @Autowired
    TrabajoServicio trabajoServicio;
    @Autowired
    UsuarioServicio usuarioServicio;

    /**
     *
     * @param Long
     * @return
     */
    @GetMapping("/solicitarservicio/{id}")
    public String solicitarTrabajo(@PathVariable Long id, ModelMap modelo) throws MiException {
        
        modelo.put("proveedor", usuarioServicio.getOne(id));
        

        return "solicitarTrabajo.html";
    }

    @GetMapping("/trabajoSolicitado/")
    public String trabajoSolicitado(/*@PathVariable String id, ModelMap modelo**/) {
        
       // modelo.put("direccion", trabajoServicio.getOne(id));

        return "trabajoSolicitado.html";
    }
    
    @PostMapping("/solicitarservicio/{id}")
    public String enviarSolicitud(@PathVariable Long id, @RequestParam  @DateTimeFormat(pattern="yyyy-MM-dd")Date fechaInicio, @RequestParam String direccion, @RequestParam String descripcion,HttpSession session, ModelMap modelo) throws MiException {

        try {
            
            trabajoServicio.registrarTrabajo(descripcion, fechaInicio, fechaInicio, Estados.ACEPTADO, session,id);
            
            modelo.put("exito", "trabajo solicitado con éxito");

            return "index.html";
        } 
        
        catch (MiException e) {

            modelo.put("error", "Error al enviar la solicitud de trabajo");
            
            return "solicitarTrabajo.html";

        }
    
    }

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
            return "redirect:/trabajo"; 
        } catch (MiException e) {
            return "error";
        }
    }
}
