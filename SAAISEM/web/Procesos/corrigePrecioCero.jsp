<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>

<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<!DOCTYPE html>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("yyyyMMddhhmmss"); %>
<%java.text.DateFormat df2 = new java.text.SimpleDateFormat("yyyy-MM-dd"); %>
<%java.text.DateFormat df3 = new java.text.SimpleDateFormat("dd/MM/yyyy"); %>
<%

    HttpSession sesion = request.getSession();
    String usua = "", tipo = "";
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
        tipo = (String) sesion.getAttribute("Tipo");
    } else {
    }
    ConectionDB_Linux con = new ConectionDB_Linux();

    try {
        con.conectar();
        ResultSet rset = con.consulta("select F_ClaPro, F_IdCom, F_CanCom from tb_compra");
        while (rset.next()) {
            double costo = 0.0;
            ResultSet rset2 = con.consulta("select F_Costo from tb_medica where F_ClaPro ='" + rset.getString("F_ClaPro") + "'");
            while (rset2.next()) {
                costo = rset2.getDouble("F_Costo");
            }
            double cantidad = rset.getInt("F_CanCom");
            double F_ClaPro = rset.getInt("F_ClaPro");
            double impuesto = 0.0;
            double total = 0.0;

            if (F_ClaPro < 9999) {
                total = cantidad * costo;
            } else {
                total = (cantidad * costo) * 1.16;
                impuesto = (cantidad * costo) * 0.16;
            }

            con.insertar("update tb_compra set F_Costo= '" + costo + "', F_ImpTo = '" + impuesto + "', F_ComTot = '" + total + "' where F_IdCom = '" + rset.getString("F_IdCom") + "' ");
        }
        con.cierraConexion();
    } catch (Exception e) {
        System.out.println(e.getMessage());
    }
%>