<%-- 
    Document   : cambioFechas
    Created on : 14/04/2015, 12:58:35 PM
    Author     : Americo
--%>

<%@page import="javax.print.PrintServiceLookup"%>
<%@page import="javax.print.PrintService"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%

    HttpSession sesion = request.getSession();
    String usua = "";
    String tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();

    String fecha_ini = "", fecha_fin = "", folio1 = "", folio2 = "", radio = "", unidad = "", Proyecto = "", TipoInsum = "" ;
    try {
        fecha_ini = request.getParameter("fecha_ini");
        fecha_fin = request.getParameter("fecha_fin");
        folio1 = request.getParameter("folio1");
        folio2 = request.getParameter("folio2");
        radio = request.getParameter("radio");
        unidad = request.getParameter("SelectUnidad");
        Proyecto = request.getParameter("Proyecto");
    } catch (Exception e) {

    }
    if (fecha_ini == null) {
        fecha_ini = "";
    }
    if (fecha_fin == null) {
        fecha_fin = "";
    }
    if (folio1 == null) {
        folio1 = "";
    }
    if (folio2 == null) {
        folio2 = "";
    }
    if (unidad == null) {
        unidad = "";
    }

    if (unidad.equals("--Seleccione--")) {
        unidad = "";
    }
    if (Proyecto == null) {
        Proyecto = "0";
    }
    int  ban4= 0;
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link href="../css/cupertino/jquery-ui-1.10.3.custom.css" rel="stylesheet"/>
        <link href="../css/datepicker3.css" rel="stylesheet">
        <link href="../css/dataTables.bootstrap.css" rel="stylesheet" type="text/css" >
        <link href="../css/select2.css" rel="stylesheet" type="text/css"/>
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    <body>
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>

            <%@include file="../jspf/menuPrincipal.jspf" %>

            <div class="panel-heading">
                <h3 class="panel-title">Recalendarizar/Imprimir Múltiples Remisiones</h3>
            </div>
            <form action="cambioFechas.jsp" method="post">
                <div class="panel-footer">
                    <div class="row">
                        <label class="control-label col-sm-1" for="fecha_ini">Unidad</label>
                        <div class="col-sm-5">
                            <select name="SelectUnidad" id="SelectUnidad" class="form-control">
                                <option>--Seleccione--</option>
                                <%
                                    try {
                                        con.conectar();
                                        ResultSet Unidad = con.consulta("SELECT F_ClaCli,CONCAT(F_ClaCli,' [',F_NomCli,']') AS F_NomCli FROM tb_uniatn order by F_NomCli ");
                                        while (Unidad.next()) {
                                %>
                                <option value="<%=Unidad.getString(1)%>"><%=Unidad.getString(2)%></option>
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                    }
                                %>
                            </select>
                        </div>
                        <label class="control-label col-sm-1" for="fecha_ini">Proyecto</label>
                        <div class="col-sm-2">
                            <select name="Proyecto" id="Proyecto" class="form-control">
                                <option value="0">--Seleccione--</option>
                                <%
                                    try {
                                        con.conectar();
                                        ResultSet Unidad = con.consulta("SELECT * FROM tb_proyectos order by F_DesProy;");
                                        while (Unidad.next()) {
                                %>
                                <option value="<%=Unidad.getString(1)%>"><%=Unidad.getString(2)%></option>
                                <%
                                        }
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                    <br />
                    <div class="row">
                        <label class="control-label col-sm-1" for="fecha_ini">Folios</label>
                        <div class="col-lg-1">
                            <input class="form-control" id="folio1" name="folio1" type="text" value="" onchange="habilitar(this.value);" />
                        </div>
                        <div class="col-lg-1">
                            <input class="form-control" id="folio2" name="folio2" type="text" value="" onchange="habilitar(this.value);"/>
                        </div>

                        <label class="control-label col-sm-1" for="fecha_ini">Fechas</label>
                        <div class="col-sm-2">
                            <input class="form-control" id="fecha_ini" name="fecha_ini" type="date" onchange="habilitar(this.value);"/>
                        </div>
                        <div class="col-sm-2">
                            <input class="form-control" id="fecha_fin" name="fecha_fin" type="date" onchange="habilitar(this.value);"/>
                        </div>
                    </div>   
                </div>
                <div class="panel-body">
                    <div class="row">
                        <button class="btn btn-block btn-primary" id="btn_capturar" onclick="return confirma();">MOSTRAR&nbsp;<label class="glyphicon glyphicon-search"></label></button>                        
                    </div>
                </div>  
            </form>
            <%
                int Contar = 0;
                try {
                    con.conectar();
                    try {
                        String QUni = "", QFolio = "", QFecha = "", Query = "";
                        int ban = 0, ban2 = 0, ban3 = 0;
                        if (unidad != "") {
                            ban = 1;
                        }
                        if (folio1 != "" && folio2 != "") {
                            ban2 = 1;
                        }
                        if (fecha_ini != "" && fecha_fin != "") {
                            ban3 = 1;
                        }
                        if (ban == 1) {
                            QUni = " WHERE F_ClaCli = '" + unidad + "' ";
                        }
                        if (ban2 == 1) {
                            if (ban == 0) {
                                QFolio = " WHERE F_ClaDoc between '" + folio1 + "' and '" + folio2 + "' ";
                            } else {
                                QFolio = " AND F_ClaDoc between '" + folio1 + "' and '" + folio2 + "' ";
                            }
                        }

                        if (ban3 == 1) {
                            if (ban == 0 && ban2 == 0) {
                                QFecha = " WHERE F_FecEnt between '" + fecha_ini + "' and '" + fecha_fin + "' ";
                            } else {
                                QFecha = " AND F_FecEnt between '" + fecha_ini + "' and '" + fecha_fin + "' ";
                            }
                        }

                        Query = QUni + QFolio + QFecha;
                        if (Query != "") {

                        } else {
                            Query = " Where F_ClaDoc=0 ";
                        }
                        System.out.println("Query " + Query);
                        ResultSet rset = con.consulta("SELECT count(F_ClaDoc) FROM tb_factura " + Query + " and F_StsFact='A';");
                        if (rset.next()) {
                            Contar = rset.getInt(1);
                        }
                    } catch (Exception e) {

                    }
                    con.cierraConexion();
                } catch (Exception e) {

                }

            %>
            <form action="../Facturacion" method="post" id="formCambioFechas">
                <%                    if (Contar > 0) {
                %>
                <div class="row">
                    <input class="form-control" id="unidad1" name="unidad1" type="hidden" value="<%=unidad%>" />
                    <input class="form-control" id="radio1" name="radio1" type="hidden" value="<%=radio%>" />
                    <input class="form-control" id="foio11" name="folio11" type="hidden" value="<%=folio1%>" />
                    <input class="form-control" id="folio21" name="folio21" type="hidden" value="<%=folio2%>" />
                    <input class="form-control" id="fecha_ini1" name="fecha_ini1" type="hidden" value="<%=fecha_ini%>" />
                    <input class="form-control" id="fecha_fin1" name="fecha_fin1" type="hidden" value="<%=fecha_fin%>" />
                    <input class="form-control" id="Proyecto1" name="Proyecto1" type="hidden" value="<%=Proyecto%>" />
                   
                    <div class="row">
                        <label class="control-label col-sm-2" for="imprera">Seleccione Impresora</label>
                        <div class="col-sm-2 col-sm-2">                       
                            <select id="impresora" name="impresora">
                                <option value="">--Seleccione Impresora--</option>
                                <%
                                    String Nom = "";
                                    PrintService[] impresoras = PrintServiceLookup.lookupPrintServices(null, null);
                                    for (PrintService printService : impresoras) {
                                        Nom = printService.getName();
                                        //System.out.println("impresora" + Nom);                            
%>
                                <option value="<%=Nom%>"><%=Nom%></option>                            
                                <%}%>
                            </select>                        
                        </div>
                        <label class="control-label col-sm-2 col-sm-offset-1" for="imprera">No. Copias</label>
                        <div class="col-sm-1">                       
                            <select id="Copy" name="Copy">
                                <option value="">-Copias-</option>                            
                                <option value="1">1</option>
                                <option value="2">2</option>
                                <option value="3">3</option>
                                <option value="4">4</option>
                            </select>                        
                        </div>
                        <div class="col-sm-2 col-sm-2">                       
                            <select id="TipoInsumo" name="TipoInsumo">
                                <option value="0">--Tipo de Remision--</option>
                                <option value="3">APE</option>  
                                <option value="4">Controlado</option>  
                                <option value="2">Red Fria</option>  
                                <option value="1">Seco</option>  
                               
                            </select>                        
                        </div>
                    </div>
                    <br />
                    <div class="row">
                        <div class="col-sm-2 col-sm-offset-1">                       
                            <button type="submit" class="btn btn-info btn-block" id="btnImpMult" name="accion" value="impRemisMultples" >Impresiones Múltiples</button>
                        </div>
                        <!--div class="col-sm-2">                       
                            <button type="submit" class="btn btn-info btn-block" id="btnImpMult" name="accion" value="impRemisMultplesSurtido" >Impresiones Surtido</button>
                        </div-->
                        <div class="col-sm-2">                       
                            <button type="submit" class="btn btn-primary btn-block" id="btnImpMult" name="accion" value="ImpRelacion" >Imprime Relación</button>
                        </div>    
                        <div class="col-sm-2">                      
                            <button type="button" class="btn btn-primary btn-block" data-toggle="modal" data-target="#modalCambioFecha" id="btnRecalendarizar" >Recalendarizar</button>
                        </div>
                       
                        <div class="col-sm-2">
                            
                            <button type="submit" class="btn btn-info btn-block" id="btnImpMultMich" name="accion" value="impRemisMultplesMich" >Impresiones Surtido</button>
                        </div>
                        
                        <div class="col-sm-2"> 
                            <a type="button" class="btn btn-info btn-block" href="../reportes/gnrFoliosImp.jsp?Unidad=<%=unidad%>&Folio1=<%=folio1%>&Folio2=<%=folio2%>&Fec1=<%=fecha_ini%>&Fec2=<%=fecha_fin%>&Proyecto=<%=Proyecto%>" >Exportar</a>
                        </div>
                    </div>
                </div>
                <%}%>


                <div>
                    <input class="hidden" name="accion" value="recalendarizarRemis"  />
                    <input class="hidden" id="F_FecEnt" name="F_FecEnt" value=""  />
                    <div class="panel panel-primary">
                        <div class="panel-body table-responsive">
                            <div style="width:100%; height:400px; overflow:auto;">
                                <table class="table table-bordered table-striped">
                                    <thead>
                                        <tr>
                                           <th>Sts</th>
                                            <th>No. Folio</th>
                                            <th>Punto de Entrega</th>
                                            <th>Proyecto Unidad</th>
                                            <th>Fecha de Entrega</th>
                                            <th>Factura</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <%
                                            try {
                                                con.conectar();
                                                try {
                                                    String QUni = "", QFolio = "", QFecha = "", Query = "", AND = "";
                                                    int ban = 0, ban2 = 0, ban3 = 0;
                                                    if (Proyecto.equals("0")) {
                                                        AND = "";
                                                    } else {
                                                        AND = " AND f.F_Proyecto = '" + Proyecto + "' ";
                                                    }
                                                    if (unidad != "") {
                                                        ban = 1;
                                                    }
                                                    if (folio1 != "" && folio2 != "") {
                                                        ban2 = 1;
                                                    }
                                                    if (fecha_ini != "" && fecha_fin != "") {
                                                        ban3 = 1;
                                                    }
                                                    if (ban == 1) {
                                                        QUni = " F.F_ClaCli = '" + unidad + "' ";
                                                    }
                                                    if (ban2 == 1) {
                                                        if (ban == 0) {
                                                            QFolio = " f.F_ClaDoc between '" + folio1 + "' and '" + folio2 + "' ";
                                                        } else {
                                                            QFolio = " AND f.F_ClaDoc between '" + folio1 + "' and '" + folio2 + "' ";
                                                        }
                                                    }
                                                    if (ban3 == 1) {
                                                        if (ban == 0 && ban2 == 0) {
                                                            QFecha = " F_FecEnt between '" + fecha_ini + "' and '" + fecha_fin + "' ";
                                                        } else {
                                                            QFecha = " AND F_FecEnt between '" + fecha_ini + "' and '" + fecha_fin + "' ";
                                                        }
                                                    }

                                                    Query = QUni + QFolio + QFecha;
                                                   ResultSet rset = con.consulta(""
                                                            + "(SELECT CONCAT(f.F_ClaDoc, '-', f.F_Proyecto) AS ClaDoc, f.F_ClaCli, CONCAT(f.F_ClaCli, ' - ', u.F_NomCli, ' - ', 'NORMAL') AS F_NomCli, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F_StsFact, p.F_DesProy, f.F_ClaDoc AS documento, p.F_DesProy AS F_DesProyFact, '1' AS Ban,f.F_Ubicacion  FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON F.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE " + Query + " " + AND + " AND f.F_CantSur > 0 AND f.F_StsFact = 'A' AND f.F_Ubicacion NOT RLIKE 'CONTROLADO|CTRL' AND f.F_Ubicacion NOT RLIKE 'REDFRIA|RF' AND f.F_Ubicacion NOT LIKE '%APE%' AND f.F_Ubicacion NOT LIKE '%VACUNA%' GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0) UNION "
                                                            + "(SELECT CONCAT(f.F_ClaDoc, '-', f.F_Proyecto) AS ClaDoc, f.F_ClaCli, CONCAT(f.F_ClaCli, ' - ', u.F_NomCli, ' - ', 'CONTROLADO') AS F_NomCli, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F_StsFact, p.F_DesProy, f.F_ClaDoc AS documento, p.F_DesProy AS F_DesProyFact, '4' AS Ban,f.F_Ubicacion  FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON F.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE f.F_CantSur > 0 " + AND + "  AND f.F_Ubicacion RLIKE 'CONTROLADO|CTRL' OR f.F_Ubicacion LIKE '%VACUNA%' GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto HAVING " + Query + " AND f.F_StsFact = 'A' ORDER BY f.F_ClaDoc + 0) UNION "
                                                            + "(SELECT CONCAT(f.F_ClaDoc, '-', f.F_Proyecto) AS ClaDoc, f.F_ClaCli, CONCAT(f.F_ClaCli, ' - ', u.F_NomCli, ' - ', 'RED FRÍA') AS F_NomCli, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F_StsFact, p.F_DesProy, f.F_ClaDoc AS documento, p.F_DesProy AS F_DesProyFact, '2' AS Ban,f.F_Ubicacion FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli INNER JOIN tb_proyectos p ON f.F_Proyecto = p.F_Id where "+ Query +" and F_StsFact='A' AND f.F_Ubicacion RLIKE 'REDFRIA|RF' AND f.F_CantSur > 0 "+ AND +" order by ClaDoc + 0) UNION "
                                                            + "(SELECT CONCAT(f.F_ClaDoc, '-', f.F_Proyecto) AS ClaDoc, f.F_ClaCli, CONCAT(f.F_ClaCli, ' - ',u.F_NomCli, ' - APE') AS F_NomCli, DATE_FORMAT(F_FecApl, '%d/%m/%Y') AS F_FecApl, DATE_FORMAT(F_FecEnt, '%d/%m/%Y') AS F_FecEnt, F_StsFact, p.F_DesProy, f.F_ClaDoc AS documento, ps.F_DesProy AS F_DesProyFact, '3' AS BAN,f.F_Ubicacion FROM tb_factura f INNER JOIN tb_uniatn u ON f.F_ClaCli = u.F_ClaCli LEFT JOIN tb_obserfact o ON f.F_ClaDoc = o.F_IdFact INNER JOIN tb_proyectos p ON F.F_Proyecto = p.F_Id INNER JOIN tb_proyectos ps ON f.F_Proyecto = ps.F_Id WHERE "+Query+" AND f.F_StsFact = 'A' AND f.F_Ubicacion LIKE '%APE%' AND f.F_CantSur > 0 "+AND+" GROUP BY f.F_ClaDoc , f.F_ClaCli , F_StsFact , f.F_Proyecto ORDER BY f.F_ClaDoc + 0)ORDER BY ClaDoc + 0;");
                                                 
                                                  while (rset.next()) {
                                                 ban4 = Integer.parseInt(rset.getString(10));
                                        %>
                                        <tr>
                                            <td>
                                                <div class="checkbox">
                                                    <label>
                                                        <input type="checkbox" name="checkRemis" checked="true" onchange="activarBtnReCal();" value="<%=rset.getString(1)%>">
                                                        
                                                    </label>
                                                </div>
                                            </td>
                                            <td><%=rset.getString(8)%></td>
                                            <td><%=rset.getString(3)%></td>
                                            <td><%=rset.getString(7)%></td>
                                            <td><%=rset.getString("F_FecEnt")%></td>
                                            <td><%=rset.getString(9)%></td>
                                        </tr>
                                        <%
                                                    }
                                                } catch (Exception e) {

                                                }
                                                con.cierraConexion();
                                            } catch (Exception e) {

                                            }
                                        %>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>

        <!-- Modal -->
        <div class="modal fade" id="modalCambioFecha" tabindex="-1" role="dialog" aria-labelledby="basicModal" aria-hidden="true">
            <div class="modal-dialog modal-sm">
                <div class="modal-content">
                    <form>
                        <div class="modal-header">
                            <div class="row">
                                <h4 class="col-sm-12">Cambiar Fecha</h4>
                            </div>
                        </div>
                        <div class="modal-body">
                            <h4 class="modal-title" id="myModalLabel">Seleccionar fecha:</h4>
                            <div class="row">
                                <div class="col-sm-12">
                                    <input type="date" class="form-control" required name="" id="ModalFecha" />
                                </div>
                            </div>
                            <div style="display: none;" class="text-center" id="Loader">
                                
                                <img src="../imagenes/ajax-loader-1.gif" height="150" alt=""/>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-primary" onclick="return confirmaModal();" name="accion" value="recalendarizarRemis">Recalendarizar</button>
                                <button type="button" class="btn btn-default" data-dismiss="modal">Cerrar</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- Modal -->

        <%@include file="../jspf/piePagina.jspf" %>
        <!-- 
        ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="../js/jquery-1.9.1.js"></script>
        <script src="../js/bootstrap.js"></script>
        <script src="../js/jquery-ui-1.10.3.custom.js"></script>
        <script src="../js/bootstrap-datepicker.js"></script>
        <script src="../js/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
        <script src="../js/select2.js" type="text/javascript"></script>
        <script>
                                    $(document).ready(function () {
                                        $("#SelectUnidad").select2();
                                        $('#datosCompras').dataTable();
                                        $("#fecha").datepicker();
                                        $("#fecha").datepicker('option', {dateFormat: 'dd/mm/yy'});

                                    });

                                   

                                    function confirmaModal() {
                                        var valida = confirm('Seguro que desea cambiar la fecha de entrega?');
                                        if ($('#ModalFecha').val() === "") {
                                            alert('Falta la fecha');
                                            return false;
                                        } else {
                                            if (valida) {
                                                $('#F_FecEnt').val($('#ModalFecha').val());
                                             //   alert($('#F_FecEnt').val($('#ModalFecha').val()));
                                                $('#formCambioFechas').submit();
                                            } else {
                                                return false;
                                            }
                                        }
                                    }


        </script>
        <script>
            function habilitar(value) {
                
                if (value == "si") {
                    document.getElementById("fecha_ini").disabled = true;
                    document.getElementById("fecha_fin").disabled = true;
                    document.getElementById("folio1").disabled = false;
                    document.getElementById("folio2").disabled = false;
                    document.getElementById("fecha_ini").value = "";
                    document.getElementById("fecha_fin").value = "";

                } else if (value == "no") {
                    document.getElementById("folio1").disabled = true;
                    document.getElementById("folio2").disabled = true;
                    document.getElementById("folio1").value = "";
                    document.getElementById("folio2").value = "";
                    document.getElementById("fecha_ini").disabled = false;
                    document.getElementById("fecha_fin").disabled = false;
                }
            }
        </script>
        <script type="text/javascript">
            $(function () {
                var availableTags = [
            <%
                  try {
                      con.conectar();
                      try {
                          ResultSet rset = con.consulta("SELECT F_NomCli FROM tb_uniatn");
                          while (rset.next()) {
                              out.println("'" + rset.getString(1) + "',");
                          }
                      } catch (Exception e) {

                      }
                      con.cierraConexion();
                  } catch (Exception e) {

                  }
            %>
                ];
                $("#NombreUnidad").autocomplete({
                    source: availableTags
                });
            });
        </script>
    </body>
</html>

