package com.egg.biblioteca.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.egg.biblioteca.entidades.Imagen;
import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.enumeraciones.Rol;
import com.egg.biblioteca.excepciones.MiExcepcion;
import com.egg.biblioteca.repositorios.UsuarioRepositorio;

import jakarta.servlet.http.HttpSession;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
    }

    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String email, String password, String password2) throws MiExcepcion {
        validar(nombre, email, password, password2);
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setRol(Rol.USER);
        Imagen imagen = imagenServicio.guardar(archivo);
        usuario.setImagen(imagen);
        usuarioRepositorio.save(usuario);
    }

    @Transactional
    public Usuario getOne(String id) {
        return usuarioRepositorio.getReferenceById(id);
    }

    @Transactional
    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
    }

    @Transactional
    public void cambiarRol(String id) {
        Usuario usuario = usuarioRepositorio.buscarPorId(id);
        if (usuario.getRol().equals(Rol.USER)) {
            usuario.setRol(Rol.ADMIN);
        } else {
            usuario.setRol(Rol.USER);
        }
        usuarioRepositorio.save(usuario);
    }

    @Transactional
    public void modificarUsuario(MultipartFile archivo, String id, String nombre, String email, String rol, String password, String password2) throws MiExcepcion {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            validar(nombre, email, password, password2);
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            usuario.setRol(rol.equals("ADMIN") ? Rol.ADMIN : Rol.USER);
            Imagen imagen = usuario.getImagen();
            if (!archivo.isEmpty()) {
                imagenServicio.eliminar(imagen.getId());
                Imagen nuevaImagen = imagenServicio.guardar(archivo);
                usuario.setImagen(nuevaImagen);
            }
            usuarioRepositorio.save(usuario);
            
        }
    }

    private void validar(String nombre, String email, String password, String password2) throws MiExcepcion {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiExcepcion("el nombre no puede ser nulo o estar vacío");
        }
        if (email.isEmpty() || email == null) {
            throw new MiExcepcion("el email no puede ser nulo o estar vacío");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiExcepcion("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");
        }
        if (!password.equals(password2)) {
            throw new MiExcepcion("Las contraseñas ingresadas deben ser iguales");
        }
    }
}
