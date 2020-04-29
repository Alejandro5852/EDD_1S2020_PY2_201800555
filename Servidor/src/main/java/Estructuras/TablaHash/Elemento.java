/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructuras.TablaHash;

import Objetos.Usuario;

/**
 *
 * @author alejandro
 */
public class Elemento {

    private Usuario usuario;
    private Elemento siguiente;

    public Elemento(Usuario usuario) {
        this.usuario = usuario;
        this.siguiente = null;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Elemento getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Elemento siguiente) {
        this.siguiente = siguiente;
    }

}
