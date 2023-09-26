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
    private String phone;
    private Date fecharegistro;
    private String direccion;
    private Boolean activo;

    @OneToMany(mappedBy = "remitente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notificaciones> notificacionesEnviadas;

    @OneToMany(mappedBy = "receptor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notificaciones> notificacionesRecividas;

    @OneToOne
    private Imagen profilePicture;

    @Enumerated(EnumType.STRING)
    private Rol rol;
    @Enumerated(EnumType.STRING)
    private Provincias provincia;

    @ManyToOne
    @JoinColumn(name = "ocupacion_id")
    private Ocupaciones ocupacion;

    @OneToMany(mappedBy = "cliente")
    private List<Trabajo> trabajosCliente;

    @OneToMany(mappedBy = "proveedor")
    private List<Trabajo> trabajosProveedor;

    @OneToMany(mappedBy = "clienteEmisor")
    private List<Calificacion> calificacionesEmitidas;

    @OneToMany(mappedBy = "proveedorReceptor")
    private List<Calificacion> calificacionesRecibidas;

    public Usuario(String email, String name, String password, String phone, Date fecharegistro, Rol rol, Provincias provincia, String direccion) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.fecharegistro = fecharegistro;
        this.rol = rol;
        this.provincia = provincia;
        this.direccion = direccion;
    }

    public Usuario(String email, String name, String password, String phone, Date fecharegistro, String direccion, Rol rol, Provincias provincia, Ocupaciones ocupacion, List<Trabajo> trabajosCliente, List<Trabajo> trabajosProveedor, List<Calificacion> calificacionesEmitidas, List<Calificacion> calificacionesRecibidas) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.fecharegistro = fecharegistro;
        this.direccion = direccion;
        this.rol = rol;
        this.provincia = provincia;
        this.ocupacion = ocupacion;
        this.trabajosCliente = trabajosCliente;
        this.trabajosProveedor = trabajosProveedor;
        this.calificacionesEmitidas = calificacionesEmitidas;
        this.calificacionesRecibidas = calificacionesRecibidas;
    }

    public Usuario() {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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

    public Imagen getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Imagen profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Ocupaciones getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(Ocupaciones ocupacion) {
        this.ocupacion = ocupacion;
    }

    public List<Notificaciones> getNotificacionesEnviadas() {
        return notificacionesEnviadas;
    }

    public void setNotificacionesEnviadas(List<Notificaciones> notificacionesEnviadas) {
        this.notificacionesEnviadas = notificacionesEnviadas;
    }

    public List<Notificaciones> getNotificacionesRecividas() {
        return notificacionesRecividas;
    }

    public void setNotificacionesRecividas(List<Notificaciones> notificacionesRecividas) {
        this.notificacionesRecividas = notificacionesRecividas;
    }

    public void setOcupacion(String ocupacion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
