/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Estructuras.Arboles.ArbolAVL;
import Objetos.Categoria;
import Objetos.Libro;
import Estructuras.Listas.SimpleMenteEnlazada;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonParseException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;

/**
 *
 * @author alejandro
 */
public class main {
    
    public static void main(String[] args) throws Exception {
        /*
        ArbolAVL arbolito = new ArbolAVL();
        Categoria uno = new Categoria("Ficción");
        Categoria dos = new Categoria("Ciencia");
        Categoria tres = new Categoria("Adultos");
        Categoria cuatro = new Categoria("Fabulas");
        Categoria cinco = new Categoria("Cuentos");
        Categoria seis = new Categoria("Anime");
        Categoria siete = new Categoria("Comics");
        Categoria ocho = new Categoria("Aventura");
        Categoria nueve = new Categoria("Matemática");
        prueba.insertar(uno);
        prueba.insertar(dos);
        prueba.insertar(tres);
        prueba.insertar(cuatro);
        prueba.insertar(cinco);
        prueba.insertar(seis);
        prueba.insertar(siete);
        prueba.insertar(ocho);
        prueba.insertar(nueve);
        prueba.eliminar(dos);
        prueba.dot();
        SimpleMenteEnlazada prueba = new SimpleMenteEnlazada();
        ArbolAVL arbolito = new ArbolAVL();
        try {
            JsonParser parser = new JsonParser();
            Object objects = parser.parse(new FileReader("/home/alejandro/Escritorio/Libros.json"));
            JsonObject jsonObject = (JsonObject) objects;
            JsonArray libros = (JsonArray) jsonObject.get("libros");
            for (int i = 0; i < libros.size(); i++) {
                JsonObject obj = (JsonObject) libros.get(i);
                Libro libro = new Libro(Integer.parseInt(obj.get("ISBN").toString()), Integer.parseInt(obj.get("Año").toString()), obj.get("Idioma").toString(),
                        obj.get("Titulo").toString(), obj.get("Editorial").toString(), obj.get("Autor").toString(),
                        Integer.parseInt(obj.get("Edicion").toString()), obj.get("Categoria").toString());
                Categoria categoria = new Categoria(obj.get("Categoria").toString());
                prueba.insertar(libro);
                //arbolito.insertar(categoria);
            }
        } catch (Exception e) {
            System.out.println("Error en la lectura del archivo de configuracion " + e);
        }
        prueba.dot();
       // arbolito.dot();
*/
    }
}
