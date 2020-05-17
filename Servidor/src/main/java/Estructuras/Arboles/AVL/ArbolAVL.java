/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructuras.Arboles.AVL;

import Estructuras.Arboles.Comparador;
import Estructuras.Arboles.ABB.Logical;
import Estructuras.Listas.SimplementeEnlazada.SimpleMenteEnlazada;
import Objetos.Categoria;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 *
 * @author alejandro
 */
public class ArbolAVL {

    NodoAVL raiz;
    private String carpeta;

    public ArbolAVL() {
        raiz = null;
        this.carpeta = "";
    }

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    public NodoAVL raizArbol() {
        return raiz;
    }

    private NodoAVL rotacionII(NodoAVL n, NodoAVL n1) {
        n.ramaIzdo(n1.subarbolDcho());
        n1.ramaDcho(n);
        if (n1.fe == -1) {
            n.fe = 0;
            n1.fe = 0;
        } else {
            n.fe = -1;
            n1.fe = 1;
        }
        return n1;
    }

    private NodoAVL rotacionDD(NodoAVL n, NodoAVL n1) {
        n.ramaDcho(n1.subarbolIzdo());
        n1.ramaIzdo(n);
        if (n1.fe == +1) {
            n.fe = 0;
            n1.fe = 0;
        } else {
            n.fe = +1;
            n1.fe = -1;
        }
        return n1;
    }

    private NodoAVL rotacionID(NodoAVL n, NodoAVL n1) {
        NodoAVL n2;
        n2 = (NodoAVL) n1.subarbolDcho();
        n.ramaIzdo(n2.subarbolDcho());
        n2.ramaDcho(n);
        n1.ramaDcho(n2.subarbolIzdo());
        n2.ramaIzdo(n1);
        if (n2.fe == +1) {
            n1.fe = -1;
        } else {
            n1.fe = 0;
        }
        if (n2.fe == -1) {
            n.fe = 1;
        } else {
            n.fe = 0;
        }
        n2.fe = 0;
        return n2;
    }

    private NodoAVL rotacionDI(NodoAVL n, NodoAVL n1) {
        NodoAVL n2;
        n2 = (NodoAVL) n1.subarbolIzdo();
        n.ramaDcho(n2.subarbolIzdo());
        n2.ramaIzdo(n);
        n1.ramaIzdo(n2.subarbolDcho());
        n2.ramaDcho(n1);
        if (n2.fe == +1) {
            n.fe = -1;
        } else {
            n.fe = 0;
        }
        if (n2.fe == -1) {
            n1.fe = 1;
        } else {
            n1.fe = 0;
        }
        n2.fe = 0;
        return n2;
    }

    public void insertar(Object valor) throws Exception {
        Comparador dato;
        Logical h = new Logical(false);
        dato = (Comparador) valor;
        raiz = insertarAvl(raiz, dato, h);
    }

    private NodoAVL insertarAvl(NodoAVL raiz, Comparador dt, Logical h) throws Exception {
        NodoAVL n1;
        if (raiz == null) {
            raiz = new NodoAVL(dt);
            h.setLogical(true);
        } else if (dt.menorQue(raiz.valorNodo())) {
            NodoAVL iz;
            iz = insertarAvl((NodoAVL) raiz.subarbolIzdo(), dt, h);
            raiz.ramaIzdo(iz);
            if (h.booleanValue()) {
                switch (raiz.fe) {

                    case 1:
                        raiz.fe = 0;
                        h.setLogical(false);
                        break;

                    case 0:
                        raiz.fe = -1;
                        break;
                    case -1:
                        n1 = (NodoAVL) raiz.subarbolIzdo();
                        if (n1.fe == -1) {
                            raiz = rotacionII(raiz, n1);
                        } else {
                            raiz = rotacionID(raiz, n1);
                        }
                        h.setLogical(false);
                }
            }
        } else if (dt.mayorQue(raiz.valorNodo())) {
            NodoAVL dr;
            dr = insertarAvl((NodoAVL) raiz.subarbolDcho(), dt, h);
            raiz.ramaDcho(dr);
            if (h.booleanValue()) {
                switch (raiz.fe) {
                    case 1:
                        n1 = (NodoAVL) raiz.subarbolDcho();
                        if (n1.fe == +1) {
                            raiz = rotacionDD(raiz, n1);
                        } else {
                            raiz = rotacionDI(raiz, n1);
                        }
                        h.setLogical(false);
                        break;
                    case 0:
                        raiz.fe = +1;
                        break;

                    case -1:
                        raiz.fe = 0;
                        h.setLogical(false);
                }
            }
        } else {
            System.out.println("CLAVE REPETIDA");
        }
        return raiz;
    }

