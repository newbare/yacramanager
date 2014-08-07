package fr.wati.yacramanager.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.beans.Attachement;
import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.NoteDeFrais;
import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.beans.Task;
import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.beans.WorkLog;
import fr.wati.yacramanager.dao.repository.EmployeDto;
import fr.wati.yacramanager.dao.repository.UserDto;
import fr.wati.yacramanager.web.dto.AbsenceDTO;
import fr.wati.yacramanager.web.dto.ClientDTO;
import fr.wati.yacramanager.web.dto.CompanyDTO;
import fr.wati.yacramanager.web.dto.NoteDeFraisDTO;
import fr.wati.yacramanager.web.dto.ProjectDTO;
import fr.wati.yacramanager.web.dto.TaskDTO;
import fr.wati.yacramanager.web.dto.UserInfoDTO.ManagedEmployeInfoDTO;
import fr.wati.yacramanager.web.dto.WorkLogDTO;

public class DtoMapper {

	public static UserDto map(Users user) {
		UserDto dto = new UserDto();
		dto.setId(Integer.valueOf(user.getId().toString()));
		dto.setUsername(user.getUsername());
		return dto;
	}

	public static List<UserDto> mapUsers(Page<Users> users) {
		List<UserDto> dtos = new ArrayList<UserDto>();
		for (Users user : users) {
			dtos.add(map(user));
		}
		return dtos;
	}
	
	
	public static ClientDTO map(Client client) {
		ClientDTO dto = new ClientDTO();
		dto.setId(client.getId());
		dto.setName(client.getName());
		return dto;
	}

	public static List<ClientDTO> mapClients(Page<Client> clients) {
		List<ClientDTO> dtos = new ArrayList<ClientDTO>();
		for (Client client : clients) {
			dtos.add(map(client));
		}
		return dtos;
	}
	
	public static ProjectDTO map(Project project) {
		ProjectDTO dto = new ProjectDTO();
		dto.setId(project.getId());
		dto.setName(project.getName());
		dto.setCreatedDate(project.getCreatedDate());
		dto.setDescription(project.getDescription());
		return dto;
	}

	public static List<ProjectDTO> mapProjects(Iterable<Project> projects) {
		List<ProjectDTO> dtos = new ArrayList<>();
		for (Project project : projects) {
			dtos.add(map(project));
		}
		return dtos;
	}
	

	public static AbsenceDTO map(Absence absence) {
		AbsenceDTO dto = new AbsenceDTO();
		dto.setDescription(absence.getDescription());
		dto.setStartDate(absence.getStartDate());
		dto.setEmployeId(absence.getEmploye().getId());
		dto.setEmployeName(absence.getEmploye().getFullName());
		dto.setEndDate(absence.getEndDate());
		dto.setTypeAbsence(String.valueOf(absence.getTypeAbsence()));
		dto.setDate(absence.getDate());
		dto.setId(absence.getId());
		dto.setValidated(absence.isValidated());
		return dto;
	}
	
	public static CompanyDTO map(Company company) {
		CompanyDTO dto = new CompanyDTO();
		dto.setId(company.getId());
		dto.setName(company.getName());
		dto.setLicenseEndDate(company.getLicenseEndDate());
		dto.setContacts(company.getContacts());
		dto.setRegisteredDate(company.getRegisteredDate());
		dto.setContacts(company.getContacts());
		return dto;
	}
	
	public static ManagedEmployeInfoDTO mapManagedEmployeInfoDTO(Employe employe) {
		ManagedEmployeInfoDTO dto = new ManagedEmployeInfoDTO();
		dto.setName(String.valueOf(employe.getId()));
		dto.setLabel(employe.getFullName());
		return dto;
	}
	
	
	public static NoteDeFraisDTO map(NoteDeFrais noteDeFrais) {
		NoteDeFraisDTO dto = new NoteDeFraisDTO();
		dto.setDate(noteDeFrais.getDate());
		dto.setDescription(noteDeFrais.getDescription());
		dto.setAmount(noteDeFrais.getAmount());
		dto.setEmployeId(noteDeFrais.getEmploye().getId());
		dto.setEmployeName(noteDeFrais.getEmploye().getFullName());
		dto.setId(noteDeFrais.getId());
		List<Long> attachementIds=new ArrayList<>();
		for(Attachement attachement: noteDeFrais.getAttachements()){
			attachementIds.add(attachement.getId());
		}
		dto.setAttachementsIds(attachementIds);
		return dto;
	}
	
