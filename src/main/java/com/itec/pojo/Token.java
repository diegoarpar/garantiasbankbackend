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
public class Token {
    @JsonProperty
    private User user;
    @JsonProperty
    private String token;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    
    
}
