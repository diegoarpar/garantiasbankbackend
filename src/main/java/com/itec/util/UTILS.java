package com.itec.util;

import com.google.common.base.Optional;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.bson.types.ObjectId;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.*;
import java.util.*;

/**
 * Created by iTech on 19/03/2017.
 */
public class UTILS {
    //get menu list and sublist
    public static final String COLLECTION_ARCHIVO = "archivo";
    public static final String COLLECTION_MENU = "menu";
    public static final String COLLECTION_ARCHIVOS_DATOS = "archivo_fields";
    public static final String COLLECTION_ARCHIVOS_TRD = "archivo_trd";
    public static final String COLLECTION_ARCHIVO_PARAMETRICS_VALUES = "archivo_parametrics_values";
    public static final String COLLECTION_ARCHIVO_DOCUMENTS = "archivo_documents";
    public static final String COLLECTION_REGIONAL = "archivo_regional_recepcion";
    public static final String COLLECTION_METADATA = "archivo_metadata";
    public static final String COLLECTION_REPORT = "archivo_report";
    public static final String APPLICATION_NAME = "gar";


    public static ArrayList<HashMap> fillCriterialListFromDBOBject(BasicDBList dbList, HashMap criterial, ArrayList<HashMap> criterialList){
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
    public static ArrayList<HashMap> fillCriterialListFromDBOBject(@Context HttpServletRequest req, HashMap criterial, ArrayList<HashMap> criterialList) throws IOException {
        criterialList.clear();
        BasicDBList dbList=(BasicDBList) JSON.parse(fillStringFromRequestPost(req));

        return fillCriterialListFromDBOBject(dbList,criterial,criterialList);

    }


    public static HashMap tryJson( HashMap criterial){
        HashMap temp = new HashMap();
        DBObject obj = new BasicDBObject();
        try {
            Iterator it = criterial.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                if(pair.getKey().toString().equals("_id")){


                        obj.put(pair.getKey().toString(), generateObjectid(pair.getValue().toString()).get());

                }else if(pair.getKey().toString().equals("json")&&((BasicDBObject)pair.getValue()).get("_id")!=null){

                    obj.put("_id", generateObjectid(((BasicDBObject)pair.getValue()).get("_id").toString()));
                }

                else
                obj.put(pair.getKey().toString(), pair.getValue());
            }
            criterial.clear();
            criterial.put("json",obj);
        }catch(Exception e){
            e.printStackTrace();
        }
        return criterial;
    }
    public static HashMap fillCriterialFromString( String queryString, HashMap criterial){
        criterial.clear();
        BasicDBObject obj = new BasicDBObject();
        if(queryString!=null)
            for (String split : queryString.split("&")) {
                if (split.split("=").length == 2) {

                    obj.append(split.split("=")[0],split.split("=")[1]);
                }
            }
        criterial.put("json",obj);
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
    public static ObjectId generateObjectid(String timeStamp, String machineIdentifier, String processIdentifier, String counter ){
        return new ObjectId(Integer.parseInt(timeStamp),
                Integer.parseInt(machineIdentifier),
                (short)Integer.parseInt(processIdentifier),
                Integer.parseInt(counter));

    }
    public static ObjectId generateObjectid(String  objectId){
       try{
           return new ObjectId(objectId);
       }catch (Exception e){
       }
        return null;


    }

    public static void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) throws IOException {
        int read;
        final int BUFFER_LENGTH = 1024;
        final byte[] buffer = new byte[BUFFER_LENGTH];
        OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
        while ((read = uploadedInputStream.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        out.flush();
        out.close();
    }

    public static String getToken(@Context HttpServletRequest req) {
        String autorization="";
        if(req.getHeader("Authorization").split(",").length>1) {
            autorization = req.getHeader("Authorization").split(",")[0];
        }

        return  autorization;
    }
}
