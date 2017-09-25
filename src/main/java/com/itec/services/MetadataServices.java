package com.itec.services;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iTech on 28/03/2017.
 */

@Path("/garantias/metadata")
@Produces(MediaType.APPLICATION_JSON)
public class MetadataServices {
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
        return fm.retrive(criterial,UTILS.COLLECTION_METADATA);

    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @PermitAll
    public String remove(@Context HttpServletRequest req, @PathParam("id") String id) throws IOException {
        criterial.clear();
        criterial=UTILS.fillCriterialFromString(req.getQueryString(),criterial);
        criterial=UTILS.getTenant(req,criterial);
        fm.delete(criterial, UTILS.COLLECTION_METADATA);
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
        HashMap aux = new HashMap();
        aux=UTILS.getTenant(req,aux);

        for(HashMap o : criterialList){
            BasicDBObject temp = (BasicDBObject) o.get("json");
            temp = (BasicDBObject) o.get("json");
            o=UTILS.getTenant(req,o);
            fm.insert(o, UTILS.COLLECTION_METADATA);
        }
        return  "FIRMANDO";
    }
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/savechanges")
    public String insertAll(@Context HttpServletRequest req) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String read;
        while((read=br.readLine()) != null) {
            stringBuilder.append(read);
        }
        br.close();
        criterialList=UTILS.fillCriterialListFromDBOBject((BasicDBList) JSON.parse(stringBuilder.toString()),criterial, criterialList);
        HashMap aux = new HashMap();
        aux=UTILS.getTenant(req,aux);
        try{
            List<DBObject> tem = fm.retriveAll(aux,UTILS.COLLECTION_METADATA);
            for(DBObject db : tem){
                aux.clear();
                aux.put("o",db);
                aux=UTILS.getTenant(req,aux);
                fm.delete(aux,UTILS.COLLECTION_METADATA);
            }
        }catch (Exception e){}
        for(HashMap o : criterialList){
            BasicDBObject temp = (BasicDBObject) o.get("json");
            temp = (BasicDBObject) o.get("json");
            o=UTILS.getTenant(req,o);
            fm.insert(o, UTILS.COLLECTION_METADATA);
        }
        return  "FIRMANDO";
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/retrive")
    public List<DBObject> retrivePost(@Context HttpServletRequest req) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String read;
        while((read=br.readLine()) != null) {
            stringBuilder.append(read);
        }
        br.close();
        criterialList=UTILS.fillCriterialListFromDBOBject((BasicDBList) JSON.parse(stringBuilder.toString()),criterial, criterialList);
        HashMap o=criterialList.get(0);
        o=UTILS.getTenant(req,o);
        return fm.retrive(o, UTILS.COLLECTION_METADATA);


    }
}
