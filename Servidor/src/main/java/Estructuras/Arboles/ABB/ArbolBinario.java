/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructuras.Arboles.ABB;

/**
 *
 * @author alejandro
 */
public class ArbolBinario {

    protected Nodo raiz;

    public ArbolBinario() {
        raiz = null;
    }

    public ArbolBinario(Nodo raiz) {
        this.raiz = raiz;
    }

    public Nodo raizArbol() {
        return raiz;
    }
    // Comprueba el estatus del árbol

    boolean esVacio() {
        return raiz == null;
    }

    public static Nodo nuevoArbol(
            Nodo ramaIzqda, Object dato, Nodo ramaDrcha) {
        return new Nodo(ramaIzqda, dato, ramaDrcha);
    }
    // Recorrido de un árbol binario en preorden

    public static void preorden(Nodo r) {
        if (r != null) {
            r.visitar();
            preorden(r.subarbolIzdo());
            preorden(r.subarbolDcho());
        }
    }
// Recorrido de un árbol binario en inorden

    public static void inorden(Nodo r) {
        if (r != null) {
            inorden(r.subarbolIzdo());
            r.visitar();
            inorden(r.subarbolDcho());
        }
    }
// Recorrido de un árbol binario en postorden

    public static void postorden(Nodo r) {
        if (r != null) {
            postorden(r.subarbolIzdo());
            postorden(r.subarbolDcho());
            r.visitar();
        }
    }

    public static int numNodos(Nodo raiz) {
        if (raiz == null) {
            return 0;
        } else {
            return 1 + numNodos(raiz.subarbolIzdo())
                    + numNodos(raiz.subarbolDcho());
        }
    }
}
