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
 * Class Carga archivos pdf formatos iso
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class FileUploadPDF {

    /**
     * Metodo boolean guarda el archivo pdf en la ruta
     *
     * @return true o false
     */
    public static boolean processFile(String path, FileItemStream item, String Unidad) {

        try {
            File f = new File(path + File.separator + "iso9001");
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
            e.printStackTrace();
        }
        return false;
    }
}
