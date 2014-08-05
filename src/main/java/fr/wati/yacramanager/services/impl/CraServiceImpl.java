package fr.wati.yacramanager.services.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.services.AbsenceService;
import fr.wati.yacramanager.services.CraService;
import fr.wati.yacramanager.utils.CalendarUtil;
import fr.wati.yacramanager.web.dto.AbsenceDTO;
import fr.wati.yacramanager.web.dto.CraDTO;
import fr.wati.yacramanager.web.dto.CraDTO.Day;
import fr.wati.yacramanager.web.dto.CraDTO.DayElement;

@Service
public class CraServiceImpl implements CraService {

	@Autowired
	private AbsenceService absenceService;

	@Override
	public CraDTO generateCra(Employe employe, DateTime startDate, DateTime endDate) {
		CraDTO craDTO = new CraDTO(startDate, endDate);
		Day day = null;
		List<Absence> absences = absenceService
				.findByEmployeAndStartDateBetween(employe, startDate, endDate);
		for (DateTime currentDate = startDate; currentDate.isBefore(endDate); currentDate = currentDate.plusDays(1))
		{
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
					if (absence.isStartAfternoon() && absence.getStartDate().toDateMidnight().isEqual(currentDate.toDateMidnight())) {
						afternoonDayElement.setWorked(false);
						afternoonDayElement.setAbsenceDTO(AbsenceDTO
								.fromAbsence(absence));
					}else if (absence.isEndMorning() && absence.getEndDate().toDateMidnight().isEqual(currentDate.toDateMidnight())) {
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

	private boolean isDayBetween(DateTime dateToTest, DateTime startDate, DateTime endDate) {
		DateTimeComparator dateTimeComparator=DateTimeComparator.getDateOnlyInstance();
		return dateTimeComparator.compare(startDate, dateToTest)<=0 && dateTimeComparator.compare(dateToTest, endDate)<=0;
	}

	private boolean isDayOff(DateTime date) {
		return isWeekEnd(date) || jourFerie(date);
	}

	private boolean isDateInPast(DateTime date) {
		return date.isBefore(new DateTime());
	}

	private boolean isWeekEnd(DateTime date) {
		int dayOfWeek = date.getDayOfWeek();
		return (DateTimeConstants.SUNDAY == dayOfWeek || DateTimeConstants.SATURDAY == dayOfWeek);
	}

	private boolean jourFerie(final DateTime date) {
		List<DateTime> jourFeries = CalendarUtil.getJourFeries(date.getYear());
		int countMatches = CollectionUtils.countMatches(jourFeries,
				new Predicate() {

					@Override
					public boolean evaluate(Object object) {
						DateTime predicateDate = (DateTime) object;
						return predicateDate.toDateMidnight().isEqual(date.toDateMidnight());
					}
				});
		return countMatches >= 1;
	}

}
