/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package catalogo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author lp-ub-14
 */
public class catalogo extends HttpServlet {

    LeerJson aLeerJson = new LeerJson();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            String menu = request.getParameter("menu");
            String columna = request.getParameter("columna");
            String articulos = "nada";
            //para el path_de_catalogos
            String path_catalogos = getServletConfig().getServletContext().getRealPath("/");
            String path_de_catalogos = leerArchivo(path_catalogos + "/path_de_catalogos.txt");
            /*
            
             lista de menu ---> lista de los catalogos
            
             */

            /* TODO output your page here. You may use following sample code. */
            out.println(
                    "<!DOCTYPE html>\n"
                    + "<html lang=\"es\">\n"
                    + "    <head>\n"
                    + "        <meta charset=\"UTF-8\">\n"
                    + "        <meta name=\"description\" content=\"Ejemplo de HTML5\">\n"
                    + "        <meta name=\"keywords\" content=\"HTML5, CSS3, JavaScript\">\n"
                    + "        <title>Catálogo</title>\n"
                    + "        <link rel=\"stylesheet\" href=\"misestilos.css\">\n"
                    + "    </head>"
            );
            out.println("<body>");
            out.println("<div id=\"agrupar\">");
            out.println("<header id=\"cabecera\">\n"
                    + "                <h1>" + menu + "</h1>\n"
                    + "            </header>"
            );
            out.println(
                    "<nav id=\"menu\">\n"
                    + "                <ul>\n"
                    + "                     <li><a href=\"./catalogo?menu=principal\">principal</a></li>"
                    + getlstcatalogos_paraEl_Menu(path_de_catalogos)
                    + "                </ul>\n"
                    + "            </nav>"
            );
            out.println("<section id=\"seccion\">");
            // AQUI VAN LOS ARTICULOS

            out.println(articulo(menu, columna, path_de_catalogos));
            out.println("</section>");
            out.println("<aside id=\"columna\">");
            // AQUI VA LA COLUMNA

            out.println(columna(menu, path_de_catalogos));

            out.println("</aside>");
            out.println(
                    "<footer id=\"pie\">\n"
                    + "                Derechos Reservados &copy; \n"
                    + "            </footer>"
            );

            out.println("</div>");
            out.println("</body>");
            out.println("</html>");

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private boolean para_ignorar_archivos_temporales(String dato) {
        // si es un archivo temporal devolvera TRUE
        // temporal es alquel que termina con el simbolo ~
        boolean a = false;
        Pattern pat = Pattern.compile(".*~");
        Matcher mat = pat.matcher(dato);
        if (mat.matches()) {
            System.out.println("SI");
            a = true;
        } else {
            System.out.println("NO");
        }
        return a;
    }

    private String getlstcatalogos_paraEl_Menu(String path_de_catalogos) {
        //recibe la carpeta donde se encuentran los archivos JSON
        class Comprobar {

            private String quitarExtenciones(String nombre) {
                String dato = nombre;
                int i = 0;
                String[] tem = dato.split("\\."); // se parte la cadena en 2
                for (String tem1 : tem) {
                    if (i == 0) {
                        dato = tem1;
                    }
                    i = 1;
                }
                return dato;
            }

        }
        Comprobar comprobar = new Comprobar();
        String menu = "";

        ArrayList<String> Catalogos = getcatalogos(path_de_catalogos);

        if (verificar_path_de_catalogos(path_de_catalogos)) {
            for (String fichero : Catalogos) {
                String mmenu = comprobar.quitarExtenciones(fichero);
                menu += "<li><a href=\"./catalogo?menu=" + mmenu + "&columna=todo\">"
                        + mmenu + "</a></li>";

            }
        } else {
            menu = "NO se encontro la carpeta catalogos";
        }
        return menu;
    }

    private boolean verificar_path_de_catalogos(String path_de_catalogos) {
        boolean a = false;
        File f = new File(path_de_catalogos);
        if (f.exists()) {
            a = true;
        }
        return a;
    }

    private ArrayList<String> getcatalogos(String path_de_catalogos) {
        ArrayList<String> Catalogos = new ArrayList<>();
        Catalogos.clear();

        File f = new File(path_de_catalogos);
        if (f.exists()) {
            //System.out.println("Directorio existe ");
            File[] catalogos = f.listFiles();
            for (File fichero : catalogos) {
                if (!para_ignorar_archivos_temporales(fichero.getName())) {
                    String tem = fichero.getName();
                    Catalogos.add(tem);
                }
            }
        }
        return Catalogos;
    }

