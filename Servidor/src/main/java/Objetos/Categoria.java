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
public class Categoria implements Comparador {

    String Nombre;

    @Override
    public boolean igualQue(Object q) {
        Categoria aux = (Categoria) q;
        return Nombre.compareTo(aux.Nombre) == 0;
    }

    @Override
    public boolean menorQue(Object q) {
        Categoria aux = (Categoria) q;
        return Nombre.compareTo(aux.Nombre) < 0;
    }

    @Override
    public boolean menorIgualQue(Object q) {
        Categoria aux = (Categoria) q;
        return Nombre.compareTo(aux.Nombre) == 0 ||Nombre.compareTo(aux.Nombre) < 0;
    }

    @Override
    public boolean mayorQue(Object q) {
        Categoria aux = (Categoria) q;
        return Nombre.compareTo(aux.Nombre) > 0;
    }

    @Override
    public boolean mayorIgualQue(Object q) {
        Categoria aux = (Categoria) q;
        return Nombre.compareTo(aux.Nombre) == 0 ||Nombre.compareTo(aux.Nombre) > 0;
    }

}
