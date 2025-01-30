/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.db;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ConcurrentHashMap;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author IngMa
 */
public class ConnectionManager {

    private static final ConcurrentHashMap<Source, DataSource> map;
    private static final ConcurrentHashMap<Source, ConnectionManager> mapManagers;

    private final Source source;

    static {
        map = new ConcurrentHashMap<>();
        mapManagers = new ConcurrentHashMap<>();
    }

    public Connection getConnection() throws SQLException {
        return map.get(this.source).getConnection();
    }

    private ConnectionManager(Source source) {
        this.source = source;
    }

    /**
     * Create un nuevo Datasource para el recurso indicado.
     *
     * @param source
     * @return
     * @throws NamingException Si encuentra el recurso.
     */
    private static DataSource createDataSource(Source source)
            throws NamingException {

        InitialContext ctx = new InitialContext();
        // nombre del recurso en el context.xml
        return (DataSource) ctx.lookup(source.text);
    }

    /**
     * Crea un administrador de base datos desde el recurso indicado, este posee
     * una conexión a la base de datos.
     *
     * @param source Indicado en el contex.xml
     * @return
     * @throws SQLException Si crear una nueva conexión.
     * @throws NamingException Si encuentra el recurso.
     */
    public static ConnectionManager getManager(Source source) throws SQLException,
            NamingException {

        if (!map.containsKey(source)) {
            map.put(source, createDataSource(source));
        }

        if (!mapManagers.containsKey(source)) {
            mapManagers.put(source, new ConnectionManager(source));
        }

        return mapManagers.get(source);
    }

    /**
     * Ejecuta y verifica que el query ejecutado se haya realizado alguna
     * modificación sobre la db.
     *
     * @param ps {@link PreparedStatement} con el query que se desea ejecutar.
     * @param modelName Nombre del modelo desde el que se realiza el llamado.
     * @param idRow Id del objeto del que proviene la acción.
     *
     * @return {@link PreparedStatement} en caso que se necesite para realizar
     * otra operación.
     * @throws SQLException
     */
    public static PreparedStatement checkUpdatedSuccessfully(PreparedStatement ps, String modelName,
            int idRow) throws SQLException {

        if (ps.executeUpdate() <= 0) {
            throw new SQLException(String.format("El objeto con id '%d' de la clase '%s' no pudo ser actualizado.",
                    idRow, modelName));
        }

        return ps;
    }

    /**
     * Ejecuta y verifica que el batch de queries enviados si realicen una
     * modificación sobre la db.
     *
     * @param ps {@link PreparedStatement} con el query que se desea ejecutar.
     * @param modelName Nombre del modelo desde el que se realiza el llamado.
     * @param idRow Id del objeto del que proviene la acción.
     *
     * @return {@link PreparedStatement} en caso que se necesite para realizar
     * otra operación.
     * @throws SQLException
     */
    public static PreparedStatement checkUpdatedSuccessfullyBatch(PreparedStatement ps, String modelName,
            int idRow) throws SQLException {

        int[] results = ps.executeBatch();
        int totalsUpdated = 0;

        for (int result : results) {
            totalsUpdated += result == Statement.SUCCESS_NO_INFO ? 1 : result; //Para el bug de MariaDB SUCCESS_NO_INFO
        }

        if (totalsUpdated <= 0) {
            throw new SQLException(String.format("El objeto con id '%d' de la clase '%s' no pudo ser actualizado.",
                    idRow, modelName));
        }

        return ps;
    }

    /**
     * Obtiene el hash generado por el algoritmo SHA-256.
     *
     * @see MessageDigest
     * @param password cadena sin encriptar.
     * @return cadena encriptada
     * @throws NoSuchAlgorithmException en caso que la plataforma no contenga el
     * algoritmo indicado.
     */
    public static String convertToSHA256(String password) throws NoSuchAlgorithmException {

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(
                password.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        String hex;

        for (int i = 0; i < hash.length; i++) {
            hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Obtiene el id generado para la ultima ejecución del
     * {@link PreparedStatement}.
     *
     * @param ps
     * @return
     * @throws SQLException
     */
    public static int getLastRow(PreparedStatement ps) throws SQLException {
        int id;
        try (ResultSet rs = ps.getGeneratedKeys()) {
            rs.next();
            id = rs.getInt(1);
        }
        return id;
    }

    /**
     * Cuando se usa un {@code "IN"} en un query, no se puede usar los metódos
     * en {@link PreparedStatement} para asignar el array como parametro, por
     * esto es necesario usar el {@code String.format(format, args)} en compañia
     * de este metódo para conseguir el resultado deseado.
     *
     * @see String#format(java.lang.String, java.lang.Object...)
     *
     * @param arr
     * @param separator
     * @return
     */
    public static String joinArray(Integer[] arr, String separator) {
        if (null == arr || 0 == arr.length) {
            return "";
        }

        StringBuilder sb = new StringBuilder(256);
        sb.append(arr[0]);

        for (int i = 1; i < arr.length; i++) {
            sb.append(separator).append(arr[i]);
        }

        return sb.toString();
    }

    /**
     * Ejecuta y verifica si un {@link ResultSet} esta complementamente vacio.
     *
     * @param ps
     * @return
     * @throws SQLException
     */
    public static boolean isEmptyResultSet(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            return !rs.isBeforeFirst();
        }
    }

}
