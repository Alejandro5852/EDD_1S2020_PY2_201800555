/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estructuras.Arboles;

/**
 *
 * @author alejandro
 */
public class NodoAVL extends Nodo {

    int fe;

    public NodoAVL(Object valor) {
        super(valor);
        fe = 0;
    }

    public NodoAVL(Object valor, NodoAVL ramaIzdo, NodoAVL ramaDcho) {
        super(ramaIzdo, valor, ramaDcho);
        fe = 0;
    }
}
/*
    string Dot = "";
    Dot+='\"'+player->getNombre().toStdString() + '\"'+"[label = " +'\"' +player->getNombre().toStdString() + '\"'+"];\n";
    if(izq)
    {
        Dot+= izq->dot() + '\"' + player->getNombre().toStdString() + '\"' + ":C0->" + '\"' + izq->getPlayer()->getNombre().toStdString() + '\"' +";\n";
    }
    if(der)
    {
        Dot+= der->dot() + '\"' + player->getNombre().toStdString() + '\"' + ":C1->" + '\"' + der->getPlayer()->getNombre().toStdString() + '\"' +";\n";
    }
    Dot+="\n";
    return Dot;


*/