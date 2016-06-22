/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itec.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itec.pojo.NameValuePair;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author iTech-Pc
 */
public class ProbarJson {
    
    public void probarJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String postData = "{\"name\":\"pais\", \"value\":\"Colombia\"}";
            NameValuePair obj = mapper.readValue(postData, NameValuePair.class);
            BasicDBObject document =(BasicDBObject) JSON.parse(postData);
            System.out.println(obj.getName() + " - "+obj.getValue() );
            FactoryMongo f = new FactoryMongo();
            //f.insertDigtalizacion(postData);
        } catch (IOException ex) {
            Logger.getLogger(ProbarJson.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
}
