package com.equipoa.servicewebapp.Servicios;

import com.equipoa.servicewebapp.Entidades.Calificacion;
import com.equipoa.servicewebapp.Entidades.Usuario;
import com.equipoa.servicewebapp.Excepciones.MiException;
import com.equipoa.servicewebapp.Repositorios.CalificacionRepositorio;
import com.equipoa.servicewebapp.Repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class CalificacionServicio {

    @Autowired
    private CalificacionRepositorio calificacionRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void crearCalificacion(Long idCliente, Long idProveedor, String comentario, int puntuacion) throws MiException {
        validar(idCliente, idProveedor, comentario, puntuacion);
        Usuario cliente;
        Usuario proveedor;

        Calificacion calificacion = new Calificacion();
        Optional<Usuario> respuestaCliente = usuarioRepositorio.findById(idCliente);
        Optional<Usuario> respuestaProveedor = usuarioRepositorio.findById(idProveedor);
        if (!respuestaCliente.isPresent()) {
            throw new MiException("Cliente no encontrado");
        } else if (!respuestaProveedor.isPresent()) {
            throw new MiException("Proveedor no encontrado");
        } else {
            cliente = respuestaCliente.get();
            proveedor = respuestaProveedor.get();

            calificacion.setClienteEmisor(cliente);
            calificacion.setProveedorReceptor(proveedor);
            calificacion.setComentario(comentario);
            calificacion.setPuntuacion(puntuacion);

            calificacionRepositorio.save(calificacion);
        }
    }

    @Transactional
    public void modificarCalificacion(Long idCalificacion, Long idCliente, Long idProveedor, String comentario, int puntuacion) throws MiException {
        validar(idCliente, idProveedor, comentario, puntuacion);

        if (idCalificacion < 1 || idCalificacion == null) {
            throw new MiException("Calificacion inexistente");
        }

        Usuario cliente;
        Usuario proveedor;

        Optional<Calificacion> respuesta = calificacionRepositorio.findById(idCalificacion);
        Optional<Usuario> respuestaCliente = usuarioRepositorio.findById(idCliente);
        Optional<Usuario> respuestaProveedor = usuarioRepositorio.findById(idProveedor);

        if (!respuestaCliente.isPresent()) {
            throw new MiException("Cliente no encontrado");
        } else if (!respuestaProveedor.isPresent()) {
            throw new MiException("Proveedor no encontrado");
        } else if (!respuesta.isPresent()) {
            throw new MiException("Calificacion no encontrda");
        } else {

            Calificacion calificacion = respuesta.get();

            cliente = respuestaCliente.get();
            proveedor = respuestaProveedor.get();

            calificacion.setClienteEmisor(cliente);
            calificacion.setProveedorReceptor(proveedor);
            calificacion.setComentario(comentario);
            calificacion.setPuntuacion(puntuacion);

            calificacionRepositorio.save(calificacion);
        }
    }

    @Transactional
    public void eliminarCalificacion(Long idCalificacion, Long idCliente) throws MiException {
        if (idCliente < 1 || idCliente == null) {
            throw new MiException("Cliente inexistente");
        }
        if (idCalificacion == null || idCalificacion < 1) {
            throw new MiException("Calificacion inexistente");
        }

        Optional<Calificacion> respuesta = calificacionRepositorio.findById(idCalificacion);
        Optional<Usuario> usr = usuarioRepositorio.findById(idCliente);
        if (!usr.isPresent()) {
            throw new MiException("Cliente no encontrado");
        } else if (!respuesta.isPresent()) {
            throw new MiException("Calificacion no encontrada");
        } else {
            Calificacion calificacion = respuesta.get();

            if (Objects.equals(calificacion.getClienteEmisor().getID(), idCliente)) {

                calificacionRepositorio.delete(calificacion);
                System.out.println("Calificacion eliminada exitosamente");
            } else {
                throw new MiException("Sólo un administrador o el cliente que escribió la calificación pueden eliminarla.");
            }
        }
    }

    private void validar(Long idCliente, Long idProveedor, String comentario, int puntuacion) throws MiException {
        if (idCliente < 1 || idCliente == null) {
            throw new MiException("Cliente inexistente");
        }
        if (idProveedor < 1 || idProveedor == null) {
            throw new MiException("Proveedor inexistente");
        }
        if (comentario.trim().isEmpty() || comentario == null) {
            throw new MiException("Comentario vacío");
        }
        if (puntuacion < 0 || puntuacion > 10) {
            throw new MiException("Puntuacion fuera de rango");
        }
    }
}
