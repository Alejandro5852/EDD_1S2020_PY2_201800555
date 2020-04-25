/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructuras.Listas;

/**
 *
 * @author alejandro
 */
public class NodoL {

    private NodoL anterior;
    private NodoL siguiente;
    private Object valor;

    public NodoL(Object valor) {
        this.valor = valor;
        this.anterior = null;
        this.siguiente = null;
    }

    /**
     * @return the anterior
     */
    public NodoL getAnterior() {
        return anterior;
    }

    /**
     * @param anterior the anterior to set
     */
    public void setAnterior(NodoL anterior) {
        this.anterior = anterior;
    }

    /**
     * @return the siguiente
     */
    public NodoL getSiguiente() {
        return siguiente;
    }

    /**
     * @param siguiente the siguiente to set
     */
    public void setSiguiente(NodoL siguiente) {
        this.siguiente = siguiente;
    }

    /**
     * @return the valor
     */
    public Object getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(Object valor) {
        this.valor = valor;
    }
}
