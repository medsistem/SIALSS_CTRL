/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.picking.service;

import java.io.IOException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import org.apache.catalina.Context;
import org.apache.catalina.Manager;
import org.apache.catalina.Session;
import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.ApplicationContextFacade;

/**
 *
 * @author HP-MEDALFA
 */
public class SessionManagerService implements ServletContextListener {
    static Manager manager;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
             manager = getManagerFromServletContextEvent(sce);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        manager = null;
    }

    private static Manager getManagerFromServletContextEvent(ServletContextEvent sce) throws NoSuchFieldException, IllegalAccessException {
        ApplicationContextFacade contextFacade = (ApplicationContextFacade)sce.getSource();

        java.lang.reflect.Field appContextField = ApplicationContextFacade.class.getDeclaredField("context");
        appContextField.setAccessible(true);
        ApplicationContext applicationContext = (ApplicationContext)
                appContextField.get(contextFacade);

        java.lang.reflect.Field contextField = ApplicationContext.class.getDeclaredField("context");
        contextField.setAccessible(true);
        Context context = (Context) contextField.get(applicationContext);

        return context.getManager();
    }

    public static boolean closeSessionId(String sessionID) throws IOException {
        Session[] sessions = manager.findSessions();
        for(Session session: sessions){
            HttpSession s = (HttpSession) session;
            if(sessionID.equals(s.getAttribute("IdUsu"))){
                s.invalidate();
                return true;
            }
        }
//        return manager.findSession(sessionID);
        return false;
    }
}
