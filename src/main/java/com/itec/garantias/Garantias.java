/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itec.garantias;

import com.itec.configuration.ConfigurationExample;
import com.itec.oauth.Autenticator;
import com.itec.oauth.Autorization;
import com.itec.pojo.User;
import com.itec.services.ConfigServices;
import com.itec.services.SearchServices;
import com.itec.services.Services;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.setup.Environment;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.TimeZone;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

/**
 *
 * @author iTech-Pc
 */
public class Garantias extends  Application<ConfigurationExample> {

       public static void main (String[] args) throws Exception{

       if(args.length > 0) new Garantias().run(args);
       else{
        new Garantias().run(new String[] { "server","./src/main/java/com/itec/garantias/config.yml" });
           System.err.println("qui");
       }
    }


    @Override
    public void run(ConfigurationExample t, Environment e) throws Exception {
            t.getTemplate();
            t.getDefaultName();
        FilterRegistration.Dynamic filter = e.servlets().addFilter("CORSFilter", CrossOriginFilter.class);
                                    filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, e.getApplicationContext().getContextPath() + "*");
                                    filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
                                    filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
                                    filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Origin, Content-Type, Accept, Authorization");
                                    filter.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
        final Services db = new Services();
        final SearchServices searchServices = new SearchServices();
        final ConfigServices configServices = new ConfigServices();
        e.jersey().register(db);
        e.jersey().register(searchServices);
        e.jersey().register(configServices);

        e.jersey().register(new AuthDynamicFeature(
        new OAuthCredentialAuthFilter.Builder<User>()
            .setAuthenticator(new Autenticator())
            .setAuthorizer(new Autorization())
            .setPrefix("Bearer")
            .buildAuthFilter()));

        e.jersey().register(RolesAllowedDynamicFeature.class);
    }
}


