/**
 * 
 */
package fr.wati.yacramanager.dao.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Company_;

/**
 * @author Rachid Ouattara
 *
 */
public class CompanySpecifications {

	public static Specification<Company> namelike(final String searchTerm) {
	    return new Specification<Company>() {
	      public Predicate toPredicate(Root<Company> root, CriteriaQuery<?> query,
	            CriteriaBuilder builder) {
	         return builder.like(root.get(Company_.name), "%"+searchTerm+"%");
	      }
	    };
	  }
}
