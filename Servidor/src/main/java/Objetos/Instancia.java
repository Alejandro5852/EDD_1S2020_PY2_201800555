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
public class Instancia implements Runnable {

    private String host;
    private int puerto;
    private Socket sc;
    private OutputStream salida;
    private InputStream entrada;
    private PrintWriter escriba;
    private BufferedReader lector;

    public Instancia(String host, int puerto) {
        this.host = host;
        this.puerto = puerto;
    }

    @Override
    public void run() {
        try {
            sc = new Socket(host, puerto);
            salida = sc.getOutputStream();
            entrada = sc.getInputStream();
            escriba = new PrintWriter(salida, true);
            lector = new BufferedReader(new InputStreamReader(entrada));
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
        if (mensaje.compareTo("adios") == 0) {
            escriba.println(mensaje);
            try {
                sc.close();
            } catch (IOException ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (mensaje.compareTo("LISTA_IP") == 0) {
            escriba.println(mensaje);
            String a = "";
            int contador = 0;
            while (true) {
                try {
                    a = lector.readLine();
                    System.out.println(a);
                } catch (IOException ex) {
                    Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (a.compareTo("FINAL") != 0) {
                    switch (contador) {
                        case 0:
                            System.out.println("IP: " + a);
                            contador++;
                            break;
                        case 1:
                            System.out.println("PUERTO: " + a);
                            contador = 0;
                            break;
                    }
                } else {
                    break;
                }
            }
        } else {
            escriba.println(mensaje);
        }
    }
}
