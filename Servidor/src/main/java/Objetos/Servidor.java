/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import Estructuras.Arboles.AVL.ArbolAVL;
import Estructuras.Listas.DoblementeEnlazada.DobleMenteEnlazada;
import Estructuras.Listas.SimplementeEnlazada.SimpleMenteEnlazada;
import Estructuras.TablaHash.Elemento;
import Estructuras.TablaHash.TablaHash;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
    private SimpleMenteEnlazada oidos;
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
        this.oidos = new SimpleMenteEnlazada();
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
            System.out.println("El servidor está encendido");
            System.out.println("Información del servidor:");
            System.out.println("Dirección IP: " + getIp());
            System.out.println("Puerto: " + puerto);
            while (true) {
                cliente = servidor.accept();
                System.out.println("Cliente conectado: IP: " + cliente.getInetAddress() + "::PUERTO: " + cliente.getPort());
                clientes.insertar(cliente);
                Cliente nuevo = new Cliente(cliente, (clientes.Tamaño() - 1), this);
                oidos.insertar(nuevo);
                nuevo.start();
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
        nodos.dot(1, "NODOS_EN_RED");
        System.out.println("NUEVA INSTANCIA :" + ip + "::" + puerto);
    }

    public String getIp() {
        return this.IP;
    }

    public void eliminar(int indice) {
        this.clientes.quitar(indice);
        this.instancias.quitar(indice);
        this.nodos.quitar(indice);
        this.nodos.dot(1, "NODOS_EN_RED");
    }

    public SimpleMenteEnlazada getInstancias() {
        return this.instancias;
    }

    public SimpleMenteEnlazada getNodos() {
        return this.nodos;
    }

    public Cliente getCliente(int indice) {
        return (Cliente) this.oidos.at(indice);
    }

    public void nuevoBloque() {
        if (!DATA.estaVacio()) {
            Bloque nuevo = null;
            if (bloques.estaVacio()) {
                try {
                    nuevo = new Bloque(0, DATA, "0000");
                    bloques.insertar(nuevo);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                Bloque ultimo = (Bloque) bloques.Ultimo();
                try {
                    nuevo = new Bloque(bloques.getTamaño(), DATA, ultimo.getHASH());
                    bloques.insertar(nuevo);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            accionar(DATA);
            bloques.dot();
            this.DATA = new SimpleMenteEnlazada();
            almacenarJSON();
            sincronizar(nuevo);
        }
    }

    public void accionar(SimpleMenteEnlazada DATA) {
        for (int i = 0; i < DATA.Tamaño(); i++) {
            Operacion actual = (Operacion) DATA.at(i);
            if (actual.getTipo().compareTo("CREAR_USUARIO") == 0) {
                Usuario nuevo = (Usuario) actual.getInvolucrado();
                if (!usuarioExistente(nuevo.getCarnet())) {
                    usuarios.insertar(nuevo);
                    usuarios.dot();
                } else {
                    DATA.quitar(i);
                }
            } else if (actual.getTipo().compareTo("CREAR_LIBRO") == 0) {
                Libro lib = (Libro) actual.getInvolucrado();
                if (categorias.estaVacio()) {
                    Categoria nueva = new Categoria(lib.getCategoria());
                    nueva.setCarpeta(carpeta);
                    nueva.setCarnet(lib.getCarnet());
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
                    nueva.setCarnet(lib.getCarnet());
                    nueva.insertarLibro(lib.getISBN(), lib);
                    try {
                        categorias.insertar(nueva);
                    } catch (Exception ex) {
                        Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    categorias.dot();
                }
            } else if (actual.getTipo().compareTo("EDITAR_USUARIO") == 0) {
                Usuario a_cambiar = (Usuario) actual.getInvolucrado();
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
            } else if (actual.getTipo().compareTo("ELIMINAR_USUARIO") == 0) {
                Usuario a_eliminar = (Usuario) actual.getInvolucrado();
                usuarios.eliminar(a_eliminar.getCarnet());
            }
        }
    }

    public void sincronizar(Bloque bloque) {
        System.out.println("Mandando información a la red...");
        for (int i = 0; i < instancias.Tamaño(); i++) {
            Instancia temp = (Instancia) instancias.at(i);
            temp.mandar("NUEVO_BLOQUE");
            temp.mandar(bloque.JSON());
        }
        System.out.println(bloque.JSON());
        System.out.println("¡Información esparcida por toda la red!");
    }

    public void nuevaOperacion(Operacion.Tipo tipo, Object involucrado) {
        this.DATA.insertar(new Operacion(tipo, involucrado));
    }

    public void desconectar() {
        for (int i = 0; i < instancias.Tamaño(); i++) {
            Instancia temp = (Instancia) instancias.at(i);
            temp.mandar("adios");
        }
    }

    public void almacenarJSON() {
        String json = "{\n\t\"BLOCKCHAIN\":\n\t[\n";
        for (int i = 0; i < bloques.getTamaño(); i++) {
            Bloque temp = (Bloque) bloques.at(i);
            json += temp.getJson();
            if (i < (bloques.getTamaño() - 1)) {
                json += ",\n";
            }else{
                json+="\n";
            }
        }
        json += "\t]\n}";
        String ruta = carpeta + "/Blockchain.json";
        FileWriter fichero = null;
        PrintWriter escritor;
        try {
            fichero = new FileWriter(ruta);
            escritor = new PrintWriter(fichero);
            escritor.print(json);
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo " + "Blockchain.json");
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (IOException e2) {
                System.err.println("Error al cerrar el archivo " + "Blockchain.json");
            }
        }
    }

    public void guardarBloque(Bloque bloque) {
        bloques.insertar(bloque);
        bloques.dot();
    }

    public boolean usuarioExistente(int Carnet) {
        return usuarios.buscar(Carnet) != null;
    }

    public boolean inicioSesion(int Carnet, String pass) {
        Elemento temp = usuarios.buscar(Carnet);
        return temp.getUsuario().getContraseña().compareTo(encriptar(pass)) == 0 && temp.getUsuario().getCarnet() == Carnet;
    }

    public Usuario user(int Carnet) {
        return usuarios.buscar(Carnet).getUsuario();
    }

    public DobleMenteEnlazada getBloques() {
        return bloques;
    }

    private String encriptar(String contraseña) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(contraseña.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public SimpleMenteEnlazada bibliotecaUsuario(int Carnet) {
        return categorias.bibliotecaUsuario(Carnet);
    }

    public SimpleMenteEnlazada categoriasUsuario(int Carnet) {
        return categorias.categoriasDeUsuario(Carnet);
    }

    public boolean libroExistente(int ISBN) {
        return categorias.buscarLibro(ISBN) == true;
    }

    public SimpleMenteEnlazada biblioteca() {
        return categorias.bibliotecaEnLista();
    }

    public boolean yaTieneBloques() {
        return bloques.getTamaño() > 0;
    }

    public DobleMenteEnlazada clonar() {
        DobleMenteEnlazada clonada = bloques;
        bloques = new DobleMenteEnlazada();
        bloques.setCarpeta(carpeta);
        return clonada;
    }

    public void ponerAlDia(DobleMenteEnlazada lista) {
        for (int i = 0; i < lista.getTamaño(); i++) {
            Bloque temp = (Bloque) lista.at(i);
            Bloque x = (Bloque) bloques.Ultimo();
            temp.setINDEX(bloques.getTamaño());
            temp.setPREVIOUSHASH(x.getHASH());
            bloques.insertar(temp);
            System.out.println("Mandando bloque# " + i);
            sincronizar(temp);
            System.out.println("¡Bloque mandado!");
        }
        bloques.dot();
        almacenarJSON();
    }

    public int getPuerto() {
        return puerto;
    }
}
