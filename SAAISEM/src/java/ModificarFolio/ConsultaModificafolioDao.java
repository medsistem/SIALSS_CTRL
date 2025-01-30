/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModificarFolio;

import org.json.simple.JSONArray;

/**
 *
 * @author MEDALFA
 */
public interface ConsultaModificafolioDao {

    public JSONArray getRegistro(String Folio);

    public JSONArray getEliminaRegistro(String Folio, String IdReg);

    public JSONArray getValidaDevolucion(String Folio, String Obs, String Usuario);
}
