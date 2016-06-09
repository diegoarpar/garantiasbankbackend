/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itec.oauth;

import com.google.common.base.Optional;
import com.itec.db.FactoryMongo;
import com.itec.pojo.Token;
import com.itec.pojo.User;
import io.dropwizard.auth.Authorizer;
import java.util.UUID;
import javax.ws.rs.core.Response;


/**
 *
 * @author iTech-Pc
 */
public class Autorization implements Authorizer<User> {
    FactoryMongo f = new FactoryMongo();

    @Override
    public boolean authorize(User u, String role) {
        FactoryMongo  f= new FactoryMongo();
         String token = UUID.randomUUID().toString();
         Token t = new Token();
         t.setToken(token);
        if (token.equals("35b6b8202ca92164151af7e2d7ea667b6bf01968d28400899fd1f0cdc5f51aa1")){
            Response.status(Response.Status.ACCEPTED);
            return  true;
            
        }
         //if(f.isValidUser(u)){
         //    f.insertToken(t, u);
         //    return true;
         //}
        return true;
    }
}