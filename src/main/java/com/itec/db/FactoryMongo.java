/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itec.db;

import com.itec.pojo.*;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.gridfs.GridFSDBFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author iTech-Pc
 */
public class FactoryMongo {
    private static final String COLLECTION_GARANTIAS= "garantias";
    private static final String COLLECTION_GARANTIAS_FIELDS= "garantias_fields";
    private static final String COLLECTION_GARANTIAS_PARAMETRICS_VALUES= "garantias_parametrics_values";
    private static final String USER_PASS_GARANTIAS= "certi:certi123";
    private static final String URL_GARANTIAS= "localhost";
    private static final String DATA_BASE_GARANTIAS= "garantiasdb";
    private static final String MONGO_COLLECTION_USERS= "digitalizacion_user";
    private static final String MONGO_COLLECTION_TOKEN= "digitalizacion_token";
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
            mongoClient = new MongoClient(new MongoClientURI("mongodb://"+user+":"+pass+"@"+url+":27017/?authSource="+dataBase+"&authMechanism=MONGODB-CR"));
        }
        return mongoClient;
    }

    public DB getDatabase(String dataBase){
        if(mongoClient!=null){
            if(database==null){
                //database=mongoClient.getDB("reportestelefonica");
                //database=mongoClient.getDB("SWD_DB");
                database=mongoClient.getDB(dataBase);
            }
        }
        return database;
    }


    public DBCollection getCollection(String name, String user, String pass, String url, String dataBase){

        getMongoClient(user,pass,url,dataBase);

        getDatabase(dataBase);

        switch (name){
            case COLLECTION_GARANTIAS:
                return database.getCollection(name);
            default:
                break;
        }

        return database.getCollection(name);
    }

    public DBCollection getCollection(){
        return getCollection(COLLECTION_GARANTIAS,USER_PASS_GARANTIAS.split(":")[0],USER_PASS_GARANTIAS.split(":")[1],URL_GARANTIAS,DATA_BASE_GARANTIAS);
    }

    public void insertGarantias(String c){

        dbP.insertGarantias(getCollection(), curs, mongoClient, c);

    }
    public void actualizarGarantias(String c){

        dbP.updateGarantias(getCollection(), curs, mongoClient, c);

    }
    public List<DBObject> getGarantias(HashMap criterial){

        return dbP.getGarantiasCriterial(getCollection(), curs, mongoClient, criterial);

    }
    public List<DBObject> getGarantiasFields(HashMap criterial){

        return dbP.getGarantiasCriterial(getCollection(), curs, mongoClient, criterial);

    }
    public void insertGarantiasFields(String c){

        dbP.insertGarantias(getCollection(), curs, mongoClient, c);

    }
    public void deleteGarantiasFields(HashMap c){

        dbP.removeGarantias(getCollection(), curs, mongoClient, c);

    }
    public List<DBObject> getGarantiasParametricValues(HashMap criterial){

        return dbP.getGarantiasCriterial(getCollection(), curs, mongoClient, criterial);

    }
    public void insertGarantiasParametricValues(String c){

        dbP.insertGarantias(getCollection(), curs, mongoClient, c);

    }
    public void deleteParametricValues(HashMap c){

        dbP.removeGarantias(getCollection(), curs, mongoClient, c);

    }

    public List<DBObject> getMetadata(String criterial){
        return dbP.getListMetadata(getCollection(), database, criterial);

    }

    public List<DBObject> searchWithMetadata(ArrayList<HashMapKeyValue> criterial){
        return dbP.searchMetadata(getCollection(), criterial);

    }

    public void saveFileUpload(InputStream uploadedInputStream, String fileName){
        dbP.saveFileUpload(getCollection(), database, uploadedInputStream, fileName);
    }

    public GridFSDBFile retrieveFileUpload(String fileName){
        return dbP.retrieveFileUpload(getCollection(), database, fileName);
    }
}
