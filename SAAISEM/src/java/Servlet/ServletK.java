/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import conn.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Class toma de inventario
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class ServletK extends HttpServlet {

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

        conection ObjMySQL = new conection();
//        ConectionDBInv ObjInv = new ConectionDBInv();

        String Query;
        ResultSet Consultas = null;

        try {
            processRequest(request, response);
            PrintWriter out = response.getWriter();
            HttpSession sesion = request.getSession(true);
            String Folio = "", Ubicacion = "", QueryDatos = "", QueryDatosSQL = "", Id = "", Fabricacion = "", CB = "", PiezasL = "", an1 = "", mes = "", dia = "", F_Usu = "", F_nombre = "", F_TipUsu = "";
            String Origen = "", Caduca = "", IdFact = "", ClaCli = "", FecEnt = "", StsFact = "", User2 = "";
            int ban, posi = 0, CajasM = 0, RestoM = 0, Piezas = 0, Existencia = 0, Cantidad = 0, CantidadM = 0, Resultado = 0, Diferencia = 0, ban2 = 0, CajasN = 0, x = 0;
            int posiid = 0, Org = 0, Marca = 0, TipoM = 0, FolioL = 0, FolioLote = 0, ContSql = 0, Cont = 0, FCont = 0, ann1 = 0, difeann1 = 0, FolMov = 0, FolMovi = 0, posi1 = 0, banusu = 0, Existencias = 0, Existencias2 = 0, TotalExi = 0;
            double Costo = 0.0, Monto = 0.0, Iva = 0.0, IvaT = 0.0, MontoT = 0.0;
            double ExistenciaSql = 0.0, ExistenciaSql2 = 0.0, TotalExiSql = 0.0, RestoMSql = 0.0, ResultadoSql = 0.0;
            int exifact = 0, Difexifact = 0, Difexifact1 = 0, exifactMov = 0, UbicaFol = 0, IdMov = 0;
            ResultSet Consulta = null;
            ResultSet ConsultaSQL = null;

            /////****VARIABLES COMPARACIÓN****//////
            ResultSet ConsultaUbi = null;
            ResultSet ConsultaComp = null;
            ResultSet ConsultaCompS = null;
            ResultSet ConsultaCompU = null;
            String QueryUbi = "", ClaveU = "", QueryComp = "", ClaveC = "", QueryCompS = "", CantidadS = "", QueryCompU = "", CantidadUB = "", Cadu = "";
            String LoteU = "", CaduU = "", LoteC = "", CaduC = "";
            int ResultadoU = 0, CantidadU = 0, conts = 0, CantidadSG = 0, contu = 0, CantidadUBI = 0;

            /////****
            ban = Integer.parseInt(request.getParameter("ban"));
            String Usuario = request.getParameter("usuario");
            String Usuario2 = request.getParameter("usua");
            String Pass = request.getParameter("password");
            String Cadena = request.getParameter("folio");
            String Folio1 = request.getParameter("folio1");
            String Folio2 = request.getParameter("folio2");
            String Fecha = request.getParameter("fecha");
            String Cajas = request.getParameter("cajasm");
            String Resto = request.getParameter("restom");
            String Ubinew = request.getParameter("ubin");
            String Clave = request.getParameter("clave");
            String Lote = request.getParameter("lote");
            String Caducidad = request.getParameter("caducidad");
            String Caducidad2 = request.getParameter("caducidad");
            String Pzcj = request.getParameter("piezas");
            String Unidad = request.getParameter("nombre");
            String Provee = request.getParameter("proveedor");
            String CBM = request.getParameter("cb");
            String MarcaM = request.getParameter("marca");
            String CaduNew = request.getParameter("cad");
            String LoteNew = request.getParameter("lotenew");
            String Nombren = request.getParameter("nombre2");
            String Passn = request.getParameter("pass");
            String foliom = request.getParameter("Foliou");
            String ubicam = request.getParameter("Ubica");
            String idm = request.getParameter("Id");
            String nombrem = request.getParameter("Nombreu");
            String usuariom = request.getParameter("Usuariou");
            String tipom = request.getParameter("Tipou");
            HttpSession Session = request.getSession(true);

            ObjMySQL.conectar();
//            ObjInv.conectar();

            out.println(ban);

            switch (ban) {
                case 1:
                    Query = "SELECT F_Usu,F_Nombre,F_TipUsu FROM tb_usuario WHERE F_Usu='" + Usuario + "' AND F_Pass=PASSWORD('" + Pass + "') AND F_Status='A'";
                    System.out.println(Query);
                    Consultas = ObjMySQL.consulta(Query);
                    if (Consultas.next()) {

                        String Usuarios = Consultas.getString("F_Usu");
                        String Nombre = Consultas.getString("F_Nombre");
                        String Tipo = Consultas.getString("F_TipUsu");
                        Session.setAttribute("Valida", "Valido");
                        Session.setAttribute("Usuario", Usuarios);
                        Session.setAttribute("Nombre", Nombre);
                        Session.setAttribute("Tipo", Tipo);
                        if ((Tipo.equals("3")) || (Tipo.equals("4"))) {
                            response.sendRedirect("Ubicaciones/Consultas.jsp");
                        } else {
                            response.sendRedirect("index.jsp");
                        }
                    } else {
                        response.sendRedirect("index.jsp");
                    }
                    break;
                case 2:
                    posi = Cadena.indexOf(':');
                    posiid = Cadena.lastIndexOf(';');
                    posi1 = Cadena.indexOf('/');
                    Folio = Cadena.substring(0, posi);
                    Ubicacion = Cadena.substring(posi + 1, posiid);
                    Id = Cadena.substring(posiid + 1, posi1);
                    ban2 = Integer.parseInt(Cadena.substring(posi1 + 1));
                    if (ban2 == 2) {
                        Session.setAttribute("folio", Folio);
                        Session.setAttribute("ubicacion", Ubicacion);
                        Session.setAttribute("id", Id);
                        response.sendRedirect("Ubicaciones/Redistribucion.jsp");
                    } else if (ban2 == 3) {
                        Session.setAttribute("folio", Folio);
                        Session.setAttribute("ubicacion", Ubicacion);
                        Session.setAttribute("id", Id);
                        //response.sendRedirect("Ubicaciones/Modificacion.jsp");                        
                        response.sendRedirect("Ubicaciones/indexValida.jsp");
                    }
                    break;

                case 3:

                    Folio = (String) Session.getAttribute("folio");
                    Ubicacion = (String) Session.getAttribute("ubicacion");
                    Id = (String) Session.getAttribute("id");
                    Usuario = (String) Session.getAttribute("Usuario");
                    //out.println(Folio+"/"+Ubicacion+"/"+Usuario);

                    QueryDatos = "SELECT SUM(F_Cant) AS F_Cant from tb_facttemp where F_StsFact <= '4' AND F_IdLot='" + Id + "' group by F_IdLot";
                    Consulta = ObjMySQL.consulta(QueryDatos);
                    if (Consulta.next()) {
                        exifact = Integer.parseInt(Consulta.getString("F_Cant"));
                    }

                    QueryDatos = "SELECT L.F_ClaPro AS F_ClaPro,L.F_ClaLot AS F_ClaLot,L.F_FecCad AS F_FecCad,L.F_ClaOrg AS F_ClaOrg,L.F_FecFab AS F_FecFab,L.F_Cb AS F_Cb,L.F_ClaMar AS F_ClaMar,M.F_Costo AS F_Costo,M.F_TipMed AS F_TipMed,L.F_ExiLot as cant FROM tb_lote L INNER JOIN tb_medica M ON L.F_ClaPro=M.F_ClaPro where L.F_ExiLot>0 AND L.F_IdLote='" + Id + "' AND L.F_FolLot='" + Folio + "' AND L.F_Ubica='" + Ubicacion + "'";
                    Consulta = ObjMySQL.consulta(QueryDatos);
                    if (Consulta.next()) {
                        Clave = Consulta.getString("F_ClaPro");
                        Lote = Consulta.getString("F_ClaLot");
                        Caducidad = Consulta.getString("F_FecCad");
                        Org = Integer.parseInt(Consulta.getString("F_ClaOrg"));
                        Fabricacion = Consulta.getString("F_FecFab");
                        CB = Consulta.getString("F_Cb");
                        Marca = Integer.parseInt(Consulta.getString("F_ClaMar"));
                        Costo = Double.parseDouble(Consulta.getString("F_Costo"));
                        TipoM = Integer.parseInt(Consulta.getString("F_TipMed"));
                        Existencia = Integer.parseInt(Consulta.getString("cant"));

                    }
                    if (TipoM == 2504) {
                        Iva = 0.0;
                    } else {
                        Iva = 0.16;
                    }
                    if (Resto != "") {
                        RestoM = Integer.parseInt(Resto);
                        CantidadM = RestoM;
                    }

                    QueryDatos = "select F_ExiLot as EXI,F_IdLote from tb_lote where F_FolLot='" + Folio + "' AND F_Ubica='" + Ubinew + "'";
                    Consulta = ObjMySQL.consulta(QueryDatos);
                    if (Consulta.next()) {
                        Cantidad = Integer.parseInt(Consulta.getString("EXI"));
                        PiezasL = Consulta.getString("EXI");
                        UbicaFol = Integer.parseInt(Consulta.getString("F_IdLote"));
                    }

                    Diferencia = Existencia - CantidadM;
                    IvaT = (CantidadM * Costo) * Iva;
                    Monto = CantidadM * Costo;
                    MontoT = Monto + IvaT;

                    if (!(PiezasL.equals(""))) {

                        Resultado = CantidadM + Cantidad;
                        ObjMySQL.actualizar("update tb_lote set F_ExiLot='" + Resultado + "' WHERE F_FolLot='" + Folio + "' AND F_Ubica='" + Ubinew + "'");

                        if ((CantidadM >= exifact)) {

                            /*QueryDatos = "SELECT F_IdFact,F_ClaCli,F_FecEnt,F_StsFact,F_User,F_Cant,F_IdLot from tb_facttemp where F_StsFact<='4' AND F_IdLot='"+UbicaFol+"'";
                             Consulta = ObjMySQL.consulta(QueryDatos);
                             while (Consulta.next()) {
                             Cont ++;
                             Difexifact = Integer.parseInt(Consulta.getString("F_Cant"));
                             IdMov = Integer.parseInt(Consulta.getString("F_IdLot"));
                             IdFact = Consulta.getString("F_IdFact");
                             ClaCli = Consulta.getString("F_ClaCli");
                             FecEnt = Consulta.getString("F_FecEnt");
                             StsFact = Consulta.getString("F_StsFact");
                             User2 = Consulta.getString("F_User");
                             
                             if (Cont > 0){
                                 
                             for (int z=0; z<Cont; z++){
                                     
                             Cantidad = CantidadM - Difexifact;    
                                     
                             ObjMySQL.actualizar("UPDATE tb_facttemp SET F_IdLot='"+UbicaFol+"',F_Cant='"+Cantidad+"' where F_StsFact <= '4' AND F_IdLot='"+UbicaFol+"' AND F_IdFact='"+IdFact+"' AND F_ClaCli='"+ClaCli+"' AND F_FecEnt='"+FecEnt+"' ");
                             ObjMySQL.actualizar("DELETE FROM tb_facttemp where F_StsFact <= '4' AND F_IdLot='"+Id+"'");    
                             }
                                
                             }else{*/
                            ObjMySQL.actualizar("UPDATE tb_facttemp SET F_IdLot='" + UbicaFol + "' where F_StsFact <= '4' AND F_IdLot='" + Id + "'");
                            /* }
                             
                             }
                            
                             */

                        } else if (!(Diferencia > exifact)) {
                            exifactMov = exifact - CantidadM;

                            QueryDatos = "SELECT F_IdFact,F_Id,F_ClaCli,F_FecEnt,F_StsFact,F_User,F_Cant from tb_facttemp where F_StsFact<='4' AND F_IdLot='" + Id + "'";
                            Consulta = ObjMySQL.consulta(QueryDatos);
                            while (Consulta.next()) {
                                Cont++;
                                Difexifact = Integer.parseInt(Consulta.getString("F_Cant"));
                                IdMov = Integer.parseInt(Consulta.getString("F_Id"));
                                IdFact = Consulta.getString("F_IdFact");
                                ClaCli = Consulta.getString("F_ClaCli");
                                FecEnt = Consulta.getString("F_FecEnt");
                                StsFact = Consulta.getString("F_StsFact");
                                User2 = Consulta.getString("F_User");

                                if (CantidadM > Difexifact) {
                                    ObjMySQL.actualizar("UPDATE tb_facttemp SET F_IdLot='" + UbicaFol + "' where F_StsFact <= '4' AND F_Id='" + IdMov + "' ");

                                } else {
                                    if (CantidadM > 0) {
                                        Difexifact1 = Difexifact - CantidadM;

                                        ObjMySQL.actualizar("UPDATE tb_facttemp SET F_cant='" + Difexifact1 + "' where F_StsFact <= '4' AND F_Id='" + IdMov + "'");

                                        ObjMySQL.actualizar("insert into tb_facttemp  values ('" + IdFact + "','" + ClaCli + "','" + UbicaFol + "','" + CantidadM + "','" + FecEnt + "','" + StsFact + "',0,'" + User2 + "')");
                                    }
                                }
                                CantidadM = CantidadM - Difexifact;
                            }

                        }

                    } else {
                        System.out.println(Diferencia + "/" + Folio + "/" + Ubinew + "/" + CantidadM);
                        ObjMySQL.actualizar("insert into tb_lote values(0,'" + Clave + "','" + Lote + "','" + Caducidad + "','" + CantidadM + "','" + Folio + "','" + Org + "','" + Ubinew + "','" + Fabricacion + "','" + CB + "','" + Marca + "')");

                        QueryDatos = "select F_ExiLot as EXI,F_IdLote from tb_lote where F_FolLot='" + Folio + "' AND F_Ubica='" + Ubinew + "'";
                        Consulta = ObjMySQL.consulta(QueryDatos);
                        if (Consulta.next()) {
                            Cantidad = Integer.parseInt(Consulta.getString("EXI"));
                            PiezasL = Consulta.getString("EXI");
                            UbicaFol = Integer.parseInt(Consulta.getString("F_IdLote"));
                        }

                        if ((CantidadM >= exifact)) {

                            /*QueryDatos = "SELECT F_IdFact,F_ClaCli,F_FecEnt,F_StsFact,F_User,F_Cant,F_IdLot from tb_facttemp where F_StsFact<='4' AND F_IdLot='"+UbicaFol+"'";
                             Consulta = ObjMySQL.consulta(QueryDatos);
                             while (Consulta.next()) {
                             Cont ++;
                             Difexifact = Integer.parseInt(Consulta.getString("F_Cant"));
                             IdMov = Integer.parseInt(Consulta.getString("F_IdLot"));
                             IdFact = Consulta.getString("F_IdFact");
                             ClaCli = Consulta.getString("F_ClaCli");
                             FecEnt = Consulta.getString("F_FecEnt");
                             StsFact = Consulta.getString("F_StsFact");
                             User2 = Consulta.getString("F_User");
                             
                             if (Cont > 0){
                                 
                             for (int z=0; z<Cont; z++){
                                     
                             Cantidad = CantidadM - Difexifact;    
                                     
                             ObjMySQL.actualizar("UPDATE tb_facttemp SET F_IdLot='"+UbicaFol+"',F_Cant='"+Cantidad+"' where F_StsFact <= '4' AND F_IdLot='"+UbicaFol+"' AND F_IdFact='"+IdFact+"' AND F_ClaCli='"+ClaCli+"' AND F_FecEnt='"+FecEnt+"' ");
                             ObjMySQL.actualizar("DELETE FROM tb_facttemp where F_StsFact <= '4' AND F_IdLot='"+Id+"'");    
                             }
                                
                             }else{*/
                            ObjMySQL.actualizar("UPDATE tb_facttemp SET F_IdLot='" + UbicaFol + "' where F_StsFact <= '4' AND F_IdLot='" + Id + "'");
                            /* }
                             
                             }
                            
                             */

                        } else if (!(Diferencia > exifact)) {
                            exifactMov = exifact - CantidadM;

                            QueryDatos = "SELECT F_IdFact,F_Id,F_ClaCli,F_FecEnt,F_StsFact,F_User,F_Cant from tb_facttemp where F_StsFact<='4' AND F_IdLot='" + Id + "'";
                            Consulta = ObjMySQL.consulta(QueryDatos);
                            Consulta.last();
                            int renConsulta = Consulta.getRow();
                            Consulta.first();
                            while (Consulta.next()) {
                                Cont++;
                                Difexifact = Integer.parseInt(Consulta.getString("F_Cant"));
                                IdMov = Integer.parseInt(Consulta.getString("F_Id"));
                                IdFact = Consulta.getString("F_IdFact");
                                ClaCli = Consulta.getString("F_ClaCli");
                                FecEnt = Consulta.getString("F_FecEnt");
                                StsFact = Consulta.getString("F_StsFact");
                                User2 = Consulta.getString("F_User");

                                if (CantidadM > Difexifact) {
                                    ObjMySQL.actualizar("UPDATE tb_facttemp SET F_IdLot='" + UbicaFol + "' where F_StsFact <= '4' AND F_Id='" + IdMov + "' ");

                                } else {
                                    if (CantidadM > 0) {
                                        Difexifact1 = Difexifact - CantidadM;

                                        ObjMySQL.actualizar("UPDATE tb_facttemp SET F_cant='" + Difexifact1 + "' where F_StsFact <= '4' AND F_Id='" + IdMov + "'");

                                        ObjMySQL.actualizar("insert into tb_facttemp  values ('" + IdFact + "','" + ClaCli + "','" + UbicaFol + "','" + CantidadM + "','" + FecEnt + "','" + StsFact + "',0,'" + User2 + "')");
                                    }
                                }
                                if (Consulta.getRow() < renConsulta - 1) {
                                    CantidadM = CantidadM - Difexifact;
                                }
                            }

                        }

                    }
                    if (CantidadM < 0) {
                        CantidadM = CantidadM * (-1);
                    }
                    ObjMySQL.actualizar("insert into tb_movinv values(0,curdate(),'0','1000','" + Clave + "','" + CantidadM + "','" + Costo + "','" + MontoT + "','-1','" + Folio + "','" + Ubicacion + "','" + Org + "',curtime(),'" + Usuario + "','')");
                    ObjMySQL.actualizar("insert into tb_movinv values(0,curdate(),'0','1000','" + Clave + "','" + CantidadM + "','" + Costo + "','" + MontoT + "','1','" + Folio + "','" + Ubinew + "','" + Org + "',curtime(),'" + Usuario + "','')");

                    if (Diferencia == 0) {

                        ObjMySQL.actualizar("update tb_lote set F_ExiLot='0' where F_IdLote='" + Id + "' AND F_FolLot='" + Folio + "' AND F_Ubica='" + Ubicacion + "'");
                        response.sendRedirect("Ubicaciones/Consultas.jsp");
                    } else {
                        ObjMySQL.actualizar("update tb_lote set F_ExiLot='" + Diferencia + "' where F_IdLote='" + Id + "' AND F_FolLot='" + Folio + "' AND F_Ubica='" + Ubicacion + "'");
                        Session.setAttribute("folio", Folio);
                        Session.setAttribute("ubicacion", Ubicacion);
                        Session.setAttribute("id", Id);
                        response.sendRedirect("Ubicaciones/Redistribucion.jsp");
                    }

                    break;
                case 4:
                    Session.setAttribute("clave", Clave);
                    Session.setAttribute("lote", Lote);
                    Session.setAttribute("caducidad", Caducidad);
                    Session.setAttribute("ubicacion", Ubinew);
                    Session.setAttribute("piezas", Pzcj);
                    Session.setAttribute("cajas", Cajas);
                    Session.setAttribute("resto", Resto);
                    Session.setAttribute("ban", "1");
                    response.sendRedirect("jsp/IngresoM.jsp");
                    break;
                case 5:

                    posi = Cadena.indexOf('/');
                    Folio = Cadena.substring(0, posi);
                    ban2 = Integer.parseInt(Cadena.substring(posi + 1));

                    if (ban2 == 1) {

                    } else if (ban2 == 2) {
                        Session.setAttribute("folio", Folio);
                        response.sendRedirect("Eliminar.jsp");
                    }

                    break;
                case 6:
                    Session.setAttribute("folio", Cadena);
                    Session.setAttribute("ban", "2");
                    response.sendRedirect("jsp/IngresoM.jsp");
                    break;
                case 7:
                    Query = "SELECT * from tb_marbetes_cajas where F_Fecha=CURDATE() and F_NomUni='" + Unidad + "' and F_Folio='" + Cadena + "' and F_Paq='" + Cajas + "'";
                    Consultas = ObjMySQL.consulta(Query);
                    if (Consultas.next()) {
                        posi++;
                    }
                    if (posi == 0) {
                        CajasN = Integer.parseInt(Cajas);
                        for (x = 1; x <= CajasN; x++) {
                            out.println(x);
                            ObjMySQL.actualizar("Insert into tb_marbetes_cajas values('" + Unidad + "','" + Cadena + "','" + Cajas + "','" + x + "',curdate(),0)");
                        }
                        Session.setAttribute("nombre", Unidad);
                        Session.setAttribute("cajas", Cajas);
                        Session.setAttribute("folio", Cadena);
                        response.sendRedirect("Ubicaciones/Marbete.jsp");
                    } else {
                        Session.setAttribute("nombre", Unidad);
                        Session.setAttribute("cajas", Cajas);
                        Session.setAttribute("folio", Cadena);
                        response.sendRedirect("Ubicaciones/Marbete.jsp");
                    }
                    break;
                case 8:
                    if (Folio1 != "" && Folio2 != "" && Fecha != "") {
                        Session.setAttribute("folio1", Folio1);
                        Session.setAttribute("folio2", Folio2);
                        Session.setAttribute("fecha", Fecha);
                        Session.setAttribute("ban", "1");
                        response.sendRedirect("MarbeteT.jsp");

                    } else {
                        Session.setAttribute("folio1", Folio1);
                        Session.setAttribute("folio2", Folio2);
                        Session.setAttribute("ban", "2");
                        response.sendRedirect("MarbeteT.jsp");
                    }
                    break;
                case 9:
                    RestoM = Integer.parseInt(Resto);
                    RestoMSql = Double.parseDouble(Resto);
                    QueryDatos = "SELECT F_TipMed,F_Costo FROM tb_medica where F_ClaPro='" + Clave + "'";
                    Consulta = ObjMySQL.consulta(QueryDatos);
                    if (Consulta.next()) {
                        Costo = Double.parseDouble(Consulta.getString("F_Costo"));
                        TipoM = Integer.parseInt(Consulta.getString("F_TipMed"));
                    }
                    QueryDatos = "SELECT STR_TO_DATE('" + Caducidad + "', '%d/%m/%Y')";
                    Consulta = ObjMySQL.consulta(QueryDatos);
                    if (Consulta.next()) {
                        Caducidad = Consulta.getString("STR_TO_DATE('" + Caducidad + "', '%d/%m/%Y')");
                    }
                    if (TipoM == 2504) {
                        Iva = 0.0;
                        an1 = Caducidad.substring(0, 4);
                        mes = Caducidad.substring(5, 7);
                        dia = Caducidad.substring(8, 10);
                        ann1 = Integer.parseInt(an1);
                        difeann1 = ann1 - 3;
                        Fabricacion = difeann1 + "-" + mes + "-" + dia;

                    } else {
                        Iva = 0.16;
                        an1 = Caducidad.substring(0, 4);
                        mes = Caducidad.substring(5, 7);
                        dia = Caducidad.substring(8, 10);
                        ann1 = Integer.parseInt(an1);
                        difeann1 = ann1 - 5;
                        Fabricacion = difeann1 + mes + dia;
                    }
                    IvaT = (RestoM * Costo) * Iva;
                    Monto = RestoM * Costo;
                    MontoT = Monto + IvaT;

                    //////************************** consulta mysql********//////////////
                    QueryDatos = "SELECT F_FolLot FROM tb_lote WHERE F_ClaPro='" + Clave + "' AND F_ClaLot='" + Lote + "' AND F_FecCad='" + Caducidad + "' AND F_ClaOrg='" + Provee + "' AND F_Cb='" + CBM + "' AND F_ClaMar='" + MarcaM + "'";
                    Consulta = ObjMySQL.consulta(QueryDatos);
                    if (Consulta.next()) {
                        FCont++;
                        FolioL = Integer.parseInt(Consulta.getString("F_FolLot"));
                    }
                    QueryDatos = "SELECT F_ExiLot FROM tb_lote WHERE F_ClaPro='" + Clave + "' AND F_ClaLot='" + Lote + "' AND F_FecCad='" + Caducidad + "' AND F_Ubica='" + Ubinew + "' AND F_ClaOrg='" + Provee + "' AND F_Cb='" + CBM + "' AND F_ClaMar='" + MarcaM + "'";
                    Consulta = ObjMySQL.consulta(QueryDatos);
                    if (Consulta.next()) {
                        Cont++;
                        Existencia = Integer.parseInt(Consulta.getString("F_ExiLot"));
                    }
                    if (FCont > 0) {
                        if (Cont > 0) {
                            Resultado = Existencia + RestoM;
                            ObjMySQL.actualizar("UPDATE tb_lote SET F_ExiLot='" + Resultado + "' where F_FolLot='" + FolioL + "' AND F_Ubica='" + Ubinew + "'");
                            ObjMySQL.actualizar("INSERT INTO tb_movinv values(0,curdate(),'0','2','" + Clave + "','" + RestoM + "','" + Costo + "','" + MontoT + "','1','" + FolioL + "','" + Ubinew + "','" + Provee + "',CURTIME(),'" + sesion.getAttribute("nombre") + "','')");
                            out.println(Resultado);
                        } else {
                            ObjMySQL.actualizar("INSERT INTO tb_lote VALUES (0,'" + Clave + "','" + Lote + "','" + Caducidad + "','" + RestoM + "','" + FolioL + "','" + Provee + "','" + Ubinew + "','" + Fabricacion + "','" + CBM + "','" + MarcaM + "')");
                            ObjMySQL.actualizar("INSERT INTO tb_movinv values(0,curdate(),'0','2','" + Clave + "','" + RestoM + "','" + Costo + "','" + MontoT + "','1','" + FolioL + "','" + Ubinew + "','" + Provee + "',CURTIME(),'" + sesion.getAttribute("nombre") + "','')");
                        }
                    } else {
                        QueryDatos = "SELECT F_IndLote FROM tb_indice";
                        Consulta = ObjMySQL.consulta(QueryDatos);
                        if (Consulta.next()) {
                            FolioL = Integer.parseInt(Consulta.getString("F_IndLote"));
                        }
                        FolioLote = FolioL + 1;
                        ObjMySQL.actualizar("update tb_indice set F_IndLote='" + FolioLote + "'");
                        ObjMySQL.actualizar("INSERT INTO tb_lote VALUES (0,'" + Clave + "','" + Lote + "','" + Caducidad + "','" + RestoM + "','" + FolioL + "','" + Provee + "','" + Ubinew + "','" + Fabricacion + "','" + CBM + "','" + MarcaM + "')");
                        ObjMySQL.actualizar("INSERT INTO tb_movinv values(0,curdate(),'0','2','" + Clave + "','" + RestoM + "','" + Costo + "','" + MontoT + "','1','" + FolioL + "','" + Ubinew + "','" + Provee + "',CURTIME(),'" + sesion.getAttribute("nombre") + "','')");

                    }
                    //////**************************fin consulta mysql********//////////////

                    response.sendRedirect("Ubicaciones/Agregar.jsp");
                    break;
                case 10:
                    posi = Cadena.indexOf(':');
                    posiid = Cadena.lastIndexOf(';');
                    Folio = Cadena.substring(0, posi);
                    Ubicacion = Cadena.substring(posi + 1, posiid);
                    Id = Cadena.substring(posiid + 1);
                    Session.setAttribute("folio", Folio);
                    Session.setAttribute("ubicacion", Ubicacion);
                    Session.setAttribute("id", Id);
                    response.sendRedirect("Ubicaciones/Redistribucion.jsp");
                    break;
                case 11:
                    Folio = (String) Session.getAttribute("folio");
                    Ubicacion = (String) Session.getAttribute("ubicacion");
                    Id = (String) Session.getAttribute("id");
                    Usuario = (String) Session.getAttribute("Usuario");

                    Caduca = CaduNew;

                    /*
                     *Se comienza la chamba
                     */
                    try {
                        ObjMySQL.conectar();
                        ResultSet rset = ObjMySQL.consulta("select F_ClaPro, F_ClaLot, F_FecCad from tb_lote where F_IdLote='" + Id + "'");
                        while (rset.next()) {
                            ResultSet rset2 = ObjMySQL.consulta("select F_IdLote from tb_lote where F_ClaPro = '" + rset.getString("F_ClaPro") + "' and F_ClaLot = '" + rset.getString("F_ClaLot") + "' and F_FecCad='" + rset.getString("F_FecCad") + "'");
                            while (rset2.next()) {
                                System.out.println(rset2.getString(1));
                                System.out.println(LoteNew);
                                System.out.println(Caduca);
                                if (!LoteNew.equals("") && Caduca.equals("")) {
                                    ObjMySQL.insertar("update tb_lote set F_ClaLot = '" + LoteNew + "' where F_IdLote = '" + rset2.getString(1) + "'");
                                    ObjMySQL.insertar("INSERT INTO tb_lotemov values(0,'" + Folio + "','" + rset.getString("F_ClaPro") + "','" + Lote + "','','" + LoteNew + "','','" + sesion.getAttribute("nombre") + "','" + sesion.getAttribute("modificau") + "',curdate(),curtime())");
                                }
                                if (LoteNew.equals("") && !Caduca.equals("")) {
                                    ObjMySQL.insertar("update tb_lote set F_FecCad = STR_TO_DATE('" + CaduNew + "', '%d/%m/%Y') where F_IdLote = '" + rset2.getString(1) + "'");
                                    ObjMySQL.insertar("INSERT INTO tb_lotemov values(0,'" + Folio + "','" + rset.getString("F_ClaPro") + "','','" + rset.getString("F_FecCad") + "','',STR_TO_DATE('" + CaduNew + "', '%d/%m/%Y'),'" + sesion.getAttribute("nombre") + "','" + sesion.getAttribute("modificau") + "',curdate(),curtime())");
                                }
                                if (!LoteNew.equals("") && !Caduca.equals("")) {
                                    ObjMySQL.insertar("update tb_lote set F_FecCad = STR_TO_DATE('" + CaduNew + "', '%d/%m/%Y'), F_ClaLot = '" + LoteNew + "' where F_IdLote = '" + rset2.getString(1) + "'");
                                    ObjMySQL.insertar("INSERT INTO tb_lotemov values(0,'" + Folio + "','" + rset.getString("F_ClaPro") + "','" + Lote + "','" + rset.getString("F_FecCad") + "','" + LoteNew + "',STR_TO_DATE('" + CaduNew + "', '%d/%m/%Y'),'" + sesion.getAttribute("nombre") + "','" + sesion.getAttribute("modificau") + "',curdate(),curtime())");
                                }
                            }
                        }

                        ObjMySQL.CierreConn();
                        response.sendRedirect("Ubicaciones/Modificacion.jsp");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 12:
                    sesion.setAttribute("Usuario", usuariom);
                    sesion.setAttribute("nombre", nombrem);
                    sesion.setAttribute("Tipo", tipom);
                    response.sendRedirect("Ubicaciones/Consultas.jsp");
                    break;
                case 13:
                    String usuariomodifica2 = (String) Session.getAttribute("nombre");
                    System.out.println(usuariomodifica2);
                    QueryDatos = "select F_Usu, F_nombre, F_Status, F_TipUsu from tb_usuario where F_Usu = '" + Nombren + "' and F_Pass = MD5('" + Passn + "' ) and f_tipusu='6'";
                    Consulta = ObjMySQL.consulta(QueryDatos);
                    if (Consulta.next()) {
                        banusu = 1;
                        F_Usu = Consulta.getString("F_Usu");
                        F_nombre = Consulta.getString("F_nombre");
                        F_TipUsu = Consulta.getString("F_TipUsu");

                    }
                    if (banusu == 1) {
                        //----------------------EL USUARIO ES VÁLIDO
                        sesion.setAttribute("modificau", F_Usu);
                        sesion.setAttribute("modifican", F_nombre);
                        sesion.setAttribute("folio", foliom);
                        sesion.setAttribute("ubicacion", ubicam);
                        sesion.setAttribute("id", idm);
                        sesion.setAttribute("Usuario", usuariom);
                        sesion.setAttribute("nombre", nombrem);
                        sesion.setAttribute("Tipo", tipom);

                        ObjMySQL.insertar("insert into tb_registroentradas values ('" + Nombren + "',NOW(),1,0)");
                        response.sendRedirect("Ubicaciones/Modificacion.jsp");
                    } else {//--------------------------EL USUARIO NO ES VÁLIDO
                        out.println("hola");
                        ObjMySQL.insertar("insert into tb_registroentradas values ('" + Nombren + "',NOW(),0,0)");
                        sesion.setAttribute("mensaje", "Usuario no válido");
                        sesion.setAttribute("folio", foliom);
                        sesion.setAttribute("ubicacion", ubicam);
                        sesion.setAttribute("id", idm);
                        sesion.setAttribute("Usuario", usuariom);
                        sesion.setAttribute("nombre", nombrem);
                        sesion.setAttribute("Tipo", tipom);
                        response.sendRedirect("Ubicaciones/indexValida.jsp");
                    }
                    break;
                case 14:

                    response.sendRedirect("hh/ubicacionesConsultas.jsp");
                    break;

                case 15:
                    Query = "SELECT * FROM tb_marbetes_resto WHERE clave='" + Clave + "' AND lote='" + Lote + "' AND caducidad='" + Caducidad + "' AND piezas='" + Resto + "'";
                    Consultas = ObjMySQL.consulta(Query);
                    if (Consultas.next()) {
                        posi++;
                    }
                    if (posi == 0) {

                        ObjMySQL.actualizar("Insert into tb_marbetes_resto values('" + Clave + "','" + Lote + "','" + Caducidad + "','" + Resto + "',0)");

                        Session.setAttribute("clave", Clave);
                        Session.setAttribute("lote", Lote);
                        Session.setAttribute("cadu", Caducidad);
                        Session.setAttribute("piezas", Resto);
                        response.sendRedirect("Ubicaciones/Marbete_resto.jsp");
                    } else {
                        Session.setAttribute("clave", Clave);
                        Session.setAttribute("lote", Lote);
                        Session.setAttribute("cadu", Caducidad);
                        Session.setAttribute("piezas", Resto);
                        response.sendRedirect("Ubicaciones/Marbete_resto.jsp");
                    }
                    break;
                case 16:

                    ObjMySQL.actualizar("DELETE FROM tb_comparacion WHERE F_Tipo='" + ubicam + "' and F_Usuario='" + sesion.getAttribute("nombre") + "'");
                    ObjMySQL.actualizar("DELETE FROM tb_comparacion2 WHERE F_Tipo='" + ubicam + "' and F_Usuario='" + sesion.getAttribute("nombre") + "'");

                    if (ubicam.equals("CLAVE")) {
                        System.out.println("ENTRO CLAVE");
                        //QueryDatos = "SELECT F_ClaPro AS clave,SUM(F_ExiLot) AS cantidad FROM tb_lote  WHERE F_ExiLot>0 GROUP BY F_ClaPro ORDER BY F_ClaPro ASC";    
                        QueryDatos = "SELECT F_ClaPro AS clave,SUM(F_ExiLot) AS cantidad FROM tb_lote GROUP BY F_ClaPro ORDER BY F_ClaPro ASC";
                        Consulta = ObjMySQL.consulta(QueryDatos);
                        while (Consulta.next()) {
                            Resultado = Integer.parseInt(Consulta.getString("cantidad"));
                            Cantidad = (int) Resultado;
                            Clave = Consulta.getString("clave");
                            ObjMySQL.actualizar("insert into tb_comparacion values('" + Clave + "','-',curdate(),'" + Cantidad + "','sistemas','CLAVE','" + sesion.getAttribute("nombre") + "',0)");
                        }
                        //QueryUbi="SELECT F_ClaPro AS clave,SUM(F_ExiLot) AS cantidad FROM tb_lote  WHERE F_ExiLot>0 GROUP BY F_ClaPro ORDER BY F_ClaPro ASC";
                        QueryUbi = "SELECT F_ClaPro AS clave,SUM(F_ExiLot) AS cantidad FROM tb_lote  GROUP BY F_ClaPro ORDER BY F_ClaPro ASC";
//                        ConsultaUbi = ObjInv.consulta(QueryUbi);
//                        while (ConsultaUbi.next()) {
//                            ClaveU = ConsultaUbi.getString("clave");
//                            ResultadoU = Integer.parseInt(ConsultaUbi.getString("cantidad"));
//                            CantidadU = (int) ResultadoU;
//                            ObjMySQL.actualizar("insert into tb_comparacion values('" + ClaveU + "','-',curdate(),'" + CantidadU + "','inventario','CLAVE','" + sesion.getAttribute("nombre") + "',0)");
//                        }
                        QueryComp = "SELECT F_ClaPro from tb_comparacion where F_Tipo='CLAVE' and F_Usuario='" + sesion.getAttribute("nombre") + "' GROUP BY F_ClaPro";
                        ConsultaComp = ObjMySQL.consulta(QueryComp);
                        while (ConsultaComp.next()) {
                            ClaveC = ConsultaComp.getString("F_ClaPro");
                            QueryCompS = "select F_ClaPro,SUM(F_Cantidad) as cantidad from tb_comparacion where F_ClaPro='" + ClaveC + "' AND F_Tipo='CLAVE' AND F_Sistemas='sistemas' and F_Usuario='" + sesion.getAttribute("nombre") + "' group by F_ClaPro";
                            ConsultaCompS = ObjMySQL.consulta(QueryCompS);
                            while (ConsultaCompS.next()) {
                                CantidadS = ConsultaCompS.getString("cantidad");
                                conts++;
                            }
                            if (conts > 0) {
                                CantidadSG = Integer.parseInt(CantidadS);
                            } else {
                                CantidadSG = 0;
                            }
                            QueryCompU = "select F_ClaPro,SUM(F_Cantidad) as cantidad from tb_comparacion where F_ClaPro='" + ClaveC + "' AND F_Tipo='CLAVE' AND F_Sistemas='inventario' and F_Usuario='" + sesion.getAttribute("nombre") + "' group by F_ClaPro";
                            ConsultaCompU = ObjMySQL.consulta(QueryCompU);
                            while (ConsultaCompU.next()) {
                                CantidadUB = ConsultaCompU.getString("cantidad");
                                contu++;
                            }
                            if (contu > 0) {
                                CantidadUBI = Integer.parseInt(CantidadUB);
                            } else {
                                CantidadUBI = 0;
                            }
                            Diferencia = CantidadSG - CantidadUBI;
                            ObjMySQL.insertar("insert into tb_comparacion2 values('" + ClaveC + "','-',curdate(),'" + CantidadSG + "','" + CantidadUBI + "','" + Diferencia + "','CLAVE','" + sesion.getAttribute("nombre") + "',0)");
                            conts = 0;
                            contu = 0;
                        }
                        Session.setAttribute("UbicaU", ubicam);
                        response.sendRedirect("Ubicaciones/Comparacion.jsp");
                    } else {
                        out.println("ENTRO LOTE");
                        //QueryDatos = "SELECT F_ClaPro as clave,F_ClaLot as lote,F_FecCad,SUM(F_ExiLot) as cantidad FROM Tb_Lote WHERE F_ExiLot>0 GROUP BY F_ClaPro,F_ClaLot order by F_ClaPro,F_ClaLot,F_FecCad asc";    
                        QueryDatos = "SELECT F_ClaPro as clave,F_ClaLot as lote,F_FecCad,SUM(F_ExiLot) as cantidad FROM tb_lote GROUP BY F_ClaPro,F_ClaLot,F_FecCad order by F_ClaPro,F_ClaLot,F_FecCad asc";
                        Consulta = ObjMySQL.consulta(QueryDatos);
                        while (Consulta.next()) {
                            Resultado = Integer.parseInt(Consulta.getString("cantidad"));
                            Cantidad = (int) Resultado;
                            Clave = Consulta.getString("clave");
                            Lote = Consulta.getString("lote");
                            Cadu = Consulta.getString("F_FecCad");
                            ObjMySQL.actualizar("insert into tb_comparacion values('" + Clave + "','" + Lote + "','" + Cadu + "','" + Cantidad + "','sistemas','LOTE','" + sesion.getAttribute("nombre") + "',0)");
                        }
                        //QueryUbi="SELECT F_ClaPro as clave,F_ClaLot as lote,F_FecCad,SUM(F_ExiLot) as cantidad FROM tb_lote WHERE F_ExiLot>0 GROUP BY F_ClaPro,F_ClaLot order by F_ClaPro,F_ClaLot,F_FecCad asc";
                        QueryUbi = "SELECT F_ClaPro as clave,F_ClaLot as lote,F_FecCad,SUM(F_ExiLot) as cantidad FROM tb_lote GROUP BY F_ClaPro,F_ClaLot,F_FecCad order by F_ClaPro,F_ClaLot,F_FecCad asc";
//                        ConsultaUbi = ObjInv.consulta(QueryUbi);
//                        while (ConsultaUbi.next()) {
//                            ClaveU = ConsultaUbi.getString("clave");
//                            LoteU = ConsultaUbi.getString("Lote");
//                            CaduU = ConsultaUbi.getString("F_FecCad");
//                            ResultadoU = Integer.parseInt(ConsultaUbi.getString("cantidad"));
//                            CantidadU = (int) ResultadoU;
//                            ObjMySQL.actualizar("insert into tb_comparacion values('" + ClaveU + "','" + LoteU + "','" + CaduU + "','" + CantidadU + "','inventario','LOTE','" + sesion.getAttribute("nombre") + "',0)");
//                        }
                        QueryComp = "SELECT F_ClaPro,F_Lote,F_FecCad,DATE_FORMAT(F_FecCad,'%d/%m/%Y') as fecha from tb_comparacion where F_Tipo='LOTE' and F_Usuario='" + sesion.getAttribute("nombre") + "' GROUP BY F_ClaPro,F_Lote,F_FecCad order by F_ClaPro,F_Lote,F_FecCad asc";
                        ConsultaComp = ObjMySQL.consulta(QueryComp);
                        while (ConsultaComp.next()) {
                            ClaveC = ConsultaComp.getString("F_ClaPro");
                            LoteC = ConsultaComp.getString("F_Lote");
                            CaduC = ConsultaComp.getString("F_FecCad");
                            Fecha = ConsultaComp.getString("fecha");
                            QueryCompS = "select F_ClaPro,SUM(F_Cantidad) as cantidad from tb_comparacion where F_ClaPro='" + ClaveC + "' and F_Lote='" + LoteC + "' and F_FecCad='" + CaduC + "' AND F_Tipo='LOTE' AND F_Sistemas='sistemas' and F_Usuario='" + sesion.getAttribute("nombre") + "' group by F_ClaPro";
                            ConsultaCompS = ObjMySQL.consulta(QueryCompS);
                            while (ConsultaCompS.next()) {
                                CantidadS = ConsultaCompS.getString("cantidad");
                                conts++;
                            }
                            if (conts > 0) {
                                CantidadSG = Integer.parseInt(CantidadS);
                            } else {
                                CantidadSG = 0;
                            }
                            QueryCompU = "select F_ClaPro,SUM(F_Cantidad) as cantidad from tb_comparacion where F_ClaPro='" + ClaveC + "' and F_Lote='" + LoteC + "' and F_FecCad='" + CaduC + "' AND F_Tipo='LOTE' AND F_Sistemas='inventario' and F_Usuario='" + sesion.getAttribute("nombre") + "' group by F_ClaPro";
                            ConsultaCompU = ObjMySQL.consulta(QueryCompU);
                            while (ConsultaCompU.next()) {
                                CantidadUB = ConsultaCompU.getString("cantidad");
                                contu++;
                            }
                            if (contu > 0) {
                                CantidadUBI = Integer.parseInt(CantidadUB);
                            } else {
                                CantidadUBI = 0;
                            }
                            Diferencia = CantidadSG - CantidadUBI;
                            ObjMySQL.insertar("insert into tb_comparacion2 values('" + ClaveC + "','" + LoteC + "','" + CaduC + "','" + CantidadSG + "','" + CantidadUBI + "','" + Diferencia + "','LOTE','" + sesion.getAttribute("nombre") + "',0)");
                            conts = 0;
                            contu = 0;

                        }
                        Session.setAttribute("UbicaU", ubicam);
                        response.sendRedirect("Ubicaciones/Comparacion.jsp");
                    }

                    break;

            }

            ObjMySQL.CierreConn();
//            ObjInv.cierraConexion();

            out.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
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
