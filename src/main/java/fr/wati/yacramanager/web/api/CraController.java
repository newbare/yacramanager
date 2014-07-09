package fr.wati.yacramanager.web.api;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.wati.yacramanager.services.CraService;
import fr.wati.yacramanager.utils.SecurityUtils;
import fr.wati.yacramanager.web.dto.CraDTO;

@Controller
@RequestMapping("/app/api/cra")
public class CraController {

	@Autowired
	private CraService craService;
	
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class,
                  new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"),true));
    }
	
	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody CraDTO getCra(@RequestParam(value="start", required=true) Date startDate, @RequestParam(value="end", required=true) Date endDate){
		CraDTO craDTO=craService.generateCra(SecurityUtils.getConnectedUser(), startDate, endDate);
		return craDTO;
	}
}
