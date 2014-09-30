////////////////////////////////////////////////////////////////////////////////
//
// Copyright (c) 2014, Suncorp Metway Limited. All rights reserved.
//
// This is unpublished proprietary source code of Suncorp Metway Limited.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
////////////////////////////////////////////////////////////////////////////////
// $Id$
// $Revision$
// $Date$
// $Author$
////////////////////////////////////////////////////////////////////////////////
package co.fifa.world.cup;

import java.net.UnknownHostException;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.authentication.UserCredentials;

import com.mongodb.MongoClient;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;


public class TestMD {

    public static void main(String[] args) throws UnknownHostException {
        final String DB_NAME = "football";
        String LOCALHOST = "127.0.0.1";
        UserCredentials userCredentials = new UserCredentials("soccer", "soccer");
        MongoClient client = new MongoClient(LOCALHOST);
        MongoTemplate mongoTemplate = new MongoTemplate(client, DB_NAME, userCredentials);
        Query query = new Query(where("name").exists(true));
        // Execute the query and find all matching entries
        List<Person> persons = mongoTemplate.find(query, Person.class);
        
        for (Person person : persons) {
            
            System.err.println(person.getName());
        }
        
        Aggregation agg = newAggregation( //
                group("id").sum("amount").as("totalAmount")
                //
        );
        
        //get total
        AggregationResults<Person> aggregate = mongoTemplate.aggregate(agg, "person", Person.class);
        Person pp = aggregate.getUniqueMappedResult();
        System.err.println("gg : " + pp.getTotalAmount());
        

     /*   Aggregation agg1 = newAggregation( //
               // group("id").sum("amount").as("totalAmount"),
                group("country").count().as("winningAmount").addToSet("country").as("name")
                
          
        );*/
        
       /* AggregationResults<Person> aggregate1 = mongoTemplate.aggregate(agg1, "person", Person.class);
        List<Person> pp2 = aggregate1.getMappedResults();
        for (Person person : pp2) {
          //  System.err.println(person.getWinningAmount());
           // System.err.println(person.getName());
        }
        
        */
       
        
        TypedAggregation<Person> agg4 = newAggregation(Person.class, //
                group("country").count().as("countryCount"),
                project("country") //
                        .andExpression(pp.getTotalAmount() + " / countryCount" ).as("winningAmount")
                        .and("country").previousOperation()
                        
        );
        
        AggregationResults<Person> aggregate55 = mongoTemplate.aggregate(agg4, Person.class);
        for (Person person : aggregate55) {
            System.err.println(person.getWinningAmount() + " " + person.getCountry());
           
        }
        
        
      //  System.err.println("gg : " + pp2.get(0).getWinningAmount());
        //get winning bit
        
      /*sys  MongoClientURI uri  = new MongoClientURI("mongodb://localhost/football"); 
        MongoClient client;
        try {
            client = new MongoClient(uri);
            DB db = client.getDB(uri.getDatabase());
            Set<String> abc = db.getCollectionNames();
            for (String string : abc) {
                System.err.println(abc);
            }
            
            
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        

    }

}
