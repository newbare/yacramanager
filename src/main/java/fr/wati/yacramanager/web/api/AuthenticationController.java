/**
 * 
 */
package fr.wati.yacramanager.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.web.dto.RegistrationDTO;

/**
 * @author Rachid Ouattara
 *
 */
@RestController
@RequestMapping("/auth/api")
public class AuthenticationController {
	
	@Autowired
	private EmployeService employeService;
	
	@RequestMapping(value="/register",method = RequestMethod.POST)
	public ResponseEntity<String> register(@RequestBody RegistrationDTO registrationDTO){
		employeService.registerEmploye(registrationDTO);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/password-recovery",method = RequestMethod.POST)
	public ResponseEntity<String> recoverPassword(@RequestParam String email){
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
