package com.egg.biblioteca.controladores;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.entidades.Libro;
import com.egg.biblioteca.excepciones.MiExcepcion;
import com.egg.biblioteca.servicios.AutorServicio;
import com.egg.biblioteca.servicios.EditorialServicio;
import com.egg.biblioteca.servicios.LibroServicio;

@Controller
@RequestMapping("/libro")
public class LibroControlador {
    
    @Autowired
    private LibroServicio libroServicio;
    @Autowired
    private AutorServicio autorServicio;
    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/registrar")
    public String registrar(ModelMap modelo) {
        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);
        return "libro_form.html";
    }

    @PostMapping("/registro")
    public String registro(
        @RequestParam(required=false) Long isbn,
        @RequestParam String titulo,
        @RequestParam(required=false) Integer ejemplares,
        @RequestParam String idAutor,
        @RequestParam String idEditorial,
        ModelMap modelo
    ) {
        try {
            libroServicio.crearLibro(isbn, titulo, ejemplares, idAutor, idEditorial);
            modelo.put("exito", "El libro fue cargado correctamente!");
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            Logger.getLogger(AutorControlador.class.getName()).log(Level.SEVERE, null, ex);
            return "libro_form.html";
        }
        return "index.html";
    }

    @GetMapping("/lista")
    public String lista(ModelMap modelo) {
        List<Libro> libros = libroServicio.listarLibros();
        modelo.addAttribute("libros", libros);
        return "libro_list.html";
    }

    @GetMapping("/modificar/{isbn}")
    public String modificar(@PathVariable Long isbn, ModelMap modelo) {
        modelo.put("libro", libroServicio.getOne(isbn));
        List<Autor> autores = autorServicio.listarAutores();
        List<Editorial> editoriales = editorialServicio.listarEditoriales();
        modelo.addAttribute("autores", autores);
        modelo.addAttribute("editoriales", editoriales);
        return "libro_modificar.html";
    }

    @PostMapping("{isbn}")
    public String modificar(
        @PathVariable Long isbn,
        String titulo,
        Integer ejemplares,
        String idAutor,
        String idEditorial,
        ModelMap modelo
    ) {
        try {
            libroServicio.modificarLibro(isbn, titulo, ejemplares, idAutor, idEditorial);
            return "redirect:/libro/lista";
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            return "libro_modificar.html";
        }
    }
}
