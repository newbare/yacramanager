package fr.wati.yacramanager.services.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.WorkLog;
import fr.wati.yacramanager.services.AbsenceService;
import fr.wati.yacramanager.services.CraService;
import fr.wati.yacramanager.services.EmployeService;
import fr.wati.yacramanager.services.WorkLogService;
import fr.wati.yacramanager.utils.CalendarUtil;
import fr.wati.yacramanager.utils.DtoMapper;
import fr.wati.yacramanager.web.dto.AbsenceDTO;
import fr.wati.yacramanager.web.dto.CraDTO;
import fr.wati.yacramanager.web.dto.CraDTO.Day;
import fr.wati.yacramanager.web.dto.CraDTO.DayElement;
import fr.wati.yacramanager.web.dto.CraDetailsDTO;
import fr.wati.yacramanager.web.dto.CraDetailsDTO.CraAbsenceDetail;
import fr.wati.yacramanager.web.dto.CraDetailsDTO.CraDetailDay;
import fr.wati.yacramanager.web.dto.CraDetailsDTO.CraTaskRow;
import fr.wati.yacramanager.web.dto.CraDetailsDTO.EmployeCraDetailsDTO;

@Service
public class CraServiceImpl implements CraService {

	@Autowired
	private AbsenceService absenceService;

	@Autowired
	private WorkLogService workLogService;

	@Autowired
	private EmployeService employeService;

	@Override
	public CraDTO generateCra(Employe employe, DateTime startDate,
			DateTime endDate) {
		CraDTO craDTO = new CraDTO(startDate, endDate);
		Day day = null;
		List<Absence> absences = absenceService
				.findByEmployeAndStartDateBetween(employe, startDate, endDate);
		for (DateTime currentDate = startDate; currentDate.isBefore(endDate); currentDate = currentDate
				.plusDays(1)) {
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
					if (absence.isStartAfternoon()
							&& absence.getStartDate().toDateMidnight()
									.isEqual(currentDate.toDateMidnight())) {
						afternoonDayElement.setWorked(false);
						afternoonDayElement.setAbsenceDTO(AbsenceDTO
								.fromAbsence(absence));
					} else if (absence.isEndMorning()
							&& absence.getEndDate().toDateMidnight()
									.isEqual(currentDate.toDateMidnight())) {
						morningDayElement.setWorked(false);
						morningDayElement.setAbsenceDTO(AbsenceDTO
								.fromAbsence(absence));
					} else {
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

	private boolean isDayBetween(DateTime dateToTest, DateTime startDate,
			DateTime endDate) {
		DateTimeComparator dateTimeComparator = DateTimeComparator
				.getDateOnlyInstance();
		return dateTimeComparator.compare(startDate, dateToTest) <= 0
				&& dateTimeComparator.compare(dateToTest, endDate) <= 0;
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
						return predicateDate.toDateMidnight().isEqual(
								date.toDateMidnight());
					}
				});
		return countMatches >= 1;
	}

	@Override
	public CraDetailsDTO generateCraDetail(Iterable<Employe> employes,
			DateTime startDate, DateTime endDate) {
		CraDetailsDTO craDTO = new CraDetailsDTO();
		craDTO.setStartDate(startDate);
		craDTO.setEndDate(endDate);
		EmployeCraDetailsDTO employeCraDetailsDTO = null;
		// iterate over employees
		
		List<Employe> employesList=Lists.newArrayList(employes);
		Collections.sort(employesList, new Comparator<Employe>() {
			@Override
			public int compare(Employe employe1, Employe employe2) {
				return employe1.getId().compareTo(employe2.getId());
			}
		});
		for (Employe currentEmploye : employesList) {

			employeCraDetailsDTO = new EmployeCraDetailsDTO();
			employeCraDetailsDTO.setEmployeId(currentEmploye.getId());
			employeCraDetailsDTO.setEmployeName(currentEmploye.getFullName());
			craDTO.getEmployeCraDetailsDTOs().add(employeCraDetailsDTO);
			List<Absence> absences = absenceService
					.findByEmployeAndStartDateBetween(currentEmploye,
							startDate, endDate);
			CraAbsenceDetail craAbsenceDetail = new CraAbsenceDetail();
			List<WorkLog> employeWorkLogs = workLogService
					.findByEmployeAndStartDateBetween(currentEmploye,
							startDate, endDate);
			// handle CraTaskRow
			CraTaskRow craTaskRow = null;
			Map<Long, CraTaskRow> craTaskMap=new HashMap<>();
			for (WorkLog workLog : employeWorkLogs) {
				if(craTaskMap.containsKey(workLog.getTask().getId())){
					craTaskRow=craTaskMap.get(workLog.getTask().getId());
				}else {
					craTaskRow=new CraTaskRow();
					craTaskRow.setProject(DtoMapper.map(workLog.getTask()
							.getProject()));
					craTaskRow.setTask(DtoMapper.map(workLog.getTask()));
					craTaskMap.put(workLog.getTask().getId(), craTaskRow);
					employeCraDetailsDTO.getTaskRows().add(craTaskRow);
				}
				for (DateTime currentDate = startDate; currentDate
						.isBefore(endDate); currentDate = currentDate
						.plusDays(1)) {
					Long currentDuration=0L;
					switch (workLog.getWorkLogType()) {
					case DURATION:
						if (DateTimeComparator
								.getDateOnlyInstance().compare(currentDate, workLog.getStartDate())==0) {
							currentDuration=workLog.getDuration();
						}
						break;
					case TIME:
						if(isDayBetween(currentDate, workLog.getStartDate(),
								workLog.getEndDate())){
							currentDuration=(workLog.getEndDate().getMillis()-workLog.getStartDate().getMillis())/1000/60;
						}
						break;
					default:
						break;
					}
					if(!craTaskRow.getDuration().containsKey(currentDate)){
						craTaskRow.getDuration().put(currentDate,
								currentDuration);
					}else {
						craTaskRow.getDuration().put(currentDate,craTaskRow.getDuration().get(currentDate)+
								currentDuration);
					}
				}
			}

			for (DateTime currentDate = startDate; currentDate
					.isBefore(endDate); currentDate = currentDate.plusDays(1)) {
				employeCraDetailsDTO.getDays().add(
						new CraDetailDay(currentDate, isDayOff(currentDate)));

				// handle absence row
				for (Absence currentAbsence : absences) {
					if (isDayBetween(currentDate,
							currentAbsence.getStartDate(),
							currentAbsence.getEndDate())
							&& currentAbsence.isValidated()) {
						if ((currentAbsence.isStartAfternoon() && currentAbsence
								.getStartDate().toDateMidnight()
								.isEqual(currentDate.toDateMidnight()))
								|| (currentAbsence.isEndMorning() && currentAbsence
										.getEndDate().toDateMidnight()
										.isEqual(currentDate.toDateMidnight()))) {
							craAbsenceDetail.getDuration().put(currentDate,
									4 * 60L);
						} else {
							craAbsenceDetail.getDuration().put(currentDate,
									8 * 60L);
						}
					} else {
						if(!craAbsenceDetail.getDuration().containsKey(currentDate)){
							craAbsenceDetail.getDuration().put(currentDate, 0L);
						}
						
					}
				}
				employeCraDetailsDTO.setCraAbsenceDetail(craAbsenceDetail);
			}
		}
		return craDTO;
	}

}
