/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itec.services;

import com.itec.db.FactoryMongo;
import com.mongodb.DBObject;
import org.adrianwalker.multilinestring.Multiline;
import sun.misc.IOUtils;


import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 * @author iTech-Pc
 */
@Path("/garantias/search")
@Produces(MediaType.APPLICATION_JSON)
public class SearchServices {

    FactoryMongo f = new FactoryMongo();

    /**
    function lines(){
        var mr = db.runCommand({
          "mapreduce" : "garantias",
          "map" : function() {
            for (var key in this) { emit(key, null); }
          },
          "reduce" : function(key, stuff) { return null; },
          "out": "garantias" + "_keys"
        })

        return db[mr.result].distinct("_id")
    }
     */
    @Multiline
    private static String searchMetaData;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/getMetadata")
    public  List<DBObject> getMetaData() throws IOException {
        return f.getMetadata(searchMetaData);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/searchWithMetadata")
    public String searchWithMetData(String jsonRequest) throws IOException {


        return  "FIRMANDO";
    }

}
