package com.itec.oauth;

import com.itec.configuration.ConfigurationApp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by iTech on 22/04/2017.
 */
public class UrlFactory {

    public static final String IS_VALID_TOKEN="isValidToken";
    public static final String GET_ROLES="getRoles";
    public static final String INSERT_FILE="getCMSInserFile";
    public static final String AUTENTICATION="autentication";
    public static URL getUrl(String url, HashMap<String,String> parameters) throws MalformedURLException{

        String query="";
        if(parameters!=null){
            query="?";
            Iterator it = parameters.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                query+=pair.getKey()+"="+pair.getValue()+"&";
            }
            query=query.substring(0,query.length()-1);
        }
        switch (url){

            case IS_VALID_TOKEN: return new URL(ConfigurationApp.URLAUTENTICATION+"token"+query);
            case GET_ROLES: return new URL(ConfigurationApp.URLAUTENTICATION+"roles"+query);
            case INSERT_FILE: return new URL(ConfigurationApp.URL_CMS+"FileToIndex/IndexId"+query);
            case AUTENTICATION: return new URL(ConfigurationApp.URLAUTENTICATION+"users/logIn"+query);


        }
        return null;
    }

}
