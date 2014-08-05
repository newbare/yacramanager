/**
 * 
 */
package fr.wati.yacramanager.dao.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;

import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Company_;
import fr.wati.yacramanager.beans.Employe;

/**
 * @author Rachid Ouattara
 * 
 */
public class CompanySpecifications {

	public static Specification<Company> namelike(final String searchTerm) {
		return new Specification<Company>() {
			public Predicate toPredicate(Root<Company> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.like(builder.lower(root.get(Company_.name)), "%" + searchTerm.toLowerCase() + "%");
			}
		};
	}

	public static Specification<Company> registeredDateBetween(
			final DateTime startRangeDate, final DateTime endRangeDate) {
		return new Specification<Company>() {
			public Predicate toPredicate(Root<Company> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.between(root.get(Company_.registeredDate),
						startRangeDate, endRangeDate);
			}
		};
	}

	public static Specification<Company> employeMemberOfCompany(final Employe employe) {
		return new Specification<Company>() {
			public Predicate toPredicate(Root<Company> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.isMember(employe, root.get(Company_.employes));
			}
		};
	}
}
