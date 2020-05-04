/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alejandro
 */
public class Cliente implements Runnable {

    private String host;
    private int puerto;
    private Socket sc;
    private OutputStream salida;
    private InputStream entrada;
    private PrintWriter escriba;
    private BufferedReader lector;

    public Cliente(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
    }

    @Override
    public void run() {
        try {
            sc = new Socket(host, puerto);
            salida = sc.getOutputStream();
            escriba = new PrintWriter(salida, true);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean estaConectado() {
        return sc.isConnected();
    }

    public void desconectar() {
        try {
            sc.close();
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void mandar(String mensaje) {
        if (mensaje == "adios") {
            escriba.println(mensaje);
            try {
                sc.close();
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            escriba.println(mensaje);
            lector = new BufferedReader(new InputStreamReader(entrada));
            String a;
            try {
                a = lector.readLine();
                System.out.println(a);
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
