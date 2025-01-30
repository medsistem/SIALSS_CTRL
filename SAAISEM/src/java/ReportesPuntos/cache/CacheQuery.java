package ReportesPuntos.cache;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Clase que permite crear json apartir de consultas a la db. Además permite
 * guardar dichos resultados en el disco duro para evitar realizar nuevamente la
 * consulta.
 *
 * @author MEDALFA SOFTWARE
 * @version 1.40
 */
public class CacheQuery {

    /**
     * Singleton.
     */
    private static CacheQuery current;

    /**
     * Total de registros almanacenados en cada parte.
     */
    public static int CHUCK_SIZE = 100000;

    /**
     * Ubicación donde se almacenan los archivos del cache.
     */
    private final Path dir;

    /**
     * Crea un objeto que almacenera en la ruta indicada. Si la carpeta no
     * existe la creará.
     *
     * @param path donde se guardará los archivos.
     * @throws IOException Si no puede crearse la carpeta para el almacenado.
     */
    public CacheQuery(String path) throws IOException {
        this.dir = Paths.get(path);

        if (Files.notExists(dir)) {
            Files.createDirectory(dir);
        }

    }

    /**
     * Añade el nombre del archivo a la ruta de almacenado.
     *
     * @param user actual que genero la consulta.
     * @param session activa actualmente por el usuario.
     * @param category identifica la consulta.
     * @return La ruta para el almacenado del archivo.
     */
    private Path getFilenamePath(String user, String session,
            String category, int part) {

        return Paths.get(dir.toString(), getFilename(user, session, category, part));
    }

    /**
     * Crea un nombre para el archivo usando el usuario actual, la sesion activa
     * y la categoria. Ejemplo: usuario_sesion_categoria.json.
     *
     * @param user actual que genero la consulta.
     * @param session activa actualmente por el usuario.
     * @param category identifica la consulta.
     * @return El nombre del archivo.
     */
    private String getFilename(String user, String session, String category,
            int part) {

        return String.format("%s_%s_%s_%d.json", user, session,
                category, part);
    }

    /**
     * Guarda la informacion del json en un archivo en el disco duro. Además
     * elimina archivos del usuarios de sesiones pasadas.
     *
     * @param user actual que genero la consulta.
     * @param session activa actualmente por el usuario.
     * @param category identifica la consulta.
     * @param json contiene la estructura en JSON que se desea guardar.
     * @throws IOException En caso de no poder crear el archivo.
     */
    private void writeJSON(String user, String session, String category,
            ArrayList<ArrayList<String>> json, int part) throws IOException {

        try (Writer writer = new FileWriter(getFilenamePath(user, session, category, part).toString())) {
            Gson gson = new GsonBuilder().create();

            gson.toJson(json, writer);
        }
    }

    public String readJSON(String user, String session, String category,
            int part) throws IOException {
        return new String(Files.readAllBytes(getFilenamePath(user, session,
                category, part)));
    }

    /**
     * Permite leer y parsear aun array una estructura JSON guarda en cache.
     *
     * @param user actual que genero la consulta.
     * @param session activa actualmente por el usuario.
     * @param category identifica la consulta.
     * @param part de la consulta que se desea obtener, la consulta se divide en
     * el numero almacenado en CHUCK_SIZE.
     * @return
     * @throws IOException Si no puede leerse en archivo en cache.
     */
    public ArrayList<ArrayList<String>> readArray(String user, String session,
            String category, int part)
            throws IOException {
        String json = readJSON(user, session, category, part);

        return getArray(json);
    }

    /**
     * Parsea la estructura JSON a un array.
     *
     * @param json con la estructura a parsear.
     * @return informacion parseada.
     */
    private ArrayList<ArrayList<String>> getArray(String json) {

        Gson gson = new GsonBuilder().create();

        return gson.fromJson(json, new TypeToken<ArrayList<ArrayList<String>>>() {
        }.getType());
    }

    /**
     * Convierte un consulta a un array que cada posición contine otro array con
     * cada una de las filas del resultset. Ejemplo [['casa',
     * '100'],['hospital', '101'],['hotel', '102']]
     *
     * @param resultSet con los resultados de la consultada a convertir.
     * @return array con la informacion obtenida.
     * @throws SQLException Si no puede recorrerse el resulset.
     */
    public ArrayList<ArrayList<String>> getArray(ResultSet resultSet) throws SQLException {

        int total_columnas = resultSet.getMetaData().getColumnCount();

        ArrayList<ArrayList<String>> resultadoJSON = new ArrayList<>();
        ArrayList<String> filaJSON;
        int rows = 0;

        while (resultSet.next()) {
            filaJSON = new ArrayList<>();
            filaJSON.add(String.valueOf(rows));

            for (int i = 1; i <= total_columnas; i++) {
                filaJSON.add(resultSet.getString(i));
            }

            resultadoJSON.add(filaJSON);
            rows++;
        }

        return resultadoJSON;
    }

    /**
     * Guarda directamente los registros en un resultset a archivo cache. Para
     * almacenarlo este convierte los registros a array con estructura JSON.
     *
     * @param user actual que genero la consulta.
     * @param session activa actualmente por el usuario.
     * @param category identifica la consulta.
     * @param resultSet con los resultados de la consultada a convertir.
     * @throws SQLException Si no puede recorrerse el resulset.
     * @throws IOException En caso de no poder crear el archivo.
     */
    public void writeJSON(String user, String session, String category, ResultSet resultSet)
            throws SQLException, IOException {

        //Logica para limpiar archivos de sessiones anteriores.
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir,
                new FilterSession(session, user, category))) {
            for (Path path : stream) {
                Files.delete(path);
            }
        }

        ArrayList<ArrayList<String>> resultado;
        int first = 0;
        int part = 0;
        while (true) {
            resultado = getArray(resultSet, first);
            this.writeJSON(user, session, category, resultado, part);

            if (resultado.size() < CHUCK_SIZE) {
                break;
            }

            part++;
            first += CHUCK_SIZE;
        }

    }

    public ArrayList<ArrayList<String>> getArray(ResultSet resultSet, int first) throws SQLException {

        int total_columnas = resultSet.getMetaData().getColumnCount();

        ArrayList<ArrayList<String>> resultadoJSON = new ArrayList<>();
        ArrayList<String> filaJSON;
        int rows = 0;

        while (resultSet.next()) {
            filaJSON = new ArrayList<>();
            filaJSON.add(String.valueOf(first + rows));

            for (int i = 1; i <= total_columnas; i++) {
                filaJSON.add(resultSet.getString(i));
            }

            resultadoJSON.add(filaJSON);
            rows++;

            if (rows == CHUCK_SIZE) {
                System.out.println(filaJSON);
                break;
            }
        }

        return resultadoJSON;
    }
}
