package com.egg.biblioteca.servicios;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.entidades.Editorial;
import com.egg.biblioteca.entidades.Libro;
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
    public void crearLibro(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) {
        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setAlta(new Date());
        libro.setTitulo(titulo);
        libro.setEjemplares(ejemplares);
        Autor autor = autorRepositorio.findById(UUID.fromString(idAutor)).get();
        Editorial editorial = editorialRepositorio.findById(UUID.fromString(idEditorial)).get();
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        libroRepositorio.save(libro);
    }
}
