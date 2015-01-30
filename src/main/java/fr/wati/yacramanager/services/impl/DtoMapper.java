package fr.wati.yacramanager.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.beans.Attachement;
import fr.wati.yacramanager.beans.Client;
import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Contact;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.NoteDeFrais;
import fr.wati.yacramanager.beans.Project;
import fr.wati.yacramanager.beans.Settings;
import fr.wati.yacramanager.beans.Task;
import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.beans.ValidationStatus;
import fr.wati.yacramanager.beans.WorkLog;
import fr.wati.yacramanager.dao.repository.EmployeDto;
import fr.wati.yacramanager.dao.repository.UserDto;
import fr.wati.yacramanager.services.ClientService;
import fr.wati.yacramanager.services.CompanyService;
import fr.wati.yacramanager.services.ProjectService;
import fr.wati.yacramanager.web.dto.AbsenceDTO;
import fr.wati.yacramanager.web.dto.ClientDTO;
import fr.wati.yacramanager.web.dto.CompanyDTO;
import fr.wati.yacramanager.web.dto.ContactDTO;
import fr.wati.yacramanager.web.dto.NoteDeFraisDTO;
import fr.wati.yacramanager.web.dto.ProjectDTO;
import fr.wati.yacramanager.web.dto.SettingsDTO;
import fr.wati.yacramanager.web.dto.TaskDTO;
import fr.wati.yacramanager.web.dto.UserInfoDTO.ManagedEmployeInfoDTO;
import fr.wati.yacramanager.web.dto.WorkLogDTO;


@Transactional
@Service
public class DtoMapper {

	@Autowired
	private ClientService clientService;
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private ProjectService projectService;
	
	public  UserDto map(Users user) {
		UserDto dto = new UserDto();
		dto.setId(Integer.valueOf(user.getId().toString()));
		dto.setUsername(user.getUsername());
		return dto;
	}
	
	

	public  List<UserDto> mapUsers(Page<Users> users) {
		List<UserDto> dtos = new ArrayList<UserDto>();
		for (Users user : users) {
			dtos.add(map(user));
		}
		return dtos;
	}
	
	
	public  ClientDTO map(Client client) {
		ClientDTO dto = new ClientDTO();
		dto.setId(client.getId());
		dto.setName(client.getName());
		dto.setContacts(mapContacts(client));
		return dto;
	}
	
	public  ContactDTO map(Contact contact) {
		ContactDTO dto = new ContactDTO();
		dto.setId(contact.getId());
		dto.setName(contact.getName());
		dto.setAdresse(contact.getAdresse());
		dto.setEmail(contact.getEmail());
		dto.setPhoneNumbers(contact.getPhoneNumbers());
		return dto;
	}
	@Transactional(readOnly=true)
	public  List<ContactDTO> mapContacts(Iterable<Contact> contacts) {
		List<ContactDTO> dtos = new ArrayList<>();
		for (Contact contact : contacts) {
			dtos.add(map(contact));
		}
		return dtos;
	}
	
	@Transactional(readOnly=true)
	public  List<ContactDTO> mapContacts(Client client) {
		Client findOne = clientService.findOne(client.getId());
		List<ContactDTO> dtos = new ArrayList<>();
		for (Contact contact : findOne.getContacts()) {
			dtos.add(map(contact));
		}
		return dtos;
	}
	
	@Transactional(readOnly=true)
	public  List<ProjectDTO> mapProjects(Client client) {
		Client findOne = clientService.findOne(client.getId());
		List<ProjectDTO> dtos = new ArrayList<>();
		for (Project project : findOne.getProjects()) {
			dtos.add(map(project));
		}
		return dtos;
	}
	
	@Transactional(readOnly=true)
	public  List<ContactDTO> mapContacts(Company company) {
		Company findOne = companyService.findOne(company.getId());
		List<ContactDTO> dtos = new ArrayList<>();
		for (Contact contact : findOne.getContacts()) {
			dtos.add(map(contact));
		}
		return dtos;
	}

	public  List<ClientDTO> mapClients(Page<Client> clients) {
		List<ClientDTO> dtos = new ArrayList<ClientDTO>();
		for (Client client : clients) {
			dtos.add(map(client));
		}
		return dtos;
	}
	@Transactional
	public  ProjectDTO map(Project project) {
		ProjectDTO dto = new ProjectDTO();
		dto.setId(project.getId());
		dto.setName(project.getName());
		dto.setCreatedDate(project.getCreatedDate());
		dto.setDescription(project.getDescription());
		dto.setColor(project.getColor());
//		if(project.getClient()!=null){
//			ClientDTO clientDTO=map(project.getClient());
//			dto.setClient(clientDTO);
//		}
		dto.setNumberOfEmployes(projectService.findOne(project.getId()).getAssignedEmployees().size());
		dto.setNumberOfTasks(projectService.findOne(project.getId()).getTasks().size());
		return dto;
	}

	public  List<ProjectDTO> mapProjects(Iterable<Project> projects) {
		List<ProjectDTO> dtos = new ArrayList<>();
		for (Project project : projects) {
			dtos.add(map(project));
		}
		return dtos;
	}
	
	public  SettingsDTO map(Settings settings) {
		SettingsDTO dto = new SettingsDTO();
		dto.setId(settings.getId());
		dto.setKey(settings.getKey());
		dto.setValue(settings.getValue());
		dto.setDescription(settings.getDescription());
		return dto;
	}
	
	public  List<SettingsDTO> mapSettings(Iterable<Settings> settings) {
		List<SettingsDTO> dtos = new ArrayList<>();
		for (Settings setting : settings) {
			dtos.add(map(setting));
		}
		return dtos;
	}
	

