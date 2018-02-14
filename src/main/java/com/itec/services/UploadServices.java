package com.itec.services;

import com.itec.configuration.ConfigurationApp;
import com.itec.db.FactoryMongo;
import com.itec.oauth.CallServices;
import com.itec.oauth.UrlFactory;
import com.itec.util.UTILS;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.util.JSON;
import org.bson.types.ObjectId;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by root on 14/06/16.
 */

@Path("/garantias/upload")
@Produces(MediaType.APPLICATION_JSON)
public class UploadServices {
    private CallServices cs = new CallServices();
    FactoryMongo fm = new FactoryMongo();
    HashMap criterial= new HashMap<>();
    @POST
    @Path("/save")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@Context HttpServletRequest req,
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("fileName") String name,
            @FormDataParam("metadata") String metadata,
            @FormDataParam("timestamp") String timestamp,
           @FormDataParam("machineIdentifier") String machineIdentifier,
           @FormDataParam("processIdentifier") String processIdentifier,
           @FormDataParam("counter") String counter

            ) throws IOException {

        ObjectId o = UTILS.generateObjectid(timestamp,machineIdentifier,processIdentifier,counter);

        criterial.clear();
        UTILS.getTenant(req,criterial);
        String fileId;
        do{
            criterial.remove("fileId");
            fileId = UUID.randomUUID().toString();
            criterial.put("fileId",fileId);

        }while(fm.retrive(criterial,UTILS.COLLECTION_ARCHIVO_DOCUMENTS).size()>0);

        BasicDBObject obj= new BasicDBObject();
        obj.put("fileName",fileDetail.getFileName());
        obj.put("internalName",fileId);
        obj.put("fechaCarga",new Date());
        obj.put("metadata",JSON.parse(metadata));
        obj.put("path",ConfigurationApp.UPLOAD_FILE_PATH);
        obj.put("status","PENDIENTE");
        obj.put("garid",o);
        criterial.clear();

        String uploadedFileLocation = ConfigurationApp.UPLOAD_FILE_PATH + fileId;
        UTILS.writeToFile(uploadedInputStream, uploadedFileLocation);

        UTILS.getTenant(req,criterial);
        criterial.put("json",obj);
        fm.insert(criterial,UTILS.COLLECTION_ARCHIVO_DOCUMENTS);



        criterial.clear();
        criterial.put("garid",o);
        criterial.put("caseFolder",o);
        criterial.put("fileName", fileDetail.getFileName());
        criterial.put("internalName", fileId);
        criterial.put("metadata",JSON.parse(metadata));
        criterial.put("path",ConfigurationApp.UPLOAD_FILE_PATH);

        String rta ="ERROR";
        int cont=0;
        do{
             cont++;
             rta = cs.callPostServices(req.getHeader("Authorization"), UrlFactory.INSERT_FILE_CENTRAL,criterial);

        }while (rta.equals("ERROR")&&cont<10);

        return Response.ok().build();
    }


    @GET
    @Path("/retrieve")
    @Produces("application/pdf")
    public Response retrieveFile(@Context HttpServletRequest req,@QueryParam(value="name") String pdfFileName) throws IOException {
        criterial.clear();
        UTILS.getTenant(req,criterial);

        //GridFSDBFile fileOutput = fm.retrieveFileUpload(criterial,pdfFileName);
        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //fileOutput.writeTo(stream);
       // return Response
                //.ok(stream.toByteArray(), MediaType.APPLICATION_OCTET_STREAM)
                //.header("content-disposition","attachment; filename = " + pdfFileName)
               // .build();
        return null;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public List<DBObject> retrieveListOfFiles(@Context HttpServletRequest req,@FormDataParam("timestamp") String timestamp,
                                              @FormDataParam("machineIdentifier") String machineIdentifier,
                                              @FormDataParam("processIdentifier") String processIdentifier,
                                              @FormDataParam("counter") String counter) throws IOException {
        ObjectId o = UTILS.generateObjectid(timestamp,machineIdentifier,processIdentifier,counter);
        criterial.clear();
        UTILS.getTenant(req,criterial);
        DBObject obj = new BasicDBObject();
        obj.put("garid",o);
        criterial.put("garid",obj);
        return fm.retrive(criterial,UTILS.COLLECTION_ARCHIVO_DOCUMENTS);

    }


  }
