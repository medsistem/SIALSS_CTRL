/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.folios;

import com.gnk.model.FoliosNoOficialDetail;
import com.google.gson.Gson;
import com.medalfa.saa.querys.FoliosNoOficialesQuerys;
import conn.ConectionDBTrans;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.stream.Collectors;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
/**
 *
 * @author DotorMedalfa
 */
@WebServlet(name = "FoliosNoOficialAud", urlPatterns = {"/FoliosNoOficialAud"})
public class FoliosNoOficialAud extends HttpServlet {

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-control-allow-origin", "*");
        response.setHeader("Access-control-allow-methods", "POST");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        double Costo = 0.0;
        String F_FecEnt="",F_ClaCli = "", F_StsFact = "", F_ClaPro = "", F_CantReq = "", F_FecCad = "", F_Hora = "", F_User = "", F_Ubicacion = "", F_Obs = "", F_DocAnt = "", F_Contrato = "";
        String DesV = "", Lote = "", Contrato = "", FolLote = "";
        String FolioV = "", Clave = "";
        int Cont = 0;
        
        
        SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");
        Date FecCad = null;
        Date FecFact = null;
        Date FecEnt = null;
        
        ServletContext context = request.getServletContext();

        PreparedStatement ps = null;

        ResultSet rsetBuscaFactAud = null;
        ResultSet rsetBuscaFactcont = null;

        ResultSet rsetLote = null;
        ConectionDBTrans con = new ConectionDBTrans();

        try {
            con.conectar();
            /*Recorre el Gjon*/
            String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Gson Gson = new Gson();
            FoliosNoOficialDetail nF = Gson.fromJson(body, FoliosNoOficialDetail.class);

            ps = con.getConn().prepareStatement(FoliosNoOficialesQuerys.BUSCA_LOTEAUD);
            ps.setString(1, nF.getClave());
            ps.setString(2, nF.getLote());
            ps.setString(3, nF.getNuevaCadu());
            rsetLote = ps.executeQuery();

            while (rsetLote.next()) {
                FolLote = rsetLote.getString(1);
            }

            /*Busca Lote id lote*/
            ps.clearParameters();
            ps = con.getConn().prepareStatement(FoliosNoOficialesQuerys.BUSCA_FACTAUDCONT);
            ps.setString(1, nF.getFolio());
            ps.setString(2, nF.getClave());
            ps.setString(3, nF.getNuevaFact());

            rsetBuscaFactcont = ps.executeQuery();
            while (rsetBuscaFactcont.next()) {
                Cont = rsetBuscaFactcont.getInt(1);
            }

            /*Obtener la clave de la unidad*/
            ps.clearParameters();
            if (Cont == 0) {
                System.out.println("sin lote");
                ps = con.getConn().prepareStatement(FoliosNoOficialesQuerys.BUSCA_FACTAUD);
                ps.setString(1, nF.getFolio());
                ps.setString(2, nF.getClave());

                rsetBuscaFactAud = ps.executeQuery();
                while (rsetBuscaFactAud.next()) {
                    F_ClaCli = rsetBuscaFactAud.getString(1);
                    F_StsFact = rsetBuscaFactAud.getString(2);
                    F_ClaPro = rsetBuscaFactAud.getString(3);
                    F_CantReq = nF.getSurtido();
                    F_FecEnt = rsetBuscaFactAud.getString(5);
                    F_Hora = rsetBuscaFactAud.getString(6);
                    F_User = rsetBuscaFactAud.getString(7);
                    F_Ubicacion = rsetBuscaFactAud.getString(8);
                    F_Obs = rsetBuscaFactAud.getString(9);
                    F_DocAnt = rsetBuscaFactAud.getString(10);
                    F_Contrato = rsetBuscaFactAud.getString(11);
                }
            } else {
                System.out.println("con lote");
                ps = con.getConn().prepareStatement(FoliosNoOficialesQuerys.BUSCA_FACTAUDLOT);
                ps.setString(1, nF.getFolio());
                ps.setString(2, nF.getClave());
                ps.setString(3, FolLote);

                rsetBuscaFactAud = ps.executeQuery();
                while (rsetBuscaFactAud.next()) {
                    F_ClaCli = rsetBuscaFactAud.getString(1);
                    F_StsFact = rsetBuscaFactAud.getString(2);
                    F_ClaPro = rsetBuscaFactAud.getString(3);
                    F_CantReq = rsetBuscaFactAud.getString(4);
                    F_FecEnt = rsetBuscaFactAud.getString(5);
                    F_Hora = rsetBuscaFactAud.getString(6);
                    F_User = rsetBuscaFactAud.getString(7);
                    F_Ubicacion = rsetBuscaFactAud.getString(8);
                    F_Obs = rsetBuscaFactAud.getString(9);
                    F_DocAnt = rsetBuscaFactAud.getString(10);
                    F_Contrato = rsetBuscaFactAud.getString(11);
                }
            }
            /*Inserta Datos*/
            ps.clearParameters();
            ps = con.getConn().prepareStatement(FoliosNoOficialesQuerys.INSERT_FACTAUD);

            ps.setString(1, nF.getFolioNuevo());
            ps.setString(2, F_ClaCli);
            ps.setString(3, F_StsFact);
            ps.setString(4, nF.getNuevaFecha());
            ps.setString(5, F_ClaPro);
            ps.setString(6, F_CantReq);
            ps.setString(7, nF.getSurtido());
            ps.setString(8, FolLote);
            ps.setString(9, nF.getNuevaFact());
            ps.setString(10, F_Hora);
            ps.setString(11, F_User);
            ps.setString(12, F_Ubicacion);
            ps.setString(13, F_Obs);
            ps.setString(14, F_DocAnt);
            ps.setString(15, F_Contrato);
            System.out.println(ps);
            ps.executeUpdate();

            con.cierraConexion();
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().print("No se pudo cargar el archivo");
        }
        response.getWriter().print("Archivo cargado con éxito");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

    }
}
