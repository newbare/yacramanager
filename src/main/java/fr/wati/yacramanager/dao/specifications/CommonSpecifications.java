package fr.wati.yacramanager.dao.specifications;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;

import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Company_;

public class CommonSpecifications {

	
	public static <T> Specification<T> globalSearch(final String searchTerm,final Class<T> entityClass,final Class<?> entityMetaModelClass){
		return new Specification<T>() {
			public Predicate toPredicate(Root<T> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				final List<Field> singularAttributesFields=new ArrayList<>();
				ReflectionUtils.doWithFields(entityMetaModelClass,new FieldCallback() {
					@Override
					public void doWith(Field field) throws IllegalArgumentException,
							IllegalAccessException {
						if(SingularAttribute.class.equals(field.getType()) && String.class.equals(((SingularAttribute)field.get(null)).getJavaType())){
							singularAttributesFields.add(field);
						}
					}
				});
				List<Predicate> predicates=new ArrayList<>();
				for (Field field : singularAttributesFields) {
					try {
						predicates.add(builder.like(builder.lower((Expression<String>) root.get((SingularAttribute<T, ?>)field.get(null))), "%" + searchTerm.toLowerCase() + "%"));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
				return builder.or(predicates.toArray(new Predicate[predicates.size()]));
			}
		};
	}
	
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
	
	public static <T> Specification<T> betweenDate(
			final DateTime start, final DateTime end,Class<T> entityClass,final String attributName) {
		return new Specification<T>() {
			public Predicate toPredicate(Root<T> root,
					CriteriaQuery<?> query, CriteriaBuilder builder) {
				return builder.between(root.<Date>get(attributName), start.toDate(), end.toDate());
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
