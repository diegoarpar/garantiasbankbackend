package com.itec.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iTech on 19/03/2017.
 */
public class UTILS {
    public static final String COLLECTION_ARCHIVO = "archivo";
    public static final String COLLECTION_ARCHIVOS_DATOS = "archivo_fields";
    public static final String COLLECTION_ARCHIVOS_TRD = "archivo_trd";
    public static final String COLLECTION_ARCHIVO_PARAMETRICS_VALUES = "archivo_parametrics_values";
    public static final String COLLECTION_ARCHIVO_DOCUMENTS = "archivo_documents";
    public static final String COLLECTION_REGIONAL = "archivo_regional_recepcion";
    public static final String COLLECTION_METADATA = "archivo_metadata";

    public static ArrayList<HashMap<String, DBObject>> fillCriterialListFromDBOBject(BasicDBList dbList, HashMap criterial, ArrayList<HashMap<String, DBObject>> criterialList){
        criterialList.clear();



        for(String s : dbList.keySet()){
            criterial = new HashMap();
            DBObject dbObject =((BasicDBObject) JSON.parse(dbList.get(s).toString()));
            criterial.put("json",dbObject);
            /*for(String o : dbObject.keySet()){
                criterial.put(o,dbObject.get(o).toString());
            }*/
            criterialList.add(criterial);
        }
        return criterialList;

    }


    public static HashMap fillCriterialFromString( String queryString, HashMap criterial){
        criterial.clear();
        if(queryString!=null)
            for (String split : queryString.split("&")) {
                if (split.split("=").length == 2) {
                    criterial.put(split.split("=")[0], split.split("=")[1]);
                }
            }
        return criterial;
    }

    public static HashMap getTenant(HttpServletRequest request,HashMap criterial){
        String tenant="";
        if(request.getHeader("Authorization").split(",").length>1) {
             tenant = request.getHeader("Authorization").split(",")[1];
        }
        criterial.put("tenant",tenant);
        return  criterial;
    }

    public static String fillStringFromRequestPost (@Context HttpServletRequest req) throws IOException {
        req.getParameterMap();
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String read;
        while ((read = br.readLine()) != null) {
            stringBuilder.append(read);
        }
        br.close();
        return stringBuilder.toString();
    }

}
