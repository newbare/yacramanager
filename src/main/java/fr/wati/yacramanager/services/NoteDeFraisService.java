package fr.wati.yacramanager.services;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.NoteDeFrais;
import fr.wati.yacramanager.web.dto.NoteDeFraisDTO;

public interface NoteDeFraisService extends CrudService<NoteDeFrais, Long>,SpecificationFactory<NoteDeFrais>,StatusValidator<NoteDeFrais, Employe>{
	
	Page<NoteDeFrais> findByDateBetween(DateTime dateDebut,DateTime dateFin,Pageable pageable);
	
	Page<NoteDeFrais> findByEmployeAndDateBetween(Employe employe,DateTime dateDebut,DateTime dateFin,Pageable pageable);
	
	List<NoteDeFrais> findByEmployeAndDateBetween(Employe employe,DateTime dateDebut,DateTime dateFin);
	
	Page<NoteDeFrais> findByEmploye(Employe employe,Pageable pageable);
	
	List<NoteDeFraisDTO> mapNoteDeFrais(Iterable<NoteDeFrais> noteDeFrais);
	
	NoteDeFraisDTO map(NoteDeFrais noteDeFrais);
	
}
