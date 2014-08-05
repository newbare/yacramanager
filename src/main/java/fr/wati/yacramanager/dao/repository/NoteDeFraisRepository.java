package fr.wati.yacramanager.dao.repository;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.NoteDeFrais;
import fr.wati.yacramanager.beans.Personne;

public interface NoteDeFraisRepository extends JpaRepository<NoteDeFrais, Long>, JpaSpecificationExecutor<NoteDeFrais> {

	Page<NoteDeFrais> findByDateBetween(DateTime  dateDebut,DateTime dateFin,Pageable pageable);
	
	Page<NoteDeFrais> findByEmployeAndDateBetween(Employe employe,DateTime dateDebut,DateTime dateFin,Pageable pageable);
	
	List<NoteDeFrais> findByEmployeAndDateBetween(Employe employe,DateTime dateDebut,DateTime dateFin);
	
	Page<NoteDeFrais> findByEmploye(Personne personne,Pageable pageable);
}
