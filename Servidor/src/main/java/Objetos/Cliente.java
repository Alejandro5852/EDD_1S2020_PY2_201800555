/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import Estructuras.Listas.SimplementeEnlazada.SimpleMenteEnlazada;
;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    int indice;
    private Servidor servidor;

    public Cliente(Socket socket, int indice, Servidor servidor) {
        this.socket = socket;
        this.indice = indice;
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
                if (mensaje.compareTo("LISTA_IP") == 0) {
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
                } else if (mensaje.compareTo("NUEVO_BLOQUE") == 0) {
                    System.out.println("JSON entrante");
                    String jsonBloque = lector.readLine();
                    JsonParser parser = new JsonParser();
                    JsonObject data = parser.parse(jsonBloque).getAsJsonObject();
                    Bloque nuevo = leerJson(data);
                    System.out.println("¡JSON convertido a bloque!");
                    servidor.accionar(nuevo.getDATA());
                    servidor.guardarBloque(nuevo);
                    servidor.almacenarJSON(nuevo);
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
        }
    }

    private Bloque leerJson(JsonObject objetoJson) {
        int INDEX = Integer.parseInt(objetoJson.get("INDEX").toString());
        String TIMESTAMP = objetoJson.get("TIMESTAMP").toString().substring(1);
        TIMESTAMP = TIMESTAMP.substring(0, (TIMESTAMP.length() - 1));
        int NONCE = Integer.parseInt(objetoJson.get("NONCE").toString());
        SimpleMenteEnlazada DATA = new SimpleMenteEnlazada();
        JsonArray data = (JsonArray) objetoJson.get("DATA");
        for (int i = 0; i < data.size(); i++) {
            JsonObject obj = (JsonObject) data.get(i);
            if (obj.get("TIPO").toString().compareTo("\"CREAR_USUARIO\"") == 0) {
                String Carnet = obj.get("Carnet").toString();
                String Nombre = obj.get("Nombre").toString().substring(1);
                Nombre = Nombre.substring(0, (Nombre.length() - 1));
                String Apellido = obj.get("Apellido").toString().substring(1);
                Apellido = Apellido.substring(0, (Apellido.length() - 1));
                String Carrera = obj.get("Carrera").toString().substring(1);
                Carrera = Carrera.substring(0, (Carrera.length() - 1));
                String Password = obj.get("Password").toString().substring(1);
                Password = Password.substring(0, (Password.length() - 1));
                Usuario nuevo = new Usuario(Nombre, Apellido, Carrera, Password, Integer.parseInt(Carnet));
                DATA.insertar(new Operacion(Operacion.Tipo.CREAR_USUARIO, nuevo));
            } else if (obj.get("TIPO").toString().compareTo("\"EDITAR_USUARIO\"") == 0) {
                String Carnet = obj.get("Carnet").toString();
                String Nombre = obj.get("Nombre").toString().substring(1);
                Nombre = Nombre.substring(0, (Nombre.length() - 1));
                String Apellido = obj.get("Apellido").toString().substring(1);
                Apellido = Apellido.substring(0, (Apellido.length() - 1));
                String Carrera = obj.get("Carrera").toString().substring(1);
                Carrera = Carrera.substring(0, (Carrera.length() - 1));
                String Password = obj.get("Password").toString().substring(1);
                Password = Password.substring(0, (Password.length() - 1));
                Usuario auxiliar = new Usuario(Nombre, Apellido, Carrera, Password, Integer.parseInt(Carnet));
                DATA.insertar(new Operacion(Operacion.Tipo.EDITAR_USUARIO, auxiliar));
            } else if (obj.get("TIPO").toString().compareTo("\"ELIMINAR_LIBRO\"") == 0) {
                String ISBN = obj.get("ISBN").toString();
                String Titulo = obj.get("TITULO").toString().substring(1);
                Titulo = Titulo.substring(0, (Titulo.length() - 1));
                String Categoria = obj.get("CATEGORIA").toString().substring(1);
                Categoria = Categoria.substring(0, (Categoria.length() - 1));
                Libro temp = new Libro(Integer.parseInt(ISBN), Titulo, Categoria);
                DATA.insertar(new Operacion(Operacion.Tipo.ELIMINAR_LIBRO, temp));
            } else if (obj.get("TIPO").toString().compareTo("\"CREAR_LIBRO\"") == 0) {
                String ISBN = obj.get("ISBN").toString();
                String Año = obj.get("AÑO").toString();
                String Idioma = obj.get("IDIOMA").toString().substring(1);
                Idioma = Idioma.substring(0, (Idioma.length() - 1));
                String Titulo = obj.get("TITULO").toString().substring(1);
                Titulo = Titulo.substring(0, (Titulo.length() - 1));
                String Editorial = obj.get("EDITORIAL").toString().substring(1);
                Editorial = Editorial.substring(0, (Editorial.length() - 1));
                String Autor = obj.get("AUTOR").toString().substring(1);
                Autor = Autor.substring(0, (Autor.length() - 1));
                String Edicion = obj.get("EDICION").toString();
                String Categoria = obj.get("CATEGORIA").toString().substring(1);
                Categoria = Categoria.substring(0, (Categoria.length() - 1));
                Libro nuevo = new Libro(Integer.parseInt(ISBN), Integer.parseInt(Año), Idioma, Titulo, Editorial, Autor, Integer.parseInt(Edicion), Categoria);
                DATA.insertar(new Operacion(Operacion.Tipo.CREAR_LIBRO, nuevo));
            } else if (obj.get("TIPO").toString().compareTo("\"CREAR_CATEGORIA\"") == 0) {
                String Nombre = obj.get("Nombre").toString().substring(1);
                Nombre = Nombre.substring(0, (Nombre.length() - 1));
                Categoria nueva = new Categoria(Nombre);
                DATA.insertar(new Operacion(Operacion.Tipo.CREAR_CATEGORIA, nueva));
            } else if (obj.get("TIPO").toString().compareTo("\"ELIMINAR_CATEGORIA\"") == 0) {
                String Nombre = obj.get("Nombre").toString().substring(1);
                Nombre = Nombre.substring(0, (Nombre.length() - 1));
                Categoria temp = new Categoria(Nombre);
                DATA.insertar(new Operacion(Operacion.Tipo.ELIMINAR_CATEGORIA, temp));
            }
        }
        String PREVIOUSHASH = objetoJson.get("PREVIOUSHASH").toString().substring(1);
        PREVIOUSHASH = PREVIOUSHASH.substring(0, (PREVIOUSHASH.length() - 1));
        String HASH = objetoJson.get("HASH").toString().substring(1);
        HASH = HASH.substring(0, (HASH.length() - 1));
        Bloque bloque = new Bloque(INDEX, NONCE, TIMESTAMP, DATA, PREVIOUSHASH, HASH);
        return bloque;
    }
}
