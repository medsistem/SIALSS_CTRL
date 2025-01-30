/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Inventario;

import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Proceso de inventario ciclico
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class JQInvenCiclico extends HttpServlet {

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
        ConectionDB con = new ConectionDB();
        try {
            try {
                if (request.getParameter("accion").equals("GuardarInsumo")) {
                    con.conectar();
                    String F_IdLote = "";
                    int FolCant = 0;
                    int ncant = Integer.parseInt(request.getParameter("F_Total"));
                    ResultSet rset = con.consulta("select F_IdLote, F_ExiLot from tb_loteinv where F_ClaPro = '" + request.getParameter("F_ClaPro") + "' and F_ClaLot = '" + request.getParameter("F_ClaLot") + "' and F_FecCad = STR_TO_Date('" + request.getParameter("F_FecCad") + "', '%d/%m/%Y') and F_Ubica = '" + request.getParameter("F_ClaUbi") + "' group by F_FolLot");
                    while (rset.next()) {
                        F_IdLote = rset.getString("F_IdLote");
                        FolCant = rset.getInt("F_ExiLot");
                    }

                    if (!F_IdLote.equals("")) {
                        con.insertar("update tb_loteinv set F_ExiLot = '" + (ncant + FolCant) + "', F_FolLot='" + request.getParameter("F_Presentacion") + "' where F_IdLote = '" + F_IdLote + "'");
                    } else {
                        con.insertar("insert into tb_loteinv values (0,'" + request.getParameter("F_ClaPro") + "','" + request.getParameter("F_ClaLot") + "',STR_TO_DATE('" + request.getParameter("F_FecCad") + "', '%d/%m/%Y'),'" + (ncant) + "','" + request.getParameter("F_Presentacion") + "','1','" + request.getParameter("F_ClaUbi") + "','2014-01-01','1111111','1')");
                    }
                    con.cierraConexion();

                } else if (request.getParameter("accion").equals("buscaDescrip")) {
                    con.conectar();
                    JSONObject json = new JSONObject();
                    JSONArray jsona = new JSONArray();
                    ResultSet rset = con.consulta("select F_DesPro from tb_medica where F_DesPro like '%" + request.getParameter("descrip") + "%' limit 0,50");
                    while (rset.next()) {
                        json.put("DesPro", rset.getString(1).trim().replaceAll("\\n", ""));
                        jsona.add(json);
                        json = new JSONObject();
                    }
                    con.cierraConexion();
                    out.println(jsona);
                    System.out.println(jsona);

                    /*Autocompletar de redistribucion*/
                } else if (request.getParameter("accion").equals("buscaClaUbi")) {
                    HttpSession sesion = request.getSession();
                    con.conectar();
                    String UbiCrossd = "";
                    JSONObject json = new JSONObject();
                    JSONArray jsona = new JSONArray();
                    //String Origen = request.getParameter("Origen");
                    String user = (String) sesion.getAttribute("Usuario");

                    ResultSet rsetUbica = con.consulta("SELECT * FROM tb_ubicacrosdock;");
                    if (rsetUbica.next()) {
                        UbiCrossd = rsetUbica.getString(1);
                    }
                    ResultSet rset;
                    //System.out.println("Origen=" + Origen);
                    if (!user.equals("RCuellarB") && !user.equals("ICuellarB")) {
                        rset = con.consulta("select F_ClaUbi from tb_ubica where F_ClaUbi like '%" + request.getParameter("descrip") + "%' AND F_ClaUbi NOT IN ('NUEVATMP','CONTROLADOTMP','VACUNASTMP','GNKVACUNAS','GNKCONTROLADO','MERE-CTRL','NUEVAMERE-CTRL') limit 0,10");

                    } else {
                        rset = con.consulta("select F_ClaUbi from tb_ubica where F_ClaUbi like '%" + request.getParameter("descrip") + "%' AND F_ClaUbi NOT IN (" + UbiCrossd + ") limit 0,10");
                    }
                    while (rset.next()) {
                        json.put("F_ClaUbi", rset.getString(1));
                        jsona.add(json);
                        json = new JSONObject();
                    }
                    con.cierraConexion();
                    out.println(jsona);
                    System.out.println(jsona);


                    /*Autocompletar de redistribucion*/
                } else if (request.getParameter("accion").equals("buscaClaUbi2")) {
                    HttpSession sesion = request.getSession();
                    con.conectar();
                    String UbiCrossd = "";
                    String user = (String) sesion.getAttribute("Usuario");
                    JSONObject json = new JSONObject();
                    JSONArray jsona = new JSONArray();
                    ResultSet rsetUbica = con.consulta("SELECT * FROM tb_ubicacrosdock;");
                    if (rsetUbica.next()) {
                        UbiCrossd = rsetUbica.getString(1);
                    }
                    ResultSet rset;
                    System.out.println("User: "+user);
                    if (!user.equals("RCuellarB") && !user.equals("ICuellarB")) {
                        rset = con.consulta("select F_DesUbi from tb_ubica where F_DesUbi like '%" + request.getParameter("descrip") + "%' AND F_DesUbi NOT IN ('NUEVA-TMP','CONTROLADO-TMP','VACUNAS-TMP','GNK-VACUNAS','GNK-CONTROLADO','MERE-CTRL','NUEVAMERE-CTRL') limit 0,10");

                    } else {
                       rset = con.consulta("select F_DesUbi from tb_ubica where F_DesUbi like '%" + request.getParameter("descrip") + "%' AND F_DesUbi NOT IN (" + UbiCrossd + ") limit 0,10");
                    }
                    while (rset.next()) {
                        json.put("F_ClaUbi2", rset.getString(1));
                        jsona.add(json);
                        json = new JSONObject();
                    }
                    con.cierraConexion();
                    out.println(jsona);
                    System.out.println(jsona);



                }else if (request.getParameter("accion").equals("buscaClaUbiCross")) {
                    con.conectar();
                    String UbiCross = "";
                    JSONObject json = new JSONObject();
                    JSONArray jsona = new JSONArray();
                    ResultSet rsetUbica = con.consulta("SELECT * FROM tb_ubicacrosdock;");
                    if (rsetUbica.next()) {
                        UbiCross = rsetUbica.getString(1);
                    }
                    ResultSet rset = con.consulta("SELECT F_ClaUbi from tb_ubica where F_ClaUbi like '%" + request.getParameter("descrip") + "%' AND  F_ClaUbi IN (" + UbiCross + ") limit 0,10");
                    while (rset.next()) {
                        json.put("F_ClaUbi", rset.getString(1));
                        jsona.add(json);
                        json = new JSONObject();
                    }
                    con.cierraConexion();
                    out.println(jsona);
                    System.out.println(jsona);
                } else if (request.getParameter("accion").equals("BuscarUbi")) {
                    con.conectar();
                    JSONObject json = new JSONObject();
                    JSONArray jsona = new JSONArray();
                    ResultSet rset = con.consulta("select F_ClaUbi from tb_ubica where F_CB= '" + request.getParameter("buscarUbi") + "'");
                    while (rset.next()) {
                        json.put("F_ClaUbi", rset.getString(1));
                        jsona.add(json);
                        json = new JSONObject();
                        ResultSet rset2 = con.consulta("select F_ClaPro, F_ClaLot, DATE_FORMAT(F_FecCad, '%d/%m/%Y') as F_FecCad from tb_loteinv where F_Ubica = '" + rset.getString("F_ClaUbi") + "'");
                        while (rset2.next()) {
                            json.put("F_ClaPro", rset2.getString("F_ClaPro"));
                            json.put("F_ClaLot", rset2.getString("F_ClaLot"));
                            json.put("F_FecCad", rset2.getString("F_FecCad"));
                            jsona.add(json);
                            json = new JSONObject();
                        }
                    }
                    con.cierraConexion();
                    out.println(jsona);
                    System.out.println(jsona);
                } else if (request.getParameter("accion").equals("BuscarCBMed")) {/*
                    con.conectar();
                    JSONObject json = new JSONObject();
                    JSONArray jsona = new JSONArray();
                    ResultSet rset = con.consulta("select F_Ubica from tb_lote where F_Cb= '" + request.getParameter("F_CBMed") + "' group by F_Ubica");
                    while (rset.next()) {
                        json.put("F_ClaUbi", rset.getString(1));
                        jsona.add(json);
                        json = new JSONObject();
                    }

                    //out.println(jsona);
                    rset = con.consulta("select F_ClaPro, F_ClaLot, DATE_FORMAT(F_FecCad, '%d/%m/%Y') as F_FecCad from tb_loteinv where F_Cb = '" + request.getParameter("F_CBMed") + "' group by F_ClaPro, F_ClaLot, F_FecCad");
                    while (rset.next()) {
                        json.put("F_ClaPro", rset.getString("F_ClaPro"));
                        json.put("F_ClaLot", rset.getString("F_ClaLot"));
                        json.put("F_FecCad", rset.getString("F_FecCad"));
                        jsona.add(json);
                        json = new JSONObject();
                    }
                    con.cierraConexion();
                    out.println(jsona);
                    System.out.println(jsona);*/
                } else if (request.getParameter("accion").equals("BuscarUnidad")) {
                    con.conectar();
                    JSONObject json = new JSONObject();
                    JSONArray jsona = new JSONArray();
                    ResultSet rset = con.consulta("select F_ClaCli, F_NomCli from tb_uniatn where F_NomCli like '%" + request.getParameter("nom_uni") + "%' or F_ClaCli like '%" + request.getParameter("nom_uni") + "%' limit 0,10");
                    while (rset.next()) {
                        json.put("nom_com", rset.getString(1).trim().replaceAll("\\n", "") + " - " + rset.getString(2).trim().replaceAll("\\n", ""));
                        //json.put("F_NomCli", rset.getString(2).trim().replaceAll("\\n", ""));
                        jsona.add(json);
                        json = new JSONObject();
                    }
                    con.cierraConexion();
                    out.println(jsona);
                    System.out.println(jsona);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } finally {
            out.close();
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
