package com.itec.oauth;

import com.itec.util.UTILS;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by root on 11/06/16.
 */
public class CallServices {



    private static BasicDBList objt;
    private static BufferedReader br;
    private static OutputStream os;
    public  String callGetServices( String autorization, String servicesName,HashMap<String,String> parameters ) throws IOException {
        String outputReturn="";
        String output="";
        try {

            URL url = UrlFactory.getUrl(servicesName,parameters);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer "+autorization);

            if (conn.getResponseCode() != 200) {
                System.out.println("Error al llamar el servicio "+url.toString());
                System.out.println("respuesta del servicio "+conn.getResponseCode());
                //conn.disconnect();
                return "ERROR";
            }
            br=null;
            br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            //System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                //System.out.println(output);
                outputReturn+=output;
            }

            //conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        }
        return  outputReturn;
    }
    public String callPostServices( String autorization, String servicesName,HashMap parameters ) throws IOException {
        String outputReturn="";
        String output="";
        try {

            URL url = UrlFactory.getUrl(servicesName,null);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer "+autorization);
            UTILS.tryJson(parameters);
            objt= new BasicDBList();
            objt.add((BasicDBObject)parameters.get("json"));

            os = conn.getOutputStream();
            os.write(objt.toString().getBytes());
            os.flush();

            if (conn.getResponseCode() != 200) {
                System.out.println("Error al llamar el servicio "+url.toString());
                System.out.println("respuesta del servicio "+conn.getResponseCode());
                conn.disconnect();
                return "ERROR";
            }
            br=null;
            br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            //System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
                //System.out.println(output);
                outputReturn+=output;
            }

            //conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();
            return "ERROR";

        } catch (IOException e) {

            e.printStackTrace();
            return "ERROR";

        }
        return  outputReturn;
    }
}
