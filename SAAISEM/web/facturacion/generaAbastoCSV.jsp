<%-- 
    Document   : generaAbastoCSV
    Created on : 21/04/2015, 09:15:47 AM
    Author     : Americo
--%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileWriter"%>
<%@page import="java.io.BufferedWriter"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="conn.ConectionDB"%>
<%
    HttpSession sesion = request.getSession();
    String usua = "", tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
        response.sendRedirect("index.jsp");
    }
    File archivo;
    archivo = new File("C:\\ABASTO\\MICH\\Abasto_" + request.getParameter("F_ClaDoc") + "-" + request.getParameter("ConInv") + "A.csv");
    BufferedWriter fw = new BufferedWriter(new FileWriter(archivo));
    int Origen = 0, Origen2 = 0, Contar = 0;
    String Origenes = "", Origenes2 = "", Proyecto = "", Documento = "";

    ConectionDB con = new ConectionDB();
    PreparedStatement ps;
    ResultSet rsetValida;

    Proyecto = request.getParameter("idProyecto");
    Documento = request.getParameter("F_ClaDoc");

    try {
        String queryElimina = "DELETE FROM tb_abastoweb WHERE F_Sts = 0 and F_Proyecto = ? AND F_ClaDoc = ?;";

        String queryInserta = "INSERT INTO tb_abastoweb VALUES (?,?,?,?,?,?,?,?,?,?,NOW(),?,0,0,?);";

        String getFactorEmpaque = "SELECT IFNULL(F_FactorEmpaque, 0) as factor FROM tb_compra where F_Lote = ? order by F_IdCom DESC;";

        String queryDatosCsV = "SELECT F.F_ClaCli, F.F_Proyecto, F.F_ClaDoc, LTRIM(RTRIM(F.F_ClaPro)), M.F_DesPro, LTRIM(RTRIM(L.F_ClaLot)), "
                + "DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantSur), L.F_Origen, SUBSTR(L.F_Cb, 1, 13) AS F_Cb, NOW() ,"
                + "L.F_FolLot AS LOTE, L.F_Origen AS ORIGEN FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_ClaPro = L.F_ClaPro AND F.F_Ubicacion = L.F_Ubica INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro WHERE F_ClaDoc = '" + request.getParameter("F_ClaDoc") + "' AND F_CantSur > 0 AND F_StsFact = 'A' AND F.F_Proyecto = '" + request.getParameter("idProyecto") + "' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen;";
        //String queryDatosCsV = "SELECT F.F_ClaCli, F.F_Proyecto, F.F_ClaDoc, LTRIM(RTRIM(F.F_ClaPro)), M.F_DesPro, LTRIM(RTRIM(L.F_ClaLot)), DATE_FORMAT(L.F_FecCad, '%d/%m/%Y') AS F_FecCad, SUM(F.F_CantSur), L.F_Origen, SUBSTR(L.F_Cb, 1, 13) AS F_Cb, NOW(),CASE WHEN ORI.F_TipOri = 'AR' THEN '1' ELSE '0' END AS ORIGEN, F.F_Lote as LOTE FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote = L.F_FolLot AND F.F_ClaPro = L.F_ClaPro AND F.F_Ubicacion = L.F_Ubica INNER JOIN tb_medica M ON F.F_ClaPro = M.F_ClaPro INNER JOIN tb_origen ORI ON ORI.F_ClaOri = L.F_Origen WHERE F_ClaDoc = '" + request.getParameter("F_ClaDoc") + "' AND F_CantSur > 0 AND F_StsFact = 'A' AND F.F_Proyecto = '" + request.getParameter("idProyecto") + "' GROUP BY F.F_ClaPro, L.F_ClaLot, L.F_FecCad, L.F_Origen;";

        ps = con.getConn().prepareStatement(queryElimina);
        ps.setString(1, Proyecto);
        ps.setString(2, Documento);
        ps.execute();

        ps.clearParameters();

        ps = con.getConn().prepareStatement(queryDatosCsV);
        ResultSet rsetDatos = ps.executeQuery();
        System.out.println("query" + rsetDatos);
        while (rsetDatos.next()) {

            Origenes2 = "0";

            int factorEmpaque = 1;
            int folLot = rsetDatos.getInt("LOTE");
            PreparedStatement psfe = con.getConn().prepareStatement(getFactorEmpaque);
            psfe.setInt(1, folLot);
            ResultSet rsfe = psfe.executeQuery();
            if (rsfe.next()) {
                factorEmpaque = rsfe.getInt("factor");
            }

            ps.clearParameters();
            ps = con.getConn().prepareStatement(queryInserta);

            ps.setString(1, rsetDatos.getString(1));
            ps.setString(2, rsetDatos.getString(2));
            ps.setString(3, rsetDatos.getString(3));
            ps.setString(4, rsetDatos.getString(4));
            ps.setString(5, rsetDatos.getString(5));
            ps.setString(6, rsetDatos.getString(6));
            ps.setString(7, rsetDatos.getString(7));
            ps.setString(8, rsetDatos.getString(8));
            ps.setString(9, rsetDatos.getString("ORIGEN"));
            ps.setString(10, rsetDatos.getString(10));
            ps.setString(11, usua);
            ps.setInt(12, factorEmpaque);
            ps.execute();

        }

        con.cierraConexion();
    } catch (Exception e) {
        //Logger.getLogger(AdministraRemisiones.class.getName()).log(Level.SEVERE, null, e);
        e.getMessage();
        con.cierraConexion();
    }
    try {
        con.conectar();
        String query = "SELECT LTRIM(RTRIM(F.F_ClaPro)),M.F_DesPro,LTRIM(RTRIM(L.F_ClaLot)),DATE_FORMAT(L.F_FecCad,'%d/%m/%Y') AS F_FecCad,SUM(F.F_CantSur),L.F_Origen,SUBSTR(L.F_Cb,1,13) AS F_Cb, F.F_Proyecto as proyecto,L.F_FolLot AS LOTE FROM tb_factura F INNER JOIN tb_lote L ON F.F_Lote=L.F_FolLot AND F.F_ClaPro=L.F_ClaPro AND F.F_Ubicacion=L.F_Ubica INNER JOIN tb_medica M ON F.F_ClaPro=M.F_ClaPro WHERE F_ClaDoc='" + request.getParameter("F_ClaDoc") + "' AND F_CantSur>0 AND F_StsFact='A' AND F.F_Proyecto = '" + request.getParameter("idProyecto") + "' GROUP BY F.F_ClaPro,L.F_ClaLot,L.F_FecCad,L.F_Origen;";

        ps = con.getConn().prepareStatement(query);
        ResultSet rset = ps.executeQuery();

        while (rset.next()) {
            System.out.println("Actualiza el abasto");

            Origen = rset.getInt(6);
            int proyecto = rset.getInt("proyecto");
         /*   if ((Origen == 0) || (Origen == 1)) {
                Origenes = "0";
            } else {
                Origenes = "0";
            }
            if (proyecto == 2) {
                Origenes = "0";
            }*/
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
            fw.append(rset.getString(6));
            fw.append(",");
            fw.append(rset.getString(7));
            fw.newLine();
        }

        fw.flush();
        fw.close();
        con.cierraConexion();
    } catch (Exception e) {
        //Logger.getLogger(AdministraRemisiones.class.getName()).log(Level.SEVERE, null, e);
        con.cierraConexion();
    }

    //out.println("<script>window.location='../reimp_factura.jsp'</script>");
    //con.cierraConexion();
%>
<head>
    <script type="text/javascript">

        var ventana = window.self;
        ventana.opener = window.self;
        setTimeout("window.close()", 7000);

    </script>
</head>