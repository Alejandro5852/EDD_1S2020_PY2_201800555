/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import Estructuras.Arboles.AVL.ArbolAVL;
import Estructuras.Listas.SimplementeEnlazada.SimpleMenteEnlazada;
import Estructuras.TablaHash.TablaHash;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alejandro
 */
public class Servidor implements Runnable {

    private SimpleMenteEnlazada clientes;
    private int puerto;
    private ArbolAVL categorias;
    private TablaHash usuarios;

    public Servidor(int puerto) {
        this.puerto = puerto;
        this.clientes = new SimpleMenteEnlazada();
        this.categorias = new ArbolAVL();
        this.usuarios = new TablaHash();
    }

    @Override
    public void run() {
        ServerSocket servidor = null;
        Socket cliente = null;
        DataInputStream in;
        DataOutputStream out;
        try {
            servidor = new ServerSocket(puerto);
            System.out.println("El servidor está encendido, este escucha en el puerto: " + puerto);
            while (true) {
                cliente = servidor.accept();
                System.out.println("Cliente conectado");
                clientes.insertar(cliente);
                new Cliente(cliente, clientes, (clientes.Tamaño() - 1), categorias, usuarios).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
