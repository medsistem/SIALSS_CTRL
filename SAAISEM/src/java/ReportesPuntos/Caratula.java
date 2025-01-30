package ReportesPuntos;

import conn.ConectionDB;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpSession;
//import sun.org.mozilla.javascript.internal.ast.Loop;

/**
 * Creación de la caratula para firma de los folios generados
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class Caratula extends HttpServlet {

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
        try {

            String F_FacGNKLAgr = "", Fecha1 = "", Fecha2 = "", Impresora = "", FoliosFact = "", Reportes = "", Folios = "";
            String C1 = "N", C2 = "N", C3 = "N", C4 = "N", C5 = "N", C6 = "N", C7 = "N", C8 = "N";
            String C1_1 = "A", C2_1 = "A", C3_1 = "A", C4_1 = "A", C5_1 = "A", C6_1 = "A", C7_1 = "A", C8_1 = "A";
            int ban = 0, Puntos = 0, Fecha = 0;
            String Fechas = "", Mes = "", Dia = "", AA = "", FecEntrega = "", Punto = "", DesUniIS = "", DesJurIS = "", DesCooIS = "";
            DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            ConectionDB con = new ConectionDB();
            con.conectar();
            HttpSession sesion = request.getSession(true);
            F_FacGNKLAgr = request.getParameter("folios");
            Fecha1 = request.getParameter("fecha_ini1");
            Fecha2 = request.getParameter("fecha_fin1");
            ban = Integer.parseInt(request.getParameter("ban"));
            //Impresora = request.getParameter("impresora");

            //if (Impresora != "") {
            if (ban == 1) {
                if ((Fecha1 != "") && (Fecha2 != "") && (F_FacGNKLAgr != "")) {
                    /*out.println(" <script>window.open('ReportesPuntos/ReporteCaratula.jsp?factura=" + F_FacGNKLAgr + "&ban=1&Impresora=" + Impresora + "', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                     out.println("<script>alert('se Genero Caratula Correctamente.')</script>");
                     out.println("<script>window.history.back()</script>");*/

                    con.actualizar("DELETE FROM tb_caratulasobre;");
                    con.actualizar("DELETE FROM tb_caratulasimp;");
                    ResultSet Consulta2 = con.consulta("SELECT * FROM tb_caratula WHERE F_Fecsur between '" + Fecha1 + "' and '" + Fecha2 + "' AND F_FacGNKLAgr='" + F_FacGNKLAgr + "';");
                    while (Consulta2.next()) {
                        con.insertar("INSERT INTO tb_caratulasimp VALUES('" + Consulta2.getString(1) + "','" + Consulta2.getString(2) + "','" + Consulta2.getString(3) + "','" + Consulta2.getString(4) + "','" + Consulta2.getString(5) + "','" + Consulta2.getString(6) + "','" + Consulta2.getString(7) + "',0);");
                    }

                    ResultSet Consulta = con.consulta("SELECT F_FacGNKLAgr FROM tb_caratulasimp WHERE F_Fecsur between '" + Fecha1 + "' and '" + Fecha2 + "' AND F_FacGNKLAgr='" + F_FacGNKLAgr + "' group by F_FacGNKLAgr");
                    while (Consulta.next()) {
                        FoliosFact = Consulta.getString(1);
                        ResultSet Folio = con.consulta("SELECT F_FacGNKLAgr,F_Folios,F_DesUniIS,F_DesJurIS,F_DesCooIS,DATE_FORMAT(F_Fecsur,'%d/%m/%Y') AS F_Fecsur,F_Puntos FROM tb_caratulasimp WHERE F_FacGNKLAgr='" + FoliosFact + "'");
                        while (Folio.next()) {
                            Folios = Folio.getString(2);
                            DesUniIS = Folio.getString(3);
                            DesJurIS = Folio.getString(4);
                            DesCooIS = Folio.getString(5);
                            Puntos = Folio.getInt(7);
                            Fechas = Folio.getString(6);
                            Reportes = Reportes + Puntos;
                        }

                        Dia = Fechas.substring(0, 2);
                        AA = Fechas.substring(6, 10);
                        Fecha = Integer.parseInt(Fechas.substring(3, 5));
                        System.out.println("fecha" + Fecha);
                        if (Fecha == 1) {
                            Mes = "ENERO";
                        } else if (Fecha == 2) {
                            Mes = "FEBRERO";
                        } else if (Fecha == 3) {
                            Mes = "MARZO";
                        } else if (Fecha == 4) {
                            Mes = "ABRIL";
                        } else if (Fecha == 5) {
                            Mes = "MAYO";
                        } else if (Fecha == 6) {
                            Mes = "JUNIO";
                        } else if (Fecha == 7) {
                            Mes = "JULIO";
                        } else if (Fecha == 8) {
                            Mes = "AGOSTO";
                        } else if (Fecha == 9) {
                            Mes = "SEPTIEMBRE";
                        } else if (Fecha == 10) {
                            Mes = "OCTUBRE";
                        } else if (Fecha == 11) {
                            Mes = "NOVIEMBRE";
                        } else {
                            Mes = "DICIEMBRE";
                        }

                        FecEntrega = Dia + "/" + Mes + "/" + AA;

                        int y = 1;

                        for (int x = 0; x < Reportes.length(); x++) {
                            Punto = Reportes.substring(x, y);
                            y = y + 1;
                            System.out.println("Factura" + FoliosFact + " punto:" + Punto);

                            if (Punto.equals("1")) {
                                C1 = "X";
                                C1_1 = "";
                            } else if (Punto.equals("2")) {
                                C2 = "X";
                                C2_1 = "";
                            } else if (Punto.equals("3")) {
                                C3 = "X";
                                C3_1 = "";
                            } else if (Punto.equals("4")) {
                                C4 = "X";
                                C4_1 = "";
                            } else if (Punto.equals("5")) {
                                C5 = "X";
                                C5_1 = "";
                            } else if (Punto.equals("6")) {
                                C6 = "X";
                                C6_1 = "";
                            } else if (Punto.equals("7")) {
                                C7 = "X";
                                C7_1 = "";
                            } else {
                                C8 = "X";
                                C8_1 = "";
                            }

                        }
                        con.insertar("INSERT INTO tb_caratulasobre VALUES('" + FoliosFact + "','" + Folios + "','" + DesUniIS + "','" + DesJurIS + "','" + DesCooIS + "','" + FecEntrega + "','" + C1 + "','" + C1_1 + "','" + C2 + "','" + C2_1 + "','" + C3 + "','" + C3_1 + "','" + C4 + "','" + C4_1 + "','" + C5 + "','" + C5_1 + "','" + C6 + "','" + C6_1 + "','" + C7 + "','" + C7_1 + "','" + C8 + "','" + C8_1 + "', 0);");
                        //out.println(" <script>window.open('ReportesPuntos/ReporteCaratula.jsp?ban=2&Impresora="+Impresora+"&factura="+FoliosFact+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                        FoliosFact = "";
                        Reportes = "";
                        C1 = "N";
                        C2 = "N";
                        C3 = "N";
                        C4 = "N";
                        C5 = "N";
                        C6 = "N";
                        C7 = "N";
                        C8 = "N";

                        C1_1 = "A";
                        C2_1 = "A";
                        C3_1 = "A";
                        C4_1 = "A";
                        C5_1 = "A";
                        C6_1 = "A";
                        C7_1 = "A";
                        C8_1 = "A";
                    }
                    //out.println(" <script>window.open('ReportesPuntos/ReporteCaratula.jsp?Fecha1="+Fecha1+"&Fecha2="+Fecha2+"&ban=2&Impresora="+Impresora+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                    out.println(" <script>window.open('ReportesPuntos/ReimpCaratula.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                    out.println("<script>alert('se Genero Caratula Correctamente.')</script>");
                    out.println("<script>window.history.back()</script>");
                } else {
                    out.println("<script>alert('No se Genero Caratula.')</script>");
                    out.println("<script>window.history.back()</script>");
                }
            } else if ((Fecha1 != "") && (Fecha2 != "")) {
                con.actualizar("DELETE FROM tb_caratulasobre;");
                con.actualizar("DELETE FROM tb_caratulasimp;");

                ResultSet Consulta2 = con.consulta("SELECT * FROM tb_caratula WHERE F_Fecsur between '" + Fecha1 + "' and '" + Fecha2 + "';");
                while (Consulta2.next()) {
                    con.insertar("INSERT INTO tb_caratulasimp VALUES('" + Consulta2.getString(1) + "','" + Consulta2.getString(2) + "','" + Consulta2.getString(3) + "','" + Consulta2.getString(4) + "','" + Consulta2.getString(5) + "','" + Consulta2.getString(6) + "','" + Consulta2.getString(7) + "',0);");
                }

                ResultSet Consulta = con.consulta("SELECT F_FacGNKLAgr FROM tb_caratulasimp WHERE F_Fecsur between '" + Fecha1 + "' and '" + Fecha2 + "' group by F_FacGNKLAgr");
                while (Consulta.next()) {
                    FoliosFact = Consulta.getString(1);
                    ResultSet Folio = con.consulta("SELECT F_FacGNKLAgr,F_Folios,F_DesUniIS,F_DesJurIS,F_DesCooIS,DATE_FORMAT(F_Fecsur,'%d/%m/%Y') AS F_Fecsur,F_Puntos FROM tb_caratulasimp WHERE F_FacGNKLAgr='" + FoliosFact + "'");
                    while (Folio.next()) {
                        Folios = Folio.getString(2);
                        DesUniIS = Folio.getString(3);
                        DesJurIS = Folio.getString(4);
                        DesCooIS = Folio.getString(5);
                        Puntos = Folio.getInt(7);
                        Fechas = Folio.getString(6);
                        Reportes = Reportes + Puntos;
                    }

                    Dia = Fechas.substring(0, 2);
                    AA = Fechas.substring(6, 10);
                    Fecha = Integer.parseInt(Fechas.substring(3, 5));
                    System.out.println("fecha" + Fecha);
                    if (Fecha == 1) {
                        Mes = "ENERO";
                    } else if (Fecha == 2) {
                        Mes = "FEBRERO";
                    } else if (Fecha == 3) {
                        Mes = "MARZO";
                    } else if (Fecha == 4) {
                        Mes = "ABRIL";
                    } else if (Fecha == 5) {
                        Mes = "MAYO";
                    } else if (Fecha == 6) {
                        Mes = "JUNIO";
                    } else if (Fecha == 7) {
                        Mes = "JULIO";
                    } else if (Fecha == 8) {
                        Mes = "AGOSTO";
                    } else if (Fecha == 9) {
                        Mes = "SEPTIEMBRE";
                    } else if (Fecha == 10) {
                        Mes = "OCTUBRE";
                    } else if (Fecha == 11) {
                        Mes = "NOVIEMBRE";
                    } else {
                        Mes = "DICIEMBRE";
                    }

                    FecEntrega = Dia + "/" + Mes + "/" + AA;

                    int y = 1;

                    for (int x = 0; x < Reportes.length(); x++) {
                        Punto = Reportes.substring(x, y);
                        y = y + 1;
                        System.out.println("Factura" + FoliosFact + " punto:" + Punto);

                        if (Punto.equals("1")) {
                            C1 = "X";
                            C1_1 = "";
                        } else if (Punto.equals("2")) {
                            C2 = "X";
                            C2_1 = "";
                        } else if (Punto.equals("3")) {
                            C3 = "X";
                            C3_1 = "";
                        } else if (Punto.equals("4")) {
                            C4 = "X";
                            C4_1 = "";
                        } else if (Punto.equals("5")) {
                            C5 = "X";
                            C5_1 = "";
                        } else if (Punto.equals("6")) {
                            C6 = "X";
                            C6_1 = "";
                        } else if (Punto.equals("7")) {
                            C7 = "X";
                            C7_1 = "";
                        } else if (Punto.equals("8")) {
                            C8 = "X";
                            C8_1 = "";
                        }

                    }
                    con.insertar("INSERT INTO tb_caratulasobre VALUES('" + FoliosFact + "','" + Folios + "','" + DesUniIS + "','" + DesJurIS + "','" + DesCooIS + "','" + FecEntrega + "','" + C1 + "','" + C1_1 + "','" + C2 + "','" + C2_1 + "','" + C3 + "','" + C3_1 + "','" + C4 + "','" + C4_1 + "','" + C5 + "','" + C5_1 + "','" + C6 + "','" + C6_1 + "','" + C7 + "','" + C7_1 + "','" + C8 + "','" + C8_1 + "', 0);");
                    //out.println(" <script>window.open('ReportesPuntos/ReporteCaratula.jsp?ban=2&Impresora="+Impresora+"&factura="+FoliosFact+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                    FoliosFact = "";
                    Reportes = "";
                    C1 = "N";
                    C2 = "N";
                    C3 = "N";
                    C4 = "N";
                    C5 = "N";
                    C6 = "N";
                    C7 = "N";
                    C8 = "N";

                    C1_1 = "A";
                    C2_1 = "A";
                    C3_1 = "A";
                    C4_1 = "A";
                    C5_1 = "A";
                    C6_1 = "A";
                    C7_1 = "A";
                    C8_1 = "A";
                }
                //out.println(" <script>window.open('ReportesPuntos/ReporteCaratula.jsp?Fecha1="+Fecha1+"&Fecha2="+Fecha2+"&ban=2&Impresora="+Impresora+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                out.println(" <script>window.open('ReportesPuntos/ReimpCaratula.jsp', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
                out.println("<script>alert('se Genero Caratula Correctamente.')</script>");
                out.println("<script>window.history.back()</script>");
            } else {
                out.println("<script>alert('No se Genero Caratula.')</script>");
                out.println("<script>window.history.back()</script>");
            }
            /*} else {
             out.println("<script>alert('Favor de Seleccionar Impresora')</script>");
             out.println("<script>window.history.back()</script>");
             }*/

            //  out.println(" <script>window.open('ReportesPuntos/ReporteHorizontal.jsp?F_Title="+F_Title+"&F_FecIni="+F_FecIni+"&F_FecFin="+F_FecFin+"&F_SecIni="+F_SecIni+"&F_SecFin="+F_SecFin+"&F_Surti="+F_Surti+"&F_Cober="+F_Cober+"&F_Sumi="+F_Sumi+"&F_Cvepro="+F_Cvepro+"&F_DesRegion="+F_DesRegion+"&FolCon="+F_FolCon+"', '', 'width=1200,height=800,left=50,top=50,toolbar=no'); </script>");
            con.cierraConexion();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        /*String folio_sp = request.getParameter("sp_pac");
         System.out.println(folio_sp);
         out.println(folio_sp);*/
    }

    public static void main(String[] args) {

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