	public  AbsenceDTO map(Absence absence) {
		AbsenceDTO dto = new AbsenceDTO();
		dto.setDescription(absence.getDescription());
		dto.setStartDate(absence.getStartDate());
		dto.setEmployeId(absence.getEmploye().getId());
		dto.setEmployeName(absence.getEmploye().getFullName());
		dto.setEndDate(absence.getEndDate());
		dto.setTypeAbsence(String.valueOf(absence.getTypeAbsence()));
		dto.setDate(absence.getDate());
		dto.setId(absence.getId());
		dto.setValidationStatus(absence.getValidationStatus());
		return dto;
	}
	
	public  CompanyDTO map(Company company) {
		CompanyDTO dto = new CompanyDTO();
		dto.setId(company.getId());
		dto.setName(company.getName());
		dto.setLicenseEndDate(company.getLicenseEndDate());
		dto.setContacts(mapContacts(company));
		dto.setRegisteredDate(company.getRegisteredDate());
		return dto;
	}
	
	public  ManagedEmployeInfoDTO mapManagedEmployeInfoDTO(Employe employe) {
		ManagedEmployeInfoDTO dto = new ManagedEmployeInfoDTO();
		dto.setName(String.valueOf(employe.getId()));
		dto.setLabel(employe.getFullName());
		return dto;
	}
	
	
	public  NoteDeFraisDTO map(NoteDeFrais noteDeFrais) {
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
	
	public  List<AbsenceDTO> mapAbsences(Iterable<Absence> absences) {
		List<AbsenceDTO> dtos = new ArrayList<AbsenceDTO>();
		for (Absence absence : absences) {
			dtos.add(map(absence));
		}
		return dtos;
	}
	
	public  List<CompanyDTO> mapCompanies(Iterable<Company> companies) {
		List<CompanyDTO> dtos = new ArrayList<CompanyDTO>();
		for (Company company : companies) {
			dtos.add(map(company));
		}
		return dtos;
	}
	
	public  List<ManagedEmployeInfoDTO> mapManagedEmployeInfoDTOs(Iterable<Employe> employees) {
		List<ManagedEmployeInfoDTO> dtos = new ArrayList<>();
		for (Employe employe : employees) {
			dtos.add(mapManagedEmployeInfoDTO(employe));
		}
		return dtos;
	}
	
	public  List<NoteDeFraisDTO> mapNoteDeFrais(Iterable<NoteDeFrais> noteDeFrais) {
		List<NoteDeFraisDTO> dtos = new ArrayList<NoteDeFraisDTO>();
		for (NoteDeFrais noteDeFrai : noteDeFrais) {
			dtos.add(map(noteDeFrai));
		}
		return dtos;
	}
	
	public  EmployeDto map(Employe employe) {
		EmployeDto dto = new EmployeDto();
		dto.setId(Long.valueOf(employe.getId().toString()));
		dto.setUsername(employe.getUsername());
		dto.setLastName(employe.getLastName());
		dto.setFirstName(employe.getFirstName());
		dto.setGender(employe.getGender());
		dto.setPostCode(employe.getContact().getAdresse().getPostCode());
		dto.setEmail(employe.getContact().getEmail());
		dto.setAdress(employe.getContact().getAdresse().getAdress());
		dto.setBirthDay(employe.getBirthDay());
		dto.setPhoneNumbers(employe.getContact().getPhoneNumbers());
		return dto;
	}

	public  List<EmployeDto> mapEmployees(Page<Employe> employees) {
		List<EmployeDto> dtos = new ArrayList<EmployeDto>();
		for (Employe employe : employees) {
			dtos.add(map(employe));
		}
		return dtos;
	}

	public  List<WorkLogDTO> mapWorkLogs(Iterable<WorkLog> workLogs) {
		List<WorkLogDTO> dtos=new ArrayList<>();
		for (WorkLog workLog : workLogs) {
			dtos.add(map(workLog));
		}
		return dtos;
	}

	public  WorkLogDTO mapForDetails(WorkLog workLog) {
		return map(workLog);
	}

	public  WorkLogDTO map(WorkLog workLog) {
		WorkLogDTO workLogDTO=new WorkLogDTO();
		workLogDTO.setStart(workLog.getStartDate());
		workLogDTO.setEnd(workLog.getEndDate());
		workLogDTO.setId(workLog.getId());
		workLogDTO.setEditable(ValidationStatus.APPROVED!= workLog.getValidationStatus());
		workLogDTO.setValidationStatus(workLog.getValidationStatus());
		if(workLog.getTask()!=null){
			workLogDTO.setTaskName(workLog.getTask().getName());
			workLogDTO.setTitle(workLog.getTask().getName());
			workLogDTO.setProjectName(workLog.getTask().getProject().getName());
			workLogDTO.setClientName(workLog.getTask().getProject().getClient().getName());
			workLogDTO.setColor(workLog.getTask().getColor() != null ? workLog
					.getTask().getColor() : workLog.getTask().getProject()
					.getColor());
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
		workLogDTO.setExtraTime(workLog.isExtraTime());
		
		return workLogDTO;
	}

	/**
	 * @param tasks
	 * @return
	 */
	public  List<TaskDTO> mapTasks(Iterable<Task> tasks) {
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
	public  TaskDTO map(Task task) {
		TaskDTO dto=new TaskDTO();
		dto.setCreatedDate(task.getCreatedDate());
		dto.setDescription(task.getDescription());
//		dto.setEmployeId(task.getEmploye().getId());
		dto.setId(task.getId());
		dto.setName(task.getName());
		dto.setTaskStatus(task.getTaskStatus());
		dto.setProjectId(task.getProject().getId());
		return dto;
	}
}