	public static List<AbsenceDTO> mapAbsences(Iterable<Absence> absences) {
		List<AbsenceDTO> dtos = new ArrayList<AbsenceDTO>();
		for (Absence absence : absences) {
			dtos.add(map(absence));
		}
		return dtos;
	}
	
	public static List<CompanyDTO> mapCompanies(Iterable<Company> companies) {
		List<CompanyDTO> dtos = new ArrayList<CompanyDTO>();
		for (Company company : companies) {
			dtos.add(map(company));
		}
		return dtos;
	}
	
	public static List<ManagedEmployeInfoDTO> mapManagedEmployeInfoDTOs(Iterable<Employe> employees) {
		List<ManagedEmployeInfoDTO> dtos = new ArrayList<>();
		for (Employe employe : employees) {
			dtos.add(mapManagedEmployeInfoDTO(employe));
		}
		return dtos;
	}
	
	public static List<NoteDeFraisDTO> mapNoteDeFrais(Iterable<NoteDeFrais> noteDeFrais) {
		List<NoteDeFraisDTO> dtos = new ArrayList<NoteDeFraisDTO>();
		for (NoteDeFrais noteDeFrai : noteDeFrais) {
			dtos.add(map(noteDeFrai));
		}
		return dtos;
	}
	
	public static EmployeDto map(Employe employe) {
		EmployeDto dto = new EmployeDto();
		dto.setId(Long.valueOf(employe.getId().toString()));
		dto.setUsername(employe.getUsername());
		dto.setNom(employe.getNom());
		dto.setPrenom(employe.getPrenom());
		dto.setCivilite(employe.getCivilite());
		dto.setCodePostal(employe.getContact().getAdresse().getCodePostal());
		dto.setEmail(employe.getContact().getEmail());
		dto.setRue(employe.getContact().getAdresse().getRue());
		dto.setDateNaissance(employe.getDateNaissance());
		dto.setNumeroTelephone(employe.getContact().getNumeroTelephone());
		return dto;
	}

	public static List<EmployeDto> mapEmployees(Page<Employe> employees) {
		List<EmployeDto> dtos = new ArrayList<EmployeDto>();
		for (Employe employe : employees) {
			dtos.add(map(employe));
		}
		return dtos;
	}

	public static List<WorkLogDTO> mapWorkLogs(List<WorkLog> workLogs) {
		List<WorkLogDTO> dtos=new ArrayList<>();
		for (WorkLog workLog : workLogs) {
			dtos.add(map(workLog));
		}
		return dtos;
	}

	public static WorkLogDTO mapForDetails(WorkLog workLog) {
		return map(workLog);
	}

	public static WorkLogDTO map(WorkLog workLog) {
		WorkLogDTO workLogDTO=new WorkLogDTO();
		workLogDTO.setStart(workLog.getStartDate());
		workLogDTO.setEnd(workLog.getEndDate());
		workLogDTO.setId(workLog.getId());
		workLogDTO.setEditable(true);
		if(workLog.getTask()!=null){
			workLogDTO.setTitle(workLog.getTask().getName());
			workLogDTO.setColor(workLog.getTask().getColor());
		}
		workLogDTO.setType(String.valueOf(workLog.getWorkLogType()));
		switch (workLog.getWorkLogType()) {
		case DURATION:
			workLogDTO.setAllDay(true);
			workLogDTO.setDuration(workLog.getDuration());
			break;
		default:
			workLogDTO.setAllDay(false);
			break;
		}
		workLogDTO.setDescription(workLog.getDescription());
		
		return workLogDTO;
	}

	/**
	 * @param tasks
	 * @return
	 */
	public static List<TaskDTO> mapTasks(Iterable<Task> tasks) {
		List<TaskDTO> dtos = new ArrayList<>();
		for (Task task : tasks) {
			dtos.add(map(task));
		}
		return dtos;
	}

	/**
	 * @param task
	 * @return
	 */
	private static TaskDTO map(Task task) {
		TaskDTO dto=new TaskDTO();
		dto.setCreatedDate(task.getCreatedDate());
		dto.setDescription(task.getDescription());
		dto.setEmployeId(task.getEmploye().getId());
		dto.setId(task.getId());
		dto.setName(task.getName());
		dto.setTaskStatus(task.getTaskStatus());
		dto.setProjectId(task.getProject().getId());
		return dto;
	}
}
