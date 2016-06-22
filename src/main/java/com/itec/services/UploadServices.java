package com.itec.services;

import com.itec.configuration.ConfigurationExample;
import com.itec.db.FactoryMongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
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
    public Response uploadFile(@Context HttpServletRequest req,
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail,
            @FormDataParam("fileName") String name) throws IOException {

        // TODO: uploadFileLocation should come from config.yml
        String uploadedFileLocation = ConfigurationExample.UPLOAD_FILE_PATH + fileDetail.getFileName();
        // save it
        writeToFile(uploadedInputStream, uploadedFileLocation);
        String output = "File uploaded to : " + uploadedFileLocation;
        fm.saveFileUpload(uploadedInputStream,uploadedFileLocation,fileDetail.getFileName());

        return Response.ok("ok").build();
    }

    @GET
    @Path("/retrieve")
    @Produces("application/pdf")
    public Response retrieveFile(@QueryParam(value="name") String pdfFileName) throws IOException {
        GridFSDBFile fileOutput = fm.retrieveFileUpload(pdfFileName);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        fileOutput.writeTo(stream);
        return Response
                .ok(stream.toByteArray(), MediaType.APPLICATION_OCTET_STREAM)
                .header("content-disposition","attachment; filename = " + pdfFileName)
                .build();
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
