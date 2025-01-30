/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package in.co.sneh.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.apache.commons.fileupload.FileItemStream;

/**
 * Class Carga requerimiento excel unidades rurales
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class CargaExcelRuralModel {

    /**
     * Metodo boolean guarda el archivo excel en la ruta para procesarla
     *
     * @return true o false
     */
    public static boolean processFile(String path, FileItemStream item) {
        try {
            File f = new File(path + File.separator + "exceles" + File.separator);
            if (!f.exists()) {
                f.mkdir();
            }
            File savedFile = new File(f.getAbsolutePath() + File.separator + item.getName());
            FileOutputStream fos = new FileOutputStream(savedFile);
            InputStream is = item.openStream();
            int x = 0;
            byte[] b = new byte[1024];
            while ((x = is.read(b)) != -1) {
                fos.write(b, 0, x);
            }
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
