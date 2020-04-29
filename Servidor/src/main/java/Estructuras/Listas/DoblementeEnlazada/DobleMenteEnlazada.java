/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructuras.Listas.DoblementeEnlazada;

import Estructuras.Listas.NodoL;
import Objetos.Bloque;
import Objetos.Operacion;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author alejandro
 */
public class DobleMenteEnlazada {

    private String puerto;
    private NodoL cabeza;
    private int tamaño;

    public DobleMenteEnlazada() {
        this.cabeza = null;
        this.tamaño = 0;
        this.puerto = "";
    }

    public void insertar(Object dato) {
        NodoL nuevo = new NodoL(dato);
        if (estaVacio()) {
            this.cabeza = nuevo;
            this.cabeza.setAnterior(null);
            this.cabeza.setSiguiente(null);
        } else {
            NodoL auxiliar = this.cabeza;
            while (auxiliar.getSiguiente() != null) {
                auxiliar = auxiliar.getSiguiente();
            }
            auxiliar.setSiguiente(nuevo);
            nuevo.setAnterior(auxiliar);
            nuevo.setSiguiente(null);
            this.cabeza.setAnterior(nuevo);
        }
        this.tamaño++;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public boolean existe(Object dato) {
        boolean existe = false;
        if (!estaVacio()) {
            NodoL temp = this.cabeza;
            for (int i = 0; i < tamaño; i++) {
                if (temp.getValor() == dato) {
                    existe = true;
                    break;
                } else {
                    temp = temp.getSiguiente();
                }
            }
        }
        return existe;
    }

    public NodoL getCabeza() {
        return cabeza;
    }

    public int getTamaño() {
        return tamaño;
    }

    public boolean estaVacio() {
        return cabeza == null;
    }

    public void dot() {
        String Dot = "digraph G{\nnode[fillcolor = skyblue, style = filled, shape = record];\n";
        Dot += "label =  <<font point-size='20'>Lista doblemente enlazada BLOCKCHAIN del puerto: " + this.puerto + "</font>>;\nlabelloc = \"t \";\n";
        NodoL aux = cabeza;
        int contador = 0;
        while (aux != null) {
            Bloque temp = (Bloque) aux.getValor();
            if (aux != cabeza) {
                Dot += "a" + (contador - 1) + "->a" + contador + ";\n";
                Dot += "a" + contador + "->a" + (contador - 1) + ";\n";
            }
            String Data = "";
            for (int i = 0; i < temp.getDATA().Tamaño(); i++) {
                Operacion op = (Operacion)temp.getDATA().at(i);
                Data += op.paraGraphviz();
            }
            Dot += "a" + contador + "[label = \"{INDEX|TIMESTAMP|DATA|NONCE|PREVIOUSHASH|HASH}|{"
                    + temp.getINDEX() + "|" + temp.getTIMESTAMP() + "|" + Data + "|" + temp.getNONCE()
                    + "|" + temp.getPREVIOUSHASH() + "|" + temp.getHASH() + "}\"];\n";
            contador++;
            aux = aux.getSiguiente();
        }
        Dot += "}";
        FileWriter fichero = null;
        PrintWriter escritor;
        try {
            fichero = new FileWriter("/home/alejandro/Escritorio/BloquesPuerto_" + this.puerto + ".dot");
            escritor = new PrintWriter(fichero);
            escritor.print(Dot);
        } catch (Exception e) {
            System.err.println("Error al escribir el archivo BloquesPuerto_" + this.puerto + ".dot");
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                System.err.println("Error al cerrar el archivo BloquesPuerto_" + this.puerto + ".dot");
            }
        }
        try {
            Runtime rt = Runtime.getRuntime();
            rt.exec("dot -Tjpg -o " + "/home/alejandro/Escritorio/BloquesPuerto_" + this.puerto + ".jpg" + " /home/alejandro/Escritorio/BloquesPuerto_" + this.puerto + ".dot");
            Thread.sleep(500);
        } catch (Exception ex) {
            System.err.println("Error al generar la imagen para el archivo BloquesPuerto_" + this.puerto + ".dot");
        }
    }
}
