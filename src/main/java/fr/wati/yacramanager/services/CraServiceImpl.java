package fr.wati.yacramanager.services;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.utils.DateIterator;
import fr.wati.yacramanager.web.dto.CraDTO;
import fr.wati.yacramanager.web.dto.CraDTO.Day;
import fr.wati.yacramanager.web.dto.CraDTO.DayElement;

@Service
public class CraServiceImpl implements CraService {

	@Autowired
	private AbsenceService absenceService;

	@Override
	public CraDTO generateCra(Users users, Date startDate, Date endDate) {
		CraDTO craDTO = new CraDTO(startDate, endDate);
		Iterator<Date> dateIterator = new DateIterator(startDate, endDate);
		Day day = null;
		while (dateIterator.hasNext()) {
			Date currentDate = dateIterator.next();
			day = new Day();
			day.setDate(currentDate);
			day.setDayOff(isWeekEnd(currentDate));
			day.setMorning(new DayElement());
			day.setAfternoon(new DayElement());
			craDTO.addDay(day);
		}
		return craDTO;
	}

	private boolean isWeekEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return (Calendar.SUNDAY == dayOfWeek || Calendar.SATURDAY == dayOfWeek);
	}
}
