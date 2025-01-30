/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.dao;

import org.json.simple.JSONArray;

/**
 *
 * @author MEDALFA
 */
public interface ReporteadorDao {

    public JSONArray ObtenerJurisdicciones();

    public JSONArray ObtenerMunicipio(String Jurisdicciones);

    public JSONArray ObtenerUnidad(String Jurisdicciones, String Municipio);

    public JSONArray ObtenerUnidades();

    public JSONArray ObtenerTipoUnidad();
}
