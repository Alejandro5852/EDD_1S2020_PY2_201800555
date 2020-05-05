/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import java.net.InetAddress;

/**
 *
 * @author alejandro
 */
public class NodoRed {
    
    private String DireccionIP;
    private int Puerto;

    public NodoRed(String DireccionIP, int Puerto) {
        this.DireccionIP = DireccionIP;
        this.Puerto = Puerto;
    }

    public String getDireccionIP() {
        return DireccionIP;
    }

    public int getPuerto() {
        return Puerto;
    }
    
}
