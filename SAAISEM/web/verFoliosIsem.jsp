<%-- 
    Document   : capturaISEM.jsp
    Created on : 14-jul-2014, 14:48:02
    Author     : Americo
--%>

<%@page import="java.text.*"%>
<%@page import="conn.ConectionDB"%>
<%@page import="ISEM.CapturaPedidos"%>
<%@page import="javax.servlet.http.HttpSession"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%java.text.DateFormat df1 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "";
    if (sesion.getAttribute("Usuario") != null) {
        usua = (String) sesion.getAttribute("Usuario");
    } else {
        response.sendRedirect("indexCompras.jsp");
    }
    ConectionDB con = new ConectionDB();
    String NoCompra = "", Fecha = "";

    try {
        Fecha = request.getParameter("Fecha");
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
    if (Fecha == null) {
        Fecha = "";
    }
    try {
        NoCompra = request.getParameter("NoCompra");
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
    if (Fecha == null) {
        NoCompra = "";
    }

    String claPro = "", desPro = "";
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>FOLIOS</title>
        <!-- Estilos CSS -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <link href="css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" href="css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="css/navbar-fixed-top.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.css">
        <!---->
    </head>
    <body onload="focusLocus();">
        <div class="container">
            <div class="row">
                <h3>Ver Órdenes de Compra</h3>
                <a class="btn btn-default" href="capturaISEM.jsp">Captura de Órdenes de Compra</a>
                <a class="btn btn-default" href="verFoliosIsem.jsp">Ver Órdenes de Compra</a>
            </div>
            <br/>
            <div class="row">
                <form method="post" action="verFoliosIsem.jsp">
                    <div class="row">
                        <label class="col-sm-1">
                            <h4>Proveedor</h4>
                        </label>
                        <div class="col-sm-6">
                            <select class="form-control" name="Proveedor" id="Proveedor" onchange="SelectProve(this.form);
                                    document.getElementById('Fecha').focus()">
                                <option value="">--Proveedor--</option>
                                <%
                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("select F_ClaProve, F_NomPro from tb_proveedor p, tb_pedidoisem o where p.F_ClaProve = o.F_Provee group by o.F_Provee order by F_NomPro");
                                        while (rset.next()) {
                                %>
                                <option value="<%=rset.getString(1)%>" ><%=rset.getString(2)%></option>
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                    }
                                %>

                            </select>
                        </div>

                        <label class="col-sm-3">
                            <h4>Fecha de Entrega a MEDALFA:</h4>
                        </label>
                        <div class="col-sm-2">
                            <input type="text" class="form-control" data-date-format="dd/mm/yyyy" id="Fecha" name="Fecha" value="<%=Fecha%>" onchange="document.getElementById('Hora').focus()" />
                        </div>
                    </div>
                    <br/>
                    <div class="row">
                        <label class="col-sm-1">
                            <h4>Usuario:</h4>
                        </label>
                        <div class="col-sm-6">
                            <select class="form-control" name="Usuario" id="Usuario" onchange="">
                                <option value="">--Usuarios--</option>
                                <%
                                    try {
                                        con.conectar();
                                        ResultSet rset = con.consulta("select u.F_IdUsu, u.F_Usuario from tb_usuariosisem u, tb_pedidoisem p where F_Usuario !='root' and u.F_IdUsu = p.F_IdUsu group by F_IdUsu");
                                        while (rset.next()) {
                                %>
                                <option value="<%=rset.getString(1)%>" ><%=rset.getString(2)%></option>
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                    }
                                %>

                            </select>
                        </div>
                    </div>
                    <br/>
                    <div class="row">
                        <button class="btn btn-primary btn-block" name="accion" value="fecha">Buscar</button>
                    </div>
                </form>
            </div>
        </div>
        <br/>
        <div class="row" style="width: 90%; margin: auto;">
            <div class="col-sm-6">
                <form method="post">
                    <input value="<%=Fecha%>" name="Fecha" class="hidden"/>
                    <input value="<%=request.getParameter("Usuario")%>" name="Usuario"  class="hidden"/>
                    <input value="<%=request.getParameter("Proveedor")%>" name="Proveedor"  class="hidden"/>
                    <label class="col-sm-12">
                        <h4>Órdenes de Compra: </h4>
                    </label>
                    <table class="table table-bordered table-condensed table-striped" id="datosCompras">
                        <thead>
                            <tr>
                                <td>No. Orden</td>
                                <td>Capturó</td>
                                <td>Proveedor</td>
                                <td>Fecha Entrega</td>
                                <td>Hora entrega</td>
                                <td>Ver</td>
                            </tr>
                        </thead>
                        <tbody>
                            <%
                                String fecha = "";
                                try {
                                    fecha = df1.format(df2.parse(Fecha));
                                } catch (Exception e) {

                                }
                                try {
                                    con.conectar();
                                    ResultSet rset = con.consulta("select o.F_NoCompra, u.F_Usuario, p.F_NomPro,DATE_FORMAT(o.F_FecSur, '%d/%m/%Y') F_FecSur, F_HorSur, F_Fecha from tb_pedidoisem o, tb_proveedor p, tb_usuariosisem u where u.F_IdUsu = o.F_IdUsu and  o.F_Provee = p.F_ClaProve and o.F_FecSur like  '%" + fecha + "%' and o.F_IdUsu like '%" + request.getParameter("Usuario") + "' and o.F_Provee like '%" + request.getParameter("Proveedor") + "' group by o.F_NoCompra");
                                    while (rset.next()) {
                            %>
                            <tr>
                                <td><%=rset.getString(1)%></td>
                                <td><%=rset.getString(2)%></td>
                                <td><%=rset.getString(3)%></td>
                                <td><%=rset.getString(4)%></td>
                                <td><%=rset.getString(5)%></td>
                                <td>
                                    <button class="btn btn-primary text-center" name="NoCompra" value="<%=rset.getString(1)%>"><span class="glyphicon glyphicon-search"></span></button>
                                </td>
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
                </form>
            </div>
            <div class="col-sm-6">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                    </div>
                    <div class="panel-body">
                        <%                try {
                                con.conectar();
                                ResultSet rset = con.consulta("select o.F_NoCompra, p.F_NomPro, DATE_FORMAT(o.F_FecSur, '%d/%m/%Y'), F_HorSur, F_Usuario, F_StsPed from tb_pedidoisem o, tb_proveedor p, tb_usuariosisem u where u.F_IdUsu = o.F_IdUsu and  o.F_Provee = p.F_ClaProve and F_NoCompra = '" + NoCompra + "'  group by o.F_NoCompra");
                                while (rset.next()) {
                        %>
                        <div class="panel-heading">
                            Orden: <%=NoCompra%>
                        </div>
                        <div class="panel-body">
                            <form name="FormBusca" action="CapturaPedidos" method="post">
                                <div class="row">
                                    <h4 class="col-sm-3">Orden No. </h4>
                                    <div class="col-sm-3">
                                        <input class="form-control" value="<%=rset.getString(1)%>" name="NoCompra" id="NoCompra" readonly="" />
                                    </div>
                                    <h4 class="col-sm-3">Capturó: </h4>
                                    <div class="col-sm-3">
                                        <input class="form-control" value="<%=rset.getString("F_Usuario")%>" readonly="" />
                                    </div>

                                </div>
                                <%
                                    System.out.println("###" + rset.getString("F_StsPed"));
                                    if (rset.getString("F_StsPed").equals("2")) {
                                %>
                                <h4 class="text-danger">FOLIO CANCELADO</h4>
                                <%
                                    }
                                %>
                                <div class="row">
                                    <h4 class="col-sm-3">Proveedor: </h4>
                                    <div class="col-sm-9">
                                        <input class="form-control" value="<%=rset.getString(2)%>" readonly="" />
                                    </div>
                                </div>
                                <div class="row">
                                    <h4 class="col-sm-3">Fecha de Entrega: </h4>
                                    <div class="col-sm-3">
                                        <input class="form-control" value="<%=rset.getString(3)%>" readonly="" />
                                    </div>
                                    <h4 class="col-sm-3">Hora de Entrega: </h4>
                                    <div class="col-sm-3">
                                        <input class="form-control" value="<%=rset.getString(4)%>" readonly="" />
                                    </div>
                                </div>
                                <%
                                    if (!rset.getString("F_StsPed").equals("2")) {
                                %>
                                <br/>
                                <textarea class="form-control" name="Observaciones" id="Observaciones" placeholder="Observaciones para cancelar"></textarea>
                                <br>
                                <div class="row">
                                    <button class="btn btn-info btn-block" name="accion" value="cancelaOrden" onclick="return CancelaCompra();">CANCELAR ORDEN DE COMPRA</button>
                                </div>
                                <%
                                    }
                                    if (rset.getString("F_StsPed").equals("2")) {
                                        ResultSet rset2 = con.consulta("select F_Observaciones from tb_obscancela where F_NoCompra = '" + NoCompra + "' ");
                                        while (rset2.next()) {
                                %>
                                <br/>
                                <textarea class="form-control" name="Observa" id="Observa" readonly=""><%=rset2.getString(1)%></textarea>
                                <br>
                                <%
                                        }
                                    }
                                %>

                                <br/>
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {

                                    }
                                %>
                            </form>
                            <div class="row">
                                <br/>
                                <table class="table table-bordered table-condensed table-striped">
                                    <tr>
                                        <td><strong>Clave</strong></td>
                                        <td><strong>Descripción</strong></td>
                                        <!--td><strong>Lote</strong></td>
                                        <td><strong>Caducidad</strong></td-->
                                        <td><strong>Cantidad</strong></td>
                                    </tr>
                                    <%
                                        try {
                                            con.conectar();
                                            ResultSet rset = con.consulta("select s.F_Clave, m.F_DesPro, s.F_Lote, DATE_FORMAT(F_Cadu, '%d/%m/%Y'), s.F_Cant, F_IdIsem from tb_pedidoisem s, tb_medica m where s.F_Clave = m.F_ClaPro and F_NoCompra = '" + NoCompra + "' ");
                                            while (rset.next()) {
                                    %>
                                    <tr>
                                        <td><%=rset.getString(1)%></td>
                                        <td><%=rset.getString(2)%></td>
                                        <!--td><%=rset.getString(3)%></td>
                                        <td><%=rset.getString(4)%></td-->
                                        <td><%=formatter.format(rset.getInt(5))%></td>
                                    </tr>
                                    <%
                                            }
                                            con.cierraConexion();
                                        } catch (Exception e) {

                                        }
                                    %>

                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="js/jquery-1.9.1.js"></script>
    <script src="js/bootstrap.js"></script>
    <script src="js/jquery-ui-1.10.3.custom.js"></script>
    <script src="js/bootstrap-datepicker.js"></script>
    <script src="js/jquery.dataTables.js"></script>
    <script src="js/dataTables.bootstrap.js"></script>
    <script>
                                        $(document).ready(function() {
                                            $('#datosCompras').dataTable();
                                        });

                                        function CancelaCompra() {
                                            var confirma = confirm("¿Seguro que desea cancelar la orden? ");
                                            if (confirma === true) {
                                                var obser = document.getElementById('Observaciones').value;
                                                if (obser === "") {
                                                    alert('Favor de llenar el campo de observaciones');
                                                    document.getElementById('Observaciones').focus();
                                                    return false;
                                                } else {
                                                    return true;
                                                }
                                            } else {
                                                return false;
                                            }
                                        }
                                        function focusLocus() {
                                            document.getElementById('Proveedor').focus();
                                            if (document.getElementById('Fecha').value !== "") {
                                                document.getElementById('Clave').focus();
                                            }
                                            if (document.getElementById('ClaPro').value !== "") {
                                                document.getElementById('Prioridad').focus();
                                            }
                                        }

                                        $(function() {
                                            $("#Fecha").datepicker();
                                            $("#Fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});
                                        });
                                        $(function() {
                                            $("#CadPro").datepicker();
                                            $("#CadPro").datepicker('option', {dateFormat: 'dd/mm/yy'});
                                        });


                                        $(function() {
                                            var availableTags = [
        <%            try {
                con.conectar();
                ResultSet rset = con.consulta("SELECT F_DesPro  FROM tb_medica");
                while (rset.next()) {
                    out.println("\'" + rset.getString("F_DesPro") + "\',");
                }
                con.cierraConexion();
            } catch (Exception e) {
            }
        %>
                                            ];
                                            $("#Descripcion").autocomplete({
                                                source: availableTags
                                            });
                                        });

                                        function validaClaDes(boton) {
                                            var btn = boton.value;
                                            var prove = document.getElementById('Proveedor').value;
                                            var fecha = document.getElementById('Fecha').value;
                                            var hora = document.getElementById('Hora').value;
                                            var NoCompra = document.getElementById('NoCompra').value;
                                            if (prove === "" || fecha === "" || hora === "0:00" || NoCompra === "") {
                                                alert("Complete los datos");
                                                return false;
                                            }
                                            var valor = "";
                                            var mensaje = "";
                                            if (btn === "Clave") {
                                                valor = document.getElementById('Clave').value;
                                                mensaje = "Introduzca la clave";
                                            }
                                            if (btn === "Descripcion") {
                                                valor = document.getElementById('Descripcion').value;
                                                mensaje = "Introduzca la descripcion";
                                            }
                                            if (valor === "") {
                                                alert(mensaje);
                                                return false;
                                            }
                                            return true;
                                        }

                                        function validaCaptura() {
                                            var ClaPro = document.getElementById('ClaPro').value;
                                            var DesPro = document.getElementById('DesPro').value;
                                            var CanPro = document.getElementById('CanPro').value;
                                            if (ClaPro === "" |DesPro === "" || CanPro === "") {
                                                alert("Complete los datos");
                                                return false;
                                            }
                                            return true;
                                        }


    </script>
</html>
