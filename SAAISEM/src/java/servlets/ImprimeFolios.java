/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import conn.*;
import java.sql.ResultSet;
import javax.servlet.http.HttpSession;

/**
 * Imprime de multiples las folios de entrega, listado de folios y modificación de fechas entrega
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ImprimeFolios extends HttpServlet {

    java.text.DateFormat df2 = new java.text.SimpleDateFormat("dd/MM/yyyy");
    java.text.DateFormat df3 = new java.text.SimpleDateFormat("yyyy-MM-dd");
    java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession sesion = request.getSession(true);
        String clave = "", descr = "";
        int ban1 = 0;

        ConectionDB con = new ConectionDB();
        //ConectionDB_SQLServer consql = new ConectionDB_SQLServer();
       
        try {
            
            if (request.getParameter("accion").equals("impRemisMultples")) {
                con.conectar();
                String Impresora = request.getParameter("impresora");
                String Copy = request.getParameter("Copy");
                String[] claveschk = request.getParameterValues("checkRemis");
                String remisionesReImp = "";
                System.out.println("Impresora:"+Impresora);
                //out.println("<script>alert('Impresora:"+Impresora+"')</script>");
                if(!(Impresora.equals(""))){
                    if(!(Copy.equals(""))){                        
                 con.actualizar("DELETE FROM tb_folioimp WHERE F_User='"+sesion.getAttribute("nombre")+"'");   
                for (int i = 0; i < claveschk.length; i++) {
                    System.out.println("claveschk: "+claveschk);
                    //response.sendRedirect("reportes/multiplesRemis.jsp?remis=" + claveschk[i]);
                    
                    con.actualizar("INSERT INTO tb_folioimp VALUES('"+claveschk[i]+"','"+Copy+"','"+sesion.getAttribute("nombre")+"')");
                    //out.println(" <script>window.open('reportes/multiplesRemis.jsp?remis=" + claveschk[i] + "&Impresora="+Impresora+"&Copy="+Copy+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                    //out.println("<script>window.open('reportes/multiplesRemis.jsp?remis=" + claveschk[i] + "', '_blank')</script>");
                    if (i == (claveschk.length - 1)) {
                        remisionesReImp = remisionesReImp + "" + claveschk[i] + "";
                        out.println("remisionesReImp:" +remisionesReImp);
                    } else {
                        remisionesReImp = remisionesReImp + "" + claveschk[i] + ",";
                        out.println("remisionesReImp:" +remisionesReImp);
                    }
                }
                out.println(" <script>window.open('reportes/multiplesRemis.jsp?Impresora="+Impresora+"&User="+sesion.getAttribute("nombre")+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                //out.println("<script>window.location='facturacion/cambioFechas.jsp'</script>");
                out.println("<script>window.history.back()</script>");
                out.println("remisionesReImp:" +remisionesReImp);
                }else{
                    out.println("<script>alert('Favor de Seleccionar No Copias')</script>"); 
                    out.println("<script>window.history.back()</script>");
                    }
                }else{
                    out.println("<script>alert('Favor de Seleccionar Impresora')</script>"); 
                    out.println("<script>window.history.back()</script>");
                }
                con.cierraConexion();
            }
            if (request.getParameter("accion").equals("ImpRelacion")) {
                con.conectar();
                int LargoF=0;
                String FechaFol="",ContFolio="",MuestraFolio="",FecMin="",FecMax="";
                String Radio = request.getParameter("radio1");
                String Folio1 = request.getParameter("folio11");
                String unidad = request.getParameter("unidad1");
                String unidad2 = request.getParameter("unidad2");
                String Folio2 = request.getParameter("folio21");
                String Fecha1 = request.getParameter("fecha_ini1");
                String Fecha2 = request.getParameter("fecha_fin1");                
                String Impresora = request.getParameter("impresora");
                
                String QUni="",QFolio="",QFecha="",Query="";
                    int ban=0,ban2=0,ban3=0;
                    if(unidad !="" && unidad2 !="") {                        
                        ban=1;
                    } 
                    if(Folio1 !="" && Folio2 !=""){
                        ban2=1;
                    }
                    if(Fecha1 !="" && Fecha2 !=""){                        
                        ban3=1;
                    }
                    if(ban == 1){
                        QUni = " WHERE F_ClaCli BETWEEN '"+unidad+"' AND '"+unidad2+"' ";
                    }
                    if(ban2 == 1){
                        if(ban == 0){
                            QFolio = " WHERE F_ClaDoc between '"+Folio1+"' and '"+Folio2+"' ";
                        }else{
                            QFolio = " AND F_ClaDoc between '"+Folio1+"' and '"+Folio2+"' ";
                        }
                    }
                    
                    if(ban3 == 1){
                        if(ban == 0 && ban2 == 0){
                            QFecha=" WHERE F_FecEnt between '"+Fecha1+"' and '"+Fecha2+"' ";
                        }else{
                            QFecha=" AND F_FecEnt between '"+Fecha1+"' and '"+Fecha2+"' ";
                        }
                    }
                    
                    Query = QUni + QFolio + QFecha;
                
                String remisionesReImp = "";
                System.out.println("Impresora:"+Impresora);
                //out.println("<script>alert('Impresora:"+Impresora+"')</script>");
                if(!(Impresora.equals(""))){
                    
                   con.actualizar("delete from tb_imprelacion where F_User='"+sesion.getAttribute("nombre")+"'");
                   ResultSet Folios = con.consulta("SELECT F_ClaDoc FROM tb_factura "+Query+" GROUP BY F_ClaDoc");
                    //ResultSet Folios = con.consulta("SELECT MIN(F_ClaDoc),MAX(F_ClaDoc) FROM tb_factura WHERE "+FechaFol+"");
                   while(Folios.next()){
                       ContFolio = Folios.getString(1);
                       //Folio1 = Folios.getString(1);
                       //Folio2 = Folios.getString(2);
                       con.insertar("INSERT INTO tb_imprelacion VALUES (0,'"+Folios.getString(1)+"','"+sesion.getAttribute("nombre")+"')");
                       MuestraFolio = MuestraFolio + ContFolio + ",";
                   }
                   LargoF = MuestraFolio.length();
                   MuestraFolio = MuestraFolio.substring(0, LargoF-1);
                   ResultSet Fechas = con.consulta("SELECT DATE_FORMAT(MIN(F_FecEnt),'%d/%m/%Y'),DATE_FORMAT(MAX(F_FecEnt),'%d/%m/%Y') FROM tb_factura WHERE F_ClaDoc IN ('"+MuestraFolio+"')");
                   if(Fechas.next()){
                       FecMin = Fechas.getString(1);
                       FecMax = Fechas.getString(2);
                   }
                          
                out.println(" <script>window.open('reportes/ImprimeRelacion.jsp?FecMin="+FecMin+"&FecMax="+FecMax+"&Impresora="+Impresora+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                //out.println("<script>window.location='facturacion/cambioFechas.jsp'</script>");
                out.println("<script>window.history.back()</script>");
                MuestraFolio="";
                }else{
                    out.println("<script>alert('Favor de Seleccionar Impresora')</script>"); 
                    out.println("<script>window.history.back()</script>");
                }
                con.cierraConexion();
            }
            if (request.getParameter("accion").equals("recalendarizarRemis")) {
                con.conectar();

                try {
                    String[] claveschk = request.getParameterValues("checkRemis");
                    String remisionesReCal = "";
                    for (int i = 0; i < claveschk.length; i++) {
                        if (i == (claveschk.length - 1)) {
                            remisionesReCal = remisionesReCal + "'" + claveschk[i] + "'";
                        } else {
                            remisionesReCal = remisionesReCal + "'" + claveschk[i] + "',";
                        }
                    }
                    out.println(remisionesReCal);

                    con.insertar("update tb_factura set F_FecEnt = '" + request.getParameter("F_FecEnt") + "' where F_ClaDoc in (" + remisionesReCal + ")");
                    out.println("<script>alert('Actualización correcta')</script>");
                } catch (Exception e) {
                    out.println("<script>alert('Error al actualizar')</script>");
                }
                out.println("<script>window.location='facturacion/cambioFechas.jsp'</script>");
                con.cierraConexion();
            }
            
                        
                        

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println(e.getLocalizedMessage());
                }
                
            
    }

    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
