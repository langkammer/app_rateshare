package br.com.rateshare.util;


import java.util.Date;

public class UtilGenerate {




    public static String gerarChave(){
        Date date = new Date();
        String retorno = Long.toString(date.getTime());
        return  retorno;
    }



}
