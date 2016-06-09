/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itec.pojo;

/**
 *
 * @author iTech-Pc
 */
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
public class NameValuePair {
    private String name;
    private String value;
    private String firstFilter;
    private String second_Filter;

    @JsonProperty
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @JsonProperty
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    @JsonProperty
    public String getFirstFilter() {
        return firstFilter;
    }

    public void setFirstFilter(String firstFilter) {
        this.firstFilter = firstFilter;
    }
    @JsonProperty
    public String getSecond_Filter() {
        return second_Filter;
    }

    public void setSecond_Filter(String second_Filter) {
        this.second_Filter = second_Filter;
    }
    
    
    
}
