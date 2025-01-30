<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    DecimalFormat formatter2 = new DecimalFormat("000");
    DecimalFormatSymbols custom = new DecimalFormatSymbols();
    custom.setDecimalSeparator('.');
    custom.setGroupingSeparator(',');
    formatter.setDecimalFormatSymbols(custom);
    HttpSession sesion = request.getSession();
    String usua = "", Clave = "";
    String tipo = "", F_Ruta = "", F_FecEnt = "", Unidad1 = "", Unidad2 = "", Catalogo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        Clave = (String) session.getAttribute("clave");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    if (Clave == null) {
        Clave = "";
    }

    ConectionDB con = new ConectionDB();
    ConectionDB_Inventarios conInv = new ConectionDB_Inventarios();

    String IdReq = "", ClaveU = "", NombreU = "", Fecha = "", DFecha = "", IdInv = "", CantInv = "";
    int Sol = 0, Inv = 0, TSol = 0, TInv = 0;
    ResultSet Consulta = null;
    ResultSet ConsultaInv = null;
    try {
        IdReq = request.getParameter("F_ClaDoc");

    } catch (Exception e) {
    }
    try {
        con.conectar();
        conInv.conectar();
        Consulta = con.consulta("SELECT U.F_ClaCli,U.F_NomCli,R.F_Fecha FROM tb_requerimientoprocesar R INNER JOIN tb_uniatn U ON R.F_ClaCli=U.F_ClaCli WHERE F_IdReq='" + IdReq + "' GROUP BY R.F_ClaCli;");
        while (Consulta.next()) {
            ClaveU = Consulta.getString(1);
            NombreU = Consulta.getString(2);
            DFecha = Consulta.getString(3);
        }

    } catch (Exception e) {
        Logger.getLogger("reimpresion.jsp").log(Level.SEVERE, null, e);
    } finally {
        try {
            con.cierraConexion();
            conInv.cierraConexion();
        } catch (Exception ex) {
            Logger.getLogger("reimpresion.jsp").log(Level.SEVERE, null, ex);
        }
    }


