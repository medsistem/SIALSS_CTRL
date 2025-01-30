<%-- 
    Document   : genMarbete
    Created on : 11/05/2016, 12:47:24 PM
    Author     : juan
--%>

<%@page import="java.sql.ResultSet"%>
<%@page import="conn.ConectionDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HttpSession sesion = request.getSession();
    String usua = "";
    String tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("../index.jsp");
    }
    ConectionDB con = new ConectionDB();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Marbetes</title>
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <script src="../js/jquery-1.9.1.js" type="text/javascript"></script>
        <script src="../js/bootstrap.js"></script>
        <script src="../js/jquery-ui.js"></script>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
        <script src="../js/marbetesGeneracion.js"></script>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <%@include file="../jspf/menuPrincipal.jspf" %>  
           
            <div class=" panel panel-primary" >
                <div class="panel-heading">
                    <h4>Marbete para Jurisdiccion</h4>
                </div> 
                <div class="panel-body">
                    <div class="row" >
                        <h4 class="col-lg-1 col-md-1 col-sm-1 text-primary" style="font-weight: bold;" >Folios:</h4>
                        <div class="col-lg-2 col-md-2 col-sm-2" >
                            <input class="form-control" placeholder="Folio Referencia" type="number" id="folNumber0" min="1">
                        </div>
                        <!--div class="col-lg-1 col-md-1 col-sm-1" >
                            <input class="form-control" type="number" id="folNumber1" >
                        </div-->

                        <h5 class="col-sm-2 text-primary" style="font-weight: bold;">Seleccione Proyecto</h5>

                        <div class="col-sm-3">

                            <select id="Nombre" name="Nombre" class="form-control">
                                <option value="0">--Proyectos--</option>
                                <%
                                    try {
                                        con.conectar();
                                        try {
                                            ResultSet RsetProy = con.consulta("SELECT * FROM tb_proyectos;");
                                            while (RsetProy.next()) {
                                %>
                                <option value="<%=RsetProy.getString(1)%>"><%=RsetProy.getString(2)%></option>
                                <%
                                            }
                                        } catch (Exception e) {
                                            e.getMessage();
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                    }
                                %>
                            </select>
                        </div>
                         
                    </div>  
                    <hr/>
                    <div class="row">
                        <h4 class="col-lg-3 col-md-3 col-sm-3  text-primary" style="font-weight: bold;" >Seleccionar unidad:</h4>
                        <div class="col-sm-4">
                            <select id="uniName" name="uniName" class="form-control">
                                <option value="0">--Unidades--</option>
                                <%
                                    try {
                                        con.conectar();
                                        try {
                                            ResultSet RsetUnidd = con.consulta("SELECT u.F_ClaCli,u.F_NomCli FROM tb_uniatn AS u WHERE u.F_Tipo = '14' GROUP BY u.F_ClaCli;");
                                            while (RsetUnidd.next()) {
                                %>
                                <option value="<%=RsetUnidd.getString(1)%>"><%=RsetUnidd.getString(2)%></option>
                                <%
                                            }
                                        } catch (Exception e) {
                                            e.getMessage();
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                    }
                                %>
                            </select>
                            
                        </div>
                    </div>
                    <hr/>
                    <div class="row" >
                        <h4 class="col-lg-3 col-md-3 col-sm-3  text-primary" style="font-weight: bold;" >Número de marbetes:</h4>
                        <div class="col-lg-2 col-md-2 col-sm-2" >
                            <input class="form-control" placeholder="Cantidad de marbetes" type="number" id="marbetNumber" min="1" oninput="javascript: if (this.value.length > this.maxLength) this.value = this.value.slice(0, this.maxLength);" maxlength="3">
                        </div>
                        <h4 class="col-lg-3 col-md-3 col-sm-3  text-success" style="font-weight: bold; text-align: right" >Ruta:</h4>
                        <div class="col-lg-2 col-md-2 col-sm-2" >
                            <input class="form-control" type="text" id="ruta" maxlength="10">
                        </div>
                    </div>   
                    <hr/>
                    <div class="row" >
                        <div class="col-lg-4 col-md-4 col-sm-4" >

                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-4" >
                            <button class="btn btn-block btn-info" type="button" id="generarMarbete0" >Generar Marbetes</button>
                        </div>
                        <div class="col-lg-4 col-md-4 col-sm-4" >
                        </div>
                    </div>
                </div>
            </div>
        </div>  
    </body>
    
    <script type="text/javascript">
                // Initialize our function when the document is ready for events.
                jQuery(document).ready(function(){
                        // Listen for the input event.
                        jQuery("#folNumber0").on('input', function (evt) {
                                // Allow only numbers.
                                jQuery(this).val(jQuery(this).val().replace(/[^0-9]/g, ''));
                        });
                        
                        jQuery("#marbetNumber").on('input', function (evt) {
                                // Allow only numbers.
                                jQuery(this).val(jQuery(this).val().replace(/[^0-9]/g, ''));
                        });
                });
    </script>
</html>
