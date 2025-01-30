<%-- 
    Document   : Reporte
    Created on : 26/12/2012, 09:05:24 AM
    Author     : Unknown
--%>

<%@page import="ReportesPuntos.Requerimiento"%>
<%@page import="conn.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="net.sf.jasperreports.engine.*" %> 
<%@ page import="java.util.*" %> 
<%@ page import="java.io.*" %> 
<%@ page import="java.sql.*" %> 
<% /*Parametros para realizar la conexión*/

    HttpSession sesion = request.getSession();
    ConectionDB con = new ConectionDB();
    ConRequerimiento Req = new ConRequerimiento();
    String usua = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("../index.jsp");
    }
    String folio_gnk = "0";
    String ClaUni = request.getParameter("Unidad");
    
    /*Establecemos la ruta del reporte*/
    try{
        con.conectar();
        Req.conectar();
        
        String F_DesUniIS="",F_DesJurIS="",F_DesMunIS="",F_DesLocIS="",F_Ruta="",Modulo="",Nucleo="",TOrigen="",DesOrigen="";
        String FechaMax="",F_ClaPro="",F_FecSur="",Requerido="",Requerido2="";
        int No=0,Origen=0,PzFacturado=0,FacturaPro=0,PzFacturado2=0,FacturaPro2=0;
        
        con.actualizar("DELETE FROM tb_reqimprime WHERE F_ClaUni='"+ClaUni+"' AND F_FolReq='"+folio_gnk+"'");
        
        ResultSet DatosUnidad = con.consulta("SELECT CONCAT(u.F_ClaUniIS,' ',u.F_DesUniIS) AS F_DesUniIS,CONCAT(u.F_JurUniIS,' ',j.F_DesJurIS) AS F_DesJurIS,CONCAT(u.F_MunUniIS,' ',m.F_DesMunIS) AS F_DesMunIS,CONCAT(u.F_LocUniIS,' ',l.F_DesLocIS) AS F_DesLocIS "
                + "FROM tb_unidis u INNER JOIN tb_juriis j on u.F_JurUniIS=j.F_ClaJurIS INNER JOIN tb_muniis m on u.F_MunUniIS=m.F_ClaMunIS AND u.F_JurUniIS=m.F_JurMunIS INNER JOIN tb_locais l on u.F_LocUniIS=l.F_ClaLocIS AND u.F_MunUniIS=l.F_MunLocIS "
                + "WHERE F_ClaInt1='"+ClaUni+"' OR F_ClaInt2='"+ClaUni+"' OR F_ClaInt3='"+ClaUni+"' OR F_ClaInt4='"+ClaUni+"' OR F_ClaInt5='"+ClaUni+"' OR F_ClaInt6='"+ClaUni+"' OR F_ClaInt7='"+ClaUni+"' OR F_ClaInt8='"+ClaUni+"' OR F_ClaInt9='"+ClaUni+"' OR F_ClaInt10='"+ClaUni+"'");
        if(DatosUnidad.next()){
            F_DesUniIS = DatosUnidad.getString(1);
            F_DesJurIS = DatosUnidad.getString(2);
            F_DesMunIS = DatosUnidad.getString(3);
            F_DesLocIS = DatosUnidad.getString(4);
        }
        
        ResultSet Ruta = con.consulta("SELECT F_Ruta FROM tb_fecharuta WHERE F_ClaUni='"+ClaUni+"'");
        if(Ruta.next()){
            F_Ruta = Ruta.getString(1);
        }
        
        Modulo = ClaUni.substring(4,5);
        if(Modulo.equals("A")){
         Modulo = "(1)";   
        }else if(Modulo.equals("B")){
            Modulo = "(2)";   
        }else if(Modulo.equals("C")){
            Modulo = "(3)";   
        }else if(Modulo.equals("D")){
            Modulo = "(4)";   
        }else if(Modulo.equals("E")){
            Modulo = "(5)";   
        }else if(Modulo.equals("F")){
            Modulo = "(6)";   
        }else if(Modulo.equals("G")){
            Modulo = "(7)";   
        } 
        Nucleo = Modulo +" "+ClaUni;
        ResultSet Fecha = con.consulta("SELECT MAX(F_FecEnt) FROM tb_factura WHERE F_ClaCli='"+ClaUni+"' AND F_StsFact='A'");
        if(Fecha.next())
            FechaMax = Fecha.getString(1);
        ResultSet rset = con.consulta("select F_ClaPro,F_DesPro,F_Origen,F_Cp7  from tb_medica WHERE F_N1='1' AND F_StsPro='A' order by F_ClaPro+0");
        while (rset.next()) {            
            No = No+1;
            String color = "";
            int cantSolPrev = 0;
            Origen = rset.getInt(3);
            TOrigen = rset.getString(4);
            if (Origen == 1){
                DesOrigen="ADM";
                if(TOrigen.equals("EST")){
                    if(Origen == 1){
                        DesOrigen="A-O";
                    }else{
                        DesOrigen="ODO";
                    }
                }
            }
            
            if(FechaMax !=""){
            ResultSet Facturado = con.consulta("SELECT SUM(F_CantSur) FROM tb_factura WHERE F_ClaCli='"+ClaUni+"' AND F_StsFact='A' AND F_FecEnt='"+FechaMax+"' AND F_ClaPro='"+rset.getString(1)+"' GROUP BY F_ClaPro");
              if(Facturado.next())
                  PzFacturado = Facturado.getInt(1); 
            }else{
                PzFacturado = 0;
            }
            
            ResultSet Artiis = con.consulta("SELECT LTRIM(F_ClaArtIS) FROM tb_artiis WHERE F_ClaInt='"+rset.getString(1)+"'");
            if(Artiis.next())
                F_ClaPro = Artiis.getString(1);
            ResultSet FactPro = Req.consulta("SELECT F_FacPro FROM tb_factupromedio WHERE F_ClaUA='"+ClaUni+"' AND F_ClaMed='"+rset.getString(1)+"'");
            if(FactPro.next())
                FacturaPro = FactPro.getInt(1);
            
           

            
            con.insertar("INSERT INTO tb_reqimprime VALUES('"+F_DesJurIS+"','"+F_DesMunIS+"','"+F_DesLocIS+"','"+F_DesUniIS+"','"+Nucleo+"','"+F_Ruta+"','"+No+"','"+F_ClaPro+"','"+rset.getString(2)+"','"+FacturaPro+"','"+PzFacturado+"','','"+DesOrigen+"','"+ClaUni+"','"+folio_gnk+"','"+F_FecSur+"',0)");
            
                FacturaPro2 = FacturaPro2 + FacturaPro;
                PzFacturado2 = PzFacturado2 +PzFacturado;
                Requerido2 = "";
            
            DesOrigen="";
            Requerido="";
            FacturaPro=0;
            PzFacturado=0;
            Origen=0;
        }   
        con.insertar("INSERT INTO tb_reqimprime VALUES('"+F_DesJurIS+"','"+F_DesMunIS+"','"+F_DesLocIS+"','"+F_DesUniIS+"','"+Nucleo+"','"+F_Ruta+"','','','','"+FacturaPro2+"','"+PzFacturado2+"','','"+DesOrigen+"','"+ClaUni+"','"+folio_gnk+"','"+F_FecSur+"',0)");
        
        con.cierraConexion();
        Req.cierraConexion();
    }catch(Exception e ){
        System.out.println(e.getMessage());
                
    }
    Connection conexion;
    System.out.println(folio_gnk+"/"+ClaUni);
    Class.forName("org.mariadb.jdbc.Driver").newInstance();
    conexion = con.getConn();        
    File reportFile = new File(application.getRealPath("/ReportesPuntos/RequerimientosCeros.jasper"));
    /* No enviamos parámetros porque nuestro reporte no los necesita asi que escriba 
     cualquier cadena de texto ya que solo seguiremos el formato del método runReportToPdf*/
    Map parameters = new HashMap();
    parameters.put("Folfact", folio_gnk);
    parameters.put("Clauni", ClaUni);
    /*Enviamos la ruta del reporte, los parámetros y la conexión(objeto Connection)*/
    byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, conexion);
    /*Indicamos que la respuesta va a ser en formato PDF*/ response.setContentType("application/pdf");
    response.setContentLength(bytes.length);
    ServletOutputStream ouputStream = response.getOutputStream();
    ouputStream.write(bytes, 0, bytes.length); /*Limpiamos y cerramos flujos de salida*/ ouputStream.flush();
    ouputStream.close();
%>
