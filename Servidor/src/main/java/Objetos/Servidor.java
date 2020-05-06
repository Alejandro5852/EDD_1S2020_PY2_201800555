/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import Estructuras.Arboles.AVL.ArbolAVL;
import Estructuras.Arboles.B.BTree;
import Estructuras.Listas.DoblementeEnlazada.DobleMenteEnlazada;
import Estructuras.Listas.SimplementeEnlazada.SimpleMenteEnlazada;
import Estructuras.TablaHash.Elemento;
import Estructuras.TablaHash.TablaHash;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
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
    private SimpleMenteEnlazada DATA;
    private DobleMenteEnlazada bloques;
    private int puerto;
    private ArbolAVL categorias;
    private TablaHash usuarios;
    private ServerSocket servidor = null;
    private String IP;
    private String carpeta;

    public Servidor(int puerto, String IP) {
        this.puerto = puerto;
        this.clientes = new SimpleMenteEnlazada();
        this.categorias = new ArbolAVL();
        this.usuarios = new TablaHash();
        this.instancias = new SimpleMenteEnlazada();
        this.nodos = new SimpleMenteEnlazada();
        this.IP = IP;
        this.DATA = new SimpleMenteEnlazada();
        this.bloques = new DobleMenteEnlazada();
        this.carpeta = "";
    }

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
        this.bloques.setCarpeta(carpeta);
        this.clientes.setCarpeta(carpeta);
        this.instancias.setCarpeta(carpeta);
        this.nodos.setCarpeta(carpeta);
        this.usuarios.setCarpeta(carpeta);
        this.categorias.setCarpeta(carpeta);
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
                System.out.println("Cliente conectado: IP: " + cliente.getInetAddress() + "::PUERTO: " + cliente.getPort());
                clientes.insertar(cliente);
                new Cliente(cliente, (clientes.Tamaño() - 1), categorias, usuarios, this).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void nuevaInstancia(String ip, int puerto) {
        Instancia nueva = new Instancia(ip, puerto);
        nueva.setServidor(this);
        Thread hiloNuevaInstancia = new Thread(nueva);
        hiloNuevaInstancia.run();
        nueva.mandar("Cliente proveniente de la instancia con servidor de direccion IP: " + this.IP + ", y puerto: " + this.puerto);
        instancias.insertar(nueva);
        nodos.insertar(new NodoRed(ip, puerto));
        nodos.dot(1, "NODOS EN RED");
        System.out.println("NUEVA INSTANCIA :" + ip + "::" + puerto);
    }

    public String getIp() {
        return this.IP;
    }

    public void eliminar(int indice) {
        this.clientes.quitar(indice);
        this.instancias.quitar(indice);
        this.nodos.quitar(indice);
        this.nodos.dot(1, "NODOS EN RED");
    }

    public SimpleMenteEnlazada getInstancias() {
        return this.instancias;
    }

    public SimpleMenteEnlazada getNodos() {
        return this.nodos;
    }

    public void nuevoBloque() {
        if (!DATA.estaVacio()) {
            if (bloques.estaVacio()) {
                try {
                    bloques.insertar(new Bloque(0, DATA, "0000"));
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                Bloque ultimo = (Bloque) bloques.Ultimo();
                try {
                    bloques.insertar(new Bloque(bloques.getTamaño(), DATA, ultimo.getHASH()));
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            bloques.dot();
            accionar(DATA);
            DATA.vaciar();
        }
    }

    public void accionar(SimpleMenteEnlazada DATA) {
        for (int i = 0; i < DATA.Tamaño(); i++) {
            Operacion actual = (Operacion) DATA.at(i);
            if (actual.getTipo().compareTo("CREAR_USUARIO") == 0) {
                usuarios.insertar((Usuario) actual.getInvolucrado());
                usuarios.dot();
            } else if (actual.getTipo().compareTo("CREAR_LIBRO") == 0) {
                Libro lib = (Libro) actual.getInvolucrado();
                if (categorias.estaVacio()) {
                    Categoria nueva = new Categoria(lib.getCategoria());
                    nueva.insertarLibro(lib.getISBN(), lib);
                    try {
                        categorias.insertar(nueva);
                    } catch (Exception ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    categorias.dot();
                } else if (categorias.get(lib.getCategoria()) != null) {
                    categorias.get(lib.getCategoria()).insertarLibro(lib.getISBN(), lib);
                } else {
                    Categoria nueva = new Categoria(lib.getCategoria());
                    nueva.setCarpeta(carpeta);
                    nueva.insertarLibro(lib.getISBN(), lib);
                    try {
                        categorias.insertar(nueva);
                    } catch (Exception ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    categorias.dot();
                }
            } else if (actual.getTipo().compareTo("EDITAR_USUARIO") == 0) {
                Usuario a_cambiar = (Usuario) actual.getInvolucrado2();
                Elemento buscado = usuarios.buscar(a_cambiar.getCarnet());
                buscado.setUsuario((Usuario) actual.getInvolucrado());
                usuarios.dot();
            } else if (actual.getTipo().compareTo("ELIMINAR_LIBRO") == 0) {
                Libro lib = (Libro) actual.getInvolucrado();
                Categoria categoria = categorias.get(lib.getCategoria());
                categoria.eliminarLibro(lib.getISBN());
                categorias.dot();
            } else if (actual.getTipo().compareTo("CREAR_CATEGORIA") == 0) {
                Categoria nueva = (Categoria) actual.getInvolucrado();
                nueva.setCarpeta(carpeta);
                try {
                    categorias.insertar(nueva);
                } catch (Exception ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
                categorias.dot();
            } else if (actual.getTipo().compareTo("ELIMINAR_CATEGORIA") == 0) {
                Categoria a_eliminar = (Categoria) actual.getInvolucrado();
                try {
                    categorias.eliminar(a_eliminar);
                } catch (Exception ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
                categorias.dot();
            }
        }
    }

    public void sincronizar(Bloque bloque) {
        for (int i = 0; i < instancias.Tamaño(); i++) {
            Instancia temp = (Instancia) instancias.at(i);
            temp.mandar("NUEVO_BLOQUE");
            temp.mandar(bloque.getJson());
        }
    }

    public void nuevaOperacion(Operacion.Tipo tipo, Object involucrado) {
        this.DATA.insertar(new Operacion(tipo, involucrado));
    }

}
