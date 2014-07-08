package fr.wati.yacramanager.services;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.wati.yacramanager.beans.NoteDeFrais;
import fr.wati.yacramanager.beans.Personne;
import fr.wati.yacramanager.web.dto.NoteDeFraisDTO;

public interface NoteDeFraisService extends CrudService<NoteDeFrais, Long>{
	
	Page<NoteDeFrais> findByDateBetween(Date dateDebut,Date dateFin,Pageable pageable);
	
	Page<NoteDeFrais> findByPersonneAndDateBetween(Personne personne,Date dateDebut,Date dateFin,Pageable pageable);
	
	List<NoteDeFrais> findByPersonneAndDateBetween(Personne personne,Date dateDebut,Date dateFin);
	
	Page<NoteDeFrais> findByPersonne(Personne personne,Pageable pageable);
	
	List<NoteDeFraisDTO> mapNoteDeFrais(Iterable<NoteDeFrais> noteDeFrais);
	
	NoteDeFraisDTO map(NoteDeFrais noteDeFrais);
	
}
