/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import conn.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Alta y modificación de catálogo de unidades
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class AltaUnidad extends HttpServlet {

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
        ConectionDB con = new ConectionDB();

        java.text.DateFormat df2 = new java.text.SimpleDateFormat("dd/MM/yyyy");
        java.text.DateFormat df3 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession sesion = request.getSession(true);

        try {
            if (request.getParameter("accion").equals("guardar")) {
                String Clave = request.getParameter("Clave");
                byte[] a = request.getParameter("Nombre").getBytes("ISO-8859-1");
                String Nombre = (new String(a, "UTF-8")).toUpperCase();
                byte[] d = request.getParameter("Direccion").getBytes("ISO-8859-1");
                String Direc = (new String(d, "UTF-8")).toUpperCase();
                //int Mun = Integer.parseInt(request.getParameter("mun"));
                int Juris = Integer.parseInt(request.getParameter("juris"));
                String Tipo = request.getParameter("tipo");
                String dispensador = request.getParameter("dispensador");
                String Proyecto = request.getParameter("Proyecto");
                String ClaJur = "";
                int Contar = 0;
                try {
                    con.conectar();
                    int Mun = 0;
                    String Jurisdiccion = "";

                    ResultSet rset1 = con.consulta("SELECT COUNT(F_ClaCli) FROM tb_uniatn WHERE F_ClaCli='" + Clave + "';");
                    while (rset1.next()) {
                        Contar = rset1.getInt(1);
                    }
                    if (Contar > 0) {
                        out.println("<script>alert('Clave de la Unidad Existente')</script>");
                        out.println("<script>window.history.back()</script>");
                    } else {
                        rset1 = con.consulta("SELECT F_ClaMunIS,F_JurMunIS FROM tb_muniis WHERE F_ClaMunIS=" + Juris + ";");
                        while (rset1.next()) {
                            Mun = rset1.getInt(1);
                            Jurisdiccion = rset1.getString(2);
                        }
                        ClaJur = "I";

                        if (!(Tipo == "") && (!(Proyecto == ""))) {
                            con.insertar("INSERT INTO tb_uniatn VALUES('" + Clave + "','" + Nombre + "','A','" + Jurisdiccion + "','" + Juris + "','" + Tipo + "','" + Mun + "','" + Direc + "','R1001','" + dispensador + "','','','" + Proyecto + "','','1');");
                            con.insertar("INSERT INTO tb_fecharuta VALUES('" + Clave + "',CURDATE(),'R1001','Z01',0);");
                            response.sendRedirect("catalogoUnidades.jsp");
                        } else {
                            out.println("<script>alert('Seleccione Tipo Unidad O Proyecto')</script>");
                            out.println("<script>window.history.back()</script>");
                        }

                    }
                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (request.getParameter("accion").equals("Modificar")) {
                String Clave = request.getParameter("Clave1");
                byte[] a = request.getParameter("Nombre1").getBytes("ISO-8859-1");
                String Nombre = (new String(a, "UTF-8")).toUpperCase();
                byte[] s = request.getParameter("Sts1").getBytes("ISO-8859-1");
                String Sts = (new String(s, "UTF-8")).toUpperCase();
                byte[] d = request.getParameter("Direc1").getBytes("ISO-8859-1");
                String Direc = (new String(d, "UTF-8")).toUpperCase();
                String Tipo = request.getParameter("TipoU");
                String dispensador = request.getParameter("Dispensa");
                try {
                    con.conectar();
                    if (!(Clave == "")) {
                        if ((Sts.equals("A")) || (Sts.equals("S"))) {
                            if (!(Nombre == "")) {
                                if (!(Nombre == "")) {
                                    con.actualizar("UPDATE tb_uniatn SET F_NomCli='" + Nombre + "',F_StsCli='" + Sts + "', F_Direc='" + Direc + "',F_Tipo='" + Tipo + "',F_Dispen='" + dispensador + "' WHERE F_ClaCli='" + Clave + "'");
                                    response.sendRedirect("catalogoUnidades.jsp");
                                } else {
                                    out.println("<script>alert('Ingrese Datos En el campo Dirección')</script>");
                                    out.println("<script>window.history.back()</script>");
                                }
                            } else {
                                out.println("<script>alert('Ingrese Datos En el campo Nombre')</script>");
                                out.println("<script>window.history.back()</script>");
                            }
                        } else {
                            out.println("<script>alert('Ingrese Datos Correctos en el campo Sts es: A o S')</script>");
                            out.println("<script>window.history.back()</script>");
                        }
                    } else {
                        out.println("<script>alert('Ingrese Datos')</script>");
                        out.println("<script>window.history.back()</script>");
                    }
                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
        } catch (Exception e) {
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
