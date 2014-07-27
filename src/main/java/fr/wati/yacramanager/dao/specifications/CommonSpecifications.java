package fr.wati.yacramanager.dao.specifications;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

public class CommonSpecifications {

	
	public static <T> Specification<T> likeIgnoreCase(final String searchTerm,final SingularAttribute<T, String> attribut) {
		return new Specification<T>() {
			public Predicate toPredicate(Root<T> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.like(builder.lower(root.get(attribut)), "%" + searchTerm.toLowerCase() + "%");
			}
		};
	}
	
	public static <T> Specification<T> like(final String searchTerm,final SingularAttribute<T, String> attribut) {
		return new Specification<T>() {
			public Predicate toPredicate(Root<T> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.like(root.get(attribut), "%" + searchTerm + "%");
			}
		};
	}
	
	public static <T,ATTRIBUTE_TYPE extends Comparable<ATTRIBUTE_TYPE>> Specification<T> greaterThan(final ATTRIBUTE_TYPE value,final SingularAttribute<T, ATTRIBUTE_TYPE> attribut) {
		return new Specification<T>() {
			public Predicate toPredicate(Root<T> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.greaterThanOrEqualTo (root.get(attribut), value); 
			}
		};
	}
	
	public static <T,ATTRIBUTE_TYPE extends Comparable<ATTRIBUTE_TYPE>> Specification<T> lessThan(final ATTRIBUTE_TYPE value,final SingularAttribute<T, ATTRIBUTE_TYPE> attribut) {
		return new Specification<T>() {
			public Predicate toPredicate(Root<T> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.lessThanOrEqualTo (root.get(attribut), value); 
			}
		};
	}
	
	public static <T,ATTRIBUTE_TYPE extends Comparable<ATTRIBUTE_TYPE>> Specification<T> between(
			final ATTRIBUTE_TYPE start, final ATTRIBUTE_TYPE end,final SingularAttribute<T, ATTRIBUTE_TYPE> attribut) {
		return new Specification<T>() {
			public Predicate toPredicate(Root<T> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.between(root.get(attribut),
						start, end);
			}
		};
	}
	
	public static <T,E> Specification<T> isMember(final E element,final ListAttribute<T, E> attribut) {
		return new Specification<T>() {
			public Predicate toPredicate(Root<T> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.isMember(element, root.get(attribut));
			}
		};
	}
	
	
	public static <T> Specification<T> isTrue(final SingularAttribute<T, Boolean> attribut) {
		return new Specification<T>() {
			public Predicate toPredicate(Root<T> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.isTrue(root.get(attribut));
			}
		};
	}
	
	public static <T> Specification<T> isFalse(final SingularAttribute<T, Boolean> attribut) {
		return new Specification<T>() {
			public Predicate toPredicate(Root<T> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.isFalse(root.get(attribut));
			}
		};
	}
	
	public static <T,E> Specification<T> equals(final E element,final SingularAttribute<T, E> attribut) {
		return new Specification<T>() {
			public Predicate toPredicate(Root<T> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.equal(root.get(attribut),element);
			}
		};
	}
	
	public static <T,E> Specification<T> equalsAny(final List<E> elements,final SingularAttribute<T, E> attribut) {
		Specifications<T> specifications=null;
		for(E element:elements){
			if(specifications==null){
				specifications=Specifications.where(equals(element,attribut));
			}else {
				specifications=specifications.or(equals(element,attribut));
			}
		}
		return specifications;
	}
}