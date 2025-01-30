/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.service;

import com.google.gson.Gson;
import conn.ConectionDB;
import in.co.sneh.controller.vo.RequirementProduct;
import in.co.sneh.controller.vo.ThomasantRequirement;
import in.co.sneh.controller.vo.ThomasantSurtido;
import in.co.sneh.model.Factura;
import in.co.sneh.persistance.FacturaDAOImpl;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author HP-MEDALFA
 */
public class ThomasantService {

    private final String urlIsem = "https://isem.thomasant.com.mx/thomasant/concentrate/attended";
    private final String urlMerida = "https://merida.thomasant.com.mx/thomasant/concentrate/attended";

    private final ConectionDB c;
    private FacturaDAOImpl facturaDao;

    public ThomasantService() {
        c = new ConectionDB();
    }

    public int enviaRemision(String flio, Integer proyecto) {
        String urlDestino;
        if (proyecto == 14) {
            urlDestino = this.urlIsem;
        } else {
            urlDestino = this.urlMerida;
        }
        try {
            c.conectar();
            facturaDao = new FacturaDAOImpl(c.getConn());

            List<Factura> factura = facturaDao.findByFolioAndProyectoFull(Integer.parseInt(flio), proyecto);
            ThomasantSurtido surtido = this.buildBody(factura);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlDestino);

            Gson gson = new Gson();
            String json = gson.toJson(surtido);
            StringEntity input = new StringEntity(json);
            input.setContentType("application/json");
            post.setEntity(input);
            HttpResponse response = httpClient.execute(post);
            Logger.getLogger(ThomasantService.class.getName()).log(Level.INFO, "Se envió el folio " + flio + " Respuesta: "
                    + response.getStatusLine().getStatusCode() + "|json: " + json + "|mensaje: " + EntityUtils.toString(response.getEntity()));
            return response.getStatusLine().getStatusCode();

        } catch (SQLException ex) {
            Logger.getLogger(ThomasantService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ThomasantService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ThomasantService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    private ThomasantSurtido buildBody(List<Factura> factura) {
        ThomasantSurtido b = new ThomasantSurtido();
        Factura f = factura.get(0);
        b.setFolioRemission(f.getClaDoc() + "");
        b.setProject(f.getProyecto());
        b.setUnit(f.getClaCli());
        b.setUserAttended(f.getUsuario());

        for (Factura fac : factura) {
            RequirementProduct r = new RequirementProduct();
            r.setKey(fac.getClaPro());
            r.setQuantity(fac.getCantSur());
            r.setLotExpiredDate(fac.getLoteData().getFecCadD().getTime());
            r.setName(fac.getMedicaLote().getDescription());
            r.setLotKey(fac.getLoteData().getClaLot());
            b.addProduct(r);
        }
        return b;
    }
}
