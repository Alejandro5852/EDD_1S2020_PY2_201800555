/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructuras.Arboles.B;

import Estructuras.Listas.SimplementeEnlazada.SimpleMenteEnlazada;
import Objetos.Libro;

/**
 *
 * @author alejandro
 */
public class NodoB {

    private int orden;
    private Libro claves[];
    private int n;
    private NodoB hijos[];
    private NodoB padre;
    private boolean esHoja;
    private boolean esRaiz;

    public NodoB(int orden, boolean esHoja) {
        this.orden = orden;
        this.hijos = new NodoB[orden + 1];
        this.claves = new Libro[orden];
        this.n = 0;
        this.esHoja = esHoja;
        this.esRaiz = false;
    }

    public void insertar(Libro lib) {
        int i = n;
        while (i > 0 && claves[i - 1].getISBN() > lib.getISBN()) {
            claves[i] = claves[i - 1];
            i--;
        }
        claves[i] = lib;
        n++;
        if (n == orden) {
            this.partir();
        }
    }

    public void partir() {
        if (esRaiz) {
            NodoB auxL = new NodoB(orden, esHoja);
            NodoB auxR = new NodoB(orden, esHoja);
            int i;
            int j = orden / 2 + 1;
            for (i = 0; i < orden / 2; i++, j++) {
                auxL.claves[i] = claves[i];
                auxL.hijos[i] = hijos[i];
                auxR.claves[i] = claves[j];
                auxR.hijos[i] = hijos[j];
                if (!esHoja) {
                    auxL.hijos[i].padre = auxL;
                    auxR.hijos[i].padre = auxR;
                }
                auxL.n++;
                auxR.n++;
            }
            auxL.hijos[i] = hijos[i];
            auxR.hijos[i] = hijos[j];
            if (!esHoja) {
                auxL.hijos[i].padre = auxL;
                auxR.hijos[i].padre = auxR;
            }
            claves[0] = claves[orden / 2];
            hijos[0] = auxL;
            hijos[1] = auxR;
            auxL.padre = this;
            auxR.padre = this;
            esHoja = false;
            n = 1;
        } else {
            NodoB aux = new NodoB(orden, esHoja);
            int i = orden / 2 + 1;
            int j;
            for (j = 0; j < orden / 2; j++, i++) {
                aux.claves[j] = claves[i];
                aux.hijos[j] = hijos[i];
                if (!esHoja) {
                    aux.hijos[j].padre = aux;
                }
                aux.n++;
            }
            aux.hijos[j] = hijos[i];
            if (!esHoja) {
                aux.hijos[j].padre = aux;
            }
            aux.padre = padre;
            n = orden / 2;
            int z = padre.n;
            while (z > 0 && claves[orden / 2].getISBN() < padre.claves[z - 1].getISBN()) {
                padre.hijos[z + 1] = padre.hijos[z];
                z--;
            }
            padre.hijos[z + 1] = aux;
            padre.insertar(claves[orden / 2]);
        }
    }

    public NodoB buscar(int key) {
        int i = 0;
        while (i < n && key > claves[i].getISBN()) {
            i++;
        }
        if (claves[i] != null) {
            if (claves[i].getISBN() == key) {
                return this;
            }
        }
        if (esHoja) {
            return null;
        }
        return hijos[i].buscar(key);
    }

    public void quitar(int key) {
        NodoB aux = buscar(key);
        if (aux != null) {
            int i = 0;
            while (key != aux.claves[i].getISBN()) {
                i++;
            }
            Libro del_Book = aux.claves[i];
            if (!aux.esHoja) {
                NodoB temp = aux.hijos[i + 1];
                if (!temp.esHoja) {
                    temp = temp.hijos[0];
                }
                aux.claves[i] = temp.claves[0];
                temp.n--;
                int j;
                for (j = 0; j < temp.n; j++) {
                    temp.claves[j] = temp.claves[j + 1];
                }
                aux = temp;
            } else {
                while (i < aux.n) {
                    aux.claves[i] = aux.claves[i + 1];
                    i++;
                }
                aux.n--;

            }
            while (!aux.esRaiz && aux.n < orden / 2) {
                int j = 0;
                while (aux.padre.hijos[j] != aux) {
                    j++;
                }
                if (aux.n >= orden / 2) {
                    return;
                } else if (j != aux.padre.n && aux.padre.hijos[j + 1].n > orden / 2) {
                    aux.insertarIzq(j + 1);
                } else if (j != 0 && aux.padre.hijos[j - 1].n > orden / 2) {
                    aux.insertarDer(j - 1);
                } else if (aux.padre.esRaiz) {
                    if (aux.padre.n == 1) {
                        aux.unirRaiz();
                    } else {
                        if (j != aux.padre.n) {
                            aux.unirIzq(j + 1);
                        } else {
                            aux.unirDer(j - 1);
                        }
                    }
                    return;
                } else {
                    if (j != aux.padre.n) {
                        aux.unirIzq(j + 1);
                    } else {
                        aux.unirDer(j - 1);
                    }
                }
                aux = aux.padre;
            }
        }
    }

