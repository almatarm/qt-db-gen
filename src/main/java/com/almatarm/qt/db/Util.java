package com.almatarm.qt.db;

import java.util.ArrayList;

/**
 *
 * @author <a href="mailto:almatarm@gmail.com">Mufeed H. AlMatar</a>
 * @version 1.0
 */
public class Util {
    public static String toSmall(String clazz) {
        return clazz.length() > 1 ? clazz.substring(0, 1).toLowerCase() 
                    + clazz.substring(1): clazz.toLowerCase();
    }

    public static String capitalizeFirstLetter(String str) {
        if(str == null || str.isEmpty()) return "";
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String decapitalizeFirstLetter(String str) {
        if(str == null || str.isEmpty()) return "";
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static String toCamelCase(String str) {
        String r = str;
        int idx = -1;
        while((idx = r.indexOf("_")) != -1 && idx != r.length() -1) {
            if(idx + 1 < str.length()) {
                r = r.substring(0, idx) + capitalizeFirstLetter(r.substring(idx + 1));
            }
        }
        return r;
    }

    public static ArrayList<Field> list(Field... fields) {
        ArrayList<Field> f = new ArrayList<>();
        for(Field field: fields) {
            f.add(field);
        }
        return f;
    }

    public static void main(String[] args) {
        System.out.println(toCamelCase("parent_id_"));
    }
}
