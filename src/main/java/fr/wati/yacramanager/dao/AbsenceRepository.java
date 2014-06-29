package fr.wati.yacramanager.dao;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import fr.wati.yacramanager.beans.Absence;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {

	Page<Absence> findByDateBetween(Date dateDebut,Date dateFin,Pageable pageable);
}
