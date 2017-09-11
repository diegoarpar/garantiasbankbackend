package com.itec.services;

import com.itec.configuration.ConfigurationApp;
import com.itec.db.FactoryMongo;
import com.itec.util.MongoDbConnection;
import com.itec.util.UTILS;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.query.JsonQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import sun.net.ConnectionResetException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.InflaterInputStream;

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
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN,CONFIG_BODEGA","USER_BODEGA","USER_REGIONAL"})
    @Path("/generate")
    public String generate(@Context HttpServletRequest req) throws IOException, JRException {

        String reporName = ConfigurationApp.REPORT_PATH+"garantias_idoneidad_json.jrxml";
        criterial.clear();
        criterial=UTILS.fillCriterialFromString(req.getQueryString(),criterial);
        criterial=UTILS.getTenant(req,criterial);


        PrintWriter writer = new PrintWriter(reporName+".json", "UTF-8");
        writer.println("[");
        List<DBObject> retrive = fm.retrive(criterial,UTILS.COLLECTION_ARCHIVO);
        for (int i=0; i<retrive.size();i++) {
           // o.removeField("_id");
            //retrive.add(o);
            writer.println(retrive.get(i).toString());
            if((i+1)<retrive.size())
                writer.println(",");
        }
        writer.println("]");
        writer.close();



        HashMap params = new HashMap();
        JasperDesign jasperDesign = JRXmlLoader.load(reporName);
        JasperReport jasperReport=JasperCompileManager.compileReport(jasperDesign);
        params.put(JsonQueryExecuterFactory.JSON_INPUT_STREAM, new FileInputStream(reporName+".json"));

        JsonDataSource datasource = new JsonDataSource(new File(reporName+".json"));
        JasperPrint objJasperPrint = JasperFillManager.fillReport( jasperReport, params, datasource );

        byte[] exportReportToPdf = JasperExportManager.exportReportToPdf(objJasperPrint);

        FileOutputStream pdf = new FileOutputStream(reporName + "prueba.pdf");
        pdf.write(exportReportToPdf);
        pdf.close();
        System.out.println("GENERADO");

        return "Generated";
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed({"ADMIN,CONFIG_BODEGA","USER_BODEGA","USER_REGIONAL"})
    @Path("/retriveReport")
    public Response getReport(@Context HttpServletRequest req) throws IOException, JRException {

        String reporName = ConfigurationApp.REPORT_PATH+"garantias_idoneidad_json.jrxml";
        InputStream stream =  new FileInputStream(reporName + "prueba.pdf");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int b;
        while ((b = stream.read(buffer)) > -1) {
            baos.write(buffer, 0, b);
        }

        return Response
                .ok(baos.toByteArray(),MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition","filename = \"" + "prueba.pdf"+"\"")
                .build();
    }

}
