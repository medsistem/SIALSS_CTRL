/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.medalfa.saa.utils;

/**
 *
 * @author HP-MEDALFA
 */
public class ClaveUtils {

    public static String validarClave(String claPro) {
        String[] punto = claPro.split("\\.");
        System.out.println(punto.length);
        if (punto.length > 1) {
            System.out.println(claPro + "***" + punto[0] + "////" + punto[1]);
            for (int j = punto[0].length(); j < 4; j++) {
                punto[0] = "0" + punto[0];
            }
            if (punto[1].equals("01")) {
                claPro = (punto[0]) + ".01";
            } else if (punto[1].equals("02")) {
                claPro = (punto[0]) + ".02";
            } else if (punto[1].equals("10")) {
                claPro = (punto[0]) + ".1";
            } else if (punto[1].equals("20")) {
                claPro = (punto[0]) + ".2";
            } else if (punto[1].equals("30")) {
                claPro = (punto[0]) + ".3";
            } else if (punto[1].equals("40")) {
                claPro = (punto[0]) + ".4";
            } else if (punto[1].equals("50")) {
                claPro = (punto[0]) + ".5";
            } else if (punto[1].equals("03")) {
                claPro = (punto[0]) + ".03";
            } else if (punto[1].equals("04")) {
                claPro = (punto[0]) + ".04";
            } else if (punto[1].equals("05")) {
                claPro = (punto[0]) + ".05";
            } else if (punto[1].equals("1")) {
                claPro = (punto[0]) + ".1";
            } else if (punto[1].equals("2")) {
                claPro = (punto[0]) + ".2";
            } else if (punto[1].equals("00")) {
                claPro = (punto[0]);
            } else {
                claPro = (punto[0]);
            }
            System.out.println(claPro);
        }
        return claPro;
    }

}
