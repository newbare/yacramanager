/**
 * 
 */
package fr.wati.yacramanager.dao.specifications;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.NoteDeFrais;
import fr.wati.yacramanager.beans.NoteDeFrais_;

/**
 * @author Rachid Ouattara
 *
 */
public class NoteDeFraisSpecifications {

	public static Specification<NoteDeFrais> descriptionLike(final String searchTerm) {
		return new Specification<NoteDeFrais>() {
			public Predicate toPredicate(Root<NoteDeFrais> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.like(builder.lower(root.get(NoteDeFrais_.description)), "%" + searchTerm.toLowerCase() + "%");
			}
		};
	}
	
	public static Specification<NoteDeFrais> createdDateBetween(
			final DateTime startRangeDate, final DateTime endRangeDate) {
		return new Specification<NoteDeFrais>() {
			public Predicate toPredicate(Root<NoteDeFrais> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.between(root.get(NoteDeFrais_.date),
						startRangeDate, endRangeDate);
			}
		};
	}
	
	public static Specification<NoteDeFrais> forEmploye(final Employe employe) {
		return new Specification<NoteDeFrais>() {
			public Predicate toPredicate(Root<NoteDeFrais> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.equal(root.get(NoteDeFrais_.employe),employe);
			}
		};
	}
	
	public static Specification<NoteDeFrais> forEmployes(final List<Employe> employes) {
		Specifications<NoteDeFrais> specifications=null;
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
