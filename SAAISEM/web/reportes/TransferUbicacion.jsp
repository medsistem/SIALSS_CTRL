<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.sql.ResultSet"%>

<%@page import="conn.*" %>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" import="java.text.*" import="java.lang.*" import="java.util.*" import= "javax.swing.*" import="java.io.*" import="java.text.DateFormat" import="java.text.ParseException" import="java.text.SimpleDateFormat" import="java.util.Calendar" import="java.util.Date"  import="java.text.NumberFormat" import="java.util.Locale" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns:v="urn:schemas-microsoft-com:vml"
xmlns:o="urn:schemas-microsoft-com:office:office"
xmlns:x="urn:schemas-microsoft-com:office:excel"
xmlns="http://www.w3.org/TR/REC-html40">
<% 

ConectionDB con = new ConectionDB();
NumberFormat nf1 = NumberFormat.getInstance(Locale.US); 
DecimalFormat formatter2 = new DecimalFormat("#,###,###.##");

ResultSet rset2 = null;
ResultSet rset = null;
String F_ClaDoc="",ConInv="";
String only="";

	try {
		F_ClaDoc = request.getParameter("F_ClaDoc");       			   
                ConInv = request.getParameter("ConInv");       			   
		       			   
        } catch (Exception e) { }
        
        
String but="r";
	  

response.setContentType("application/vnd.ms-excel");
response.setHeader("Content-Disposition","attachment; filename=Transferencia_"+F_ClaDoc+".xls");


//ingresar un try y realizar una sola conexión
   
				  
%>
<head>
<meta http-equiv=Content-Type content="text/html; charset=windows-1252">
<meta name=ProgId content=Excel.Sheet>
<meta name=Generator content="Microsoft Excel 12">
<link id=Main-File rel=Main-File href="../Libro1.htm">
<link rel=File-List href=filelist.xml>
<!--link rel=Stylesheet href=stylesheet.css-->
<style> 
<!--table
	{mso-displayed-decimal-separator:"\.";
	mso-displayed-thousand-separator:"\,";}
@page
	{margin:.75in .7in .75in .7in;
	mso-header-margin:.3in;
	mso-footer-margin:.3in;
	mso-page-orientation:landscape;}
.style1 {color: #FF0000}
-->
</style>
<![if !supportTabStrip]><script language="JavaScript"> 
<!--
function fnUpdateTabs()
 {
  if (parent.window.g_iIEVer>=4) {
   if (parent.document.readyState=="complete"
    && parent.frames['frTabs'].document.readyState=="complete")
   parent.fnSetActiveSheet(0);
  else
   window.setTimeout("fnUpdateTabs();",150);
 }
}
 
if (window.name!="frSheet")
 window.location.replace("../Libro1.htm");
else
 fnUpdateTabs();
//-->
</script>
<![endif]>
</head>
 
<body link=blue vlink=purple>
 
<table border=0 cellpadding=0 cellspacing=0 width=439 style='border-collapse:
 collapse;table-layout:fixed;width:330pt'>
 <col width=120 style='mso-width-source:userset;mso-width-alt:4388;width:90pt'>
 <col width=117 style='mso-width-source:userset;mso-width-alt:4278;width:88pt'>
 <col width=101 span=2 style='mso-width-source:userset;mso-width-alt:3693;
 width:76pt'>
 
 <tr height=20 style='height:15.0pt'>
  <td height=20 colspan=3 style='height:15.0pt;mso-ignore:colspan'></td>
 </tr>
 <%
         try{
       con.conectar();
       
           
               rset2   = con.consulta("SELECT M.F_ProMov,L.F_ClaLot,DATE_FORMAT(F_FecCad,'%d/%m/%Y') AS F_FecCad,U.F_DesUbi,SUM(F_CantMov) AS F_CantMov FROM tb_movinv M INNER JOIN tb_lote L ON M.F_LotMov=L.F_FolLot AND M.F_UbiMov=L.F_Ubica INNER JOIN tb_ubica U ON M.F_UbiMov=U.F_ClaUbi WHERE F_ConMov='"+ConInv+"' AND F_DocMov='"+F_ClaDoc+"' GROUP BY M.F_ProMov,L.F_ClaLot,F_FecCad,U.F_DesUbi");
           
  %>
   <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt;mso-ignore:colspan'>Clave</td>
  <td class=xl66>Lote</td>
  <td class=xl66>Caducidad</td>
  <td class=xl66>Ubicación</td>
  <td class=xl66>Cantidad</td>  
  </tr>
  <tr height=20 style='height:15.0pt'>
 
 <%
   while (rset2.next()) {
       
 %>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset2.getString(1)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset2.getString(2)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset2.getString(3)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset2.getString(4)%></td>
  <td height=20 class=xl75 ><%=rset2.getString(5)%></td>
 </tr>
  <%
       
   }
       
       
 %>

 


 
  
  
 
 <![if supportMisalignedColumns]>
 <tr height=0 style='display:none'>
  <td width=120 style='width:90pt'></td>
  <td width=117 style='width:88pt'></td>
  <td width=101 style='width:76pt'></td>
  <td width=101 style='width:76pt'></td>
 </tr>
 <![endif]>
</table>
 
</body>
 
</html>
<%
 con.cierraConexion();
 } catch (Exception e) {
     System.out.println(e.getMessage());
 }
                                
%>