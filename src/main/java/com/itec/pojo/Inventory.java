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
public class Inventory {
    private String lot;
    private String amount;
    @JsonProperty
    public String getLot() {
        return lot;
    }

    public void setLot(String lot) {
        this.lot = lot;
    }
    @JsonProperty
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    
    
}
