/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

/**
 *
 * @author alejandro
 */
public class Operacion {

    public enum Tipo {
        CREAR_LIBRO,
        CREAR_USUARIO,
        EDITAR_USUARIO,
        ELIMINAR_LIBRO,
        CREAR_CATEGORIA,
        ELIMINAR_CATEGORIA
    }
    private Tipo tipo;
    private Object involucrado;

    public Operacion(Tipo tipo, Object involucrado) {
        this.tipo = tipo;
        this.involucrado = involucrado;
    }

    public String getTipo() {
        return tipo.toString();
    }

    public Object getInvolucrado() {
        return involucrado;
    }

    public String paraGraphviz() {
        String dot = getTipo() + ": ";
        if (involucrado instanceof Libro) {
            Libro lib = (Libro) involucrado;
            if (tipo == Tipo.ELIMINAR_LIBRO) {
                dot += "ISBN: " + lib.getISBN() + ", " + "TITULO: " + lib.getTitulo() + ": " + "CATEGORIA: " + lib.getCategoria() + "; ";
            } else {
                dot += "ISBN: " + lib.getISBN() + ", AÑO: " + lib.getAño() + ", IDIOMA: " + lib.getIdioma() + ", TITULO: " + lib.getTitulo() + ": "
                        + ", EDITORIAL: " + lib.getEditorial() + ", AUTOR: " + lib.getAutor() + ", EDICION: " + lib.getEdicion()
                        + ", CATEGORIA: " + lib.getCategoria() + "; ";
            }
        } else if (involucrado instanceof Usuario) {
            Usuario user = (Usuario) involucrado;
            dot += "CARNET: " + user.getCarnet() + ", NOMBRE: " + user.getNombre() + ", APELLIDO: " + user.getApellido()
                    + ", CARRERA: " + user.getCarrera() + ", PASSWORD: " + user.getContraseña() + "; ";
        } else {
            Categoria cat = (Categoria) involucrado;
            dot += "NOMBRE: " + cat.getNombre() + "; ";
        }
        return dot;
    }
}
