/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Objetos.Servidor;

/**
 *
 * @author alejandro
 */
public class main {

    public static void main(String[] args) {
        Servidor servidor = new Servidor(30000);
        Thread hilo = new Thread(servidor);
        hilo.start();
    }

}
