/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Estructuras.Arboles.ArbolAVL;
import Objetos.Categoria;

/**
 *
 * @author alejandro
 */
public class main {

    public static void main(String[] args) throws Exception {
        ArbolAVL prueba = new ArbolAVL();
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
    }
}
