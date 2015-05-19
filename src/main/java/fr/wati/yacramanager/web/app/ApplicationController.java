package fr.wati.yacramanager.web.app;

import java.security.Principal;
import java.util.Date;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.utils.SecurityUtils;

@Controller
public class ApplicationController {

	@RequestMapping(value = "/app/view/**")
	public ModelAndView index(Principal principal){
		ModelAndView modelAndView=new ModelAndView("app/index");
		Employe connectedUser = SecurityUtils.getConnectedUser();
//		modelAndView.addObject("userFirstName", connectedUser.getFirstName());
//		modelAndView.addObject("userId", connectedUser.getId());
//		modelAndView.addObject("userCompanyName", connectedUser.getCompany().getName());
//		modelAndView.addObject("userCompanyId", connectedUser.getCompany().getId());
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
