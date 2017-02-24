/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itec.db;

import com.itec.configuration.ConfigurationExample;
import com.itec.pojo.*;
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
    private static final String COLLECTION_ARCHIVO = "archivo";
    private static final String COLLECTION_ARCHIVOS_DATOS = "archivo_fields";
    private static final String COLLECTION_ARCHIVO_PARAMETRICS_VALUES = "archivo_parametrics_values";
    private static final String COLLECTION_ARCHIVO_DOCUMENTS = "archivo_documents";

    private MongoClient mongoClient =null;
    private DB database =null ;
    private DBCursor curs;

    DBMongo dbP= new DBMongo();

    public FactoryMongo() {
    }

    public MongoClient getMongoClient(String user, String pass, String url, String dataBase){
        if(mongoClient==null){
            //mongoClient = new MongoClient(new MongoClientURI("mongodb://"+MONGO_SERVER));
            //mongoClient = new MongoClient(new MongoClientURI("mongodb://certi:Certi123@10.130.186.221:27017/?authSource=reportestelefonica&authMechanism=MONGODB-CR"));
            mongoClient = new MongoClient(
                    new MongoClientURI("mongodb://"+user+":"+pass+"@"+url+":27017/?authSource="+dataBase+"&authMechanism=MONGODB-CR"));
        }
        return mongoClient;
    }

    public DB getDatabase(String dataBase){
        database=mongoClient.getDB(dataBase);
        return database;
    }


    public DBCollection getCollection(String name, String user, String pass, String url, String dataBase){
        getMongoClient(user,pass,url,dataBase);
        getDatabase(dataBase);
        return database.getCollection(name);
    }

    public DBCollection getCollection(String collection,HashMap c){
        String tenant="";
        if(c.get("tenant")!=null)
        if(!c.get("tenant").toString().isEmpty()){
            tenant=c.get("tenant").toString();
            collection=collection+"_"+tenant;
            c.remove("tenant");
        }
        return getCollection(collection, ConfigurationExample.DATABASE_USER,ConfigurationExample.DATABASE_PASS,
                ConfigurationExample.DATABASE_SERVER_URL,ConfigurationExample.DATABASE_NAME);
    }

    public void insertGarantias(HashMap c){

        dbP.insertGarantias(getCollection(COLLECTION_ARCHIVO,c), curs, mongoClient, c);

    }
    public void actualizarGarantias(HashMap c){

        dbP.updateGarantias(getCollection(COLLECTION_ARCHIVO,c), curs, mongoClient, c);

    }
    public List<DBObject> getGarantias(HashMap c){

        return dbP.getGarantiasCriterial(getCollection(COLLECTION_ARCHIVO,c), curs, mongoClient, c);

    }
    public List<DBObject> getGarantiasFields(HashMap c){

        return dbP.getGarantiasCriterial(getCollection(COLLECTION_ARCHIVOS_DATOS,c), curs, mongoClient, c);

    }
    public void insertGarantiasFields(HashMap c){

        dbP.insertGarantias(getCollection(COLLECTION_ARCHIVOS_DATOS,c), curs, mongoClient, c);

    }
    public void deleteGarantiasFields(HashMap c){

        dbP.removeGarantias(getCollection(COLLECTION_ARCHIVOS_DATOS,c), curs, mongoClient, c);

    }
    public List<DBObject> getGarantiasParametricValues(HashMap c){

        return dbP.getGarantiasCriterial(getCollection(COLLECTION_ARCHIVO_PARAMETRICS_VALUES,c), curs, mongoClient, c);

    }
    public void insertGarantiasParametricValues(HashMap c){

        dbP.insertGarantias(getCollection(COLLECTION_ARCHIVO_PARAMETRICS_VALUES,c), curs, mongoClient, c);

    }
    public void deleteParametricValues(HashMap c){

        dbP.removeGarantias(getCollection(COLLECTION_ARCHIVO_PARAMETRICS_VALUES,c), curs, mongoClient, c);

    }

    public List<DBObject> getMetadata(HashMap c){
        return dbP.getListMetadata(getCollection(COLLECTION_ARCHIVO,c), database, c);
    }

    public List<DBObject> searchWithMetadata(ArrayList<HashMapKeyValue> criterial){
        return dbP.searchMetadata(getCollection(COLLECTION_ARCHIVO,null), criterial);
    }

    public List<DBObject> searchWithMetadata(HashMap criterial, ArrayList<HashMapKeyValue> criterial2, Long startDate, Long endDate, String word){
        return dbP.searchMetadata(getCollection(COLLECTION_ARCHIVO,criterial), criterial2, startDate, endDate, word);
    }

    public void saveFileUpload(HashMap criterial,InputStream uploadedInputStream, String location, String fileName, ObjectId garid){
        dbP.saveFileUpload(getCollection(COLLECTION_ARCHIVO_DOCUMENTS,criterial), database, uploadedInputStream,location,fileName, garid);
    }

    public GridFSDBFile retrieveFileUpload(HashMap criterial, String fileName){
        return dbP.retrieveFileUpload(getCollection(COLLECTION_ARCHIVO_DOCUMENTS,criterial), database, fileName);
    }

    public List<DBObject> retrieveListOfFiles(ObjectId garid, HashMap criterial){
        return dbP.retrieveListOfFiles(getCollection(COLLECTION_ARCHIVO_DOCUMENTS,criterial), database, garid);
    }
}
