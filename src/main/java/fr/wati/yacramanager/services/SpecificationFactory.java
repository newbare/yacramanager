package fr.wati.yacramanager.services;

import org.springframework.data.jpa.domain.Specification;

import fr.wati.yacramanager.utils.Filter;

public interface SpecificationFactory<T> {

	Specification<T> buildSpecification(Filter filter);
	
	Specification<T> getGlobalSpecification(String text);
}
