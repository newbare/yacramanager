package fr.wati.yacramanager.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Attachement;
import fr.wati.yacramanager.beans.Employe;
import fr.wati.yacramanager.beans.NoteDeFrais;
import fr.wati.yacramanager.dao.repository.NoteDeFraisRepository;
import fr.wati.yacramanager.services.NoteDeFraisService;
import fr.wati.yacramanager.web.dto.NoteDeFraisDTO;

@Service
public class NoteDeFraisServiceImpl implements NoteDeFraisService {

	@Autowired
	private NoteDeFraisRepository noteDeFraisRepository;
	
	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#save(java.lang.Object)
	 */
	@Override
	public <S extends NoteDeFrais> S save(S entity) {
		return noteDeFraisRepository.save(entity);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#save(java.lang.Iterable)
	 */
	@Override
	public <S extends NoteDeFrais> Iterable<S> save(Iterable<S> entities) {
		return noteDeFraisRepository.save(entities);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#findOne(java.io.Serializable)
	 */
	@Override
	public NoteDeFrais findOne(Long id) {
		return noteDeFraisRepository.findOne(id);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#exists(java.io.Serializable)
	 */
	@Override
	public boolean exists(Long id) {
		return noteDeFraisRepository.exists(id);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#findAll()
	 */
	@Override
	public Iterable<NoteDeFrais> findAll() {
		return noteDeFraisRepository.findAll();
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#findAll(java.lang.Iterable)
	 */
	@Override
	public Iterable<NoteDeFrais> findAll(Iterable<Long> ids) {
		return noteDeFraisRepository.findAll(ids);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#count()
	 */
	@Override
	public long count() {
		return noteDeFraisRepository.count();
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#delete(java.io.Serializable)
	 */
	@Override
	public void delete(Long id) {
		noteDeFraisRepository.delete(id);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#delete(java.lang.Object)
	 */
	@Override
	public void delete(NoteDeFrais entity) {
		noteDeFraisRepository.delete(entity);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#delete(java.lang.Iterable)
	 */
	@Override
	public void delete(Iterable<? extends NoteDeFrais> entities) {
		noteDeFraisRepository.delete(entities);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#deleteAll()
	 */
	@Override
	public void deleteAll() {
		noteDeFraisRepository.deleteAll();
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.NoteDeFraisService#findByDateBetween(java.util.Date, java.util.Date, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<NoteDeFrais> findByDateBetween(Date dateDebut,
			Date dateFin, Pageable pageable) {
		return noteDeFraisRepository.findByDateBetween(dateDebut, dateFin, pageable);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.NoteDeFraisService#findByPersonneAndDateBetween(fr.wati.yacramanager.beans.Personne, java.util.Date, java.util.Date, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<NoteDeFrais> findByEmployeAndDateBetween(
			Employe employe, Date dateDebut, Date dateFin, Pageable pageable) {
		return noteDeFraisRepository.findByEmployeAndDateBetween(employe, dateDebut, dateFin, pageable);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.NoteDeFraisService#findByPersonneAndDateBetween(fr.wati.yacramanager.beans.Personne, java.util.Date, java.util.Date)
	 */
	@Override
	public List<NoteDeFrais> findByEmployeAndDateBetween(
			Employe employe, Date dateDebut, Date dateFin) {
		return noteDeFraisRepository.findByEmployeAndDateBetween(employe, dateDebut, dateFin);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.NoteDeFraisService#findByPersonne(fr.wati.yacramanager.beans.Personne, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<NoteDeFrais> findByEmploye(Employe employe, Pageable pageable) {
		return noteDeFraisRepository.findByEmploye(employe, pageable);
	}

	@Transactional
	public List<NoteDeFraisDTO> mapNoteDeFrais(Iterable<NoteDeFrais> noteDeFrais) {
		List<NoteDeFraisDTO> dtos = new ArrayList<NoteDeFraisDTO>();
		for (NoteDeFrais noteDeFrai : noteDeFrais) {
			dtos.add(map(noteDeFrai));
		}
		return dtos;
	}
	
	@Transactional
	public NoteDeFraisDTO map(NoteDeFrais noteDeFrais) {
		NoteDeFrais findOne = noteDeFraisRepository.findOne(noteDeFrais.getId());
		NoteDeFraisDTO dto = new NoteDeFraisDTO();
		dto.setDate(findOne.getDate());
		dto.setDescription(findOne.getDescription());
		dto.setAmount(findOne.getAmount());
		dto.setId(findOne.getId());
		List<Long> attachementIds=new ArrayList<>();
		for(Attachement attachement: findOne.getAttachements()){
			attachementIds.add(attachement.getId());
		}
		dto.setAttachementsIds(attachementIds);
		return dto;
	}
	
	

}
