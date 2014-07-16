package fr.wati.yacramanager.services;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.NoteDeFrais;
import fr.wati.yacramanager.web.dto.NoteDeFraisDTO;

public interface NoteDeFraisService extends CrudService<NoteDeFrais, Long>{
	
	Page<NoteDeFrais> findByDateBetween(Date dateDebut,Date dateFin,Pageable pageable);
	
	Page<NoteDeFrais> findByEmployeAndDateBetween(Employe employe,Date dateDebut,Date dateFin,Pageable pageable);
	
	List<NoteDeFrais> findByEmployeAndDateBetween(Employe employe,Date dateDebut,Date dateFin);
	
	Page<NoteDeFrais> findByEmploye(Employe employe,Pageable pageable);
	
	List<NoteDeFraisDTO> mapNoteDeFrais(Iterable<NoteDeFrais> noteDeFrais);
	
	NoteDeFraisDTO map(NoteDeFrais noteDeFrais);
	
}
