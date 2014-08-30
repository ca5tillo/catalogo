CATALOGO
========



##Referencias

### [MultiplesHostTomcat](http://www.adictosaltrabajo.com/tutoriales/tutoriales.php?pagina=MultiplesHostTomcat)
## [configurar-varios-sitios-con-xampp-en-windows](http://pablo.enlapc.com/2007/08/23/configurar-varios-sitios-con-xampp-en-windows/)
### [Abrir archivos del servidor desde un servlet](http://www.forosdelweb.com/f45/abrir-archivos-del-servidor-desde-servlet-446815/)

```bash
necesitas poner todo el path hasta tu archivo, ejemplo:

/home/usuario/proyecto/documentos/archivo.txt

pero, ponerlo explicitamente no es correcto, pues si el proyecto se mueve dejaria de funcionar verdad, ok, para eso necesitas averiguar el path de tu aplicacion, eso lo haces asi:

String docBase = getServletConfig().getServletContext().getRealPath ("/");

y ahora si haces esto:

FileOutputStream miFicheroSt;
miFicheroSt = new FileOutputStream( docBase+"proyecto/documentos/archivo.txt" );
```
### [como-leer-y-escribir-ficheros-json-en-java](http://blog.openalfa.com/como-leer-y-escribir-ficheros-json-en-java)

### [servlet-y-jsp-–-2-paso-de-parametros](http://franciscovaldivia.wordpress.com/2011/10/29/servlet-y-jsp-%E2%80%93-2-paso-de-parametros/)


```bash
**Envía de A.jsp a B.jsp (método GET)**
* En A.jsp:
      <a href=”B.jsp?ID=1&NOMBRE=Fran”> Ir a B </a>
* En B.jsp:
      id=request.getParameter(“ID”);
      nombre=request.getParameter(“NOMBRE”);


**Envía de A.jsp a Servlet.java:**
* En A.jsp:
      <a href=”Servlet?ID=1″>Ir a Servlet</a>
> o
      request.setAttribute(“nombre_del_parametro”,valorDelParametro);
      request.getRequestDispatcher(“Servlet”).forward(request,response);
* En Servlet:
      id=request.getParameter(“ID”);
> o
      p=request.getAttribute(“nombre_del_parametro”);


**Envía de Servlet.java a B.jsp:**
* En Servlet.java:
      request.setAttribute(“nombre_del_parametro”,valorDelParametro);
      request.getRequestDispacher(“/B.jsp”).forward(request,response);
* En B.jsp:
      p=request.getAttribute(“nombre_del_parametro”);
```

### [metodos-para-redireccionar-urls](http://www.cristalab.com/tutoriales/metodos-para-redireccionar-urls-html-php-y-javascript-c38527l/)

### [html](http://www.uv.es/jac/guia/link1.htm)

### [tag_iframe](http://www.w3schools.com/tags/tag_iframe.asp)
