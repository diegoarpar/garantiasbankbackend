/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itec.services;

import com.itec.db.FactoryMongo;
import com.mongodb.DBObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author iTech-Pc
 */
@Path("/garantias")
@Produces(MediaType.APPLICATION_JSON)
public class Services {

    FactoryMongo f = new FactoryMongo();
        HashMap<String, String> criterial= new HashMap<>();

     @POST
     @Consumes(MediaType.APPLICATION_JSON)
     @Path("/insertGarantias")
     @PermitAll
        public String insertDigitalizacionValues(@Context HttpServletRequest req) throws IOException {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
            String read;
            while((read=br.readLine()) != null) {
                stringBuilder.append(read);
            }
            System.out.println("PARAMETRO \n\n"+stringBuilder.toString()+"\n\n");
            br.close();
            f.insertGarantias(stringBuilder.toString());
            return  "FIRMANDO";
        }
     @POST
     @Consumes(MediaType.APPLICATION_JSON)
     @Path("/updateGarantias")
     @PermitAll
    public String updateGarantias(@Context HttpServletRequest req) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String read;
        while((read=br.readLine()) != null) {
            stringBuilder.append(read);
        }
        br.close();
        f.actualizarGarantias(stringBuilder.toString());
        return  "FIRMANDO";
    }
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/getGarantias")
    @PermitAll
    public  List<DBObject> getGarantias(@Context HttpServletRequest req) throws IOException {
        fillCriterialFromString(req.getQueryString());
        return f.getGarantias(criterial);
    }
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/getNumber")
    @PermitAll
    public  String getNumber() throws IOException {
        Date d = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmssmmmm'Z'", Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
           dateFormat.format(d);

        return "[{\"number\":\""+dateFormat.format(d)+"\"}]";
    }

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
