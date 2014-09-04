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
            String header = "";
            String menu = request.getParameter("menu");
            String columna = request.getParameter("columna");
            String articulos = "nada";

            /*path_catalogos
             Me devuelve un String con la direccion de estes peoyecto. 
            
             Asi puedo localizar el archivo path_de_catalogos.txt
             que se localiza en la carpeta web de este proyecto (/web/path_de_catalogos.txt)
            
             path_de_catalogos.txt Contiene la direccion a la carpeta "catalogos"
             que contienen los catalogos en formato JSON
             */
            String path_proyecto = getServletConfig().getServletContext().getRealPath("/");
            /*
             lee el archivo que se encuentra en el servidor.
             En el archivo seencuentra una direccion local que indica la direccion 
             de la carpeta de mis catalogos JSON.
             */
            String path_de_catalogos = leerArchivo(path_proyecto + "/path_de_catalogos.txt");
            /*
            
             lista de menu ---> lista de los catalogos
            
             */
            header = menu(menu, columna, path_de_catalogos);

            /* TODO output your page here. You may use following sample code. */
            String seccionYcolumna = "";
            if (obteneSeccion().equalsIgnoreCase("videos")) {
                seccionYcolumna = "<link rel=\"stylesheet\" href=\"css/seccionYcolumna_videos.css\">";
            } else {
                seccionYcolumna = "<link rel=\"stylesheet\" href=\"css/seccionYcolumna_sencillo.css\">";
            }
            out.println(
                    "<!DOCTYPE html>"
                    + "<html lang=\"es\">"
                    + "    <head>"
                    + "        <meta charset=\"UTF-8\">"
                    + "        <meta name=\"description\" content=\"Ejemplo de HTML5\">"
                    + "        <meta name=\"keywords\" content=\"HTML5, CSS3, JavaScript\">"
                    + "        <title>Catálogo</title>"
                    + "        <link href='iconos/a.ico' rel='shortcut icon'/>"
                    + "        <link rel=\"stylesheet\" href=\"css/pag.css\">"
                    + seccionYcolumna
                    + "        <link rel=\"stylesheet\" href=\"css/posterpelicula.css\">"
                    + "        <link rel=\"stylesheet\" href=\"css/tablas.css\">"
                    + "    </head>"
            );
            out.println("<body>");
            // out.println(header);

            out.println(
                    "<header id=\"menu\">"
                    + "     <ul class=\"nav\">"
                    + "          <li><a href=\"\" style= \"padding: 5px\"><img src=\"iconos/a.ico\" width=\"37\" height=\"35\"></a></li>"
                    + header
                    + "      </ul>\n"
                    + "</header>"
            );
            out.println("<div id=\"principal\">");
            out.println("<section id=\"seccion\">");
            // AQUI VAN LOS ARTICULOS
            out.println("<h2>Seccion  -> " + obteneSeccion() + "</h2>");
            if (obteneSeccion().equalsIgnoreCase("videos")) {
                out.println("<iframe src=\"iconos/inicio.jpg\" name= \"videos\"  width=\"650px\" height=\"400px\" >\n"
                        + "                <p>Your browser does not support iframes.</p>\n"
                        + "                </iframe> ");
            } else {
                out.println(articulo());
            }

            out.println("</section>");
            out.println("<aside id=\"columna\">");
            // AQUI VA LA COLUMNA
            if (obteneSeccion().equalsIgnoreCase("videos")) {
                out.println(articulo());
            } else {
                
            out.println(columna(menu));
            }

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

            a = true;
        } else {

        }
        return a;
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

    private String menu(String varmenu, String varcolumna, String path_de_catalogos) {
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
        String submenu = "";

        if (verificar_path_de_catalogos(path_de_catalogos)) {

            ArrayList<String> Catalogos = getcatalogos(path_de_catalogos);
            for (String fichero : Catalogos) {
                String mmenu = comprobar.quitarExtenciones(fichero);

                String pathh = path_de_catalogos + "/" + mmenu + ".json";
                aLeerJson.leer("nada", pathh, mmenu);
                ArrayList<String> Etiquetas_para_columna = aLeerJson.getEtiquetas_para_columna();
                for (String string : Etiquetas_para_columna) {
                    submenu += "<li><a href=\"./catalogo?menu=" + mmenu + "&columna=" + string + "\">" + string + "</a></li>";
                }
                menu += "<li><h2><a href=\"./catalogo?menu=" + mmenu + "&columna=todo\">"
                        + mmenu + "</a></h2>"
                        + "    <ul>"
                        + submenu
                        + "    </ul>"
                        + "</li>";
                submenu = "";
            }
            String path = path_de_catalogos + "/" + varmenu + ".json";
            aLeerJson.leer(varcolumna, path, varmenu);
        } else {
            menu = "NO se encontro la carpeta catalogos";
        }

        return menu;
    }

    private String columna(String menu) {
        String datos = "";
        ArrayList<String> Etiquetas_para_columna = aLeerJson.getEtiquetas_para_columna();
        for (String string : Etiquetas_para_columna) {

            String links = ""
                    + "<blockquote><h2><a href=\"./catalogo?menu=" + menu + "&columna=" + string + "\">" + string + "</a></h2></blockquote>\n";
            datos += links;
        }
        return datos;
    }

    private String articulo() {
        String datos;
        datos = aLeerJson.crearContenido();
        return datos;
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

    public String obteneSeccion() {
        String a = "";
        a += aLeerJson.obtenerSeccion();
        return a;
    }

}