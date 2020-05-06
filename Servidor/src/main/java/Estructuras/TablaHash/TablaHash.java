/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructuras.TablaHash;

import Objetos.Usuario;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author alejandro
 */
public class TablaHash {

    private Elemento[] elementos;
    private int M;
    private int contador;
    String carpeta;

    public TablaHash() {
        this.M = 45;
        this.elementos = new Elemento[M];
        for (int i = 0; i < M; i++) {
            this.elementos[i] = null;
        }
        this.contador = 0;
        this.carpeta = "";
    }

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    public void insertar(Usuario usuario) {
        int pos = funcionHash(usuario.getCarnet());
        Elemento nuevo = new Elemento(usuario);
        nuevo.setSiguiente(elementos[pos]);
        elementos[pos] = nuevo;
        this.contador++;
    }

    public void eliminar(int carnet) {
        int pos = funcionHash(carnet);
        if (elementos[pos] != null) {
            Elemento anterior = null;
            Elemento actual = elementos[pos];
            while (actual.getSiguiente() != null && actual.getUsuario().getCarnet() != carnet) {
                anterior = actual;
                actual = actual.getSiguiente();
            }
            if (actual.getUsuario().getCarnet() != carnet) {
                System.out.println("Usuario inexistente");
            } else {
                if (anterior == null) {
                    elementos[pos] = actual.getSiguiente();
                } else {
                    anterior.setSiguiente(actual.getSiguiente());
                }
                actual = null;
                this.contador--;
            }
        }
    }

    public Elemento buscar(int carnet) {
        Elemento encontrado = null;
        int pos = funcionHash(carnet);
        if (elementos[pos] != null) {
            encontrado = elementos[pos];
            while (encontrado.getSiguiente() != null && encontrado.getUsuario().getCarnet() != carnet) {
                encontrado = encontrado.getSiguiente();
            }
            if (encontrado.getUsuario().getCarnet() != carnet) {
                encontrado = null;
            }
        }
        return encontrado;
    }

    protected int funcionHash(int carnet) {
        return carnet % this.M;
    }

    public int getContador() {
        return contador;
    }

    public void dot() {
        int contador = 0;
        String Dot = "digraph G{\nrankdir = TB;\nnode[shape = box, style= filled, fillcolor = lightblue];\n";
        Dot += "label =  <<font point-size='20'>Tabla hash de usuarios</font>>;\n";
        for (int i = 0; i < M; i++) {
            if (i > 0) {
                Dot += "p" + (i - 1) + "->p" + i + ";\n";
            }
            if (elementos[i] == null) {
                Dot += "p" + i + "[label = \"null\"];\n";
            } else {
                Dot += "p" + i + "[label = \"" + elementos[i].getUsuario().getNombre() + " " + elementos[i].getUsuario().getApellido() + "\"];\n";
                if (elementos[i].getSiguiente() != null) {
                    Elemento aux = elementos[i].getSiguiente();
                    Dot += "p" + i + "->a_" + contador + ";\n";
                    String rank = "{rank = same; " + "p" + i + "; a_" + contador + "; ";
                    while (aux != null) {
                        Dot += "a_" + contador + "[label = \"" + aux.getUsuario().getNombre() + " " + aux.getUsuario().getApellido() + "\"];\n";
                        contador++;
                        if (aux.getSiguiente() != null) {
                            Dot += "a_" + (contador - 1) + "->a_" + contador + ";\n";
                            rank += "a_" + contador + "; ";
                            aux = aux.getSiguiente();
                        } else {
                            break;
                        }
                    }
                    Dot += rank + "}\n";
                }
            }
        }
        Dot += "}";
        FileWriter fichero = null;
        PrintWriter escritor;
        try {
            fichero = new FileWriter(carpeta + "/TablaHash.dot");
            escritor = new PrintWriter(fichero);
            escritor.print(Dot);
        } catch (Exception e) {
            System.err.println("Error al escribir el archivo TablaHash.dot");
        } finally {
            try {
                if (null != fichero) {
                    fichero.close();
                }
            } catch (Exception e2) {
                System.err.println("Error al cerrar el archivo TablaHash.dot");
            }
        }
        try {
            Runtime rt = Runtime.getRuntime();
            rt.exec("dot -Tjpg -o " + carpeta + "/TablaHash.jpg" + " " + carpeta + "/TablaHash.dot");
            Thread.sleep(500);
        } catch (Exception ex) {
            System.err.println("Error al generar la imagen para el archivo TablaHash.dot");
        }
    }

}
