<%-- 
    Document   : index
    Created on : 01-oct-2013, 12:05:12
    Author     : wence
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession sesion = request.getSession();
    String info = null;

%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>COMPRAS MULTIPROYECTOS</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <!-- Bootstrap -->
        <link href="css/bootstrap.css" rel="stylesheet" media="screen">
        <link href="css/login.css" rel="stylesheet" media="screen">
       <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
        <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
        <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    </head>
    <body>
        <div class="wrapper fadeInDown">
            <div id="formContent">              
                <!-- Icon -->
                <div class="fadeIn first">
                    
                    <h3>COMPRAS MULTIPROYECTOS</h3>
                </div>
                <hr/>
                <!-- Login Form -->
                <form name ="form" id="forma-login" class="marco" action="loginISEM" method="post" >
                    <input type="text" name="nombre" id="nombre" class="fadeIn second" autofocus="" placeholder="Introduzca Nombre de Usuario"/>  

                    <input type="password" name="pass" id="pass" class="fadeIn third"  placeholder="Introduzca Contrase&ntilde;a V&aacute;lida"/>


                    <input type="submit" class="fadeIn fourth" value="Entrar" name="envio">       

                    <div id="formFooter">
                        <%         info = (String) session.getAttribute("mensaje");
                            //out.print(info);
                            if (!(info == null || info.equals(null))) {
                        %>
                        <div><%=info%></div>
                        <%
                            }
                            session.invalidate();
                        %>
                    </div>
                </form>     
            </div>
        </div>
    </body>
</html>


