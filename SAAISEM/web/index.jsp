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
        <title>SIALSS_CTRL</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Bootstrap -->
        <link href="css/bootstrap.css" rel="stylesheet" media="screen">
        <link href="WEB-INF/resources/scss/custom.scss">
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
                    
                    <h2>SIALSS CONTROLADOS</h2>
                </div>
                <!-- Login Form -->
                <form name ="form" id="forma-login" class="marco" action="login" method="post" >
                 
                    <input type="text" name="nombre" id="nombre" class="fadeIn second glyphicon glyphicon-user" autofocus="" placeholder="Usuario"/>  
                    <input type="password" name="pass" id="pass" class="fadeIn third"  placeholder="Contrase&ntilde;a V&aacute;lida"/>

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
                        <div ALIGN="right"> <h5>Versión 1.0.2.2 </h5>
                    </div>
                </form>     
            </div>
        </div> 
        
        
       
    </body>
</html>

