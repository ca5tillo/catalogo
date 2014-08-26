/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catalogo;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author lp-ub-14
 */
public class LeerJson {

    private String Titulo = "", Sinopsis = "", Imagen = "",
            Fecha_de_publicacion = "", Duracion = "";
    private ArrayList<String> etiquetas_para_columna = new ArrayList<String>();
    private ArrayList<String> Autores = new ArrayList<String>();
    private ArrayList<String> links = new ArrayList<String>();
    private String articulos = "";

    public LeerJson() {
    }

    public void limpiarVariables_dentro_articulo() {
        Imagen = "";
        Titulo = "";
        Sinopsis = "";
        Autores.clear();
        Fecha_de_publicacion = "";
        Duracion = "";
        links.clear();
    }

    public void limpiarVariable_articulos() {
        articulos = "";
    }

    public void limpiarVariable_columna() {
        etiquetas_para_columna.clear();

    }

    public void leer(String etiqueta, String direccion) {
        String path = direccion;
        //"/home/lp-ub-14/NetBeansProjects/catalogo/contenido/v2b.json"
        limpiarVariable_articulos();
        limpiarVariables_dentro_articulo();
        limpiarVariable_columna();
        int i = 0;
        JSONParser parser = new JSONParser();

        try {
            Object base = parser.parse(new FileReader(path));
            JSONObject jsonBase = (JSONObject) base;

            JSONArray lst_etiquetas = (JSONArray) jsonBase.get("Etiquetas");
            for (Object para_columna : lst_etiquetas) {
                etiquetas_para_columna.add((String) para_columna);
            }
            JSONArray lst_articulos = (JSONArray) jsonBase.get("v2b");// lista de objetosJSON
            for (Object articulo : lst_articulos) {
                limpiarVariables_dentro_articulo();
                JSONObject Articulo = (JSONObject) articulo;// un objeto
                //Etiquetas [x]
                JSONArray Etiquetas = (JSONArray) Articulo.get("Etiquetas");
                for (Object etiquetas : Etiquetas) {

                    String Etiqueta = (String) etiquetas;
                    if ((Etiqueta.equalsIgnoreCase(etiqueta)
                            || etiqueta.equalsIgnoreCase("todo")) && i == 0) {
                        i = 1;
                        //Titulo
                        Titulo = (String) Articulo.get("Titulo");

                        //Sinopsis
                        Sinopsis = (String) Articulo.get("Sinopsis");

                        //Imagen
                        Imagen = (String) Articulo.get("Imagen");

                        //Formadores [x]
                        JSONArray Formadores = (JSONArray) Articulo.get("Formadores");
                        for (Object formadores : Formadores) {

                            Autores.add((String) formadores);
                        }
                        //Fecha de publicacion
                        Fecha_de_publicacion = (String) Articulo.get("Fecha de publicacion");

                        //Duracion
                        Duracion = (String) Articulo.get("Duracion");

                        //Enlaces [x]
                        JSONArray Enlaces = (JSONArray) Articulo.get("Enlaces");
                        for (Object enlaces : Enlaces) {

                            links.add((String) enlaces);
                        }
                        articulos += crearArticulo(Imagen, Titulo, Sinopsis,
                                Autores, Fecha_de_publicacion, Duracion, links);
                    }//cierra if que verifica las etiquetas
                }// cierra ek for que obtiene las etiquetas
                i = 0;
            }// cierra el for que obtienelos articulos

        } catch (ParseException e) {
            System.out.println("ERROR EN EL PARSER DE JSON");
        } catch (IOException e) {
            System.out.println("ERROS AL ABRIR EL ARCHIVO");
        }
    }

    private String crearArticulo(
            String Imagen, String Titulo, String Sinopsis,
            ArrayList<String> autores, String Fecha_de_publicacion, String Duracion,
            ArrayList<String> links) {
        class secciones {

            private String autores(ArrayList<String> lstautores) {
                String autores = "";
                for (String autor : lstautores) {
                    autores += autor + ", ";
                }
                return autores;
            }
        }
        secciones sc = new secciones();
        String articulo = "";
        articulo = "<article>\n"
                + "                    <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n"
                + "                        <caption><h1>" + Titulo + "</h1></caption>\n"
                + "                        <tr>\n"
                + "                           <td>\n"
                + "                                <figure>                               \n"
                + "                                    <img class =\"grande\" src=\"" + Imagen + "\" />\n"
                + "                                </figure>\n"
                + "                           </td>\n"
                + "                           <td> <p>\n"
                + Sinopsis
                + "                              </p>\n"
                + "                              <br>\n"
                + "                              Formadores: " + sc.autores(autores) + "<br>\n"
                + "                              Fecha de publicación: " + Fecha_de_publicacion + "<br>\n"
                + "                              Duración: " + Duracion + "<br><br>\n"
                + "                              \n"
                + "                              <a href=\"\">Carpeta</a><br>\n"
                + "                              <a href=\"\">rar</a>\n"
                + "                           </td>\n"
                + "                        </tr>\n"
                + "                    </table>\n"
                + "                </article>";

        return articulo;
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

    public String getArticulo() {

        return articulos;
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
