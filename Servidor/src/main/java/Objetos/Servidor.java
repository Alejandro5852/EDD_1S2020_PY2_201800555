/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import Estructuras.Listas.SimplementeEnlazada.SimpleMenteEnlazada;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import Interfaz.IntefazGrafica;

/**
 *
 * @author alejandro
 */
public class Servidor implements Runnable {

    private SimpleMenteEnlazada clientes;
    private int puerto;
    private IntefazGrafica interfaz;

    public Servidor(int puerto) {
        this.puerto = puerto;
        this.clientes = new SimpleMenteEnlazada();
        interfaz = new IntefazGrafica();
        interfaz.setVisible(true);
    }

    @Override
    public void run() {
        ServerSocket servidor = null;
        Socket cliente = null;
        DataInputStream in;
        DataOutputStream out;
        try {
            servidor = new ServerSocket(puerto);
            System.out.println("El servidor está encendido, este escucha en el puerto: " + servidor.getLocalPort());
            while (true) {
                actualizar();
                cliente = servidor.accept();
                clientes.insertar(cliente);
                System.out.println("Nuevo cliente conectado");
                new Cliente(cliente, clientes, (clientes.Tamaño() - 1)).start();
                actualizar();
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizar() {
        String texto = "";
        if (clientes.estaVacio()) {
            interfaz.texto("Se ha iniciado el servidor, este escucha en el puerto: " + puerto);
        } else {
            for (int i = 0; i < clientes.Tamaño(); i++) {
                Socket socket = (Socket) clientes.at(i);
                texto += i + ". " + "IP: " + socket.getInetAddress().toString() + " PUERTO: " + socket.getPort() + "\n";
            }
            interfaz.texto(texto);
        }
    }

}
