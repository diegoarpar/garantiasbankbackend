package com.itec.services;

import com.itec.db.FactoryMongo;
import com.itec.util.UTILS;
import com.mongodb.BasicDBList;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iTech on 19/03/2017.
 */

@Path("/garantias/trd")
@Produces(MediaType.APPLICATION_JSON)
public class TRDServices {
    FactoryMongo fm = new FactoryMongo();
    HashMap<String, String> criterial= new HashMap<>();
    ArrayList<HashMap<String, String>> criterialList= new ArrayList<>();
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public List<DBObject> get(@Context HttpServletRequest req) throws IOException {
        criterial.clear();
        criterial=UTILS.fillCriterialFromString(req.getQueryString(),criterial);
        criterial=UTILS.getTenant(req,criterial);
        return fm.retriveAll(criterial,UTILS.COLLECTION_ARCHIVOS_TRD);
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @PermitAll
    public String remove(@Context HttpServletRequest req, @PathParam("id") String id) throws IOException {
        criterial.clear();
        criterial=UTILS.fillCriterialFromString(req.getQueryString(),criterial);
        fm.delete(criterial, UTILS.COLLECTION_ARCHIVOS_TRD);
        return "Elimiando";
    }
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public String insert(@Context HttpServletRequest req) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String read;
        while((read=br.readLine()) != null) {
            stringBuilder.append(read);
        }
        br.close();
        criterialList=UTILS.fillCriterialListFromDBOBject((BasicDBList) JSON.parse(stringBuilder.toString()),criterial, criterialList);
        for(HashMap o : criterialList){
            o=UTILS.getTenant(req,o);
            fm.insert(o, UTILS.COLLECTION_ARCHIVOS_TRD);
        }
        return  "FIRMANDO";
    }


}
