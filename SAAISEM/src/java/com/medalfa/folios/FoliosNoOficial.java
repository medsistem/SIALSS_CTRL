/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.folios;

import com.gnk.model.FoliosNoOficialDetail;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.medalfa.saa.querys.FoliosNoOficialesQuerys;
import conn.ConectionDBTrans;
import java.io.File;
import java.io.IOException;
//import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JasperRunManager;


/**
 *
 * @author DotorMedalfa
 */
@WebServlet(name = "FoliosNoOficial", urlPatterns = {"/FoliosNoOficial"})
public class FoliosNoOficial extends HttpServlet {
    
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response){
        response.setHeader("Access-control-allow-origin", "*");
        response.setHeader("Access-control-allow-methods", "POST");
    }
   
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
         
         double Costo = 0.0;
        String Unidad = "",claCli = "", Fecha = "", Direc = "", FecApl = "",FecCad = "", DocFolio = "", Razon = "", Proyecto = "", Jurisdiccion = "", Municipio = "", TipMed= "";
        String DesV = "", Lote = "", Contrato = "", OC = "", Nomenclatura = "", Encabezado = "", RedFria = "", DesProy = "";
        String  FolioV = "", Clave = "",DesClave = "",PreClave = "", Imgape = "", NoImgApe = "", ImagenControlado = "", CargoResponsable = "", NombreResponsable = "";
        
        ServletContext context = request.getServletContext();
        
        PreparedStatement ps = null;
        ResultSet rsetUnidadDet = null;
        ResultSet rsetEncabeado = null;
        ResultSet rsetUnidad = null;
        ResultSet rsetTipClave = null;
        ResultSet rsetLote = null;
        ConectionDBTrans con = new ConectionDBTrans();
         
        
        
         try {
             con.conectar();
            /*Recorre el Gjon*/
             String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
             Gson Gson = new Gson(); 
             System.out.println(body);
//             FoliosNoOficialDetail nF = Gson.fromJson(body, FoliosNoOficialDetail.class);
             FoliosNoOficialDetail[] nF = Gson.fromJson(body, FoliosNoOficialDetail[].class);
             
            for(FoliosNoOficialDetail folAudDelete: nF){ 
                  /*Elimina el folio de la tabla impFolio*/  
        
             ps = con.getConn().prepareStatement(FoliosNoOficialesQuerys.REMOVE_FOLIO);
             ps.setString(1, folAudDelete.getFolioNuevo());
             ps.executeUpdate();
            }
            
            
             for(FoliosNoOficialDetail folAud: nF){
               
             /*Busca encabezados y proyecto*/
             ps = con.getConn().prepareStatement(FoliosNoOficialesQuerys.OBTENER_ENCABEZADO_PROYECTO);
                 System.out.println(folAud.getFolio());
//             ps.setString(1, folAud.getFolio());
             
            rsetEncabeado = ps.executeQuery();
             while (rsetEncabeado.next()) {                 
                Nomenclatura = rsetEncabeado.getString(1);
                Encabezado = rsetEncabeado.getString(2);
                DesProy  = rsetEncabeado.getString(3);
             }
             
             
             /*Obtener la clave de la unidad*/
             ps.clearParameters();
             ps = con.getConn().prepareStatement(FoliosNoOficialesQuerys.OBTENER_CLAVE_UNIDAD);
              ps.setString(1, folAud.getFolio());
              
              rsetUnidad = ps.executeQuery();
              while (rsetUnidad.next()) {     
                  claCli = rsetUnidad.getString(1);
                  FolioV = rsetUnidad.getString(2);
                  DocFolio =  Nomenclatura+folAud.getFolioNuevo();
              }
              // DocFolio =  Nomenclatura+folAud.getFolioNuevo();
              
             /*Datos de la unidad*/ 
             ps.clearParameters();
             ps = con.getConn().prepareStatement(FoliosNoOficialesQuerys.BUSCA_UNIDAD); 
             ps.setString(1,  "99999");
              rsetUnidadDet = ps.executeQuery();
              while (rsetUnidadDet.next()) {     
                   DocFolio =  Nomenclatura+folAud.getFolioNuevo();
                  Unidad = rsetUnidadDet.getString(1);
                  Direc = rsetUnidadDet.getString(2);
                  Jurisdiccion = rsetUnidadDet.getString(3);
                  Municipio = rsetUnidadDet.getString(4);
                  Razon = rsetUnidadDet.getString(5);
                  
              }
             
             /*Busca la Clave*/
             ps.clearParameters();
             ps = con.getConn().prepareStatement(FoliosNoOficialesQuerys.BUSCA_TIP_CLAVE);
             ps.setString(1, folAud.getClave());
              
             rsetTipClave = ps.executeQuery();
             while (rsetTipClave.next()) {
                  
                 Clave = rsetTipClave.getString(1);
                 DesClave = rsetTipClave.getString(2);
                 PreClave = rsetTipClave.getString(3);
                 Costo = rsetTipClave.getDouble(4);
                 TipMed = rsetTipClave.getString(5);   
                 
             }
             if (TipMed.equals("RedFria")) {
                 RedFria = "red_fria.jpg";
             } else if (TipMed.equals("Controlado")) {
                 RedFria = "Nored_fria.jpg";
                 ImagenControlado = "image/Controlado.jpg";
                 CargoResponsable = "RESPONSABLE SANITARIO MEDALFA";
                 NombreResponsable = "Q.F.B. ";
             } else {
                 RedFria = "Nored_fria.jpg";
                 ImagenControlado = "image/NoControlado.jpg";
                 CargoResponsable = " ";
                 NombreResponsable = " ";
             }
             
             /*Busca Lote*/
              ps.clearParameters();
               ps = con.getConn().prepareStatement(FoliosNoOficialesQuerys.BUSCA_LOTE);
               ps.setString(1, folAud.getClave());
               ps.setString(2, folAud.getLote());
             
              rsetLote = ps.executeQuery();
              
             while (rsetLote.next()) {
                FecCad = rsetLote.getString(3);
             }
              
              /*Inserta Datos*/
             ps.clearParameters();
             ps = con.getConn().prepareStatement(FoliosNoOficialesQuerys.INSERT_IMPFOLIO);
                 System.out.println("DocFolio: "+DocFolio);
             ps.setString(1, claCli);
             ps.setString(2, Unidad);
             ps.setString(3, Direc);
             ps.setString(4, DocFolio);
             ps.setString(5, folAud.getNuevaFact());
             ps.setString(6, Clave);
             ps.setString(7, DesClave);
             ps.setString(8, folAud.getLote());
             ps.setString(9, folAud.getNuevaCadu());
             ps.setString(10, folAud.getSurtido());
             ps.setString(11, folAud.getSurtido());
             ps.setString(12, folAud.getNuevaFact());
             ps.setString(13, Razon);
             ps.setString(14, DesProy);
             ps.setString(15, Jurisdiccion);
             ps.setString(16, Municipio);
             ps.setString(17, RedFria);
             ps.setString(18, Encabezado);
             ps.setString(19, folAud.getFolioNuevo());
             System.out.println(ps);
             ps.executeUpdate();
              
             }//fin del for
                    
             /*Generar Jasper y Pdf*/
                        File reportFile = new File(context.getRealPath("/reportes/ImprimeFoliosMicAud.jasper"));
                        Map parameters = new HashMap();
                        parameters.put("Folfact", DocFolio);
                        parameters.put("Usuario", "Reimprime");
                        parameters.put("F_Obs", " ");
                        parameters.put("RedFria", RedFria);
                        byte[] bytes = JasperRunManager.runReportToPdf(reportFile.getPath(), parameters, con.getConn());
                        response.setContentType("application/pdf");
                        response.setContentLength(bytes.length);
                        ServletOutputStream ouputStream = response.getOutputStream();
                        ouputStream.write(bytes, 0, bytes.length);
                        ouputStream.flush();
                        ouputStream.close();
                        
          con.cierraConexion();
         }catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
         }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        
    }
}
