package fr.wati.yacramanager.services;

import java.util.List;

import fr.wati.yacramanager.beans.EmployesProjects;
import fr.wati.yacramanager.beans.EmployesProjectsId;
import fr.wati.yacramanager.web.dto.EmployesProjectsDTO;

public interface EmployesProjectsService extends CrudService<EmployesProjects, EmployesProjectsId>,SpecificationFactory<EmployesProjects>{

	EmployesProjectsDTO toEmployeesProjectsDTO(EmployesProjects employesProjects);

	List<EmployesProjectsDTO> toEmployeesProjectsDTOs(
			Iterable<EmployesProjects> findBySpecificationAndOrder);

}
