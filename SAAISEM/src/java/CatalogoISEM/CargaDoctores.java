/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package CatalogoISEM;

import java.sql.ResultSet;
import conn.*;

/**
 * Proceso para cargar médicos
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class CargaDoctores {

    ConectionDB con = new ConectionDB();

    public String cargaDoct(String id_usu, String fol_abasto) {
        String mensaje;
        int Cont = 0;
        try {
            con.conectar();

            try {
                ResultSet rset = con.consulta("select * from tb_mediistemp");
                while (rset.next()) {

                    ResultSet medico = con.consulta("SELECT F_ClaMedIs FROM tb_mediis WHERE F_ClaMedIs='" + rset.getString(1) + "'");
                    while (medico.next()) {
                        Cont++;
                    }
                    if (Cont > 0) {
                        con.actualizar("update tb_mediis set F_DesMedIS='" + rset.getString(2) + "' WHERE F_ClaMedIs='" + rset.getString(1) + "'");
                    } else {
                        con.actualizar("insert into tb_mediis values('" + rset.getString(1) + "','" + rset.getString(2) + "')");
                    }

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
