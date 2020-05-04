/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import Estructuras.Listas.SimplementeEnlazada.SimpleMenteEnlazada;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author alejandro
 */
public class Cliente extends Thread {

    private Socket socket;
    SimpleMenteEnlazada lista;
    int indice;

    public Cliente(Socket socket, SimpleMenteEnlazada lista, int indice) {
        this.socket = socket;
        this.lista = lista;
        this.indice = indice;
    }

    public void run() {
        try {
            InputStream entrada = socket.getInputStream();
            BufferedReader lector = new BufferedReader(new InputStreamReader(entrada));
            OutputStream salida = socket.getOutputStream();
            PrintWriter escriba = new PrintWriter(salida, true);
            String mensaje;
            do {
                mensaje = lector.readLine();
                System.out.println(mensaje);
                String reverseText = new StringBuilder(mensaje).reverse().toString();
                escriba.println("Server: " + reverseText);
            } while (!mensaje.equals("adios"));
            socket.close();
            lista.quitar(indice);
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
