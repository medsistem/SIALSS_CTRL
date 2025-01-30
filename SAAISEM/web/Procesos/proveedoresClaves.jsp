<%-- 
    Document   : capturaISEM.jsp
    Created on : 14-jul-2014, 14:48:02
    Author     : Americo
--%>

<%@page import="conn.ConectionDB"%>
<%@page import="ISEM.CapturaPedidos"%>
<%@page import="javax.servlet.http.HttpSession"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    ConectionDB con = new ConectionDB();
    try {
        con.conectar();
        try {
            ResultSet rset = con.consulta("select pro, cla from comodin;");
            while (rset.next()) {
                ResultSet rset2 = con.consulta("select F_ClaProve from tb_proveedor where F_NomPro = '"+rset.getString(1)+"' ");
                while(rset2.next()){
                    con.insertar("insert into tb_prodprov values('"+rset2.getString(1) +"','"+rset.getString(2)+"','0','0',0);");
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        con.cierraConexion();
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
%>