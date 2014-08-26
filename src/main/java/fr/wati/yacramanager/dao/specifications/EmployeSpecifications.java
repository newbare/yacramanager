package fr.wati.yacramanager.dao.specifications;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import fr.wati.yacramanager.beans.Gender;
import fr.wati.yacramanager.beans.Company;
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
	
	public static Specification<Employe> firstNamelike(final String searchTerm) {
		return new Specification<Employe>() {
			public Predicate toPredicate(Root<Employe> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.like(builder.lower(root.get(Employe_.firstName)), "%" + searchTerm.toLowerCase() + "%");
			}
		};
	}
	
	public static Specification<Employe> birthDayBetween(
			final DateTime startRangeDate, final DateTime endRangeDate) {
		return new Specification<Employe>() {
			public Predicate toPredicate(Root<Employe> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.between(root.get(Employe_.birthDay),
						startRangeDate, endRangeDate);
			}
		};
	}
	
	public static Specification<Employe> forCompany(final Company company) {
		return new Specification<Employe>() {
			public Predicate toPredicate(Root<Employe> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.equal(root.get(Employe_.company),company);
			}
		};
	}
	
	public static Specification<Employe> forCompanies(final List<Company> companies) {
		Specifications<Employe> specifications=null;
		for(Company company:companies){
			if(specifications==null){
				specifications=Specifications.where(forCompany(company));
			}else {
				specifications=specifications.or(forCompany(company));
			}
		}
		return specifications;
	}
	
	public static Specification<Employe> lastNamelike(final String searchTerm) {
		return new Specification<Employe>() {
			public Predicate toPredicate(Root<Employe> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.like(builder.lower(root.get(Employe_.lastName)), "%" + searchTerm.toLowerCase() + "%");
			}
		};
	}

	/**
	 * @param civilities
	 * @return
	 */
	public static Specification<Employe> withGenders(
			List<Gender> civilities) {
		Specifications<Employe> specifications=null;
		for(Gender civility:civilities){
			if(specifications==null){
				specifications=Specifications.where(withGender(civility));
			}else {
				specifications=specifications.or(withGender(civility));
			}
		}
		return specifications;
	}
	
	
	public static Specification<Employe> withGender(final Gender gender) {
		return new Specification<Employe>() {
			public Predicate toPredicate(Root<Employe> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.equal(root.get(Employe_.gender),gender);
			}
		};
	}
}
