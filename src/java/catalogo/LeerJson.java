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

    private ArrayList<Articulo> articulos = new ArrayList<>();
    private ArrayList<String> etiquetas_para_columna = new ArrayList<String>();
    private String seccion;

    public LeerJson() {
    }

    public void leer(String etiqueta, String path, String varmenu) {
        //"/home/lp-ub-14/NetBeansProjects/catalogo/contenido/v2b.json"
        etiquetas_para_columna.clear();
        articulos.clear();
        int i = 0;
        JSONParser parser = new JSONParser();

        try {
            Object base = parser.parse(new FileReader(path));
            JSONObject jsonBase = (JSONObject) base;

            JSONArray lst_etiquetas = (JSONArray) jsonBase.get("EtiquetasCatalogo");

            for (Object para_columna : lst_etiquetas) {
                etiquetas_para_columna.add((String) para_columna);
            }

            JSONArray lst_articulos = (JSONArray) jsonBase.get("Contenido");// lista de objetosJSON
            for (Object articulo : lst_articulos) {
                JSONObject Articulo = (JSONObject) articulo;// un objeto JSON

                Articulo objArt = new Articulo();

                //Etiquetas [x]
                JSONArray Etiquetas = (JSONArray) Articulo.get("Etiquetas");
                for (Object etiquetas : Etiquetas) {

                    String Etiqueta = (String) etiquetas;
                    if (Etiqueta.equalsIgnoreCase("nada")) {

                    } else if ((Etiqueta.equalsIgnoreCase(etiqueta)
                            || etiqueta.equalsIgnoreCase("todo")) && i == 0) {
                        i = 1;
                        //seccion 
                        objArt.setSeccion(varmenu);
                        seccion = varmenu;
                        //Titulo
                        objArt.setTitulo((String) Articulo.get("Titulo"));
                        // subtitulo
                        objArt.setSubtitulo((String) Articulo.get("Subtitulo"));
                        //Sinopsis
                        objArt.setSinopsis((String) Articulo.get("Sinopsis"));
                        // idioma
                        objArt.setIdioma((String) Articulo.get("Idioma"));
                        //Imagen
                        objArt.setImagen((String) Articulo.get("Imagen"));// direccion de la imagen 

                        //Formadores [x]
                        JSONArray Formadores = (JSONArray) Articulo.get("Autores");
                        ArrayList<String> autores = new ArrayList<>();
                        autores.clear();
                        for (Object formadores : Formadores) {
                            autores.add((String) formadores);
                        }
                        objArt.setAutores(autores);
                        //Fecha de publicacion
                        objArt.setFecha((String) Articulo.get("Fecha de publicacion"));

                        //Duracion
                        objArt.setDuracion((String) Articulo.get("Duracion"));

                        //Enlaces [x]
                        JSONArray Enlaces = (JSONArray) Articulo.get("Enlaces");
                        ArrayList<Links> links = new ArrayList<>();
                        links.clear();
                        for (Object enlaces : Enlaces) {
                            Links ln = new Links();
                            JSONObject link = (JSONObject) enlaces;// un objeto JSON
                            ln.setNombre((String) link.get("Nombre"));
                            ln.setPath((String) link.get("Path"));
                            links.add(ln);
                        }
                        objArt.setLinks(links);
//                        articulos += crearArticulo(Imagen, Titulo, Sinopsis,
//                                Autores, Fecha_de_publicacion, Duracion, links);
                        articulos.add(objArt);// El objecot articulo lo inserto en una lista
                    }//cierra if que verifica las etiquetas
                }// cierra el for que obtiene las etiquetas

                i = 0;

            }// cierra el for que obtienelos articulos

        } catch (ParseException e) {
            System.out.println("ERROR EN EL PARSER DE JSON");
        } catch (IOException e) {
            System.out.println("ERROS AL ABRIR EL ARCHIVO");
        }
    }

    private String autores(ArrayList<String> lstautores) {
        String autores = "";
        if (!lstautores.get(0).equalsIgnoreCase("")) {
            for (String autor : lstautores) {
                autores += autor + ", ";
            }
        }
        return autores;
    }

    private String links(ArrayList<Links> links) {
        String datos = "<br>";
        for (Links links1 : links) {
            datos += "<a href=\"" + links1.getPath() + "\" TARGET=\"Ventana-2\">" + links1.getNombre() + "</a><br>";
        }

        return datos;
    }

    public String crearContenido() {
        String articulo = "";

        if (seccion.equalsIgnoreCase("videos")) {
            articulo = contenidoVideos();
        } else if (seccion.equalsIgnoreCase("libros")) {
            articulo = contenidoLibros();
        } else if (seccion.equalsIgnoreCase("peliculas")){
            articulo = contenidoPeliculas();
        }else {
            articulo = contenidoVarios();
        }
        return articulo;
    }

    private String contenidoVarios() {
        String articulo = "";
        for (Articulo articulos1 : articulos) {
            String tmp_autores = autores(articulos1.getAutores());
            String autores = "";
            if (!tmp_autores.equalsIgnoreCase("")) {
                autores = "Autore: " + tmp_autores + "<br>";
            }
            String tmp_Fecha_de_publicación = articulos1.getFecha();
            String fecha_de_publicación = "";
            if (!tmp_Fecha_de_publicación.equalsIgnoreCase("")) {
                fecha_de_publicación = "Fecha de publicación: " + tmp_Fecha_de_publicación + "<br>";
            }
            String tmp_Duracion = articulos1.getDuracion();
            String duracion = "";
            if (!tmp_Duracion.equalsIgnoreCase("")) {
                duracion = "Duración: " + tmp_Duracion + "<br>";
            }
            String pathImg = articulos1.getImagen();
            String img = "";
            if (!pathImg.equalsIgnoreCase("")) {
                img = "<img class =\"grande\" src=\"" + pathImg + "\" />";
            }
            articulo += "<article>\n"
                    + "                    <table border=\"0\" cellspacing=\"15\" cellpadding=\"0\">\n"
                    + "                        <caption><h1>" + articulos1.getTitulo() + "</h1></caption>\n"
                    + "                        <tr>\n"
                    + "                           <td>\n"
                    + "                                <figure>                               \n"
                    + img
                    + "                                </figure>\n"
                    + "                           </td>\n"
                    + "                           <td> <p>\n"
                    + articulos1.getSinopsis()
                    + "                              </p>\n"
                    + "                              <br>\n"
                    + autores
                    + fecha_de_publicación
                    + duracion
                    + links(articulos1.getLinks())
                    + "                           </td>\n"
                    + "                        </tr>\n"
                    + "                    </table><br><br>\n"
                    + "                </article>";
        }
        return articulo;
    }

    private String contenidoVideos() {
        class Ln {

            private String links(ArrayList<Links> links) {
                String datos = "<br>";
                for (Links links1 : links) {

                    datos += "<h2><a href=\"" + links1.getPath() + "\"  target=\"videos\"> " + links1.getNombre() + " </a></h2>";
                }

                return datos;
            }
        }
        Ln ln = new Ln();
        String videos = "";
        for (Articulo articulos1 : articulos) {

            /*
             <a href="a.mp4"  target="videos">  </a>
             */
            videos += ln.links(articulos1.getLinks());

        }

        return videos;
    }

    private String contenidoLibros() {
        String libros = "<div id=\"portada\">\n"
                + "            <img src=\"iconos/portada.png\" >\n"
                + "         </div>\n"
                + "         <div id= \"tablaLibros\">\n"
                + "            <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\">";
        class Ln {

            private String links(ArrayList<Links> links) {
                String datos = "<br>";
                for (Links links1 : links) {

                    datos += "<tr>"
                            + "                  <td>"
                            + "                     <a href=\"\"><img src=\"iconos/libro.png\" width=\"42\" height=\"42\"></a>\n"
                            + "                  </td>"
                            + "                  <td>"
                            + "                     <a href=\"" + links1.getPath() + "\" TARGET=\"Ventana-2\"><h1>" + links1.getNombre() + "</h1></a>"
                            + "                  </td>"
                            + "               </tr>";
                }

                return datos;
            }
        }
        Ln ln = new Ln();

        for (Articulo articulos1 : articulos) {

            libros += ln.links(articulos1.getLinks());

        }
        libros += "</table></div>";
        return libros;
    }

    private String contenidoPeliculas() {
        String contenido = "";
        class Ln {

            private String links(ArrayList<Links> links) {
                String datos = "<br>";
                for (Links links1 : links) {

                    datos += "<div class=\"pelicula\" id=\"post-6828\">\n"
                            + "	<div class=\"poster marco-sinopsis\">\n"
                            + "		<a href=\"\">\n"
                            + "			<img src=\"http://2.bp.blogspot.com/-MRictagtHYs/VAUeutMbHHI/AAAAAAAAIyM/8xQZETE3wb4/s1600/Como-Entrenar-A-Tu-Dragon-2-DVDRip-Subtitulado-MEGA-FireDrive-Shared.png\" alt=\"Como Entrenar A Tu Dragon 2 [DVDRip][Subtitulada][FD-SD-MG]\" />\n"
                            + "		</a>	   		\n"
                            + "		<div class=\"categorias\">\n"
                            + "			<h2>\n"
                            + "				<a href=\"\">\n"
                            + "					Como Entrenar A Tu Dragon 2 <br>\n"
                            + "					Audion : litino <br>\n"
                            + "				</a>\n"
                            + "			</h2>\n"
                            + "		</div>\n"
                            + "	</div>\n"
                            + "</div>";
                }

                return datos;
            }
        }
        Ln ln = new Ln();

        for (Articulo articulos1 : articulos) {

            contenido
                    += "<div class=\"pelicula\" >"
                    + "	<div class=\"poster marco-sinopsis\">"
                    + "		<a href=\"\">"
                    + "			<img src=\"" + articulos1.getImagen() + "\" alt=\""+articulos1.getTitulo()+"\" />"
                    + "		</a>	   		"
                    + "		<div class=\"categorias\">"
                    + "			<h2>"
                    + "				<a href=\"\">"
                    + "					"+articulos1.getTitulo()+" <br>"
                    + "					Audion : "+articulos1.getIdioma()+" <br>"
                    + "				</a>"
                    + "			</h2>"
                    + "		</div>"
                    + "	</div>"
                    + "</div>";

        }
        return contenido;
    }

    public ArrayList<String> getEtiquetas_para_columna() {
        return etiquetas_para_columna;
    }

    public String obtenerSeccion() {
        return seccion;
    }

}
