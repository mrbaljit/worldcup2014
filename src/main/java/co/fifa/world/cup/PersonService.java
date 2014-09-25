package co.fifa.world.cup;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service("personService")
@Transactional
public class PersonService {

	protected static Logger logger = Logger.getLogger("service");
	
	@Resource(name="mongoTemplate")
	private MongoTemplate mongoTemplate;
	
	/**
	 * Retrieves all persons
	 */
	public List<Person> getAll() {
		logger.debug("Retrieving all persons");
 
		// Find an entry where pid property exists
        Query query = new Query(where("name").exists(true));
        query.with(new Sort(Sort.Direction.ASC, "name"));
        // Execute the query and find all matching entries
        List<Person> persons = mongoTemplate.find(query, Person.class);
        Aggregation aggTotalAmt = newAggregation(group("id").sum("amount").as("totalAmount"));
        
        AggregationResults<Person> aggregate = mongoTemplate.aggregate(aggTotalAmt, "person", Person.class);
        BigDecimal totalBetAmount = aggregate.getUniqueMappedResult().getTotalAmount();
        
        TypedAggregation<Person> winningAmt = newAggregation(Person.class, //
                group("country").count().as("countryCount"),
                project("country") //
                        .andExpression(totalBetAmount + " / countryCount" ).as("winningAmount")
                        .and("country").previousOperation()
                        
        );
        
        AggregationResults<Person> aggWinAmt = mongoTemplate.aggregate(winningAmt, Person.class);
        for (Person person : aggWinAmt) {
            for (Person p : persons) {
                
                if(p.getCountry().equals(person.getCountry()))
                {
                    p.setWinningAmount(person.getWinningAmount());
                    p.setTotalAmount(totalBetAmount);
                }
            }
           
        }
        
        
        logger.debug("Persons size : " + persons.size());
		return persons;
	}
	
	/**
	 * Retrieves a single person
	 */
	public Person get( String name ) {
		logger.debug("Retrieving an existing person");
		
		// Find an entry where pid matches the id
        Query query = new Query(where("name").is(name));
        // Execute the query and find one matching entry
        Person person = mongoTemplate.findOne(query, Person.class);
       
    	
		return person;
	}


	

}
