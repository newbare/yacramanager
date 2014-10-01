package fr.wati.yacramanager.utils;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;

public class DateUtils {

	private DateUtils() {
	}

	
	public static int getBusinessDaysBetween(LocalDate startDate,LocalDate endDate){
		int days = Days.daysBetween(
				startDate,
				endDate).getDays()+1;
		for (LocalDate currentDate = startDate; currentDate
				.isBefore(endDate); currentDate = currentDate.plusDays(1)) {
			if(isDayOff(currentDate)){
				days-=1;
			}
		}
		return days;
	}
	
	public static boolean isDayOff(LocalDate date) {
		return isWeekEnd(date) || jourFerie(date);
	}

	public static boolean isDateInPast(DateTime date) {
		return date.isBefore(new DateTime());
	}

	public static boolean isWeekEnd(LocalDate date) {
		int dayOfWeek = date.getDayOfWeek();
		return (DateTimeConstants.SUNDAY == dayOfWeek || DateTimeConstants.SATURDAY == dayOfWeek);
	}

	public static boolean jourFerie(final LocalDate date) {
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
	
	public static boolean isDayBetween(DateTime dateToTest, DateTime startDate,
			DateTime endDate) {
		DateTimeComparator dateTimeComparator = DateTimeComparator
				.getDateOnlyInstance();
		return dateTimeComparator.compare(startDate, dateToTest) <= 0
				&& dateTimeComparator.compare(dateToTest, endDate) <= 0;
	}
}
