/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructuras.Arboles.B;

import Estructuras.Arboles.Comparador;

/**
 *
 * @author alejandro
 */
public class ArbolB {

    int grado;
    NodoB raiz;

    public ArbolB(int grado) {
        this.grado = grado;
        this.raiz = null;
    }

    public boolean estaVacio() {
        return raiz == null;
    }
    public boolean insertar(Object dato){
        boolean repetido = false;
        Comparador comp = (Comparador)dato;
        repetido = Insertar(raiz,comp);
        return repetido;
    }
    public boolean Insertar(NodoB raiz, Comparador comp){
    boolean repetido = false;
    if(raiz == null){
        NodoB nuevo = new NodoB(grado);
        nuevo.getClaves().insertar(comp);
    }
    return repetido;
    }
}
