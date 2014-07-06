package fr.wati.yacramanager.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.beans.NoteDeFrais;
import fr.wati.yacramanager.beans.Personne;
import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.dao.PersonneDto;
import fr.wati.yacramanager.dao.UserDto;
import fr.wati.yacramanager.web.dto.AbsenceDTO;
import fr.wati.yacramanager.web.dto.NoteDeFraisDTO;

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
		dto.setDescription(absence.getDescription());
		dto.setStartDate(absence.getStartDate());
		dto.setEndDate(absence.getEndDate());
		dto.setType(String.valueOf(absence.getTypeAbsence()));
		dto.setPostedDate(absence.getDate());
		dto.setId(absence.getId());
		dto.setValidated(absence.isValidated());
		return dto;
	}
	
	public static NoteDeFraisDTO map(NoteDeFrais noteDeFrais) {
		NoteDeFraisDTO dto = new NoteDeFraisDTO();
		dto.setDate(noteDeFrais.getDate());
		dto.setDescription(noteDeFrais.getDescription());
		dto.setAmount(noteDeFrais.getAmount());
		dto.setId(noteDeFrais.getId());
		return dto;
	}
	
	public static List<AbsenceDTO> mapAbsences(Iterable<Absence> absences) {
		List<AbsenceDTO> dtos = new ArrayList<AbsenceDTO>();
		for (Absence absence : absences) {
			dtos.add(map(absence));
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
