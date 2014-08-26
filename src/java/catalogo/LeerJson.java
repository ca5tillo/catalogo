/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catalogo;

import java.util.ArrayList;

/**
 *
 * @author lp-ub-14
 */
public class LeerJson {

    private String Titulo = "titulo de leer json", Sinopsis = "", Imagen = "",
            Fecha_de_publicacion = "", Duracion = "";
    private ArrayList<String> etiquetas_para_columna = new ArrayList<String>();
    private ArrayList<String> Autores = new ArrayList<String>();
    private ArrayList<String> links = new ArrayList<String>();

    public LeerJson(String path_de_catalogos) {
        Titulo=path_de_catalogos;
    }

    public String getTitulo() {
        return Titulo;
    }

    public String getSinopsis() {
        return Sinopsis;
    }

    public String getImagen() {
        return Imagen;
    }

    public String getFecha_de_publicacion() {
        return Fecha_de_publicacion;
    }

    public String getDuracion() {
        return Duracion;
    }

    public ArrayList<String> getEtiquetas_para_columna() {
        return etiquetas_para_columna;
    }

    public ArrayList<String> getAutores() {
        return Autores;
    }

    public ArrayList<String> getLinks() {
        return links;
    }

}
