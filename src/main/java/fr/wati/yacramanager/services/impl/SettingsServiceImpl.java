package fr.wati.yacramanager.services.impl;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.wati.yacramanager.beans.Company;
import fr.wati.yacramanager.beans.Settings;
import fr.wati.yacramanager.beans.Users;
import fr.wati.yacramanager.dao.repository.CompanyRepository;
import fr.wati.yacramanager.dao.repository.SettingsRepository;
import fr.wati.yacramanager.dao.repository.UserRepository;
import fr.wati.yacramanager.services.SettingsService;

@Service("settingsService")
@Transactional
public class SettingsServiceImpl implements SettingsService {

	@Inject
	private SettingsRepository settingsRepository;
	
	@Inject
	private UserRepository usersRepository;
	
	@Inject
	private CompanyRepository companyRepository;

	private ApplicationEventPublisher applicationEventPublisher;
	
	@Override
	public <S extends Settings> S save(S entity) {
		return settingsRepository.save(entity);
	}

	@Override
	public <S extends Settings> Iterable<S> save(Iterable<S> entities) {
		return settingsRepository.save(entities);
	}

	@Override
	public Settings findOne(Long id) {
		return settingsRepository.findOne(id);
	}

	@Override
	public boolean exists(Long id) {
		return settingsRepository.exists(id);
	}

	@Override
	public Iterable<Settings> findAll() {
		return settingsRepository.findAll();
	}

	@Override
	public Iterable<Settings> findAll(Iterable<Long> ids) {
		return settingsRepository.findAll(ids);
	}

	@Override
	public long count() {
		return settingsRepository.count();
	}

	@Override
	public void delete(Long id) {
		settingsRepository.delete(id);
	}

	@Override
	public void delete(Settings entity) {
		settingsRepository.delete(entity);
	}

	@Override
	public void delete(Iterable<? extends Settings> entities) {
		settingsRepository.delete(entities);
	}

	@Override
	public void deleteAll() {
		settingsRepository.deleteAll();
	}

	@Override
	public Page<Settings> findAll(Specification<Settings> spec,
			Pageable pageable) {
		return settingsRepository.findAll(spec, pageable);
	}

	@Override
	public List<Settings> findByUser(Users users) {
		return settingsRepository.findSettingsByUser(users);
	}

	@Override
	public List<Settings> findByCompany(Company company) {
		return settingsRepository.findSettingsByCompany(company);
	}

	@Override
	public void addSetting(Users users, Settings settings) {
		Users findOne = usersRepository.findOne(users.getId());
		settings.setUser(findOne);
		settingsRepository.save(settings);
		findOne.getSettings().add(settings);
	}

	@Override
	public void addSetting(Company company, Settings settings) {
		Company findOne = companyRepository.findOne(company.getId());
		settings.setCompany(findOne);
		settingsRepository.save(settings);
		findOne.getSettings().add(settings);
	}

	@Override
	public void setApplicationEventPublisher(
			ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher=applicationEventPublisher;
	}
}
