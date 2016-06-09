/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itec.db;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.util.JSON;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author iTech-Pc
 */
public class ProbarJsonTest {
    
    public ProbarJsonTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of probarJson method, of class ProbarJson.
     */
    //@Test
    public void testProbarJson() throws Exception {
        //System.out.println("probarJson");
        //ProbarJson instance = new ProbarJson();
        //instance.probarJson();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    @Test
    public void consultarUsuario() throws Exception {
        BasicDBObject document =(BasicDBObject) JSON.parse("{\"instanceId\":\"30120\",\"clienteTipoNotificacion\":\"null\",\"tipologiaDocumental\":\"null\",\"clienteCedula\":\"null\",\"CUN\":\"null\",\"clienteApellido\":\"null\",\"clienteCelular\":\"null\",\"clienteNombre\":\"null\",\"clienteCiudad\":\"null\",\"clienteDireccion\":\"null\",\"incidencia\":\"null\",\"clienteTelefono\":\"null\",\"numeroStiker\":\"null\",\"resolucionSIC\":\"null\",\"clienteCorreo\":\"null\",\"processKey\":\"PQRS\",\"jefe_asignado\":\"pepe\"}");
        System.out.println("LISTO");
       
        //DBFactoryMongo f = new DBFactoryMongo();
        //f.getAllDigitalizacionByProcessToday("PQRSTelefonica");
        //f.getUserByToken("[B@ccc0e9");
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
        
        Date startDate =new Date();
        startDate.setDate(startDate.getDate()-3);
        startDate.setHours(0);
        startDate.setMinutes(0);
        startDate.setSeconds(0);
        
        Date endDate =new Date();
        endDate.setDate(0);
        endDate.setHours(0);
        endDate.setMinutes(0);
        endDate.setSeconds(0);
      
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                
        BasicDBObject searchQuery2  = new BasicDBObject().append("processKey", "PROCESO PRUEBA");
                    searchQuery2.append("dateMigration",  BasicDBObjectBuilder.start("$gte", dateFormat.format(startDate)).add("$lte", dateFormat.format(endDate)).get());
    }
}
