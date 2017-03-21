package com.itec.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.itec.db.FactoryMongo;
import com.itec.util.UTILS;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import static com.sun.corba.se.spi.logging.CORBALogDomains.UTIL;

/**
 * Created by root on 14/06/16.
 */

@Path("/garantias/config")
@Produces(MediaType.APPLICATION_JSON)


public class ConfigServices {
    FactoryMongo fm = new FactoryMongo();
    HashMap<String, String> criterial= new HashMap<>();
    ArrayList<HashMap<String, String>> criterialList= new ArrayList<>();



    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/garantias-field")
    @PermitAll
    public List<DBObject> getGarantiasFiled(@Context HttpServletRequest req) throws IOException {
        criterial.clear();
        fillCriterialFromString(req.getQueryString());
        if(req.getHeader("Authorization").split(",").length>1) {
            criterial.put("tenant", req.getHeader("Authorization").split(",")[1]);
        }
        return fm.retrive(criterial,UTILS.COLLECTION_ARCHIVOS_DATOS);
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/garantias-field/{id}")
    @PermitAll
    public String removeGarantiasFiled(@Context HttpServletRequest req, @PathParam("id") String id) throws IOException {
        criterial.clear();
        fillCriterialFromString(req.getQueryString());
        fm.delete(criterial,UTILS.COLLECTION_ARCHIVOS_DATOS);
        return "Elimiando";
    }
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/garantias-field")
    @PermitAll
    public String insertGarantiasFiled(@Context HttpServletRequest req) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String read;
        while((read=br.readLine()) != null) {
            stringBuilder.append(read);
        }
        br.close();
        fillCriterialListFromDBOBject((BasicDBList) JSON.parse(stringBuilder.toString()));
        for(HashMap o : criterialList){
            fm.insert(o, UTILS.COLLECTION_ARCHIVOS_DATOS);
        }
        return  "FIRMANDO";
    }



    /*PARAMETRICS VALUES*/
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/garantias-parametricvalues")
    @PermitAll
    public List<DBObject> getGarantiasParametricValues(@Context HttpServletRequest req) throws IOException {
        criterial.clear();
        criterial=UTILS.fillCriterialFromString(req.getQueryString(),criterial);
        criterial=UTILS.getTenant(req,criterial);
        return fm.retrive(criterial,UTILS.COLLECTION_ARCHIVO_PARAMETRICS_VALUES);
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/garantias-parametricvalues/delete/")
    @PermitAll
    public String removeGarantiasParametricValues(@Context HttpServletRequest req) throws IOException {
        criterial.clear();
        fillCriterialFromString(req.getQueryString());
        fm.delete(criterial,UTILS.COLLECTION_ARCHIVO_PARAMETRICS_VALUES);
        return "Elimiando";
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/garantias-parametricvalues")
    @PermitAll
    public String insertGarantiasParametricValues(@Context HttpServletRequest req) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String read;
        while((read=br.readLine()) != null) {
            stringBuilder.append(read);
        }
        br.close();
        fillCriterialListFromDBOBject((BasicDBList) JSON.parse(stringBuilder.toString()));
        for(HashMap o : criterialList){
            fm.insert(o,UTILS.COLLECTION_ARCHIVO_PARAMETRICS_VALUES);
        }

        return  "FIRMANDO";
    }



    /*OTHER METHOD*/
    private void fillCriterialListFromDBOBject(BasicDBList dbList){
        criterialList.clear();

        for(String s : dbList.keySet()){
            criterial.clear();
            DBObject dbObject =((BasicDBObject) JSON.parse(dbList.get(s).toString()));
            for(String o : dbObject.keySet()){
                criterial.put(o,dbObject.get(o).toString());
            }
            criterialList.add(criterial);
        }

    }


    private void fillCriterialFromString( String queryString){
        criterial.clear();
        if(queryString!=null)
            for (String split : queryString.split("&")) {
                if (split.split("=").length == 2) {
                    criterial.put(split.split("=")[0], split.split("=")[1]);
                }
            }
    }
}
