/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.dao.impl;

import com.gnk.model.DetalleFactura;
import com.medalfa.saa.model.NotificacionFoliosUrgentes;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP-MEDALFA
 */
public class NotificacionFoliosUrgentesDAO {

    Connection c;

    public NotificacionFoliosUrgentesDAO(Connection c) {
        this.c = c;
    }

    public NotificacionFoliosUrgentes getNotificacionPendiente() {
        try {
            if (this.c != null) {
                String queryString = "SELECT n.*, rs.F_ClaCli, u.F_NomCli, rs.F_TipoReq, TIMESTAMPADD(second,10,fecha_creacion) as delay from notificacion_urgente n "
                        + "inner join tb_requerimientos rs on rs.F_IdReq = n.folio "
                        + "inner join tb_uniatn u on u.F_ClaCli = rs.F_ClaCli "
                        + "where n.status = 0 and rs.F_StsReq in (4,8,11) having delay < now() order by id_notificacion limit 1";
                PreparedStatement ps = c.prepareStatement(queryString);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    queryString = "UPDATE notificacion_urgente SET status = 1 where id_notificacion = ?";
                    ps = this.c.prepareStatement(queryString);
                    ps.setInt(1, rs.getInt(1));
                    ps.executeUpdate();
                    return this.buildEntity(rs);
                }
            }else{
                System.out.println("No se pudo crear la conexión");
            }
        } catch (SQLException ex) {
            Logger.getLogger(NotificacionFoliosUrgentes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private NotificacionFoliosUrgentes buildEntity(ResultSet rs) {
        try {
            NotificacionFoliosUrgentes notificacion = new NotificacionFoliosUrgentes(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));
            notificacion.setUnidad(rs.getString(6) + " - " + rs.getString(7));
            notificacion.setClaCli(rs.getString(6));
            notificacion.setPrioridad(rs.getInt(8));
            return notificacion;
        } catch (SQLException ex) {
            Logger.getLogger(NotificacionFoliosUrgentesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<DetalleFactura> getDetails(NotificacionFoliosUrgentes notificacion) {
        List<DetalleFactura> lista = new ArrayList<DetalleFactura>();
        try {

            String queryString = this.getDetailsQuery(notificacion.getTipo());
            PreparedStatement ps = c.prepareStatement(queryString);
            ps.setInt(1, notificacion.getFolio());
            System.out.println(ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DetalleFactura detail = new DetalleFactura();
                detail.setClave(rs.getString(1));
                detail.setPiezas(rs.getInt(3));
                detail.setObservaciones(rs.getString(2));
                detail.setOc(rs.getString(4));
                lista.add(detail);
            }

        } catch (SQLException ex) {
            Logger.getLogger(NotificacionFoliosUrgentesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lista;
    }

    private String getDetailsQuery(Integer tipo) {
        switch (tipo) {
            case 1:
                return "Select r.F_ClaPro, IFNULL(m.F_DesPro,'FUERA DE CATALOGO') as F_DesPro, r.F_Cant, '' as F_Lote  FROM tb_detrequerimiento r left join tb_medica m on r.F_ClaPro = m.F_ClaPro where F_SurtidoPor = 1 "
                        + "AND r.F_IdReq = ?;";
            case 2:
                return "Select r.F_ClaPro, IFNULL(m.F_DesPro,'FUERA DE CATALOGO') as F_DesPro, r.F_Entrega, '' as F_Lote  FROM tb_detreqcatalogo r left join tb_medica m on r.F_ClaPro = m.F_ClaPro where F_SurtidoPor = 1 "
                        + "AND r.F_IdReq = ?;";
            case 3:
                return "Select r.F_ClaPro, IFNULL(m.F_DesPro,'FUERA DE CATALOGO') as F_DesPro, r.F_Entrega, '' as F_Lote  FROM tb_detreqstock r left join tb_medica m on r.F_ClaPro = m.F_ClaPro where F_SurtidoPor = 1 "
                        + "AND r.F_IdReq = ?;";
        }
        return null;
    }
}
