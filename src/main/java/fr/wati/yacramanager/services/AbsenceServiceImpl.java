package fr.wati.yacramanager.services;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.dao.AbsenceRepository;

@Service
public class AbsenceServiceImpl implements AbsenceService {

	private AbsenceRepository absenceRepository;
	
	@Override
	public Absence save(Absence absence) {
		return absenceRepository.save(absence);
	}

	@Override
	public Page<Absence> findByStartDateBetween(Date dateDebut, Date dateFin,
			Pageable pageable) {
		return absenceRepository.findByDateBetween(dateDebut, dateFin, pageable);
	}

	@Override
	public void delete(Absence absence) {
		absenceRepository.delete(absence);
	}

	@Override
	public void validateAbsence(Absence absence) {
		// TODO Auto-generated method stub

	}

}
