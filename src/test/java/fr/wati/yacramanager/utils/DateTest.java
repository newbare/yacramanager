/**
 * 
 */
package fr.wati.yacramanager.utils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;

/**
 * @author Rachid Ouattara
 *
 */

public class DateTest {

	@Test
	public void testJodaTimeFormat(){
//		DateTimeFormatter dateTimeFormatter=DateTimeFormat.fullDate().withLocale(Locale.getDefault()).withChronology(ISOChronology.getInstanceUTC());
//		dateTimeFormatter.parseDateTime("2014-08-31T23:59:59+02:00");
//		System.out.println(dateTimeFormatter.print(new DateTime()));
		
		DateTime  dateTime=new DateTime();
		System.out.println(dateTime);
		LocalDate localDate=new LocalDate();
		System.out.println(localDate);
		LocalDateTime localDateTime=new LocalDateTime();
		System.out.println(localDateTime);
	}
}
