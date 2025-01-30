package com.medalfa.saa.cronJobs;


import Correo.CorreoIncidenciaRecibo;
import conn.ConectionDB;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;



public class IncidenciaReciboQuartz implements Job{

    ConectionDB con = new ConectionDB();
    CorreoIncidenciaRecibo correoIncRec = new CorreoIncidenciaRecibo();
    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        
        try {
            con.conectar();
            
            try{
                ResultSet rset = con.consulta("SELECT CONCAT( l.F_FolLot, l.F_Ubica ) , Count(*) AS incidencia, L.F_FolLot FROM tb_lote AS l WHERE l.F_Ubica LIKE '%NUEVA%' AND l.F_ClaLot <> 'x' AND l.F_FolLot NOT IN('11629', '20948') GROUP BY l.F_FolLot, l.F_Ubica HAVING COUNT(*) > 1;");
                if (rset.next()){
                    System.out.println("incidencia recibo");
                    String incidencia = rset.getString("incidencia"); 
                    System.out.println(incidencia + "dato");
                    if (incidencia == null ? ("") != null : !incidencia.equals("")){
                        correoIncRec.enviarCorreoInc();
                    }
                }
            
            } catch (SQLException ex){
            }
            
          
                
            } catch (Exception e) {
        }
            
        
        
        
    }
    

}

