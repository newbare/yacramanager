package fr.wati.yacramanager.web.api;

import java.util.List;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import fr.wati.yacramanager.beans.Settings;
import fr.wati.yacramanager.services.CompanyService;
import fr.wati.yacramanager.services.SettingsService;
import fr.wati.yacramanager.services.UserService;
import fr.wati.yacramanager.services.impl.DtoMapper;
import fr.wati.yacramanager.web.dto.ResponseWrapper;
import fr.wati.yacramanager.web.dto.SettingsDTO;

@RestController
@RequestMapping("/app/api/settings/")
public class SettingsRestController {

	private static final Log LOG=LogFactory.getLog(SettingsRestController.class);
	
	@Inject
	private SettingsService settingsService;
	
	@Inject
	private DtoMapper dtoMapper;
	
	@Inject
	private UserService userService;
	
	@Inject
	private CompanyService companyService;
	
	@RequestMapping(value = "/company/{companyId}/{settingsId}", method = RequestMethod.GET)
	public SettingsDTO readCompanySettings(@PathVariable("settingsId") Long settingsId,@PathVariable("companyId") Long companyId) {
		return  dtoMapper.map(settingsService.findOne(settingsId));
	}
	
	@RequestMapping(value = "/user/{userId}/{settingsId}", method = RequestMethod.GET)
	public SettingsDTO readUserSettings(@PathVariable("settingsId") Long settingsId,@PathVariable("userId") Long userId) {
		return  dtoMapper.map(settingsService.findOne(settingsId));
	}

	@RequestMapping(value = "/user/{userId}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateUserSettings(@PathVariable("userId") Long userId, @RequestBody SettingsDTO dto) {
		settingsService.save(dto.toSettings(settingsService.findOne(dto.getId())));
	}
	
	@RequestMapping(value = "/company/{companyId}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateCompanySettings(@PathVariable("companyId") Long userId, @RequestBody SettingsDTO dto) {
		settingsService.save(dto.toSettings(settingsService.findOne(dto.getId())));
	}
	
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
	public ResponseWrapper<List<SettingsDTO>> getAllUserSettings(@PathVariable("userId") Long userId)
			throws RestServiceException {
		List<Settings> settings = settingsService.findByUser(userService.findOne(userId));
		return new ResponseWrapper<List<SettingsDTO>>(dtoMapper.mapSettings(settings), Long.valueOf(settings.size()));
	}
	
	@RequestMapping(value = "/company/{companyId}", method = RequestMethod.GET)
	public ResponseWrapper<List<SettingsDTO>> getAllCompanySettings(@PathVariable("companyId") Long companyId)
			throws RestServiceException {
		List<Settings> settings = settingsService.findByCompany(companyService.findOne(companyId));
		return new ResponseWrapper<List<SettingsDTO>>(dtoMapper.mapSettings(settings), Long.valueOf(settings.size()));
	}

	@RequestMapping(value = "/company/{companyId}", method = RequestMethod.POST)
	public ResponseEntity<String> createCompanySettings(@PathVariable("companyId") Long companyId,@RequestBody SettingsDTO dto) {
		settingsService.addSetting(companyService.findOne(companyId), dto.toSettings());
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.POST)
	public ResponseEntity<String> createUserSettings(@PathVariable("userId") Long userId,@RequestBody SettingsDTO dto) {
		settingsService.addSetting(userService.findOne(userId), dto.toSettings());
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value = "/company/{companyId}/{id}", method = RequestMethod.DELETE)
	public void deleteCompanySettings(@PathVariable("companyId") Long companyId,@PathVariable("id") Long id) throws RestServiceException {
		try {
			settingsService.delete(id);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RestServiceException(e.getMessage(), e);
		}
	}
	
	@RequestMapping(value = "/user/{userId}/{id}", method = RequestMethod.DELETE)
	public void deleteUserSettings(@PathVariable("userId") Long userId,@PathVariable("id") Long id) throws RestServiceException {
		try {
			settingsService.delete(id);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RestServiceException(e.getMessage(), e);
		}
		
	}

	
}
