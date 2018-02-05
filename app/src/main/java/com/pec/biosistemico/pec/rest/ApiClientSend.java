package com.pec.biosistemico.pec.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pec.biosistemico.pec.util.Global;

import java.lang.reflect.Modifier;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ricardo on 16/11/2016.
 */

public class ApiClientSend {

    private static Global mDados = Global.getInstance();
    private static String local;
    public static String BASE_URL = "http://www.projecttrace.com.br/projectservice/";
    public static String BASE_URL_CRIATF = "http://www.projecttrace.com.br:8092/";
    public static String BASE_URL_REST = "http://www.projecttrace.com.br:8080/rest/";
    public static String BASE_URL_LDA = "http://www.projecttrace.com.br:8089/rest/";
    private static String rede;

    public static Retrofit retrofit = null;

    public static  Retrofit getClient(){

        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .excludeFieldsWithModifiers(Modifier.TRANSIENT)
                .create();

        if(retrofit == null){


            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL_LDA)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }
}
