/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itec.configuration;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class ConfigurationExample extends Configuration {
    public static String DATABASE_NAME="";
    public static String DATABASE_USER="";
    public static String DATABASE_PASS="";
    public static String UPLOAD_FILE_PATH="";
    public static String DATABASE_SERVER_URL="";


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


}