/**
 * 
 */
package fr.wati.yacramanager.beans;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsence;

/**
 * @author Rachid Ouattara
 * 
 */
@SuppressWarnings("serial")
@Entity
public class AbsencePortfolio implements Serializable {

	@EmbeddedId
	private AbsencePortfolioPK absencePortfolioPK;

	private Long remaining;
	private Long consumed;
	@Type(type = "org.joda.time.contrib.hibernate.PersistentDateTime")
	private DateTime lastModifiedDate = DateTime.now();

	/**
	 * @return the absencePortfolioPK
	 */
	public AbsencePortfolioPK getAbsencePortfolioPK() {
		return absencePortfolioPK;
	}

	/**
	 * @param absencePortfolioPK
	 *            the absencePortfolioPK to set
	 */
	public void setAbsencePortfolioPK(AbsencePortfolioPK absencePortfolioPK) {
		this.absencePortfolioPK = absencePortfolioPK;
	}

	/**
	 * @return the remaining
	 */
	public Long getRemaining() {
		return remaining;
	}

	/**
	 * @param remaining
	 *            the remaining to set
	 */
	public void setRemaining(Long remaining) {
		this.remaining = remaining;
	}

	/**
	 * @return the consumed
	 */
	public Long getConsumed() {
		return consumed;
	}

	/**
	 * @param consumed
	 *            the consumed to set
	 */
	public void setConsumed(Long consumed) {
		this.consumed = consumed;
	}
	
	

	/**
	 * @return the lastModifiedDate
	 */
	public DateTime getLastModifiedDate() {
		return lastModifiedDate;
	}

	/**
	 * @param lastModifiedDate the lastModifiedDate to set
	 */
	public void setLastModifiedDate(DateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}



	@Embeddable
	public static class AbsencePortfolioPK implements Serializable {
		private Long userId;
		@Enumerated(EnumType.STRING)
		private TypeAbsence typeAbsence;

		/**
		 * 
		 */
		public AbsencePortfolioPK() {
			super();
		}

		/**
		 * @param userId
		 * @param typeAbsence
		 */
		public AbsencePortfolioPK(Long userId, TypeAbsence typeAbsence) {
			super();
			this.userId = userId;
			this.typeAbsence = typeAbsence;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((typeAbsence == null) ? 0 : typeAbsence.hashCode());
			result = prime * result
					+ ((userId == null) ? 0 : userId.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			AbsencePortfolioPK other = (AbsencePortfolioPK) obj;
			if (typeAbsence != other.typeAbsence)
				return false;
			if (userId == null) {
				if (other.userId != null)
					return false;
			} else if (!userId.equals(other.userId))
				return false;
			return true;
		}

	}
}
