package com.itec.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.itec.db.FactoryMongo;
import com.itec.util.UTILS;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
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
    ArrayList<HashMap<String, DBObject>> criterialList= new ArrayList<>();



    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/garantias-field")
    @RolesAllowed({"ADMIN","CONFIG_BODEGA"})

    public List<DBObject> getGarantiasFiled(@Context HttpServletRequest req) throws IOException {
        criterial.clear();

        criterial=UTILS.fillCriterialFromString(req.getQueryString(),criterial);
        criterial=UTILS.getTenant(req,criterial);
        return fm.retrive(criterial,UTILS.COLLECTION_ARCHIVOS_DATOS);
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/garantias-field/{id}")
    @RolesAllowed({"ADMIN,CONFIG_BODEGA"})
    public String removeGarantiasFiled(@Context HttpServletRequest req, @PathParam("id") String id) throws IOException {
        criterial=UTILS.fillCriterialFromString(req.getQueryString(),criterial);
        criterial=UTILS.getTenant(req,criterial);
        fm.delete(criterial,UTILS.COLLECTION_ARCHIVOS_DATOS);
        return "Elimiando";
    }
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/garantias-field")
    @RolesAllowed({"ADMIN,CONFIG_BODEGA"})
    public String insertGarantiasFiled(@Context HttpServletRequest req) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String read;
        while((read=br.readLine()) != null) {
            stringBuilder.append(read);
        }
        br.close();
        criterialList=UTILS.fillCriterialListFromDBOBject((BasicDBList) JSON.parse(stringBuilder.toString()),criterial,criterialList);
        for(HashMap o : criterialList){
            o=UTILS.getTenant(req,o);
            fm.insert(o, UTILS.COLLECTION_ARCHIVOS_DATOS);
        }
        return  "FIRMANDO";
    }



    /*PARAMETRICS VALUES*/
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/garantias-parametricvalues")
    @RolesAllowed({"ADMIN,CONFIG_BODEGA"})
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
    @RolesAllowed({"ADMIN,CONFIG_BODEGA"})
    public String removeGarantiasParametricValues(@Context HttpServletRequest req) throws IOException {
        criterial.clear();
        criterial=UTILS.fillCriterialFromString(req.getQueryString(),criterial);
        criterial=UTILS.getTenant(req,criterial);
        fm.delete(criterial,UTILS.COLLECTION_ARCHIVO_PARAMETRICS_VALUES);
        return "Elimiando";
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/garantias-parametricvalues")
    @RolesAllowed({"ADMIN,CONFIG_BODEGA"})
    public String insertGarantiasParametricValues(@Context HttpServletRequest req) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String read;
        while((read=br.readLine()) != null) {
            stringBuilder.append(read);
        }
        br.close();
        criterialList=UTILS.fillCriterialListFromDBOBject((BasicDBList) JSON.parse(stringBuilder.toString()),criterial,criterialList);
        for(HashMap o : criterialList){
            o=UTILS.getTenant(req,o);
            fm.insert(o,UTILS.COLLECTION_ARCHIVO_PARAMETRICS_VALUES);
        }

        return  "FIRMANDO";
    }




}
