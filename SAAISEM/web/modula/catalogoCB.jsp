<%-- 
    Document   : catalogoCB
    Created on : 05-dic-2014, 12:42:18
    Author     : amerikillo
--%>

<%@page import="net.sourceforge.jbarcodebean.model.Code128"%>
<%@page import="javax.imageio.*"%>
<%@page import="java.io.*"%>
<%@page import="java.awt.image.*"%>
<%@page import="net.sourceforge.jbarcodebean.*"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%

    HttpSession sesion = request.getSession();
    String usua = "",  ClaveCB = "", Descripcion = "" , CB = "", tipou = "", username="";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        username= (String) sesion.getAttribute("Usuario");
        tipou = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    ClaveCB = request.getParameter("Clave");
    Descripcion = request.getParameter("Descripcion");
    CB = request.getParameter("CB");
    if (ClaveCB == null) {
        ClaveCB = "";
    }
    if (Descripcion == null) {
       Descripcion = "";
    }
    if (CB == null) {
        CB = "";
    }
    ConectionDB con = new ConectionDB();
%>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- Estilos CSS -->
        <link href="../css/bootstrap.css" rel="stylesheet">
        <link rel="stylesheet" href="../css/cupertino/jquery-ui-1.10.3.custom.css" />
        <link href="../css/navbar-fixed-top.css" rel="stylesheet">
        <link rel="stylesheet" type="text/css" href="../css/dataTables.bootstrap.css">
        <!---->
        <title>SIE Sistema de Ingreso de Entradas</title>
    </head>
    
        <div class="container">
            <h1>SIALSS_CTRL</h1>
            <h4>SISTEMA INTEGRAL DE ADMINISTRACIÓN Y LOGÍSTICA PARA SERVICIOS DE SALUD</h4>
            <hr/>
            <div class="panel panel-primary">

                <div class=" panel-body bg-primary">
                    <form class="form-horizontal " role="form" name="formularioCB" id="formularioCB" method="post" action="catalogoCB.jsp">
                        <div class="row">
                            <div class="form-group">                                
                                <label for="Clave" class="col-xs-1 control-label">CLAVE</label>
                                <div class="col-xs-2">
                                    <input type="text" class="form-control" id="Clave" name="Clave" maxlength="10" placeholder="CLAVE" onKeyPress="return tabular(event, this)"  />
                                </div>

                                <label for="Descripcion" class="col-xs-1 control-label">Descripción</label>
                                <div class="col-xs-3">
                                    <input type="text" class="form-control" id="Descrip" name="Descripcion" maxlength="80"  placeholder="Descripcion" />
                                </div>
                                <label for="Descripcion" class="col-xs-1 control-label">CB</label>
                                <div class="col-xs-2">
                                    <input type="text" class="form-control" id="cb" name="CB" maxlength="14"  placeholder="700000000123" />
                                </div>
                                <div class="col-sm-1">
                                    <button class="btn btn-block btn-info glyphicon glyphicon-search" type="submit" /> 
                                </div>
                                <div class="col-sm-1 navbar-right">
                                    <a class="btn btn-block btn-info" onclick="window.close();"><span class="glyphicon glyphicon-log-out"></a>
                                </div>
                            </div>
                        </div>
                    </form>                    
                </div>
                <hr/>

                <div class="panel-footer table-responsive ">
                    <form method="post" >


                        <table  cellpadding="0" cellspacing="0" border="1" class="table table-bordered table-striped bg-info"  id="tablaCB">
                            <thead>
                                <tr>
                                    <th style="text-align: center"><h3>Clave</h3></th>
                                    <th style="text-align: center"><h3>Descripcion</h3></th>
                                    <th style="text-align: center"><h3>Lote</h3></th>
                                    <th style="text-align: center"><h3>Caducidad</h3></th>
                                    <th style="text-align: center"><h3>CB</h3></th>

                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    try {
                                        con.conectar();
                                       ResultSet rset = null;

                                        if (ClaveCB != "") {
                                            System.out.println(" CLAVE: "+ClaveCB+"");
                                            rset = con.consulta("select l.F_ClaPro, l.F_Cb,SUBSTR( m.F_DesPro, 1, 100 ) as  F_DesPro , l.F_ClaLot, l.F_FecCad from tb_lote l, tb_medica m where l.F_ClaPro = m.F_ClaPro AND l.F_ClaLot != 'X' and l.F_ClaPro = '"+ClaveCB+"'  group by l.F_ClaPro, l.F_Cb, l.F_ClaLot, l.F_FecCad LIMIT 0,100");
                                            
                                        } else if (Descripcion != "") {
                                            System.out.println("DESC:"+Descripcion+"");
                                            rset = con.consulta("select l.F_ClaPro, l.F_Cb,SUBSTR( m.F_DesPro, 1, 100 ) as  F_DesPro , l.F_ClaLot, l.F_FecCad from tb_lote l, tb_medica m where l.F_ClaPro = m.F_ClaPro AND l.F_ClaLot != 'X' and  m.F_DesPro like '%" +Descripcion+ "%' group by l.F_ClaPro, l.F_Cb, l.F_ClaLot, l.F_FecCad LIMIT 0,100");
                                        }
                                         else if (CB != "") {
                                            System.out.println("CB:"+CB+"");
                                            rset = con.consulta("select l.F_ClaPro, l.F_Cb,SUBSTR( m.F_DesPro, 1, 100 ) as  F_DesPro , l.F_ClaLot, l.F_FecCad from tb_lote l, tb_medica m where l.F_ClaPro = m.F_ClaPro AND l.F_ClaLot != 'X' and  l.F_Cb like '"+CB+"%' group by l.F_ClaPro, l.F_Cb, l.F_ClaLot, l.F_FecCad LIMIT 0,100");
                                        }
                                        else {
                                          
                                         rset = con.consulta("select l.F_ClaPro, l.F_Cb,SUBSTR( m.F_DesPro, 1, 100 ) as  F_DesPro , l.F_ClaLot, l.F_FecCad from tb_lote l, tb_medica m where l.F_ClaPro = m.F_ClaPro AND l.F_ClaLot != 'X' group by l.F_ClaPro, l.F_Cb, l.F_ClaLot, l.F_FecCad LIMIT 0,100");
                                        }
                                            while (rset.next()) {
                                                try {
                                                    JBarcodeBean barcode = new JBarcodeBean();
                                                    barcode.setCodeType(new Code128());
                                                    barcode.setCode(rset.getString("F_Cb"));
                                                    barcode.setCheckDigit(true);
                                                    barcode.setBounds(0, 0, 200, 100);
                                                    barcode.setSize(200, 200);
                                                    barcode.setSize(1000, 1000);
                                                    //barcode.
                                                    BufferedImage bufferedImage = barcode.draw(new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB));
                                                    File file = new File(getServletContext().getRealPath("/imagenes/cb") + "/" + rset.getString("F_Cb") + ".png");
                                                    ImageIO.write(bufferedImage, "png", file);
                                                } catch (Exception e) {
                                                    System.out.println(e.getMessage());
                                                }
                                            


                                %>
                                <tr>
                                    <td><h3><%=rset.getString("F_ClaPro")%></h3></td>
                                    <td><%=rset.getString("F_DesPro")%></td>
                                    <td><h4><%=rset.getString("F_ClaLot")%></h4></td>
                                    <td><h4><%=rset.getString("F_FecCad")%></h4></td>
                                    <!--td><h4>< %=rset.getString("F_Cb")%></h4></td-->
                                    <td><img src="../imagenes/cb/<%=rset.getString("F_Cb")%>.png" width="250 px" /></td>
                                </tr>
                                <% } 
                                        con.cierraConexion();
                                    } catch (Exception e) {
                                        out.println(e.getMessage());
                                    }
                                %>
                            </tbody>
                        </table>
                    </form>
                </div>
            </div>
        </div>
        <!-- ================================================== -->
        <!-- Se coloca al final del documento para que cargue mas rapido -->
        <!-- Se debe de seguir ese orden al momento de llamar los JS -->
        <script src="../js/jquery-1.9.1.js"></script>
        <script src="../js/bootstrap.js"></script>
        <script src="../js/jquery-ui-1.10.3.custom.js"></script>
        <script src="../js/jquery.dataTables.js"></script>
        <script src="../js/dataTables.bootstrap.js"></script>
        <script>
                $(document).ready(function () {
            $('#tablaCB').dataTable();     });
        </script>
        
       <script language="javascript">
            function justNumbers(e)
            {
                var keynum = window.event ? window.event.keyCode : e.which;
                if ((keynum === 8) || (keynum === 46))
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
                    if (esto.value.length === 0) {
                        esto.value += "(";
                    }
                }
                if (esto.value.length > otro) {
                    if (esto.value.length === 4) {
                        esto.value += ") ";
                    }
                }
                if (esto.value.length > otro) {
                    if (esto.value.length === 9) {
                        esto.value += "-";
                    }
                }
                if (esto.value.length < otro) {
                    if (esto.value.length === 4 || esto.value.length === 9) {
                        esto.value = esto.value.substring(0, esto.value.length - 1);
                    }
                }
                otro = esto.value.length
            }

          function tabular(e, obj)
            {
                tecla = (document.all) ? e.keyCode : e.which;
                if (tecla !=== 13)
                    return;
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

        </script>
        <script type="text/javascript">
            $(function () {
                var desc = [
            <%
               try {
                  con.conectar();
                      ResultSet rset2 = con.consulta("SELECT SUBSTR( F_DesPro, 1, 80 ) AS F_DesPro FROM tb_medica ORDER BY F_DesPro;");
                      while (rset2.next()) {
                          out.println(" '" + rset2.getString(1) + "',");
                      }
                        con.cierraConexion();
                  } catch (Exception e) {
                    e.getMessage();
                  }
                
              
            %>
                ];
                $("#Descrip").autocomplete({
                    source: desc
                });
            });
        </script>
        
        <script type="text/javascript">
            $(function () {
                var availableTags = [
            <%
              try {
                  con.conectar();
                
                      ResultSet rset = con.consulta("SELECT F_ClaPro FROM tb_medica ORDER BY F_ClaPro+0");
                      while (rset.next()) {
                          out.println("'" + rset.getString(1) + "',");
                      }
                  
                  con.cierraConexion();
              } catch (Exception e) {
                    e.getMessage();
              }
            %>
                ];
                $("#Clave").autocomplete({
                    source: availableTags
                });
            });
        </script>
        <script type="text/javascript">
            $(function () {
                var availableTags = [
            <%
              try {
                  con.conectar();
                
                      ResultSet rset = con.consulta("SELECT c.F_Cb FROM tb_cb AS c GROUP BY c.F_Cb ORDER BY c.F_Cb ASC");
                      while (rset.next()) {
                          out.println("'"+rset.getString(1)+"',");
                      }
                  
                  con.cierraConexion();
              } catch (Exception e) {
                    e.getMessage();
              }
            %>
                ];
                $("#cb").autocomplete({
                    source: availableTags
                });
            });
        </script>
    

