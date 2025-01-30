<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormat formatterDecimal = new DecimalFormat("#,###,##0.00");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    formatterDecimal.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "" , tipou = "", username="";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        username= (String) sesion.getAttribute("Usuario");
        tipou = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    String Clave = "", Descripcion = "", Btn = "";
    
    Clave = request.getParameter("Clave");
    Descripcion = request.getParameter("Descripcion");
    Btn = request.getParameter("guardar");
    if(Clave == null){
        Clave = "";
    }
    if(Descripcion == null){
        Descripcion = "";
    }
    
    System.out.println("Clave: "+ Clave + " Desc: "+ Descripcion + " Btn :" + Btn );
    
    ConectionDB con = new ConectionDB();
    
    try{
        con.conectar();
        
        if (Btn.equals("guardar")){
            if(Clave !="" && Descripcion !=""){
                con.insertar("INSERT INTO tb_coninv VALUES ('"+Clave+"','"+Descripcion+"')");
                 System.out.println("Registro correcto");
            }else{
                System.out.println("Falta datos");
            }
        }
        
        con.cierraConexion();
    }catch(Exception e){
        System.out.println(e.getMessage());
    }
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="css/navbar-fixed-top.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.css">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <hr/>
            <div class="panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title">Conceptos Movimientos al Inventario</h3>
                </div>
                <% if(tipou.equals('1')) { %>
                <div class="panel-body ">
                    <form class="form-horizontal" role="form" name="formulario1" id="formulario1" method="post" action="ConMov.jsp">
                        <div class="row">
                            <div class="form-group">                                
                                <label for="Clave" class="col-xs-1 control-label">CLAVE</label>
                                <div class="col-xs-3">
                                    <input type="text" class="form-control" id="Clave" name="Clave" maxlength="60" placeholder="CLAVE" onKeyPress="return tabular(event, this)"  />
                                </div>                      
                                
                                <label for="Descripcion" class="col-xs-1 control-label">Descripción</label>
                                <div class="col-xs-3">
                                    <input type="text" class="form-control" id="Descripcion" maxlength="40" name="Descripcion" placeholder="Descripcion" onKeyPress="return tabular(event, this)"  />
                                </div>
                            </div>
                        </div>
                        
                        <br/>                     
                        
                        <button class="btn btn-block btn-primary" type="submit" name="guardar" value="guardar" onclick="return valida_alta();"> Guardar</button> 

                    </form>
                    <div>
                        <h6>Los campos marcados con * son obligatorios</h6>
                    </div>
                </div>
            <% } %>
                <div class="panel-footer">
                    <form method="post" action="ConMov.jsp">
                        <div style="width:100%; height:400px; overflow:auto;">
                        <table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered">
                            <thead>
                                <tr>
                                    <td>CLAVE</td>
                                    <td>Descripción</td>                                    
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    try {
                                        con.conectar();
                                        
                                        ResultSet rset = con.consulta("SELECT F_IdCon,F_DesCon FROM tb_coninv WHERE F_IdCon IN (11,62,3,19,18,2,10,16,1,23,4,6,5,9,30,1000,51,53,59,58,66,54,68,57,67,70,31,63) ORDER BY F_IdCon+0");
                                        while (rset.next()) {
                                            
                                %>
                                <tr>
                                    <td><small><%=rset.getString(1)%></small></td>
                                    <td><small><%=rset.getString(2)%></small></td>                                    
                                </tr>
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                %>
                            </tbody>
                        </table>
                        </div>
                        
                    </form>
                </div>
            </div>
        </div>
        <%@include file="jspf/piePagina.jspf" %>
        <!-- 
================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="js/jquery-1.9.1.js"></script>
        <script src="js/bootstrap.js"></script>
        <script src="js/jquery-ui-1.10.3.custom.js"></script>
        <script src="js/jquery.dataTables.js"></script>
        <script src="js/dataTables.bootstrap.js"></script>
        <script>
                            $(document).ready(function() {
                                $('#datosProv').dataTable();
                            });
        </script>
        <script>


            function isNumberKey(evt, obj)
            {
                var charCode = (evt.which) ? evt.which : event.keyCode;
                if (charCode === 13 || charCode > 31 && (charCode < 48 || charCode > 57)) {
                    if (charCode === 13) {
                        frm = obj.form;
                        for (i = 0; i < frm.elements.length; i++)
                            if (frm.elements[i] === obj)
                            {
                                if (i === frm.elements.length - 1)
                                    i = -1;
                                break
                            }
                        /*ACA ESTA EL CAMBIO*/
                        if (frm.elements[i + 1].disabled === true)
                            tabular(e, frm.elements[i + 1]);
                        else
                            frm.elements[i + 1].focus();
                        return false;
                    }
                    return false;
                }
                return true;

            }


            function valida_alta() {
                var missinginfo = "";
                if ($("#Clave").val() == "") {
                    missinginfo += "\n El campo Clave no debe de estar vacío";
                }
                if ($("#Descripcion").val() == "") {
                    missinginfo += "\n El campo Descripcion no debe de estar vacío";
                }
                
                if (missinginfo != "") {
                    missinginfo = "\n TE HA FALTADO INTRODUCIR LOS SIGUIENTES DATOS PARA ENVIAR PETICIÓN DE SOPORTE:\n" + missinginfo + "\n\n ¡INGRESA LOS DATOS FALTANTES Y TRATA OTRA VEZ!\n";
                    alert(missinginfo);

                    return false;
                } else {

                    return true;
                }

            }
        </script>
        <script language="javascript">
            function justNumbers(e)
            {
                var keynum = window.event ? window.event.keyCode : e.which;
                if ((keynum == 8) || (keynum == 46))
                    return true;

                return /\d/.test(String.fromCharCode(keynum));
            }
            otro = 0;
            function LP_data() {
                var key = window.event.keyCode;//codigo de tecla. 
                if (key < 48 || key > 57) {//si no es numero 
                    window.event.keyCode = 0;//anula la entrada de texto. 
                }
            }
            function anade(esto) {
                if (esto.value.length === 0) {
                    if (esto.value.length == 0) {
                        esto.value += "(";
                    }
                }
                if (esto.value.length > otro) {
                    if (esto.value.length == 4) {
                        esto.value += ") ";
                    }
                }
                if (esto.value.length > otro) {
                    if (esto.value.length == 9) {
                        esto.value += "-";
                    }
                }
                if (esto.value.length < otro) {
                    if (esto.value.length == 4 || esto.value.length == 9) {
                        esto.value = esto.value.substring(0, esto.value.length - 1);
                    }
                }
                otro = esto.value.length
            }


            function tabular(e, obj)
            {
                tecla = (document.all) ? e.keyCode : e.which;
                if (tecla != 13)
                    return;
                frm = obj.form;
                for (i = 0; i < frm.elements.length; i++)
                    if (frm.elements[i] == obj)
                    {
                        if (i == frm.elements.length - 1)
                            i = -1;
                        break
                    }
                /*ACA ESTA EL CAMBIO*/
                if (frm.elements[i + 1].disabled == true)
                    tabular(e, frm.elements[i + 1]);
                else
                    frm.elements[i + 1].focus();
                return false;
            }

        </script> 
    </body>
</html>



