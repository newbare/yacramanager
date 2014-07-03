package fr.wati.yacramanager.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.beans.Personne;
import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.dao.PersonneDto;
import fr.wati.yacramanager.dao.UserDto;
import fr.wati.yacramanager.web.dto.AbsenceDTO;

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

	public static AbsenceDTO map(Absence absence) {
		AbsenceDTO dto = new AbsenceDTO();
		return dto;
	}

	public static List<AbsenceDTO> mapAbsences(Page<Absence> absences) {
		List<AbsenceDTO> dtos = new ArrayList<AbsenceDTO>();
		for (Absence absence : absences) {
			dtos.add(map(absence));
		}
		return dtos;
	}
	
	public static PersonneDto map(Personne personne) {
		PersonneDto dto = new PersonneDto();
		dto.setId(Long.valueOf(personne.getId().toString()));
		dto.setUsername(personne.getUsername());
		dto.setPassword(personne.getPassword());
		dto.setNom(personne.getNom());
		dto.setPrenom(personne.getPrenom());
		dto.setCivilite(personne.getCivilite());
		dto.setCodePostal(personne.getContact().getAdresse().getCodePostal());
		dto.setEmail(personne.getContact().getEmail());
		dto.setRue(personne.getContact().getAdresse().getRue());
		dto.setDateNaissance(personne.getDateNaissance());
		dto.setNumeroTelephone(personne.getContact().getNumeroTelephone());
		return dto;
	}

	public static List<PersonneDto> mapPersonne(Page<Personne> personnes) {
		List<PersonneDto> dtos = new ArrayList<PersonneDto>();
		for (Personne personne : personnes) {
			dtos.add(map(personne));
		}
		return dtos;
	}
}
