package fr.wati.yacramanager.services.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTimeComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.services.AbsenceService;
import fr.wati.yacramanager.services.CraService;
import fr.wati.yacramanager.utils.CalendarUtil;
import fr.wati.yacramanager.utils.DateIterator;
import fr.wati.yacramanager.web.dto.AbsenceDTO;
import fr.wati.yacramanager.web.dto.CraDTO;
import fr.wati.yacramanager.web.dto.CraDTO.Day;
import fr.wati.yacramanager.web.dto.CraDTO.DayElement;

@Service
public class CraServiceImpl implements CraService {

	@Autowired
	private AbsenceService absenceService;

	@Override
	public CraDTO generateCra(Employe employe, Date startDate, Date endDate) {
		CraDTO craDTO = new CraDTO(startDate, endDate);
		Iterator<Date> dateIterator = new DateIterator(startDate, endDate);
		Day day = null;
		List<Absence> absences = absenceService
				.findByEmployeAndStartDateBetween(employe, startDate, endDate);
		while (dateIterator.hasNext()) {
			Date currentDate = dateIterator.next();
			day = new Day();
			day.setDate(currentDate);
			day.setDayOff(isDayOff(currentDate));
			DayElement morningDayElement = new DayElement();
			boolean worked = isDateInPast(currentDate)
					&& !isDayOff(currentDate);
			morningDayElement.setWorked(worked);
			DayElement afternoonDayElement = new DayElement();
			afternoonDayElement.setWorked(worked);
			for (Absence absence : absences) {
				// test current day is between absence start and end
				if (isDayBetween(currentDate, absence.getStartDate(),
						absence.getEndDate())) {
					if (absence.isStartAfternoon() && DateUtils.isSameDay(absence.getStartDate(), currentDate)) {
						afternoonDayElement.setWorked(false);
						afternoonDayElement.setAbsenceDTO(AbsenceDTO
								.fromAbsence(absence));
					}else if (absence.isEndMorning() && DateUtils.isSameDay(absence.getEndDate(), currentDate)) {
						morningDayElement.setWorked(false);
						morningDayElement.setAbsenceDTO(AbsenceDTO
								.fromAbsence(absence));
					}else {
						afternoonDayElement.setWorked(false);
						morningDayElement.setWorked(false);
						morningDayElement.setAbsenceDTO(AbsenceDTO
								.fromAbsence(absence));
						afternoonDayElement.setAbsenceDTO(AbsenceDTO
								.fromAbsence(absence));
					}
				}
			}
			day.setMorning(morningDayElement);
			day.setAfternoon(afternoonDayElement);
			craDTO.addDay(day);
		}
		return craDTO;
	}

	private boolean isDayBetween(Date dateToTest, Date startDate, Date endDate) {
		DateTimeComparator dateOnlyInstanceComparator = DateTimeComparator.getDateOnlyInstance();
		return dateOnlyInstanceComparator.compare(startDate, dateToTest)<=0 && dateOnlyInstanceComparator.compare(dateToTest, endDate)<=0;
	}

	private boolean isDayOff(Date date) {
		return isWeekEnd(date) || jourFerie(date);
	}

	private boolean isDateInPast(Date date) {
		Calendar nowCalendar = Calendar.getInstance();
		nowCalendar.setTime(new Date());
		Calendar testDateCalendar = Calendar.getInstance();
		testDateCalendar.setTime(date);
		return testDateCalendar.before(nowCalendar);
	}

	private boolean isWeekEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		return (Calendar.SUNDAY == dayOfWeek || Calendar.SATURDAY == dayOfWeek);
	}

	private boolean jourFerie(final Date date) {
		Calendar calendar = DateUtils.toCalendar(date);
		List<Date> jourFeries = CalendarUtil.getJourFeries(calendar
				.get(Calendar.YEAR));
		int countMatches = CollectionUtils.countMatches(jourFeries,
				new Predicate() {

					@Override
					public boolean evaluate(Object object) {
						Date predicateDate = (Date) object;
						return DateUtils.isSameDay(predicateDate, date);
					}
				});
		return countMatches >= 1;
	}

}
