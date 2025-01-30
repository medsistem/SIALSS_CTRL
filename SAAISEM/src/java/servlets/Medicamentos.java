/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

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
 * Creación y modificaciones del cátalogo de medicamentos
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class Medicamentos extends HttpServlet {

    //ConectionDB_SQLServer consql = new ConectionDB_SQLServer();
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
        ConectionDB con = new ConectionDB();
        ConectionDB_SQLServer conModula = new ConectionDB_SQLServer();
        try {
            /*
             *Para actualizar Registros
             */

 /*
             *Manda al jsp el id del registro a editar
             */
            if (request.getParameter("accion").equals("editar")) {
                request.getSession().setAttribute("id", request.getParameter("id"));
                response.sendRedirect("editar_proveedor.jsp");
                }
            /*
             *Para eliminar registro
             */
            if (request.getParameter("accion").equals("eliminar")) {
                //consql.conectar();
                con.conectar();
                try {
                    //consql.insertar("delete from TB_Provee where F_ClaPrv = '" + request.getParameter("id") + "' ");
                    con.insertar("delete from provee_all where F_ClaPrv = '" + request.getParameter("id") + "' ");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());

                    out.println("<script>alert('Error al eliminar')</script>");
                    out.println("<script>window.location='catalogo.jsp'</script>");
                }
                con.cierraConexion();
                //consql.cierraConexion();

                out.println("<script>alert('Se elimino el proveedor correctamente')</script>");
                out.println("<script>window.location='catalogo.jsp'</script>");
            }
            /*
             *Guarda Registros
             */
            if (request.getParameter("accion").equals("guardar")) {
                try {
                    String Nombre = "";
                    int TpMed = 0;
                    con.conectar();
                    //conModula.conectar();
                    try {

                        ResultSet rst_prov = con.consulta("SELECT F_ClaPro FROM tb_medica WHERE F_ClaPro='" + request.getParameter("Clave").toUpperCase() + "'");
                        while (rst_prov.next()) {
                            Nombre = rst_prov.getString("F_ClaPro");
                        }
                        if (!(Nombre.equals(""))) {
                            out.println("<script>alert('Ya esta registrado éste Medicamento')</script>");
                            out.println("<script>window.location='medicamento.jsp'</script>");

                        } else {
                            TpMed = Integer.parseInt(request.getParameter("list_medica").toUpperCase());
                            conModula.ejecutar("insert into IMP_ARTICOLI (ART_OPERAZIONE, ART_ARTICOLO, ART_DES, ART_UMI, ART_MIN, ART_PROY) values ('I','" + request.getParameter("Clave").toUpperCase() + "','" + request.getParameter("Descripcion").toUpperCase() + "','PZ','" + request.getParameter("Min").toUpperCase() + "', 'ISSEMyM')");
                            con.insertar("insert into tb_medica values ('" + request.getParameter("Clave").toUpperCase() + "','" + request.getParameter("Descripcion").toUpperCase() + "','A','" + TpMed + "','" + request.getParameter("Costo").toUpperCase() + "','" + request.getParameter("PresPro").toUpperCase() + "','" + request.getParameter("F_Origen").toUpperCase() + "','" + request.getParameter("SAP").toUpperCase() + "');");
                            con.insertar("insert into tb_maxmodula values('" + request.getParameter("Clave").toUpperCase() + "','" + request.getParameter("Max").toUpperCase() + "','" + request.getParameter("Min").toUpperCase() + "','0')");
                        }
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                        out.println("<script>alert('Error: " + e.getMessage() + "')</script>");
                        out.println("<script>window.location='medicamento.jsp'</script>");
                    }
                    //conModula.cierraConexion();
                    con.cierraConexion();

                    out.println("<script>alert('Medicamento capturado correctamente.')</script>");
                    out.println("<script>window.location='medicamento.jsp'</script>");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (request.getParameter("accion").equals("Modificar")) {
                try {
                    con.conectar();
                    String Clave = "", Descripcion = "",Presentacion = "", radiosts = "", radiorigen = "", radiocat = "", radiotipo = "", Costo = "", Incmen = "", Catalogo = "", CostoNim = "";
                    String OrigeNim = "", GrupoNim = "", ComentarioNim = "", CatalogoNim = "", CantRecibir = "", ProIsem = "", ProMic = "", ProIssemym = "", Pro30 = "",ProSol = "",ProAnes = "",ProAnual = "",ProEem = "",ProThoma= "",ProUcin= "",ProCivilM = "", ProUru = "", ProMinds = "", ProThomAnes = "",ProThomMer = "",ProAcre = "",ProHemo = "",ProDDS = "", ProEnse = "", ProAPM = "" , ProSonora = "",ProSinergo = "";
                    String Cat1 = "", Cat2 = "", Cat3 = "", Cat4 = "", Cat5 = "", Cat6 = "", Cat7 = "", Cat8 = "", Cat9 = "", Cat10 = "", Cat11 = "", Cat12 = "";
                    String Cat14 = "", Cat15 = "", Cat16 = "", Cat17 = "", Cat216 = "", Cat217 = "", Cat18 = "", Cat19 = "", Cat20 = "", Cat30 = "", Cat100 = "";
                    int ban1 = 0, ban2 = 0, ban3 = 0, ban4 = 0, ban5 = 0, ban6 = 0, ban7 = 0, ban8 = 0, ban9 = 0, ban10 = 0, ban11 = 0, ban12 = 0, ban14 = 0;
                    int ban15 = 0, ban16 = 0, ban216 = 0, ban17 = 0, ban217 = 0, ban18 = 0, ban19 = 0, ban20 = 0, ban30 = 0; 
                    int bProIsem = 0, bProMic = 0, bProIssemym = 0,  bProSAME = 0, bProTHOMA = 0, bProUPUEBLA = 0, bProEHCM = 0, bProEHURU = 0,  bProMinds = 0,bProTHOMANES = 0, bProTHOMAMER = 0,bProAcre = 0, bProHEMO = 0,bProDDS = 0, bProEnseres = 0, bProAPM = 0, bProSonora = 0,bProSinergo = 0 ;
                    
                    Clave = request.getParameter("Clave");
                    Descripcion = request.getParameter("Descripcion");
                    radiosts = request.getParameter("radiosts");
                    radiorigen = request.getParameter("selectorigen");
                   Presentacion = request.getParameter("Presentacion"); 
                    Cat1 = request.getParameter("cat1");
                    Cat2 = request.getParameter("cat2");
                    Cat3 = request.getParameter("cat3");
                    Cat4 = request.getParameter("cat4");
                    Cat5 = request.getParameter("cat5");
                    Cat6 = request.getParameter("cat6");
                    Cat7 = request.getParameter("cat7");
                    Cat8 = request.getParameter("cat8");
                    Cat9 = request.getParameter("cat9");
                    Cat10 = request.getParameter("cat10");
                    Cat11 = request.getParameter("cat11");
                    Cat12 = request.getParameter("cat12");
                    Cat14 = request.getParameter("cat14");
                    Cat15 = request.getParameter("cat15");
                    Cat16 = request.getParameter("cat16");
                    Cat17 = request.getParameter("cat17");
                    Cat216 = request.getParameter("cat216");
                    Cat217 = request.getParameter("cat217");
                    Cat18 = request.getParameter("cat18");
                    Cat19 = request.getParameter("cat19");
                    Cat20 = request.getParameter("cat20");
                    Cat30 = request.getParameter("cat30");
                    ProIsem = request.getParameter("bProIsem");
                    ProMic = request.getParameter("bProMic");
                    ProIssemym = request.getParameter("bProIssemym");
                    Pro30 = request.getParameter("bProIsem");
                    ProSol = request.getParameter("bProIsem");
                    ProAnes = request.getParameter("bProIsem");
                    ProAnual = request.getParameter("bProIsem");
                    ProEem = request.getParameter("bProSAME");
                    ProThoma = request.getParameter("bProTHOMA");
                    ProUcin = request.getParameter("bProUPUEBLA");
                    ProCivilM = request.getParameter("bProEHCM");
                    ProUru = request.getParameter("bProEHURU");
                    ProMinds = request.getParameter("bProMinds");
                    ProThomAnes = request.getParameter("bProTHOMANES");
                    ProThomMer = request.getParameter("bProTHOMAMER");                   
                    ProAcre = request.getParameter("bProAcre");
                    ProHemo = request.getParameter("bProHEMO");
                    ProDDS = request.getParameter("bProDDS");
                    ProEnse = request.getParameter("bProEnseres");
                    ProAPM = request.getParameter("bProAPM");
                    ProSonora = request.getParameter("bProSonora");
                    ProSinergo = request.getParameter("bProSinergo");
                    //Cat100 = request.getParameter("cat100");
                    radiotipo = request.getParameter("radiotipo");
                    Costo = request.getParameter("Costo");
                    Incmen = request.getParameter("Incmen");
                    CostoNim = request.getParameter("CostoNim");
                    OrigeNim = request.getParameter("origennim");
                    GrupoNim = request.getParameter("GrupoNim");
                    ComentarioNim = request.getParameter("ComentarioNim");
                    CatalogoNim = request.getParameter("CatalogoNim");
                    CantRecibir = request.getParameter("CantRecibir");

                    /*if(radiocat == null){radiocat = "";}*/
                    //System.out.println("Cat:"+ radiocat);
                    if (Cat1 == null) {
                        Cat1 = "";
                        ban1 = 0;
                    } else {
                        ban1 = 1;
                    }
                    if (Cat2 == null) {
                        Cat2 = "";
                        ban2 = 0;
                    } else {
                        ban2 = 1;
                    }
                    if (Cat3 == null) {
                        Cat3 = "";
                        ban3 = 0;
                    } else {
                        ban3 = 1;
                    }
                    if (Cat4 == null) {
                        Cat4 = "";
                        ban4 = 0;
                    } else {
                        ban4 = 1;
                    }
                    if (Cat5 == null) {
                        Cat5 = "";
                        ban5 = 0;
                    } else {
                        ban5 = 1;
                    }
                    if (Cat6 == null) {
                        Cat6 = "";
                        ban6 = 0;
                    } else {
                        ban6 = 1;
                    }
                    if (Cat7 == null) {
                        Cat7 = "";
                        ban7 = 0;
                    } else {
                        ban7 = 1;
                    }
                    if (Cat8 == null) {
                        Cat8 = "";
                        ban8 = 0;
                    } else {
                        ban8 = 1;
                    }
                    if (Cat9 == null) {
                        Cat9 = "";
                        ban9 = 0;
                    } else {
                        ban9 = 1;
                    }
                    if (Cat10 == null) {
                        Cat10 = "";
                        ban10 = 0;
                    } else {
                        ban10 = 1;
                    }
                    if (Cat11 == null) {
                        Cat11 = "";
                        ban11 = 0;
                    } else {
                        ban11 = 1;
                    }
                    if (Cat12 == null) {
                        Cat12 = "";
                        ban12 = 0;
                    } else {
                        ban12 = 1;
                    }
                    if (Cat14 == null) {
                        Cat14 = "";
                        ban14 = 0;
                    } else {
                        ban14 = 1;
                    }
                    if (Cat15 == null) {
                        Cat15 = "";
                        ban15 = 0;
                    } else {
                        ban15 = 1;
                    }
                    if (Cat16 == null) {
                        Cat16 = "";
                        ban16 = 0;
                    } else {
                        ban16 = 1;
                    }
                    if (Cat17 == null) {
                        Cat17 = "";
                        ban17 = 0;
                    } else {
                        ban17 = 1;
                    }
                    if (Cat216 == null) {
                        Cat216 = "";
                        ban216 = 0;
                    } else {
                        ban216 = 1;
                    }
                    if (Cat217 == null) {
                        Cat217 = "";
                        ban217 = 0;
                    } else {
                        ban217 = 1;
                    }
                    if (Cat18 == null) {
                        Cat18 = "";
                        ban18 = 0;
                    } else {
                        ban18 = 1;
                    }
                    if (Cat19 == null) {
                        Cat19 = "";
                        ban19 = 0;
                    } else {
                        ban19 = 1;
                    }
                    if (Cat20 == null) {
                        Cat20 = "";
                        ban20 = 0;
                    } else {
                        ban20 = 1;
                    }
                    if (Cat30 == null) {
                        Cat30 = "";
                        ban30 = 0;
                    } else {
                        ban30 = 1;
                    }
                    if (ProIsem == null) {
                        ProIsem = "";
                        bProIsem = 0;
                    } else {
                        bProIsem = 1;
                    }
                    if (ProMic == null) {
                        ProMic = "";
                        bProMic = 0;
                    } else {
                        bProMic = 1;
                    }
                    if (ProIssemym == null) {
                        ProIssemym = "";
                        bProIssemym = 0;
                    } else {
                        bProIssemym = 1;
                    }
                    if (Pro30 == null) {
                        Pro30 = "";
                        bProIsem = 0;
                    } else {
                        bProIsem = 1;
                    }
                    if (ProSol == null) {
                        ProSol = "";
                        bProIsem  = 0;
                    } else {
                        bProIsem  = 1;
                    }
                    if (ProAnes == null) {
                        ProAnes = "";
                        bProIsem = 0;
                    } else {
                        bProIsem = 1;
                    }
                    if (ProAnual == null) {
                        ProAnual = "";
                        bProIsem = 0;
                    } else {
                        bProIsem = 1;
                    }
                    if (ProEem == null) {
                        ProEem = "";
                        bProSAME = 0;
                    } else {
                        bProSAME = 1;
                    }
                    if (ProThoma == null) {
                        ProThoma = "";
                         bProTHOMA = 0;
                    } else {
                         bProTHOMA = 1;
                    }
                    if (ProUcin == null) {
                        ProUcin = "";
                        bProUPUEBLA = 0;
                    } else {
                        bProUPUEBLA = 1;
                    }
                    if (ProCivilM == null) {
                        ProCivilM = "";
                        bProUPUEBLA = 0;
                    } else {
                        bProUPUEBLA = 1;
                    }
                    if (ProMinds == null) {
                        ProMinds = "";
                        bProMinds = 0;
                    } else {
                        bProMinds = 1;
                    }
                     if (ProThomAnes == null) {
                        ProThomAnes = "";
                        bProTHOMANES = 0;
                    } else {
                        bProTHOMANES = 1;
                    } 
                     if (ProThomMer == null) {
                        ProThomMer = "";
                         bProTHOMAMER = 0;
                    } else {
                         bProTHOMAMER = 1;
                    }
                     if (ProAcre == null) {
                        ProAcre = "";
                        bProAcre = 0;
                    } else {
                        bProAcre = 1;
                    }
                      if (ProHemo == null) {
                        ProHemo = "";
                        bProHEMO = 0;
                    } else {
                        bProHEMO = 1;
                    }
                       if (ProDDS == null) {
                        ProDDS = "";
                        bProDDS = 0;
                    } else {
                        bProDDS = 1;
                    }
                        if (ProEnse == null) {
                        ProEnse = "";
                        bProEnseres = 0;
                    } else {
                        bProEnseres = 1;
                    }
                        if (ProAPM == null) {
                        ProAPM = "";
                        bProAPM = 0;
                    } else {
                        bProAPM = 1;
                    }
                    if (ProSonora == null) {
                        ProSonora = "";
                        bProSonora = 0;
                    } else {
                        bProSonora = 1;
                    }
                     if (ProSinergo == null) {
                        ProSinergo = "";
                        bProSinergo = 0;
                    } else {
                        bProSinergo = 1;
                    }    

                    if ((Clave != "") && (Descripcion != "") && (radiosts != "") && (radiorigen != "") && (radiotipo != "") && (Costo != "") && (Incmen != "") && (CostoNim != "") && (OrigeNim != "") && (GrupoNim != "") && (ComentarioNim != "") && (CatalogoNim != "") && (CantRecibir != "")) {

                        /*
                        con.actualizar("UPDATE tb_medica SET F_N216='0',F_N217='0' WHERE F_ClaPro='" + Clave + "';");

                        if ((ban2 == 1) && (ban16 == 1) && (ban17 == 1)) {
                            ban216 = 1;
                            ban217 = 1;
                        } else if ((ban2 == 1) && (ban16 == 1)) {
                            ban216 = 1;
                        } else if ((ban2 == 1) && (ban17 == 1)) {
                            ban217 = 1;
                        }
                         */
                        con.actualizar("UPDATE tb_medica SET F_DesPro='" + Descripcion + "',F_StsPro='" + radiosts + "',F_TipMed='" + radiotipo + "',F_Costo='" + Costo + "',F_PrePro='"+Presentacion+"',F_Origen='" + radiorigen + "',F_IncMen='" + Incmen + "',F_N1='" + ban1 + "',F_N2='" + ban2 + "',F_N3='" + ban3 + "',F_N4='" + ban4 + "',F_N5='" + ban5 + "',F_N6='" + ban6 + "',F_N7='" + ban7 + "',F_N8='" + ban8 + "',F_N9='" + ban9 + "',F_N10='" + ban10 + "',F_N11='" + ban11 + "',F_N12='" + ban12 + "',F_N14='" + ban14 + "',F_N15='" + ban15 + "',F_N16='" + ban16 + "',F_N216='" + ban216 + "',F_N17='" + ban17 + "',F_N217='" + ban217 + "',F_N18='" + ban18 + "',F_N19='" + ban19 + "',F_N20='" + ban20 + "',F_N30='" + ban30 + "',F_OriNim='" + OrigeNim + "',F_CostoNim='" + CostoNim + "',F_Grupo='" + GrupoNim + "',F_Comentario='" + ComentarioNim + "',F_Catalogo='" + CatalogoNim + "',F_CantMax='" + CantRecibir + "',F_ProIsem='" + bProIsem + "',F_ProMichoacan='" + bProMic + "',F_ProIssemym='" + bProIssemym + "',F_ProSamedic='" + bProSAME + "' ,F_ProThomasant='" + bProTHOMA + "' ,F_ProUcinpuebla='" + bProUPUEBLA + "' ,F_proCivilMor='" + bProEHCM + "' ,F_ProEHUruapan='" + bProEHURU + "' ,F_ProMinds='" + bProMinds + "' ,F_ProThomaIsem = '" + bProTHOMANES + "' ,F_ProThomaMerida='" + bProTHOMAMER + "',F_ProMichAcre='" + bProAcre + "',F_ProMichHemodi='" + bProHEMO + "',F_ProDDS='" + bProDDS + "',F_ProEnseres='" + bProEnseres + "',F_ProAPM='" + bProAPM + "',F_ProSonora='" + bProSonora + "',F_ProSinergo='" + bProSinergo + "' WHERE F_ClaPro='" + Clave + "';");
                        ban217 = 0;
                        out.println("<script>alert('Medicamento Modificado correctamente.')</script>");
                        out.println("<script>window.location='medicamento.jsp'</script>");
                    } else {
                        out.println("<script>alert('Favor de llenar todos los campos')</script>");
                        out.println("<script>window.location='ModiClave.jsp?Clave=" + Clave + "'</script>");
                    }
                    con.cierraConexion();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (request.getParameter("accion").equals("ActualizarUnidis")) {
                try {
                    con.conectar();
                    String Destina = "", coordi = "", doctor = "", Id = "", Nombre = "", Local = "", Muni = "", Region = "", Nivel = "";
                    String m1 = "", m2 = "", m3 = "", m4 = "", m5 = "", m6 = "", m7 = "", m8 = "", m9 = "", m10 = "";
                    Id = request.getParameter("Id");
                    Destina = request.getParameter("Destina");
                    coordi = request.getParameter("coordi");
                    doctor = request.getParameter("doctor");
                    m1 = request.getParameter("m1");
                    m2 = request.getParameter("m2");
                    m3 = request.getParameter("m3");
                    m4 = request.getParameter("m4");
                    m5 = request.getParameter("m5");
                    m6 = request.getParameter("m6");
                    m7 = request.getParameter("m7");
                    m8 = request.getParameter("m8");
                    m9 = request.getParameter("m9");
                    m10 = request.getParameter("m10");

                    Local = request.getParameter("localidad");
                    Muni = request.getParameter("municipio");
                    Region = request.getParameter("region");
                    Nivel = request.getParameter("nivel");
                    Nombre = request.getParameter("descrip");

                    con.actualizar("UPDATE tb_unidis SET F_DesUniIS='" + Nombre + "',F_LocUniIS='" + Local + "',F_MunUniIS='" + Muni + "',F_RegUniIS='" + Region + "',F_NivUniIS='" + Nivel + "',F_ClaSap='" + Destina + "',F_CooUniIS='" + coordi + "',F_MedUniIS='" + doctor + "',F_ClaInt1='" + m1 + "',F_ClaInt2='" + m2 + "',F_ClaInt3='" + m3 + "',F_ClaInt4='" + m4 + "',F_ClaInt5='" + m5 + "',F_ClaInt6='" + m6 + "',F_ClaInt7='" + m7 + "',F_ClaInt8='" + m8 + "',F_ClaInt9='" + m9 + "',F_ClaInt10='" + m10 + "' WHERE F_Id='" + Id + "'");
                    out.println("<script>alert('Unidad Modificado correctamente.')</script>");
                    out.println("<script>window.location='facturacion/catalogoUniDis.jsp'</script>");

                    con.cierraConexion();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (request.getParameter("accion").equals("RegistrarUnidis")) {
                try {
                    con.conectar();
                    String Clave = "", Nombre = "", Juris = "", Muni = "", Local = "", coordi = "", doctor = "", Region = "", Nivel = "";
                    String m1 = "", m2 = "", m3 = "", m4 = "", m5 = "";

                    Clave = request.getParameter("Clave");
                    Nombre = request.getParameter("Nombre");
                    Juris = request.getParameter("selectJuris");
                    Muni = request.getParameter("selectMuni");
                    Local = request.getParameter("selectLoc");
                    coordi = request.getParameter("selectCoo");
                    Region = request.getParameter("region");
                    Nivel = request.getParameter("nivel");
                    doctor = request.getParameter("selectDoct");

                    m1 = request.getParameter("selectM1");
                    m2 = request.getParameter("selectM2");
                    m3 = request.getParameter("selectM3");
                    m4 = request.getParameter("selectM4");
                    m5 = request.getParameter("selectM5");

                    if (m1.equals("--Seleccione M1--")) {
                        m1 = "";
                    }
                    if (m2.equals("--Seleccione M2--")) {
                        m2 = "";
                    }
                    if (m3.equals("--Seleccione M3--")) {
                        m3 = "";
                    }
                    if (m4.equals("--Seleccione M4--")) {
                        m4 = "";
                    }
                    if (m5.equals("--Seleccione M5--")) {
                        m5 = "";
                    }

                    if ((Clave != "") && (Nombre != "") && (Juris != "") && (Muni != "") && (Local != "") && (coordi != "") && (Region != "") && (Nivel != "") && (doctor != "") && (m1 != "")) {
                        int Contar = 0;
                        ResultSet ContarReg = con.consulta("SELECT COUNT(F_ClaUniIS) FROM tb_unidis WHERE F_ClaUniIS='" + Clave + "' AND F_LocUniIS='" + Local + "' AND F_MunUniIS='" + Muni + "' AND F_JurUniIS='" + Juris + "';");
                        if (ContarReg.next()) {
                            Contar = ContarReg.getInt(1);
                        }
                        if (Contar == 0) {
                            con.insertar("INSERT INTO tb_unidis VALUES('" + Clave + "','" + Nombre + "','" + m1 + "','" + m2 + "','" + m3 + "','" + m4 + "','" + m5 + "','" + doctor + "','" + Local + "','" + Muni + "','" + Juris + "','" + Region + "','" + Nivel + "','','','','','','" + Clave + "','" + coordi + "','',0);");
                            out.println("<script>alert('Unidad Registrada correctamente.')</script>");
                            out.println("<script>window.location='facturacion/catalogoUniDis.jsp'</script>");
                        } else {
                            out.println("<script>alert('Favor de verificar, datos existentes.')</script>");
                            out.println("<script>window.history.back()</script>");
                        }

                    } else {
                        out.println("<script>alert('Favor de verificar, campos vacíos.')</script>");
                        out.println("<script>window.history.back()</script>");
                    }
                    con.cierraConexion();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (request.getParameter("accion").equals("RegistrarInsumo")) {
                try {
                    con.conectar();
                    String Clave = "", Nombre = "", ClaInt = "", Presenta = "", Suministro = "", PA = "", SP = "", Causes = "", Espacio = "", ClaveIsem = "";
                    int Long = 0;
                    Clave = request.getParameter("Clave");
                    Nombre = request.getParameter("Nombre");
                    ClaInt = request.getParameter("selectClave");
                    Presenta = request.getParameter("presenta");
                    Suministro = request.getParameter("suministro");
                    PA = request.getParameter("pa");
                    SP = request.getParameter("sp");
                    Causes = request.getParameter("causes");

                    if ((Clave != "") && (Nombre != "") && (ClaInt != "") && (Presenta != "") && (Suministro != "") && (PA != "") && (SP != "") && (Causes != "")) {
                        int Contar = 0;
                        ResultSet ContarReg = con.consulta("SELECT COUNT(F_ClaInt) FROM tb_artiis WHERE F_ClaInt='" + ClaInt + "';");
                        if (ContarReg.next()) {
                            Contar = ContarReg.getInt(1);
                        }
                        if (Contar == 0) {
                            Long = Clave.length();
                            for (int E = Long; E < 15; E++) {
                                Espacio = Espacio + " ";
                            }
                            ClaveIsem = Espacio + Clave;

                            con.insertar("INSERT INTO tb_artiis VALUES('" + ClaveIsem + "','" + Nombre + "','" + ClaInt + "','0','0','" + Presenta + "','" + Suministro + "','" + PA + "','" + SP + "','" + Causes + "','','1');");
                            out.println("<script>alert('Insumo Registrado correctamente.')</script>");
                            out.println("<script>window.location='facturacion/catalogoArtIS.jsp'</script>");
                        } else {
                            out.println("<script>alert('Favor de verificar, datos existentes.')</script>");
                            out.println("<script>window.history.back()</script>");
                        }

                    } else {
                        out.println("<script>alert('Favor de verificar, campos vacíos.')</script>");
                        out.println("<script>window.history.back()</script>");
                    }
                    con.cierraConexion();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (request.getParameter("accion").equals("Agregar")) {
                try {
                    con.conectar();
                    String Clave = "", ClaveSS = "", Descripcion = "",Presentacion = "", radiosts = "", radiorigen = "", radiocat = "", radiotipo = "", Costo = "", Incmen = "", CostoNim = "", Prioridad = "";
                    String OrigeNim = "", GrupoNim = "", ComentarioNim = "", CatalogoNim = "", CantRecibir = "", ProIsem = "", ProMic = "", ProIssemym = "",  Pro30 = "",ProSol = "",ProAnes = "",ProAnual = "",ProEem = "",ProThoma= "",ProUcin= "",ProCivilM = "", ProUru = "", ProMinds = "", ProThomAnes = "",ProThomMer = "",ProAcre = "",ProHemo = "",ProDDS = "", ProEnse = "", ProAPM = "", ProSonora = "",ProSinergo = "" ;
                    String Cat1 = "", Cat2 = "", Cat3 = "", Cat4 = "", Cat5 = "", Cat6 = "", Cat7 = "", Cat8 = "", Cat9 = "", Cat10 = "", Cat11 = "", Cat12 = "";
                    String Cat14 = "", Cat15 = "", Cat16 = "", Cat17 = "", Cat216 = "", Cat217 = "", Cat18 = "", Cat19 = "", Cat20 = "", Cat30 = "", Cat100 = "";
                    int ban1 = 0, ban2 = 0, ban3 = 0, ban4 = 0, ban5 = 0, ban6 = 0, ban7 = 0, ban8 = 0, ban9 = 0, ban10 = 0, ban11 = 0, ban12 = 0, ban14 = 0;
                    int ban15 = 0, ban16 = 0, ban216 = 0, ban17 = 0, ban217 = 0, ban18 = 0, ban19 = 0, ban20 = 0, ban30 = 0;
                    int bProIsem = 0, bProMic = 0, bProIssemym = 0,  bProSAME = 0, bProTHOMA = 0, bProUPUEBLA = 0, bProEHCM = 0, bProEHURU = 0,  bProMinds = 0,bProTHOMANES = 0, bProTHOMAMER = 0,bProAcre = 0, bProHEMO = 0,bProDDS = 0, bProEnseres = 0, bProAPM = 0,bProSonora = 0,bProSinergo = 0  ;
                    int Cont = 0;
                    Clave = request.getParameter("Clave");
                    ClaveSS = request.getParameter("ClaveSS");
                    Descripcion = request.getParameter("Descripcion");
                    radiosts = request.getParameter("radiosts");
                    radiorigen = request.getParameter("radiorigen");
                    Presentacion = request.getParameter("Presentacion");                    
                    Cat1 = request.getParameter("cat1");
                    Cat2 = request.getParameter("cat2");
                    Cat3 = request.getParameter("cat3");
                    Cat4 = request.getParameter("cat4");
                    Cat5 = request.getParameter("cat5");
                    Cat6 = request.getParameter("cat6");
                    Cat7 = request.getParameter("cat7");
                    Cat8 = request.getParameter("cat8");
                    Cat9 = request.getParameter("cat9");
                    Cat10 = request.getParameter("cat10");
                    Cat11 = request.getParameter("cat11");
                    Cat12 = request.getParameter("cat12");
                    Cat14 = request.getParameter("cat14");
                    Cat15 = request.getParameter("cat15");
                    Cat16 = request.getParameter("cat16");
                    Cat17 = request.getParameter("cat17");
                    Cat216 = request.getParameter("cat216");
                    Cat217 = request.getParameter("cat217");
                    Cat18 = request.getParameter("cat18");
                    Cat19 = request.getParameter("cat19");
                    Cat20 = request.getParameter("cat20");
                    Cat30 = request.getParameter("cat30");
                    ProIsem = request.getParameter("bProIsem");
                    ProIssemym = request.getParameter("bProIssemym");
                    Pro30 = request.getParameter("bProIsem");
                    ProSol = request.getParameter("bProIsem");
                    ProAnes = request.getParameter("bProIsem");
                    ProAnual = request.getParameter("bProIsem");
                    ProEem = request.getParameter("bProSAME");
                    ProThoma = request.getParameter("bProTHOMA");
                    ProUcin = request.getParameter("bProUPUEBLA");
                    ProCivilM = request.getParameter("bProEHCM");
                    ProUru = request.getParameter("bProEHURU");
                    ProMinds = request.getParameter("bProMinds");
                    ProThomAnes = request.getParameter("bProTHOMANES");
                    ProThomMer = request.getParameter("bProTHOMAMER");                   
                    ProAcre = request.getParameter("bProAcre");
                    ProHemo = request.getParameter("bProHEMO");
                    ProDDS = request.getParameter("bProDDS");
                    ProEnse = request.getParameter("bProEnseres");
                    ProAPM = request.getParameter("bProAPM");
                    ProSonora = request.getParameter("bProSonora");
                    ProSinergo = request.getParameter("bProSinergo");
                    radiotipo = request.getParameter("radiotipo");
                    Costo = request.getParameter("Costo");
                    Incmen = request.getParameter("Incmen");

                    CostoNim = request.getParameter("CostoNim");
                    OrigeNim = request.getParameter("origennim");
                    GrupoNim = request.getParameter("GrupoNim");
                    ComentarioNim = request.getParameter("ComentarioNim");
                    CatalogoNim = request.getParameter("CatalogoNim");
                    CantRecibir = request.getParameter("CantRecibir");
                    Prioridad = request.getParameter("Prioridad");
                    if (Prioridad == null) {
                        Prioridad = "";
                    }
                    /*if(radiocat == null){
                        radiocat = "";
                    }*/
                    //System.out.println("Cat:"+ radiocat);
                    if (Cat1 == null) {
                        Cat1 = "";
                        ban1 = 0;
                    } else {
                        ban1 = 1;
                    }
                    if (Cat2 == null) {
                        Cat2 = "";
                        ban2 = 0;
                    } else {
                        ban2 = 1;
                    }
                    if (Cat3 == null) {
                        Cat3 = "";
                        ban3 = 0;
                    } else {
                        ban3 = 1;
                    }
                    if (Cat4 == null) {
                        Cat4 = "";
                        ban4 = 0;
                    } else {
                        ban4 = 1;
                    }
                    if (Cat5 == null) {
                        Cat5 = "";
                        ban5 = 0;
                    } else {
                        ban5 = 1;
                    }
                    if (Cat6 == null) {
                        Cat6 = "";
                        ban6 = 0;
                    } else {
                        ban6 = 1;
                    }
                    if (Cat7 == null) {
                        Cat7 = "";
                        ban7 = 0;
                    } else {
                        ban7 = 1;
                    }
                    if (Cat8 == null) {
                        Cat8 = "";
                        ban8 = 0;
                    } else {
                        ban8 = 1;
                    }
                    if (Cat9 == null) {
                        Cat9 = "";
                        ban9 = 0;
                    } else {
                        ban9 = 1;
                    }
                    if (Cat10 == null) {
                        Cat10 = "";
                        ban10 = 0;
                    } else {
                        ban10 = 1;
                    }
                    if (Cat11 == null) {
                        Cat11 = "";
                        ban11 = 0;
                    } else {
                        ban11 = 1;
                    }
                    if (Cat12 == null) {
                        Cat12 = "";
                        ban12 = 0;
                    } else {
                        ban12 = 1;
                    }
                    if (Cat14 == null) {
                        Cat14 = "";
                        ban14 = 0;
                    } else {
                        ban14 = 1;
                    }
                    if (Cat15 == null) {
                        Cat15 = "";
                        ban15 = 0;
                    } else {
                        ban15 = 1;
                    }
                    if (Cat16 == null) {
                        Cat16 = "";
                        ban16 = 0;
                    } else {
                        ban16 = 1;
                    }
                    if (Cat17 == null) {
                        Cat17 = "";
                        ban17 = 0;
                    } else {
                        ban17 = 1;
                    }
                    if (Cat216 == null) {
                        Cat216 = "";
                        ban216 = 0;
                    } else {
                        ban216 = 1;
                    }
                    if (Cat217 == null) {
                        Cat217 = "";
                        ban217 = 0;
                    } else {
                        ban217 = 1;
                    }
                    if (Cat18 == null) {
                        Cat18 = "";
                        ban18 = 0;
                    } else {
                        ban18 = 1;
                    }
                    if (Cat19 == null) {
                        Cat19 = "";
                        ban19 = 0;
                    } else {
                        ban19 = 1;
                    }
                    if (Cat20 == null) {
                        Cat20 = "";
                        ban20 = 0;
                    } else {
                        ban20 = 1;
                    }
                    if (Cat30 == null) {
                        Cat30 = "";
                        ban30 = 0;
                    } else {
                        ban30 = 1;
                    }
                    if (ProIsem == null) {
                        ProIsem = "";
                        bProIsem = 0;
                    } else {
                        bProIsem = 1;
                    }
                    if (ProMic == null) {
                        ProMic = "";
                        bProMic = 0;
                    } else {
                        bProMic = 1;
                    }
                    if (ProIssemym == null) {
                        ProIssemym = "";
                        bProIssemym = 0;
                    } else {
                        bProIssemym = 1;
                    }

                    if (Pro30 == null) {
                        Pro30 = "";
                        bProIsem = 0;
                    } else {
                        bProIsem = 1;
                    }
                    if (ProSol == null) {
                        ProSol = "";
                        bProIsem = 0;
                    } else {
                        bProIsem = 1;
                    }
                    if (ProAnes == null) {
                        ProAnes = "";
                        bProIsem = 0;
                    } else {
                        bProIsem = 1;
                    }
                    if (ProAnual == null) {
                        ProAnual = "";
                        bProIsem = 0;
                    } else {
                        bProIsem = 1;
                    }
                    if (ProEem == null) {
                        ProEem = "";
                        bProSAME = 0;
                    } else {
                        bProSAME = 1;
                    }
                    if (ProThoma == null) {
                        ProThoma = "";
                        bProTHOMA = 0;
                    } else {
                        bProTHOMA = 1;
                    }
                    if (ProUcin == null) {
                        ProUcin = "";
                        bProUPUEBLA = 0;
                    } else {
                        bProUPUEBLA = 1;
                    }
                    if (ProCivilM == null) {
                        ProCivilM = "";
                        bProUPUEBLA = 0;
                    } else {
                        bProUPUEBLA = 1;
                    }
                    if (ProMinds == null) {
                        ProMinds = "";
                        bProMinds = 0;
                    } else {
                        bProMinds = 1;
                    }
                    if (ProThomAnes == null) {
                        ProThomAnes = "";
                        bProTHOMANES = 0;
                    } else {
                        bProTHOMANES = 1;
                    }
                    if (ProThomMer == null) {
                        ProThomMer = "";
                        bProTHOMAMER = 0;
                    } else {
                        bProTHOMAMER = 1;
                    }
                    if (ProAcre == null) {
                        ProAcre = "";
                        bProAcre = 0;
                    } else {
                        bProAcre = 1;
                    }
                    if (ProHemo == null) {
                        ProHemo = "";
                        bProHEMO = 0;
                    } else {
                        bProHEMO = 1;
                    }
                    if (ProDDS == null) {
                        ProDDS = "";
                        bProDDS = 0;
                    } else {
                        bProDDS = 1;
                    }
                    if (ProEnse == null) {
                        ProEnse = "";
                        bProEnseres = 0;
                    }
                    if (ProAPM == null) {
                        ProAPM = "";
                        bProAPM = 0;
                    } else {
                        bProAPM = 1;
                    }
                    if (ProSonora == null) {
                        ProSonora = "";
                        bProSonora = 0;
                    } else {
                        bProSonora = 1;
                    }
                     if (ProSinergo == null) {
                        ProSinergo = "";
                        bProSinergo = 0;
                    } else {
                        bProSinergo = 1;
                    }
 
                    if ((Clave != "") && (Descripcion != "") && (radiosts != "") && (radiorigen != "") && (radiotipo != "") && (Costo != "") && (Incmen != "") && (CostoNim != "") && (OrigeNim != "") && (GrupoNim != "") && (ComentarioNim != "") && (CatalogoNim != "") && (CantRecibir != "") && (Prioridad != "")) {

                        ResultSet DClave = con.consulta("SELECT * FROM tb_medica WHERE F_ClaPro='" + Clave + "'");
                        if (DClave.next()) {
                            Cont++;
                        }
                        if (Cont > 0) {
                            out.println("<script>alert('Medicamento Existente.')</script>");
                            out.println("<script>window.location='AltaClave.jsp'</script>");
                        } else {
                            /*
                            if ((ban2 == 1) && (ban16 == 1) && (ban17 == 1)) {
                                ban216 = 1;
                                ban217 = 1;
                            } else if ((ban2 == 1) && (ban16 == 1)) {
                                ban216 = 1;
                            } else if ((ban2 == 1) && (ban17 == 1)) {
                                ban217 = 1;
                            }
                             */
                            con.actualizar("INSERT INTO tb_medica VALUES('" + Clave + "','" + ClaveSS + "','" + Descripcion + "','" + radiosts + "','" + radiotipo + "','" + Costo + "','"+ Presentacion +"','" + radiorigen + "','" + radiocat + "','0','0','0','" + Incmen + "','" + ban1 + "','" + ban2 + "','" + ban3 + "','" + ban4 + "','" + ban5 + "','" + ban6 + "','" + ban7 + "','" + ban8 + "','" + ban9 + "','" + ban10 + "','" + ban11 + "','" + ban12 + "','" + ban14 + "','" + ban15 + "','" + ban16 + "','" + ban216 + "','" + ban17 + "','" + ban217 + "','" + ban18 + "','" + ban19 + "','" + ban20 + "','" + ban30 + "','" + OrigeNim + "','" + CostoNim + "','" + GrupoNim + "','" + ComentarioNim + "','" + CatalogoNim + "','" + CantRecibir + "','" + Prioridad + "','" + bProIsem + "','" + bProMic + "','" + bProIssemym + "','" + bProSAME + "','"+ bProTHOMA +"','"+ bProUPUEBLA +"','"+ bProEHCM +"','"+ bProEHURU +"','"+ bProMinds +"','"+ bProTHOMANES +"','"+ bProTHOMAMER +"','"+ bProAcre +"','"+ bProHEMO +"','"+ bProDDS +"','"+ bProEnseres +"','"+ bProAPM +"','"+ bProSonora +"','"+ bProSinergo +"');");
                            ban216 = 0;
                            ban217 = 0;
                            out.println("<script>alert('Medicamento Guardado correctamente.')</script>");
                            out.println("<script>window.location='AltaClave.jsp'</script>");
                        }

                    } else {
                        out.println("<script>alert('Favor de llenar todos los campos')</script>");
                        out.println("<script>window.location='AltaClave.jsp?Clave=" + Clave + "'</script>");
                    }
                    con.cierraConexion();

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            }
            if (request.getParameter("accion").equals("Actualizar")) {
                try {
                    conModula.conectar();
                    con.conectar();
                    ResultSet rset = con.consulta("select * from tb_medica");
                    while (rset.next()) {
                        int banMedicamento = 0;
                        ResultSet rset2 = con.consulta("select F_Id, F_Min from tb_maxmodula where F_ClaPro = '" + rset.getString("F_ClaPro") + "'");
                        while (rset2.next()) {
                            banMedicamento = 1;
                        }
                        if (banMedicamento == 0) {
                            try {
                                con.insertar("insert into tb_maxmodula values('" + rset.getString("F_ClaPro") + "','" + request.getParameter("Max" + rset.getString("F_ClaPro").trim()) + "','" + request.getParameter("Min" + rset.getString("F_ClaPro").trim()) + "','0')");
                            } catch (Exception e) {
                            }
                        } else {

                            try {

                                con.insertar("update tb_maxmodula set F_Max = '" + request.getParameter("Max" + rset.getString("F_ClaPro")) + "', F_Min ='" + request.getParameter("Min" + rset.getString("F_ClaPro")) + "' where F_ClaPro='" + rset.getString("F_ClaPro") + "'");
                            } catch (Exception e) {
                            }
                        }
                        rset2 = con.consulta("select F_Id, F_Min from tb_maxmodula where F_ClaPro = '" + rset.getString("F_ClaPro") + "'");
                        int min = 0;
                        while (rset2.next()) {
                            banMedicamento = 1;
                            min = rset2.getInt("F_Min");
                        }
                        conModula.ejecutar("insert into IMP_ARTICOLI (ART_OPERAZIONE, ART_ARTICOLO, ART_DES, ART_UMI, ART_MIN, ART_PROY) values ('I','" + rset.getString("F_ClaPro") + "','" + rset.getString("F_DesPro") + "','PZ','" + min + "', 'ISSEMyM')");
                    }
                    conModula.cierraConexion();
                    con.cierraConexion();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    out.println("<script>alert('Error: " + e.getMessage() + "')</script>");
                    out.println("<script>window.location='medicamento.jsp'</script>");
                }
                out.println("<script>window.location='medicamento.jsp'</script>");
            }
            if (request.getParameter("accion").equals("obtieneProvee")) {
                try {
                    out.println("<script>alert('Se obtuvieron los Proveedores Correctamente')</script>");
                } catch (Exception e) {
                    out.println("<script>alert('Error al obtener proveedores')</script>");
                }
                out.println("<script>window.location='catalogo.jsp'</script>");
            }
        } catch (SQLException e) {
            e.getMessage();
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
