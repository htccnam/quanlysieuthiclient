package com.example.quanlysieuthiclient.UTIL;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class configLoader {
    private static final Properties pro=new Properties() ;
    private static final String filename="config.properties" ;

    static {
        try{
            InputStream IS=configLoader.class.getClassLoader().getResourceAsStream(filename);
            pro.load(IS);
        } catch (IOException e) {
            throw new RuntimeException("khong tim thay file"+filename);
        }
    }
    public static String getbaseapiurl(){
        return pro.getProperty("api.baseurl");
    }

}
