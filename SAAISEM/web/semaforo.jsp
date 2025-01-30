<%-- 
    Document   : exist
    Created on : 02-jul-2014, 23:24:11
    Author     : wence
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
    DecimalFormat formatter2 = new DecimalFormat("#,###,###.##");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "", Clave = "";
    int Cantidad = 0;
    Double monto = 0.0;
    String tipo = "", F_Ruta = "", F_FecEnt = "", Unidad1 = "", Unidad2 = "", Kardex = "";
    ResultSet rset = null;
    ResultSet rset2 = null;
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        Clave = (String) session.getAttribute("clave");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    Kardex = request.getParameter("kardex");
    if (Kardex == null){
        Kardex = "";
    }
    ConectionDB con = new ConectionDB();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Estilos CSS -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" href="css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="css/navbar-fixed-top.css" rel="stylesheet">
        <link href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap-glyphicons.css" rel="stylesheet">
        <link href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css" rel="stylesheet">
        <script src="//netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>
        <!---->
        <title>Ingresos en Almac&eacute;n</title>



    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <!--%@include file="jspf/menuPrincipal.jspf" %-->
        </div>
         <div class="container">
            <form action="semaforo.jsp" method="post">
                <div class="panel panel-danger">
                    <div class="panel-heading">
                        <h3 class="panel-title">Semaforización
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;<input type="checkbox" name="kardex" id="kardex" value="4" onchange="this.form.submit();" /> Caducados&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;<input type="checkbox" name="kardex" id="kardex" value="1" onchange="this.form.submit();" /> Menor a 6 Meses&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;<input type="checkbox" name="kardex" id="kardex" value="2" onchange="this.form.submit();" />Mayor a 6 Meses &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <%if((Kardex.equals("1")) || (Kardex.equals("2")) || (Kardex.equals("4"))){%><a href="Procesos/gnrSemaforo.jsp?Kardex=<%=Kardex%>">Descargar<label class="glyphicon glyphicon-download"></label></a><%}%>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <a href="semaforo.jsp">Actualizar<label class="glyphicon glyphicon-refresh"></label></a></h3>
                    </div>

                   <div class="panel-footer">
                        <table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered" id="datosProv">
                            <thead>
                                <tr>
                                    <td>Clave</td>
                                    <td>Descripción</td>
                                    <td>Lote</td>
                                    <td>Caducidad</td>
                                    <td>Cantidad</td>
                                    <td>Origen</td>
                                    <td>Proyecto</td>
                                    <td>Semaforización</td>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    try {
                                        con.conectar();

                                        if (Kardex.equals("1")) {
                                            rset = con.consulta("SELECT l.F_ClaPro, m.F_DesPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F_ExiLot), l.F_Origen, o.F_DesOri, l.F_Proyecto, p.F_DesProy FROM tb_lote l INNER JOIN tb_medica m ON m.F_ClaPro = l.F_ClaPro INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id INNER JOIN tb_origen o ON l.F_Origen = o.F_ClaOri WHERE F_ExiLot > 0 AND F_FecCad >= CURDATE() AND F_FecCad <= DATE_ADD(CURDATE(), INTERVAL 6 MONTH) AND l.F_Ubica not in ('CADUCADOS') GROUP BY l.F_ClaPro, l.F_ClaLot,l.F_FecCad, l.F_Origen,l.F_Proyecto  ORDER BY l.F_Proyecto, l.F_FecCad, l.F_ClaPro, l.F_ClaLot, l.F_Origen;");
                                        } else if (Kardex.equals("2")) {
                                            rset = con.consulta("SELECT l.F_ClaPro, m.F_DesPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F_ExiLot), l.F_Origen, o.F_DesOri, l.F_Proyecto, p.F_DesProy FROM tb_lote l INNER JOIN tb_medica m ON m.F_ClaPro = l.F_ClaPro INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id INNER JOIN tb_origen o ON l.F_Origen = o.F_ClaOri WHERE F_ExiLot > 0 AND F_FecCad > DATE_ADD(CURDATE(), INTERVAL 6 MONTH) AND l.F_Ubica NOT IN ('AT', 'A0T','AT2','AT3','AT4') GROUP BY  l.F_Proyecto, l.F_FecCad, l.F_ClaPro, l.F_ClaLot, l.F_Origen ORDER BY l.F_Proyecto, l.F_FecCad, l.F_ClaPro, l.F_ClaLot, l.F_Origen;");
                                        }  else if (Kardex.equals("4")) {
                                            rset = con.consulta("SELECT l.F_ClaPro, m.F_DesPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F_ExiLot), l.F_Origen, o.F_DesOri, l.F_Proyecto, p.F_DesProy FROM tb_lote l INNER JOIN tb_medica m ON m.F_ClaPro = l.F_ClaPro INNER JOIN tb_proyectos p ON l.F_Proyecto = p.F_Id INNER JOIN tb_origen o ON l.F_Origen = o.F_ClaOri WHERE F_ExiLot > 0 AND F_FecCad < CURDATE() AND l.F_Ubica NOT IN ('AT', 'A0T','AT2','AT3','AT4') GROUP BY l.F_Proyecto, l.F_FecCad, l.F_ClaPro, l.F_ClaLot, l.F_Origen ORDER BY l.F_Proyecto, l.F_FecCad, l.F_ClaPro, l.F_ClaLot, l.F_Origen;");                                        
                                        }
                                        while (rset.next()) {
                                %>
                                <tr>
                                    <td><%=rset.getString(1)%></td>
                                    <td><%=rset.getString(2)%></td>
                                    <td><%=rset.getString(3)%></td>
                                    <td><%=rset.getString(4)%></td>
                                    <td><%=rset.getString(5)%></td>
                                    <td><%=rset.getString(7)%></td>
                                    <td><%=rset.getString(9)%></td>
                                    
                                    <%if (Kardex.equals("1")) {%>

                                    <td><span class="label label-danger">Menor 6 Meses</span></td>

                                    <%} else if (Kardex.equals("2")) {%>

                                    <td><span class="label label-warning">Mayor a 6 Meses</span></td>

                                    <%} else if (Kardex.equals("4")) {%>

                                    <td><span class="label label-danger">Caducados</span></td>

                                    <%} else if (Kardex.equals("3")) {%>

                                    <td><span class="label label-success">Mayor a 12 Meses</span></td>
                                    <%} else {%>
                                    <td><span class="label label-success"> Mayor a 12 Meses</span></td>
                                    
                                    <%}%>


                                </tr>
                                <%
                                        }
                                        if (Kardex.equals("1")) {
                                            rset2 = con.consulta("SELECT SUM(F_ExiLot) as suma FROM tb_lote l WHERE F_FecCad > CURDATE() AND F_FecCad <= DATE_ADD(CURDATE(), INTERVAL 6 MONTH) ORDER BY l.F_Proyecto, l.F_FecCad, l.F_ClaPro, l.F_ClaLot;");
                                        } else if (Kardex.equals("2")) {
                                            rset2 = con.consulta("SELECT SUM(F_ExiLot) as suma FROM tb_lote l WHERE F_FecCad > DATE_ADD(CURDATE(), INTERVAL 6 MONTH) ORDER BY l.F_Proyecto, l.F_FecCad, l.F_ClaPro, l.F_ClaLot;");
                                        } else if (Kardex.equals("4")) {
                                            rset2 = con.consulta("SELECT SUM(F_ExiLot) as suma FROM tb_lote l WHERE F_FecCad < CURDATE() ORDER BY l.F_Proyecto, l.F_FecCad, l.F_ClaPro, l.F_ClaLot;");
                                        }
                                        while (rset2.next()) {
                                            /*double monto1 = 0;
                                            if (rset2.getFloat("F_ClaPro") < 9999.0) {
                                                monto1 = Double.parseDouble(rset2.getString("monto"));
                                            } else {
                                                monto1 = (Double.parseDouble(rset2.getString("monto")) * 1.16);
                                            }*/
                                            Cantidad = rset2.getInt(1);
                                            //monto = monto + monto1;
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                        System.out.println(e.getMessage());
                                    }
                                %>
                            </tbody>

                        </table><h3>Total Piezas = <%=formatter.format(Cantidad)%>&nbsp;&nbsp;&nbsp;<%//=formatter2.format(monto)%></h3>
                    </div>
                </div>
            </form>
        </div>
        <br><br><br>
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
        <script src="js/bootstrap-datepicker.js"></script>
        <script>
                                $(document).ready(function () {
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

            function valida_clave() {
                var missinginfo = "";
                if ($("#Nombre").val() == "") {
                    missinginfo += "\n El campo Clave de la Unidad no debe de estar vacío";
                }
                if (missinginfo != "") {
                    missinginfo = "\n TE HA FALTADO INTRODUCIR LOS SIGUIENTES DATOS PARA ENVIAR PETICIÓN DE SOPORTE:\n" + missinginfo + "\n\n ¡INGRESA LOS DATOS FALTANTES Y TRATA OTRA VEZ!\n";
                    alert(missinginfo);
                    return false;
                } else {
                    return true;
                }
            }

            function valida_alta() {
                var missinginfo = "";
                if ($("#Nombre").val() == "") {
                    missinginfo += "\n El campo Clave de la Unidad no debe de estar vacío";
                }
                if ($("#FecFab").val() == "") {
                    missinginfo += "\n El campo Fecha Entrega no debe de estar vacío";
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
                        /*ACA ESTA EL CAMBIO*/
                        if (frm.elements[i + 1].disabled == true)
                            tabular(e, frm.elements[i + 1]);
                        else
                            frm.elements[i + 1].focus();
                return false;
            }

            $(function () {
                $("#FecFab").datepicker();
                $("#FecFab").datepicker('option', {dateFormat: 'dd/mm/yy'});
            });
        </script> 



    </body>
</html>
