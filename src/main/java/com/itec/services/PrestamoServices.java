package com.itec.services;

import com.itec.db.FactoryMongo;
import com.itec.util.UTILS;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by iTech on 26/03/2017.
 */

@Path("/garantias/prestamo")
@Produces(MediaType.APPLICATION_JSON)
public class PrestamoServices {

    FactoryMongo fm = new FactoryMongo();
    HashMap<String, String> criterial= new HashMap<>();
    ArrayList<HashMap> criterialList= new ArrayList<>();
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/retrieve")
    public List<DBObject> get(@Context HttpServletRequest req) throws IOException {
        criterialList=UTILS.fillCriterialListFromDBOBject(req,criterial, criterialList);
        HashMap o=criterialList.get(0);
        o=UTILS.getTenant(req,o);
        return fm.retrive(o,UTILS.COLLECTION_BODEGA);

    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @PermitAll
    public String remove(@Context HttpServletRequest req, @PathParam("id") String id) throws IOException {
        criterial.clear();
        criterial=UTILS.fillCriterialFromString(req.getQueryString(),criterial);
        fm.delete(criterial, UTILS.COLLECTION_PRESTAMO);
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
                fm.delete(aux,UTILS.COLLECTION_PRESTAMO);
                obj=null;
            }catch (Exception e){
                e.printStackTrace();
            }
                fm.insert(o, UTILS.COLLECTION_PRESTAMO);
        }
        return  "Actualizado";
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/insertContainer")
    public String insertContenedor(@Context HttpServletRequest req) throws IOException {

        criterialList=UTILS.fillCriterialListFromDBOBject(req,criterial, criterialList);
        BasicDBObject obj;
        for(HashMap o : criterialList){
            o=UTILS.getTenant(req,o);
            HashMap aux = new HashMap();
            aux=UTILS.getTenant(req,aux);
            obj = new BasicDBObject();
            obj.append("key",((BasicDBObject) JSON.parse((o.get("json").toString()))).get("key"));
            obj.append("storage",((BasicDBObject) JSON.parse((o.get("json").toString()))).get("storage"));
            obj.append("code",((BasicDBObject) JSON.parse((o.get("json").toString()))).get("code"));

            aux.put("json",obj);
            try{
                fm.delete(aux,UTILS.COLLECTION_PRESTAMO);
                obj=null;
            }catch (Exception e){
                e.printStackTrace();
            }
            fm.insert(o, UTILS.COLLECTION_PRESTAMO);


        }
        return  "Actualizado";
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/retrieveContainer")
    public List<DBObject> getContainer(@Context HttpServletRequest req) throws IOException {
        criterialList=UTILS.fillCriterialListFromDBOBject(req,criterial, criterialList);
        HashMap o=criterialList.get(0);
        o=UTILS.getTenant(req,o);
        return fm.retrive(o,UTILS.COLLECTION_PRESTAMO);

    }


    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/insertContainerUbication")
    public String insertContenedorUbicacion(@Context HttpServletRequest req) throws IOException {
        criterialList=UTILS.fillCriterialListFromDBOBject(req,criterial, criterialList);
        BasicDBObject obj;

        obj = new BasicDBObject();
        obj.append("container",((BasicDBObject) JSON.parse((criterialList.get(0).get("json").toString()))).get("container"));
        HashMap aux = new HashMap();
        aux=UTILS.getTenant(req,aux);
        aux.put("json",obj);
        try{
            fm.delete(aux,UTILS.COLLECTION_PRESTAMO);
            obj=null;
        }catch (Exception e){
            e.printStackTrace();
        }

        for(HashMap o : criterialList){
            o=UTILS.getTenant(req,o);
            aux=UTILS.getTenant(req,aux);

            fm.insert(o, UTILS.COLLECTION_PRESTAMO);


        }
        return  "Actualizado";
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/insertContainerPrestamo")
    public String insertContenedorPrestamo(@Context HttpServletRequest req) throws IOException {
        criterialList=UTILS.fillCriterialListFromDBOBject(req,criterial, criterialList);

        for(HashMap o : criterialList){
            o=UTILS.getTenant(req,o);
            fm.insert(o, UTILS.COLLECTION_PRESTAMO);

        }
        return  "Actualizado";
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/retrieveContainerUbication")
    public List<DBObject> getContainerUbicacion(@Context HttpServletRequest req) throws IOException {
        criterialList=UTILS.fillCriterialListFromDBOBject(req,criterial, criterialList);
        HashMap o=criterialList.get(0);
        o=UTILS.getTenant(req,o);
        return fm.retrive(o,UTILS.COLLECTION_PRESTAMO);

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    @Path("/retrieveContainerPrestamo")
    public List<DBObject> getContainerPrestamo(@Context HttpServletRequest req) throws IOException {
        criterialList=UTILS.fillCriterialListFromDBOBject(req,criterial, criterialList);
        HashMap o=criterialList.get(0);
        o=UTILS.getTenant(req,o);
        return fm.retrive(o,UTILS.COLLECTION_PRESTAMO);

    }

}
