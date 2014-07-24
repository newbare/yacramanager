package fr.wati.yacramanager.dao.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.Employe_;

public class EmployeSpecifications {

	
	public static Specification<Employe> hasManager(final Employe employe) {
		return new Specification<Employe>() {
			public Predicate toPredicate(Root<Employe> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.equal(root.get(Employe_.manager),employe);
			}
		};
	}
}
