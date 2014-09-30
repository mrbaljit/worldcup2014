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
	
	@Resource(name="personService")
	private PersonService personService;
	
    @RequestMapping(method = RequestMethod.GET, value = "getSoccer.json")
    public @ResponseBody
    PersonJSONBean getSoccer()
    {
        PersonJSONBean jsonBean = new PersonJSONBean();
        List<Person> persons = personService.getAll();
        jsonBean.setBets(persons);
        jsonBean.setTotalAmount(persons.get(0).getTotalAmount().toString());
        return jsonBean;
    }
    

    
}
