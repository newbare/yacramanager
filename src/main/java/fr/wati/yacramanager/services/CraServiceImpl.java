package fr.wati.yacramanager.services;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.utils.CalendarUtil;
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
			day.setDayOff(isWeekEnd(currentDate) || jourFerie(currentDate));
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
	
	@SuppressWarnings("deprecation")
	private boolean jourFerie(final Date date) {
		Calendar calendar = DateUtils.toCalendar(date);
		List<Date> jourFeries=CalendarUtil.getJourFeries(calendar.get(Calendar.YEAR));
		int countMatches = CollectionUtils.countMatches(jourFeries, new Predicate() {
			
			@Override
			public boolean evaluate(Object object) {
				Date predicateDate=(Date) object;
				return DateUtils.isSameDay(predicateDate, date);
			}
		});
		return countMatches>=1;
	}
	
}