    void insertarIzq(int j) {
        claves[n] = padre.claves[j - 1];
        n++;

        hijos[n] = padre.hijos[j].hijos[0];
        if (!esHoja) {
            hijos[n].padre = this;
        }

        padre.claves[j - 1] = padre.hijos[j].claves[0];
        padre.hijos[j].n--;
        int i;
        for (i = 0; i < padre.hijos[j].n; i++) {
            padre.hijos[j].claves[i] = padre.hijos[j].claves[i + 1];
            padre.hijos[j].hijos[i] = padre.hijos[j].hijos[i + 1];
        }
        padre.hijos[j].hijos[i] = padre.hijos[j].hijos[i + 1];
    }

    void insertarDer(int j) {
        int i = n;

        for (n = i; i > 0; i--) {
            claves[i] = claves[i - 1];
            hijos[i + 1] = hijos[i];
        }
        hijos[i + 1] = hijos[i];

        claves[0] = padre.claves[j];
        hijos[0] = padre.hijos[j].hijos[padre.hijos[j].n];
        if (!esHoja) {
            hijos[0].padre = this;
        }
        n++;

        padre.claves[j] = padre.hijos[j].claves[padre.hijos[j].n - 1];
        padre.hijos[j].n--;
    }

    void unirIzq(int merge) {
        claves[n] = padre.claves[merge - 1];
        n++;
        int z;
        for (z = 0; z < orden / 2; z++) {
            claves[orden / 2 + z] = padre.hijos[merge].claves[z];
            hijos[orden / 2 + z] = padre.hijos[merge].hijos[z];
            if (!esHoja) {
                hijos[orden / 2 + z].padre = this;
            }
            n++;
        }

        hijos[orden / 2 + z] = padre.hijos[merge].hijos[z];
        if (!esHoja) {
            hijos[orden / 2 + z].padre = this;
        }

        for (int i = 0; i < padre.n - merge; i++) {
            padre.claves[merge - 1 + i] = padre.claves[merge + i];
            padre.hijos[merge + i] = padre.hijos[merge + i + 1];
        }
        padre.n--;

    }

    void unirDer(int merge) {
        int i = orden - 2;
        int k = n - 1;

        hijos[i + 1] = hijos[k + 1];
        for (k = n - 1; k >= 0; i--, k--) {
            claves[i] = claves[k];
            hijos[i] = hijos[k];
        }

        claves[orden / 2] = padre.claves[merge];
        n++;

        int z = 0;
        hijos[orden / 2] = padre.hijos[merge].hijos[padre.hijos[merge].n];
        if (!esHoja) {
            hijos[orden / 2 + z].padre = this;
        }

        for (z = 0; z < orden / 2; z++) {
            claves[orden / 2 - 1 - z] = padre.hijos[merge].claves[padre.hijos[merge].n - 1 - z];
            hijos[orden / 2 - 1 - z] = padre.hijos[merge].hijos[padre.hijos[merge].n - 1 - z];
            if (!esHoja) {
                hijos[orden / 2 - 1 - z].padre = this;
            }
            n++;
        }

        for (int j = 0; j < padre.n - merge; j++) {
            padre.claves[merge + j] = padre.claves[j + 1 + merge];
            padre.hijos[merge + j] = padre.hijos[j + 1 + merge];
        }
        padre.n--;
    }

    void unirRaiz() {
        if (claves[0].getISBN() < padre.claves[0].getISBN()) {
            unirPorLaIzquierda();
        } else {
            unirPorLaDerecha();
        }
    }

    void unirPorLaIzquierda() {
        padre.esHoja = esHoja;
        padre.claves[orden / 2 - 1] = padre.claves[0];
        NodoB aux = padre.hijos[1];
        int i;
        for (i = 0; i < n; i++) {
            padre.claves[i] = claves[i];
            padre.hijos[i] = hijos[i];
            if (!padre.esHoja) {
                padre.hijos[i].padre = padre;
            }
            padre.n++;
        }
        padre.hijos[i] = hijos[i];
        if (!padre.esHoja) {
            padre.hijos[i].padre = padre;
        }
        int z;
        for (z = 0; z < aux.n; z++) {
            padre.claves[orden / 2 + z] = aux.claves[z];
            padre.hijos[orden / 2 + z] = aux.hijos[z];
            if (!padre.esHoja) {
                padre.hijos[orden / 2 + z].padre = padre;
            }
            padre.n++;
        }
        padre.hijos[orden / 2 + z] = aux.hijos[z];
        if (!padre.esHoja) {
            padre.hijos[orden / 2 + z].padre = padre;
        }

    }

