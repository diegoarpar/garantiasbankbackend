package com.itec.services;

import com.itec.configuration.ConfigurationApp;
import com.itec.db.FactoryMongo;
import com.itec.util.UTILS;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
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

@Path("/garantias/report")
@Produces(MediaType.APPLICATION_JSON)
public class ReportServices {
    FactoryMongo fm = new FactoryMongo();
    HashMap<String, String> criterial= new HashMap<>();
    ArrayList<HashMap<String, DBObject>> criterialList= new ArrayList<>();
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN,CONFIG_BODEGA","USER_BODEGA","USER_REGIONAL"})
    public List<DBObject> get(@Context HttpServletRequest req) throws IOException {
        criterial.clear();
        criterial=UTILS.fillCriterialFromString(req.getQueryString(),criterial);
        criterial=UTILS.getTenant(req,criterial);
        return fm.retrive(criterial,UTILS.COLLECTION_REPORT);
    }

    @DELETE
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed({"ADMIN,CONFIG_BODEGA","USER_BODEGA","USER_REGIONAL"})
    public String remove(@Context HttpServletRequest req, @PathParam("id") String id) throws IOException {
        criterial.clear();
        criterial=UTILS.fillCriterialFromString(req.getQueryString(),criterial);
        fm.delete(criterial, UTILS.COLLECTION_REPORT);
        return "Elimiando";
    }
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN,CONFIG_BODEGA","USER_BODEGA","USER_REGIONAL"})
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
            fm.insert(o, UTILS.COLLECTION_REPORT);
        }
        return  "FIRMANDO";
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN,CONFIG_BODEGA","USER_BODEGA","USER_REGIONAL"})
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
            return fm.retrive(o, UTILS.COLLECTION_REPORT);


    }
    @GET
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @RolesAllowed({"ADMIN,CONFIG_BODEGA","USER_BODEGA","USER_REGIONAL"})
    @Path("/generate")
    public List<DBObject> generate(@Context HttpServletRequest req) throws IOException {
        String pathReporte = ConfigurationApp.REPORT_PATH;
        JasperReport reporte = (JasperReport) JRLoader.loadObject("reporte1.jasper");
        criterial.clear();
        criterial=UTILS.fillCriterialFromString(req.getQueryString(),criterial);
        criterial=UTILS.getTenant(req,criterial);
        return fm.retrive(criterial,UTILS.COLLECTION_REPORT);
    }

}
