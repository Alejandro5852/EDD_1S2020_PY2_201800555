/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructuras.Arboles;

/**
 *
 * @author alejandro
 */
public class ArbolAVL {

    NodoAVL raiz;

    public ArbolAVL() {
        raiz = null;
    }

    public NodoAVL raizArbol() {
        return raiz;
    }

    private NodoAVL rotacionII(NodoAVL n, NodoAVL n1) {
        n.ramaIzdo(n1.subarbolDcho());
        n1.ramaDcho(n);
// actualización de los factores de equilibrio
        if (n1.fe == -1) // se cumple en la inserción
        {
            n.fe = 0;
            n1.fe = 0;
        } else {
            n.fe = -1;
            n1.fe = 1;
        }
        return n1;
    }

    private NodoAVL rotacionDD(NodoAVL n, NodoAVL n1) {
        n.ramaDcho(n1.subarbolIzdo());
        n1.ramaIzdo(n);
// actualización de los factores de equilibrio
        if (n1.fe == +1) // se cumple en la inserción
        {
            n.fe = 0;
            n1.fe = 0;
        } else {
            n.fe = +1;
            n1.fe = -1;
        }
        return n1;
    }

    private NodoAVL rotacionID(NodoAVL n, NodoAVL n1) {
        NodoAVL n2;
        n2 = (NodoAVL) n1.subarbolDcho();
        n.ramaIzdo(n2.subarbolDcho());
        n2.ramaDcho(n);
        n1.ramaDcho(n2.subarbolIzdo());
        n2.ramaIzdo(n1);
// actualización de los factores de equilibrio
        if (n2.fe == +1) {
            n1.fe = -1;
        } else {
            n1.fe = 0;
        }
        if (n2.fe == -1) {
            n.fe = 1;
        } else {
            n.fe = 0;
        }
        n2.fe = 0;
        return n2;
    }

    private NodoAVL rotacionDI(NodoAVL n, NodoAVL n1) {
        NodoAVL n2;
        n2 = (NodoAVL) n1.subarbolIzdo();
        n.ramaDcho(n2.subarbolIzdo());
        n2.ramaIzdo(n);
        n1.ramaIzdo(n2.subarbolDcho());
        n2.ramaDcho(n1);
        // actualización de los factores de equilibrio
        if (n2.fe == +1) {
            n.fe = -1;
        } else {
            n.fe = 0;
        }
        if (n2.fe == -1) {
            n1.fe = 1;
        } else {
            n1.fe = 0;
        }
        n2.fe = 0;
        return n2;
    }

    public void insertar(Object valor) throws Exception {
        Comparador dato;
        Logical h = new Logical(false); // intercambia un valor booleano
        dato = (Comparador) valor;
        raiz = insertarAvl(raiz, dato, h);
    }

    private NodoAVL insertarAvl(NodoAVL raiz, Comparador dt, Logical h) throws Exception {
        NodoAVL n1;
        if (raiz == null) {
            raiz = new NodoAVL(dt);
            h.setLogical(true);
        } else if (dt.menorQue(raiz.valorNodo())) {
            NodoAVL iz;
            iz = insertarAvl((NodoAVL) raiz.subarbolIzdo(), dt, h);
            raiz.ramaIzdo(iz);
            // regreso por los nodos del camino de búsqueda
            if (h.booleanValue()) {
                // decrementa el fe por aumentar la altura de rama izquierda
                switch (raiz.fe) {

                    case 1:
                        raiz.fe = 0;
                        h.setLogical(false);
                        break;

                    case 0:

                        raiz.fe = -1;
                        break;
                    case -1: // aplicar rotación a la izquierda
                        n1 = (NodoAVL) raiz.subarbolIzdo();
                        if (n1.fe == -1) {
                            raiz = rotacionII(raiz, n1);
                        } else {
                            raiz = rotacionID(raiz, n1);
                        }
                        h.setLogical(false);
                }
            }
        } else if (dt.mayorQue(raiz.valorNodo())) {
            NodoAVL dr;
            dr = insertarAvl((NodoAVL) raiz.subarbolDcho(), dt, h);
            raiz.ramaDcho(dr);
            // regreso por los nodos del camino de búsqueda
            if (h.booleanValue()) {
                // incrementa el fe por aumentar la altura de rama izquierda
                switch (raiz.fe) {

                    case 1:		 // aplicar rotación a la derecha
                        n1 = (NodoAVL) raiz.subarbolDcho();
                        if (n1.fe == +1) {
                            raiz = rotacionDD(raiz, n1);
                        } else {
                            raiz = rotacionDI(raiz, n1);
                        }
                        h.setLogical(false);
                        break;

                    case 0:
                        raiz.fe = +1;
                        break;

                    case -1:
                        raiz.fe = 0;
                        h.setLogical(false);
                }
            }
        } else {
            throw new Exception("No puede haber claves repetidas ");
        }
        return raiz;
    }
    
}
