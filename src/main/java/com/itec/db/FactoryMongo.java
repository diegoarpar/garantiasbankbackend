/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itec.db;

import com.itec.configuration.ConfigurationApp;
import com.itec.pojo.*;
import com.itec.util.UTILS;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.gridfs.GridFSDBFile;
import org.bson.types.ObjectId;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author iTech-Pc
 */
public class FactoryMongo {


    public FactoryMongo() {
    }

    public MongoClient getMongoClient(){
        return ConfigurationApp.getMongoClient(ConfigurationApp.DATABASE_USER, ConfigurationApp.DATABASE_PASS,
                ConfigurationApp.DATABASE_SERVER_URL,ConfigurationApp.DATABASE_NAME);
    }

    public DB getDatabase(){
        return getMongoClient().getDB(ConfigurationApp.DATABASE_NAME);

    }

    public DBCollection getCollection(String collectionName){
        return getDatabase().getCollection(collectionName);
    }

    public DBCollection getCollection(String collection,HashMap c){
        String tenant="";
        if(c.get("tenant")!=null)
        if(!c.get("tenant").toString().isEmpty()){
            tenant=c.get("tenant").toString();
            collection=collection+"_"+tenant;
            c.remove("tenant");
        }
        return getCollection(collection);
    }


    public void update(HashMap c, String collection){
        ConfigurationApp.dbm.updateGarantias(getCollection(collection,c),  getMongoClient(), c);

    }
    public List<DBObject> retrive(HashMap c, String collection){
        return ConfigurationApp.dbm.getGarantiasCriterial(getCollection(collection,c),  getMongoClient(), c);

    }
    public List<DBObject> retriveAll(HashMap c, String collection){
        return ConfigurationApp.dbm.getAll(getCollection(collection,c),  getMongoClient(), c);

    }

    public void insert(HashMap c, String collection){
        ConfigurationApp.dbm.insertGarantias(getCollection(collection,c),  getMongoClient(), c);

    }


    public void delete(HashMap c, String collection){
        ConfigurationApp.dbm.removeGarantias(getCollection(collection,c),  getMongoClient(), c);
    }

    public List<DBObject> getMetadata(HashMap c){
        return ConfigurationApp.dbm.getListMetadata(getCollection(UTILS.COLLECTION_ARCHIVO,c), getDatabase(), c);
    }



    public List<DBObject> searchWithMetadata(HashMap criterial, ArrayList<HashMapKeyValue> criterial2, Long startDate, Long endDate, String word){
        return ConfigurationApp.dbm.searchMetadata(getCollection(UTILS.COLLECTION_ARCHIVO,criterial), criterial2, startDate, endDate, word);
    }



}
