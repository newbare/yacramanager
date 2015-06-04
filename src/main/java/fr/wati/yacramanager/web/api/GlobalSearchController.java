package fr.wati.yacramanager.web.api;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.wati.yacramanager.services.GlobalSearchService;
import fr.wati.yacramanager.web.dto.SearchResult;

@RestController
@RequestMapping("/app/api/globalsearch")
public class GlobalSearchController {

	@Inject
	private GlobalSearchService globalSearchService;
	
	public GlobalSearchController() {
	}

	@RequestMapping(value = "/{textToSearch}", method = RequestMethod.GET)
	public ResponseEntity<SearchResult> search(@PathVariable("textToSearch") String textToSearch){
		return new ResponseEntity<SearchResult>(globalSearchService.search(textToSearch), HttpStatus.OK);
	}
}