%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link href="../css/datepicker3.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>            
        </div>
        <div class="container">
            <div class="panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Procesar Requerimiento</h3>
                </div>

                <div class="panel-footer">
                    <form action="../ProcesaRequerimiento" method="post" onsubmit="muestraImagen()">

                        <div class="panel panel-default">
                            <div class="panel-heading">
                                N°. Req:&nbsp;<label><%=IdReq%></label> | Clave Uni:&nbsp;<label><%=ClaveU%></label> | Nombre:&nbsp;<label><%=NombreU%></label> | Fecha Captura: <label><%=DFecha%></label>
                            </div>
                            <div id="<%//=rset.getString(1)%>" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
                                <div class="panel-body">
                                    <div class="row">


                                        <div class="col-sm-2">
                                        </div>

                                    </div>
                                    <table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-condensed" id="datosProv">
                                        <tr>
                                            <td class="text-center">Req</td>
                                            <td class="text-center">Id</td>
                                            <td class="text-center">Clave</td>
                                            <td class="text-center">Descripción</td>
                                            <td class="text-center">Piezas Sol</td>
                                            <!--td class="text-center">Piezas Inv</td-->
                                            <td class="text-center">Modificar</td>
                                        </tr>
                                        <%
                                            try {
                                                con.conectar();
                                                System.out.println("Entro sin Datos");
                                                Consulta = con.consulta("SELECT R.F_ClaPro,M.F_DesPro,R.F_Cant,R.F_Id,R.F_CantInv FROM tb_requerimientoprocesar R INNER JOIN tb_medica M ON R.F_ClaPro=M.F_ClaPro WHERE F_IdReq='" + IdReq + "' AND F_Cant>0 AND M.F_StsPro='A';");
                                                while (Consulta.next()) {
                                                    Sol = Consulta.getInt(3);
                                                    Inv = Consulta.getInt(5);
                                                    TSol = TSol + Sol;
                                                    TInv = TInv + Inv;

                                        %>
                                        <tr>
                                            <td class="req text-center"><%=IdReq%></td>
                                            <td class="id text-center"><%=Consulta.getString(4)%></td>
                                            <td class="clave"><%=Consulta.getString(1)%></td>
                                            <td class="descrip"><%=Consulta.getString(2)%></td>
                                            <td class="cantidad text-center"><%=Consulta.getString(3)%></td>
                                            <!--td class="text-center"><%//=Consulta.getString(5)%></td-->
                                            <td class="text-center">
                                                <button class="btn btn-info rowButton" data-toggle="modal" data-target="#ModiOc"><span class="glyphicon glyphicon-pencil" ></span></button>                                        
                                            </td>
                                        </tr>                 

                                        <%

                                                }
                                            } catch (Exception e) {
                                                Logger.getLogger("reimpresion.jsp").log(Level.SEVERE, null, e);
                                            } finally {
                                                try {
                                                    con.cierraConexion();
                                                    conInv.cierraConexion();
                                                } catch (Exception ex) {
                                                    Logger.getLogger("reimpresion.jsp").log(Level.SEVERE, null, ex);
                                                }
                                            }
                                        %>

                                    </table> 

                                    <div class="row">

                                        <div class="col-sm-4">
                                            <h4>Piezas Sol: <%=formatter.format(TSol)%></h4>
                                        </div>
                                        <!--div class="col-sm-4">
                                            <h4>Piezas Inv: <%=formatter.format(TInv)%></h4>
                                        </div-->
                                        <div class="col-sm-2">
                                            <label><h4>Fecha Entrega:</h4></label>
                                        </div>
                                            <div class="col-sm-2">
                                            <input class="form-control" id="fecha_fin" name="fecha_fin" type="date"/>
                                            </div>
                                    </div>
                                </div>
                            </div>
                        </div>



                        <br/>

                        <div class="row">
                            <div class="col-sm-6">
                                <button class="btn btn-block btn-info" onclick="return CancelaRequerimiento()">Salir</button> 
                            </div>
                            <div class="col-sm-6">
                                <input type="hidden" name="reque" id="reque" value="<%=IdReq%>"/>
                                <input type="hidden" name="Unidad" id="Unidad" value="<%=ClaveU%>"/>
                                <button class="btn btn-block btn-primary" type="submit" name="accion" value="procesareque" onclick="return validaRemision()">Procesar Requerimiento</button> 
                            </div>
                        </div>


                    </form>
                </div>
            </div>
        </div>

        <!-- Modal -->
        <div id="ModiOc" class="modal fade" role="dialog">
            <div class="modal-dialog">
                <!-- Modal content-->
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <h4 class="modal-title">Modificar Piezas Requerimientos</h4>
                    </div>                    
                    <form name="formEditOC" action="../ProcesaRequerimiento" method="Post">
                        <div class="modal-body">
                            <div class="row">         
                                <h4 class="col-sm-2">Req:</h4>
                                <div class="col-sm-4">
                                    <input class="form-control" name="Req" id="Req" type="text" value="" readonly required/>
                                </div>
                                <h4 class="col-sm-2">Id:</h4>
                                <div class="col-sm-4">
                                    <input class="form-control" name="idreg" id="Id" type="text" value="" readonly="" required=""/>
                                </div>
                            </div>                            
                            <div class="row">                                
                                <h4 class="col-sm-2">Clave:</h4>
                                <div class="col-sm-4">
                                    <input class="form-control" name="Clave" id="Clave" type="text" value="" readonly="" required=""/>
                                </div>
                                <h4 class="col-sm-2">Descripción</h4>
                                <div class="col-sm-4">
                                    <input class="form-control" name="Descrip" id="Descrip" type="text" value="" readonly="" required=""/>
                                </div>
                            </div>
                            <div class="row">
                                <h4 class="col-sm-2">Cant. Sol:</h4>
                                <div class="col-sm-4">
                                    <input class="form-control" name="Cantidad" id="Cantidad" type="text" value="" readonly="" required=""/>
                                </div>
                                <h4 class="col-sm-2">Cant. Sur:</h4>
                                <div class="col-sm-4">
                                    <input class="form-control" name="CantV" id="CantV" type="text" value="" required="" data-behavior="only-num"/>
                                </div>                                
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button type="submit" class="btn btn-default" name="accion" value="Actualizar">Actualizar</button>
                            <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Fin Modal -->


        <br><br><br>
        <div class="navbar navbar-fixed-bottom navbar-inverse">
            <div class="text-center text-muted">
                Desarrollo de Aplicaciones 2009 - 2018 <span class="glyphicon glyphicon-registration-mark"></span><br />
                Todos los Derechos Reservados
            </div>
        </div>

        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel"></h4>
                    </div>
                    <div class="modal-body">
                        <div class="text-center" id="imagenCarga">
                            <img src="../imagenes/ajax-loader-1.gif" alt="" />
                        </div>
                    </div>
                    <div class="modal-footer">
                    </div>
                </div>
            </div>
        </div>
        <!-- 
        ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="../js/jquery-1.9.1.js"></script>
        <script src="../js/bootstrap.js"></script>
        <script src="../js/jquery-ui-1.10.3.custom.js"></script>
        <script src="../js/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
        <script src="../js/bootstrap-datepicker.js"></script>
        <script src="../js/jquery.alphanum.js" type="text/javascript"></script>
        <script>
                                    $(document).ready(function() {
                                        $('#datosProv').dataTable();
                                    });
                                    function validaRemision() {
                                        var confirmacion = confirm('Seguro que desea procesar el requerimiento');
                                        if (confirmacion === true) {
                                            $('#btnGeneraFolio').prop('disabled', true);
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }

                                    function CancelaRequerimiento() {
                                        var confirmacion = confirm('Seguro que desea salir');
                                        if (confirmacion === true) {
                                            var ventana = window.self;
                                            ventana.opener = window.self;
                                            setTimeout("window.close()", 0);
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }


        </script>
        <script type="text/javascript">
            function muestraImagen() {
                $('#myModal').modal();
            }


            $("[data-behavior~=only-alphanum]").alphanum({
                allowSpace: false,
                allowOtherCharSets: false,
                allow: '.'
            });
            $("[data-behavior~=only-alphanum-caps]").alphanum({
                allowSpace: false,
                allowOtherCharSets: false,
                forceUpper: true
            });
            $("[data-behavior~=only-alphanum-caps-15]").alphanum({
                allowSpace: false,
                allowOtherCharSets: false,
                forceUpper: true,
                maxLength: 15
            });
            $("[data-behavior~=only-alphanum-white]").alphanum({
                allow: '.',
                disallow: "'",
                allowSpace: true
            });
            $("[data-behavior~=only-num]").numeric({
                allowMinus: false,
                allowThouSep: false
            });

            $("[data-behavior~=only-alpha]").alphanum({
                allowNumeric: false,
                allowSpace: false,
                allowOtherCharSets: true
            });


        </script> 
        <script>
            $(".rowButton").click(function() {
                var $row = $(this).closest("tr");    // Find the row
                var $descrip = $row.find("td.descrip").text(); // Find the text   
                var $id = $row.find("td.id").text(); // Find the text   
                var $cantidad = $row.find("td.cantidad").text(); // Find the text 
                var $clave = $row.find("td.clave").text(); // Find the text 
                var $req = $row.find("td.req").text(); // Find the text   

                $("#Descrip").val($descrip);
                $("#Id").val($id);
                $("#Cantidad").val($cantidad);
                $("#Clave").val($clave);
                $("#Req").val($req);

            });

            $("[data-behavior~=only-alphanum]").alphanum({
                allowSpace: false,
                allowOtherCharSets: false,
                allow: '.'
            });
            $("[data-behavior~=only-alphanum-caps]").alphanum({
                allowSpace: false,
                allowOtherCharSets: false,
                forceUpper: true
            });
            $("[data-behavior~=only-alphanum-caps-15]").alphanum({
                allowSpace: false,
                allowOtherCharSets: false,
                forceUpper: true,
                maxLength: 15
            });
            $("[data-behavior~=only-alphanum-white]").alphanum({
                allow: '.',
                disallow: "'",
                allowSpace: true
            });
            $("[data-behavior~=only-num]").numeric({
                allowMinus: false,
                allowThouSep: false
            });

            $("[data-behavior~=only-alpha]").alphanum({
                allowNumeric: false,
                allowSpace: false,
                allowOtherCharSets: true
            });

        </script>
    </body>
</html>

