package Develuciones;

import org.json.simple.JSONArray;

/**
 * Interface class ConsultaDevoDao devoluciones
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public interface ConsultaDevoDao {

    public JSONArray getRegistro(String Folio);

    public JSONArray getEliminaRegistro(String Folio, String IdReg);

    public JSONArray getValidaDevolucion(String Folio, String Obs, String Usuario, String Proyectos);

    public boolean ValidaDevolucionTran(String Folio, String Obs, String Usuario, String Proyectos);
}
