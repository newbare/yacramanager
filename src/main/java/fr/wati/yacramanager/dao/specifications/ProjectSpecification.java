/**
 * 
 */
package fr.wati.yacramanager.dao.specifications;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Client_;
import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Company_;
import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.beans.Project_;

/**
 * @author Rachid Ouattara
 *
 */
public class ProjectSpecification {

	public static Specification<Project> findForCompany(final Long companyId){
		return new Specification<Project>() {
			@Override
			public Predicate toPredicate(Root<Project> root,
					CriteriaQuery<?> query, CriteriaBuilder cb) {
				Join<Project, Client> projectClientJoin = root.join(Project_.client);
				Join<Client, Company> clientCompany = projectClientJoin.join(Client_.company);
				return cb.equal(clientCompany.get(Company_.id), companyId);
			}
		};
	}
}
