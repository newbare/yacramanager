package fr.wati.yacramanager.utils;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.joda.time.DateTime;

public class CalendarUtil {

	private static List<DateTime> datesFeries;
	
	public static List<DateTime> getJourFeries(int annee) {
		if(datesFeries==null){
			datesFeries= new ArrayList<DateTime>();

			// Jour de l'an
			GregorianCalendar jourAn = new GregorianCalendar(annee, 0, 1);
			datesFeries.add(new DateTime(jourAn));

			// Lundi de pacques
			GregorianCalendar pacques = calculLundiPacques(annee);
			datesFeries.add(new DateTime(pacques));

			// Fete du travail
			GregorianCalendar premierMai = new GregorianCalendar(annee, 4, 1);
			datesFeries.add(new DateTime(premierMai));

			// 8 mai
			GregorianCalendar huitMai = new GregorianCalendar(annee, 4, 8);
			datesFeries.add(new DateTime(huitMai));

			// Ascension (= pâques + 38 jours)
			GregorianCalendar ascension = new GregorianCalendar(annee,
					pacques.get(GregorianCalendar.MONTH),
					pacques.get(GregorianCalendar.DAY_OF_MONTH));
			ascension.add(GregorianCalendar.DAY_OF_MONTH, 38);
			datesFeries.add(new DateTime(ascension));

			// Pentecôte (= pâques + 49 jours)
			GregorianCalendar pentecote = new GregorianCalendar(annee,
					pacques.get(GregorianCalendar.MONTH),
					pacques.get(GregorianCalendar.DAY_OF_MONTH));
			pentecote.add(GregorianCalendar.DAY_OF_MONTH, 49);
			datesFeries.add(new DateTime(pentecote));

			// Fête Nationale
			GregorianCalendar quatorzeJuillet = new GregorianCalendar(annee, 6, 14);
			datesFeries.add(new DateTime(quatorzeJuillet));

			// Assomption
			GregorianCalendar assomption = new GregorianCalendar(annee, 7, 15);
			datesFeries.add(new DateTime(assomption));

			// La Toussaint
			GregorianCalendar toussaint = new GregorianCalendar(annee, 10, 1);
			datesFeries.add(new DateTime(toussaint));

			// L'Armistice
			GregorianCalendar armistice = new GregorianCalendar(annee, 10, 11);
			datesFeries.add(new DateTime(armistice));

			// Noël
			GregorianCalendar noel = new GregorianCalendar(annee, 11, 25);
			datesFeries.add(new DateTime(noel));
		} 
		

		return datesFeries;
	}
	public static GregorianCalendar calculLundiPacques(int annee) {
		int a = annee / 100;
		int b = annee % 100;
		int c = (3 * (a + 25)) / 4;
		int d = (3 * (a + 25)) % 4;
		int e = (8 * (a + 11)) / 25;
		int f = (5 * a + b) % 19;
		int g = (19 * f + c - e) % 30;
		int h = (f + 11 * g) / 319;
		int j = (60 * (5 - d) + b) / 4;
		int k = (60 * (5 - d) + b) % 4;
		int m = (2 * j - k - g + h) % 7;
		int n = (g - h + m + 114) / 31;
		int p = (g - h + m + 114) % 31;
		int jour = p + 1;
		int mois = n;

		GregorianCalendar date = new GregorianCalendar(annee, mois - 1, jour);
		date.add(GregorianCalendar.DAY_OF_MONTH, 1);
		return date;
	}
}
