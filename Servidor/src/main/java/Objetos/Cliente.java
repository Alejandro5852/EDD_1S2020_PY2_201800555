/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import Estructuras.Arboles.AVL.ArbolAVL;
import Estructuras.Listas.DoblementeEnlazada.DobleMenteEnlazada;
import Estructuras.Listas.SimplementeEnlazada.SimpleMenteEnlazada;
import Estructuras.TablaHash.TablaHash;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alejandro
 */
public class Cliente extends Thread {

    private Socket socket;
    int indice;
    private DobleMenteEnlazada bloques;
    private ArbolAVL categorias;
    private TablaHash usuarios;
    private SimpleMenteEnlazada DATA = null;
    private Servidor servidor;

    public Cliente(Socket socket, int indice, ArbolAVL categorias, TablaHash usuarios, Servidor servidor) {
        this.socket = socket;
        this.indice = indice;
        this.bloques = new DobleMenteEnlazada();
        this.categorias = categorias;
        this.usuarios = usuarios;
        this.bloques.setPuerto(Integer.toString(socket.getPort()));
        this.servidor = servidor;
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
                if (mensaje.compareTo("CREAR_USUARIO") == 0) {
                    System.out.println("Operacion: CREAR_USUARIO");
                    String atributo;
                    int Carnet = 0;
                    String Nombre = null;
                    String Apellido = null;
                    String Carrera = null;
                    String Password = null;
                    int contador = 0;
                    while (contador < 5) {
                        atributo = lector.readLine();
                        switch (contador) {
                            case 0:
                                Carnet = Integer.parseInt(atributo);
                                contador++;
                                break;
                            case 1:
                                Nombre = atributo;
                                contador++;
                                break;
                            case 2:
                                Apellido = atributo;
                                contador++;
                                break;
                            case 3:
                                Carrera = atributo;
                                contador++;
                                break;
                            case 4:
                                Password = atributo;
                                contador++;
                                break;
                        }
                    }
                    Usuario nuevo = new Usuario(Nombre, Apellido, Carrera, Password, Carnet);
                    escriba.println("Usuario creado exitosamente");
                    this.usuarios.insertar(nuevo);
                    Operacion operacion = new Operacion(Operacion.Tipo.CREAR_USUARIO, nuevo);
                    if (DATA == null) {
                        DATA = new SimpleMenteEnlazada();
                        DATA.insertar(operacion);
                    } else {
                        DATA.insertar(operacion);
                    }
                } else if (mensaje.compareTo("GUARDAR") == 0) {
                    System.out.println("GUARDAR CAMBIOS");
                    if (bloques.estaVacio()) {
                        Bloque bloque = new Bloque(0, DATA, "0000");
                        System.out.println(bloque.getJson());
                        DATA = null;
                        bloques.insertar(bloque);
                    } else {
                        Bloque ultimo = (Bloque) bloques.Ultimo();
                        Bloque bloque = new Bloque((bloques.getTamaño() - 1), DATA, ultimo.getHASH());
                        DATA = null;
                        bloques.insertar(bloque);
                    }
                    bloques.dot();
                } else if (mensaje.compareTo("LISTA_IP") == 0) {
                    System.out.println("SOLICITUD DE LISTA IP");
                    if (servidor.getNodos().Tamaño() == 1) {
                        escriba.println("FINAL");
                    } else {
                        for (int i = 0; i < (servidor.getNodos().Tamaño() - 1); i++) {
                            NodoRed nodo = (NodoRed) servidor.getNodos().at(i);
                            escriba.println(nodo.getDireccionIP());
                            escriba.println(nodo.getPuerto());
                        }
                        escriba.println("FINAL");
                    }
                } else if (mensaje.compareTo("DATOS") == 0) {
                    while (true) {
                        String ip = lector.readLine();
                        if (ip.compareTo("0.0.0.0/0.0.0.0") == 0) {
                            ip = "127.0.0.1";
                        }
                        String puerto = lector.readLine();
                        servidor.nuevaInstancia(ip, Integer.parseInt(puerto));
                        break;
                    }
                } else {
                    System.out.println(mensaje);
                }
            } while (!mensaje.equals("adios"));
            System.out.println("CLIENTE DESCONECTADO");
            socket.close();
            servidor.eliminar(indice);
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
