<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="servlets.agregaCeros"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<%
    ConectionDB con = new ConectionDB();
    agregaCeros agr=new agregaCeros();
    try{
        agr.agrega();
        con.conectar();
        ResultSet rset = con.consulta("select F_ClaPro from tb_medica");
        while(rset.next()){
            ResultSet rset2=con.consulta("select pres from presentaciones where clave = '"+rset.getString("F_ClaPro") +"' ");
            while(rset2.next()){
                System.out.println("update tb_medica set F_PrePro = '"+rset2.getString("pres") +"' where F_ClaPro = '"+rset.getString("F_ClaPro")+"'  ");
                con.actualizar("update tb_medica set F_PrePro = '"+rset2.getString("pres") +"' where F_ClaPro = '"+rset.getString("F_ClaPro")+"'  ");
            }
        }
        con.cierraConexion();
    }catch(Exception e){
        System.out.println(e.getMessage());
    }
    
%>