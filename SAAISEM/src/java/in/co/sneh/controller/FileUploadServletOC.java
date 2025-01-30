/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.controller;

import LeeExcel.LeeExcelRecibo;
import conn.ConectionDB;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Anibal GNKL
 */
@WebServlet(name = "FileUploadServletOC", urlPatterns = {"/FileUploadServletOC"})
public class FileUploadServletOC extends HttpServlet {

    LeeExcelRecibo lee = new LeeExcelRecibo();
    DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat df3 = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        HttpSession sesion = request.getSession(true);
        String Usuario;
        ConectionDB con = new ConectionDB();
        Map<String, String> map = new HashMap<String, String>();
        
        String F_FileRemi="",F_FileRemiN="",F_FileFac="",F_FileFacN,F_FolRemi="",F_FolFac="";
        boolean isMultiPart = ServletFileUpload.isMultipartContent(request);
        if (isMultiPart) {
            ServletFileUpload upload = new ServletFileUpload();
            try {
                Usuario = (String) sesion.getAttribute("nombre");
                
                FileItemIterator itr = upload.getItemIterator(request);
                
                while (itr.hasNext()) {
                    FileItemStream item = itr.next();
                    if (item.isFormField()) {
                        String fielName = item.getFieldName();
                        InputStream is = item.openStream();
                        byte[] b = new byte[is.available()];
                        is.read(b);
                        String value = new String(b);
                        System.out.println(fielName + ":" + value);
                        map.put(fielName, value);                  
                        if(map.get("folioRemi") != null){
                                if(!map.get("folioRemi").equals("") ){
                                    if(F_FileRemi.equals("")){
                                        F_FolRemi = map.get("folioRemi");
                                    }
                                }
                            }
                            if(map.get("folioFac")!= null){
                                if(!map.get("folioFac").equals("")){
                                    if(F_FileFac.equals("")){
                                        F_FolFac = map.get("folioFac");
                                    }
                                }
                            }
                        //response.getWriter().println(fielName + ":" + value + "<br/>");
                    } 
                }
            } catch (FileUploadException e) {
                Logger.getLogger(FileUploadServlet.class.getName()).log(Level.SEVERE, null, e);
            }

            //out.println("<script>alert('No se pudo cargar, verifique las celdas')</script>");
        }

       // System.out.println(request.getParameter("accion") + "*****");
      //  System.out.println(map.get("accion") + "*****");
        
