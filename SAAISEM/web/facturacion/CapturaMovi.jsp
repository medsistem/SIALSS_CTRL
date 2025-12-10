<%-- 
    Document   : generaTransferencias
    Created on : 20/04/2015, 04:43:45 PM
    Author     : Americo
--%>


<%@page import="java.util.Date"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<%@page import="Facturacion.FacturacionManual" %>
<!DOCTYPE html>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%
    FacturacionManual fact = new FacturacionManual();
    HttpSession sesion = request.getSession();
    String F_IndFolMov = (String) sesion.getAttribute("F_IndFolMov");
    String usua = "", tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB con = new ConectionDB();
    
    String ClaCli = "", FechaEnt = "", ClaPro = "", DesPro = "", Desproyecto="";
    int Proyecto = 0, UbicaModu = 0;
    
    try {
        ClaCli = (String) sesion.getAttribute("ClaCliFM");
        FechaEnt = (String) sesion.getAttribute("FechaEntFM");
        ClaPro = (String) sesion.getAttribute("ClaProFM");
        DesPro = (String) sesion.getAttribute("DesProFM");
    } catch (Exception e) {
        System.out.println(e);
    }
    
    if (ClaCli == null) {
        ClaCli = "";
    }
    if (FechaEnt == null) {
        FechaEnt = "";
    }
    if (ClaPro == null) {
        ClaPro = "";
    }
    if (DesPro == null) {
        DesPro = "";
    }
    if (FechaEnt.equals("")) {
        FechaEnt = df2.format(new Date());
    }
    try {
        con.conectar();
        ResultSet UbiMod = con.consulta("SELECT PU.F_Id,P.F_Id, IFNULL(P.F_DesProy, '') AS Proyecto FROM tb_parametrousuario PU LEFT JOIN ( SELECT F_Id, F_DesProy FROM tb_proyectos ) P ON PU.F_Proyecto = P.F_Id WHERE F_Usuario = '" + usua + "';");
        if (UbiMod.next()) {
            UbicaModu = UbiMod.getInt(1);
            Proyecto = UbiMod.getInt(2);
            Desproyecto = UbiMod.getString(3);
        }
        
        ResultSet rset = con.consulta("select F_IdFact, F_StsFact, F_ClaCli, F_FecEnt from tb_capmovtemp WHERE F_User='"+usua+"' AND F_StsFact='3';");
        try {
            rset.last();
            //while (rset.next()) {
            if (rset.getString("F_StsFact").equals("3")) {
                sesion.setAttribute("F_IndFolMov", rset.getString(1));
                F_IndFolMov = (String) sesion.getAttribute("F_IndFolMov");
                ClaCli = rset.getString("F_ClaCli");
                FechaEnt = rset.getString("F_FecEnt");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        //}

        if (request.getParameter("accion").equals("nuevoFolio")) {
            sesion.setAttribute("F_IndFolMov", fact.dameIndMov() + "");
            F_IndFolMov = (String) sesion.getAttribute("F_IndFolMov");
            System.out.println("Ind: "+F_IndFolMov);
        }
        con.cierraConexion();
    } catch (Exception e) {
        System.out.println(e);
    }
   
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <!---->
        <title>SIALSS IMSS</title>
    </head>
    <body onLoad="foco();">
        <div class="container">
            <h1>SIALSS</h1>
            <h4>Módulo - Sistema de Administración de Almacenes (SAA)</h4>

            <%@include file="../jspf/menuPrincipal.jspf" %>

            <div class="row">
                <div class="col-sm-12">
                    <h2>Captura Movimiento al Inventario proyecto <%=Desproyecto%></h2>
                </div>
            </div>
            <hr/>
            <%                
                if (F_IndFolMov == null) {
            %>
            <form action="CapturaMovi.jsp" method="post">
                <button class="btn btn-block btn-primary" name="accion" value="nuevoFolio">Nuevo Folio</button>
            </form>
            <%    
            } else {
            %>
            <form action="../FacturacionManual" method="post" name="formulario1" id="formulario1">
                <input type="hidden" class="form-control" name="Proyecto" id="Proyecto" value="<%=Proyecto%>"/>
                <input type="hidden" class="form-control" name="DesProyecto" id="DesProyecto" value="<%=Desproyecto%>"/>
                <h4 class="text-muted">Folio: <%=F_IndFolMov%></h4>
                <div class="row">
                    <div class="col-sm-1">
                        <h4>Movimiento:</h4>
                    </div>
                    <div class="col-sm-5">
                        <select class="form-control" name="ClaCli" id="ClaCli">
                            <option value="" >-Seleccione Movimiento-</option>
                            <%
                                try {
                                    con.conectar();
                                    ResultSet rset = con.consulta("SELECT F_IdCon,CONCAT('[',F_IdCon,']  ',F_DesCon) AS F_DesCon FROM tb_coninv WHERE  F_IdCon  IN (11,62,19,18,2,10,16,23,6,9,30,53,59,58,66,54,68,57,67,70,71,63,69);");
                                    while (rset.next()) {
                            %>
                            <option value="<%=rset.getString(1)%>"
                                    <%
                                        if (rset.getString(1).equals(ClaCli)) {
                                            out.println("selected");
                                        }
                                    %>
                                    ><%=rset.getString(2)%></option>
                            <%
                                    }
                                    con.cierraConexion();
                                } catch (Exception e) {
                                    out.println(e.getMessage());
                                }
                            %>
                        </select>
                    </div>
                    <div class="col-sm-2">
                        <h4>Fecha de Entrega</h4>
                    </div>
                    <div class="col-sm-2">
                        <input type="date" class="form-control" name="FechaEnt" id="FechaEnt" min="<%=df2.format(new Date())%>" value="<%=FechaEnt%>" onkeydown="return false"/>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">
                        <div class="row">
                            <div class="col-sm-2">
                                <h4>Ingrese CLAVE:</h4>
                            </div>
                            <div class="col-sm-2">
                                <input class="form-control" name="ClaPro" id="ClaPro"/>
                            </div>
                            <div class="col-sm-2">
                                <button class="btn btn-primary btn-block" name="accion" value="btnClaveTMov" id="btnClaveTMov" onclick="return validaBuscar();">Buscar</button>
                            </div>
                        </div>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-sm-1">
                                <h4>Clave:</h4>
                            </div>
                            <div class="col-sm-2">
                                <input class="form-control" readonly="" value="<%=ClaPro%>" id="ClaveSel"/>
                            </div>
                            <div class="col-sm-2">
                                <h4>Descripción:</h4>
                            </div>
                            <div class="col-sm-7">
                                <textarea class="form-control" readonly="" id="DesSel"><%=DesPro%></textarea>
                            </div>
                        </div>
                        <br/>

                    </div>
                    <div class="panel-footer">
                        <div class="row">
                            <div class="col-sm-2">                                
                                <h4>Ingresé Cantidad:</h4>
                            </div>
                            <div class="col-sm-2">
                                <input class="form-control" name="Cantidad" id="Cantidad" onKeyPress="return justNumbers(event);" onpaste="return false"/>
                            </div>
                            
                            <div class="col-sm-2">
                                <button class="btn btn-block btn-info" name="accion" value="SeleccionaLoteTMovi" onclick="return validaSeleccionar();">Seleccionar</button>
                            </div>
                        </div>

                    </div>
                </div>
               <table class="table table-condensed table-striped table-bordered table-responsive" id="tablaMovimiento">
                    <thead>
                        <th class="text-center">Clave</th>
                        <th class="text-center">Lote</th>
                        <th class="text-center">Caducidad</th>
                        <th class="text-center">Ubicación</th>
                        <th class="text-center">Marca</th>
                        <th class="text-center">Cantidad</th>
                        <th class="text-center">Remover</th>
                    </thead>
                    <%
                        int banBtn = 0;
                        try {
                            con.conectar();
                            ResultSet rset = con.consulta("SELECT l.F_ClaPro, l.F_ClaLot, DATE_FORMAT(l.F_FecCad, '%d/%m/%Y'), f.F_Cant, l.F_Ubica, f.F_IdFact, mar.F_DesMar, f.F_Id FROM tb_capmovtemp f, tb_lote l, tb_medica m, tb_marca mar WHERE m.F_ClaPro = l.F_ClaPro and l.F_ClaMar = mar.F_ClaMar and f.F_IdLot = l.F_IdLote and F_ClaCli = '" + ClaCli + "' and F_StsFact=3 and F_User='"+usua+"';");
                            while (rset.next()) {
                                banBtn = 1;
                    %>
                    <tr>
                        <td><%=rset.getString(1)%></td>
                        <td><%=rset.getString(2)%></td>
                        <td><%=rset.getString(3)%></td>
                        <td><%=rset.getString(5)%></td>
                        <td><%=rset.getString("F_DesMar")%></td>
                        <td><%=rset.getString(4)%></td>
                        <td>
                            <button class="btn btn-block btn-info" name="accionEliminarMov" value="<%=rset.getString("F_Id")%>" onclick="return confirm('Seguro que desea eliminar esta clave?')"><span class="glyphicon glyphicon-remove"></span></button>
                        </td>
                    </tr>
                    <%
                            }
                            con.cierraConexion();
                        } catch (Exception e) {
                            out.println(e.getMessage());
                        }
                    %>
                </table>
                <%
                    if (banBtn == 1) {
                %>
                <div class="row">
                    <h4 class="col-sm-2">
                        Observaciones:
                    </h4>
                    <div class="col-sm-10">
                        <textarea class="form-control" name="obs" id="obs"></textarea>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6">
                        <button class="btn btn-block btn-primary" name="accion" value="ConfirmarMovimiento" onclick="return validaRemision()">Confirmar Movimiento</button>
                    </div>
                    <div class="col-sm-6">
                        <button class="btn btn-block btn-info" name="accion" value="CancelarMovimiento" onclick="return confirm('Seguro de CANCELAR?')">Cancelar Movimiento</button>
                    </div>
                </div>
                <%
                    }
                %>
            </form>
            <%
                }
            %>
        </div>
        <%@include file="../jspf/piePagina.jspf" %>
        <!-- 
    ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="../js/jquery-1.9.1.js"></script>
        <script src="../js/bootstrap.js"></script>
        <script src="../js/jquery-ui-1.10.3.custom.js"></script>
        <script src="../js/funcIngresos.js"></script>
        <script>
                            function justNumbers(e)
                            {
                                var keynum = window.event ? window.event.keyCode : e.which;
                                if ((keynum === 8) || (keynum === 46))
                                    return true;
                                return /\d/.test(String.fromCharCode(keynum));
                            }
                            
                            function cambiaLoteCadu(elemento) {
                                var indice = elemento.selectedIndex;
                                document.getElementById('SelectCadu').selectedIndex = indice;
                            }
                            
                            function validaBuscar() {
                                var Unidad = document.getElementById('ClaCli').value;
                                if (Unidad === "") {
                                    alert('Seleccione Movimiento');
                                    return false;
                                }
                                
                                var FechaEnt = document.getElementById('FechaEnt').value;
                                if (FechaEnt === "") {
                                    alert('Seleccione Fecha de Entrega');
                                    return false;
                                }
                                var clave = document.getElementById('ClaPro').value;
                                if (clave === "") {
                                    alert('Escriba una Clave');
                                    return false;
                                }
                            }
                            
                            
                            function validaSeleccionar() {
                                var DesSel = document.getElementById('DesSel').value;
                                if (DesSel === "") {
                                    alert('Favor de Capturar Toda la información');
                                    return false;
                                }
                                var cantidad = document.getElementById('Cantidad').value;
                                if (cantidad === "") {
                                    alert('Escriba una cantidad');
                                    return false;
                                }
                                
                            }
                            
                            
       function foco() {
                        document.formulario1.ClaPro.focus();
                        var ClaveSel = document.formulario1.ClaveSel.value;
                        if (ClaveSel !== "") {
                            document.formulario1.Cantidad.focus();
                            window.scrollTo(0, 380);
                        }else{
                            document.formulario1.ClaPro.focus();
                        }
                        
                    }
                            
    function validaRemision() {
            var confirmacion = confirm('Seguro que desea generar');
            var obs = $('#obs').val();
            if (confirmacion === true) {
                if(obs !=""){
                $('#btnGeneraFolio').prop('disabled', true);                
                return true;
                }else{
                    alert("Favor de agregar Observación");
                    return false;
                }
            } else {
                return false;
            }
        }
        
        $(document).ready(function () {
                $('#tablaMovimiento').dataTable();
            });
        
        
        </script>
        
        
    </body>

</html>

