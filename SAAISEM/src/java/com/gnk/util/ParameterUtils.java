/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.util;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author HP-MEDALFA
 */
public class ParameterUtils {

    public static String getParameter(String parameter, HttpServletRequest request) {
        String result = "";
        try {
            result = request.getParameter(parameter);
        } catch (Exception e) {
        }
        return result == null ? "" : result;
    }

}
