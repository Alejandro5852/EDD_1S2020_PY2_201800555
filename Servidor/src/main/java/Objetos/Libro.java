/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import Estructuras.Arboles.Comparador;

/**
 *
 * @author alejandro
 */
public class Libro implements Comparador {

    private int ISBN;
    private int año;
    private String idioma;
    private String Titulo;
    private String Editorial;
    private String Autor;
    private int Edicion;
    private String Categoria;
    private int carnet;

    @Override
    public boolean igualQue(Object q) {
        Libro aux = (Libro) q;
        return ISBN == aux.getISBN();
    }

    @Override
    public boolean menorQue(Object q) {
        Libro aux = (Libro) q;
        return ISBN < aux.getISBN();
    }

    @Override
    public boolean menorIgualQue(Object q) {
        Libro aux = (Libro) q;
        return ISBN < aux.getISBN() || ISBN == aux.getISBN();
    }

    @Override
    public boolean mayorQue(Object q) {
        Libro aux = (Libro) q;
        return ISBN > aux.getISBN();
    }

    @Override
    public boolean mayorIgualQue(Object q) {
        Libro aux = (Libro) q;
        return ISBN == aux.getISBN() || ISBN > aux.getISBN();
    }

    /**
     * @return the ISBN
     */
    public int getISBN() {
        return ISBN;
    }

    /**
     * @param ISBN the ISBN to set
     */
    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * @return the año
     */
    public int getAño() {
        return año;
    }

    /**
     * @param año the año to set
     */
    public void setAño(int año) {
        this.año = año;
    }

    /**
     * @return the idioma
     */
    public String getIdioma() {
        return idioma;
    }

    /**
     * @param idioma the idioma to set
     */
    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    /**
     * @return the Titulo
     */
    public String getTitulo() {
        return Titulo;
    }

    /**
     * @param Titulo the Titulo to set
     */
    public void setTitulo(String Titulo) {
        this.Titulo = Titulo;
    }

    /**
     * @return the Editorial
     */
    public String getEditorial() {
        return Editorial;
    }

    /**
     * @param Editorial the Editorial to set
     */
    public void setEditorial(String Editorial) {
        this.Editorial = Editorial;
    }

    /**
     * @return the Autor
     */
    public String getAutor() {
        return Autor;
    }

    /**
     * @param Autor the Autor to set
     */
    public void setAutor(String Autor) {
        this.Autor = Autor;
    }

    /**
     * @return the Edicion
     */
    public int getEdicion() {
        return Edicion;
    }

    /**
     * @param Edicion the Edicion to set
     */
    public void setEdicion(int Edicion) {
        this.Edicion = Edicion;
    }

    /**
     * @return the Categoria
     */
    public String getCategoria() {
        return Categoria;
    }

    /**
     * @param Categoria the Categoria to set
     */
    public void setCategoria(String Categoria) {
        this.Categoria = Categoria;
    }

    public Libro(int ISBN, int año, String idioma, String Titulo, String Editorial, String Autor, int Edicion, String Categoria) {
        this.ISBN = ISBN;
        this.año = año;
        this.idioma = idioma;
        this.Titulo = Titulo;
        this.Editorial = Editorial;
        this.Autor = Autor;
        this.Edicion = Edicion;
        this.Categoria = Categoria;
        this.carnet = 0;
    }

    public int getCarnet() {
        return carnet;
    }

    public void setCarnet(int carnet) {
        this.carnet = carnet;
    }

    public Libro() {
    }

    public Libro(int ISBN, String Titulo, String Categoria) {
        this.ISBN = ISBN;
        this.Titulo = Titulo;
        this.Categoria = Categoria;
    }

}
