/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itec.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author iTech-Pc
 */
public class Category {
    private String name;
    private String cod;
    private String description;
    private String imageLocation;

     @JsonProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @JsonProperty
    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }
     @JsonProperty
    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }
    @JsonProperty
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    
}
