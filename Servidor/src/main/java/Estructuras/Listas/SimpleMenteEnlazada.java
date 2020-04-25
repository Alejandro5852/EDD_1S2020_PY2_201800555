/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructuras.Listas;

import Estructuras.Arboles.Comparador;
import Objetos.Libro;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author alejandro
 */
public class SimpleMenteEnlazada {

    private NodoL cabeza;
    private int tamaño;

    public SimpleMenteEnlazada() {
        cabeza = null;
        tamaño = 0;
    }

    public void insertar(Object dato) {
        NodoL nuevo = new NodoL(dato);
        if (this.estaVacio()) {
            cabeza = nuevo;
        } else {
            Comparador dt = (Comparador) dato;
            if (dt.menorQue(cabeza.getValor())) {
                nuevo.setSiguiente(cabeza);
                cabeza = nuevo;
            } else {
                NodoL temp = cabeza;
                while (temp.getSiguiente() != null && dt.mayorQue(temp.getSiguiente().getValor())) {
                    temp = temp.getSiguiente();
                }
                nuevo.setSiguiente(temp.getSiguiente());
                temp.setSiguiente(nuevo);
            }
        }
        tamaño++;
    }

    public boolean estaVacio() {
        return cabeza == null;
    }

    public Object quitar(int indice) {
        Object encontrado = null;
        if (indice < tamaño) {
            NodoL temp = cabeza;
            for (int i = 0; i < indice; i++) {
                temp = temp.getSiguiente();
            }
            if (indice == 0) {
                encontrado = temp.getValor();
                cabeza = cabeza.getSiguiente();
            } else if (indice == (tamaño - 1)) {
                encontrado = temp.getValor();
                temp = cabeza;
                for (int i = 0; i < (indice - 1); i++) {
                    temp = temp.getSiguiente();
                }
                temp.setSiguiente(null);
            } else {
                encontrado = temp.getValor();
                NodoL aux = cabeza;
                for (int i = 0; i < (indice - 1); i++) {
                    aux = aux.getSiguiente();
                }
                aux.setSiguiente(temp.getSiguiente());
                temp.setSiguiente(null);
            }
        }
        tamaño--;
        return encontrado;
    }

    public void dot() {
        if (!this.estaVacio()) {
            int contador = 0;
            String Dot = "digraph G{\n rankdir = LR;\n node[color = green, style = filled, shape = record];\n";
            NodoL temp = cabeza;
            while (temp.getSiguiente() != null) {
                Libro libro = (Libro) temp.getValor();
                if (contador > 0) {
                    Dot += "a" + (contador - 1) + "->a" + contador + ";\n";
                }
                Dot += "a" + contador + "[label = " + libro.getTitulo() + "];\n";
                contador++;
                temp = temp.getSiguiente();
            }
            Dot += "}";
            FileWriter fichero = null;
            PrintWriter escritor;
            try {
                fichero = new FileWriter("/home/alejandro/Escritorio/Libros.dot");
                escritor = new PrintWriter(fichero);
                escritor.print(Dot);
            } catch (Exception e) {
                System.err.println("Error al escribir el archivo aux_grafico.dot");
            } finally {
                try {
                    if (null != fichero) {
                        fichero.close();
                    }
                } catch (Exception e2) {
                    System.err.println("Error al cerrar el archivo aux_grafico.dot");
                }
            }
            try {
                Runtime rt = Runtime.getRuntime();
                rt.exec("dot -Tjpg -o " + "/home/alejandro/Escritorio/Libros.jpg" + " /home/alejandro/Escritorio/Libros.dot");
                Thread.sleep(500);
            } catch (Exception ex) {
                System.err.println("Error al generar la imagen para el archivo aux_grafico.dot");
            }
        }
    }
}
