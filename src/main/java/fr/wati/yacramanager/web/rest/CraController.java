package fr.wati.yacramanager.web.rest;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.wati.yacramanager.web.dto.CraDTO;
import fr.wati.yacramanager.web.dto.CraDTO.Day;
import fr.wati.yacramanager.web.dto.CraDTO.DayElement;

@Controller
@RequestMapping("/rest/cra")
public class CraController {

	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class,
                  new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"),true));
    }
	
	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody CraDTO getCra(@RequestParam(value="start", required=true) Date startDate, @RequestParam(value="end", required=true) Date endDate){
		Day day1=new Day();
		day1.setDate(new Date());
		day1.setMorning(new DayElement());
		day1.setAfternoon(new DayElement());
		Day day2=new Day();
		day2.setDate(new Date());
		day2.setMorning(new DayElement());
		day2.setAfternoon(new DayElement());
		Day day3=new Day();
		day3.setDate(new Date());
		day3.setMorning(new DayElement());
		day3.setAfternoon(new DayElement());
		CraDTO craDTO=new CraDTO(startDate, endDate);
		craDTO.addDay(day1);
		craDTO.addDay(day2);
		craDTO.addDay(day3);
		return craDTO;
	}
}
