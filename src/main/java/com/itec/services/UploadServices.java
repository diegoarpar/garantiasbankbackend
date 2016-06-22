package com.itec.services;

import com.itec.configuration.ConfigurationExample;
import com.itec.db.FactoryMongo;
import com.mongodb.gridfs.GridFS;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;

/**
 * Created by root on 14/06/16.
 */

@Path("/garantias/upload")
@Produces(MediaType.APPLICATION_JSON)
public class UploadServices {
    FactoryMongo fm = new FactoryMongo();

    @POST
    @Path("/save")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("fileName") String name) throws IOException {

        // TODO: uploadFileLocation should come from config.yml
        String uploadedFileLocation = ConfigurationExample.UPLOAD_FILE_PATH + fileDetail.getFileName();
        // save it
        writeToFile(uploadedInputStream, uploadedFileLocation);
        String output = "File uploaded to : " + uploadedFileLocation;
        fm.saveFileUpload(uploadedInputStream);

        return Response.ok("ok").build();
    }

    @POST
    @Path("/retrieve")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response retrieveFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("fileName") String name) throws IOException {
        // TODO: uploadFileLocation should come from config.yml
        /*String uploadedFileLocation = "/home/joag/Documents/" + fileDetail.getFileName();
        // save it
        writeToFile(uploadedInputStream, uploadedFileLocation);
        String output = "File uploaded to : " + uploadedFileLocation;*/
        //fm.retrieveFileUpload(uploadedInputStream);

        return Response.ok("ok").build();
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
