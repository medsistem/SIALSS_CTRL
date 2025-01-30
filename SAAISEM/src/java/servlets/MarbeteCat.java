/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Creación de marbete identificador del ingreso
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class MarbeteCat extends HttpServlet {

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

            /**
             * Para validar varios registros por auditoria del concentrado
             * global
             */
            if (request.getParameter("accion").equals("generarCarta")) {
                con.conectar();
                int tarimas=0,cajas=0,pcajas=0,tarimasi=0,resto=0,Ptarima=0;
                String cadu="",Despro="",lote="",cb="";
                
                    clave = request.getParameter("clave");
                    lote = request.getParameter("lote");
                    cadu = request.getParameter("cadu");
                    cb = request.getParameter("cb");
                    //tarimas = Integer.parseInt(request.getParameter("tarimas"));
                    //cajas = Integer.parseInt(request.getParameter("cajas"));
                    //pcajas = Integer.parseInt(request.getParameter("pcajas"));
                    //tarimasi = Integer.parseInt(request.getParameter("tarimasi"));
                    resto = Integer.parseInt(request.getParameter("resto"));
                    Ptarima = pcajas * cajas;
                    
                    con.actualizar("delete from tb_marbetecap");
                    
                    ResultSet rset = con.consulta("SELECT F_DesPro FROM tb_medica WHERE F_ClaPro='"+clave+"'");
                    if(rset.next()){
                        Despro = rset.getString(1);
                    }
                    if(cb == null){
                        cb="0";
                    }
                    if(Despro == null){
                        Despro = "";
                    }
                    if(tarimas>0){
                        for(int x=0; x<tarimas; x++){
                        //con.actualizar("insert into tb_marbetecap values ('"+clave+"','"+Despro+"','"+lote+"','"+cadu+"','"+cb+"','"+tarimas+"','"+cajas+"','"+pcajas+"','0','"+Ptarima+"',0)");    
                        con.actualizar("insert into tb_marbetecap values ('"+clave+"','"+Despro+"','"+lote+"','"+cadu+"','"+cb+"','0','0','0','0','"+Ptarima+"',0)");    
                        }
                    }
                    if(resto > 0){
                        con.actualizar("insert into tb_marbetecap values ('"+clave+"','"+Despro+"','"+lote+"','"+cadu+"','"+cb+"','0','0','0','0','"+resto+"',0)");    
                    }
                    
                    
                con.cierraConexion();

               
                //out.println("<script>window.location='marbete.jsp'</script>");
                out.println("<script>window.open('reimp_marbetecap.jsp?ban=1','_blank')</script>");
            }
            
            if (request.getParameter("accion").equals("generarMedia")) {
                con.conectar();
                int tarimas=0,cajas=0,pcajas=0,tarimasi=0,resto=0,Ptarima=0;
                String cadu="",Despro="",lote="",cb="";
                
                    clave = request.getParameter("clave");
                    lote = request.getParameter("lote");
                    cadu = request.getParameter("cadu");
                    cb = request.getParameter("cb");
                    //tarimas = Integer.parseInt(request.getParameter("tarimas"));
                    //cajas = Integer.parseInt(request.getParameter("cajas"));
                    //pcajas = Integer.parseInt(request.getParameter("pcajas"));
                    //tarimasi = Integer.parseInt(request.getParameter("tarimasi"));
                    resto = Integer.parseInt(request.getParameter("resto"));
                    Ptarima = pcajas * cajas;
                    
                    con.actualizar("delete from tb_marbetecap");
                    
                    ResultSet rset = con.consulta("SELECT F_DesPro FROM tb_medica WHERE F_ClaPro='"+clave+"'");
                    if(rset.next()){
                        Despro = rset.getString(1);
                    }
                    if(cb == null){
                        cb="0";
                    }
                    if(Despro == null){
                        Despro = "";
                    }
                    if(tarimas>0){
                        for(int x=0; x<tarimas; x++){
                        //con.actualizar("insert into tb_marbetecap values ('"+clave+"','"+Despro+"','"+lote+"','"+cadu+"','"+cb+"','"+tarimas+"','"+cajas+"','"+pcajas+"','0','"+Ptarima+"',0)");    
                        con.actualizar("insert into tb_marbetecap values ('"+clave+"','"+Despro+"','"+lote+"','"+cadu+"','"+cb+"','0','0','0','0','"+Ptarima+"',0)");    
                        }
                    }
                    if(resto > 0){
                        con.actualizar("insert into tb_marbetecap values ('"+clave+"','"+Despro+"','"+lote+"','"+cadu+"','"+cb+"','0','0','0','0','"+resto+"',0)");    
                    }
                    
                    
                con.cierraConexion();

               
                //out.println("<script>window.location='marbete.jsp'</script>");
                out.println("<script>window.open('reimp_marbetecap.jsp?ban=2','_blank')</script>");
            }
            
            

        } catch (Exception e) {
        }
        /*request.getSession().setAttribute("folio", request.getParameter("folio"));
        request.getSession().setAttribute("fecha", request.getParameter("fecha"));
        request.getSession().setAttribute("folio_remi", request.getParameter("folio_remi"));
        request.getSession().setAttribute("orden", request.getParameter("orden"));
        request.getSession().setAttribute("provee", request.getParameter("provee"));
        request.getSession().setAttribute("recib", request.getParameter("recib"));
        request.getSession().setAttribute("entrega", request.getParameter("entrega"));
        request.getSession().setAttribute("clave", clave);
        request.getSession().setAttribute("descrip", descr);*/

        //String original = "hello world";
        //byte[] utf8Bytes = original.getBytes("UTF8");
        //String value = new String(utf8Bytes, "UTF-8"); 
        //out.println(value);
        if (ban1 == 0) {
            //out.println("<script>alert('Clave Inexistente')</script>");
            out.println("<script>window.location='marbete.jsp'</script>");
        } else {
            out.println("<script>window.location='marbete.jsp'</script>");
        }
        //response.sendRedirect("captura.jsp");
    }

    public String dame7car(String clave) {
        try {
            int largoClave = clave.length();
            int espacios = 7 - largoClave;
            for (int i = 1; i <= espacios; i++) {
                clave = " " + clave;
            }
        } catch (Exception e) {
        }
        return clave;
    }

    public String dame5car(String clave) {
        try {
            int largoClave = clave.length();
            int espacios = 5 - largoClave;
            for (int i = 1; i <= espacios; i++) {
                clave = " " + clave;
            }
        } catch (Exception e) {
        }
        return clave;
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
