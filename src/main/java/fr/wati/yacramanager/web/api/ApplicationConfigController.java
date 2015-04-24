package fr.wati.yacramanager.web.api;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conf")
public class ApplicationConfigController implements EnvironmentAware {

	private Environment environment;
	private RelaxedPropertyResolver propertyResolver;
	
	public ApplicationConfigController() {
	}
	
	@RequestMapping(value = "/env", method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> getEnv(){
		Map<String, String> env=new HashMap<>();
		env.put("env", StringUtils.join(environment.getActiveProfiles(),","));
		env.put("google.recaptcha.publicKey", propertyResolver.getProperty("google.recaptcha.publicKey"));
		return new ResponseEntity<Map<String,String>>(env, HttpStatus.OK);
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment=environment;
		this.propertyResolver=new RelaxedPropertyResolver(environment);
	}

}