        if (map.get("accion").equals("CodigoBarras")) {
            try {
                String folioRemi = map.get("folioRemi");
                String folioFac = map.get("folioFac");
                if (!folioFac.equals("")) {
                    folioRemi = folioFac;
                }

                String CodBar = map.get("codbar");
                //String seleccionaClave = map.get("seleccionaClave");
                sesion.setAttribute("NoCompra", map.get("folio"));
                sesion.setAttribute("folioRemi", folioRemi);
                sesion.setAttribute("CodBar", CodBar);
                sesion.setAttribute("Lote", "");
                sesion.setAttribute("Cadu", "");
                //sesion.setAttribute("claveSeleccionada", seleccionaClave);
                response.sendRedirect("hh/compraAuto3.jsp");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        
        //GEnerar codigo aleatorio
        if (map.get("accion").equals("GeneraCodigo")) {
                String CodBar = "";
                try {
                    con.conectar();
                    ResultSet rset = con.consulta("SELECT MAX(F_IdCb) AS F_IdCb FROM tb_gencb");
                    while (rset.next()) {
                        CodBar = rset.getString("F_IdCb");
                    }
                    System.out.println(CodBar);
                    Long CBL = Long.parseLong(CodBar) + 1;
                    con.insertar("insert into tb_gencb values('" + CBL + "','CEDIS CENTRAL')");
//                      con.insertar("update tb_gencb set  F_IdCb = '"+CBL+"' where F_IdCb = "+CodBar+" ");
                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                
                try {
                    String posCla = sesion.getAttribute("posClave").toString();
                    String folio = map.get("folio");
                    String folioRemi = map.get("folioRemi");
                    String folioFac = map.get("folioFac");
                    if(!folioFac.equals(""))
                        folioRemi =  folioFac;
                
                    String lote = map.get("lot").toUpperCase();
                    String cadu = map.get("cad");
                    sesion.setAttribute("NoCompra", map.get("folio"));
                    sesion.setAttribute("folioRemi", folioRemi);
                    sesion.setAttribute("CodBar", CodBar);
                    sesion.setAttribute("Lote", "");
                    sesion.setAttribute("Cadu", "");
                    response.sendRedirect("hh/compraAuto3.jsp");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        
        if (map.get("accion").equals("seleccionaClave")) {
            //String folio = request.getParameter("folio");
            String folio = map.get("folio");
            String folioRemi = map.get("folioRemi");
            String folioFac = map.get("folioFac");
            
            if (folioFac != null) {
                if (!folioFac.equals("")) {
                    folioRemi = folioFac;
                }
            }
            String seleccionaClave = map.get("selectClave");
            sesion.setAttribute("NoCompra", map.get("folio"));
            sesion.setAttribute("folioRemi", folioRemi);
            sesion.setAttribute("Lote", "");
            sesion.setAttribute("Cadu", "");
            sesion.setAttribute("CodBar", "");
            sesion.setAttribute("claveSeleccionada", seleccionaClave);
            response.sendRedirect("hh/compraAuto3.jsp");
        }
        
        
        if (map.get("accion").equals("refresh")) {
            try {
                String posCla = sesion.getAttribute("posClave").toString();
                String folio = map.get("folio");
                String folioRemi = map.get("folioRemi");
                String folioFac = map.get("folioFac");
                String CodBar = map.get("Codbar");
                
                if (!folioFac.equals("")) {
                    folioRemi = folioFac;
                }

                
                
                String lote = map.get("lot").toUpperCase();
                String cadu = map.get("cad");
                sesion.setAttribute("NoCompra", map.get("folio"));
                sesion.setAttribute("folioRemi", folioRemi);
                
                sesion.setAttribute("CodBar", CodBar);
                sesion.setAttribute("Lote", lote);
                sesion.setAttribute("Cadu", cadu);
                response.sendRedirect("hh/compraAuto3.jsp");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        
        if(map.get("accion").equals("guardarLote")) {
            String folioRemi = "";
            String folioFac = "";
            try {
                String posCla = sesion.getAttribute("posClave").toString();
                String folio = map.get("folio");
                folioRemi = map.get("folioRemi");
                folioFac = map.get("folioFac");
                if (folioRemi.equals("")) {
                    folioRemi = folioFac;
                }

                sesion.setAttribute("NoCompra", map.get("folio"));
                sesion.setAttribute("folioRemi", "");
                sesion.setAttribute("CodBar", "");
                sesion.setAttribute("Lote", "");
                sesion.setAttribute("Cadu", "");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            try {
                con.conectar();
                Calendar c1 = GregorianCalendar.getInstance();
                String Tipo = "";
                double Costo = 0.0, IVA = 0.0, Monto = 0.0, IVAPro = 0.0, MontoIva = 0.0;
                String lote = map.get("lot").toUpperCase();
                String Clave = map.get("ClaPro");
                String ClaveSS = map.get("ClaProSS");
                String Proyectos = map.get("Proyectos");
                String CostoI = map.get("F_Costo");
                Costo = Double.parseDouble(CostoI);
                String cadu = df2.format(df3.parse(map.get("cad")));
                c1.setTime(df3.parse(map.get("cad")));
                ResultSet rset_medica = con.consulta("SELECT F_TipMed,F_Costo FROM tb_medica WHERE F_ClaPro='" + Clave + "' AND F_ClaProSS='" + ClaveSS + "';");
                while (rset_medica.next()) {
                    Tipo = rset_medica.getString("F_TipMed");
                    //Costo = Double.parseDouble(rset_medica.getString("F_Costo"));
                    if (Tipo.equals("2505")) {
                        c1.add(Calendar.YEAR, -3);
                        IVA = 0.16;
                    } else {
                        c1.add(Calendar.YEAR, -5);
                        IVA = 0.0;
                    }
                }

                Calendar FecAct = GregorianCalendar.getInstance();
                FecAct.setTime(new Date());
                while (c1.after(FecAct)) {
                    c1.add(Calendar.YEAR, -1);
                }
                int totalTarimas = 0, TotalCajasInc = 0, TotalCajas = 0, TotalResto = 0;
                String fecFab = (df2.format(c1.getTime()));
                String CodBar = map.get("codbar");
                String Tarimas = map.get("Tarimas");
                String claPro = map.get("claPro");
                String Marca = map.get("list_marca");
                byte[] a = map.get("Obser").getBytes("ISO-8859-1");
                String F_Obser = (new String(a, "UTF-8")).toUpperCase();

                String TCajas = map.get("TCajas");
                TCajas = TCajas.replace(",", "");

                if (Tarimas.equals("")) {
                    Tarimas = "0";
                }
                Tarimas = Tarimas.replace(",", "");

                totalTarimas = Integer.parseInt(Tarimas);
                String Cajas = map.get("Cajas");
                if (Cajas.equals("")) {
                    Cajas = "0";
                }
                Cajas = Cajas.replace(",", "");
                TotalCajas = Integer.parseInt(Cajas);
                String Piezas = map.get("Piezas");
                if (Piezas.equals("")) {
                    Piezas = "0";
                }
                Piezas = Piezas.replace(",", "");
                String TarimasI = map.get("TarimasI");
                if (TarimasI.equals("")) {
                    TarimasI = "0";
                }
                TarimasI = TarimasI.replace(",", "");
                String CajasxTI = map.get("CajasxTI");
                if (CajasxTI.equals("")) {
                    CajasxTI = "0";
                }
                CajasxTI = CajasxTI.replace(",", "");
                TotalCajasInc = Integer.parseInt(CajasxTI);

                String Resto = map.get("Resto");
                if (Resto.equals("")) {
                    Resto = "0";
                }
                
                String factorEmpaque = map.get("factorEmpaque");
                 
                if (factorEmpaque.equals("")) {
                    factorEmpaque = "1";
                }
                String ordenSuministro = map.get("ordenSuministro");
                
                
                Resto = Resto.replace(",", "");
                TotalResto = Integer.parseInt(Resto);
                String PzaCajas = map.get("PzsxCC");
                if (PzaCajas.equals("")) {
                    PzaCajas = "0";
                }
                PzaCajas = PzaCajas.replace(",", "");

                if (TotalCajasInc > 0) {
                    totalTarimas = totalTarimas - 1;
                    TarimasI = "1";
                    TotalCajas = TotalCajas - TotalCajasInc;
                }
                /*else if (TotalResto > 0) {
                    totalTarimas = totalTarimas - 1;
                }*/
                Tarimas = Integer.toString(totalTarimas);
                Cajas = Integer.toString(TotalCajas);
                TCajas = Integer.toString(TotalCajas);

                //IVAPro = (Double.parseDouble(Piezas) * Costo) * IVA;
                
                IVAPro = (Costo * Double.parseDouble(Piezas)) * IVA; // F_Impto
                Monto = Double.parseDouble(Piezas) * Costo;
                //MontoIva = Monto + IVAPro;
                MontoIva = (Double.parseDouble(Piezas) * Costo) * (1+IVA); // F_ComTot
                //String url="http://187.176.10.53:3000/bills/";
                String url="http://127.0.0.1:8080/bills/";
//                System.out.println(url+F_FileRemi);
                if(!F_FileRemi.equals("")){
                    F_FileRemi = url+F_FileRemi;
                    System.out.println(F_FileRemi);
                }
                
//                System.out.println(F_FileFac);
                if(!F_FileFac.equals("")){
                    F_FileFac = url + F_FileFac;
                    System.out.println(F_FileFac);
                }
                //con.insertar("insert into tb_compratemp values(0,CURDATE(),'" + Clave + "','" + lote + "','" + cadu + "','" + fecFab + "','" + Marca + "','" + claPro + "','" + CodBar + "','" + Tarimas + "','" + TCajas + "','" + Piezas + "','" + TarimasI + "','" + CajasxTI + "','" + Resto + "','" + PzaCajas + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_Obser + "','" + request.getParameter("folioRemi") + "','" + request.getParameter("folio") + "','" + claPro + "','" + sesion.getAttribute("nombre") + "','1', '" + request.getParameter("F_Origen") + "','" + Proyectos + "')"
//                con.insertar("insert into tb_compratemp values(0,CURDATE(),'" + Clave + "','" + lote + "','" + cadu + "','" + fecFab + "','" + Marca + "','" + claPro + "','" + CodBar + "','" + Tarimas + "','" + TCajas + "','" + Piezas + "','" + TarimasI + "','" + CajasxTI + "','" + Resto + "','" + PzaCajas + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_Obser + "','" + F_FolRemi + "','" + map.get("folio") + "','" + claPro + "','" + sesion.getAttribute("nombre") + "','1', '" + map.get("F_Origen") + "','" + Proyectos + "','" + F_FileFac + "','" + F_FileRemi + "','" + F_FolFac + "', curtime())"
                System.out.println("insert into tb_compratemp values(0,CURDATE(),'" + Clave + "','" + lote + "','" + cadu + "','" + fecFab + "','" + Marca + "','" + claPro + "','" + CodBar + "','" + Tarimas + "','" + TCajas + "','" + Piezas + "','" + TarimasI + "','" + CajasxTI + "','" + Resto + "','" + PzaCajas + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_Obser + "','" + F_FolRemi + "','" + map.get("folio") + "','" + claPro + "','" + sesion.getAttribute("Usuario") + "','1', '" + map.get("F_Origen") + "','" + Proyectos + "','" + F_FileFac + "','" + F_FileRemi + "','" + F_FolFac + "', curtime(),"+factorEmpaque+",'"+ordenSuministro+"')");
                con.insertar("insert into tb_compratemp values(0,CURDATE(),'" + Clave + "','" + lote + "','" + cadu + "','" + fecFab + "','" + Marca + "','" + claPro + "','" + CodBar + "','" + Tarimas + "','" + TCajas + "','" + Piezas + "','" + TarimasI + "','" + CajasxTI + "','" + Resto + "','" + PzaCajas + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_Obser + "','" + F_FolRemi + "','" + map.get("folio") + "','" + claPro + "','" + sesion.getAttribute("Usuario") + "','1', '" + map.get("F_Origen") + "','" + Proyectos + "','" + F_FileFac + "','" + F_FileRemi + "','" + F_FolFac + "', curtime(),"+factorEmpaque+",'"+ordenSuministro+"')");
                con.insertar("insert into tb_cb values(0,'" + CodBar + "','" + Clave + "','" + lote + "','" + cadu + "','" + fecFab + "', '" + Marca + "')");
              
                //con.insertar("insert into tb_compraregistro values(0,CURDATE(),'" + Clave + "','" + lote + "','" + cadu + "','" + fecFab + "','" + Marca + "','" + claPro + "','" + CodBar + "','" + Tarimas + "','" + TCajas + "','" + Piezas + "','" + TarimasI + "','" + CajasxTI + "','" + Resto + "','" + Costo + "','" + IVAPro + "','" + MontoIva + "','" + F_Obser + "','" + request.getParameter("folioRemi") + "','" + request.getParameter("folio") + "','" + claPro + "','" + sesion.getAttribute("nombre") + "');");
                try {
                    con.insertar("insert into tb_pzcaja values (0,'" + claPro + "','" + Marca + "','" + map.get("PzsxCC") + "','" + Clave + "','" + ClaveSS + "')");
                } catch (Exception e) {
                    e.getMessage();
                }
                 //con.insertar("update tb_pedidoisem set F_Recibido = '1' where F_Clave = '" + Clave + "' and  ");

                con.cierraConexion();
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.sendRedirect("hh/compraAuto3.jsp");
        }

    }
}
