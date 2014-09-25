package co.fifa.world.cup;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/")
public class MainController {

	protected static Logger logger = Logger.getLogger("controller");
	
	public static final String SOCCER = "soccer";
	
	@Resource(name="personService")
	private PersonService personService;
	
	/**
	 * Handles and retrieves all persons and show it in a JSP page
	 * 
	 * @return the name of the JSP page
	 */
    @RequestMapping(value = "/bett", method = RequestMethod.GET)
    public String getPersons(Model model) {
    	logger.debug("Received request to show all bets");
    	
    	// Retrieve all persons by delegating the call to PersonService
    	List<Person> persons = personService.getAll();
    	
    	// Attach persons to the Model
    	model.addAttribute("persons", persons);
    	model.addAttribute("totalAmount", persons.get(0).getTotalAmount());
    	
    	// This will resolve to /WEB-INF/jsp/personspage.jsp
    	return "personspage";
	}
    
    /**
     * Retrieves coinsurance details given Policy Id.
     * @param policyId
     * @param model
     * @return
     
    @RequestMapping(method = RequestMethod.GET, value = "bets.html")
    public String openCoinsurance(Model model) {

        System.err.println(">>22222222222222 >>>>>>>>>>>>>>>>>>>>>> ");

        return SOCCER;
    } */   
    
    @RequestMapping(method = RequestMethod.GET, value = "getSoccer.json")
    public @ResponseBody
    PersonJSONBean getPremiumHistory()
    {
        
        PersonJSONBean jsonBean = new PersonJSONBean();
        List<Person> persons = personService.getAll();
        jsonBean.setBets(persons);
        jsonBean.setTotalAmount(persons.get(0).getTotalAmount().toString());
        System.err.println("returning >>>>>>>>>>>>>>>>>>>>>>>>> ");
        return jsonBean;
    }
    

    
}
