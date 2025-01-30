/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.service;

import conn.ConectionDB;
import in.co.sneh.controller.vo.ThomasantRequirement;
import in.co.sneh.model.CargaRequerimiento;
import in.co.sneh.model.MonitorReq;
import in.co.sneh.model.Requerimiento;
import in.co.sneh.persistance.CargaReqDAOImpl;
import in.co.sneh.persistance.MonitorReqDAOImpl;
import in.co.sneh.persistance.RequerimientoDAOImpl;
import in.co.sneh.service.exception.RequerimentException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP-MEDALFA
 */
public class RequerimientoService {

    private Connection con;

    public RequerimientoService() {
    }

    public Message cargarRequerimientoThomasant(ThomasantRequirement req, String user) {
        String claveError="";
        try {
            List<CargaRequerimiento> requerimientos = req.buildCR();

            ConectionDB c = new ConectionDB();
            c.conectar();
            this.con = c.getConn();
            this.con.setAutoCommit(false);
            CargaReqDAOImpl cargaDao = new CargaReqDAOImpl(con);
            MonitorReqDAOImpl monitorDao = new MonitorReqDAOImpl(con);
            RequerimientoDAOImpl reqDao = new RequerimientoDAOImpl(con);

            cargaDao.deleteByUser(user);

            claveError = cargaDao.insert(requerimientos);
            List<MonitorReq> monitores = monitorDao.findDataByUser(user);
            monitorDao.insertar(monitores);

            List<String> unidades = cargaDao.getClaUniByUser(user);
            reqDao.updateStatus(unidades, 0);

            for (CargaRequerimiento cr : requerimientos) {
                reqDao.insert(cr);
            }
            con.commit();
            c.cierraConexion();
            
        } catch (SQLException ex) {
            try {
                String error = ex.getMessage();
                Integer tipo = 0;
                if(error.contains("F_ClaCli")){
                    tipo = 1;
                }
                if(error.contains("F_ClaPro")){
                    tipo = 2;
                }
//                Logger.getLogger(RequerimientoService.class.getName()).log(Level.SEVERE, null, ex);
                Message m = new Message("Error al cargar el requerimiento "+ claveError, 400, tipo);
                con.rollback();
                System.out.println(m.toString());
                return m;
            } catch (SQLException ex1) {
//                Logger.getLogger(RequerimientoService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (RequerimentException ex) {
//            ex.printStackTrace();
            Message m = new Message("Error al cargar el requerimiento, Clave: "+ ex.getMessage(), 400, ex.getType());
            System.out.println(m.toString());
            return m;
        }
        Message m = new Message("Requerimiento cargado correctamente", 200, 0);
        System.out.println(m.toString());
        return m;
    }

}
