package fr.wati.yacramanager.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import fr.wati.yacramanager.beans.NoteDeFrais;
import fr.wati.yacramanager.beans.Personne;

public interface NoteDeFraisRepository extends JpaRepository<NoteDeFrais, Long> {

	Page<NoteDeFrais> findByDateBetween(Date dateDebut,Date dateFin,Pageable pageable);
	
	Page<NoteDeFrais> findByPersonneAndDateBetween(Personne personne,Date dateDebut,Date dateFin,Pageable pageable);
	
	List<NoteDeFrais> findByPersonneAndDateBetween(Personne personne,Date dateDebut,Date dateFin);
	
	Page<NoteDeFrais> findByPersonne(Personne personne,Pageable pageable);
}
