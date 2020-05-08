/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructuras.Arboles.AVL;

import Estructuras.Listas.SimplementeEnlazada.SimpleMenteEnlazada;
import Objetos.Categoria;

/**
 *
 * @author alejandro
 */
public class NodoAVL {

    protected Object dato;
    protected NodoAVL izdo;
    protected NodoAVL dcho;

    int fe;

    public NodoAVL(Object valor) {
        dato = valor;
        izdo = dcho = null;
        fe = 0;
    }

    public NodoAVL(Object valor, NodoAVL ramaIzdo, NodoAVL ramaDcho) {
        dato = valor;
        izdo = ramaIzdo;
        dcho = ramaDcho;
        fe = 0;
    }
    //operaciones de acceso

    public Object valorNodo() {
        return dato;
    }

    public NodoAVL subarbolIzdo() {
        return izdo;
    }

    public NodoAVL subarbolDcho() {
        return dcho;
    }

    public void nuevoValor(Object d) {
        dato = d;
    }

    public void ramaIzdo(NodoAVL n) {
        izdo = n;
    }

    public void ramaDcho(NodoAVL n) {
        dcho = n;
    }

    public String dot() {
        String Dot = "";
        Categoria aux = (Categoria) this.dato;
        SimpleMenteEnlazada a = new SimpleMenteEnlazada();
        aux.librosDeCategoria(a);
        int num = a.Tamaño();
        Dot += "\"" + aux.getNombre() + "\" [label = \"<C0>|" + aux.getNombre() + ": " + num + "|<C1>\"]\n";
        if (izdo != null) {
            Categoria temp = (Categoria) izdo.dato;
            Dot += izdo.dot() + "\"" + aux.getNombre() + "\" :C0->\"" + temp.getNombre() + "\";\n";
        }
        if (dcho != null) {
            Categoria temp = (Categoria) dcho.dato;
            Dot += dcho.dot() + "\"" + aux.getNombre() + "\" :C1->\"" + temp.getNombre() + "\";\n";
        }
        Dot += "\n";
        return Dot;
    }
}
