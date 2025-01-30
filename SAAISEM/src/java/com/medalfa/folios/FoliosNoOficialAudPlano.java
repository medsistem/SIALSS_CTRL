/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.folios;

import com.gnk.model.FoliosNoOficialDetailPlano;
import com.gnk.model.ListFolioDetailPlano;
import com.google.gson.Gson;
import conn.ConectionDBTrans;
import java.io.IOException;
import java.sql.PreparedStatement;

 
import java.sql.ResultSet;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author DotorMedalfa
 */
@WebServlet(name = "FoliosNoOficialAudPlano", urlPatterns = {"/FoliosNoOficialAudPlano"})
public class FoliosNoOficialAudPlano extends HttpServlet {

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
        response.setHeader("Access-control-allow-origin", "*");
        response.setHeader("Access-control-allow-methods", "POST");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        PreparedStatement ps = null;

        ResultSet rsetLote = null;
        ConectionDBTrans con = new ConectionDBTrans();

        try {
            con.conectar();
            /*Recorre el Gjon*/
            String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            Gson Gson = new Gson();
            ListFolioDetailPlano lista = Gson.fromJson(body, ListFolioDetailPlano.class);

            String query = "INSERT INTO tb_factura_plain VALUES (0,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            for (FoliosNoOficialDetailPlano detail : lista.getDetalles()) {
                ps = con.getConn().prepareStatement(query);
                ps.setString(1, detail.getUnidad());
                ps.setString(2, detail.getNombreUnidad());
                ps.setString(3, detail.getFolioNuevo());
                ps.setString(4, detail.getFechaIng());
                ps.setString(5, detail.getNuevaFecha());
                ps.setString(6, detail.getFechaEntrega());
                ps.setString(7, detail.getClaveLarga());
                ps.setString(8, detail.getClave());
                ps.setString(9, detail.getDescripcionClave());
                ps.setString(10, detail.getLote());
                ps.setString(11, detail.getNuevaCadu());
                ps.setString(12, detail.getRequerido());
                ps.setString(13, detail.getSurtido());
                
                System.out.println(ps);
                ps.executeUpdate();
            }

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
