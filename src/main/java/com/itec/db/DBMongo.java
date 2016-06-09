/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itec.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itec.pojo.Category;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.bson.types.ObjectId;

/**
 *
 * @author iTech-Pc
 */

public class DBMongo {
    public String insertGarantias(DBCollection collection,DBCursor curs,MongoClient mongoClient, String c){
    
        BasicDBList documentList =(BasicDBList) JSON.parse(c);
        BasicDBObject document;
        for (Object object : documentList) {
            document=(BasicDBObject) object;
            collection.insert(document);
        }
        
        
    return "Insertado";
    }
    public String updateGarantias(DBCollection collection,DBCursor curs,MongoClient mongoClient, String c){
    
        BasicDBList documentList =(BasicDBList) JSON.parse(c);
        BasicDBObject document ;
        BasicDBObject searchQuery2  = new BasicDBObject();
        BasicDBObject _id;
        for (Object object : documentList) {
            document=(BasicDBObject) object;
            //collection.update(searchQuery2.append("_id", document.get("_id")),document);
             _id=(BasicDBObject) document.get("_id");
             ObjectId o =new ObjectId((int)_id.get("timestamp"), (int)_id.get("machineIdentifier"), (short)(int)_id.get("processIdentifier"), (int)_id.get("counter")); 
             searchQuery2.append("_id", o);
             document.remove("_id");
             collection.remove(searchQuery2);
            collection.insert(document);
        }
        
    return "actualizado";
    }
    public List<DBObject> getGarantiasCriterial(DBCollection collection,DBCursor curs,MongoClient mongoClient, HashMap criterial){
        List<DBObject> data= new ArrayList<>();
        BasicDBObject searchQuery2  = new BasicDBObject();
        Iterator it = criterial.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            
            searchQuery2.append(pair.getKey().toString(),pair.getValue().toString().equals("null")?null:pair.getValue().toString().equals("true")?true:pair.getValue().toString());
            it.remove(); 
        }
         
        //BasicDBObject searchQuery2  = new BasicDBObject();
        curs=collection.find(searchQuery2);
        while(curs.hasNext()) {
                DBObject o = curs.next();
                data.add(o);
            }
    return data;
    }
}
