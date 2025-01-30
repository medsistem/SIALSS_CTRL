/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.picking.service;

import conn.ConectionDB;
import in.co.sneh.picking.persistance.FolioDAOImpl;
import java.sql.Connection;

/**
 *
 * @author HP-MEDALFA
 */
public class PickingService {
    
    private Connection con;
    private FolioDAOImpl folioDao;
    
    public PickingService(){
        ConectionDB c = new ConectionDB();
        this.con = c.getConn();
        folioDao = new FolioDAOImpl(this.con);
    }
    
    public int isFolioEditable(Integer folio, Integer proyecto){
        int status = this.folioDao.getFolioStatus(folio, proyecto);
        return status;
    }
}
