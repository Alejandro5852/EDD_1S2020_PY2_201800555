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
                dot += "ISBN: " + lib.getISBN() + ", " + "TITULO: " + lib.getTitulo() + ": " + "CATEGORIA: " + lib.getCategoria() + "<br/>";
            } else {
                dot += "ISBN: " + lib.getISBN() + ", AÑO: " + lib.getAño() + ", IDIOMA: " + lib.getIdioma() + ", TITULO: " + lib.getTitulo() + ": "
                        + ", EDITORIAL: " + lib.getEditorial() + ", AUTOR: " + lib.getAutor() + ", EDICION: " + lib.getEdicion()
                        + ", CATEGORIA: " + lib.getCategoria() + "<br/>";
            }
        } else if (involucrado instanceof Usuario) {
            Usuario user = (Usuario) involucrado;
            dot += "CARNET: " + user.getCarnet() + ", NOMBRE: " + user.getNombre() + ", APELLIDO: " + user.getApellido()
                    + ", CARRERA: " + user.getCarrera() + ", PASSWORD: " + user.getContraseña() + "<br/>";
        } else {
            Categoria cat = (Categoria) involucrado;
            dot += "NOMBRE: " + cat.getNombre() + "<br/>";
        }
        return dot;
    }

    public String paraJSON() {
        String salida = "";
        if (involucrado instanceof Libro) {
            Libro lib = (Libro) involucrado;
            if (tipo == Tipo.ELIMINAR_LIBRO) {
                salida += "\t\t\"ELIMINAR_LIBRO\":[\n\t\t\t{\n";
                salida += "\t\t\t\"ISBN\":" + lib.getISBN() + ",\n";
                salida += "\t\t\t\"TITULO\":\"" + lib.getTitulo() + "\",\n";
                salida += "\t\t\t\"CATEGORIA\":\"" + lib.getCategoria() + "\"\n";
                salida += "\t\t\t}\n\t\t]";
            } else {
                salida += "\t\t\"CREAR_LIBRO\":[\n\t\t\t{\n";
                salida += "\t\t\t\"ISBN\":" + lib.getISBN() + ",\n";
                salida += "\t\t\t\"AÑO\":" + lib.getAño() + ",\n";
                salida += "\t\t\t\"IDIOMA\":\"" + lib.getIdioma() + "\",\n";
                salida += "\t\t\t\"TITULO\":\"" + lib.getTitulo() + "\",\n";
                salida += "\t\t\t\"EDITORIAL\":\"" + lib.getEditorial() + "\",\n";
                salida += "\t\t\t\"AUTOR\":\"" + lib.getAutor() + "\",\n";
                salida += "\t\t\t\"EDICION\":" + lib.getEdicion() + ",\n";
                salida += "\t\t\t\"CATEGORIA\":\"" + lib.getCategoria() + "\"\n";
                salida += "\t\t\t}\n\t\t]";
            }
        } else if (involucrado instanceof Usuario) {
            Usuario user = (Usuario) involucrado;
            salida += "\t\t\"" + tipo.toString() + "\":[\n\t\t\t{\n";
            salida += "\t\t\t\"Carnet\":" + user.getCarnet() + ",\n";
            salida += "\t\t\t\"Nombre\":\"" + user.getNombre() + "\",\n";
            salida += "\t\t\t\"Apellido\":\"" + user.getApellido() + "\",\n";
            salida += "\t\t\t\"Carrera\":\"" + user.getCarrera() + "\",\n";
            salida += "\t\t\t\"Password\":\"" + user.getContraseña() + "\"\n";
            salida += "\t\t\t}\n\t\t]";
        } else {
            Categoria cat = (Categoria) involucrado;
            salida += "\t\t\"" + tipo.toString() + "\":[\n\t\\tt{\n";
            salida += "\t\t\t\"Nombre\":\"" + cat.getNombre() + "\"\n";
            salida += "\t\t\t}\n\t\t]";
        }
        return salida;
    }
}
