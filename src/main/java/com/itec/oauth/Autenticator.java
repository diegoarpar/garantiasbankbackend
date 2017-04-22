/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itec.oauth;

import java.util.Optional;

import com.itec.pojo.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

import java.io.IOException;

/**
 *
 * @author iTech-Pc
 */
public class Autenticator implements Authenticator<String, User>{
    @Override
    public Optional<User> authenticate(String token) throws AuthenticationException {
        try {
            CallServices.callGetServices(token,UrlFactory.IS_VALID_TOKEN,null);
            return  Optional.of(new User("diego",token,"123"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();

    }

}
