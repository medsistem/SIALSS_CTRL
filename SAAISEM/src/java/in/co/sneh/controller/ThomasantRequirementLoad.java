/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.controller;

import com.google.gson.Gson;
import in.co.sneh.controller.vo.ThomasantRequirement;
import in.co.sneh.service.Message;
import in.co.sneh.service.RequerimientoService;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author HP-MEDALFA
 */
@WebServlet(name = "ThomasantRequirementLoad", urlPatterns = {"/thomasant/requirement"})
public class ThomasantRequirementLoad extends HttpServlet {
    
    private static final Logger log = Logger.getLogger("ThomasantRequirementLoad");
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse resp) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Gson gson = new Gson();
        log.info("Llegada de requerimiento de Thomasant: \n " + body);
        ThomasantRequirement req = gson.fromJson(body, ThomasantRequirement.class);
        RequerimientoService service = new RequerimientoService();
        Message m = service.cargarRequerimientoThomasant(req, req.getUser());
        resp.setStatus(m.status);
        resp.getWriter().print(gson.toJson(m));
    }
    
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse resp) {
        resp.setHeader("Access-control-allow-origin", "*");
        resp.setHeader("Access-control-allow-methods", "POST");
    }
    
}