    private String columna(String menu, String path_de_catalogos) {
        String datos = "";
        if (verificar_path_de_catalogos(path_de_catalogos)) {

            String path = path_de_catalogos + "/" + menu + ".json";
            aLeerJson.leer("todo", path);
            ArrayList<String> Etiquetas_para_columna = aLeerJson.getEtiquetas_para_columna();
            for (String string : Etiquetas_para_columna) {

                String links = ""
                        + "<blockquote><a href=\"./catalogo?menu=" + menu + "&columna=" + string + "\">" + string + "</a></blockquote>\n";
                datos += links;
            }
        } else {
            datos = "directorio de catalogos no encontrado";
        }

        return datos;
    }

    private String articulo(String menu, String columna, String path_de_catalogos) {
        String datos = "";
        String etiqueta = columna;
        if (verificar_path_de_catalogos(path_de_catalogos)) {

            String path = path_de_catalogos + "/" + menu + ".json";
            aLeerJson.leer(etiqueta, path);
            datos = aLeerJson.getArticulo();
        } else {
            datos = "directorio de catalogos no encontrado";
        }

        return datos;
    }

    private String contenido(String menu, String columna) {

        String contenido = "";
        switch (menu) {
            case "v2b":
                switch (columna) {
                    case "todo":

                        break;
                    case "Programacion":

                        break;
                    case "Web":

                        break;
                }
                break;
            case "Drivers":
                //    out.println("ESTAMOS EN drivers");
                break;
            case "Sistemas Operativos":
                // out.println("ESTAMOS EN sistemas operativos");
                break;
        }
        return contenido;
    }

    private String leerJson(String etiqueta) {

        String contenido = "";
        String Titulo = "", Sinopsis = "", Imagen = "",
                Fecha_de_publicacion = "", Duracion = "";
        ArrayList<String> etiquetas_para_columna = new ArrayList<String>();
        ArrayList<String> Autores = new ArrayList<String>();
        ArrayList<String> links = new ArrayList<String>();
        Autores.clear();
        links.clear();
        JSONParser parser = new JSONParser();

        try {
            Object base = parser.parse(new FileReader("/home/lp-ub-14/NetBeansProjects/catalogo/contenido/v2b.json"));
            JSONObject jsonBase = (JSONObject) base;

            JSONArray lst_etiquetas = (JSONArray) jsonBase.get("Etiquetas");
            for (Object para_columna : lst_etiquetas) {
                etiquetas_para_columna.add((String) para_columna);
            }
            JSONArray lst_articulos = (JSONArray) jsonBase.get("v2b");// lista de objetosJSON
            for (Object articulo : lst_articulos) {
                JSONObject Articulo = (JSONObject) articulo;// un objeto
                //Etiquetas [x]
                JSONArray Etiquetas = (JSONArray) Articulo.get("Etiquetas");
                for (Object etiquetas : Etiquetas) {

                    String Etiqueta = (String) etiquetas;
                    if (Etiqueta.equalsIgnoreCase(etiqueta)) {

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
                        contenido += crearArticulo(Imagen, Titulo, Sinopsis,
                                Autores, Fecha_de_publicacion, Duracion, links);
                    }//cierra if que verifica las etiquetas
                }// cierra ek for que obtiene las etiquetas
            }// cierra el for que obtienelos articulos

        } catch (ParseException e) {
            System.out.println("ERROR EN EL PARSER DE JSON");
        } catch (IOException e) {
            System.out.println("ERROS AL ABRIR EL ARCHIVO");
        }

        return contenido;
    }

    private void eleccindeArchivoDatos(String DlstMenu) {

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

// Se usara para leer el archivo que contenga la direccion a los catalogos en formato Json
    public String leerArchivo(String path) {
//        String path = "/home/lp-ub-14/Documentos/arhpru/a.txt";
        String s = "";
        ArrayList<String> lineas = new ArrayList<>();
        File archivo = new File(path);
        final Formatter nuevoarchivo;
        //comprovar si existe el archivo
        if (archivo.exists()) {
            //Lectura 
            try (Scanner archivo_entrada = new Scanner(new File(path));) {
                while (archivo_entrada.hasNext()) {
                    lineas.add(archivo_entrada.nextLine());
                }
                archivo_entrada.close();
            } catch (FileNotFoundException e) {
                System.err.println("error de lectura" + e);
            }
            s = lineas.get(0);

        } else {
            try {//si no existe el archivo crearlo y escribir en el.
                // con este metodo de escritura se reescribe todo el archivo (se pierden todos los datos anteriores)
                nuevoarchivo = new Formatter(path);
                System.out.println("El archivo ha sido creado");
                nuevoarchivo.format("%s", "/home/lp-ub-14/Documentos/catagolo/catalogos/v2b.json");
                nuevoarchivo.close();
            } catch (FileNotFoundException e) {
                System.out.println("Error " + e);
            }
        }

        return s;
    }

}
