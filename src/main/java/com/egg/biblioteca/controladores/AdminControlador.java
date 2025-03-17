package com.egg.biblioteca.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.enumeraciones.Rol;
import com.egg.biblioteca.servicios.UsuarioServicio;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/dashboard")
    public String panelAdministrativo() {
        return "panel.html";
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list.html";
    }

    @PostMapping("/cambiarRol/{id}")
    public String cambiarRol(@PathVariable String id) {
        usuarioServicio.cambiarRol(id);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/modificarUsuario/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        modelo.put("usuario", usuarioServicio.getOne(id));
        List<Rol> roles = Rol.getRoles();
        modelo.addAttribute("roles", roles);
        return "usuario_modificar.html";
    }

    @PostMapping("{id}")
    public String modificar(@PathVariable String id, String nombre, String email, String password, String rol, MultipartFile archivo, String password2, ModelMap modelo) {
        try {
            usuarioServicio.modificarUsuario(archivo, id, nombre, email, rol, password, password2);

            return "redirect:/admin/usuarios";
        } catch (Exception e) {
            modelo.put("error", e.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "usuario_modificar.html";
        }
    }
}
