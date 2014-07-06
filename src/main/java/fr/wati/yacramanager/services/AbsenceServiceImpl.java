package fr.wati.yacramanager.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import fr.wati.yacramanager.beans.Absence;
import fr.wati.yacramanager.beans.Personne;
import fr.wati.yacramanager.dao.AbsenceRepository;

@Service
public class AbsenceServiceImpl implements AbsenceService {

	@Autowired
	private AbsenceRepository absenceRepository;

	@Override
	public Page<Absence> findByStartDateBetween(Date dateDebut, Date dateFin,
			Pageable pageable) {
		return absenceRepository.findByDateBetween(dateDebut, dateFin, pageable);
	}


	@Override
	public void validateAbsence(Absence absence) {
		// TODO Auto-generated method stub

	}


	@Override
	public <S extends Absence> S save(S entity) {
		return absenceRepository.save(entity);
	}


	@Override
	public <S extends Absence> Iterable<S> save(Iterable<S> entities) {
		return absenceRepository.save(entities);
	}


	@Override
	public Absence findOne(Long id) {
		return absenceRepository.findOne(id);
	}


	@Override
	public boolean exists(Long id) {
		return absenceRepository.exists(id);
	}


	@Override
	public Iterable<Absence> findAll() {
		return absenceRepository.findAll();
	}


	@Override
	public Iterable<Absence> findAll(Iterable<Long> ids) {
		return absenceRepository.findAll(ids);
	}


	@Override
	public long count() {
		return absenceRepository.count();
	}


	@Override
	public void delete(Long id) {
		absenceRepository.delete(id);
	}


	@Override
	public void delete(Absence entity) {
		absenceRepository.delete(entity);
	}


	@Override
	public void delete(Iterable<? extends Absence> entities) {
		absenceRepository.delete(entities);
	}


	@Override
	public void deleteAll() {
		absenceRepository.deleteAll();
	}


	@Override
	public Page<Absence> findByPersonneAndStartDateBetween(Personne personne,
			Date dateDebut, Date dateFin, Pageable pageable) {
		return absenceRepository.findByPersonneAndStartDateBetween(personne, dateDebut, dateFin, pageable);
	}


	@Override
	public Page<Absence> findByPersonne(Personne personne, Pageable pageable) {
		return absenceRepository.findByPersonne(personne, pageable);
	}


	@Override
	public List<Absence> findByPersonneAndStartDateBetween(Personne personne,
			Date dateDebut, Date dateFin) {
		return absenceRepository.findByPersonneAndStartDateBetween(personne, dateDebut, dateFin);
	}

}
