package com.itec.services;

import com.itec.db.FactoryMongo;
import com.itec.util.UTILS;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DuplicateKeyException;
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
 * Created by iTech on 26/03/2017.
 */

@Path("/garantias/regional")
@Produces(MediaType.APPLICATION_JSON)
public class RegionalServices {

    FactoryMongo fm = new FactoryMongo();
    HashMap<String, String> criterial= new HashMap<>();
    ArrayList<HashMap> criterialList= new ArrayList<>();
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public List<DBObject> get(@Context HttpServletRequest req) throws IOException {
        criterial.clear();
        criterial= UTILS.fillCriterialFromString(req.getQueryString(),criterial);
        criterial=UTILS.getTenant(req,criterial);
            return fm.retrive(criterial,UTILS.COLLECTION_REGIONAL);

    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @PermitAll
    public String remove(@Context HttpServletRequest req, @PathParam("id") String id) throws IOException {
        criterial.clear();
        criterial=UTILS.fillCriterialFromString(req.getQueryString(),criterial);
        fm.delete(criterial, UTILS.COLLECTION_REGIONAL);
        return "Elimiando";
    }
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public String insert(@Context HttpServletRequest req) throws IOException {
        criterialList=UTILS.fillCriterialListFromDBOBject(req,criterial, criterialList);
        BasicDBObject obj;
        for(HashMap o : criterialList){
            o=UTILS.getTenant(req,o);
                HashMap aux = new HashMap();
                aux=UTILS.getTenant(req,aux);
                 obj = new BasicDBObject();
                 obj.append("key",((BasicDBObject) JSON.parse((o.get("json").toString()))).get("key"));

                aux.put("json",obj);
            try{
                fm.delete(aux,UTILS.COLLECTION_REGIONAL);
                obj=null;
            }catch (Exception e){
                e.printStackTrace();
            }
                fm.insert(o, UTILS.COLLECTION_REGIONAL);
        }
        return  "Actualizado";
    }
}
