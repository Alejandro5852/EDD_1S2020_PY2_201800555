/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Estructuras.Arboles.AVL.ArbolAVL;
import Objetos.Categoria;
import Objetos.Libro;
import Estructuras.Listas.SimpleMenteEnlazada;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;

/**
 *
 * @author alejandro
 */
public class main {

    public static void main(String[] args) throws Exception {

        SimpleMenteEnlazada prueba = new SimpleMenteEnlazada();
        ArbolAVL arbolito = new ArbolAVL();
        try {
            JsonParser parser = new JsonParser();
            Object objects = parser.parse(new FileReader("/home/alejandro/Escritorio/Libros.json"));
            JsonObject jsonObject = (JsonObject) objects;
            JsonArray libros = (JsonArray) jsonObject.get("libros");
            for (int i = 0; i < libros.size(); i++) {
                JsonObject obj = (JsonObject) libros.get(i);
                String idioma = obj.get("Idioma").toString().substring(1);
                idioma = idioma.substring(0, (idioma.length() - 1));
                String titulo = obj.get("Titulo").toString().substring(1);
                titulo = titulo.substring(0, (titulo.length() - 1));
                String editorial = obj.get("Editorial").toString().substring(1);
                editorial = editorial.substring(0, (editorial.length() - 1));
                String autor = obj.get("Autor").toString().substring(1);
                autor = autor.substring(0, (autor.length() - 1));
                String cat = obj.get("Categoria").toString().substring(1);
                cat = cat.substring(0, (cat.length() - 1));
                Libro libro = new Libro(Integer.parseInt(obj.get("ISBN").toString()), Integer.parseInt(obj.get("Año").toString()),
                        idioma, titulo, editorial, autor, Integer.parseInt(obj.get("Edicion").toString()), cat);
                Categoria categoria = new Categoria(cat);
                prueba.insertar(libro);
                arbolito.insertar(categoria);
            }
        } catch (Exception e) {
            System.out.println("Error en la lectura del archivo de configuracion " + e);
        }
        prueba.dot();
        arbolito.dot();

    }
}
