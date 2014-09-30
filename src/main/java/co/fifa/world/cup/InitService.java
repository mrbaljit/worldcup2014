package co.fifa.world.cup;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class InitService {

	protected static Logger logger = Logger.getLogger("service");
	
	@Resource(name="mongoTemplate")
	private MongoTemplate mongoTemplate;

	private void init() {
		// Populate our MongoDB database during server startup
		logger.debug("Init MongoDB users");
		
		// Drop existing collection
		mongoTemplate.dropCollection(Person.class);
		
		// Create new object
		Person p = new Person ();
	    //p.setPid(UUID.randomUUID().toString());
		p.setName("John");
		p.setCountry("Smith");
		p.setAmount(1000.0);
		
		// Insert to db
	    mongoTemplate.insert(p);

	}
}
