/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itec.db;

import com.itec.pojo.HashMapKeyValue;
import com.mongodb.*;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.mongodb.util.JSON;
import org.adrianwalker.multilinestring.Multiline;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
    public String removeGarantias(DBCollection collection,DBCursor curs,MongoClient mongoClient, HashMap criterial){

        List<DBObject> data= new ArrayList<>();
        BasicDBObject searchQuery2  = new BasicDBObject();
        Iterator it = criterial.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            searchQuery2.append(pair.getKey().toString(),pair.getValue().toString().equals("null")?null:pair.getValue().toString().equals("true")?true:pair.getValue().toString());
            it.remove();
        }

        //BasicDBObject searchQuery2  = new BasicDBObject();
        collection.remove(searchQuery2);

        return "eliminado";
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

    /**
    function(){
        for (var key in this) { emit(key, null);}
     }
     */
    @Multiline
    private static String map;



    /**
  function(key, stuff) {
            for (var key in this) { return null;}
          }
     */
    @Multiline
    private static String reduce;

    public List getListMetadata(DBCollection dbCollection, DB dataBase,  String criterial){
        MapReduceCommand cmd = new MapReduceCommand(dbCollection, map, reduce,
            "garantias_keys" , MapReduceCommand.OutputType.INLINE, null);
        MapReduceOutput out = dbCollection.mapReduce(cmd);
        DBCollection dbCollection1 = dataBase.getCollection("garantias_keys");
        ArrayList dbCollectionLista = (ArrayList)dbCollection1.distinct("_id");
        return dbCollectionLista;
    }

    public List<DBObject> searchMetadata(DBCollection collection, ArrayList<HashMapKeyValue> criterial){
        List<DBObject> data= new ArrayList<>();
        BasicDBObject andQuery = new BasicDBObject();
        List<BasicDBObject> searchQuery2  =  new ArrayList<BasicDBObject>();
        DBCursor curs;
        if(criterial.size()>0) {
            if (criterial.size() > 1) {
                for (HashMapKeyValue hashMapKeyValue : criterial) {
                    searchQuery2.add(new BasicDBObject(hashMapKeyValue.getKey(), hashMapKeyValue.getValue()));
                }
                andQuery.put("$and", searchQuery2);
                curs = collection.find(andQuery);
            }
            else {
                BasicDBObject basicDBObject = new BasicDBObject(criterial.get(0).getKey(), criterial.get(0).getValue());
                curs = collection.find(basicDBObject);
            }
        }
        else{
            curs = collection.find();
        }

         while (curs.hasNext()) {
            DBObject o = curs.next();
            data.add(o);
        }
        return data;

    }

    public List saveFileUpload(DBCollection dbCollection, DB dataBase,  InputStream uploadedInputStream, String location, String fileName){
        GridFS gridfs = new GridFS(dataBase, "downloads");
        GridFSInputFile gfsFile = gridfs.createFile(uploadedInputStream);
        gfsFile.setFilename(fileName);
        gfsFile.save();
        //
        // Let's create a new JSON document with some "metadata" information on the download
        //
        BasicDBObject info = new BasicDBObject();
        info.put("fileName", fileName);
        info.put("rawPath", location);

        //
        // Let's store our document to MongoDB
        //
        dbCollection.insert(info, WriteConcern.SAFE);

        return null;
    }

    public GridFSDBFile retrieveFileUpload(DBCollection dbCollection, DB dataBase,  String fileName){
        GridFS gridfs = new GridFS(dataBase, "downloads");
        // get image file by it's filename
        GridFSDBFile imageForOutput = gridfs.findOne(fileName);
        return imageForOutput;
    }

}
