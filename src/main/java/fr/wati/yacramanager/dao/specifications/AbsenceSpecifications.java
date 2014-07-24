/**
 * 
 */
package fr.wati.yacramanager.dao.specifications;

import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

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
	
	public static Specification<Absence> isValidated() {
		return new Specification<Absence>() {
			public Predicate toPredicate(Root<Absence> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.isTrue(root.get(Absence_.validated));
			}
		};
	}
	
	public static Specification<Absence> isNotValidated() {
		return new Specification<Absence>() {
			public Predicate toPredicate(Root<Absence> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.isFalse(root.get(Absence_.validated));
			}
		};
	}
	
	public static Specification<Absence> withTypeAbsences(final List<TypeAbsence> typeAbsences) {
		Specifications<Absence> specifications=null;
		for(TypeAbsence typeAbsence:typeAbsences){
			if(specifications==null){
				specifications=Specifications.where(withTypeAbsence(typeAbsence));
			}else {
				specifications=specifications.or(withTypeAbsence(typeAbsence));
			}
			
		}
		return specifications;
	}
	
	public static Specification<Absence> forEmploye(final Employe employe) {
		return new Specification<Absence>() {
			public Predicate toPredicate(Root<Absence> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.equal(root.get(Absence_.employe),employe);
			}
		};
	}
	
	public static Specification<Absence> forEmployes(final List<Employe> employes) {
		Specifications<Absence> specifications=null;
		for(Employe employe:employes){
			if(specifications==null){
				specifications=Specifications.where(forEmploye(employe));
			}else {
				specifications=specifications.or(forEmploye(employe));
			}
		}
		return specifications;
	}
}
