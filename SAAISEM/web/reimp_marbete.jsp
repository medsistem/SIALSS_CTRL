<%-- 
    Document   : Reporte
    Created on : 26/12/2012, 09:05:24 AM
    Author     : Unknown
--%>

<%@page import="conn.ConectionDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="net.sf.jasperreports.engine.*" %> 
<%@ page import="java.util.*" %> 
<%@ page import="java.io.*" %> 
<%@ page import="java.sql.*" %> 
<% /*Parametros para realizar la conexión*/

    HttpSession sesion = request.getSession();
    ConectionDB con = new ConectionDB();
    String usua = "";
    int Tarimas = 0, TTarimas = 0, TarimasI = 0, Piezas = 0, TPiezas = 0, Cajas = 0, TCajas = 0, CajasI = 0, Resto = 0, Restop = 0, PiezasT = 0, PiezasC = 0, PiezasTI = 0, TotalP = 0, Bandera = 0;
    String Clave = "", Cb = "", Lote = "", Cadu = "", Descrip = "", Orden = "", F_OrdCom = "", FechaA = "", DesProy = "", Contrato = "", Origen = "";
    int Proyecto = 0, idorigen = 0;
    String imgOrigen = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }
    String folio_gnk = request.getParameter("fol_gnkl");
    F_OrdCom = request.getParameter("F_OrdCom");
    Bandera = Integer.parseInt(request.getParameter("Ban"));

    try {
        con.conectar();
        con.insertar("delete from tb_marbetes where F_OrdCom='" + F_OrdCom + "'");
        //ResultSet rset = con.consulta("SELECT C.F_ClaPro,SUBSTR(M.F_DesPro,1,150) AS F_DesPro,L.F_ClaLot,L.F_FecCad,L.F_Cb,C.F_ClaDoc,C.F_OrdCom,C.F_CanCom,C.F_FecApl,C.F_Tarimas,F_Cajas,F_Pz,F_CajasI,F_Resto,F_TarimasI,P.F_DesProy,P.F_Contrato  FROM tb_compra C INNER JOIN tb_lote L ON C.F_Lote=L.F_FolLot AND C.F_ClaPro=L.F_ClaPro INNER JOIN tb_medica M ON C.F_ClaPro=M.F_ClaPro INNER JOIN tb_proyectos AS P ON C.F_Proyecto = P.F_Id WHERE C.F_OrdCom='" + request.getParameter("F_OrdCom") + "' and (C.F_FolRemi = '" + request.getParameter("F_FolRemi") + "' or C.F_FolFac = '" + request.getParameter("F_FolRemi") + "') GROUP BY C.F_ClaPro,M.F_DesPro,L.F_ClaLot,L.F_FecCad,L.F_Cb,C.F_ClaDoc,C.F_OrdCom,C.F_CanCom;");
       
        ResultSet rset = con.consulta("SELECT C.F_ClaPro,SUBSTR(M.F_DesPro,1,150) AS F_DesPro,L.F_ClaLot,L.F_FecCad,L.F_Cb,C.F_ClaDoc,C.F_OrdCom,C.F_CanCom,C.F_FecApl,C.F_Tarimas,F_Cajas,F_Pz,F_CajasI,F_Resto,F_TarimasI,P.F_DesProy,P.F_Contrato,O.F_DesOri,O.F_ClaOri,C.F_Proyecto FROM tb_compra C INNER JOIN tb_lote L ON C.F_Lote=L.F_FolLot AND C.F_ClaPro=L.F_ClaPro INNER JOIN tb_medica M ON C.F_ClaPro=M.F_ClaPro INNER JOIN tb_proyectos AS P ON C.F_Proyecto = P.F_Id LEFT JOIN tb_origen O ON L.F_Origen = O.F_ClaOri WHERE C.F_OrdCom='" + request.getParameter("F_OrdCom") + "' and (C.F_FolRemi = '" + request.getParameter("F_FolRemi") + "' or C.F_FolFac = '" + request.getParameter("F_FolRemi") + "') GROUP BY C.F_ClaPro,M.F_DesPro,L.F_ClaLot,L.F_FecCad,L.F_Cb,C.F_ClaDoc,C.F_OrdCom,C.F_CanCom;");
      
        while (rset.next()) {
            TPiezas = Integer.parseInt(rset.getString("C.F_CanCom"));
            Clave = rset.getString("C.F_ClaPro");
            Lote = rset.getString("L.F_ClaLot");
            Cb = rset.getString("L.F_Cb");
            Cadu = rset.getString("L.F_FecCad");
            Descrip = rset.getString("F_DesPro");
            Orden = rset.getString("C.F_OrdCom");
            TotalP = rset.getInt("C.F_CanCom");
            FechaA = rset.getString("C.F_FecApl");
            Tarimas = rset.getInt(10);
            Cajas = rset.getInt(11);
            Piezas = rset.getInt(12);
            CajasI = rset.getInt(13);
            Resto = rset.getInt(14);
            TarimasI = rset.getInt(15);
            DesProy = rset.getString(16);
            Contrato = rset.getString(17);
            Origen = rset.getString(18);
            idorigen = rset.getInt(19);
            Proyecto = rset.getInt(20);
           
          if (Proyecto == 7 && (idorigen == 26 || idorigen == 27)) {
                  imgOrigen =  "image/almacen.png";
              } else {
              imgOrigen = "image/Nored_fria.jpg";
              }
                  
            if (Bandera == 2) {
                if (TarimasI == 0) {
                    TarimasI = 1;
                }
            }

            if (Tarimas > 0) {
                TCajas = Cajas / Tarimas;
            }

            int contar = 0;
            TTarimas = Tarimas + TarimasI;

            if (TTarimas == 0) {
                TTarimas = 1;
            }

            for (int x = 1; x <= TTarimas; x++) {
                    contar++;
                    if (Tarimas > 0) {
                        TotalP = TCajas * Piezas;
                    } else {
                        TotalP = (CajasI * Piezas);
                    }

                    if (contar == TTarimas) {
                        TotalP = TotalP + Resto;
                    }
                    Tarimas = Tarimas - 1;

                 
                        if (F_OrdCom.contains("U013")) {
                            System.out.println("sientre a inserta");
                            con.insertar("insert into tb_marbetes values ('" + folio_gnk + "','" + Cb + "','" + Clave + "','" + Descrip + "','" + Lote + "','" + Cadu + "', '" + Orden + "', '" + TotalP + "', '" + FechaA + "','0', '" + DesProy + "','" + Contrato + "','" + Origen + "')");
                        }else{
                            con.insertar("insert into tb_marbetes values ('" + folio_gnk + "','" + Cb + "','" + Clave + "','" + Descrip + "','" + Lote + "','" + Cadu + "', '" + Orden + "', '" + TotalP + "', '" + FechaA + "','0','" + DesProy + "','','" + Origen + "')");
             
                        }
                      
                       }
        }

        con.cierraConexion();
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }

    Connection conexion;
    Class.forName("org.mariadb.jdbc.Driver").newInstance();
    conexion = con.getConn();
    /*Establecemos la ruta del reporte*/
    File reportFile = new File(application.getRealPath("/reportes/Marbete.jasper"));
    /* No enviamos parámetros porque nuestro reporte no los necesita asi que escriba 
     cualquier cadena de texto ya que solo seguiremos el formato del método runReportToPdf*/
    Map parameters = new HashMap();
    parameters.put("folmar", folio_gnk);
    parameters.put("F_OrdCom", request.getParameter("F_OrdCom"));
    parameters.put("ImgOrigen", imgOrigen);
      System.out.println("Origen imagen:"+imgOrigen);
    /*Enviamos la ruta del reporte, los parámetros y la conexión(objeto Connection)*/
    byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conexion);
    /*Indicamos que la respuesta va a ser en formato PDF*/ response.setContentType("application/pdf");
    response.setContentLength(bytes.length);
    ServletOutputStream ouputStream = response.getOutputStream();
    ouputStream.write(bytes, 0, bytes.length);
    /*Limpiamos y cerramos flujos de salida*/ ouputStream.flush();
    ouputStream.close();
%>
