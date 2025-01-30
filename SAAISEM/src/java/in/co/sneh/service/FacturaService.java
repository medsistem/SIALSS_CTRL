/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.service;

import conn.ConectionDB;
import in.co.sneh.model.Apartado;
import in.co.sneh.persistance.ApartadoDAOImpl;
import java.sql.Connection;

/**
 *
 * @author HP-MEDALFA
 */
public class FacturaService {
    
    private Connection con;
    ApartadoDAOImpl apartadoDAO;
    
    public FacturaService(){
        ConectionDB c = new ConectionDB();
        this.con = c.getConn();
        this.apartadoDAO = new ApartadoDAOImpl(con);
    }
    
    public void cambiarLote(Integer folLot, Integer folLot1, Integer lote1, Integer lote2, Integer folio){
        Apartado a = apartadoDAO.getByIdLoteAndClaDoc(lote1, folio+"");
        Apartado aux = apartadoDAO.getByIdLoteAndClaDoc(lote2, folio+"");
        if(aux==null){
            this.apartadoDAO.updateIdLote(a.getId(), lote2);
        }else{
            int plus = a.getCant();
            this.apartadoDAO.delete(a.getId());
            this.apartadoDAO.updateCant(aux.getId(), aux.getCant() + a.getCant());
        }
    }
}
