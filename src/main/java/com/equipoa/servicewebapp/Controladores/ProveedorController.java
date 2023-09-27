/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equipoa.servicewebapp.Controladores;

import com.equipoa.servicewebapp.Repositorios.TrabajoRepositorio;
import com.equipoa.servicewebapp.Repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/")
    public String perfilProveedor() {

        return ("perfilProveedor.html");

    }

 
}

    
    
    

