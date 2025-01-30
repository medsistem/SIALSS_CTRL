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
ResultSet Consulta = null;
ResultSet Municipio = null;
ResultSet Juris = null;
ResultSet Localidad = null;
ResultSet Unidades = null;
ResultSet Coordinacion = null;
ResultSet Secuencial = null;
ResultSet DatosUni = null;
ResultSet rset = null;
String fecha1="",fecha2="",radio="",F_Idsur="",F_DesSur="",F_Descob="",F_DesSum="";
int F_IdePro=0,F_Cvesum=0;
double costo=0.0,iva=0.0,total=0.0;
String only="";

	try {
		fecha1 = request.getParameter("f1");       			   
                fecha2 = request.getParameter("f2");       			   
                radio = request.getParameter("ra");       			   
		       			   
        } catch (Exception e) { }
        
        System.out.println("f1"+fecha1+" f2:"+fecha2+" ra:"+radio);
String but="r";
	  

response.setContentType("application/vnd.ms-excel");
response.setHeader("Content-Disposition","attachment; filename=Facturacion TXT.xls");


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
 
 
 <%
         try{
       con.conectar();
       con.actualizar("DELETE FROM tb_listaplana WHERE F_FecsurT BETWEEN '"+fecha1+"' AND '"+fecha2+"'");
       if ((fecha1.equals("")) && (fecha2.equals(""))){
          // out.println("<script>alert('Favor de Introducir Fechas')</script>");
          // out.println("<script>window.history.back()</script>");
       }else{
           String DesSur="",Descob="",DesSum="",DesJuris="",DesMunIS="",DesLoc="",DesUnidad="",CooUniIS="",Descor="",DesSecu="",Folio="";
           String FacSAVI="";
           int Idsur=0,IdePro=0,Cvesum=0,Punto=0,Diferencia=0;
           double Costos=0.0,Ivas=0.0,Totales=0.0;
           if (radio.equals("no")){
               System.out.println("No relacionado");
               Consulta= con.consulta("SELECT F_CveJur,F_Cvemun,F_Cvemed,F_CveLoc,F_Cveuni,F_Idsur,F_IdePro,F_Cvesum,F_Fecsur,F_FacGNKLAgr,F_FacSAVI,F_CosServ,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,SUM(F_CostMed) AS F_CostMed,CONCAT(MIN(F_SecuencialMin),'-',MAX(F_SecuencialMax)) AS F_Secuencial  "
                       + "FROM tb_txtlistaplana WHERE F_Fecsur BETWEEN '"+fecha1+"' AND '"+fecha2+"' AND F_FacGNKLAgr LIKE 'AG-0%' AND F_FacSAVI = '' "
                       + "GROUP BY F_CveJur,F_Cvemun,F_Cvemed,F_CveLoc,F_Cveuni,F_Idsur,F_IdePro,F_Cvesum,F_Fecsur,F_FacGNKLAgr,F_FacSAVI");
           }else{
               System.out.println("Si relacionado");
               
               Consulta= con.consulta("SELECT F_CveJur,F_Cvemun,F_Cvemed,F_CveLoc,F_Cveuni,F_Idsur,F_IdePro,F_Cvesum,F_Fecsur,F_FacGNKLAgr,F_FacSAVI,F_CosServ,SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,SUM(F_CostMed) AS F_CostMed,CONCAT(MIN(F_SecuencialMin),'-',MAX(F_SecuencialMax)) AS F_Secuencial  "
                       + "FROM tb_txtlistaplana WHERE F_Fecsur BETWEEN '"+fecha1+"' AND '"+fecha2+"' AND F_FacGNKLAgr LIKE 'AG-0%' AND F_FacSAVI != '' "
                       + "GROUP BY F_CveJur,F_Cvemun,F_Cvemed,F_CveLoc,F_Cveuni,F_Idsur,F_IdePro,F_Cvesum,F_Fecsur,F_FacGNKLAgr,F_FacSAVI");
               
               /*Consulta= con.consulta("SELECT F_CveJur,F_Cvemun,F_Cveuni,F_Cvemed,F_CveLoc,F_Idsur,F_IdePro,F_Cvesum,F_Cveart,DATE_FORMAT(F_Fecsur,'%Y-%m-%d') AS F_Fecsur,F_FacGNKLAgr,"
                       + "SUM(F_Canreq) AS F_Canreq,SUM(F_Cansur) AS F_Cansur,SUM(F_Canreq-F_Cansur) AS F_Diferencia,F_CosUni,F_FacSAVI,sum((F_CosUni * F_Cansur)) AS F_CostMed,F_CosServ AS F_CosServ,F_Secuencial "
                       + "FROM tb_txtis WHERE F_Fecsur BETWEEN '"+fecha1+"' AND '"+fecha2+"' AND F_Status !='C' AND F_FacGNKLAgr LIKE 'AG-0%' AND F_FacSAVI != '' "
                       + "GROUP BY F_Cveuni,F_CveJur,F_Cvemun,F_Cvemed,F_CveLoc,F_Idsur,F_IdePro,F_Cvesum,F_Cveart,F_Fecsur,F_FacGNKLAgr ");*/
           }
           while(Consulta.next()){
               
               Idsur = Consulta.getInt("F_Idsur");
               IdePro = Consulta.getInt("F_IdePro");
               Cvesum = Consulta.getInt("F_Cvesum");
               Costos = Consulta.getDouble("F_CostMed");
               FacSAVI = Consulta.getString("F_FacSAVI");
               Diferencia = Consulta.getInt("F_Canreq") - Consulta.getInt("F_Cansur"); 
               DesSecu = Consulta.getString("F_Secuencial");
               
               if((Idsur == 1) && (IdePro == 0) && (Cvesum == 1)){
                   Punto = 1;
               }else if((Idsur == 1) && (IdePro == 1) && (Cvesum == 1)){
                   Punto = 2;
               }else if((Idsur == 1) && (IdePro == 0) && (Cvesum == 2)){
                   Punto = 3;
               }else if((Idsur == 1) && (IdePro == 1) && (Cvesum == 2)){
                   Punto = 4;
               }else if((Idsur == 2) && (IdePro == 0) && (Cvesum == 1)){
                   Punto = 5;
               }else if((Idsur == 2) && (IdePro == 1) && (Cvesum == 1)){
                   Punto = 6;
               }else if((Idsur == 2) && (IdePro == 0) && (Cvesum == 2)){
                   Punto = 7;
               }else if((Idsur == 2) && (IdePro == 1) && (Cvesum == 2)){
                   Punto = 8;
               }
               
               if (Idsur == 1){
                    DesSur="ADM";
                }else{
                    DesSur="VTA";
                }
                if(IdePro == 0){
                    Descob="PA";
                }else{
                    Descob="SP";
                }
                if(Cvesum == 1){
                    DesSum="MED";
                }else{
                    DesSum="CUR";
                }
                
                if (DesSum.equals("CUR")){
                Ivas = Costos * 0.16;
               }
                Totales = Costos + Ivas;
               
                DatosUni = con.consulta("SELECT F_ClaCooIS,F_DesCooIS,F_DesJurIS,F_DesMunIS,F_DesLocIS,F_DesUniIS FROM view_unidadestxt WHERE F_ClaJurIS='"+Consulta.getString("F_CveJur")+"' AND F_ClaMunIS='"+Consulta.getString("F_Cvemun")+"' AND F_ClaLocIS='"+Consulta.getString("F_CveLoc")+"' AND F_ClaUniIS='"+Consulta.getString("F_Cveuni")+"'");
                if(DatosUni.next()){
                    CooUniIS = DatosUni.getString(1);
                    Descor = DatosUni.getString(2);
                    DesJuris = DatosUni.getString(3);
                    DesMunIS = DatosUni.getString(4);
                    DesLoc = DatosUni.getString(5);
                    DesUnidad = DatosUni.getString(6);
                }
               /*Juris = con.consulta("SELECT F_DesJurIS FROM tb_juriis WHERE F_ClaJurIS='"+Consulta.getString("F_CveJur")+"'");
               if(Juris.next()){
                   DesJuris = Juris.getString(1);
               }
               Municipio = con.consulta("SELECT F_DesMunIS FROM tb_muniis WHERE F_ClaMunIS='"+Consulta.getString("F_Cvemun")+"' AND F_JurMunIS='"+Consulta.getString("F_CveJur")+"'");
               if(Municipio.next()){
                   DesMunIS = Municipio.getString(1);
               }
               
               Localidad = con.consulta("SELECT F_DesLocIS FROM tb_locais WHERE F_ClaLocIS='"+Consulta.getString("F_CveLoc")+"' AND F_MunLocIS='"+Consulta.getString("F_Cvemun")+"'");
               if(Localidad.next()){
                   DesLoc = Localidad.getString(1);
               }
               
               Unidades = con.consulta("SELECT F_DesUniIS,F_CooUniIS FROM tb_unidis WHERE F_ClaUniIS='"+Consulta.getString("F_Cveuni")+"' AND F_JurUniIS='"+Consulta.getString("F_CveJur")+"' AND F_MunUniIS='"+Consulta.getString("F_Cvemun")+"' AND F_LocUniIS='"+Consulta.getString("F_CveLoc")+"' AND F_MedUniIS='"+Consulta.getString("F_Cvemed")+"'");
               if(Unidades.next()){
                DesUnidad = Unidades.getString(1);
                CooUniIS = Unidades.getString(2);
               }
               
               Coordinacion = con.consulta("SELECT F_DesCooIS FROM tb_cooris WHERE F_ClaCooIS='"+CooUniIS+"'");
               if(Coordinacion.next()){
                   Descor = Coordinacion.getString(1);
               }*/
               Folio = Consulta.getString("F_FacGNKLAgr")+"."+Punto;
               
               //Secuencial = con.consulta("SELECT CONCAT(MIN(F_Secuencial),'-',MAX(F_Secuencial)) AS F_Secuencial FROM tb_txtis WHERE F_FolCon='"+Folio+"'");
               /*Secuencial = con.consulta("SELECT F_Secuencial FROM view_foliosecuencial WHERE F_FolCon='"+Folio+"'");
               if(Secuencial.next()){
                   DesSecu = Secuencial.getString(1);
               }*/
               if(DesSecu == null){DesSecu = "";}
               con.insertar("INSERT INTO tb_listaplana VALUES('"+CooUniIS+"','"+Descor+"','"+Consulta.getString("F_CveJur")+"','"+DesJuris+"','"+Consulta.getString("F_Cvemun")+"','"+DesMunIS+"','"+Consulta.getString("F_CveLoc")+"','"+DesLoc+"','"+Consulta.getString("F_Cveuni")+"','"+DesUnidad+"','"+Consulta.getString("F_Fecsur")+"',"
                       + "'"+Consulta.getString("F_FacGNKLAgr")+"','"+Punto+"','"+DesSur+"','"+Descob+"','"+DesSum+"','"+DesSecu+"','"+FacSAVI+"','"+Consulta.getString("F_Canreq")+"','"+Consulta.getString("F_Cansur")+"','"+Diferencia+"','"+Consulta.getString("F_CostMed")+"','"+Consulta.getString("F_CosServ")+"','"+Ivas+"','"+Totales+"',0)");
               Punto=0;
               Folio = "";
               Idsur=0;
               IdePro=0;
               Cvesum=0;
               Ivas = 0.0;
               Totales = 0.0;
           }
           
  %>
   <tr height=20 style='height:15.0pt'>
  <td height=20 style='height:15.0pt;mso-ignore:colspan'>F_CooUniIS</td>
  <td class=xl66>F_DesCooIS</td>
  <td class=xl66>F_JurUniIS</td>
  <td class=xl66>F_DesJurIS</td>
  <td class=xl66>F_MunUniIS</td>
  <td class=xl66>F_DesMunIS</td>
  <td class=xl66>F_LocUniIS</td>
  <td class=xl66>F_DesLocIS</td>
  <td class=xl66>F_CveUniT</td>
  <td class=xl66>F_DesUni</td>
  <td class=xl66>F_FecsurT</td>
  <td class=xl66>F_FacMEDALFAT</td>
  <td class=xl66>F_FolConT</td>
  <td class=xl66>F_Surtido</td>
  <td class=xl66>F_Cobertura</td>
  <td class=xl66>F_Suministro</td>
  <td class=xl66>F_Secuencial</td>
  <td class=xl66>F_FacSAVIT</td>
  <td class=xl66>F_canreq</td>
  <td class=xl66>F_CanSur</td>
  <td class=xl66>F_Diferencia</td>
  <td class=xl66>F_CostMed</td>
  <td class=xl66>F_CostServ</td>
  <td class=xl66>F_IVA</td>
  <td class=xl66>F_Total</td>
  </tr>
  <tr height=20 style='height:15.0pt'>
 
 <%
   rset2 = con.consulta("SELECT * FROM tb_listaplana WHERE F_FecsurT BETWEEN '"+fecha1+"' AND '"+fecha2+"' ");
   while (rset2.next()) {
      
 %>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset2.getString(1)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset2.getString(2)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset2.getString(3)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset2.getString(4)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset2.getString(5)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset2.getString(6)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset2.getString(7)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset2.getString(8)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset2.getString(9)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset2.getString(10)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset2.getString(11)%></td>
  <td height=20 class=xl75 ><%=rset2.getString(12)%></td>
  <td height=20 class=xl75 ><%=rset2.getString(13)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset2.getString(14)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset2.getString(15)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset2.getString(16)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset2.getString(17)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"@"'><%=rset2.getString(18)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"###,###,###,###,###"'><%=rset2.getString(19)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"###,###,###,###,###"'><%=rset2.getString(20)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"###,###,###,###,###"'><%=rset2.getString(21)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"###,###,###,###,###.00"'><%=rset2.getString(22)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"###,###,###,###,###.00"'><%=rset2.getString(23)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"###,###,###,###,###.00"'><%=rset2.getString(24)%></td>
  <td height=20 class=xl75 style='height:15.0pt;mso-number-format:"###,###,###,###,###.00"'><%=rset2.getString(25)%></td>    
 
 </tr>
  <%
       
   }
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