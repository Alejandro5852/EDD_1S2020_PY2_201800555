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
    private SimpleMenteEnlazada instancias;
    private SimpleMenteEnlazada nodos;
    private int puerto;
    private ArbolAVL categorias;
    private TablaHash usuarios;
    private ServerSocket servidor = null;

    public Servidor(int puerto) {
        this.puerto = puerto;
        this.clientes = new SimpleMenteEnlazada();
        this.categorias = new ArbolAVL();
        this.usuarios = new TablaHash();
        this.instancias = new SimpleMenteEnlazada();
        this.nodos = new SimpleMenteEnlazada();
    }

    @Override
    public void run() {
        Socket cliente = null;
        DataInputStream in;
        DataOutputStream out;
        try {
            servidor = new ServerSocket(puerto);
            System.out.println("El servidor está encendido, este escucha en el puerto: " + puerto + " " + getIp());
            while (true) {
                cliente = servidor.accept();
                System.out.println("Cliente conectado");
                clientes.insertar(cliente);
                new Cliente(cliente, (clientes.Tamaño() - 1), categorias, usuarios, this).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void nuevaInstancia(String ip, int puerto) {
        Instancia nueva = new Instancia(ip, puerto);
        Thread hiloNuevaInstancia = new Thread(nueva);
        hiloNuevaInstancia.run();
        instancias.insertar(nueva);
        nodos.insertar(new NodoRed(ip, puerto));
        System.out.println("NUEVA INSTANCIA :" + ip + "::" + puerto);
    }

    public String getIp() {
        return servidor.getInetAddress().toString();
    }

    public void eliminar(int indice) {
        this.clientes.quitar(indice);
        this.instancias.quitar(indice);
    }

    public SimpleMenteEnlazada getInstancias() {
        return this.instancias;
    }

    public SimpleMenteEnlazada getNodos() {
        return this.nodos;
    }

}
