<%@page import="org.json.JSONObject"%>
<%@page import="org.json.JSONArray"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.ResultSetMetaData"%>
<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@page import="conn.*" %>
<%
    ConectionDB con = new ConectionDB();

    try {
        con.conectar();
        String query = "CALL medalfa_SIALSS_CTRL22.VIEW_DATA_SAA(" + request.getParameter("projectId") + "," + request.getParameter("typeConfig") + ",'" + request.getParameter("data1") + "','" + request.getParameter("data2") + "','" + request.getParameter("data3") + "')";
        System.out.println(query);
        ResultSet rset = con.consulta(query);
        JSONArray json = new JSONArray();
        ResultSetMetaData rsmd = rset.getMetaData();
        while (rset.next()) {
            int numColumns = rsmd.getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 1; i <= numColumns; i++) {
                String column_name = rsmd.getColumnName(i);
                obj.put(column_name, rset.getObject(column_name));
            }
            json.put(obj);
        }
        con.cierraConexion();
%>{ 
"request" : {"projectId":"<%= request.getParameter("projectId")%>","typeConfig":"<%= request.getParameter("typeConfig")%>","data1":"<%= request.getParameter("data1")%>","data2":"<%= request.getParameter("data2")%>","data3":"<%= request.getParameter("data3")%>"},
"data" : <%=json%>
}
<%
} catch (Exception e) {
%>{ 
"request" : {"projectId":"<%= request.getParameter("projectId")%>","typeConfig":"<%= request.getParameter("typeConfig")%>","data1":"<%= request.getParameter("data1")%>","data2":"<%= request.getParameter("data2")%>","data3":"<%= request.getParameter("data3")%>"},
"error" : "<%=e.toString()%>"
}
<%
    }
%>