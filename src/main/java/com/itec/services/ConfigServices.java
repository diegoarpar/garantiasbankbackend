package com.itec.services;

import com.itec.db.FactoryMongo;
import com.mongodb.DBObject;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 14/06/16.
 */

@Path("/garantias/config")
@Produces(MediaType.APPLICATION_JSON)


public class ConfigServices {
    FactoryMongo fm = new FactoryMongo();
    HashMap<String, String> criterial= new HashMap<>();



    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/garantias-field")
    @PermitAll
    public List<DBObject> getGarantiasFiled(@Context HttpServletRequest req) throws IOException {
        criterial.clear();
        fillCriterialFromString(req.getQueryString());
        return fm.getGarantiasFields(criterial);
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/garantias-field")
    @PermitAll
    public String removeGarantiasFiled(@Context HttpServletRequest req) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String read;
        while((read=br.readLine()) != null) {
            stringBuilder.append(read);
        }
        br.close();
        fm.deleteGarantiasFields(stringBuilder.toString());
        return "Elimiando";
    }
    @POST
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
        fm.insertGarantias(stringBuilder.toString());
        return  "FIRMANDO";
    }



    /*PARAMETRICS VALUES*/
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/garantias-parametricvalues")
    @PermitAll
    public List<DBObject> getGarantiasParametricValues(@Context HttpServletRequest req) throws IOException {
        criterial.clear();
        fillCriterialFromString(req.getQueryString());
        return fm.getGarantiasParametricValues(criterial);
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/garantias-parametricvalues")
    @PermitAll
    public String removeGarantiasParametricValues(@Context HttpServletRequest req) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String read;
        while((read=br.readLine()) != null) {
            stringBuilder.append(read);
        }
        br.close();
        fm.deleteParametricValues(stringBuilder.toString());
        return "Elimiando";
    }
    @POST
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
        fm.insertGarantiasParametricValues(stringBuilder.toString());
        return  "FIRMANDO";
    }



    /*OTHER METHOD*/
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
