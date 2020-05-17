/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructuras.Arboles.B;

import Estructuras.Listas.SimplementeEnlazada.SimpleMenteEnlazada;
import Objetos.Libro;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author alejandro
 */
public class ArbolB {

    private NodoB raiz;
    private int orden;
    private String categoria = "";
    private String carpeta = "";

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public ArbolB(int orden) {
        this.orden = orden;
        this.raiz = null;
    }

    public boolean estaVacio() {
        return this.raiz == null;
    }

    public void insertar(Libro key) {
        if (estaVacio()) {
            this.raiz = new NodoB(this.orden, true);
            this.raiz.setEsRaiz(true);
            this.raiz.insertar(key);
        } else {
            NodoB aux = this.raiz;
            while (!aux.EsHoja()) {
                int i = 0;
                while (key.getISBN() > aux.getClave(i).getISBN() && i < aux.getN()) {
                    if (aux.getClave(i + 1) == null) {
                        i++;
                        break;
                    }
                    i++;
                }
                aux = aux.getHijo(i);
            }
            aux.insertar(key);
        }
    }

    public void quitar(int key) {
        if (!estaVacio()) {
            this.raiz.quitar(key);
        }

    }

    public NodoB getRaiz() {
        return raiz;
    }

    public int traverse() {
        if (!estaVacio()) {
            return this.raiz.traverse();
        }
        return 0;
    }

    public void libros(SimpleMenteEnlazada arg) {
        if (raiz != null) {
            raiz.general(arg);
        }
    }

    public void bibliotecaUsuario(SimpleMenteEnlazada arg, int Carnet) {
        if (raiz != null) {
            raiz.porUsuario(arg, Carnet);
        }
    }

    public void dot() {
        if (raiz != null) {
            String Dot = "digraph G{\nrankdir=TB\nnode[shape = record, style = filled, fillcolor = skyblue];\n";
            Dot += "label =  <<font point-size='20'>Arbol B: " + this.categoria + "</font>>;\nlabelloc = \"t \";\n";
            Dot += raiz.dot();
            Dot += "}";
            FileWriter fichero = null;
            PrintWriter escritor;
            try {
                fichero = new FileWriter(carpeta + "/ArbolB_" + this.categoria + ".dot");
                escritor = new PrintWriter(fichero);
                escritor.print(Dot);
            } catch (Exception e) {
                System.err.println("Error al escribir el archivo ArbolB_" + this.categoria + ".dot");
            } finally {
                try {
                    if (null != fichero) {
                        fichero.close();
                    }
                } catch (Exception e2) {
                    System.err.println("Error al cerrar el archivo ArbolB_" + this.categoria + ".dot");
                }
            }
            try {
                Runtime rt = Runtime.getRuntime();
                rt.exec("dot -Tjpg -o " + carpeta + "/ArbolB_" + this.categoria + ".jpg" + " " + carpeta + "/ArbolB_" + this.categoria + ".dot");
                Thread.sleep(500);
            } catch (Exception ex) {
                System.err.println("Error al generar la imagen para el archivo ArbolB_" + this.categoria + ".dot");
            }
        }
    }

    public int librosAsociados() {
        int librosAsociados = 0;
        raiz.librosAsociados(librosAsociados);
        return librosAsociados;
    }

    public Libro buscar(int ISBN) {
        NodoB currentNode = raiz;
        Libro currentKey;
        int i, numberOfKeys;

        while (currentNode != null) {
            numberOfKeys = currentNode.getN();
            i = 0;
            currentKey = currentNode.getClave(i);
            while ((i < numberOfKeys) && (ISBN > currentKey.getISBN())) {
                ++i;
                if (i < numberOfKeys) {
                    currentKey = currentNode.getClave(i);
                } else {
                    --i;
                    break;
                }
            }

            if ((i < numberOfKeys) && (ISBN == currentKey.getISBN())) {
                return currentKey;
            }
            if (ISBN > currentKey.getISBN()) {
                currentNode = NodoB.getRightChildAtIndex(currentNode, i);
            } else {
                currentNode = NodoB.getLeftChildAtIndex(currentNode, i);
            }
        }

        return null;
    }
}
