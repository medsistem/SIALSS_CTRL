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
    String usua = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    ConectionDB_Inventarios con = new ConectionDB_Inventarios();

    String fecha_ini = "", fecha_fin = "";
    try {
        //if (request.getParameter("accion").equals("buscar")) {
            fecha_ini = request.getParameter("fecha_ini");
            fecha_fin = request.getParameter("fecha_fin");
            
        //}
    } catch (Exception e) {

    }
    if (fecha_ini == null) {
        fecha_ini = "";        
    }
    if (fecha_fin == null) {
        fecha_fin = "";        
    }
   
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=\"Reporte_Inventario_"+fecha_ini+"_al_"+fecha_fin+".xls\"");
%>
<div>
    <div class="panel panel-primary">
        <div class="panel-body">
            <table class="table table-bordered table-striped" id="datosCompras" border="1">
                <thead>
                    <tr>
                        <td>Claves</td>
                        <td>Nombre</td>
                        <td>Cantidad</td> 
                    </tr>
                </thead>
                <tbody>
                    <%
                        try {
                            con.conectar();
                            try {
                                ResultSet rset = null;
                                ResultSet RsetDatos = null;
                                int Cant=0,tipo_mod=0,Total=0;
                                String DesUni="",des_uni="";
                                if(fecha_ini !="" && fecha_fin !=""){                        
                                    rset = con.consulta("SELECT cla_mod,tipo_mod FROM inventarios WHERE hora_ini BETWEEN '"+fecha_ini+"' AND '"+fecha_fin+"' GROUP BY cla_mod,tipo_mod;");
                                                    while (rset.next()) {
                                                         tipo_mod = rset.getInt(2);
                                                        
                                                        if(tipo_mod == 1){
                                                            DesUni=" (Dispensario)";
                                                        }else{
                                                            DesUni=" (Almacen)";
                                                        }
                                                      ResultSet DatosU = con.consulta("SELECT des_uni FROM unidades WHERE cla_mod='"+rset.getString(1)+"';");
                                                      if(DatosU.next()){
                                                          des_uni = DatosU.getString(1);
                                                      }
                                                        des_uni = des_uni+" "+DesUni;
                                                     
                                                     ResultSet DatosI = con.consulta("SELECT SUM(cant) FROM inventarios i INNER JOIN det_inv d ON i._id=d.id_inv WHERE hora_ini BETWEEN '"+fecha_ini+"' AND '"+fecha_fin+"' AND i.cla_mod='"+rset.getString(1)+"' AND tipo_mod='"+rset.getString(2)+"';");
                                                     if(DatosI.next()){
                                                         Total = DatosI.getInt(1);
                                                     }
                                                     
                                   %>
                                <tr>
                                    <td><%=rset.getString(1)%></td>
                                    <td><%=des_uni%></td>
                                    <td><%=Total%></td>  
                                </tr>
                    <%
                                      
                                }
                     %>
                    
                    <%
                        Total=0;
                        Cant = 0;
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
        <br />
        <br />
        <br />
        <!--div class="panel panel-primary">
        <div class="panel-body">
            <table class="table table-bordered table-striped" id="datosfirmas" border="0">
                <tr>
                    <td colspan="3"><img src="http://187.176.10.50:8081/SIALSS_CTRL/imagenes/firmas/juris1/1001A.jpg" width="80" height="100"></td>
                    <td colspan="3"><img src="http://187.176.10.50:8081/SIALSS_CTRL/imagenes/firmas/juris1/1001A.jpg" width="80" height="100"></td>
                </tr>
                <tr>
                    <td colspan="2"><h5>RESPONSABLE MEDICO</h5></td>
                    <td colspan="3"><h5>COORDINADOR O ADMINISTRADOR MUNICIPAL</h5></td>
                </tr>
            </table>
        </div>
        </div-->
        
               
    </div>
</div>