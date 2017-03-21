/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itec.services;

import com.itec.db.FactoryMongo;
import com.itec.oauth.CallToken;
import com.itec.util.UTILS;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author iTech-Pc
 */
@Path("/garantias")
@Produces(MediaType.APPLICATION_JSON)
public class Services {

    FactoryMongo f = new FactoryMongo();
        HashMap<String, String> criterial= new HashMap<>();
    ArrayList<HashMap<String, String>> criterialList= new ArrayList<>();
     @RolesAllowed("ADMIN")
     @POST
     @Path("/insertGarantias")

        public String insertGarantias(@Context HttpServletRequest req) throws IOException {
            req.getParameterMap();
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
            String read;
            while((read=br.readLine()) != null) {
                stringBuilder.append(read);
            }
            br.close();

         fillCriterialListFromDBOBject((BasicDBList) JSON.parse(stringBuilder.toString()));

         for(HashMap o : criterialList){
             if(req.getHeader("Authorization").split(",").length>1) {
                 o.put("tenant", req.getHeader("Authorization").split(",")[1]);
             }
             f.insert(o, UTILS.COLLECTION_ARCHIVO);
         }
            return  "[{realizado:\"ok\"}]";
        }
     @POST
     @Produces(MediaType.TEXT_PLAIN)
     @Consumes(MediaType.APPLICATION_JSON)
     @Path("/updateGarantias")
     @PermitAll
    public String updateGarantias2(@Context HttpServletRequest req) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String read;
        while((read=br.readLine()) != null) {
            stringBuilder.append(read);
        }
        br.close();
         fillCriterialListFromDBOBject((BasicDBList) JSON.parse(stringBuilder.toString()));

         for(HashMap o : criterialList){
             if(req.getHeader("Authorization").split(",").length>1) {
                 o.put("tenant", req.getHeader("Authorization").split(",")[1]);
             }
             f.update(o,UTILS.COLLECTION_ARCHIVO);
         }
        return  "FIRMANDO";
    }

    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertGarantias")
    @PermitAll
    public String updateGarantias(@Context HttpServletRequest req,@PathParam("id") String id) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String read;
        while((read=br.readLine()) != null) {
            stringBuilder.append(read);
        }
        br.close();
        fillCriterialListFromDBOBject((BasicDBList) JSON.parse(stringBuilder.toString()));

        for(HashMap o : criterialList){
            if(req.getHeader("Authorization").split(",").length>1) {
                o.put("tenant", req.getHeader("Authorization").split(",")[1]);
            }
            f.update(o,UTILS.COLLECTION_ARCHIVO);
        }
        //f.actualizarGarantias(stringBuilder.toString());
        return  "FIRMANDO";
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/insertGarantias")
    @PermitAll
    public  List<DBObject> getGarantias(@Context HttpServletRequest req) throws IOException {
        fillCriterialFromString(req.getQueryString());
        if(req.getHeader("Authorization").split(",").length>1) {
            criterial.put("tenant", req.getHeader("Authorization").split(",")[1]);
        }
        return f.retrive(criterial,UTILS.COLLECTION_ARCHIVO);
    }
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/getNumber")
    @RolesAllowed("DIEGOROLE")
    public  String getNumber() throws IOException {

        Date d = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmssmmmm'Z'", Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
           dateFormat.format(d);

        return "[{\"number\":\""+dateFormat.format(d)+"\"}]";
    }

    private void fillCriterialListFromDBOBject(BasicDBList dbList){
        criterialList.clear();

        for(String s : dbList.keySet()){
            criterial= new HashMap<>();
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
