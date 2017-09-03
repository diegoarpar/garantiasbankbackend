package com.itec.services;

import com.itec.configuration.ConfigurationApp;
import com.itec.db.FactoryMongo;
import com.itec.util.MongoDbConnection;
import com.itec.util.UTILS;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import sun.net.ConnectionResetException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @PermitAll
    public List<DBObject> generate(@Context HttpServletRequest req) throws IOException, JRException {
        Thread.currentThread().getContextClassLoader();
        Map<String, Object> reportParameters = new HashMap<String, Object>();
        String fileName="garantias";
        String pathReporte = ConfigurationApp.REPORT_PATH;
        MongoDbConnection mongoDbConnection = new MongoDbConnection(fm.getMongoClient());

        ClassLoader classLoader = getClass().getClassLoader();

        File jasperFile = new File(fileName+".jasper");
        JasperCompileManager.compileReportToFile(
                pathReporte+fileName+".jrxml",
                pathReporte+fileName+"MongoDbReport.jasper");

        InputStream url = new FileInputStream(pathReporte+fileName);


                JasperReport jasperReport = JasperCompileManager.compileReport(url);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                reportParameters, mongoDbConnection);

        criterial.clear();
        criterial=UTILS.fillCriterialFromString(req.getQueryString(),criterial);
        criterial=UTILS.getTenant(req,criterial);
        return fm.retrive(criterial,UTILS.COLLECTION_REPORT);
    }

}
