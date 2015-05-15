/**
 * 
 */
package fr.wati.yacramanager.services.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.AbsencePortfolio;
import fr.wati.yacramanager.beans.AbsencePortfolio.AbsencePortfolioPK;
import fr.wati.yacramanager.dao.repository.AbsencePortfolioRepository;
import fr.wati.yacramanager.services.AbsencePortfolioService;
import fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsence;

/**
 * @author Rachid Ouattara
 *
 */
@Transactional
@Service("absencePortfolioService")
public class AbsencePortfolioServiceImpl implements AbsencePortfolioService {

	@Inject
	private AbsencePortfolioRepository absencePortfolioRepository;
	@Inject
	private ApplicationEventPublisher applicationEventPublisher;
	
	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#save(java.lang.Object)
	 */
	@Override
	public <S extends AbsencePortfolio> S save(S entity) {
		return absencePortfolioRepository.save(entity);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#save(java.lang.Iterable)
	 */
	@Override
	public <S extends AbsencePortfolio> Iterable<S> save(Iterable<S> entities) {
		return absencePortfolioRepository.save(entities);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#findOne(java.io.Serializable)
	 */
	@Override
	public AbsencePortfolio findOne(AbsencePortfolioPK absencePortfolioPK) {
		return absencePortfolioRepository.findOne(absencePortfolioPK);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#exists(java.io.Serializable)
	 */
	@Override
	public boolean exists(AbsencePortfolioPK id) {
		return absencePortfolioRepository.exists(id);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#findAll()
	 */
	@Override
	public Iterable<AbsencePortfolio> findAll() {
		return absencePortfolioRepository.findAll();
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#findAll(java.lang.Iterable)
	 */
	@Override
	public Iterable<AbsencePortfolio> findAll(Iterable<AbsencePortfolioPK> ids) {
		return absencePortfolioRepository.findAll(ids);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#count()
	 */
	@Override
	public long count() {
		return absencePortfolioRepository.count();
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#delete(java.io.Serializable)
	 */
	@Override
	public void delete(AbsencePortfolioPK id) {
		absencePortfolioRepository.delete(id);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#delete(java.lang.Object)
	 */
	@Override
	public void delete(AbsencePortfolio entity) {
		absencePortfolioRepository.delete(entity);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#delete(java.lang.Iterable)
	 */
	@Override
	public void delete(Iterable<? extends AbsencePortfolio> entities) {
		absencePortfolioRepository.delete(entities);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#deleteAll()
	 */
	@Override
	public void deleteAll() {
		absencePortfolioRepository.deleteAll();
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.CrudService#findAll(org.springframework.data.jpa.domain.Specification, org.springframework.data.domain.Pageable)
	 */
	@Override
	public Page<AbsencePortfolio> findAll(Specification<AbsencePortfolio> spec,
			Pageable pageable) {
		return absencePortfolioRepository.findAll(spec, pageable);
	}

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationEventPublisherAware#setApplicationEventPublisher(org.springframework.context.ApplicationEventPublisher)
	 */
	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher=applicationEventPublisher;
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.AbsencePortfolioService#findByUserAndType(java.lang.Long, fr.wati.yacramanager.web.dto.AbsenceDTO.TypeAbsence)
	 */
	@Override
	public AbsencePortfolio findByUserAndType(Long userId,
			TypeAbsence typeAbsence) {
		return absencePortfolioRepository.findByUserAndType(userId, typeAbsence);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.AbsencePortfolioService#findByUser(java.lang.Long)
	 */
	@Override
	public List<AbsencePortfolio> findByUser(Long userId) {
		return absencePortfolioRepository.findByUser(userId);
	}

	@Override
	public void initAbsencePortfolioForEmploye(Long employeId) {
		for(TypeAbsence typeAbsence:TypeAbsence.values()){
			AbsencePortfolio absencePortfolio = findByUserAndType(employeId, typeAbsence);
			if(absencePortfolio==null){
				absencePortfolio=new AbsencePortfolio();
				absencePortfolio.setAbsencePortfolioPK(new AbsencePortfolioPK(employeId, typeAbsence));
				save(absencePortfolio);
			}
		}
	}

}
