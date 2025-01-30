<%-- 
    Document   : generaAbastoCSV
    Created on : 21/04/2015, 09:15:47 AM
    Author     : Americo
--%>
<%@page import="conn.ConectionDB"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileWriter"%>
<%@page import="java.io.BufferedWriter"%>
<%@page import="java.sql.ResultSet"%>

<%

     File archivo;
    archivo = new File("C:\\ABASTO\\MICH\\Abasto_" + request.getParameter("F_ClaDoc") + "-" + request.getParameter("ConInv") + "A.csv");
    BufferedWriter fw = new BufferedWriter(new FileWriter(archivo));
    int Origen = 0;
    String Origenes = "";
    try {
        
       String query = "SELECT LTRIM(RTRIM(F.F_ClaPro)),M.F_DesPro,LTRIM(RTRIM(L.F_ClaLot)),DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,SUM(F.F_CantSur),L.F_Origen,SUBSTR(L.F_Cb,1,13) AS F_Cb, F.F_Proyecto as proyecto,L.F_FolLot AS LOTE,CASE WHEN L.F_Origen = 8 THEN '1' WHEN L.F_Origen = 19 THEN '4' ELSE '0' END AS ORIGEN  FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot AND F.F_ClaPro=L.F_ClaPro AND F.F_Ubicacion=L.F_Ubica INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro WHERE F_ClaDoc='" + request.getParameter("F_ClaDoc") + "' AND F_CantSur>0 AND F_StsFact='A' AND F.F_Proyecto = '" + request.getParameter("idProyecto") + "' GROUP BY F.F_ClaPro,L.F_ClaLot,L.F_FecCad,L.F_Origen;";
 
   // String query = "SELECT LTRIM(RTRIM(F.F_ClaPro)),M.F_DesPro,LTRIM(RTRIM(L.F_ClaLot)),DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,SUM(F.F_CantSur),L.F_Origen,SUBSTR(L.F_Cb,1,13) AS F_Cb FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot AND F.F_ClaPro=L.F_ClaPro AND F.F_Ubicacion=L.F_Ubica INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro WHERE F_ClaDoc='" + request.getParameter("F_ClaDoc") + "' AND F_CantSur>0 AND F_StsFact='A' AND F.F_Proyecto = '" + request.getParameter("idProyecto") + "' GROUP BY F.F_ClaPro,L.F_ClaLot,L.F_FecCad,L.F_Origen;";
     ConectionDB conn = new ConectionDB();
    Statement stmt = null;
    ResultSet rset = stmt.executeQuery(query);
    while (rset.next()) {

        Origen = rset.getInt(6);

        if ((Origen == 0) || (Origen == 1)) {
            Origenes = "0";
        } else {
            Origenes = "0";
        }
       fw.append(rset.getString(1));
       fw.append(",");
        fw.append("-");
        fw.append(",");
        fw.append(rset.getString(3));
        fw.append(",");
        fw.append(rset.getString(4));
        fw.append(",");
        fw.append(rset.getString(5));
        fw.append(",");
        fw.append(rset.getString("ORIGEN"));
        fw.append(",");
        fw.append(rset.getString(7));
        fw.newLine();
    }
    fw.flush();
    fw.close();
    conn.cierraConexion();
    String redirectURL = "../Zip?IdReg=Abasto_"+request.getParameter("F_ClaDoc")+"-"+request.getParameter("ConInv")+"";
    response.sendRedirect(redirectURL);
   } catch (Exception e) {
        //Logger.getLogger(AdministraRemisiones.class.getName()).log(Level.SEVERE, null, e);
       e.getMessage();
    }
%>
<head>
    <script type="text/javascript">

        var ventana = window.self;
        ventana.opener = window.self;
        setTimeout("window.close()", 7000);

    </script>
</head>