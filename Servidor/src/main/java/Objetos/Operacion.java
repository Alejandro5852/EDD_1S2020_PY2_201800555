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
        ELIMINAR_CATEGORIA,
        ELIMINAR_USUARIO
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
        String dot = "\t\t" + getTipo() + "<br/>\n";
        return dot;
    }

    public String paraJSON() {
        String salida = "";
        if (involucrado instanceof Libro) {
            Libro lib = (Libro) involucrado;
            if (tipo == Tipo.ELIMINAR_LIBRO) {
                salida += "\t\t\t\"ELIMINAR_LIBRO\":[\n\t\t\t\t{\n";
                salida += "\t\t\t\t\"ISBN\":" + lib.getISBN() + ",\n";
                salida += "\t\t\t\t\"TITULO\":\"" + lib.getTitulo() + "\",\n";
                salida += "\t\t\t\t\"CATEGORIA\":\"" + lib.getCategoria() + "\"\n";
                salida += "\t\t\t\t}\n\t\t\t\t]";
            } else {
                salida += "\t\t\t\"CREAR_LIBRO\":[\n\t\t\t\t{\n";
                salida += "\t\t\t\t\"ISBN\":" + lib.getISBN() + ",\n";
                salida += "\t\t\t\t\"AÑO\":" + lib.getAño() + ",\n";
                salida += "\t\t\t\t\"IDIOMA\":\"" + lib.getIdioma() + "\",\n";
                salida += "\t\t\t\t\"TITULO\":\"" + lib.getTitulo() + "\",\n";
                salida += "\t\t\t\t\"EDITORIAL\":\"" + lib.getEditorial() + "\",\n";
                salida += "\t\t\t\t\"AUTOR\":\"" + lib.getAutor() + "\",\n";
                salida += "\t\t\t\t\"EDICION\":" + lib.getEdicion() + ",\n";
                salida += "\t\t\t\t\"CATEGORIA\":\"" + lib.getCategoria() + "\"\n";
                salida += "\t\t\t\t}\n\t\t\t\t]";
            }
        } else if (involucrado instanceof Usuario) {
            Usuario user = (Usuario) involucrado;
            salida += "\t\t\t\"" + tipo.toString() + "\":[\n\t\t\t\t{\n";
            salida += "\t\t\t\t\"Carnet\":" + user.getCarnet() + ",\n";
            salida += "\t\t\t\t\"Nombre\":\"" + user.getNombre() + "\",\n";
            salida += "\t\t\t\t\"Apellido\":\"" + user.getApellido() + "\",\n";
            salida += "\t\t\t\t\"Carrera\":\"" + user.getCarrera() + "\",\n";
            salida += "\t\t\t\t\"Password\":\"" + user.getContraseña() + "\"\n";
            salida += "\t\t\t\t}\n\t\t\t\t]";
        } else {
            Categoria cat = (Categoria) involucrado;
            salida += "\t\t\t\"" + tipo.toString() + "\":[\n\t\t\t\t{\n";
            salida += "\t\t\t\t\"Nombre\":\"" + cat.getNombre() + "\"\n";
            salida += "\t\t\t\t}\n\t\t\t\t\t]";
        }
        return salida;
    }

    public String paraEnviar() {
        String salida = "";
        if (involucrado instanceof Libro) {
            Libro lib = (Libro) involucrado;
            if (tipo == Tipo.ELIMINAR_LIBRO) {
                salida += "{\"TIPO\":\"ELIMINAR_LIBRO\",";
                salida += "\"ISBN\":" + lib.getISBN() + ",";
                salida += "\"TITULO\":\"" + lib.getTitulo() + "\",";
                salida += "\"CATEGORIA\":\"" + lib.getCategoria() + "\"";
                salida += "}";
            } else {
                salida += "{\"TIPO\":\"CREAR_LIBRO\",";
                salida += "\"ISBN\":" + lib.getISBN() + ",";
                salida += "\"YEAR\":" + lib.getAño() + ",";
                salida += "\"IDIOMA\":\"" + lib.getIdioma() + "\",";
                salida += "\"TITULO\":\"" + lib.getTitulo() + "\",";
                salida += "\"EDITORIAL\":\"" + lib.getEditorial() + "\",";
                salida += "\"AUTOR\":\"" + lib.getAutor() + "\",";
                salida += "\"EDICION\":" + lib.getEdicion() + ",";
                salida += "\"CATEGORIA\":\"" + lib.getCategoria() + "\"";
                salida += "}";
            }
        } else if (involucrado instanceof Usuario) {
            Usuario user = (Usuario) involucrado;
            salida += "{\"TIPO\":\"" + tipo.toString() + "\",";
            salida += "\"Carnet\":" + user.getCarnet() + ",";
            salida += "\"Nombre\":\"" + user.getNombre() + "\",";
            salida += "\"Apellido\":\"" + user.getApellido() + "\",";
            salida += "\"Carrera\":\"" + user.getCarrera() + "\",";
            salida += "\"Password\":\"" + user.getContraseña() + "\"";
            salida += "}";
        } else {
            Categoria cat = (Categoria) involucrado;
            salida += "{\"TIPO\":\"" + tipo.toString() + "\",";
            salida += "\"Nombre\":\"" + cat.getNombre() + "\"";
            salida += "}";
        }
        return salida;
    }
}
