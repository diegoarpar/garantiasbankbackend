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
    public List<DBObject> getGarantiasFiled() throws IOException {
        criterial.clear();
        return fm.getGarantias(criterial);
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
}
