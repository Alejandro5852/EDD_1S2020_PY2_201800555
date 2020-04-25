/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructuras.Arboles.B;

import Estructuras.Listas.SimplementeEnlazada.SimpleMenteEnlazada;

/**
 *
 * @author alejandro
 */
public class NodoB {

    private int grado;
    private SimpleMenteEnlazada claves;
    private SimpleMenteEnlazada enlaces;

    public NodoB(int grado) {
        this.grado = grado;
        this.claves = new SimpleMenteEnlazada();
        this.enlaces = new SimpleMenteEnlazada();
    }

    public SimpleMenteEnlazada getClaves() {
        return claves;
    }

    public void setClaves(SimpleMenteEnlazada claves) {
        this.claves = claves;
    }

    public SimpleMenteEnlazada getEnlaces() {
        return enlaces;
    }

    public void setEnlaces(SimpleMenteEnlazada enlaces) {
        this.enlaces = enlaces;
    }

    public boolean insertar(Object valor) {
        boolean cupo = false;
        if (claves.estaVacio()) {
            claves.insertar(valor);
            cupo = true;
        } else if (claves.Tamaño() < (grado - 1)) {
            claves.insertar(valor);
            cupo = true;
        } else {
            cupo = false;
        }
        return cupo;
    }
}
