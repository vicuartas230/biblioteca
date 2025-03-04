//Autor:

package com.egg.biblioteca.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

@Entity
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String nombre;

    public Autor() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}

//Editorial

package com.egg.biblioteca.entidades;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Editorial {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String nombre;

    public Editorial() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

}

//Libro:

package com.egg.biblioteca.entidades;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
public class Libro {

    @Id
    private Long isbn;

    private String titulo;
    private int ejemplares;

    @Temporal(TemporalType.DATE)
    private Date alta;

    @ManyToOne
    private Autor autor;

    @ManyToOne
    private Editorial editorial;

    public Libro() {
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getEjemplares() {
        return ejemplares;
    }

    public void setEjemplares(int ejemplares) {
        this.ejemplares = ejemplares;
    }

    public Date getAlta() {
        return alta;
    }

    public void setAlta(Date alta) {
        this.alta = alta;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

}



	@Transactional(readOnly = true)
    public List<Autor> listarAutores() {

        List<Autor> libros = new ArrayList<>();

        libros = autorRepositorio.findAll();
        return libros;
    }
    
  
 @Transactional(readOnly = true)
    public List<Editorial> listarEditoriales() {

        List<Editorial> libros = new ArrayList<>();

        libros = editorialRepositorio.findAll();
        return libros;
    }
    
    
   @Transactional
    public void modificarLibro(Long isbn, String titulo, Integer ejemplares, String idAutor, String idEditorial) throws ObjetoNoEncontrado{     
        Optional<Libro> respuesta = libroRepositorio.findById(isbn);
        Optional<Autor> respuestaAutor = autorRepositorio.findById(idAutor);
        Optional<Editorial> respuestaEditorial = editorialRepositorio.findById(idEditorial);
        if (respuesta.isPresent()) {
            Libro libro = respuesta.get();
            if (respuestaAutor.isPresent()){
                Autor autor = respuestaAutor.get();
                libro.setAutor(autor);
            }else{
                throw new ObjetoNoEncontrado("No existe ese Autor");
            }
            if (respuestaEditorial.isPresent()){
                Editorial editorial = respuestaEditorial.get();
                libro.setEditorial(editorial);
            }else{
                throw new ObjetoNoEncontrado("No existe esa Editorial");
            }
            libro.setTitulo(titulo);
            libro.setEjemplares(ejemplares);
            
            libroRepositorio.save(libro);
        }
    }
    
    @Transactional
    public void modificarEditorial(String nombre, String id){     
        Optional<Editorial> respuesta = editorialRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Editorial editorial = respuesta.get();
           
            editorial.setNombre(nombre);
            editorialRepositorio.save(editorial);
        }
    }
    
  public class ObjetoNoEncontrado extends Exception{

    public ObjetoNoEncontrado(String msg){
        super(msg);
    }
    
}
