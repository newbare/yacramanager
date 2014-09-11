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
import javax.persistence.Transient;

import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsence;
import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsenceDTO;

/**
 * @author Rachid Ouattara
 * 
 */
@SuppressWarnings("serial")
@Entity
public class AbsencePortfolio extends AuditableEntity implements Serializable {

	@EmbeddedId
	private AbsencePortfolioPK absencePortfolioPK;

	private Double remaining=0.0;
	private Double consumed=0.0;
	private Double waiting=0.0;
	

	@Transient
	private TypeAbsenceDTO typeAbsenceDTO;
	
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

	
	

	public TypeAbsenceDTO getTypeAbsenceDTO() {
		return typeAbsenceDTO;
	}

	public void setTypeAbsenceDTO(TypeAbsenceDTO typeAbsenceDTO) {
		this.typeAbsenceDTO = typeAbsenceDTO;
	}

	/**
	 * @return the remaining
	 */
	public Double getRemaining() {
		return remaining;
	}

	/**
	 * @param remaining
	 *            the remaining to set
	 */
	public void setRemaining(Double remaining) {
		this.remaining = remaining;
	}

	/**
	 * @return the consumed
	 */
	public Double getConsumed() {
		return consumed;
	}

	/**
	 * @param consumed
	 *            the consumed to set
	 */
	public void setConsumed(Double consumed) {
		this.consumed = consumed;
	}
	
	
	
	public Double getWaiting() {
		return waiting;
	}

	public void setWaiting(Double waiting) {
		this.waiting = waiting;
	}

	public void incrementRemaining(double step){
		this.remaining+=step;
	}
	
	public void incrementWaiting(double step){
		this.waiting+=step;
	}
	
	public void incrementConsumed(double step){
		this.consumed+=step;
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

		
		
		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public TypeAbsence getTypeAbsence() {
			return typeAbsence;
		}

		public void setTypeAbsence(TypeAbsence typeAbsence) {
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
