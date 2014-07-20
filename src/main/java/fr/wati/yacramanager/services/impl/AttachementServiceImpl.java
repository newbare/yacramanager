/**
 * 
 */
package fr.wati.yacramanager.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Attachement;
import fr.wati.yacramanager.dao.repository.AttachementRepository;
import fr.wati.yacramanager.services.AttachementService;

/**
 * @author Rachid Ouattara
 * 
 */
@Transactional
@Service
public class AttachementServiceImpl implements AttachementService {

	@Autowired
	private AttachementRepository attachementRepository;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.wati.yacramanager.services.AttachementService#addAttachement(fr.wati
	 * .yacramanager.beans.Attachement)
	 */
	@Override
	public Long addAttachement(Attachement attachement) {
		Attachement saveAttachement = attachementRepository.save(attachement);
		return saveAttachement.getId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.wati.yacramanager.services.AttachementService#findAttachementsByIds
	 * (java.lang.Integer[])
	 */
	@Override
	public List<Attachement> findAttachementsByIds(Long... ids) {
		return  attachementRepository.findByIdIn(ids);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * fr.wati.yacramanager.services.AttachementService#removeAttachement(fr
	 * .wati.yacramanager.beans.Attachement)
	 */
	@Override
	public void removeAttachement(Attachement attachement) {
		attachementRepository.delete(attachement);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.AttachementService#update(fr.wati.yacramanager.beans.Attachement)
	 */
	@Override
	public void update(Attachement attachement) {
		attachementRepository.save(attachement);
	}

	/* (non-Javadoc)
	 * @see fr.wati.yacramanager.services.AttachementService#getAttachementContent(java.lang.Long)
	 */
	@Override
	public byte[] getAttachementContent(Long id) {
		Attachement attachement=attachementRepository.findOne(id);
		byte[] content = attachement.getContent();
		return content;
	}

}