    public void eliminar(Object valor) throws Exception {
        Comparador dato;
        dato = (Comparador) valor;
        Logical flag = new Logical(false);
        raiz = borrarAvl(raiz, dato, flag);
    }

    private NodoAVL borrarAvl(NodoAVL r, Comparador clave,
            Logical cambiaAltura) throws Exception {
        if (r == null) {
            throw new Exception(" Nodo no encontrado ");
        } else if (clave.menorQue(r.valorNodo())) {
            NodoAVL iz;
            iz = borrarAvl((NodoAVL) r.subarbolIzdo(), clave, cambiaAltura);
            r.ramaIzdo(iz);
            if (cambiaAltura.booleanValue()) {
                r = equilibrar1(r, cambiaAltura);
            }
        } else if (clave.mayorQue(r.valorNodo())) {
            NodoAVL dr;
            dr = borrarAvl((NodoAVL) r.subarbolDcho(), clave, cambiaAltura);
            r.ramaDcho(dr);
            if (cambiaAltura.booleanValue()) {
                r = equilibrar2(r, cambiaAltura);
            }
        } else {
            NodoAVL q;
            q = r;
            if (q.subarbolIzdo() == null) {
                r = (NodoAVL) q.subarbolDcho();
                cambiaAltura.setLogical(true);
            } else if (q.subarbolDcho() == null) {
                r = (NodoAVL) q.subarbolIzdo();
                cambiaAltura.setLogical(true);
            } else {
                NodoAVL iz;
                iz = reemplazar(r, (NodoAVL) r.subarbolIzdo(), cambiaAltura);
                r.ramaIzdo(iz);
                if (cambiaAltura.booleanValue()) {
                    r = equilibrar1(r, cambiaAltura);
                }
            }
            q = null;
        }
        return r;
    }

    private NodoAVL reemplazar(NodoAVL n, NodoAVL act, Logical cambiaAltura) {
        if (act.subarbolDcho() != null) {
            NodoAVL d;
            d = reemplazar(n, (NodoAVL) act.subarbolDcho(), cambiaAltura);
            act.ramaDcho(d);
            if (cambiaAltura.booleanValue()) {
                act = equilibrar2(act, cambiaAltura);
            }
        } else {
            n.nuevoValor(act.valorNodo());
            n = act;
            act = (NodoAVL) act.subarbolIzdo();
            n = null;
            cambiaAltura.setLogical(true);
        }
        return act;
    }

    private NodoAVL equilibrar1(NodoAVL n, Logical cambiaAltura) {
        NodoAVL n1;
        switch (n.fe) {
            case -1:
                n.fe = 0;
                break;
            case 0:
                n.fe = 1;
                cambiaAltura.setLogical(false);
                break;
            case +1:
                n1 = (NodoAVL) n.subarbolDcho();
                if (n1.fe >= 0) {
                    if (n1.fe == 0) {
                        cambiaAltura.setLogical(false);
                    }
                    n = rotacionDD(n, n1);
                } else {
                    n = rotacionDI(n, n1);
                }
                break;
        }
        return n;
    }

    private NodoAVL equilibrar2(NodoAVL n, Logical cambiaAltura) {
        NodoAVL n1;
        switch (n.fe) {
            case -1:
                n1 = (NodoAVL) n.subarbolIzdo();
                if (n1.fe <= 0) {
                    if (n1.fe == 0) {
                        cambiaAltura.setLogical(false);
                    }
                    n = rotacionII(n, n1);
                } else {
                    n = rotacionID(n, n1);
                }
                break;
            case 0:
                n.fe = -1;
                cambiaAltura.setLogical(false);
                break;
            case +1:
                n.fe = 0;
                break;
        }
        return n;
    }

    public void dot() {
        if (raiz != null) {
            String Dot = "digraph G{\n rankdir = TB;\n node[shape = record, style= filled, fillcolor = gray];\n";
            Dot += "label =  <<font point-size='20'>AVL de categorías</font>>;\n";
            Dot += raiz.dot();
            Dot += "}\n";
            FileWriter fichero = null;
            PrintWriter escritor;
            try {
                fichero = new FileWriter(carpeta + "/AVL.dot");
                escritor = new PrintWriter(fichero);
                escritor.print(Dot);
            } catch (Exception e) {
                System.err.println("Error al escribir el archivo AVL.dot");
            } finally {
                try {
                    if (null != fichero) {
                        fichero.close();
                    }
                } catch (Exception e2) {
                    System.err.println("Error al cerrar el archivo AVL.dot");
                }
            }
            try {
                Runtime rt = Runtime.getRuntime();
                rt.exec("dot -Tjpg -o " + carpeta + "/AVL.jpg" + " " + carpeta + "/AVL.dot");
                Thread.sleep(500);
            } catch (Exception ex) {
                System.err.println("Error al generar la imagen para el archivo AVL.dot");
            }
            dotPreOrder();
            dotInOrder();
            dotPosOrder();
        }
    }

