/**
 * 
 */
package fr.wati.yacramanager.dao.specifications;

import java.util.Date;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.beans.Absence_;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsence;

/**
 * @author Rachid Ouattara
 *
 */
public class AbsenceSpecifications {

	public static Specification<Absence> descriptionLike(final String searchTerm) {
		return new Specification<Absence>() {
			public Predicate toPredicate(Root<Absence> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.like(root.get(Absence_.description), "%" + searchTerm
						+ "%");
			}
		};
	}

	public static Specification<Absence> createdDateBetween(
			final Date startRangeDate, final Date endRangeDate) {
		return new Specification<Absence>() {
			public Predicate toPredicate(Root<Absence> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.between(root.get(Absence_.date),
						startRangeDate, endRangeDate);
			}
		};
	}

	public static Specification<Absence> startDateBetween(
			final Date startRangeDate, final Date endRangeDate) {
		return new Specification<Absence>() {
			public Predicate toPredicate(Root<Absence> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.between(root.get(Absence_.startDate),
						startRangeDate, endRangeDate);
			}
		};
	}
	
	public static Specification<Absence> endDateBetween(
			final Date startRangeDate, final Date endRangeDate) {
		return new Specification<Absence>() {
			public Predicate toPredicate(Root<Absence> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.between(root.get(Absence_.endDate),
						startRangeDate, endRangeDate);
			}
		};
	}

	public static Specification<Absence> withTypeAbsence(final TypeAbsence typeAbsence) {
		return new Specification<Absence>() {
			public Predicate toPredicate(Root<Absence> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.equal(root.get(Absence_.typeAbsence),typeAbsence);
			}
		};
	}
	public static Specification<Absence> forEmploye(final Employe employe) {
		return new Specification<Absence>() {
			public Predicate toPredicate(Root<Absence> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.equal(root.get(Absence_.employe),employe);
			}
		};
	}
}
