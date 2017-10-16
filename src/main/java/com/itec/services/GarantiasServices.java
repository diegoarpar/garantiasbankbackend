/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itec.services;

import com.itec.db.FactoryMongo;
import com.itec.util.UTILS;
import com.mongodb.BasicDBList;
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
public class GarantiasServices {

    FactoryMongo f = new FactoryMongo();
    HashMap<String, DBObject> criterial= new HashMap<>();
    ArrayList<HashMap> criterialList= new ArrayList<>();
    String postString="";
     @RolesAllowed("ADMIN,ADMIN_BODEGA,USER_BODEGA")
     @POST
     @Produces(MediaType.APPLICATION_JSON)
     @Path("/insertGarantias")

        public String insertGarantias(@Context HttpServletRequest req) throws IOException {
         postString=UTILS.fillStringFromRequestPost(req);
         criterialList=UTILS.fillCriterialListFromDBOBject((BasicDBList) JSON.parse(postString.toString()),criterial, criterialList);


         for(HashMap o : criterialList){
             o=UTILS.getTenant(req,o);
             f.insert(o, UTILS.COLLECTION_ARCHIVO);
         }
            return  "[{\"realizado\":\"ok\"}]";
        }
     @POST
     @Produces(MediaType.TEXT_PLAIN)
     @Consumes(MediaType.APPLICATION_JSON)
     @Path("/updateGarantias")
     @PermitAll
    public String updateGarantias2(@Context HttpServletRequest req) throws IOException {
         postString=UTILS.fillStringFromRequestPost(req);
         criterialList=UTILS.fillCriterialListFromDBOBject((BasicDBList) JSON.parse(postString.toString()),criterial, criterialList);

         for(HashMap o : criterialList){
             o=UTILS.getTenant(req,o);
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
        postString=UTILS.fillStringFromRequestPost(req);
        criterialList=UTILS.fillCriterialListFromDBOBject((BasicDBList) JSON.parse(postString.toString()),criterial, criterialList);

        for(HashMap o : criterialList){
            o=UTILS.getTenant(req,o);
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
        criterial=UTILS.fillCriterialFromString(req.getQueryString(),criterial);
        UTILS.tryJson(criterial);
        UTILS.getTenant(req,criterial);
        return f.retrive(criterial,UTILS.COLLECTION_ARCHIVO);
    }
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/getNumber")
    @RolesAllowed("ADMIN")
    public  String getNumber() throws IOException {

        Date d = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmssmmmm'Z'", Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
           dateFormat.format(d);

        return "[{\"number\":\""+dateFormat.format(d)+"\"}]";
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/retrive")
    public List<DBObject> retrivePost(@Context HttpServletRequest req) throws IOException {

        criterialList=UTILS.fillCriterialListFromDBOBject(req,criterial, criterialList);
        HashMap o=criterialList.get(0);
        o=UTILS.getTenant(req,o);
        return f.retrive(o, UTILS.COLLECTION_ARCHIVO);


    }
}
