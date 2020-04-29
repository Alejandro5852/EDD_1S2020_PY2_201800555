/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructuras.Listas.DoblementeEnlazada;

import Estructuras.Listas.NodoL;

/**
 *
 * @author alejandro
 */
public class DobleMenteEnlazada {

    private String puerto;
    private NodoL cabeza;
    private int tamaño;

    public DobleMenteEnlazada() {
        this.cabeza = null;
        this.tamaño = 0;
        this.puerto = "";
    }

    public void insertar(Object dato) {
        NodoL nuevo = new NodoL(dato);
        if (estaVacio()) {
            this.cabeza = nuevo;
        } else {
            NodoL auxiliar = this.cabeza;
            while (auxiliar.getSiguiente() != this.cabeza) {
                auxiliar = auxiliar.getSiguiente();
            }
            auxiliar.setSiguiente(nuevo);
            nuevo.setAnterior(auxiliar);
            nuevo.setSiguiente(this.cabeza);
            this.cabeza.setAnterior(nuevo);
        }
        this.tamaño++;
    }

    public boolean existe(Object dato) {
        boolean existe = false;
        if (!estaVacio()) {
            NodoL temp = this.cabeza;
            for (int i = 0; i < tamaño; i++) {
                if (temp.getValor() == dato) {
                    existe = true;
                    break;
                } else {
                    temp = temp.getSiguiente();
                }
            }
        }
        return existe;
    }

    public NodoL getCabeza() {
        return cabeza;
    }

    public int getTamaño() {
        return tamaño;
    }

    public boolean estaVacio() {
        return cabeza == null;
    }

    public void dot() {
        String Dot = "digraph G{\nnode[shape = record, style=filled, fillcolor=red]\noverlap=false;\nsplines=true;\ngraph[dpi=90];\n";
        Dot += "label =  <<font point-size='20'>Lista doblemente enlazada BLOCKCHAIN del puerto: " + this.puerto+"</font>>;\nlabelloc = \"t \";\n";
        NodoL aux = cabeza;
    }
}