    void unirPorLaDerecha() {
        padre.esHoja = esHoja;
        padre.claves[orden / 2] = padre.claves[0];
        NodoB aux = padre.hijos[0];
        int i;
        for (i = 0; i < n; i++) {
            padre.claves[orden / 2 + 1 + i] = claves[i];
            padre.hijos[orden / 2 + 1 + i] = hijos[i];
            if (!padre.esHoja) {
                padre.hijos[orden / 2 + 1 + i].padre = padre;
            }
            padre.n++;
        }

        padre.hijos[orden / 2 + 1 + i] = hijos[i];
        if (!padre.esHoja) {
            padre.hijos[orden / 2 + 1 + i].padre = padre;
        }

        int z;
        for (z = 0; z < aux.n; z++) {
            padre.claves[z] = aux.claves[z];
            padre.hijos[z] = aux.hijos[z];
            if (!padre.esHoja) {
                padre.hijos[z].padre = padre;
            }
            padre.n++;
        }
        padre.hijos[z] = aux.hijos[z];
        if (!padre.esHoja) {
            padre.hijos[z].padre = padre;
        }

    }

    public int traverse() {
        int count = 0;
        int i = 0;
        for (i = 0; i < this.n; i++) {
            if (!this.esHoja) {
                count += hijos[i].traverse();
            }
            count++;
        }
        if (!esHoja) {
            count += hijos[i].traverse();
        }
        return count;
    }

    public boolean EsHoja() {
        return esHoja;
    }

    public void setEsHoja(boolean esHoja) {
        this.esHoja = esHoja;
    }

    public boolean EsRaiz() {
        return esRaiz;
    }

    public void setEsRaiz(boolean esRaiz) {
        this.esRaiz = esRaiz;
    }

    public Libro getClave(int indice) {
        return claves[indice];
    }

    public NodoB getHijo(int indice) {
        return hijos[indice];
    }

    public void setHijo(int indice, NodoB value) {
        this.hijos[indice] = value;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public String dot() {
        String Dot = "";
        Dot += getNombre() + "[label = \"<P0>";
        for (int i = 0; i < n; i++) {
            Libro clave = claves[i];
            Dot += "|" + clave.getTitulo() + "; ISBN:" + clave.getISBN() + "|<P" + (i + 1) + ">";
        }
        Dot += "\"];\n";
        for (int i = 0; i <= n; i++) {
            if (hijos[i] != null) {
                Dot += hijos[i].dot();
                Dot += getNombre() + ":P" + i + " ->" + hijos[i].getNombre() + ";\n";
            }
        }
        return Dot;
    }

    public String getNombre() {
        return "Nodo_" + this.hashCode();
    }

    public void general(SimpleMenteEnlazada argumento) {
        for (int i = 0; i < n; i++) {
            Libro clave = claves[i];
            argumento.insertar(clave);
        }
        for (int i = 0; i <= n; i++) {
            if (hijos[i] != null) {
                hijos[i].general(argumento);
            }
        }
    }

    public void porUsuario(SimpleMenteEnlazada argumento, int Carnet) {
        for (int i = 0; i < n; i++) {
            Libro clave = claves[i];
            if (clave.getCarnet() == Carnet) {
                argumento.insertar(clave);
            }
        }
        for (int i = 0; i <= n; i++) {
            if (hijos[i] != null) {
                hijos[i].porUsuario(argumento, Carnet);
            }
        }
    }

    public void librosAsociados(int argumento) {
        argumento += n;
        for (int i = 0; i < n; i++) {
            if (hijos[i] != null) {
                hijos[i].librosAsociados(argumento);
            }
        }
    }

    protected static NodoB getChildNodeAtIndex(NodoB btNode, int indice, int nDirection) {
        if (btNode.EsHoja()) {
            return null;
        }

        indice += nDirection;
        if ((indice < 0) || (indice > btNode.getN())) {
            return null;
        }

        return btNode.hijos[indice];
    }

    protected static NodoB getLeftChildAtIndex(NodoB btNode, int ISBN) {
        return getChildNodeAtIndex(btNode, ISBN, 0);
    }

    protected static NodoB getRightChildAtIndex(NodoB btNode, int ISBN) {
        return getChildNodeAtIndex(btNode, ISBN, 1);
    }

    protected static NodoB getLeftSiblingAtIndex(NodoB parentNode, int ISBN) {
        return getChildNodeAtIndex(parentNode, ISBN, -1);
    }

    protected static NodoB getRightSiblingAtIndex(NodoB parentNode, int ISBN) {
        return getChildNodeAtIndex(parentNode, ISBN, 1);
    }
}
