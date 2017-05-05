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

import java.io.InputStream;
import java.util.*;

/**
 *
 * @author iTech-Pc
 */

public class DBMongo {
    public void insertGarantias(DBCollection collection,DBCursor curs,MongoClient mongoClient, HashMap criterial){
        BasicDBObject searchQuery2  = new BasicDBObject();
        Iterator it = criterial.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            //searchQuery2.append(pair.getKey().toString(),pair.getValue()!=null?pair.getValue().toString().equals("null")?null:pair.getValue().toString().equals("true")?true:pair.getValue().toString():null);
            searchQuery2=(BasicDBObject)JSON.parse(pair.getValue().toString());
        }

        collection.insert(searchQuery2);

    }
    public void updateGarantias(DBCollection collection,DBCursor curs,MongoClient mongoClient, HashMap criterial){
        BasicDBObject _id  = new BasicDBObject();
        BasicDBObject searchQuery2  = new BasicDBObject();
        Iterator it = criterial.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            searchQuery2=(BasicDBObject)JSON.parse(pair.getValue().toString());
        }
        _id=(BasicDBObject)JSON.parse(searchQuery2.get("_id").toString());
        searchQuery2.remove("_id");
        ObjectId o =new ObjectId((int)_id.get("timestamp"), (int)_id.get("machineIdentifier"), (short)(int)_id.get("processIdentifier"), (int)_id.get("counter"));
        //searchQuery2.remove("_id");
        collection.update(new BasicDBObject("_id", o),searchQuery2);

    }
    public String removeGarantias(DBCollection collection,DBCursor curs,MongoClient mongoClient, HashMap criterial){

        List<DBObject> data= new ArrayList<>();
        BasicDBObject searchQuery2  = new BasicDBObject();
        Iterator it = criterial.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            //searchQuery2.append(pair.getKey().toString(),pair.getValue().toString().equals("null")?null:pair.getValue().toString().equals("true")?true:pair.getValue().toString());

            //it.remove();
            searchQuery2=(BasicDBObject)JSON.parse(pair.getValue().toString());
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
            try{
                searchQuery2=(BasicDBObject)JSON.parse(pair.getValue().toString());
            }catch (Exception e){


                searchQuery2.append(pair.getKey().toString(),pair.getValue().toString().equals("null")?null:pair.getValue().toString().equals("true")?true:pair.getValue().toString());
            }
            //it.remove();
        }

        //BasicDBObject searchQuery2  = new BasicDBObject();
        curs=collection.find(searchQuery2);

        while(curs.hasNext()) {
                DBObject o = curs.next();
                data.add(o);
            }
        return data;
    }
    public List<DBObject> getAll(DBCollection collection,DBCursor curs,MongoClient mongoClient, HashMap criterial){
        List<DBObject> data= new ArrayList<>();

        curs=collection.find();

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

    public List getListMetadata(DBCollection dbCollection, DB dataBase,  HashMap criterial){
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

    public List<DBObject> searchMetadata(DBCollection collection, ArrayList<HashMapKeyValue> criterial,
                                         Long startDate, Long endDate, String word){

        ObjectId objectStartDate= null;
        ObjectId objectEndDate = null;
        BasicDBObject objectDate = null;
        BasicDBObject basicObjectStartDate=null;
        BasicDBObject basicObjectEndDate = null;

        if(startDate != null) {
            objectStartDate = new ObjectId(new Date(startDate));
            basicObjectStartDate = new BasicDBObject("_id", new BasicDBObject("$gte", objectStartDate));
        }
        if(endDate !=null) {
            objectEndDate = new ObjectId(new Date(endDate));
            basicObjectEndDate = new BasicDBObject("_id", new BasicDBObject("$lte", objectEndDate));
        }
        if(startDate!=null && endDate !=null)
            objectDate = new BasicDBObject("_id", new BasicDBObject("$gte", objectStartDate).append("$lte",objectEndDate));

        List<DBObject> data= new ArrayList<>();
        BasicDBObject andQuery = new BasicDBObject();
        List<BasicDBObject> searchQuery2  =  new ArrayList<BasicDBObject>();

        DBCursor curs;

        if(criterial.size()>0) {
            if (criterial.size() > 1) {
                for (HashMapKeyValue hashMapKeyValue : criterial) {
                    searchQuery2.add(new BasicDBObject(hashMapKeyValue.getKey(), hashMapKeyValue.getValue()));
                }

                if(startDate !=null && endDate !=null){
                    searchQuery2.add(objectDate);
                }
                else if(startDate !=null ){
                    searchQuery2.add(basicObjectStartDate);
                }
                else if(endDate !=null) {
                    searchQuery2.add(basicObjectEndDate);
                }
                andQuery.put("$and", searchQuery2);
                curs = collection.find(andQuery);

            }
            else {
                BasicDBObject basicDBObject = new BasicDBObject(criterial.get(0).getKey(), criterial.get(0).getValue());
                curs = collection.find(basicDBObject);
            }
        }
        else if(startDate !=null && endDate !=null){
                curs = collection.find(objectDate);
        }
        else if(startDate !=null ){
            curs = collection.find(basicObjectStartDate);
        }
        else if(endDate !=null)
            curs = collection.find(basicObjectEndDate);
        else {
            curs = collection.find();
        }

        while (curs.hasNext()) {
            DBObject o = curs.next();
            data.add(o);
        }
        return data;

    }





}
