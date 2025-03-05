package com.egg.biblioteca.servicios;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.entidades.Libro;
import com.egg.biblioteca.excepciones.MiExcepcion;
import com.egg.biblioteca.repositorios.AutorRepositorio;
import com.egg.biblioteca.repositorios.EditorialRepositorio;
import com.egg.biblioteca.repositorios.LibroRepositorio;

@Service
public class LibroServicio {
    @Autowired
    private LibroRepositorio libroRepositorio;
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional
    public void crearLibro(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiExcepcion {
        validar(titulo);
        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setAlta(new Date());
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        Autor autor = autorRepositorio.findById(idAutor).get();
        Editorial editorial = editorialRepositorio.findById(idEditorial).get();
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        libroRepositorio.save(libro);
    }

    @Transactional(readOnly = true)
    public List<Libro> listarLibros() {
        List<Libro> libros = new ArrayList<>();

        libros = libroRepositorio.findAll();
        return libros;
    }

    @Transactional
    public void modificarLibro(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws MiExcepcion {
        validar(titulo);
        Optional<Libro> respuesta = libroRepositorio.findById(isbn);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            libro.setTitulo(titulo);
            libro.setEjemplares(ejemplares);
            Autor autor = autorRepositorio.findById(idAutor).get();
            Editorial editorial = editorialRepositorio.findById(idEditorial).get();
            libro.setAutor(autor);
            libro.setEditorial(editorial);
            libroRepositorio.save(libro);
        }
    }

    @Transactional(readOnly = true)
    public Libro getOne(Long isbn) {
        return libroRepositorio.getReferenceById(isbn);
    }

    private void validar(String titulo) throws MiExcepcion {
        if (titulo == null || titulo.isEmpty()) {
            throw new MiExcepcion("El título del libro no puede ser nulo ni estar vacío.");
        }
    }
}
