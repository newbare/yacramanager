package fr.wati.yacramanager.dao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.NoteDeFrais;
import fr.wati.yacramanager.beans.Personne;

public interface NoteDeFraisRepository extends JpaRepository<NoteDeFrais, Long> {

	Page<NoteDeFrais> findByDateBetween(Date dateDebut,Date dateFin,Pageable pageable);
	
	Page<NoteDeFrais> findByEmployeAndDateBetween(Employe employe,Date dateDebut,Date dateFin,Pageable pageable);
	
	List<NoteDeFrais> findByEmployeAndDateBetween(Employe employe,Date dateDebut,Date dateFin);
	
	Page<NoteDeFrais> findByEmploye(Personne personne,Pageable pageable);
}
