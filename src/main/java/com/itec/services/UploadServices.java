package com.itec.services;

import com.itec.configuration.ConfigurationExample;
import com.itec.db.FactoryMongo;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import org.bson.types.ObjectId;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 14/06/16.
 */

@Path("/garantias/upload")
@Produces(MediaType.APPLICATION_JSON)
public class UploadServices {
    FactoryMongo fm = new FactoryMongo();
    HashMap<String, String> criterial= new HashMap<>();
    @POST
    @Path("/save")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(@Context HttpServletRequest req,
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("fileName") String name,
            @FormDataParam("timestamp") String timestamp,
           @FormDataParam("machineIdentifier") String machineIdentifier,
           @FormDataParam("processIdentifier") String processIdentifier,
           @FormDataParam("counter") String counter

            ) throws IOException {

        ObjectId o =new ObjectId(Integer.parseInt(timestamp),
                Integer.parseInt(machineIdentifier),
                (short)Integer.parseInt(processIdentifier),
                Integer.parseInt(counter)
        );
        // TODO: uploadFileLocation should come from config.yml
        String uploadedFileLocation = ConfigurationExample.UPLOAD_FILE_PATH + fileDetail.getFileName();
        // save it
        writeToFile(uploadedInputStream, uploadedFileLocation);
        String output = "File uploaded to : " + uploadedFileLocation;
        criterial.clear();
        if(req.getHeader("Authorization").split(",").length>1) {
            criterial.put("tenant", req.getHeader("Authorization").split(",")[1]);
        }
        fm.saveFileUpload(criterial,new FileInputStream(uploadedFileLocation), uploadedFileLocation,fileDetail.getFileName(), o);

        return Response.ok().build();
    }


    @GET
    @Path("/retrieve")
    @Produces("application/pdf")
    public Response retrieveFile(@Context HttpServletRequest req,@QueryParam(value="name") String pdfFileName) throws IOException {
        criterial.clear();
        if(req.getHeader("Authorization").split(",").length>1) {
            criterial.put("tenant", req.getHeader("Authorization").split(",")[1]);
        }
        GridFSDBFile fileOutput = fm.retrieveFileUpload(criterial,pdfFileName);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        fileOutput.writeTo(stream);
        return Response
                .ok(stream.toByteArray(), MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition","attachment; filename = " + pdfFileName)
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public List<DBObject> retrieveListOfFiles(@Context HttpServletRequest req,@FormDataParam("timestamp") String timestamp,
                                              @FormDataParam("machineIdentifier") String machineIdentifier,
                                              @FormDataParam("processIdentifier") String processIdentifier,
                                              @FormDataParam("counter") String counter) throws IOException {
        ObjectId o =new ObjectId(Integer.parseInt(timestamp),
                Integer.parseInt(machineIdentifier),
                (short)Integer.parseInt(processIdentifier),
                Integer.parseInt(counter)
        );
        criterial.clear();
        if(req.getHeader("Authorization").split(",").length>1) {
            criterial.put("tenant", req.getHeader("Authorization").split(",")[1]);
        }
        return fm.retrieveListOfFiles(o,criterial);

    }


    // save uploaded file to new location
    private void writeToFile(InputStream uploadedInputStream, String uploadedFileLocation) throws IOException {
        int read;
        final int BUFFER_LENGTH = 1024;
        final byte[] buffer = new byte[BUFFER_LENGTH];
        OutputStream out = new FileOutputStream(new File(uploadedFileLocation));
        while ((read = uploadedInputStream.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        out.flush();
        out.close();
    }



}
