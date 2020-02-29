package com.almatarm.qt.db;

import java.util.ArrayList;

import static com.sun.tools.javac.jvm.ByteCodes.ret;

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

    public static ArrayList<Field> list(Field... fields) {
        ArrayList<Field> f = new ArrayList<>();
        for(Field field: fields) {
            f.add(field);
        }
        return f;
    }
}
