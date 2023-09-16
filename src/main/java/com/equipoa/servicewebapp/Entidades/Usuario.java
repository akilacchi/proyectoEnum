package com.equipoa.servicewebapp.Entidades;

import com.equipoa.servicewebapp.Enum.Provincias;
import com.equipoa.servicewebapp.Enum.Rol;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    private String email;
    private String name;
    private String password;
    private int phone;
    private Date fecharegistro;
    @Enumerated(EnumType.STRING)
    private Rol rol;
    @Enumerated(EnumType.STRING)
    private Provincias provincia;


    //atributos para cliente
    private String direccion;

    @OneToMany(mappedBy = "cliente")
    private List<Trabajo> trabajosCliente;

    @OneToMany(mappedBy = "proveedor")
    private List<Trabajo> trabajosProveedor;

    @OneToMany(mappedBy = "clienteEmisor")
    private List<Calificacion> calificacionesEmitidas;

    @OneToMany(mappedBy = "proveedorReceptor")
    private List<Calificacion> calificacionesRecibidas;

    public Usuario() {
    
    }

    @ManyToOne
    private Ocupaciones ocupacion;

    public Ocupaciones getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(Ocupaciones ocupacion) {
        this.ocupacion = ocupacion;
    }

    public Usuario(String email, String name, String password, int phone, Date fecharegistro, Rol rol, Provincias provincia) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.fecharegistro = fecharegistro;
        this.rol = rol;
        this.provincia = provincia;
    }

    public Usuario(Long ID, String email, String name, String password, int phone, Date fecharegistro, Rol rol, Provincias provincia, List<Trabajo> trabajosCliente, List<Trabajo> trabajosProveedor, List<Calificacion> calificacionesEmitidas, List<Calificacion> calificacionesRecibidas, String direccion) {
        this.ID = ID;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.fecharegistro = fecharegistro;
        this.rol = rol;
        this.provincia = provincia;
        this.trabajosCliente = trabajosCliente;
        this.trabajosProveedor = trabajosProveedor;
        this.calificacionesEmitidas = calificacionesEmitidas;
        this.calificacionesRecibidas = calificacionesRecibidas;
        this.direccion = direccion;
    }

    public List<Trabajo> getTrabajosCliente() {
        return trabajosCliente;
    }

    public void setTrabajosCliente(List<Trabajo> trabajosCliente) {
        this.trabajosCliente = trabajosCliente;
    }

    public List<Trabajo> getTrabajosProveedor() {
        return trabajosProveedor;
    }

    public void setTrabajosProveedor(List<Trabajo> trabajosProveedor) {
        this.trabajosProveedor = trabajosProveedor;
    }

    public List<Calificacion> getCalificacionesEmitidas() {
        return calificacionesEmitidas;
    }

    public void setCalificacionesEmitidas(List<Calificacion> calificacionesEmitidas) {
        this.calificacionesEmitidas = calificacionesEmitidas;
    }

    public List<Calificacion> getCalificacionesRecibidas() {
        return calificacionesRecibidas;
    }

    public void setCalificacionesRecibidas(List<Calificacion> calificacionesRecibidas) {
        this.calificacionesRecibidas = calificacionesRecibidas;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public Date getFecharegistro() {
        return fecharegistro;
    }

    public void setFecharegistro(Date fecharegistro) {
        this.fecharegistro = fecharegistro;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Provincias getProvincia() {
        return provincia;
    }

    public void setProvincia(Provincias provincia) {
        this.provincia = provincia;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }
}
