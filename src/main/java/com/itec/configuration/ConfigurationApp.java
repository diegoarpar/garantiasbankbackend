/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itec.configuration;

import com.itec.db.DBMongo;
import com.itec.db.FactoryMongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class ConfigurationApp extends Configuration {
    public static String DATABASE_NAME="";
    public static String DATABASE_USER="";
    public static String DATABASE_PASS="";
    public static String UPLOAD_FILE_PATH="";
    public static String DATABASE_SERVER_URL="";
    public static String URLAUTENTICATION="";
    public static String APP_USER="";
    public static String APP_PASSWORD="";
    public static String URL_CMS="";


    @NotEmpty
    private String filePath;
    @NotEmpty
    private String databasurlserver;
    @NotEmpty
    private String databaseuser;
    @NotEmpty
    private String databasepass;
    @NotEmpty
    private String databasename;
    @NotEmpty
    private String template;
    @NotEmpty
    private  String urlAutentication;
    @NotEmpty
    private  String urlCMS;
    @NotEmpty
    private  String appUser;
    @NotEmpty
    private  String appPassword;

    @NotEmpty
    private String defaultName = "Stranger";

    @JsonProperty
    public String getTemplate() {
        return template;
    }

    @JsonProperty
    public void setTemplate(String template) {
        this.template = template;
    }

    @JsonProperty
    public String getDefaultName() {
        return defaultName;
    }

    @JsonProperty
    public void setDefaultName(String name) {
        this.defaultName = name;
    }

    @JsonProperty
    public String getFilePath() {return filePath; }

    @JsonProperty
    public void setFilePath(String filePath) {
        this.filePath = filePath;
        this.UPLOAD_FILE_PATH=filePath;
    }
    @JsonProperty
    public String getDatabaseuser() {
        return databaseuser;
    }

    @JsonProperty
    public void setDatabaseuser(String databaseuser) {
        this.databaseuser = databaseuser;
        this.DATABASE_USER=databaseuser;
    }
    @JsonProperty
    public String getDatabasepass() {
        return databasepass;
    }

    @JsonProperty
    public void setDatabasepass(String databasepass) {
        this.databasepass = databasepass;
        this.DATABASE_PASS=databasepass;
    }


    @JsonProperty
    public void setDatabasename(String databasename) {
        this.databasename = databasename;
        this.DATABASE_NAME=databasename;
    }
    @JsonProperty
    public String getDatabasename() {
        return databasename;
    }
    @JsonProperty
    public void setDatabasurlserver(String databasurlserver) {
        this.databasurlserver = databasurlserver;
        this.DATABASE_SERVER_URL=databasurlserver;
    }
    @JsonProperty
    public String getDatabasurlserver() {
        return databasurlserver;
    }

    @JsonProperty
    public  String getUrlAutentication(){
        return urlAutentication;
    }
    @JsonProperty public  void setUrlAutentication(String urlAutentication){
        URLAUTENTICATION=urlAutentication;
        this.urlAutentication=urlAutentication;
    }
    @JsonProperty("urlCMS")
    public  String getUrlCMS(){
        return urlCMS;
    }
    @JsonProperty ("urlCMS")
    public  void setUrlCMS(String urlCMS){
        URL_CMS=urlCMS;
        this.urlCMS=urlCMS;
    }
    @JsonProperty("appPassword")
    public  String getAppPassword(){
        return appPassword;
    }
    @JsonProperty ("appPassword")
    public  void setAppPassword(String parameter){
        APP_PASSWORD=parameter;
        this.appPassword=parameter;
    }
    @JsonProperty("appUser")
    public  String getAppUser(){
        return appUser;
    }
    @JsonProperty ("appUser")
    public  void setAppUser(String parameter){
        APP_USER=parameter;
        this.appUser=parameter;
    }
    private static MongoClient mongoClient = null;
    public static MongoClient getMongoClient(String user, String pass, String url, String dataBase) {
        if (mongoClient == null ) {
            mongoClient = new MongoClient(new MongoClientURI("mongodb://" + user + ":" + pass + "@" + url + ":27017/?authSource=" + dataBase + "&authMechanism=MONGODB-CR"));
        }else if (mongoClient.isLocked()){
            mongoClient = new MongoClient(new MongoClientURI("mongodb://" + user + ":" + pass + "@" + url + ":27017/?authSource=" + dataBase + "&authMechanism=MONGODB-CR"));

        }

        return mongoClient;
    }
    public static DBMongo dbm= new DBMongo();
}