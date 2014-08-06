/**
 * 
 */
package fr.wati.yacramanager.utils;

import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

/**
 * @author Rachid Ouattara
 *
 */

public class DateTest {

	@Test
	public void testJodaTimeFormat(){
		DateTimeFormatter dateTimeFormatter=DateTimeFormat.fullDate().withLocale(Locale.getDefault()).withChronology(ISOChronology.getInstanceUTC());
		dateTimeFormatter.parseDateTime("2014-08-31T23:59:59+02:00");
		System.out.println(dateTimeFormatter.print(new DateTime()));
	}
}
