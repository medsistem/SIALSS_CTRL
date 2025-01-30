/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author HP-MEDALFA
 */
public class Calendario {

    public static boolean first3WorkDays() {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayMonth = c.get(Calendar.DAY_OF_MONTH);
        int dayWeek = c.get(Calendar.DAY_OF_WEEK);
        if (dayMonth > 5) {
            return false;
        }
        if (dayWeek == 1 || dayWeek == 7) {
            return false;
        }
        if ((dayMonth == 4) && (dayWeek == Calendar.MONDAY || dayWeek == Calendar.TUESDAY || dayWeek == Calendar.WEDNESDAY)) {
            return true;
        }
        if (dayMonth == 5 && (dayWeek == Calendar.MONDAY || dayWeek == Calendar.TUESDAY || dayWeek == Calendar.WEDNESDAY)) {
            return true;
        }
        return dayMonth <= 3 && dayWeek > Calendar.SUNDAY && dayWeek < Calendar.SATURDAY;
    }

    public static Date stringToDate(String date) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(Calendario.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
