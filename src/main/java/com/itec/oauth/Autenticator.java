/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itec.oauth;




import com.google.common.base.Optional;
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
    public Optional<User> authenticate(String autorization) throws AuthenticationException {
        try {
            String token="";
            int length=autorization.split(",").length;
            if(length>0) {token=autorization.split(",")[0];}
            if(token==null){return Optional.absent(); }
            if(token.length()<10){return Optional.absent(); }

            String rta = CallServices.callGetServices(autorization, UrlFactory.IS_VALID_TOKEN,null);
            int cont=0;
            do{
                cont++;
                rta = CallServices.callGetServices(autorization, UrlFactory.IS_VALID_TOKEN,null);
            }while(rta.equals("ERROR")&&cont<10);
            return  Optional.of(new User("diego",autorization,"123"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.absent();

    }

}
