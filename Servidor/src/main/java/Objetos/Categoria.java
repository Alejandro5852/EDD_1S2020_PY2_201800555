/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import Estructuras.Arboles.Comparador;
import Estructuras.Arboles.B.BTree;
import Estructuras.Listas.SimplementeEnlazada.SimpleMenteEnlazada;

/**
 *
 * @author alejandro
 */
public class Categoria implements Comparador {

    private String Nombre;
    private String carpeta;
    BTree<Integer, Libro> arbol;
    private int Carnet;

    public int getCarnet() {
        return Carnet;
    }

    public void setCarnet(int Carnet) {
        this.Carnet = Carnet;
    }

    public BTree<Integer, Libro> getArbol() {
        return arbol;
    }

    public Categoria(String Nombre) {
        this.Nombre = Nombre;
        arbol = new BTree();
        arbol.setCategory(Nombre);
        this.carpeta = "";
    }

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
        this.arbol.setCarpeta(carpeta);
    }

    @Override
    public boolean igualQue(Object q) {
        Categoria aux = (Categoria) q;
        return getNombre().compareTo(aux.getNombre()) == 0;
    }

    @Override
    public boolean menorQue(Object q) {
        Categoria aux = (Categoria) q;
        return getNombre().compareTo(aux.getNombre()) < 0;
    }

    @Override
    public boolean menorIgualQue(Object q) {
        Categoria aux = (Categoria) q;
        return getNombre().compareTo(aux.getNombre()) == 0 || getNombre().compareTo(aux.getNombre()) < 0;
    }

    @Override
    public boolean mayorQue(Object q) {
        Categoria aux = (Categoria) q;
        return getNombre().compareTo(aux.getNombre()) > 0;
    }

    @Override
    public boolean mayorIgualQue(Object q) {
        Categoria aux = (Categoria) q;
        return getNombre().compareTo(aux.getNombre()) == 0 || getNombre().compareTo(aux.getNombre()) > 0;
    }

    /**
     * @return the Nombre
     */
    public String getNombre() {
        return Nombre;
    }

    /**
     * @param Nombre the Nombre to set
     */
    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public void insertarLibro(int ISBN, Libro libro) {
        this.arbol.insert(ISBN, libro);
        this.arbol.dot();
    }

    public void eliminarLibro(int ISBN) {
        this.arbol.delete(ISBN);
        this.arbol.dot();
    }

    public void BibliotecaUsuario(int Carnet, SimpleMenteEnlazada arg) {
        arbol.bibliotecaUsuario(arg, Carnet);
    }

    public void librosDeCategoria(SimpleMenteEnlazada arg) {
        arbol.libros(arg);
    }

    public long librosAsociados() {
        return arbol.size();
    }

}
