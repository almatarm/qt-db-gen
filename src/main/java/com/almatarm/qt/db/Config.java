package com.almatarm.qt.db;

import java.io.File;
import java.io.Serializable;

/**
 * Created by almatarm on 29/11/2019.
 */
public class Config implements Serializable {
    static File projectDir;
    static String export_library;
    static String globalHeaderFile;
    static String namespace;
    static boolean write = false;
}
