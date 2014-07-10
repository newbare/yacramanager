package fr.wati.yacramanager.web.app;

import java.security.Principal;
import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import fr.wati.yacramanager.utils.SecurityUtils;

@Controller
public class ApplicationController {

	@RequestMapping(value = "/app")
	public String index(Principal principal){
		return "redirect:/app/"+principal.getName()+"/";
	}

	@RequestMapping(value = "/app/{username}/")
	public ModelAndView userindex(Principal principal){
		ModelAndView modelAndView=new ModelAndView("app/index");
		modelAndView.addObject("userName", SecurityUtils.getConnectedUser().getFullName());
		return modelAndView;
	}
	
	
	@MessageMapping("/yacra")
	@SendTo("/topic/yacra")
	public Greeting greeting(HelloMessage message) throws Exception {
	    Thread.sleep(3000); // simulated delay
	    return new Greeting("["+new Date()+"]Hello, " + message.getName() + "!");
	}

	public static class Greeting {
	
	    private String content;
	
	    public Greeting(String content) {
	        this.content = content;
	    }
	
	    public String getContent() {
	        return content;
	    }
	}

	public static class HelloMessage {
	
	    private String name;
	
	    
	    public HelloMessage() {
			super();
		}
	
	
		public String getName() {
	        return name;
	    }
	
	}
	
}