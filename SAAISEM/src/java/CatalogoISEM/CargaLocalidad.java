/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CatalogoISEM;

import java.sql.ResultSet;
import conn.*;

/**
 * Proceso para registrar Localidades
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class CargaLocalidad {

    ConectionDB con = new ConectionDB();

    public String cargaLocali(String id_usu, String fol_abasto) {
        String mensaje;
        try {
            con.conectar();
            try {
                ResultSet rset = con.consulta("select * from tb_locaistemp");
                while (rset.next()) {

                    con.actualizar("insert into tb_locais values('" + rset.getInt(1) + "','" + rset.getString(2) + "','" + rset.getInt(3) + "')");
                }

                mensaje = "Datos Cargados Correctamente";
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
