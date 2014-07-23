package fr.wati.yacramanager.utils;

import java.util.Iterator;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;

import fr.wati.yacramanager.services.SpecificationFactory;

public class SpecificationBuilder {

	public  static <T> Specification<T> buildSpecification(List<Filter> filters,SpecificationFactory<T> specificationFactory){
		if(filters!=null && !filters.isEmpty()){
			Specifications<T> specifications=null;
			Iterator<Filter> filIteratoriterator = filters.iterator();
			while (filIteratoriterator.hasNext()) {
				Filter filter = (Filter) filIteratoriterator.next();
				if(specifications==null){
					//first clause = where
					specifications=Specifications.where(specificationFactory.buildSpecification(filter));
				}else {
					//AND
					specifications=specifications.and(specificationFactory.buildSpecification(filter));
				}
			}
			return specifications;
		}
		return null;
	}
}