    public void dotPreOrder() {
        SimpleMenteEnlazada preOrder = objetos(0);
        preOrder.setCarpeta(carpeta);
        preOrder.dot(0, "AVL_PreOrder");
    }

    public void dotInOrder() {
        SimpleMenteEnlazada inOrder = objetos(1);
        inOrder.setCarpeta(carpeta);
        inOrder.dot(0, "AVL_InOrder");
    }

    public void dotPosOrder() {
        SimpleMenteEnlazada posOrder = objetos(2);
        posOrder.setCarpeta(carpeta);
        posOrder.dot(0, "AVL_PosOrder");
    }

    private SimpleMenteEnlazada objetos(int tipo) {
        SimpleMenteEnlazada lista = new SimpleMenteEnlazada();
        crearLista(lista, raiz, tipo);
        return lista;
    }

    private void crearLista(SimpleMenteEnlazada lista, NodoAVL nodo, int tipo) {
        if (nodo != null) {
            switch (tipo) {
                case 0:
                    //pre order
                    lista.insertar(nodo.valorNodo());
                    crearLista(lista, nodo.subarbolIzdo(), 0);
                    crearLista(lista, nodo.subarbolDcho(), 0);

                    break;
                case 1:
                    //in order
                    crearLista(lista, nodo.subarbolIzdo(), 1);
                    lista.insertar(nodo.valorNodo());
                    crearLista(lista, nodo.subarbolDcho(), 1);
                    break;
                case 2:
                    //pos order
                    crearLista(lista, nodo.subarbolIzdo(), 2);
                    crearLista(lista, nodo.subarbolDcho(), 2);
                    lista.insertar(nodo.valorNodo());
                    break;
            }
        }
    }

    public boolean buscar(Object buscado) {
        boolean encontrado = false;
        if (raiz != null) {
            NodoAVL raizSub = raiz;
            Comparador comp = (Comparador) buscado;
            while (encontrado == false && raizSub != null) {
                if (comp.igualQue(raizSub.valorNodo())) {
                    encontrado = true;
                } else if (comp.menorQue(raizSub.valorNodo())) {
                    raizSub = raizSub.subarbolIzdo();
                } else {
                    raizSub = raizSub.subarbolDcho();
                }
            }
        }
        return encontrado;
    }

    public Categoria get(String Nombre) {
        boolean encontrado = false;
        Categoria encontrada = null;
        if (raiz != null) {
            NodoAVL raizSub = raiz;
            while (encontrado == false && raizSub != null) {
                Categoria temp = (Categoria) raizSub.valorNodo();
                if (temp.getNombre().compareTo(Nombre) == 0) {
                    encontrado = true;
                    encontrada = temp;
                } else if (temp.getNombre().compareTo(Nombre) > 0) {
                    raizSub = raizSub.subarbolIzdo();
                } else {
                    raizSub = raizSub.subarbolDcho();
                }
            }
        }
        return encontrada;
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    public SimpleMenteEnlazada bibliotecaUsuario(int Carnet) {
        SimpleMenteEnlazada categorias = objetos(0);
        SimpleMenteEnlazada biblioteca = new SimpleMenteEnlazada();
        for (int i = 0; i < categorias.Tamaño(); i++) {
            Categoria temp = (Categoria) categorias.at(i);
            temp.BibliotecaUsuario(Carnet, biblioteca);
        }
        return biblioteca;
    }

    public SimpleMenteEnlazada bibliotecaEnLista() {
        SimpleMenteEnlazada categorias = objetos(0);
        SimpleMenteEnlazada biblioteca = new SimpleMenteEnlazada();
        for (int i = 0; i < categorias.Tamaño(); i++) {
            Categoria temp = (Categoria) categorias.at(i);
            temp.librosDeCategoria(biblioteca);
        }
        return biblioteca;
    }

    public boolean buscarLibro(int ISBN) {
        boolean encontrado = false;
        SimpleMenteEnlazada categorias = objetos(0);
        for (int i = 0; i < categorias.Tamaño(); i++) {
            Categoria temp = (Categoria) categorias.at(i);
            if (temp.getArbol().buscar(ISBN) != null) {
                encontrado = true;
                break;
            }
        }
        return encontrado;
    }

    public SimpleMenteEnlazada categoriasDeUsuario(int Carnet) {
        SimpleMenteEnlazada categorias = objetos(0);
        SimpleMenteEnlazada deUsuario = new SimpleMenteEnlazada();
        for (int i = 0; i < categorias.Tamaño(); i++) {
            Categoria actual = (Categoria) categorias.at(i);
            if (actual.getCarnet() == Carnet) {
                deUsuario.insertar(actual);
            }
        }
        return deUsuario;
    }
}
