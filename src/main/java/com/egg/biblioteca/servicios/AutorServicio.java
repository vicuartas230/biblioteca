package com.egg.biblioteca.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.egg.biblioteca.entidades.Autor;
import com.egg.biblioteca.repositorios.AutorRepositorio;


@Service
public class AutorServicio {
    @Autowired
    private AutorRepositorio autorRepositorio;
    
    
    @Transactional
    public void crearAutor(String nombre){
                
    Autor autor = new Autor();// Instancio un objeto del tipo Autor
    autor.setNombre(nombre);// Seteo el atributo, con el valor recibido como parámetro


    autorRepositorio.save(autor); // Persisto el dato en mi BBDD
    }
}
