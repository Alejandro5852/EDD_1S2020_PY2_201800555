/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructuras.Listas.SimplementeEnlazada;

import Estructuras.Arboles.Comparador;
import Estructuras.Listas.NodoL;
import Objetos.Categoria;
import Objetos.Libro;
import java.io.FileWriter;
import java.io.IOException;
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
            NodoL temp = cabeza;
            while (temp.getSiguiente() != null) {
                temp = temp.getSiguiente();
            }
            temp.setSiguiente(nuevo);
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

    public Object at(int indice) {
        Object encontrado = null;
        if (indice < tamaño) {
            NodoL temp = cabeza;
            for (int i = 0; i < indice; i++) {
                temp = temp.getSiguiente();
            }
            encontrado = temp.getValor();
        }
        return encontrado;
    }

    public void dot(int tipo, String titulo) {
        if (!this.estaVacio()) {
            int contador = 0;
            String Dot = "digraph G{\n rankdir = LR;\n node[color = mediumpurple, style = filled, shape = record];\n";
            NodoL temp = cabeza;
            while (temp != null) {
                if (contador > 0) {
                    Dot += "a" + (contador - 1) + "->a" + contador + ";\n";
                }
                if (tipo == 0) {
                    Categoria cat = (Categoria) temp.getValor();
                    Dot += "a" + contador + "[label = \"" + cat.getNombre() + "\"];\n";
                }
                contador++;
                temp = temp.getSiguiente();
            }
            Dot += "}";
            FileWriter fichero = null;
            PrintWriter escritor;
            try {
                fichero = new FileWriter("/home/alejandro/Escritorio/" + titulo + ".dot");
                escritor = new PrintWriter(fichero);
                escritor.print(Dot);
            } catch (IOException e) {
                System.err.println("Error al escribir el archivo " + titulo + ".dot");
            } finally {
                try {
                    if (null != fichero) {
                        fichero.close();
                    }
                } catch (IOException e2) {
                    System.err.println("Error al cerrar el archivo " + titulo + ".dot");
                }
            }
            try {
                Runtime rt = Runtime.getRuntime();
                rt.exec("dot -Tjpg -o " + "/home/alejandro/Escritorio/" + titulo + ".jpg" + " /home/alejandro/Escritorio/" + titulo + ".dot");
                Thread.sleep(500);
            } catch (IOException | InterruptedException ex) {
                System.err.println("Error al generar la imagen para el archivo " + titulo + ".dot");
            }
        }
    }

    public Object Primero() {
        Object primero = null;
        if (!estaVacio()) {
            NodoL aux = cabeza;
            primero = aux.getValor();
        }
        return primero;
    }

    public Object Ultimo() {
        Object ultimo = null;
        if (!estaVacio()) {
            NodoL temp = cabeza;
            for (int i = 0; i < tamaño; i++) {
                temp = temp.getSiguiente();
            }
            ultimo = temp.getValor();
        }
        return ultimo;
    }

    public int Tamaño() {
        return tamaño;
    }
}
