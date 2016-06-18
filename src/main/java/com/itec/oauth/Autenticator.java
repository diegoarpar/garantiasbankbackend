/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itec.oauth;

import java.util.Optional;
import com.itec.db.FactoryMongo;
import com.itec.pojo.Token;
import com.itec.pojo.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 *
 * @author iTech-Pc
 */
public class Autenticator implements Authenticator<String, User>{
    @Override
    public Optional<User> authenticate(String token) throws AuthenticationException {
        try {
            CallToken.isValidToken(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FactoryMongo  f= new FactoryMongo();
        Token t = new Token();
        t.setToken(token);
        if (token.equals("35b6b8202ca92164151af7e2d7ea667b6bf01968d28400899fd1f0cdc5f51aa1")){
            Response.status(Response.Status.ACCEPTED);
            return  Optional.of(new User("diego",t,"123"));

        }
/*       if (f.isValidToken(t)) {
            Response.status(Response.Status.ACCEPTED);
            return Optional.of(new User("diego",t,"123"));
        }else{
                Response.status(Response.Status.ACCEPTED);
                Response.status(Response.Status.BAD_REQUEST);
                }
        return Optional.absent();
    */
     Response.status(Response.Status.ACCEPTED);
            return  Optional.of(new User("diego",t,"123"));
    }



}
