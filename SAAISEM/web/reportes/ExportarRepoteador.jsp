<%-- 
    Document   : index
    Created on : 17/02/2014, 03:34:46 PM
    Author     : Americo
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
    String usua = "", F_User = "", FolCon1 = "";
    String FolCon = "", Reporte = "", f1 = "", f2 = "";
    int ban = 0;
    if (sesion.getAttribute("nombre") != null) {
        usua = (String) sesion.getAttribute("nombre");
    } else {
        response.sendRedirect("index.jsp");
    }

    f1 = (String) sesion.getAttribute("ultimo_inicio");
    f2 = (String) sesion.getAttribute("ultima_fin");
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment;filename=Reporteador" + f1 + "_al_" + f2 + ".xls");

    request.setAttribute("columnas", sesion.getAttribute("ultima_columnas"));
    request.setAttribute("resultado", sesion.getAttribute("ultimo_resultado"));
%>
<div>    
    <br />
    <div class="panel panel-primary">
        <div class="panel-body table-responsive">
            <table class="table table-bordered table-striped" id="datosCompras">
                <thead>
                    <tr>
                <c:forEach items="${columnas}" var="item">
                    <td>
                    <c:out value="${item}"/>
                    </td>
                </c:forEach>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${resultado}" var="fila">
                    <tr>
                    <c:forEach items="${fila}" var="item">
                        <td>
                        <c:out value="${item}"/>
                        </td>
                    </c:forEach>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
