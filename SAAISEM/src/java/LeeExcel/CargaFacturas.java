/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LeeExcel;

import java.sql.ResultSet;
import conn.*;

/**
 * Procesa archivo excel para carga de factura sap txt 
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */

public class CargaFacturas {

    ConectionDB con = new ConectionDB();

    public String cargafacturas(String id_usu, String fol_abasto) {
        String mensaje;
        try {
            con.conectar();
            int F_Punto = 0, F_Idsur = 0, F_IdePro = 0, F_Cvesum = 0;
            try {
                ResultSet rset = con.consulta("select F_FactAgr,F_Punto,F_FactSavi from tb_cargafactura");
                while (rset.next()) {
                    F_Punto = rset.getInt(2);
                    if (F_Punto == 1) {
                        F_Idsur = 1;
                        F_IdePro = 0;
                        F_Cvesum = 1;
                    } else if (F_Punto == 2) {
                        F_Idsur = 1;
                        F_IdePro = 1;
                        F_Cvesum = 1;
                    } else if (F_Punto == 3) {
                        F_Idsur = 1;
                        F_IdePro = 0;
                        F_Cvesum = 2;
                    } else if (F_Punto == 4) {
                        F_Idsur = 1;
                        F_IdePro = 1;
                        F_Cvesum = 2;
                    } else if (F_Punto == 5) {
                        F_Idsur = 2;
                        F_IdePro = 0;
                        F_Cvesum = 1;
                    } else if (F_Punto == 6) {
                        F_Idsur = 2;
                        F_IdePro = 1;
                        F_Cvesum = 1;
                    } else if (F_Punto == 7) {
                        F_Idsur = 2;
                        F_IdePro = 0;
                        F_Cvesum = 2;
                    } else if (F_Punto == 8) {
                        F_Idsur = 2;
                        F_IdePro = 1;
                        F_Cvesum = 2;
                    }
                    con.actualizar("UPDATE tb_txtis SET F_FacSAVI='" + rset.getInt(3) + "' WHERE F_FacGNKLAgr='" + rset.getString(1) + "' AND F_IdePro='" + F_IdePro + "' AND F_Cvesum='" + F_Cvesum + "' AND F_Idsur='" + F_Idsur + "'");
                }

                mensaje = "Actualización Correctamente";
            } catch (Exception e) {
                mensaje = e.getMessage();
            }
            con.cierraConexion();
        } catch (Exception e) {
            mensaje = e.getMessage();
        }
        return mensaje;
    }
}
