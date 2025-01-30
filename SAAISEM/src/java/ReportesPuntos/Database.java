package ReportesPuntos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Procesamiendo de bd sql e insertar información a bd mysql
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class Database {

    private static final String DRIVER_NAME = "org.mariadb.jdbc.Driver";

    static {
        try {
            Class.forName(DRIVER_NAME).newInstance();
            System.out.println("*** Driver loaded");
        } catch (Exception e) {
            System.out.println("*** Error : " + e.toString());
            System.out.println("*** ");
            System.out.println("*** Error : ");
            e.printStackTrace();
        }
    }

    private static final String URL = "jdbc:mariadb://localhost:3306/scr_ceaps";
    private static final String USER = "root";
    private static final String PASSWORD = "eve9397";
    private static String INSTRUCTIONS = new String();

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void resetDatabase() throws SQLException, FileNotFoundException {
        String s = new String();
        StringBuffer sb = new StringBuffer();

        try {
            FileReader fr = new FileReader(new File("C:\\Users\\Sistemas\\Desktop\\scr_ceaps20150623-1124.sql"));
            // be sure to not have line starting with "--" or "/*" or any other non aplhabetical character 
            BufferedReader br = new BufferedReader(fr);

            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            br.close();

            String[] inst = sb.toString().split(";");
            Connection c = Database.getConnection();
            Statement st = c.createStatement();
            for (int i = 0; i < inst.length; i++) {
                // we ensure that there is no spaces before or after the request string
                // in order to not execute empty statements
                if (!inst[i].trim().equals("")) {
                    st.executeUpdate(inst[i]);
                    System.out.println(">>" + inst[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("*** Error : " + e.toString());
            System.out.println("*** ");
            System.out.println("*** Error : ");
            e.printStackTrace();
            System.out.println("################################################");
            System.out.println(sb.toString());
        }
    }

}
